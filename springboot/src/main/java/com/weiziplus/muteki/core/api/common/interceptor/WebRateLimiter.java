package com.weiziplus.muteki.core.api.common.interceptor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 令牌桶限流
 *
 * @author wanglongwei
 * @date 2020/08/24 16/46
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@WebAuthToken
public @interface WebRateLimiter {

    /**
     * 每个用户,每个接口，每秒最大请求
     */
    double QPS() default 30D;

    /**
     * 获取令牌等待超时时间 默认:500
     */
    long timeout() default 1000L;

}