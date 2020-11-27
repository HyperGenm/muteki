package com.weiziplus.muteki.core.pc.system.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author wanglongwei
 * @date 2020/05/28 09/35
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Accessors(chain = true)
@ApiModel("系统用户")
public class SysUserVo implements Serializable {
    /**
     */
    private Integer id;

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("真实姓名")
    private String realName;

    @ApiModelProperty("手机号")
    private String phone;

    @ApiModelProperty("状态,1:正常,2:禁用")
    private Integer status;

    @ApiModelProperty("用户头像")
    private String icon;

    @ApiModelProperty("用户头像全路径")
    private String iconAllPath;

    @ApiModelProperty("用户最后活跃ip地址")
    private String lastIpAddress;

    @ApiModelProperty("用户最后活跃时间")
    private String lastActiveTime;

    @ApiModelProperty("用户创建时间")
    private String createTime;

    @ApiModelProperty("角色ids")
    private String roleIds;

    @ApiModelProperty("角色列表")
    private String roleList;

    @ApiModelProperty("部门ids")
    private String deptIds;

    @ApiModelProperty("部门列表")
    private String deptList;

}
