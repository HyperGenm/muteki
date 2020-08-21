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
 * 系统角色功能表
 * sys_role_function
 * @author 16028
 * @date 2020-07-30 11:28:10
 */
@BaseTable("sys_role_function")
@Alias("SysRoleFunction")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Accessors(chain = true)
@ApiModel("系统角色功能表")
public class SysRoleFunction implements Serializable {
    /**
     */
    @BaseId("id")
    private Long id;

    /**
     * 角色表id
     */
    @BaseColumn("role_id")
    @ApiModelProperty("角色表id")
    private Integer roleId;

    /**
     * 功能表id
     */
    @BaseColumn("function_id")
    @ApiModelProperty("功能表id")
    private Integer functionId;

    private static final long serialVersionUID = 1L;

    public static final String COLUMN_ID = "id";

    public static final String COLUMN_ROLE_ID = "role_id";

    public static final String COLUMN_FUNCTION_ID = "function_id";
}