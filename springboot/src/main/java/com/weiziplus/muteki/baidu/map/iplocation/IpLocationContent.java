package com.weiziplus.muteki.baidu.map.iplocation;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 百度api-普通ip定位-结构信息
 *
 * @author wanglongwei
 * @date 2020/08/17 09/22
 */
@Getter
@Setter
public class IpLocationContent implements Serializable {

    /**
     * 简要地址信息
     */
    private String address;

    /**
     * 结构化地址信息
     */
    private IpLocationAddressDetail address_detail;

    /**
     * 当前城市中心点
     */
    private IpLocationPoint point;

    private static final long serialVersionUID = 1L;

}
