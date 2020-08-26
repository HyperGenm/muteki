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
 * 用户登录日志表
 * t_user_login
 * @author 16028
 * @date 2020-08-26 08:49:59
 */
@BaseTable("t_user_login")
@Alias("TUserLogin")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Accessors(chain = true)
@ApiModel("用户登录日志表")
public class UserLogin implements Serializable {
    /**
     */
    @BaseId("id")
    private Long id;

    /**
     * 用户名
     */
    @BaseColumn("username")
    @ApiModelProperty("用户名")
    private String username;

    /**
     * 终端类型
     */
    @BaseColumn("terminal")
    @ApiModelProperty("终端类型")
    private String terminal;

    /**
     * 登陆省份
     */
    @BaseColumn("login_province")
    @ApiModelProperty("登陆省份")
    private String loginProvince;

    /**
     * 登陆城市
     */
    @BaseColumn("login_city")
    @ApiModelProperty("登陆城市")
    private String loginCity;

    /**
     * ip地址
     */
    @BaseColumn("ip_address")
    @ApiModelProperty("ip地址")
    private String ipAddress;

    /**
     * 响应码
     */
    @BaseColumn("result_code")
    @ApiModelProperty("响应码")
    private Integer resultCode;

    /**
     * 响应信息
     */
    @BaseColumn("result_msg")
    @ApiModelProperty("响应信息")
    private String resultMsg;

    /**
     * 浏览器名字
     */
    @BaseColumn("border_name")
    @ApiModelProperty("浏览器名字")
    private String borderName;

    /**
     * 操作系统名字
     */
    @BaseColumn("os_name")
    @ApiModelProperty("操作系统名字")
    private String osName;

    /**
     * 创建时间
     */
    @BaseColumn("create_time")
    @ApiModelProperty("创建时间")
    private String createTime;

    private static final long serialVersionUID = 1L;

    public static final String COLUMN_ID = "id";

    public static final String COLUMN_USERNAME = "username";

    public static final String COLUMN_TERMINAL = "terminal";

    public static final String COLUMN_LOGIN_PROVINCE = "login_province";

    public static final String COLUMN_LOGIN_CITY = "login_city";

    public static final String COLUMN_IP_ADDRESS = "ip_address";

    public static final String COLUMN_RESULT_CODE = "result_code";

    public static final String COLUMN_RESULT_MSG = "result_msg";

    public static final String COLUMN_BORDER_NAME = "border_name";

    public static final String COLUMN_OS_NAME = "os_name";

    public static final String COLUMN_CREATE_TIME = "create_time";
}