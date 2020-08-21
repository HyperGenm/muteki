package com.weiziplus.muteki.baidu.map.iplocation;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 百度api-普通ip定位-结构信息-结构化地址信息
 *
 * @author wanglongwei
 * @date 2020/08/17 09/36
 */
@Getter
@Setter
public class IpLocationPoint implements Serializable {

    /**
     * 当前城市中心点经度
     */
    private String x;

    /**
     * 当前城市中心点纬度
     */
    private String y;

    private static final long serialVersionUID = 1L;

}

