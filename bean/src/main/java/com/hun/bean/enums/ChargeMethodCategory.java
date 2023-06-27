package com.hun.bean.enums;

import com.hun.bean.entity.base.Grade;

public enum ChargeMethodCategory {
    PERIOD(1, "按课时收费"),
    COMBINATION(2, "按套餐收费");

    ChargeMethodCategory(int code, String state) {
        this.code = code;
        this.state = state;
    }
    private final long code;
    private final String state;

    public long code() {
        return code;
    }

    public static boolean isPeriod(long i) {
        return i == PERIOD.code;
    }
    public static boolean isPeriod(Grade grade) {
        return isPeriod(grade.getChargeMethodId());
    }
    public static boolean isCombination(long i) {
        return i == COMBINATION.code;
    }
    public static boolean isCombination(Grade grade) {
        return isCombination(grade.getChargeMethodId());
    }

}
