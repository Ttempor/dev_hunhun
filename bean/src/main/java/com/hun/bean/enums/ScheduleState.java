package com.hun.bean.enums;


import com.hun.bean.entity.base.Schedule;

public enum ScheduleState {
    WAIT(0, "未上"),
    ING(1, "上课中"),
    END(2, "已上"),
    MISS(3, "缺勤");

    ScheduleState(int code, String state) {
        this.code = code;
        this.state = state;
    }
    private final int code;
    private final String state;

    public int code() {
        return code;
    }

    public static boolean isING(Schedule schedule) {
        return schedule.getScheduleState() == ING.code();
    }
    public static boolean isMISS(Schedule schedule) {
        return schedule.getScheduleState() == MISS.code();
    }
    public static boolean isEnd(Schedule schedule) {
        return schedule.getScheduleState() == END.code();
    }
    public static boolean isWAIT(Schedule schedule) {
        return schedule.getScheduleState() == WAIT.code();
    }
}
