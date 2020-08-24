package com.weiziplus.muteki.core.pc.common.interceptor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 令牌桶接口限流
 *
 * @author wanglongwei
 * @date 2020/08/24 16/23
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@PcAuthToken
public @interface PcRateLimiter {

    /**
     * 每个用户,每个接口，每秒最大请求
     */
    double QPS() default 20D;

    /**
     * 获取令牌等待超时时间 默认:500
     */
    long timeout() default 500L;

}