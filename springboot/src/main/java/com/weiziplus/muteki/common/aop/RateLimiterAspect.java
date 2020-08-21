package com.weiziplus.muteki.common.aop;

import com.google.common.collect.Maps;
import com.google.common.util.concurrent.RateLimiter;
import com.weiziplus.muteki.common.base.BaseService;
import com.weiziplus.muteki.common.result.ResultBean;
import com.weiziplus.muteki.common.result.ResultEnum;
import com.weiziplus.muteki.common.util.HttpRequestUtils;
import com.weiziplus.muteki.common.util.Md5Utils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 根据ip地址限制每个接口的访问频率
 *
 * @author wanglongwei
 * @date 2020/08/18 15/09
 */
@Slf4j
@Aspect
@Component
public class RateLimiterAspect extends BaseService {

    /**
     * 使用url做为key,存放令牌桶 防止每次重新创建令牌桶
     */
    private final Map<String, RateLimiter> limitMap = Maps.newConcurrentMap();

    /**
     * 所有MyRateLimiter注解
     */
    @Pointcut("@annotation(com.weiziplus.muteki.common.aop.MyRateLimiter)")
    public void myRateLimiter() {
    }

    @Around("myRateLimiter()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        // 获取request,response
        HttpServletRequest request = getRequest();
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        //查看是否注解
        MyRateLimiter myRateLimiter = method.getAnnotation(MyRateLimiter.class);
        if (null == myRateLimiter) {
            return joinPoint.proceed();
        }
        //以ip和url作为维度
        String key = Md5Utils.encode16(HttpRequestUtils.getIpAddress(request) + request.getRequestURI());
        RateLimiter limiter;
        if (!limitMap.containsKey(key)) {
            limiter = RateLimiter.create(myRateLimiter.QPS());
            limitMap.put(key, limiter);
        } else {
            limiter = limitMap.get(key);
        }
        //获取令牌
        boolean acquire = limiter.tryAcquire(myRateLimiter.timeout(), TimeUnit.MILLISECONDS);
        if (acquire) {
            return joinPoint.proceed();
        }
        HttpRequestUtils.handleErrorResponse(ResultBean.error(ResultEnum.ERROR_FAST, myRateLimiter.msg()));
        return null;
    }

}
