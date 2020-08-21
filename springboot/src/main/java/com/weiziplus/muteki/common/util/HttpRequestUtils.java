package com.weiziplus.muteki.common.util;

import com.alibaba.fastjson.JSON;
import com.weiziplus.muteki.common.result.ResultBean;
import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

/**
 * @author wanglongwei
 * @date 2020/07/30 15/08
 */
@Slf4j
public class HttpRequestUtils {

    /**
     * 获取用户ip地址
     *
     * @param request
     * @return
     */
    public static String getIpAddress(HttpServletRequest request) {
        String unknown = "unknown";
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
            if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_CLIENT_IP");
            }
            if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            }
            if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
                if (null != ip) {
                    ip = "0:0:0:0:0:0:0:1".equals(ip) ? "127.0.0.1" : ip;
                }
            }
            return ip;
        }
        //ip地址最大长度为15
        int ipMaxNum = 15;
        if (ipMaxNum < ip.length()) {
            String[] ips = ip.split(",");
            for (String strIp : ips) {
                if (!(unknown.equalsIgnoreCase(strIp))) {
                    ip = strIp;
                    break;
                }
            }
        }
        return ip;
    }

    /**
     * 获取userAgent
     *
     * @return
     */
    public static String getUserAgent() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return request.getHeader(HttpHeaders.USER_AGENT);
    }

    /**
     * 将错误信息输出到前端页面
     *
     * @param resultBean
     */
    public static void handleErrorResponse(ResultBean<?> resultBean) {
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        try {
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            response.setContentType("application/json;charset=utf-8");
            @Cleanup PrintWriter out = response.getWriter();
            out.print(JSON.toJSONString(resultBean));
        } catch (Exception e) {
            log.warn("将错误信息输出到前端页面，详情:" + e);
        }
    }
}