package com.weiziplus.muteki.common.util;

import com.weiziplus.muteki.common.config.GlobalConfig;

import java.util.*;

/**
 * 常用工具类
 *
 * @author wanglongwei
 * @date 2020/07/30 11/35
 */
public class ToolUtils {

    /**
     * 创建集合初始化容量
     *
     * @param needNum
     * @return
     */
    public static int initialCapacity(int needNum) {
        //负载因子
        float loaderFactor = 0.75F;
        return (int) ((float) needNum * loaderFactor + 1);
    }

    /**
     * 字符串为null或""或"undefined"或"null"
     *
     * @param str
     * @return
     */
    public static boolean isBlank(String str) {
        if (null == str) {
            return true;
        }
        str = str.trim();
        return 0 >= str.length() || GlobalConfig.UNDEFINED.equals(str) || GlobalConfig.NULL.equals(str);
    }

    /**
     * 将object对象转为list
     *
     * @param obj
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> List<T> objectOfList(Object obj, Class<T> clazz) {
        if (null == obj || null == clazz) {
            return null;
        }
        List<T> result = new ArrayList<>();
        if (obj instanceof List<?>) {
            for (Object o : (List<?>) obj) {
                result.add(clazz.cast(o));
            }
            return result;
        }
        return null;
    }

    /**
     * 将object对象转为set
     *
     * @param obj
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> Set<T> objectOfSet(Object obj, Class<T> clazz) {
        if (null == obj || null == clazz) {
            return null;
        }
        Set<T> result = new HashSet<>();
        if (obj instanceof Set<?>) {
            for (Object o : (Set<?>) obj) {
                result.add(clazz.cast(o));
            }
            return result;
        }
        return null;
    }

    /**
     * 生成uuid
     *
     * @return
     */
    public static String createUUID() {
        return UUID.randomUUID().toString().replace("-", "").toUpperCase();
    }

}
