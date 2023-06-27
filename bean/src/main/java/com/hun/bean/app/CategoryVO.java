package com.hun.bean.app;

import lombok.Data;

import java.util.List;

@Data
public class CategoryVO {
    /**
     * 课程id
     */
    private Long courseId;

    /**
     * 课程名
     */
    private String courseName;

    /**
     * 课程的班级列表
     */
    private List<CategoryItemVO> grades;
}
