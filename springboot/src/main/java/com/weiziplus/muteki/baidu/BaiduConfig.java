package com.weiziplus.muteki.baidu;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 百度开放平台配置
 *
 * @author wanglongwei
 * @date 2020/08/17 08/53
 */
@Component
public class BaiduConfig {

    /**
     * 百度开放平台ak
     */
    private static String AK;

    @Value("${global.baidu.ak:}")
    private void setAk(String ak) {
        BaiduConfig.AK = ak;
    }

    /**
     * 获取ak
     *
     * @return
     */
    public static String getAk() {
        return BaiduConfig.AK;
    }

    /**
     * 调用成功的code响应码
     */
    public static Integer SUCCESS_CODE = 0;

}
