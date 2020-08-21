package com.weiziplus.muteki.common.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

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
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        return dateFormat.format(new Date());
    }

    /**
     * 获取当前日期
     *
     * @return
     */
    public static String getNowDate() {
        //设置时间格式
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        return dateFormat.format(new Date());
    }

    /**
     * 获取当前日期
     *
     * @return
     */
    public static Integer getNowDateNum() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        String format = dateFormat.format(new Date());
        return Integer.valueOf(format);
    }

    /**
     * 获取当前日期
     *
     * @return
     */
    public static Long getNowDateTimeNum() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        String format = dateFormat.format(new Date());
        return Long.valueOf(format);
    }

}
