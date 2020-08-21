package com.weiziplus.muteki.core.pc.system.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * @author wanglongwei
 * @date 2020/08/04 11/01
 */
@Data
public class LoginDto {

    @NotBlank(message = "用户名不能为空")
    @Size(min = 2, message = "用户名最少两位")
    @ApiModelProperty("用户名")
    private String username;

    @NotBlank(message = "密码不能为空")
    @Size(min = 2, message = "用户名最少两位")
    @ApiModelProperty("密码")
    private String password;

    @NotBlank(message = "验证码不能为空")
    @ApiModelProperty("验证码")
    private String code;

    @NotBlank(message = "uuid不能为空")
    @ApiModelProperty("uuid")
    private String uuid;

}
