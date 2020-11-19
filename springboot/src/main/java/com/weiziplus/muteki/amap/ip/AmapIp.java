package com.weiziplus.muteki.amap.ip;

import lombok.Data;

/**
 * @author wanglongwei
 * @date 2020/11/19 14/03
 */
@Data
public class AmapIp {

    /**
     * 返回结果状态值
     * 值为0或1,0表示失败；1表示成功
     */
    private Integer status;

    /**
     * 返回状态说明
     * 返回状态说明，status为0时，info返回错误原因，否则返回“OK”。
     */
    private String info;

    /**
     * 状态码
     * 返回状态说明,10000代表正确,详情参阅info状态表
     */
    private Integer infocode;

    /**
     * 省份名称
     * 若为直辖市则显示直辖市名称；
     * <p>
     * 如果在局域网 IP网段内，则返回“局域网”；
     * <p>
     * 非法IP以及国外IP则返回空
     */
    private String province;

    /**
     * 城市名称
     * 若为直辖市则显示直辖市名称；
     * <p>
     * 如果为局域网网段内IP或者非法IP或国外IP，则返回空
     */
    private String city;

    /**
     * 城市的adcode编码
     */
    private String adcode;

    /**
     * 所在城市矩形区域范围
     * 所在城市范围的左下右上对标对
     */
    private String rectangle;

}
