package com.weiziplus.muteki.common.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 令牌桶限流
 *
 * @author wanglongwei
 * @date 2020/08/18 15/04
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface MyRateLimiter {

    /**
     * 每个ip，针对每个接口每秒创建令牌个数，默认:200
     */
    double QPS() default 200D;

    /**
     * 获取令牌等待超时时间 默认:500
     */
    long timeout() default 500L;

    /**
     * 无法获取令牌返回提示信息
     */
    String msg() default "服务器繁忙，请稍后再试！";

}
