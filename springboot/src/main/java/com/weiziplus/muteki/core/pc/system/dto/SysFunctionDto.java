package com.weiziplus.muteki.core.pc.system.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.weiziplus.muteki.common.validate.ValidateInsert;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author wanglongwei
 * @date 2020/08/17 14/46
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Accessors(chain = true)
@ApiModel("系统功能")
public class SysFunctionDto implements Serializable {
    /**
     *
     */
    private Integer id;

    @ApiModelProperty("上级id")
    private Integer parentId;

    @NotBlank(message = "功能名不能为空", groups = ValidateInsert.class)
    @ApiModelProperty("功能名")
    private String name;

    @NotBlank(message = "功能路径不能为空")
    @ApiModelProperty("功能路径")
    private String path;

    @NotBlank(message = "标题不能为空", groups = ValidateInsert.class)
    @ApiModelProperty("标题")
    private String title;

    @NotNull(message = "类型不能为空")
    @ApiModelProperty("类型")
    private Integer type;

    @NotNull(message = "专属类型不能为空")
    @ApiModelProperty("专属类型")
    private Integer superFlag;

    @NotNull(message = "内/外部链接不能为空")
    @ApiModelProperty("内/外部链接")
    private Integer externalFlag;

    @ApiModelProperty("排序")
    private Integer sort;

    @ApiModelProperty("备注")
    private String remark;

}
