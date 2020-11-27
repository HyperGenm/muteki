package com.weiziplus.muteki.core.pc.system.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author wanglongwei
 * @date 2020/11/26 12/44
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Accessors(chain = true)
@ApiModel("系统部门")
public class SysDeptVo {

    @ApiModelProperty("id")
    private Integer id;

    @ApiModelProperty("上级id")
    private Integer parentId;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("子级")
    private List<SysDeptVo> children;

}
