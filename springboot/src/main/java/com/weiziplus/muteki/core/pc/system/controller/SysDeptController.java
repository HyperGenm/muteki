package com.weiziplus.muteki.core.pc.system.controller;

import com.weiziplus.muteki.common.models.SysDept;
import com.weiziplus.muteki.common.result.ResultBean;
import com.weiziplus.muteki.common.util.PageUtils;
import com.weiziplus.muteki.core.pc.common.aop.SysUserLogInterface;
import com.weiziplus.muteki.core.pc.common.interceptor.PcAuthToken;
import com.weiziplus.muteki.core.pc.system.dto.SysDeptDto;
import com.weiziplus.muteki.core.pc.system.service.SysDeptService;
import com.weiziplus.muteki.core.pc.system.vo.SysDeptVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author wanglongwei
 * @date 2020/11/26 11/32
 */
@RestController
@PcAuthToken
@RequestMapping("/pc/sysDept")
@Api(tags = "系统部门")
public class SysDeptController {

    @Autowired
    SysDeptService service;

    @ApiOperation(value = "获取部门树")
    @GetMapping("/getTree")
    @SysUserLogInterface(description = "获取系统部门树")
    public ResultBean<List<SysDeptVo>> getTree() {
        return service.getTree();
    }

    @ApiOperation(value = "获取分页数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "pageNum", defaultValue = "1", dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "pageSize", defaultValue = "10", dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "parentId", value = "上级id", defaultValue = "0", dataType = "Integer", paramType = "query"),
    })
    @GetMapping("/getPageList")
    @SysUserLogInterface(description = "获取系统部门分页数据")
    public ResultBean<PageUtils<SysDept>> getPageList(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "0") Integer parentId) {
        return service.getPageList(pageNum, pageSize, parentId);
    }

    @ApiOperation(value = "新增")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "名称", dataType = "String", required = true, paramType = "form"),
            @ApiImplicitParam(name = "sort", value = "排序", defaultValue = "0", dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "remark", value = "备注", dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "parentId", value = "上级id", defaultValue = "0", dataType = "Integer", paramType = "form"),
    })
    @PostMapping("/add")
    @SysUserLogInterface(description = "新增系统部门", type = SysUserLogInterface.TYPE_INSERT)
    public ResultBean add(SysDeptDto dto) {
        return service.add(dto);
    }

    @ApiOperation(value = "编辑")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id", dataType = "Integer", required = true, paramType = "form"),
            @ApiImplicitParam(name = "name", value = "名称", dataType = "String", required = true, paramType = "form"),
            @ApiImplicitParam(name = "sort", value = "排序", defaultValue = "0", dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "remark", value = "备注", dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "parentId", value = "上级id", defaultValue = "0", dataType = "Integer", paramType = "form"),
    })
    @PostMapping("/update")
    @SysUserLogInterface(description = "编辑系统部门", type = SysUserLogInterface.TYPE_UPDATE)
    public ResultBean update(SysDeptDto dto) {
        return service.update(dto);
    }

    @ApiOperation(value = "删除")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id", dataType = "Integer", required = true, paramType = "form"),
    })
    @PostMapping("/delete")
    @SysUserLogInterface(description = "删除系统部门", type = SysUserLogInterface.TYPE_DELETE)
    public ResultBean delete(Integer id) {
        return service.delete(id);
    }

}
