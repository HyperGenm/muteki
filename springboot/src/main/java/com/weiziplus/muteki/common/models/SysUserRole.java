package com.weiziplus.muteki.common.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.weiziplus.muteki.common.base.BaseColumn;
import com.weiziplus.muteki.common.base.BaseId;
import com.weiziplus.muteki.common.base.BaseTable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.ibatis.type.Alias;

/**
 * 系统用户角色表
 * sys_user_role
 * @author 16028
 * @date 2020-07-30 11:28:48
 */
@BaseTable("sys_user_role")
@Alias("SysUserRole")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Accessors(chain = true)
@ApiModel("系统用户角色表")
public class SysUserRole implements Serializable {
    /**
     */
    @BaseId("id")
    private Long id;

    /**
     * 用户表主键
     */
    @BaseColumn("user_id")
    @ApiModelProperty("用户表主键")
    private Integer userId;

    /**
     * 角色表主键
     */
    @BaseColumn("role_id")
    @ApiModelProperty("角色表主键")
    private Integer roleId;

    private static final long serialVersionUID = 1L;

    public static final String COLUMN_ID = "id";

    public static final String COLUMN_USER_ID = "user_id";

    public static final String COLUMN_ROLE_ID = "role_id";
}