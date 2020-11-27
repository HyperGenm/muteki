package com.weiziplus.muteki.core.pc.system.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * @author wanglongwei
 * @date 2020/11/26 14/52
 */
@Data
public class SysDeptDto {

    @ApiModelProperty("id")
    private Integer id;

    @NotBlank(message = "名称不能为空")
    @Size(min = 2, message = "名称最少两位")
    @ApiModelProperty("名称")
    private String name;

    @NotBlank(message = "父级id不能为空")
    @ApiModelProperty("父级名称")
    private Integer parentId = 0;

    @ApiModelProperty("排序")
    private Integer sort = 0;

    @ApiModelProperty("备注")
    private String remark = "";

}
