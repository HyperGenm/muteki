package com.weiziplus.muteki.common.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.weiziplus.muteki.common.config.GlobalConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * jwt工具
 *
 * @author wanglongwei
 * @date 2020/07/30 15/22
 */
public class JwtUtils {

    /**
     * 加密方式
     */
    private static final SignatureAlgorithm HS512 = SignatureAlgorithm.HS512;
    /**
     * 秘钥
     */
    private static final String SECRET = "weiziplus";
    /**
     * 过期时间--30天过期
     */
    private static final long EXPIRATION = 1000L * 60 * 60 * 24 * 30;

    /**
     * 创建token
     *
     * @param userId
     * @param issuer
     * @param expand
     * @return
     */
    protected static String createToken(String userId, String issuer, Object expand) {
        return Jwts.builder()
                //用户id
                .setId(Base64Utils.encode(userId))
                //issuer
                .setIssuer(issuer)
                //签名算法
                .signWith(HS512, SECRET)
                //存放自定义内容
                .setSubject(Base64Utils.encode(JSON.toJSONString(expand)))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .setIssuedAt(new Date())
                .compact();
    }

    /**
     * 获取token中的issuer
     *
     * @param token
     * @return
     */
    public static String getIssuer(String token) {
        return getTokenBody(token).getIssuer();
    }

    /**
     * 根据token判断是否失效
     *
     * @param token
     * @return
     */
    public static Boolean isExpiration(String token) {
        return getTokenBody(token).getExpiration().before(new Date());
    }

    /**
     * 根据token获取token中的信息
     *
     * @param token
     * @return
     */
    private static Claims getTokenBody(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 根据token获取token中的信息
     *
     * @return
     */
    private static Claims getTokenBody() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = request.getHeader(GlobalConfig.TOKEN);
        return Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 获取用户id
     *
     * @return
     */
    protected static String getId() {
        String id = getTokenBody().getId();
        return Base64Utils.decode(id);
    }

    /**
     * 获取自定义内容
     *
     * @param expand
     * @param <T>
     * @return
     */
    protected static <T> T getExpand(Class<T> expand) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = request.getHeader(GlobalConfig.TOKEN);
        return JSONObject.parseObject(Base64Utils.decode(getTokenBody(token).getSubject()), expand);
    }

}
