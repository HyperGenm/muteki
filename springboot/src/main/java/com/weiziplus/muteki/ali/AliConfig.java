package com.weiziplus.muteki.ali;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author wanglongwei
 * @date 2020/08/21 11/13
 */
@Slf4j
@Component
public class AliConfig {

    /**
     * 地域ID
     */
    private static String REGION_ID;

    /**
     * 域名id赋值
     *
     * @param regionId
     */
    @Value("${global.ali.region-id:}")
    private void setRegionId(String regionId) {
        AliConfig.REGION_ID = regionId;
    }

    /**
     * 对外抛出域名id
     *
     * @return
     */
    public static String getRegionId() {
        return AliConfig.REGION_ID;
    }

    /**
     * RAM账号的AccessKey ID
     */
    private static String ACCESS_KEY_ID;

    /**
     * 赋值 RAM账号的AccessKey ID
     *
     * @param accessKeyId
     */
    @Value("${global.ali.access-key-id:}")
    private void setAccessKeyId(String accessKeyId) {
        AliConfig.ACCESS_KEY_ID = accessKeyId;
    }

    /**
     * 对外抛出 RAM账号的AccessKey ID
     *
     * @return
     */
    public static String getAccessKeyId() {
        return AliConfig.ACCESS_KEY_ID;
    }


    /**
     * RAM账号AccessKey Secret
     */
    private static String ACCESS_SECRET;

    /**
     * 赋值 RAM账号AccessKey Secret
     *
     * @param accessSecret
     */
    @Value("${global.ali.access-secret:}")
    private void setAccessSecret(String accessSecret) {
        AliConfig.ACCESS_SECRET = accessSecret;
    }

    /**
     * 对外抛出 RAM账号AccessKey Secret
     *
     * @return
     */
    public static String getAccessSecret() {
        return AliConfig.ACCESS_SECRET;
    }

}
