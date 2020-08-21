package com.weiziplus.muteki.core.pc.common.token;

import com.weiziplus.muteki.common.util.JwtUtils;
import com.weiziplus.muteki.common.util.Md5Utils;
import com.weiziplus.muteki.common.util.UserAgentUtils;
import com.weiziplus.muteki.common.util.RedisUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * pc-token处理
 *
 * @author wanglongwei
 * @date 2020/07/30 15/31
 */
@Component
public class PcTokenUtils extends JwtUtils {

    /**
     * 系统用户redis过期时间--10小时过期
     */
    private static Long EXPIRE_TIME = 60L * 60 * 10;

    /**
     * token过期时间
     *
     * @param expireTime
     */
    @Value("${global.token.expire-time.admin:36000}")
    private void setExpireTime(String expireTime) {
        EXPIRE_TIME = Long.valueOf(expireTime);
    }

    /**
     * pc的redis
     *
     * @param userId
     * @return
     */
    public static String createRedisKey(Integer userId) {
        return "pc:token:" + userId;
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
     * 根据系统用户id创建token
     *
     * @param userId
     * @return
     */
    public static String createToken(Integer userId, HttpServletRequest request, PcJwtExpand expand) {
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
    public static PcJwtExpand getExpand() {
        return getExpand(PcJwtExpand.class);
    }

    /**
     * 获取用户id
     *
     * @return
     */
    public static Integer getUserId() {
        String id = getId();
        return Integer.valueOf(id);
    }

    /**
     * 根据系统用户id删除token
     *
     * @param userId
     * @return
     */
    public static Boolean deleteToken(Integer userId) {
        return RedisUtils.delete(createRedisKey(userId));
    }

    /**
     * 根据系统用户id删除token
     *
     * @param userId
     * @return
     */
    public static Boolean updateExpireTime(Integer userId) {
        return RedisUtils.expire(createRedisKey(userId), EXPIRE_TIME);
    }

}