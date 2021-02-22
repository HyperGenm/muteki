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
 * 部门表
 * sys_dept
 *
 * @author 王龙伟
 * @date 2020-11-26 12:44:31
 */
@BaseTable("sys_dept")
@Alias("SysDept")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Accessors(chain = true)
@ApiModel("部门表")
public class SysDept implements Serializable {
    /**
     *
     */
    @BaseId("id")
    private Integer id;

    /**
     * 上级部门id，最高级为0
     */
    @BaseColumn("parent_id")
    @ApiModelProperty("上级部门id，最高级为0")
    private Integer parentId;

    /**
     * 部门名称
     */
    @BaseColumn("name")
    @ApiModelProperty("部门名称")
    private String name;

    /**
     * 上级名称
     */
    @ApiModelProperty("上级名称")
    private String parentName;

    /**
     * 排序，数字越大越靠前
     */
    @BaseColumn("sort")
    @ApiModelProperty("排序，数字越大越靠前")
    private Integer sort;

    /**
     * 描述
     */
    @BaseColumn("remark")
    @ApiModelProperty("描述")
    private String remark;

    /**
     * 操作人用户名
     */
    @BaseColumn("edit_username")
    @ApiModelProperty("操作人用户名")
    private String editUsername;

    /**
     * 操作人真实姓名
     */
    @BaseColumn("edit_real_name")
    @ApiModelProperty("操作人真实姓名")
    private String editRealName;

    /**
     * 更新时间
     */
    @BaseColumn("update_time")
    @ApiModelProperty("更新时间")
    private String updateTime;

    /**
     * 创建时间
     */
    @BaseColumn("create_time")
    @ApiModelProperty("创建时间")
    private String createTime;

    /**
     * 删除时间，0:未删除，大于0:删除时间
     */
    @BaseColumn("delete_time")
    @ApiModelProperty("删除时间，0:未删除，大于0:删除时间")
    private Long deleteTime;

    private static final long serialVersionUID = 1L;

    public static final String COLUMN_ID = "id";

    public static final String COLUMN_PARENT_ID = "parent_id";

    public static final String COLUMN_NAME = "name";

    public static final String COLUMN_SORT = "sort";

    public static final String COLUMN_REMARK = "remark";

    public static final String COLUMN_EDIT_USERNAME = "edit_username";

    public static final String COLUMN_EDIT_REAL_NAME = "edit_real_name";

    public static final String COLUMN_UPDATE_TIME = "update_time";

    public static final String COLUMN_CREATE_TIME = "create_time";

    public static final String COLUMN_DELETE_TIME = "delete_time";
}