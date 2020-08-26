package com.weiziplus.muteki.core.pc.system.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author wanglongwei
 * @date 2020/08/26 11/58
 */
@Data
public class SysUserLogQueryDto implements Serializable {

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("真实姓名")
    private String realName;

    @ApiModelProperty("类型")
    private Integer type;

    @ApiModelProperty("状态码")
    private Integer resultCode;

    @ApiModelProperty("操作")
    private String description;

    @ApiModelProperty("ip地址")
    private String ipAddress;

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
