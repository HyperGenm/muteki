package com.weiziplus.muteki.common.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.weiziplus.muteki.common.base.BaseColumn;
import com.weiziplus.muteki.common.base.BaseId;
import com.weiziplus.muteki.common.base.BaseTable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.ibatis.type.Alias;

/**
 * 系统功能表
 * sys_function
 *
 * @author 16028
 * @date 2020-08-20 11:20:41
 */
@BaseTable("sys_function")
@Alias("SysFunction")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Accessors(chain = true)
@ApiModel("系统功能表")
public class SysFunction implements Serializable {
    /**
     * 系统功能表主键，自增
     */
    @BaseId("id")
    @ApiModelProperty("系统功能表主键，自增")
    private Integer id;

    /**
     * 上级id
     */
    @BaseColumn("parent_id")
    @ApiModelProperty("上级id")
    private Integer parentId;

    /**
     * 功能唯一标识
     */
    @BaseColumn("name")
    @ApiModelProperty("功能唯一标识")
    private String name;

    /**
     * 功能路径
     */
    @BaseColumn("path")
    @ApiModelProperty("功能路径")
    private String path;

    /**
     * 功能标题
     */
    @BaseColumn("title")
    @ApiModelProperty("功能标题")
    private String title;

    /**
     * 功能类型;1:菜单,2:按钮,3:外部超链接
     */
    @BaseColumn("type")
    @ApiModelProperty("功能类型;1:菜单,2:按钮")
    private Integer type;

    /**
     * 超级管理员专属功能,1:普通,2:vip
     */
    @BaseColumn("super_flag")
    @ApiModelProperty("超级管理员专属功能,1:普通,2:vip")
    private Integer superFlag;

    /**
     * 外部链接,1:内部,2:外部超链接
     */
    @BaseColumn("external_flag")
    @ApiModelProperty("外部链接,1:内部,2:外部超链接")
    private Integer externalFlag;

    /**
     * 功能图标
     */
    @BaseColumn("icon")
    @ApiModelProperty("功能图标")
    private String icon;

    /**
     * 功能排序，数字越小越靠前
     */
    @BaseColumn("sort")
    @ApiModelProperty("功能排序，数字越小越靠前")
    private Integer sort;

    /**
     * 功能描述
     */
    @BaseColumn("description")
    @ApiModelProperty("功能描述")
    private String description;

    /**
     * 功能创建时间
     */
    @BaseColumn("create_time")
    @ApiModelProperty("功能创建时间")
    private String createTime;

    /**
     * 当前功能对应的api列表，多个用,隔开
     */
    @BaseColumn("contain_api")
    @ApiModelProperty("当前功能对应的api列表，多个用,隔开")
    private String containApi;

    @ApiModelProperty("子级列表")
    private List<SysFunction> children;

    private static final long serialVersionUID = 1L;

    public static final String COLUMN_ID = "id";

    public static final String COLUMN_PARENT_ID = "parent_id";

    public static final String COLUMN_NAME = "name";

    public static final String COLUMN_PATH = "path";

    public static final String COLUMN_TITLE = "title";

    public static final String COLUMN_TYPE = "type";

    public static final String COLUMN_SUPER_FLAG = "super_flag";

    public static final String COLUMN_EXTERNAL_FLAG = "external_flag";

    public static final String COLUMN_ICON = "icon";

    public static final String COLUMN_SORT = "sort";

    public static final String COLUMN_DESCRIPTION = "description";

    public static final String COLUMN_CREATE_TIME = "create_time";

    public static final String COLUMN_CONTAIN_API = "contain_api";
}