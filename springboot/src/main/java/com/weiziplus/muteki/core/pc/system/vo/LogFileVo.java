package com.weiziplus.muteki.core.pc.system.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author wanglongwei
 * @date 2020/06/05 08/43
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Accessors(chain = true)
@ApiModel("日志文件")
public class LogFileVo {

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("如果是目录，子文件数量")
    private Integer fileNum;

    @ApiModelProperty("文件大小")
    private Long length;

    @ApiModelProperty("文件路径")
    private String url;

    @ApiModelProperty("子文件")
    private List<LogFileVo> children;

}
