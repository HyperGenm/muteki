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
 * 系统用户表
 * sys_user
 * @author 16028
 * @date 2020-07-30 11:28:23
 */
@BaseTable("sys_user")
@Alias("SysUser")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Accessors(chain = true)
@ApiModel("系统用户表")
public class SysUser implements Serializable {
    /**
     */
    @BaseId("id")
    private Integer id;

    /**
     * 用户名
     */
    @BaseColumn("username")
    @ApiModelProperty("用户名")
    private String username;

    /**
     * 密码
     */
    @BaseColumn("password")
    @ApiModelProperty("密码")
    @JsonInclude
    private String password;

    /**
     * 真实姓名
     */
    @BaseColumn("real_name")
    @ApiModelProperty("真实姓名")
    private String realName;

    /**
     * 手机号
     */
    @BaseColumn("phone")
    @ApiModelProperty("手机号")
    private String phone;

    /**
     * 状态,1:正常,2:禁用
     */
    @BaseColumn("status")
    @ApiModelProperty("状态,1:正常,2:禁用")
    private Integer status;

    /**
     * 用户头像
     */
    @BaseColumn("icon")
    @ApiModelProperty("用户头像")
    private String icon;

    /**
     * 用户创建时间
     */
    @BaseColumn("create_time")
    @ApiModelProperty("用户创建时间")
    private String createTime;

    /**
     * 删除时间,0:未删除，大于0:删除时间
     */
    @BaseColumn("delete_time")
    @ApiModelProperty("删除时间,0:未删除，大于0:删除时间")
    private Long deleteTime;

    private static final long serialVersionUID = 1L;

    public static final String COLUMN_ID = "id";

    public static final String COLUMN_USERNAME = "username";

    public static final String COLUMN_PASSWORD = "password";

    public static final String COLUMN_REAL_NAME = "real_name";

    public static final String COLUMN_PHONE = "phone";

    public static final String COLUMN_STATUS = "status";

    public static final String COLUMN_ICON = "icon";

    public static final String COLUMN_CREATE_TIME = "create_time";

    public static final String COLUMN_DELETE_TIME = "delete_time";
}