package com.weiziplus.muteki.core.pc.common.interceptor;

import com.google.common.collect.Maps;
import com.google.common.util.concurrent.RateLimiter;
import com.weiziplus.muteki.common.config.GlobalConfig;
import com.weiziplus.muteki.common.interceptor.AuthTokenIgnore;
import com.weiziplus.muteki.common.result.ResultBean;
import com.weiziplus.muteki.common.util.*;
import com.weiziplus.muteki.core.pc.common.config.PcGlobalConfig;
import com.weiziplus.muteki.core.pc.common.token.PcTokenUtils;
import com.weiziplus.muteki.core.pc.system.service.SysRoleFunctionService;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author wanglongwei
 * @date 2020/07/30 14/56
 */
@Slf4j
@Component
public class PcAuthorizationInterceptor implements HandlerInterceptor {

    @Autowired
    SysRoleFunctionService sysRoleFunctionService;

    /**
     * 使用url和用户id做为key,存放令牌桶 防止每次重新创建令牌桶
     */
    private final Map<String, RateLimiter> limitMap = Maps.newConcurrentMap();

    /**
     * 检验时间戳
     *
     * @param request
     * @param response
     * @param object
     * @return
     */
    @Value("${global.interceptor.timestamp:true}")
    private Boolean checkTimeStamp;

    /**
     * 请求之前拦截
     *
     * @param request
     * @param response
     * @param object
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) {
        // 如果不是映射到方法直接通过
        if (!(object instanceof HandlerMethod)) {
            return true;
        }
        //判断时间戳有效
        if (!handleTimeStamp(request)) {
            HttpRequestUtils.handleErrorResponse(ResultBean.error("时间戳错误"));
            return false;
        }
        //判断是否存在token注解
        if (handleTokenAnnotation(object)) {
            return true;
        }
        //判断请求头中token是否失效
        if (!handleJwtTokenNotExpiration(request)) {
            HttpRequestUtils.handleErrorResponse(ResultBean.errorToken("token失效"));
            return false;
        }
        return handleAdminToken(request, response, object);
    }

    /**
     * 处理请求的时间戳
     * 时间戳错误返回false
     *
     * @param request
     */
    protected boolean handleTimeStamp(HttpServletRequest request) {
        //如果不是生产环境，并且开启了不检验时间戳
        if (!GlobalConfig.isSpringProfilesPro()
                && !checkTimeStamp) {
            return true;
        }
        String timeStamp = request.getParameter("__t");
        if (null == timeStamp || 0 >= timeStamp.length()) {
            return false;
        }
        long timeMillis = System.currentTimeMillis();
        long timeStampLong = Long.parseLong(timeStamp);
        int allowTime = 60000;
        //如果请求时间戳和服务器当前时间相差超过60秒，本次请求失败
        return allowTime > Math.abs(timeMillis - timeStampLong);
    }

    /**
     * 判断token注解
     *
     * @param object
     * @return
     */
    private boolean handleTokenAnnotation(Object object) {
        HandlerMethod handlerMethod = (HandlerMethod) object;
        Method method = handlerMethod.getMethod();
        //如果有忽略token注解
        if (null != handlerMethod.getBeanType().getAnnotation(AuthTokenIgnore.class)
                || null != method.getAnnotation(AuthTokenIgnore.class)) {
            return true;
        }
        //检查class或者方法是否有@AdminAuthToken和@WebAuthToken，没有的话跳过拦截
        PcAuthToken pcAuthTokenClass = handlerMethod.getBeanType().getAnnotation(PcAuthToken.class);
        PcAuthToken pcAuthTokenMethod = method.getAnnotation(PcAuthToken.class);
        return null == pcAuthTokenClass && null == pcAuthTokenMethod;
    }

    /**
     * 判断token是否过期
     *
     * @param request
     * @return
     */
    private boolean handleJwtTokenNotExpiration(HttpServletRequest request) {
        //获取头部的token
        String token = request.getHeader(GlobalConfig.TOKEN);
        if (null == token || 0 >= token.trim().length()) {
            return false;
        }
        try {
            //判断jwtToken是否过期
            if (JwtUtils.isExpiration(token)) {
                return false;
            }
        } catch (Exception e) {
            log.warn("拦截器判断jwtToken是否过期出错，详情:" + e);
            return false;
        }
        //获取token中存放的issuer
        String issuer = JwtUtils.getIssuer(token);
        return PcTokenUtils.createIssuer(request).equals(issuer);
    }

    /**
     * 处理admin的token
     *
     * @param request
     * @param response
     * @param object
     * @return
     */
    private boolean handleAdminToken(HttpServletRequest request, HttpServletResponse response, Object object) {
        //获取token
        String token = request.getHeader(GlobalConfig.TOKEN);
        //获取用户id
        Integer userId = PcTokenUtils.getUserId();
        //查看redis是否过期
        if (!RedisUtils.hasKye(PcTokenUtils.createRedisKey(userId))) {
            HttpRequestUtils.handleErrorResponse(ResultBean.errorToken("token失效"));
            return false;
        }
        //查看redis中token是否是当前token
        if (!token.equals(RedisUtils.get(PcTokenUtils.createRedisKey(userId)))) {
            HttpRequestUtils.handleErrorResponse(ResultBean.errorToken("token失效"));
            return false;
        }
        //判断是否触发限流
        if (!handleRateLimiter(request, response, object)) {
            HttpRequestUtils.handleErrorResponse(ResultBean.error("访问频率过快,请稍后再试"));
            return false;
        }
        //如果当前是超级管理员，直接放过
        if (PcGlobalConfig.SUPER_ADMIN_USER_ID.equals(userId)) {
            //更新token过期时间
            PcTokenUtils.updateExpireTime(userId);
            return true;
        }
        //检查class或者方法是否有@PcSuperAdminAuth注解
        HandlerMethod handlerMethod = (HandlerMethod) object;
        Method method = handlerMethod.getMethod();
        PcSuperAdminAuth pcSuperAdminAuthTokenClass = handlerMethod.getBeanType().getAnnotation(PcSuperAdminAuth.class);
        PcSuperAdminAuth pcSuperAdminAuthTokenMethod = method.getAnnotation(PcSuperAdminAuth.class);
        //如果有超级管理员注解
        if (null != pcSuperAdminAuthTokenClass || null != pcSuperAdminAuthTokenMethod) {
            HttpRequestUtils.handleErrorResponse(ResultBean.errorRole("您没有权限"));
            return false;
        }
        //获取当前访问的url
        String requestUrl = request.getRequestURI();
        //如果有统一的请求前缀
        String servletContextPath = GlobalConfig.getServletContextPath();
        if (!ToolUtils.isBlank(servletContextPath)) {
            //如果是请求前缀开头的
            if (requestUrl.startsWith(servletContextPath)) {
                requestUrl = requestUrl.substring(servletContextPath.length());
            }
        }
        Set<String> allFunContainApiSet = sysRoleFunctionService.getAllFunContainApi();
        //如果功能api不包括当前url,直接放过
        if (null == allFunContainApiSet || !allFunContainApiSet.contains(requestUrl)) {
            //更新token过期时间
            PcTokenUtils.updateExpireTime(userId);
            return true;
        }
        Set<String> funContainApiByRoleIds = sysRoleFunctionService.getFunContainApiByRoleIds(PcTokenUtils.getExpand().getRoleIds());
        //如果没有该功能api
        if (null == funContainApiByRoleIds || funContainApiByRoleIds.contains(requestUrl)) {
            HttpRequestUtils.handleErrorResponse(ResultBean.errorToken("token失效"));
            return false;
        }
        return true;
    }

    /**
     * 令牌桶根据用户id和url对请求限流
     *
     * @param request
     * @param response
     * @param object
     * @return
     */
    private boolean handleRateLimiter(HttpServletRequest request, HttpServletResponse response, Object object) {
        HandlerMethod handlerMethod = (HandlerMethod) object;
        Method method = handlerMethod.getMethod();
        //查看是否注解
        PcRateLimiter pcRateLimiter = method.getAnnotation(PcRateLimiter.class);
        //默认每个用户,每个接口，每秒最大请求
        double qps = 20D;
        //获取令牌等待超时时间
        long timeout = 500L;
        if (null != pcRateLimiter) {
            qps = pcRateLimiter.QPS();
            timeout = pcRateLimiter.timeout();
        } else {
            //如果没有注解，并且请求方式不是get请求
            if (!HttpMethod.GET.name().equals(request.getMethod())) {
                qps = 3D;
                timeout = 200L;
            }
        }
        //获取用户id
        Integer userId = PcTokenUtils.getUserId();
        //以用户id和url作为维度
        String limitKey = Md5Utils.encode16(userId + ":" + request.getRequestURI());
        RateLimiter limiter;
        if (!limitMap.containsKey(limitKey)) {
            limiter = RateLimiter.create(qps);
            limitMap.put(limitKey, limiter);
        } else {
            limiter = limitMap.get(limitKey);
        }
        //获取令牌
        return limiter.tryAcquire(timeout, TimeUnit.MILLISECONDS);
    }

}