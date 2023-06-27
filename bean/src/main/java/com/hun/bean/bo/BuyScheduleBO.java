package com.hun.bean.bo;

import com.hun.bean.entity.base.Grade;
import com.hun.bean.entity.base.Schedule;
import com.hun.bean.entity.base.Student;
import com.hun.bean.entity.expand.GradeInfo;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class BuyScheduleBO {
    List<Schedule> schedules;
    BigDecimal price;
    Integer totalPeriod;
    Student student;
    Grade grade;
}
