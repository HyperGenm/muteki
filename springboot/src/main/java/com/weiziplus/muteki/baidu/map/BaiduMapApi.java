package com.weiziplus.muteki.baidu.map;

import com.alibaba.fastjson.JSON;
import com.weiziplus.muteki.baidu.BaiduConfig;
import com.weiziplus.muteki.baidu.map.iplocation.IpLocation;
import com.weiziplus.muteki.common.result.ResultBean;
import com.weiziplus.muteki.common.result.ResultEnum;
import com.weiziplus.muteki.common.util.ToolUtils;
import com.weiziplus.muteki.common.util.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * 百度地图api
 *
 * @author wanglongwei
 * @date 2020/08/17 08/54
 */
@Slf4j
@Component
public class BaiduMapApi {

    /**
     * 地图请求的前缀
     */
    private final static String BASE_URL = "https://api.map.baidu.com";

    /**
     * redis的前缀
     */
    private final static String REDIS_PREFIX = "baiduApi:map:";

    /**
     * 根据ip获取地址信息
     * 请求成功缓存3小时，请求失败缓存3分钟
     *
     * @param ip
     * @return
     */
    public static ResultBean<IpLocation> getLocation(String ip) {
        if (ToolUtils.isBlank(ip)) {
            return ResultBean.error("ip不能为空");
        }
        //redis的key
        String redisKey = REDIS_PREFIX + "ipLocation:" + ip;
        Object object = RedisUtils.get(redisKey);
        if (null != object) {
            return (ResultBean<IpLocation>) object;
        }
        String url = BASE_URL + "/location/ip?coor=bd09ll"
                + "&ak=" + BaiduConfig.getAk()
                + "&ip=" + ip;
        IpLocation ipLocation;
        RestTemplate restTemplate = new RestTemplate();
        try {
            ipLocation = restTemplate.getForObject(url, IpLocation.class);
        } catch (Exception e) {
            log.warn("百度api-普通ip定位调用出错，详情:" + e);
            return ResultBean.error("百度api-普通ip定位调用出错。Exception", e);
        }
        //如果请求失败
        if (!BaiduConfig.SUCCESS_CODE.equals(ipLocation.getStatus())) {
            log.warn("百度api-普通ip定位调用出错，详情:" + JSON.toJSONString(ipLocation) + "。---状态码请参考:http://lbsyun.baidu.com/index.php?title=webapi/ip-api");
            ResultBean<IpLocation> error = ResultBean.error(ResultEnum.ERROR_BAIDU, JSON.toJSONString(ipLocation));
            //请求失败，缓存3分钟
            RedisUtils.set(redisKey, error, 60L * 3);
            return error;
        }
        //请求成功，缓存3小时
        RedisUtils.set(redisKey, ResultBean.success(ipLocation), 60L * 60 * 3);
        return ResultBean.success(ipLocation);
    }

}
