package com.weiziplus.muteki.amap.ip;

import com.alibaba.fastjson.JSONObject;
import com.weiziplus.muteki.amap.AmapConfig;
import com.weiziplus.muteki.common.result.ResultBean;
import com.weiziplus.muteki.common.util.RedisUtils;
import com.weiziplus.muteki.common.util.ToolUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.RestTemplate;

/**
 * @author wanglongwei
 * @date 2020/11/19 13/56
 */
@Slf4j
public class AmapIpApi {

    /**
     * redis的前缀
     */
    private final static String REDIS_PREFIX = "amapApi:ip:";

    /**
     * 根据ip获取用户所在城市省份
     *
     * @param ip
     * @return
     */
    public static ResultBean<AmapIp> getLocation(String ip) {
        if (ToolUtils.isBlank(ip)) {
            return ResultBean.error("ip不能为空");
        }
        //redis的key
        String redisKey = REDIS_PREFIX + "ipLocation:" + ip;
        Object object = RedisUtils.get(redisKey);
        if (null != object) {
            return ResultBean.success((AmapIp) object);
        }
        String url = "https://restapi.amap.com/v3/ip?output=json"
                + "&key=" + AmapConfig.getKey()
                + "&ip=" + ip;
        RestTemplate restTemplate = new RestTemplate();
        try {
            String result = restTemplate.getForObject(url, String.class);
            AmapIp amapIp = JSONObject.parseObject(result, AmapIp.class);
            //成功的状态码
            int successStatus = 1;
            if (successStatus != amapIp.getStatus()) {
                log.warn("高德地图api-根据ip获取用户省份城市出错，ip:{},详情:{}", ip, result);
            }
            //缓存三分钟
            RedisUtils.set(redisKey, amapIp, 60L * 3);
            log.debug("高德地图api-根据ip获取用户省份城市,ip:{},信息:{}", ip, result);
            return ResultBean.success(amapIp);
        } catch (Exception e) {
            log.warn("高德地图api-根据ip获取用户省份城市出错，ip:{},详情:{}", ip, e);
            return ResultBean.errorException("高德地图api-根据ip获取用户省份城市出错。Exception", e);
        }
    }

}
