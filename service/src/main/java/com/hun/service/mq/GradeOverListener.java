package com.hun.service.mq;

import com.hun.bean.entity.base.Grade;
import com.hun.bean.entity.base.Student;
import com.hun.bean.entity.expand.StudentSchedule;
import com.hun.bean.enums.ChargeMethodCategory;
import com.hun.common.constants.MQ;
import com.hun.common.util.RedisUtil;
import com.hun.service.service.base.GradeService;
import com.hun.service.service.base.ScheduleService;
import com.hun.service.service.base.StudentService;
import com.hun.service.service.expand.GradeInfoService;
import com.hun.service.service.expand.StudentScheduleService;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@RabbitListener(queues = MQ.END_GRADE_QUEUE)
public class GradeOverListener {
    @Resource
    private GradeInfoService gradeInfoService;
    @Resource
    private GradeService gradeService;
    @Resource
    private ScheduleService scheduleService;
    @Resource
    private StudentService studentService;

    @Resource
    private StudentScheduleService studentScheduleService;

    @Resource
    private TransactionTemplate transactionTemplate;


    /**
     * 下课
     */
    @RabbitHandler
    public void gradeOver(Map<String, Object> map) {
        //不需要防止发送两次下课的消息, 因为发送的时候已经上锁了,禁止发送第二次
        //使用redis锁住该班级, 性能不足情况下用mq发别的机器上跑。

        //消息队列中获得班级id和排课id和排课课时
        Long gradeId = (Long) map.get("gradeId");
        Long scheduleId = (Long) map.get("scheduleId");


        //获得排课课时
        Integer schedulePeriod = (Integer) map.get("schedulePeriod");
        //获得班级
        Grade grade = gradeService.getOneGradeByGradeId(gradeId);
        //获得收费方式id
        Long chargeMethodId = grade.getChargeMethodId();
        //获得班级总课时
        Integer totalPeriod = grade.getGradeTotalPeriod();

        //获得该课学生
        List<StudentSchedule> studentSchedules = studentScheduleService.getByScheduleId(scheduleId);

        List<Long> attendStudentIds = new ArrayList<>();
        List<Long> missStudentIds = new ArrayList<>();

        //计算出勤和缺勤的学生
        for (StudentSchedule studentSchedule : studentSchedules) {
            if (studentSchedule.getMissReason() == null) {
                attendStudentIds.add(studentSchedule.getStudentId());
            } else {
                missStudentIds.add(studentSchedule.getStudentId());
            }
        }
        //查出出勤学生的信息
        List<Student> students = studentService.selectBatchIds(attendStudentIds);

        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(@NonNull TransactionStatus status) {
                //班级排课改为已上状态
                scheduleService.scheduleEnd(scheduleId);
                //将学生排课改为已上状态，若miss_reason != null 则缺勤
                studentScheduleService.endNotMiss(scheduleId);
                studentScheduleService.endIsMiss(scheduleId);
                //出勤学生出勤课时增加
                gradeInfoService.batchPlusConsumePeriod(gradeId, schedulePeriod, attendStudentIds);
                if (missStudentIds.size() > 0) {
                    //缺勤学生的缺勤课时增加
                    gradeInfoService.batchPlusMissPeriod(gradeId, schedulePeriod, missStudentIds);
                }
                //班级的已上课时增加
                gradeService.plusNowPeriod(gradeId, schedulePeriod);

                //按课时消费
                if (ChargeMethodCategory.isPeriod(chargeMethodId)) {
                    //获得该课的收费,该课消费=课时*收费表达式
                    BigDecimal schedulePrice = grade.getChargeMethodExpression().multiply(BigDecimal.valueOf(schedulePeriod));
                    //出勤的学生锁定余额,总余额减少p,并且记录消费记录
                    studentService.batchConsumeAndRecord(grade, schedulePrice, students,
                            attendStudentIds, "课时出勤结算，本次结算" + schedulePeriod + "课时");
                    //增加总收入, 总收入 = 该课消费 * 出勤人数
                    gradeService.income(grade.getGradeId(),
                            schedulePrice.multiply(new BigDecimal(attendStudentIds.size())));
                    //设置收入
                    studentScheduleService.incomeNotMiss(scheduleId, schedulePrice);
                }
                //已上课时=总课时
                if (totalPeriod.equals(grade.getGradeNowPeriod() + schedulePeriod)) {
                    //按套餐消费
                    if (ChargeMethodCategory.isCombination(grade)) {
                        //合并学生id
                        attendStudentIds.addAll(missStudentIds);
                        //按照套餐价统一扣除所有学生锁定余额,总余额,记录所有学生消费
                        studentService.batchConsumeAndRecord(grade, grade.getChargeMethodExpression(), students,
                                attendStudentIds, "套餐结课自动结算，共" + schedulePeriod + "课时");
                        //总收入 = 套餐收费 * 班级总人数
                        BigDecimal income =
                                grade.getChargeMethodExpression().multiply(new BigDecimal(grade.getGradeNowStudentCount()));
                        //增加总收入
                        gradeService.income(grade.getGradeId(),income);
                        //设置收入
                        studentScheduleService.income(scheduleId, income);
                    }
                    //改为已结课状态
                    gradeService.gradeEnd(gradeId);
                } else {
                    //已上课时!=总课时
                    //将班级状态从上课中改为开课中,并且自动发送到mq中
                    gradeService.gradeRunAndSendMq(gradeId);
                }
            }
        });
        //删除key,解锁
        RedisUtil.del("GradeId:" + grade.getGradeId());

    }
    //排课

    //停课,待排课状态进行排课

    //排课时间不能小于当前时间,排课课时不能大于剩余课时

    //排课成功
        //重新计算下一节课排课id
        //更新到redis中,过期时间为下一节课的下课时间加一分钟

    //停课后删除
        //从redis中获得该节课下一节课的排课id
        //要删的课是下一节课
            //删除后重新计算下一节课,并且更新到redis中,过期时间为下一节课的下课时间加一分钟
        //不是下一节课,直接删除

    //设为开课后重新计算下一节课的课表id,将班级id作为key,排课id作为value存在redis中,并且将排课id和班级id发送到mq中
}
