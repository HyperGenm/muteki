package com.weiziplus.muteki.core.pc.system.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.weiziplus.muteki.common.models.SysFunction;
import com.weiziplus.muteki.common.models.SysUser;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Set;

/**
 * @author wanglongwei
 * @date 2020/08/04 10/39
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Accessors(chain = true)
@ApiModel("登陆成功")
public class LoginVo {

    @ApiModelProperty("token")
    private String token;

    @ApiModelProperty("用户信息")
    private SysUser sysUser;

    @ApiModelProperty("角色ids")
    private Set<Integer> roleIds;

    @ApiModelProperty("菜单树")
    private List<SysFunction> menuTree;

    @ApiModelProperty("按钮列表")
    private Set<String> buttonSet;

}
