package com.hun.api.sys.controller.yg;

public class Commit {

    public static void main(String[] args) {
        Commit commit = new Commit();
        commit.handle("添加日常赛 周6 alpha  圣玛丽德蒙特(攻防)\n" +
                "https://docs.qq.com/sheet/DZUxyV01PUGhYalFS");
        commit.handle("添加日常赛 周asd alpha  圣玛丽德蒙特(攻防)\n" +
                "https://docs.qq.com/sheet/DZUxyV01PUGhYalFS");
        commit.handle("添加日常赛 周日 alpha  圣玛丽德蒙特(攻防)\n" +
                "https://docs.qq.com/sheet/DZUxyV01PUGhYalFS");
        commit.handle("删除预告 周7 1");
        commit.handle("删除预告 周7 1");
        commit.handle("删除预告 周7 1");
        commit.handle("预告");
        commit.handle("菜单");
    }

    private final YG yg = new YG();

    public void handle(String s) {
        //添加日常赛
        if (s.startsWith("添加日常赛 周") && s.length() > 12) {
            char c = s.charAt(7);
            int day = WeekCaseUtil.weekCaseInt(c);
            if (day == 0) {
                return;
            }
            s = s.substring(10);
            yg.add(s, day);
            return;
        }
        //添加日常赛
        if (s.equals("预告")) {
            System.out.println(yg.toString());
            return;
        }
        //删除日常赛
        if (s.startsWith("删除预告 周") && s.length() == 9) {
            int day = WeekCaseUtil.weekCaseInt(s.charAt(6));
            if (day == 0) {
                return;
            }
            int index = s.charAt(8) - 48 - 1;
            if (index < 0) {
                return;
            }
            day--;
            yg.remove(day, index);
            return;
        }
        //添加日常赛
        if (s.equals("菜单")) {
            System.out.println("注意有空格\n1.添加预告 周<周几就填周几> <填内容>\n2.删除预告 周<周几就填周几> <填数字, 要删第几个就填数字几>\n3.预告");
        }
    }

}
