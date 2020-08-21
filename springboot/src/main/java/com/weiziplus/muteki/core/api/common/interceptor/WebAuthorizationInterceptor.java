package com.weiziplus.muteki.core.api.common.interceptor;

import com.weiziplus.muteki.common.config.GlobalConfig;
import com.weiziplus.muteki.common.interceptor.AuthTokenIgnore;
import com.weiziplus.muteki.common.result.ResultBean;
import com.weiziplus.muteki.common.util.HttpRequestUtils;
import com.weiziplus.muteki.common.util.JwtUtils;
import com.weiziplus.muteki.common.util.RedisUtils;
import com.weiziplus.muteki.core.api.common.token.WebTokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * @author wanglongwei
 * @date 2020/08/20 08/58
 */
@Slf4j
@Component
public class WebAuthorizationInterceptor implements HandlerInterceptor {


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
        return handleWebToken(request, response, object);
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
        //检查class或者方法是否有@WebAuthToken，没有的话跳过拦截
        WebAuthToken webAuthTokenClass = handlerMethod.getBeanType().getAnnotation(WebAuthToken.class);
        WebAuthToken webAuthTokenMethod = method.getAnnotation(WebAuthToken.class);
        return null == webAuthTokenClass && null == webAuthTokenMethod;
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
        return WebTokenUtils.createIssuer(request).equals(issuer);
    }

    /**
     * 处理web的token
     *
     * @param request
     * @param response
     * @param object
     * @return
     */
    private boolean handleWebToken(HttpServletRequest request, HttpServletResponse response, Object object) {
        //获取token
        String token = request.getHeader(GlobalConfig.TOKEN);
        //获取用户id
        Long userId = WebTokenUtils.getUserId();
        //查看redis是否过期
        if (!RedisUtils.hasKye(WebTokenUtils.createRedisKey(userId))) {
            HttpRequestUtils.handleErrorResponse(ResultBean.errorToken("token失效"));
            return false;
        }
        //查看redis中token是否是当前token
        if (!token.equals(RedisUtils.get(WebTokenUtils.createRedisKey(userId)))) {
            HttpRequestUtils.handleErrorResponse(ResultBean.errorToken("token失效"));
            return false;
        }
        WebTokenUtils.updateExpireTime(userId);
        return true;
    }

}