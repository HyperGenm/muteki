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
 * web用户日志
 * t_user_log
 * @author 16028
 * @date 2020-08-26 08:52:06
 */
@BaseTable("t_user_log")
@Alias("TUserLog")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Accessors(chain = true)
@ApiModel("web用户日志")
public class UserLog implements Serializable {
    /**
     */
    @BaseId("id")
    private Long id;

    /**
     * 用户表主键
     */
    @BaseColumn("user_id")
    @ApiModelProperty("用户表主键")
    private Long userId;

    /**
     * 终端类型
     */
    @BaseColumn("terminal")
    @ApiModelProperty("终端类型")
    private String terminal;

    /**
     * 请求的路径
     */
    @BaseColumn("url")
    @ApiModelProperty("请求的路径")
    private String url;

    /**
     * 请求方法名
     */
    @BaseColumn("method_name")
    @ApiModelProperty("请求方法名")
    private String methodName;

    /**
     * 请求的类型,1:查询,2:新增,3:修改,4:删除
     */
    @BaseColumn("type")
    @ApiModelProperty("请求的类型,1:查询,2:新增,3:修改,4:删除")
    private Integer type;

    /**
     * 响应码
     */
    @BaseColumn("result_code")
    @ApiModelProperty("响应码")
    private Integer resultCode;

    /**
     * 提示信息
     */
    @BaseColumn("result_msg")
    @ApiModelProperty("提示信息")
    private String resultMsg;

    /**
     * 请求耗时
     */
    @BaseColumn("time_consuming")
    @ApiModelProperty("请求耗时")
    private Integer timeConsuming;

    /**
     * 操作描述
     */
    @BaseColumn("description")
    @ApiModelProperty("操作描述")
    private String description;

    /**
     * ip地址
     */
    @BaseColumn("ip_address")
    @ApiModelProperty("ip地址")
    private String ipAddress;

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

    /**
     * 当前请求的参数
     */
    @BaseColumn("param")
    @ApiModelProperty("当前请求的参数")
    private String param;

    private static final long serialVersionUID = 1L;

    public static final String COLUMN_ID = "id";

    public static final String COLUMN_USER_ID = "user_id";

    public static final String COLUMN_TERMINAL = "terminal";

    public static final String COLUMN_URL = "url";

    public static final String COLUMN_METHOD_NAME = "method_name";

    public static final String COLUMN_TYPE = "type";

    public static final String COLUMN_RESULT_CODE = "result_code";

    public static final String COLUMN_RESULT_MSG = "result_msg";

    public static final String COLUMN_TIME_CONSUMING = "time_consuming";

    public static final String COLUMN_DESCRIPTION = "description";

    public static final String COLUMN_IP_ADDRESS = "ip_address";

    public static final String COLUMN_BORDER_NAME = "border_name";

    public static final String COLUMN_OS_NAME = "os_name";

    public static final String COLUMN_CREATE_TIME = "create_time";

    public static final String COLUMN_PARAM = "param";
}