package com.weiziplus.muteki.common.util;

import java.time.*;
import java.time.format.DateTimeFormatter;

/**
 * 时间工具类
 *
 * @author wanglongwei
 * @date 2019/5/7 17:44
 */
public class DateUtils {

    /**
     * 获取当前时间
     *
     * @return
     */
    public static String getNowDateTime() {
        //设置时间格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return formatter.format(LocalDateTime.now());
    }

    /**
     * 获取当前日期
     *
     * @return
     */
    public static String getNowDate() {
        //设置时间格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return formatter.format(LocalDate.now());
    }

    /**
     * 获取当前日期
     *
     * @return
     */
    public static Integer getNowDateNum() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String format = formatter.format(LocalDate.now());
        return Integer.valueOf(format);
    }

    /**
     * 获取当前日期
     *
     * @return
     */
    public static Long getNowDateTimeNum() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String format = formatter.format(LocalDateTime.now());
        return Long.valueOf(format);
    }

    /**
     * 字符串转LocalDate
     *
     * @param date
     * @return
     */
    public static LocalDate parseLocalDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(date, formatter);
    }

    /**
     * 字符串转LocalDateTime
     *
     * @param dateTime
     * @return
     */
    public static LocalDateTime parseLocalDateTime(String dateTime) {
        String basePattern = "yyyy-MM-dd HH:mm:ss";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(basePattern.substring(0, dateTime.length()));
        return LocalDateTime.parse(dateTime, formatter);
    }

    /**
     * 字符串转LocalTime
     *
     * @param time
     * @return
     */
    public static LocalTime parseLocalTime(String time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        return LocalTime.parse(time, formatter);
    }

    /**
     * LocalDate转字符串
     *
     * @param localDate
     * @return
     */
    public static String formatDate(LocalDate localDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return formatter.format(localDate);
    }

    /**
     * LocalDateTime转字符串
     *
     * @param localDateTime
     * @return
     */
    public static String formatDateTime(LocalDateTime localDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return formatter.format(localDateTime);
    }

    /**
     * LocalTime转字符串
     *
     * @param localTime
     * @return
     */
    public static String formatTime(LocalTime localTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        return formatter.format(localTime);
    }

    /**
     * 指定时间是否为过去时间
     *
     * @param dateTime
     * @return
     */
    public static boolean isBeforeDateTime(String dateTime) {
        String basePattern = "yyyy-MM-dd HH:mm:ss";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(basePattern.substring(0, dateTime.length()));
        LocalDateTime localDateTime = LocalDateTime.parse(dateTime, formatter);
        return localDateTime.isBefore(LocalDateTime.now());
    }

    /**
     * 指定时间是否为未来时间
     *
     * @param dateTime
     * @return
     */
    public static boolean isAfterDateTime(String dateTime) {
        String basePattern = "yyyy-MM-dd HH:mm:ss";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(basePattern.substring(0, dateTime.length()));
        LocalDateTime localDateTime = LocalDateTime.parse(dateTime, formatter);
        return localDateTime.isAfter(LocalDateTime.now());
    }

    /**
     * 指定时间是否为过去日期
     *
     * @param date
     * @return
     */
    public static boolean isBeforeDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(date, formatter);
        return localDate.isBefore(LocalDate.now());
    }

    /**
     * 指定时间是否为当前日期
     *
     * @param date
     * @return
     */
    public static boolean isEqualsDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(date, formatter);
        return localDate.isEqual(LocalDate.now());
    }

    /**
     * 指定时间是否为未来日期
     *
     * @param date
     * @return
     */
    public static boolean isAfterDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(date, formatter);
        return localDate.isAfter(LocalDate.now());
    }

    /**
     * 计算开始时间和结束时间差
     *
     * @param startDateTime
     * @param endDateTime
     * @return
     */
    public static Duration betweenDuration(String startDateTime, String endDateTime) {
        LocalDateTime startLocalDateTime = parseLocalDateTime(startDateTime);
        LocalDateTime endLocalDateTime = parseLocalDateTime(endDateTime);
        return Duration.between(startLocalDateTime, endLocalDateTime);
    }

    /**
     * 计算开始日期和结束日期差
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static Period betweenPeriod(String startDate, String endDate) {
        LocalDate startLocalDate = parseLocalDate(startDate);
        LocalDate endLocalDate = parseLocalDate(endDate);
        return Period.between(startLocalDate, endLocalDate);
    }

}
