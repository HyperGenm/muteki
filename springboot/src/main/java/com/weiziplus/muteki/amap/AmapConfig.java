package com.weiziplus.muteki.amap;

import com.weiziplus.muteki.common.util.ToolUtils;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 高德地图
 *
 * @author wanglongwei
 * @date 2020/11/19 13/50
 */
@Slf4j
@Component
public class AmapConfig {

    /**
     * 高德地图web服务key
     * https://console.amap.com/dev/key/app
     */
    private static String KEY = "93f4a94a31bdf80cf7a6aedf01aed86e";

    /**
     * 赋值
     *
     * @param key
     */
    @Value("${global.amap.key:}")
    private void setKey(String key) {
        if (ToolUtils.isBlank(key)) {
            log.warn("如果您想根据ip获取到登录用户的城市信息，请在yml文件中设置  global.amap.key 的值,  高德开放平台控制台:https://console.amap.com/dev/key/app  。如不需要，请忽略该提示");
        }
        KEY = key;
    }

    /**
     * 抛出key
     *
     * @return
     */
    public static String getKey() {
        return KEY;
    }

}
