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
 * 系统异常表
 * sys_error
 * @author 16028
 * @date 2020-07-30 11:27:32
 */
@BaseTable("sys_error")
@Alias("SysError")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Accessors(chain = true)
@ApiModel("系统异常表")
public class SysError implements Serializable {
    /**
     */
    @BaseId("id")
    private Integer id;

    /**
     * 类名
     */
    @BaseColumn("class_name")
    @ApiModelProperty("类名")
    private String className;

    /**
     * 方法名
     */
    @BaseColumn("method_name")
    @ApiModelProperty("方法名")
    private String methodName;

    /**
     * 第几行
     */
    @BaseColumn("line_number")
    @ApiModelProperty("第几行")
    private Integer lineNumber;

    /**
     * 详情
     */
    @BaseColumn("content")
    @ApiModelProperty("详情")
    private String content;

    /**
     * 备注
     */
    @BaseColumn("remark")
    @ApiModelProperty("备注")
    private String remark;

    /**
     * 创建时间
     */
    @BaseColumn("create_time")
    @ApiModelProperty("创建时间")
    private String createTime;

    private static final long serialVersionUID = 1L;

    public static final String COLUMN_ID = "id";

    public static final String COLUMN_CLASS_NAME = "class_name";

    public static final String COLUMN_METHOD_NAME = "method_name";

    public static final String COLUMN_LINE_NUMBER = "line_number";

    public static final String COLUMN_CONTENT = "content";

    public static final String COLUMN_REMARK = "remark";

    public static final String COLUMN_CREATE_TIME = "create_time";
}