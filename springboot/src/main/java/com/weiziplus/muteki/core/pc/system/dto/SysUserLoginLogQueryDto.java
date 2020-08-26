package com.weiziplus.muteki.core.pc.system.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author wanglongwei
 * @date 2020/08/26 14/32
 */
@Data
public class SysUserLoginLogQueryDto implements Serializable {

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("省份")
    private String loginProvince;

    @ApiModelProperty("城市")
    private String loginCity;

    @ApiModelProperty("ip")
    private String ipAddress;

    @ApiModelProperty("结果")
    private String result;

    @ApiModelProperty("浏览器")
    private String borderName;

    @ApiModelProperty("操作系统")
    private String osName;

    @ApiModelProperty("起始时间")
    private String startTime;

    @ApiModelProperty("截止时间")
    private String endTime;

    @ApiModelProperty("创建时间排序")
    private String createTimeSort = "DESC";

}
