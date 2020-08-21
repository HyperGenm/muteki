package com.weiziplus.muteki.common.util;

import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 分页返回结果
 *
 * @author wanglongwei
 * @date 2020/05/27 15/25
 */
@Data
@ApiModel("分页")
public class PageUtils<T> implements Serializable {

    @ApiModelProperty("当前页码")
    private Integer pageNum;

    @ApiModelProperty("每页数量")
    private Integer pageSize;

    @ApiModelProperty("总条数")
    private Long total;

    @ApiModelProperty("数据")
    private List<T> list;

    public static <T> PageUtils<T> pageInfo(List<T> list) {
        PageInfo<T> pageInfo = new PageInfo<>(list);
        PageUtils<T> pageUtil = new PageUtils<>();
        pageUtil.setPageNum(pageInfo.getPageNum());
        pageUtil.setPageSize(pageInfo.getSize());
        pageUtil.setTotal(pageInfo.getTotal());
        pageUtil.setList(pageInfo.getList());
        return pageUtil;
    }

    private static final long serialVersionUID = 1L;

}