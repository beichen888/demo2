package com.demo2.common;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by miguo on 2018/6/29.
 */
public class DateUtil {

    public static Date getTodayFirstSecond() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }

    public static Date getTodayLastSecond() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        return calendar.getTime();
    }

    public static Date getPreDayFirstSecond() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int day = calendar.get(Calendar.DATE);
        calendar.set(Calendar.DATE, day - 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }

    public static Date getPreDayLastSecond() {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DATE);
        calendar.set(Calendar.DATE, day - 1);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        return calendar.getTime();
    }

    /**
     * 获取过去第几天的日期
     *
     * @param past
     * @return
     */
    public static String getPastDate(int past) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - past);
        Date today = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String result = format.format(today);
        return result;
    }


    /**
     * 获取过去第几周的周一
     *
     * @param weekCnt
     * @return
     */
    public static String getPastWeekMonday(int weekCnt) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getThisWeekMonday(new Date()));
        cal.add(Calendar.DATE, -7 * weekCnt);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(cal.getTime());
    }

    /**
     * 获取过去第几周的周日
     *
     * @param weekCnt
     * @return
     */
    public static String getPastWeekSunday(int weekCnt) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getThisWeekSunday(new Date()));
        cal.add(Calendar.DATE, -7 * weekCnt);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(cal.getTime());
    }


    /**
     * 取得本周一
     *
     * @param date
     * @return
     */
    public static Date getThisWeekMonday(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        // 获得当前日期是一个星期的第几天
        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);
        if (1 == dayWeek) {
            cal.add(Calendar.DAY_OF_MONTH, -1);
        }
        // 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        // 获得当前日期是一个星期的第几天
        int day = cal.get(Calendar.DAY_OF_WEEK);
        // 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
        cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);
        return cal.getTime();
    }

    /**
     * 取得本周日
     *
     * @param date
     * @return
     */
    public static Date getThisWeekSunday(Date date) {
        Calendar cal = Calendar.getInstance();
        // 本周一
        cal.setTime(getThisWeekMonday(new Date()));
        // 增加六天,得到本周日
        cal.add(Calendar.DATE, 6);
        return cal.getTime();
    }

    /**
     * 取得过去第几个月的第一天
     *
     * @param monthCnt
     * @return
     */
    public static String getPastMonthFirstDay(int monthCnt) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1 * monthCnt);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(calendar.getTime());
    }

    public static String getPastMonthLastDay(int monthCnt) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1 * monthCnt + 1);
        calendar.set(Calendar.DAY_OF_MONTH, 0);
        calendar.add(Calendar.DATE, 0);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(calendar.getTime());
    }

}
