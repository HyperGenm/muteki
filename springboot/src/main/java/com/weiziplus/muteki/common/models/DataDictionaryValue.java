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
 * 数据字典值
 * data_dictionary_value
 * @author 16028
 * @date 2020-07-30 11:27:19
 */
@BaseTable("data_dictionary_value")
@Alias("DataDictionaryValue")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Accessors(chain = true)
@ApiModel("数据字典值")
public class DataDictionaryValue implements Serializable {
    /**
     * 自增
     */
    @BaseId("id")
    @ApiModelProperty("自增")
    private Integer id;

    /**
     * 字典编号
     */
    @BaseColumn("dictionary_code")
    @ApiModelProperty("字典编号")
    private String dictionaryCode;

    /**
     * 值
     */
    @BaseColumn("value")
    @ApiModelProperty("值")
    private String value;

    /**
     * 名称
     */
    @BaseColumn("label")
    @ApiModelProperty("名称")
    private String label;

    /**
     * 可以作为排序
     */
    @BaseColumn("num")
    @ApiModelProperty("可以作为排序")
    private Integer num;

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

    public static final String COLUMN_DICTIONARY_CODE = "dictionary_code";

    public static final String COLUMN_VALUE = "value";

    public static final String COLUMN_LABEL = "label";

    public static final String COLUMN_NUM = "num";

    public static final String COLUMN_REMARK = "remark";

    public static final String COLUMN_CREATE_TIME = "create_time";
}