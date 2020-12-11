package com.jgxq.common.utils;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

/**
 * @Author: lih
 * @Date: 2019/3/5 19:18
 * @Version 1.0
 */
public class DateUtils {


    /**
     * yyyy-mm-dd字符串 得到下一天的凌晨
     * <p>
     * mfc
     *
     * @param str
     * @return
     */
    public static Date getTomorrowDateByStr(String str) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            ParsePosition position = new ParsePosition(0);
            Date parse = formatter.parse(str, position);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(parse);
            calendar.add(Calendar.DATE, 1);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            return calendar.getTime();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * yyyy-mm-dd字符串 得到当天的凌晨
     * <p>
     * mfc
     *
     * @param str
     * @return
     */
    public static Date getTodayDateByStr(String str) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            ParsePosition position = new ParsePosition(0);
            Date parse = formatter.parse(str, position);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(parse);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            return calendar.getTime();
        } catch (Exception e) {
            return null;
        }
    }


    public static Date localDateToDate(LocalDate localDate) {
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = localDate.atStartOfDay().atZone(zone).toInstant();
        Date date = Date.from(instant);
        return date;
    }

    public static LocalDate dateToLocalDate(Date date) {
        Instant instant = date.toInstant();
        ZoneId zone = ZoneId.systemDefault();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zone);
        LocalDate localDate = localDateTime.toLocalDate();
        return localDate;
    }

    public static LocalDateTime dateToLocalDateTime(Date date) {
        Instant instant = date.toInstant();
        ZoneId zone = ZoneId.systemDefault();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zone);
        return localDateTime;
    }

    public static Date localDateTimeToDate(LocalDateTime localDateTime) {
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = localDateTime.atZone(zone).toInstant();
        Date date = Date.from(instant);
        return date;
    }

    /**
     * 获取当前时间0点
     *
     * @return
     */
    public static Date initDateByDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * 获取指定时间0点
     *
     * @return
     */
    public static Date initDateByDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * 获取当前时间的下一天 的 0点
     * mfc
     *
     * @return
     */
    public static Date getTomorrowDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * 通过时间秒毫秒数判断两个时间的间隔
     *
     * @param date1
     * @param date2
     * @return
     */
    public static Integer differentDaysByMillisecond(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            return null;
        }
        int days = (int) ((date2.getTime() - date1.getTime()) / (1000 * 3600 * 24));
        return days;
    }

    /**
     * 增加天数，得到一个新的日期
     *
     * @param date
     * @param day
     * @return
     * @throws Exception
     */
    public static Date addDate(Date date, long day) {
        long time = date.getTime();
        day = day * 24 * 60 * 60 * 1000;
        time += day;
        return new Date(time);
    }

    /**
     * 日期转为yyyy-MM-dd HH:mm:ss字符串
     *
     * @param date
     * @return
     */
    public static String getDate(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(date);
        return dateString;
    }

    /**
     * 随机生成两个日期间的时间
     *
     * @param beginDate
     * @param endDate
     * @return
     */
    public static Date randomDate(String beginDate, String endDate) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date start = format.parse(beginDate);
            Date end = format.parse(endDate);
            if (start.getTime() >= end.getTime()) {
                return null;
            }
            long date = random(start.getTime(), end.getTime());
            return new Date(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param begin
     * @param end
     * @return
     */
    public static long random(long begin, long end) {
        long rtn = begin + (long) (Math.random() * (end - begin + 1));
        return rtn;
    }

}
