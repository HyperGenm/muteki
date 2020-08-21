package com.weiziplus.muteki.baidu.map.iplocation;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 百度api-普通ip定位
 *
 * @author wanglongwei
 * @date 2020/08/17 09/20
 */
@Getter
@Setter
public class IpLocation implements Serializable {

    /**
     * 状态码
     */
    private Integer status;

    /**
     * 详细地址信息
     */
    private String address;

    /**
     * 结构信息
     */
    private IpLocationContent content;

    private static final long serialVersionUID = 1L;

}
