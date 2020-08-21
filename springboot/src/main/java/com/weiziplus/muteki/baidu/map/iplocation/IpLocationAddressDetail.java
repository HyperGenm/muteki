package com.weiziplus.muteki.baidu.map.iplocation;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 百度api-普通ip定位-结构信息-当前城市中心点
 *
 * @author wanglongwei
 * @date 2020/08/17 09/35
 */
@Getter
@Setter
public class IpLocationAddressDetail implements Serializable {

    /**
     * 城市
     */
    private String city;

    /**
     * 百度城市代码
     */
    private Integer city_code;

    /**
     * 省份
     */
    private String province;

    private static final long serialVersionUID = 1L;
}
