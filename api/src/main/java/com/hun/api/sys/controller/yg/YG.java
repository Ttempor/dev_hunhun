package com.hun.api.sys.controller.yg;

import java.time.LocalDate;
import java.util.Arrays;

public class YG {

    private LocalDate weekend;

    public YG() {
        checkAndRefresh();
    }

    private String head;
    private Day[] days = new Day[7];

    public void checkAndRefresh() {
        LocalDate now = LocalDate.now();
        int offset = now.getDayOfWeek().getValue();
        if (weekend == null || (offset == 1 && weekend.isBefore(now))) {
            int month = now.getMonth().getValue();
            int day = now.getDayOfMonth();
            String startDay = month + "." + day;
            String endDay = month + "." + (day + 6);
            this.head =  startDay + " ~ " + endDay + "   国服日常赛预告\n" + "截至发布前\n";
            this.days = new Day[7];
            weekend = now.plusDays(7 - offset);
        }
    }


    public void add(String content, int day) {
        Day d = days[day - 1];
        if (d == null) {
            LocalDate date = weekend.minusDays(7 - day);
            String headDay = date.getMonth().getValue() + "." + date.getDayOfMonth() + " 周" + WeekCaseUtil.weekCaseString(day);
            d = new Day(headDay);
            days[day - 1] = d;
        }
        d.addContent(content);
    }


    @Override
    public String toString() {
        StringBuilder result = new StringBuilder(head);
        for (Day d : days) {
            if (d != null) {
                result.append(d);
            }
        }
        return result.toString();
    }

    public void remove(int day, int index) {

        Day d = days[day];
        if (d != null) {
            d.remove(index);
            if (d.isEmpty()) {
                days[day] = null;
            }
        }
    }
}
