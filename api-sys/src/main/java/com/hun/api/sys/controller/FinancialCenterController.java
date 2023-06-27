package com.hun.api.sys.controller;

import com.hun.bean.bo.FinancialAnalysisHeaderBO;
import com.hun.bean.bo.FinancialAnalysisTeacherBO;
import com.hun.bean.entity.base.Schedule;
import com.hun.bean.entity.expand.RecordGradeDrop;
import com.hun.bean.entity.expand.StudentGradeStop;
import com.hun.bean.vo.FinancialAnalysisTeacherVO;
import com.hun.bean.vo.FinancialAnalysisVO;
import com.hun.common.response.BaseResponse;
import com.hun.service.service.base.ScheduleService;
import com.hun.service.service.expand.RecordGradeDropService;
import com.hun.service.service.expand.StudentGradeStopService;
import com.hun.service.service.expand.StudentScheduleService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;

/**
 * 财务中心
 */
@RestController
@RequestMapping("/api/financialCenter")
@CrossOrigin
public class FinancialCenterController {

    @Resource
    private StudentScheduleService studentScheduleService;
    @Resource
    private RecordGradeDropService recordGradeDropService;
    @Resource
    private StudentGradeStopService studentGradeStopService;
    @Resource
    private ScheduleService scheduleService;
    @Resource(name = "threadPool")
    private ThreadPoolTaskExecutor threadPoolExecutor;

    /**
     * 开始时间, 结束时间
     * 查出这个月的在读学员数, 点名应到人次, 点名实到人次,点名课消课时,点名课消金额,总课消金额
     */
    @GetMapping("/analysis")
    public BaseResponse<FinancialAnalysisVO> analysis(@DateTimeFormat(pattern = "yyyy-M-d")
                                                      @RequestParam LocalDate date) throws InterruptedException {
        //获得开始时间
        LocalDateTime startDate = date.atTime(0, 0, 0).withDayOfMonth(1);
        //结算时间
        LocalDateTime endDate;
        if (LocalDate.now().withDayOfMonth(1).equals(startDate.toLocalDate())) {
            //如果是查询是当前月, 则结束日期为当前时间
            endDate = LocalDateTime.now();
        } else {
            endDate = startDate.plusMonths(1);
        }

        long startTimestamp = startDate.toInstant(ZoneOffset.of("+8")).toEpochMilli();
        long endTimestamp = endDate.toInstant(ZoneOffset.of("+8")).toEpochMilli();

        FinancialAnalysisVO financialAnalysisVO = new FinancialAnalysisVO();
        //线程阻塞
        CountDownLatch countDownLatch = new CountDownLatch(2);

        CompletableFuture.runAsync(() -> {
            FinancialAnalysisHeaderBO bo = studentScheduleService.getAllCountAndIncome(startTimestamp, endTimestamp);
            //获得出勤人数
//        Integer attendCount = studentScheduleService.attendCount(startTimestamp, endTimestamp);
            Integer attendCount = bo.getAttendCount();
            //获得缺勤人数
//        Integer missCount = studentScheduleService.missCount(startTimestamp, endTimestamp);
            Integer missCount = bo.getMissCount();
            //点名课消金额, 不包括套餐
//        BigDecimal callConsume = studentScheduleService.callCourseCancelIncome(startTimestamp, endTimestamp);
            BigDecimal callConsume = bo.getCallCourseCancelIncome();
            //总课消金额, 包括套餐
//        BigDecimal consume = studentScheduleService.courseCancelIncome(startTimestamp, endTimestamp);
            BigDecimal consume = bo.getCourseCancelIncome();
            //学员课消课时, 包括套餐
//            Integer period = studentScheduleService.studentCourseCancelPeriod(startTimestamp, endTimestamp);
            Integer period = bo.getConsumePeriod();
            financialAnalysisVO.setPeriod(period);
            financialAnalysisVO.setAttendCount(attendCount);
            financialAnalysisVO.setMissCount(missCount);
            financialAnalysisVO.setCallConsume(callConsume);
            financialAnalysisVO.setConsume(consume);
            countDownLatch.countDown();
        }, threadPoolExecutor);

        CompletableFuture.runAsync(() -> {
            //获得学生就总读人数
            Integer studentCount = studentScheduleService.getStudentCountByMonth(startTimestamp, endTimestamp);
            financialAnalysisVO.setStudentCount(studentCount);
            countDownLatch.countDown();
        }, threadPoolExecutor);

        countDownLatch.await();
        return BaseResponse.ok(financialAnalysisVO);
    }

    /**
     * 开始时间, 结束时间
     * 查出所有老师, 老师的班级, 班级的人数, 应到人次, 实到人次, 课消金额
     */
    @GetMapping("/analysis/teacher")
    public BaseResponse<List<List<FinancialAnalysisTeacherVO>>> analysisTeacher(@DateTimeFormat(pattern = "yyyy-M-d")
                                                                                @RequestParam LocalDate date) throws InterruptedException {
        //获得开始时间
        LocalDateTime startDate = date.atTime(0, 0, 0).withDayOfMonth(1);
        startDate = startDate.withDayOfMonth(1);

        LocalDate now = LocalDate.now();//结算时间
        LocalDateTime endDate;
        if (now.withDayOfMonth(1).equals(startDate.toLocalDate())) {
            //如果是查询是当前月, 则结束日期为当前时间
            endDate = LocalDateTime.now();
        } else {
            endDate = startDate.plusMonths(1);
        }

        long startTimestamp = startDate.toInstant(ZoneOffset.of("+8")).toEpochMilli();
        long endTimestamp = endDate.toInstant(ZoneOffset.of("+8")).toEpochMilli();

        //线程阻塞
        CountDownLatch countDownLatch = new CountDownLatch(3);
        //开启多线程
        List<Schedule> schedules = new ArrayList<>();
        CompletableFuture.runAsync(()-> {
            long l = System.currentTimeMillis();
            //查出排课的班级
            schedules.addAll(scheduleService.getByMonthAndGroupByGradeId(startTimestamp, endTimestamp));
            System.out.println("-------------------" + (System.currentTimeMillis() - l) + "-----------------------");
            countDownLatch.countDown();
        }, threadPoolExecutor);
        //开启多线程
        List<StudentGradeStop> studentGradeStops = new ArrayList<>();
        CompletableFuture.runAsync(()-> {
            long l = System.currentTimeMillis();
            //查出排课的班级
            studentGradeStops.addAll(studentGradeStopService
                    .getByMonthAndGroupByGradeId(startTimestamp, endTimestamp));
            System.out.println("-------------------" + (System.currentTimeMillis() - l) + "-----------------------");
            countDownLatch.countDown();
        }, threadPoolExecutor);
        //开启多线程
        List<RecordGradeDrop> recordGradeDrops = new ArrayList<>();
        CompletableFuture.runAsync(()-> {
            //查出退课的班级
            long l = System.currentTimeMillis();
            recordGradeDrops.addAll(recordGradeDropService
                    .getByMonthAndGroupByGradeId(startTimestamp, endTimestamp));
            System.out.println("-------------------" + (System.currentTimeMillis() - l) + "-----------------------");
            countDownLatch.countDown();
        }, threadPoolExecutor);
        //线程阻塞
        countDownLatch.await();
        //要返回的数据
        List<List<FinancialAnalysisTeacherVO>> response = new ArrayList<>();
        for (Schedule schedule : schedules) {
            warpTeacher(response, schedule.getTeacherId(), schedule.getEmployeeName(), schedule.getGradeId(),
                    schedule.getGradeName(), schedule.getChargeMethodId(), startTimestamp, endTimestamp);
        }
        for (RecordGradeDrop recordGradeDrop : recordGradeDrops) {
            warpTeacher(response, recordGradeDrop.getTeacherId(), recordGradeDrop.getEmployeeName(),
                    recordGradeDrop.getGradeId(), recordGradeDrop.getGradeName(),
                    recordGradeDrop.getChargeMethodId(), startTimestamp, endTimestamp);
        }
        for (StudentGradeStop studentGradeStop : studentGradeStops) {
            warpTeacher(response, studentGradeStop.getTeacherId(), studentGradeStop.getEmployeeName(),
                    studentGradeStop.getGradeId(), studentGradeStop.getGradeName(),
                    studentGradeStop.getChargeMethodId(), startTimestamp, endTimestamp);
        }
        return BaseResponse.ok(response);
    }


    private void warpTeacher(List<List<FinancialAnalysisTeacherVO>> response, long teacherId, String employeeName,
                             long gradeId, String gradeName, long chargeMethodId, long startTimestamp, long endTimestamp) throws InterruptedException {
        //遍历每个老师的班级
        l:
        for (List<FinancialAnalysisTeacherVO> teachers : response) {
            //遍历老师的每个班级
            for (FinancialAnalysisTeacherVO gradeVO : teachers) {
                if (!gradeVO.getTeacherId().equals(teacherId)) {
                    //这不是该老师的班级列表
                    continue l;
                }
                if (gradeVO.getGradeId().equals(gradeId)) {
                    //该老师已经统计了该班级
                    return;
                }

            }

            //发现老师未统计该班级
            addFinancialAnalysisTeacherVO(teacherId, employeeName, gradeId, gradeName,
                    chargeMethodId, teachers, startTimestamp, endTimestamp);
            return;
        }
        //不存老师则, 添加一个
        List<FinancialAnalysisTeacherVO> list = new ArrayList<>();
        response.add(list);
        addFinancialAnalysisTeacherVO(teacherId, employeeName, gradeId, gradeName,
                chargeMethodId, list, startTimestamp, endTimestamp);
    }

    private void addFinancialAnalysisTeacherVO(long teacherId, String employeeName, long gradeId, String gradeName,
                                               long chargeMethodId, List<FinancialAnalysisTeacherVO> list,
                                               long startTimestamp, long endTimestamp) throws InterruptedException {
        FinancialAnalysisTeacherVO vo = new FinancialAnalysisTeacherVO();
        //设置老师id
        vo.setTeacherId(teacherId);
        //设置老师名字
        vo.setEmployeeName(employeeName);
        //设置班级名字
        vo.setGradeName(gradeName);
        //设置班级id
        vo.setGradeId(gradeId);
        //设置收费表达式
        vo.setChargeMethodId(chargeMethodId);

        //线程阻塞
        CountDownLatch countDownLatch = new CountDownLatch(4);
        long l = System.currentTimeMillis();
        //开启多线程
        CompletableFuture.runAsync(() -> {
            //查出出勤人数, 缺勤人数, 课消金额[包括套餐]
            FinancialAnalysisTeacherBO bo = studentScheduleService.
                    getAllCountAndIncomeByGradeId(gradeId, startTimestamp, endTimestamp);
            //班级出勤人数
            vo.setAttendCount(bo.getAttendCount());
            //班级缺勤人数
            vo.setMissCount(bo.getMissCount());
            //班级课消金额, 课消金额[包括套餐]
            vo.setCourseCancelIncome(bo.getCourseCancelIncome());
            countDownLatch.countDown();
        }, threadPoolExecutor);
        //开启多线程
        CompletableFuture.runAsync(() -> {
            //查出班级学生人数[不包括退课, 停课]
            vo.setStudentCount(studentScheduleService
                    .getMonthStudentCountByGradeId(gradeId, startTimestamp, endTimestamp));
            countDownLatch.countDown();
        }, threadPoolExecutor);
        //开启多线程
        CompletableFuture.runAsync(() -> {
            //班级停课人数
            vo.setStopGradeCount(studentGradeStopService
                    .getCountByGradeIdAndMonth(gradeId, startTimestamp, endTimestamp));
            countDownLatch.countDown();
        }, threadPoolExecutor);
        //开启多线程
        CompletableFuture.runAsync(() -> {
            //班级退课人数
            vo.setTuiKeCount(recordGradeDropService
                    .getCountByGradeIdAndMonth(gradeId, startTimestamp, endTimestamp));
            countDownLatch.countDown();
        }, threadPoolExecutor);
        System.out.println("-------------------" + (System.currentTimeMillis() - l) + "-----------------------");
        //线程阻塞
        countDownLatch.await();
        list.add(vo);
    }
}
