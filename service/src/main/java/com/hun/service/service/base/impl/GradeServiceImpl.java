package com.hun.service.service.base.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hun.bean.app.AppGradeVO;
import com.hun.bean.app.CategoryItemVO;
import com.hun.bean.app.CategoryVO;
import com.hun.bean.app.StudentGradeVO;
import com.hun.bean.bo.BuyScheduleBO;
import com.hun.bean.dto.base.grade.*;
import com.hun.bean.entity.base.*;
import com.hun.bean.entity.expand.GradeInfo;
import com.hun.bean.enums.ChargeMethodCategory;
import com.hun.bean.enums.ScheduleState;
import com.hun.bean.vo.GradeVO;
import com.hun.bean.enums.GradeState;
import com.hun.common.exception.BusinessException;
import com.hun.service.mapper.base.GradeMapper;
import com.hun.service.mapper.expand.GradeInfoMapper;
import com.hun.service.service.MqService;
import com.hun.service.service.base.*;
import com.hun.service.service.expand.GradeInfoService;
import com.hun.service.service.expand.StudentScheduleService;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 性能小钢炮
 * @description 针对表【grade】的数据库操作Service实现
 * @createDate 2023-03-25 18:35:44
 */
@Service
public class GradeServiceImpl implements GradeService {
    @Resource
    private GradeMapper gradeMapper;
    @Resource
    private GradeInfoMapper gradeInfoMapper;
    @Resource
    private StudentScheduleService studentScheduleService;
    @Resource
    private CourseService courseService;
    @Resource
    private ScheduleService scheduleService;
    @Resource
    private TeacherService teacherService;
    @Resource
    private StudentService studentService;
    @Resource
    private GradeInfoService gradeInfoService;
    @Resource
    private TransactionTemplate transactionTemplate;
    @Resource
    private MqService mqService;
    @Resource
    private MapperFacade mapperFacade;
    @Value("${default-page-size}")
    private long pageSize;



    /**
     * 班级的条件分页
     * 索引顺序是grade_name, course_name, employee_name
     *
     * @param queryGradePageDto e
     */
    @Override
    public Page<Grade> getGradePage(QueryGradePageDto queryGradePageDto) {
        QueryWrapper<Grade> queryWrapper = new QueryWrapper<>();
        //是否要附上班级名条件
        if (queryGradePageDto.getGradeName() != null) {
            queryWrapper.like("grade_name", queryGradePageDto.getGradeName().trim());
        }
        //是否要附上课程姓名条件
        if (queryGradePageDto.getCourseName() != null) {
            queryWrapper.like("course_name", queryGradePageDto.getCourseName().trim());
        }
        //是否要附上员工姓名条件
        if (queryGradePageDto.getEmployeeName() != null) {
            queryWrapper.like("employee_name", queryGradePageDto.getEmployeeName().trim());
        }
        if (queryGradePageDto.getIsWait() != null || queryGradePageDto.getIsRun() != null ||
                 queryGradePageDto.getIsIng() != null || queryGradePageDto.getIsEnd() != null) {
            queryStateWrap(queryWrapper, queryGradePageDto.getIsWait(), queryGradePageDto.getIsRun(),
                    queryGradePageDto.getIsIng(), queryGradePageDto.getIsEnd());
        }
        Page<Grade> page = Page.of(queryGradePageDto.getP(), pageSize);
        return gradeMapper.selectPage(page, queryWrapper);
    }


    /**
     * 通过状态查询所有班级id和名称和状态
     */
    @Override
    public List<GradeVO> getGradeByState(QueryByStateDto queryByStateDto) {
        if (queryByStateDto.getIsWait() == null && queryByStateDto.getIsRun() == null
                && queryByStateDto.getIsIng() == null && queryByStateDto.getIsEnd() == null) {
            return new ArrayList<>();
        }
        QueryWrapper<Grade> queryWrapper = new QueryWrapper<>();
        queryStateWrap(queryWrapper, queryByStateDto.getIsWait(), queryByStateDto.getIsRun(),
                queryByStateDto.getIsIng(), queryByStateDto.getIsEnd());
        return gradeMapper.getGradeListByState(queryWrapper);
    }

    private void queryStateWrap(QueryWrapper<Grade> queryWrapper, Integer isWait, Integer isRun, Integer isIng, Integer isEnd) {
        queryWrapper.and((q)-> {
            if (isWait != null) {
                q.or((query) -> query.eq("grade_state", GradeState.wait.code()) );
            }
            if (isRun != null) {
                q.or((query) -> query.eq("grade_state", GradeState.run.code()) );
            }
            if (isIng != null) {
                q.or((query) -> query.eq("grade_state", GradeState.ing.code()));
            }
            if (isEnd != null) {
                q.or((query) -> query.eq("grade_state", GradeState.end.code()));
            }
        });
    }


    /**
     * 获得所有待排课和停课中且人数未满的班级id和班级名
     */
    @Override
    public List<GradeVO> getGradeList() {
        return gradeMapper.selectGradeIdAndGradeName();
    }



    /**
     * 通过班级id查一个班级
     */
    @Override
    public Grade getOneGradeByGradeId(Long gradeId) {
        return gradeMapper.selectById(gradeId);
    }


    /**
     *  通过老师id获得上课中的班级列表
     */

    @Override
    public List<Grade> getIngGradeListByTeacherId(Long teacherId) {
        QueryWrapper<Grade> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("teacher_id", teacherId);
        queryWrapper.eq("grade_state", GradeState.ing.code());
        return gradeMapper.selectList(queryWrapper);
    }

    /**
     *  通过老师id获得上课中的班级列表
     */
    @Override
    public List<Grade> getNotEndGradeListByTeacherId(Long teacherId) {
        QueryWrapper<Grade> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("teacher_id", teacherId);
        queryWrapper.ne("grade_state", GradeState.end.code());
        return gradeMapper.selectList(queryWrapper);
    }

    @Override
    public void incrGradeNowStudentCountByGradeId(Long gradeId, int i) {
        gradeMapper.incrGradeNowStudentCountByGradeId(gradeId, i);
    }

    @Override
    public void income(Long gradeId, BigDecimal schedulePrice) {
        gradeMapper.income(gradeId, schedulePrice);
    }

    @Override
    public Integer getNowStudentCount(Long gradeId) {
        return gradeMapper.getNowStudentCount(gradeId);
    }

    @Override
    public BuyScheduleBO buySchedulePreCheck(Long gradeId, List<Long> scheduleIds, Long studentId) {
        Grade grade = getOneGradeByGradeId(gradeId);
        //套餐不能使用
        if (ChargeMethodCategory.isCombination(grade)) {
            throw new BusinessException("套餐必须购买所有课时");
        }
        List<Schedule> schedules = scheduleService.getBatchByIds(gradeId, scheduleIds);
        if (schedules.size() != scheduleIds.size()) {
            //一个班级下的scheduleId对应相同数量的schedule, 数量不相同表示有scheduleId不是该班级的
            throw new BusinessException("班级错误");
        }
        //查询所选的schedule
        //学生不能在schedule中, 否则重复
        if (studentScheduleService.studentInScheduleByScheduleIds(studentId, scheduleIds) > 0) {
            throw new BusinessException("学员已经在该课中");
        }
        //该课的总课时
        int totalPeriod = 0;
        //购课需要的金额
        BigDecimal price = new BigDecimal("0.00");
        for (Schedule schedule : schedules) {
            //schedule的人数不能满
            if (schedule.getGradeNowStudentCount() >= schedule.getGradeMaxStudentCount()) {
                throw new BusinessException("人数已满");
            }
            //schedule要处于未上状态
            if (!ScheduleState.isWAIT(schedule)) {
                throw new BusinessException("要求处于未上状态");
            }
            //不能在上课前五分钟添加到schedule中
            if (schedule.getStartTimestamp() <= System.currentTimeMillis()) {
                throw new BusinessException("还有五分钟上课, 无法购课");
            }
            //累加购课所需金额
            price = price.add(schedule.getPrice());
            //累计购买这些课能获得的总课时
            totalPeriod += schedule.getConsumePeriod();
        }

        //查出学生
        Student student = studentService.getOneStudentById(studentId);
        //算出减去购课后的余额
        BigDecimal balance = student.getBalance().subtract(student.getLockBalance()).subtract(price);
        //判断学生余额是否足够
        if (balance.compareTo(new BigDecimal("0.00")) < 0) {
            throw new BusinessException("余额不足, 缺少:" + balance + "￥");
        }
        BuyScheduleBO buyScheduleBO = new BuyScheduleBO();
        buyScheduleBO.setSchedules(schedules);
        buyScheduleBO.setPrice(price);
        buyScheduleBO.setStudent(student);
        buyScheduleBO.setTotalPeriod(totalPeriod);
        buyScheduleBO.setGrade(grade);
        return buyScheduleBO;
    }

    @Override
    public List<CategoryItemVO> getCategoryByCourseId(Long courseId) {
        return gradeMapper.getCategoryByCourseId(courseId);
    }

    @Override
    public AppGradeVO getCategoryGrade(Long gradeId) {
        return gradeMapper.getCategoryGrade(gradeId);
    }

    @Override
    public List<StudentGradeVO> getStudentGrades(Long studentId) {
        return gradeInfoMapper.getStudentGrades(studentId);
    }


    /**
     * 插入班级
     *
     * @param saveGradeDto e
     */
    @Override
    public void saveGrade(SaveGradeDto saveGradeDto) {
        //对传过来的参数不做校验了
        Course course = courseService.getOneCourseById(saveGradeDto.getCourseId());
        if (course == null) {
            throw new BusinessException("课程id错误");
        }
        Teacher teacher = teacherService.getOneTeacherByTeacherId(saveGradeDto.getTeacherId());
        if (teacher == null) {
            throw new BusinessException("老师id错误");
        }
        //构建班级
        Grade grade = new Grade();
        grade.setCourseId(course.getCourseId());
        grade.setCourseName(course.getCourseName().trim());
        grade.setTeacherId(teacher.getTeacherId());
        grade.setEmployeeName(teacher.getEmployeeName().trim());
        grade.setGradeNowStudentCount(0);
        grade.setGradeMaxStudentCount(saveGradeDto.getGradeMaxStudentCount());
        grade.setGradeNowPeriod(0);
        grade.setGradeTotalPeriod(course.getCourseTotalPeriod());
        grade.setGradeName(saveGradeDto.getGradeName().trim());
        grade.setChargeMethodId(course.getChargeMethodId());
        grade.setChargeMethodName(course.getChargeMethodName());
        grade.setChargeMethodExpression(course.getChargeMethodExpression());
        grade.setIncome(new BigDecimal("0.00"));
        grade.setPrice(course.getPrice());
        grade.setGradeState(GradeState.wait.code());
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                //添加班级后,课程中的班级拥有数量应该+1
                courseService.incrGradeCountByCourseId(course.getCourseId(), 1);
                //添加班级后,课程中的老师拥有数量应该+1
                courseService.incrTeacherCountByCourseId(course.getCourseId(), 1);
                //添加班级后,老师所授课的班级数量应该+1
                teacherService.incrGradeCountByTeacherId(teacher.getTeacherId(), 1);
                //插入班级
                gradeMapper.insert(grade);
            }
        });
    }


    public Student getStudentAndCheckNotInGrade(Long studentId, Grade grade) {
        Student student = studentService.getOneStudentById(studentId);
        if (student == null) {
            throw new BusinessException("学生id错误");
        }
        //查询学生是否存在班级中
        if (gradeInfoService.getOneByGradeIdAndStudentId(grade.getGradeId(), student.getStudentId()) != null) {
            throw new BusinessException("学生已经在该班级中");
        }
        //学生不能报读相同课程
        if (gradeInfoService.studentInCourse(grade.getCourseId(), student.getStudentId()) != null) {
            throw new BusinessException("无法加入相同课程的班级");
        }
        return student;
    }

    public Grade getGradeAndCheckJoin(Long gradeId) {
        Grade grade = getOneGradeByGradeId(gradeId);
        if (grade == null) {
            throw new BusinessException("班级id错误");
        }
        if (grade.getGradeNowStudentCount() >= grade.getGradeMaxStudentCount()) {
            throw new BusinessException("班级学生人数已满");
        }
        //上过课的班级而且是套餐出售,禁止再加入学生
        if (ChargeMethodCategory.isCombination(grade) && grade.getGradeNowPeriod() != 0) {
            throw new BusinessException("套餐班级已开课, 禁止加入");
        }
        //班级状态不能为开课中、已结课
        if (GradeState.isIng(grade) || GradeState.isEnd(grade)) {
            throw new BusinessException("上课中, 禁止加入");
        }
        return grade;
    }

    @Override
    public void saveStudentToGrade(Student student, Grade grade) {
        GradeInfo gradeInfo = mapperFacade.map(grade, GradeInfo.class);
        gradeInfo.setStudentId(student.getStudentId());
        gradeInfo.setStudentName(student.getStudentName());
        gradeInfo.setConsumePeriod(0);
        gradeInfo.setMissPeriod(0);
        gradeInfo.setTotalPeriod(grade.getGradeTotalPeriod());
        gradeInfo.setSpendBalance(new BigDecimal("0.00"));
        gradeInfo.setLockSpendBalance(grade.getPrice());
        gradeInfo.setLockBalance(grade.getPrice());
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                //班级当前学生数+1
                incrGradeNowStudentCountByGradeId(gradeInfo.getGradeId(), 1);
                //学生班级数+1
                studentService.incrGradeCountByStudentId(student.getStudentId(), 1);
                //gradeInfo插入一条数据
                gradeInfoService.saveGradeInfo(gradeInfo);
            }
        });
    }

    /**
     * 修改班级名,最大人数
     *
     * @param updateGradeDto e
     */
    @Override
    public void updateOneGrade(UpdateGradeDto updateGradeDto) {
        //通过班级id查询班级
        Grade grade = gradeMapper.selectById(updateGradeDto.getGradeId());
        //非排课状态非已开课状态不可以修改班级
        if (grade == null || !(GradeState.isWait(grade) || GradeState.isRun(grade))) {
            throw new BusinessException("只有在待排课状态才可以修改班级");
        }
        //比较修改最大人数, 是否要修改最大人数
        Integer newMaxStudentCount = updateGradeDto.getGradeMaxStudentCount();
        //比较是否可以修改人数
        if (grade.getGradeNowStudentCount() > newMaxStudentCount) {
            throw new BusinessException("班级最大人数小于当前班级人数");
        }
        grade.setGradeName(updateGradeDto.getGradeName().trim());
        grade.setGradeMaxStudentCount(newMaxStudentCount);
        gradeMapper.updateById(grade);
    }



    /**
     * 班级变为上课中
     */
    @Override
    public void gradeIng(Long gradeId) {
        Grade grade = new Grade();
        grade.setGradeId(gradeId);
        grade.setGradeState(GradeState.ing.code());
        gradeMapper.updateById(grade);
        gradeInfoService.gradeIng(gradeId);
    }

    /**
     * 班级变为已开课
     */
    @Override
    public void gradeRunAndSendMq(Long gradeId) {
        Grade grade = new Grade();
        grade.setGradeId(gradeId);
        grade.setGradeState(GradeState.run.code());
        gradeMapper.updateById(grade);
        gradeInfoService.gradeRun(gradeId);
        //获得下一节排课
        Schedule nextSchedule = scheduleService.getNextSchedule(grade.getGradeId());
        //将下一节课班级id添加到消息队列中
        mqService.sendGradeStart(gradeId, nextSchedule);
    }

    /**
     * 班级变为已结课
     */
    @Override
    public void gradeEnd(Long gradeId) {
        long endTimestamp = System.currentTimeMillis();
        LocalDateTime endDatetime = LocalDateTime.
                ofInstant(Instant.ofEpochMilli(endTimestamp), ZoneOffset.of("+8"));
        Grade grade = new Grade();
        grade.setGradeId(gradeId);
        grade.setGradeState(GradeState.end.code());
        grade.setEndTimestamp(endTimestamp);
        grade.setEndDatetime(endDatetime);
        gradeMapper.updateById(grade);

        UpdateWrapper<GradeInfo> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("grade_id", gradeId);

        GradeInfo gradeInfo = new GradeInfo();
        gradeInfo.setEndTimestamp(endTimestamp);
        gradeInfo.setEndDatetime(endDatetime);
        gradeInfo.setGradeState(GradeState.end.code());
        gradeInfoMapper.update(gradeInfo, updateWrapper);
    }


    /**
     * 修改班级已上课时
     */

    @Override
    public void plusNowPeriod(Long gradeId, Integer period) {
        UpdateWrapper<Grade> updateWrapper = new UpdateWrapper<>();
        updateWrapper.setSql("grade_now_period = grade_now_period + " + period);
        updateWrapper.eq("grade_id", gradeId);
        gradeMapper.update(null, updateWrapper);
    }
/*
    * 修改班级老师,最大人数
    * @param updateGradeDto e
    @Override
    public void updateOneGrade(UpdateGradeDto updateGradeDto) {
        //通过班级id查询班级
        Grade grade = gradeMapper.selectById(updateGradeDto.getGradeId());
        if (grade == null) {
            throw new BusinessException("班级id错误");
        }
        if (grade.getGradeState() == GradeState.run.code()) {
            throw new BusinessException("班级已开课状态不可修改");
        }
        //比较老师id, 是否要修改老师
        Teacher teacher = null;
        //新旧老师id相同,则不需要修改老师
        if (!grade.getTeacherId().equals(updateGradeDto.getTeacherId())) {
            //查询新老师id
            teacher = teacherService.getOneTeacherByTeacherId(updateGradeDto.getTeacherId());
            if (teacher == null) {
                throw new BusinessException("老师id错误");
            }
        }
        //比较修改最大人数, 是否要修改最大人数
        Integer oldMaxStudentCount = grade.getGradeMaxStudentCount();
        Integer newMaxStudentCount = updateGradeDto.getGradeMaxStudentCount();
        if (newMaxStudentCount < oldMaxStudentCount) {
            throw new BusinessException("班级最大人数小于当前班级人数");
        }
        //人数相同不需要修改人数
        if (oldMaxStudentCount.equals(newMaxStudentCount)) {
            newMaxStudentCount = null;
        }
        //都不需要修改,直接返回
        if (newMaxStudentCount == null && teacher == null) {
            return;
        }
        final Teacher fTeacher = teacher;
        final Integer fNewMaxStudentCount = newMaxStudentCount;
        //开始统一处理,对数据库进行操作,如果判断一个就处理一个,会出现第一个条件通过进行了修改,但是第二个判断未通过且出现异常而回滚的情况
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                //确定修改老师
                if (fTeacher != null) {
                    //修改老师后,修改grade_info表中该班级的老师id和老师名称
                    gradeInfoService.updateTeacherIdAndEmployeeNameByGradeId(fTeacher.getTeacherId(),
                            fTeacher.getEmployeeName(), grade.getGradeId());
                    //修改老师后,旧老师所授课的班级数量应该-1
                    teacherService.incrGradeCountByTeacherId(grade.getTeacherId(), -1);
                    //修改老师后,新老师所授课的班级数量应该+1
                    teacherService.incrGradeCountByTeacherId(fTeacher.getTeacherId(), 1);
                    //确定修改老师
                    gradeMapper.updateTeacherInfoByGradeId(fTeacher.getTeacherId(),
                            fTeacher.getEmployeeName(), grade.getGradeId());
                }
                //修改班级最大人数
                if (fNewMaxStudentCount != null) {
                    gradeMapper.updateGradeMaxStudentCountByGradeId(fNewMaxStudentCount, grade.getGradeId());
                }
            }
        });
    }
*/


//    /**
//     * 通过id删除一个班级
//     */
//    @Override
//    public void deleteOneGradeById(Integer sid) {
//        Grade grade = gradeMapper.selectById(sid);
//        if (grade == null) {
//            throw new BusinessException("班级id错误");
//        }
//        if (grade.getGradeState() == GradeState.run.code()) {
//            throw new BusinessException("已开课班级不能删除");
//        }
//        //删除班级后,grade_info中该班级的信息应该删除
//        //添加班级后,课程中的班级拥有数量应该-1
//                --
//        //添加班级后,课程中的老师拥有数量应该-1
//                        --
//        //添加班级后,老师所授课的班级数量应该-1
//                                --
//        gradeMapper.deleteById(sid)
//    }


}




