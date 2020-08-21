package com.weiziplus.muteki.core.api.common.token;

import com.weiziplus.muteki.common.util.JwtUtils;
import com.weiziplus.muteki.common.util.Md5Utils;
import com.weiziplus.muteki.common.util.UserAgentUtils;
import com.weiziplus.muteki.common.util.RedisUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * @author wanglongwei
 * @date 2020/08/20 08/50
 */
@Component
public class WebTokenUtils extends JwtUtils {

    /**
     * web用户redis过期时间--10小时过期
     */
    private static Long EXPIRE_TIME = 60L * 60 * 10;

    /**
     * token过期时间
     *
     * @param expireTime
     */
    @Value("${global.token.expire-time.web:36000}")
    private void setExpireTime(String expireTime) {
        EXPIRE_TIME = Long.valueOf(expireTime);
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
    public static String createToken(Long userId, HttpServletRequest request, WebJwtExpand expand) {
        String issuer = createIssuer(request);
        String token = JwtUtils.createToken(String.valueOf(userId), issuer, expand);
        RedisUtils.set(createRedisKey(userId), token, EXPIRE_TIME);
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
     * 根据web用户id删除token
     *
     * @param userId
     * @return
     */
    public static Boolean deleteToken(Long userId) {
        return RedisUtils.delete(createRedisKey(userId));
    }

    /**
     * 根据web用户id删除token
     *
     * @param userId
     * @return
     */
    public static Boolean updateExpireTime(Long userId) {
        return RedisUtils.expire(createRedisKey(userId), EXPIRE_TIME);
    }

}