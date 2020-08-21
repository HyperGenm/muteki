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
 * 系统角色
 * sys_role
 * @author 16028
 * @date 2020-07-30 11:27:57
 */
@BaseTable("sys_role")
@Alias("SysRole")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Accessors(chain = true)
@ApiModel("系统角色")
public class SysRole implements Serializable {
    /**
     */
    @BaseId("id")
    private Integer id;

    /**
     * 角色名称
     */
    @BaseColumn("name")
    @ApiModelProperty("角色名称")
    private String name;

    /**
     * 状态,1:正常,2:禁用
     */
    @BaseColumn("status")
    @ApiModelProperty("状态,1:正常,2:禁用")
    private Integer status;

    /**
     * 排序，数字越小越靠前
     */
    @BaseColumn("sort")
    @ApiModelProperty("排序，数字越小越靠前")
    private Integer sort;

    /**
     * 备注
     */
    @BaseColumn("remark")
    @ApiModelProperty("备注")
    private String remark;

    /**
     * 角色创建时间
     */
    @BaseColumn("create_time")
    @ApiModelProperty("角色创建时间")
    private String createTime;

    private static final long serialVersionUID = 1L;

    public static final String COLUMN_ID = "id";

    public static final String COLUMN_NAME = "name";

    public static final String COLUMN_STATUS = "status";

    public static final String COLUMN_SORT = "sort";

    public static final String COLUMN_REMARK = "remark";

    public static final String COLUMN_CREATE_TIME = "create_time";
}