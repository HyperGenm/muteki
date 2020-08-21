package com.weiziplus.muteki.core.pc.system.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * @author wanglongwei
 * @date 2020/05/29 14/26
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Accessors(chain = true)
@ApiModel("系统用户")
public class SysUserDto implements Serializable {
    /**
     *
     */
    private Integer id;

    @NotBlank(message = "用户名不能为空")
    @Size(min = 2, message = "用户名最少两位")
    @ApiModelProperty("用户名")
    private String username;

    @NotBlank(message = "用户名不能为空")
    @ApiModelProperty("密码")
    private String password;

    @NotBlank(message = "真实姓名不能为空")
    @Size(min = 2, message = "真实姓名最少两位")
    @ApiModelProperty("真实姓名")
    private String realName;

    @NotBlank(message = "手机号不能为空")
    @ApiModelProperty("手机号")
    private String phone;

    @ApiModelProperty("状态,1:正常,2:禁用")
    private Integer status;
}