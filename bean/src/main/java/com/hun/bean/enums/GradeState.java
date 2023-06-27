package com.hun.bean.enums;

import com.hun.bean.entity.base.Grade;

/**
 * 班级状态
 */
public enum GradeState {
    wait(0, "待排课"),
    run(1, "已开课"),
    ing(2,"上课中"),
    end(3, "已结课");

    GradeState(int code, String state) {
        this.code = code;
        this.state = state;
    }
    private final int code;
    private final String state;

    public int code() {
        return code;
    }

    public static boolean isWait(int gradeState) {
        return wait.code == gradeState;
    }

    public static boolean isWait(Grade grade) {
        return isWait(grade.getGradeState());
    }

    public static boolean isRun(int gradeState) {
        return run.code == gradeState;
    }

    public static boolean isRun(Grade grade) {
        return isRun(grade.getGradeState());
    }
    public static boolean isIng(int gradeState) {
        return ing.code == gradeState;
    }

    public static boolean isIng(Grade grade) {
        return isIng(grade.getGradeState());
    }
    public static boolean isEnd(int gradeState) {
        return end.code == gradeState;
    }

    public static boolean isEnd(Grade grade) {
        return isEnd(grade.getGradeState());
    }
}
