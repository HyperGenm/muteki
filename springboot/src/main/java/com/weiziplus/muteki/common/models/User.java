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
 * web用户表
 * t_user
 *
 * @author 16028
 * @date 2020-07-30 11:29:01
 */
@BaseTable("t_user")
@Alias("TUser")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Accessors(chain = true)
@ApiModel("web用户表")
public class User implements Serializable {
    /**
     * 用户表主键，自增
     */
    @BaseId("id")
    @ApiModelProperty("用户表主键，自增")
    private Long id;

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
     * 状态,1:正常,2:禁用
     */
    @BaseColumn("status")
    @ApiModelProperty("状态,1:正常,2:禁用,3:封号")
    private Integer status;

    /**
     * 用户创建时间
     */
    @BaseColumn("create_time")
    @ApiModelProperty("用户创建时间")
    private String createTime;

    private static final long serialVersionUID = 1L;

    public static final String COLUMN_ID = "id";

    public static final String COLUMN_USERNAME = "username";

    public static final String COLUMN_PASSWORD = "password";

    public static final String COLUMN_STATUS = "status";

    public static final String COLUMN_CREATE_TIME = "create_time";
}