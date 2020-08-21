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
 * 数据字典表
 * data_dictionary
 * @author 16028
 * @date 2020-07-30 11:26:54
 */
@BaseTable("data_dictionary")
@Alias("DataDictionary")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Accessors(chain = true)
@ApiModel("数据字典表")
public class DataDictionary implements Serializable {
    /**
     * 自增
     */
    @BaseId("id")
    @ApiModelProperty("自增")
    private Integer id;

    /**
     * 字典标识
     */
    @BaseColumn("code")
    @ApiModelProperty("字典标识")
    private String code;

    /**
     * 名称
     */
    @BaseColumn("label")
    @ApiModelProperty("名称")
    private String label;

    /**
     * 字典备注
     */
    @BaseColumn("remark")
    @ApiModelProperty("字典备注")
    private String remark;

    /**
     * 字典创建时间
     */
    @BaseColumn("create_time")
    @ApiModelProperty("字典创建时间")
    private String createTime;

    private static final long serialVersionUID = 1L;

    public static final String COLUMN_ID = "id";

    public static final String COLUMN_CODE = "code";

    public static final String COLUMN_LABEL = "label";

    public static final String COLUMN_REMARK = "remark";

    public static final String COLUMN_CREATE_TIME = "create_time";
}