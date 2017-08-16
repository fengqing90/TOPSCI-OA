package com.topsci.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateTimeUtils {

    public final static Calendar cal = Calendar.getInstance();

    public final static SimpleDateFormat dateTimeFormat = new SimpleDateFormat(
        "yyyy-MM-dd HH:mm:ss");
    public final static SimpleDateFormat timeDateFormat = new SimpleDateFormat(
        "yyyy-MM-dd HH:mm");
    public final static SimpleDateFormat dateTimeHMFormat = new SimpleDateFormat(
        "yyyy-MM-dd HHmm");
    public final static SimpleDateFormat dateFormat = new SimpleDateFormat(
        "yyyy-MM-dd");
    public final static SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
        "yyyyMMdd");
    public final static SimpleDateFormat timeFormat = new SimpleDateFormat(
        "HH:mm");
    public final static SimpleDateFormat HHmmssFormat = new SimpleDateFormat(
        "HH:mm:ss");
    public final static SimpleDateFormat MMddFormat = new SimpleDateFormat(
        "MM-dd");
    public final static SimpleDateFormat EFormat = new SimpleDateFormat("E");

    /**
     * @return 明日是周几 星期一到七(1-7)
     */
    public static int getNextDayOfweek() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(
            new java.util.Date(new Date().getTime() + 24 * 60 * 60 * 1000));
        if (calendar.get(Calendar.DAY_OF_WEEK) == 1) {
            return 7;
        } else {
            return calendar.get(Calendar.DAY_OF_WEEK) - 1;
        }
    }

    /**
     * @return 明天的日期
     */
    public static String getNextDay() {
        String operationtime = DateTimeUtils.dateFormat
            .format(new Date().getTime() + 24 * 60 * 60 * 1000);
        return operationtime;
    }

    /**
     * @return 今天的日期
     */
    public static String getToday() {
        String operationtime = DateTimeUtils.dateFormat.format(new Date());
        return operationtime;

    }

    /**
     * @return 现在的时间 默认的系统格式
     */
    public static Date getNowTime() {
        return new Date();
    }

    /**
     * @return 获取明天的时间
     */
    public static Date getTomorrowmTime() {
        Date tomorrow = DateTimeUtils.validateDate(DateTimeUtils.getNextDay());
        return tomorrow;
    }

    /**
     * @param date
     * @return "yyyy-MM-dd" 格式的字符串
     */
    public static String validateDate(Date date) {
        return date == null ? null : DateTimeUtils.dateFormat.format(date);
    }

    /**
     * @param date
     * @return "yyyyMMdd" 格式的字符串
     */
    public static String validateSimpleDate(Date date) {
        return date == null ? null
            : DateTimeUtils.simpleDateFormat.format(date);
    }

    /**
     * @param date
     * @return "HH:mm" 格式的字符串
     */
    public static String validateTime(Date date) {
        return date == null ? null : DateTimeUtils.timeFormat.format(date);
    }

    /**
     * @param date
     * @return "yyyy-MM-dd HH:mm:ss" 格式的字符串
     */
    public static String validateDateTime(Date date) {
        return date == null ? null : DateTimeUtils.dateTimeFormat.format(date);
    }

    /**
     * @param date
     * @return "yyyy-MM-dd HH:mm" 格式的字符串
     */
    public static String validateDateTimeHM(Date date) {
        return date == null ? null
            : DateTimeUtils.dateTimeHMFormat.format(date);
    }

    /**
     * @param dateStr
     *        String 格式为"yyyy-MM-dd"
     * @return 日期
     */
    public static Date validateDate(String dateStr) {
        try {
            return dateStr == null || dateStr.equals("") ? null
                : DateTimeUtils.dateFormat.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @describle dateStr String 格式为“yyyy-MM-dd HH:mm”
     * @param dateStr
     * @return
     */
    public static Date validateDateTimeStr(String dateStr) {
        try {
            return dateStr == null || dateStr.equals("") ? null
                : DateTimeUtils.timeDateFormat.parse(dateStr);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * @param dateStr
     *        String 格式为"HH:mm"
     * @return 日期
     */
    public static Date validateTime(String dateStr) {
        try {
            return dateStr == null || dateStr.equals("") ? null
                : DateTimeUtils.timeFormat.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Date validateTime(Date date, String dateStr) {
        try {
            if (dateStr == null || dateStr.equals("")) {
                return null;
            }
            Date newDate = DateTimeUtils.timeFormat.parse(dateStr);
            newDate.setYear(date.getYear());
            newDate.setMonth(date.getMonth());
            newDate.setDate(date.getDate());
            return newDate;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @param dateStr
     *        String 格式为"yyyy-MM-dd HH:mm:ss"
     * @return 日期
     */
    public static Date validateDateTime(String dateStr) {
        try {
            return dateStr == null || dateStr.equals("") ? null
                : DateTimeUtils.dateTimeFormat.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @param datestr
     *        "yyyy-MM-dd HHmm" 的字符串;
     * @return
     */
    public static Date validateDateTimeHM(String datestr) {
        try {
            return datestr == null || datestr.equals("") ? null
                : DateTimeUtils.dateTimeHMFormat.parse(datestr);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 计算两个时间之间的分钟数 如果start在end之后，则返回start减去end的差值
     *
     * @param start
     * @param end
     * @return
     */
    public static int getIntervalMinitue(Date start, Date end) {
        if (start.after(end)) {
            Date cal = start;
            start = end;
            end = cal;
        }
        long sl = start.getTime();
        long el = end.getTime();
        long l = el - sl;

        return (int) l / (1000 * 60);
    }

    /**
     * 根据分钟计算相应的小时数，用于乘法或者除法计算。 每60分钟为一个小时，30分钟为0.5个小时
     * 当余数小于半个小时时，舍去，当余数大于半个小时时，加1
     *
     * @param minitue
     * @return
     */
    public static int getCalculatedHours(int minitue) {
        int mast = minitue / 60;
        int rest = minitue % 60;
        if (rest >= 30) {
            mast = mast + 1;
        }
        return mast;
    }

    /**
     * 返回指定日期的小时
     *
     * @param date
     * @return
     */
    public static int getHour(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.HOUR_OF_DAY);
    }
}
