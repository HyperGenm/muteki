package com.weiziplus.muteki.core.api.common.token;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.weiziplus.muteki.common.util.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author wanglongwei
 * @date 2020/08/20 08/50
 */
@Component
public class WebTokenUtils extends JwtUtils {

    /**
     * web用户redis过期时间--10小时过期
     */
    private static Long EXPIRE_TIME = 60L * 60 * 24;

    /**
     * token过期时间
     *
     * @param expireTime
     */
    @Value("${global.token.expire-time.web:86400}")
    private void setExpireTime(String expireTime) {
        EXPIRE_TIME = Long.valueOf(expireTime);
    }

    /**
     * 获取token过期时间,单位 秒
     *
     * @return
     */
    public static Long getExpireTime() {
        return WebTokenUtils.EXPIRE_TIME;
    }

    /**
     * web的redis
     *
     * @param userId
     * @return
     */
    public static String createRedisKey(Long userId) {
        return "web:token:" + userId;
    }

    /**
     * 创建issuer
     *
     * @param request
     * @return
     */
    public static String createIssuer(HttpServletRequest request) {
        String borderName = UserAgentUtils.getBorderName(request);
        String osName = UserAgentUtils.getOsName(request);
        String browserVersion = UserAgentUtils.getBrowserVersion(request);
        return Md5Utils.encode16(borderName + osName + browserVersion);
    }

    /**
     * 根据web用户id创建token
     *
     * @param userId
     * @param request
     * @param expand
     * @return
     */
    public static String createToken(WebTerminalEnum terminalEnum, Long userId, HttpServletRequest request, WebJwtExpand expand) {
        String issuer = createIssuer(request);
        expand.setTerminalEnum(terminalEnum);
        String token = JwtUtils.createToken(String.valueOf(userId), issuer, expand);
        String redisKey = createRedisKey(userId);
        Object object = RedisUtils.get(redisKey);
        Map<String, WebTokenModel> tokenMap;
        if (null == object) {
            tokenMap = new HashMap<>(ToolUtils.initialCapacity(1));
        } else {
            tokenMap = (Map<String, WebTokenModel>) object;
        }
        WebTokenModel webTokenModel = new WebTokenModel()
                .setToken(token)
                .setLastTimeStamp(System.currentTimeMillis());
        tokenMap.put(terminalEnum.getName(), webTokenModel);
        RedisUtils.set(createRedisKey(userId), tokenMap, EXPIRE_TIME);
        return token;
    }

    /**
     * 获取扩展部分
     *
     * @return
     */
    public static WebJwtExpand getExpand() {
        return getExpand(WebJwtExpand.class);
    }

    /**
     * 获取用户id
     *
     * @return
     */
    public static Long getUserId() {
        String id = getId();
        return Long.valueOf(id);
    }

    /**
     * 删除用户所有的token
     *
     * @param userId
     * @return
     */
    public static Boolean deleteAllToken(Long userId) {
        return RedisUtils.delete(createRedisKey(userId));
    }

    /**
     * 根据web用户id删除token
     *
     * @param userId
     * @return
     */
    public static Boolean deleteToken(WebTerminalEnum terminalEnum, Long userId) {
        String redisKey = createRedisKey(userId);
        Object object = RedisUtils.get(redisKey);
        //如果没有tokenMap
        if (null == object) {
            return true;
        }
        //取出存放的tokenMap
        Map<String, WebTokenModel> tokenMap = (Map<String, WebTokenModel>) object;
        //删除当前终端token
        tokenMap.entrySet().removeIf(entry -> entry.getKey().equals(terminalEnum.getName()));
        if (0 >= tokenMap.entrySet().size()) {
            RedisUtils.delete(redisKey);
            return true;
        }
        RedisUtils.setNotChangeTimeOut(redisKey, tokenMap);
        return true;
    }

}