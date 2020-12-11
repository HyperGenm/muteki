package com.weiziplus.muteki.core.pc.system.controller;

import com.weiziplus.muteki.common.models.SysFunction;
import com.weiziplus.muteki.common.result.ResultBean;
import com.weiziplus.muteki.common.util.PageUtils;
import com.weiziplus.muteki.common.validate.ValidateInsert;
import com.weiziplus.muteki.core.pc.common.aop.SysUserLogInterface;
import com.weiziplus.muteki.core.pc.common.interceptor.PcSuperAdminAuth;
import com.weiziplus.muteki.core.pc.system.dto.SysFunctionDto;
import com.weiziplus.muteki.core.pc.system.service.SysFunctionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author wanglongwei
 * @date 2020/08/04 09/07
 */
@PcSuperAdminAuth
@Api(tags = "系统功能")
@RestController
@RequestMapping("/pc/sysFunction")
public class SysFunctionController {

    @Autowired
    SysFunctionService service;

    @ApiOperation(value = "获取分页数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "pageNum", defaultValue = "1", dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "pageSize", defaultValue = "10", dataType = "Integer", paramType = "query"),
    })
    @GetMapping("/getPageList")
    @SysUserLogInterface(description = "获取系统功能分页数据")
    public ResultBean<PageUtils<SysFunction>> getPageList(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        return service.getPageList(pageNum, pageSize);
    }

    @ApiOperation(value = "获取树形结构")
    @GetMapping("/getTree")
    @SysUserLogInterface(description = "获取系统功能树形结构")
    public ResultBean<List<SysFunction>> getTree() {
        return service.getTree();
    }

    @ApiOperation(value = "获取树形结构")
    @GetMapping("/getAllTree")
    @SysUserLogInterface(description = "获取所有系统功能树形结构")
    public ResultBean<List<SysFunction>> getAllTree() {
        return service.getAllTree();
    }

    @ApiOperation(value = "新增功能")
    @PostMapping("/add")
    @SysUserLogInterface(description = "新增功能", type = SysUserLogInterface.TYPE_INSERT)
    public ResultBean add(@Validated({ValidateInsert.class}) SysFunctionDto sysFunctionDto) {
        return service.add(sysFunctionDto);
    }

    @ApiOperation(value = "修改功能")
    @PostMapping("/update")
    @SysUserLogInterface(description = "修改功能", type = SysUserLogInterface.TYPE_UPDATE)
    public ResultBean update(SysFunctionDto sysFunctionDto) {
        return service.update(sysFunctionDto);
    }

    @ApiOperation(value = "删除功能")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "Integer", paramType = "form"),
    })
    @PostMapping("/delete")
    @SysUserLogInterface(description = "删除功能", type = SysUserLogInterface.TYPE_DELETE)
    public ResultBean delete(Integer id) {
        return service.delete(id);
    }

    @ApiOperation(value = "修改功能拥有的api")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id", dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "apiList", value = "api列表", dataType = "String[]", paramType = "form"),
    })
    @PostMapping("/updateContainApi")
    @SysUserLogInterface(description = "修改功能拥有的api", type = SysUserLogInterface.TYPE_UPDATE)
    public ResultBean updateContainApi(Integer id, String[] apiList) {
        return service.updateContainApi(id, apiList);
    }

    @ApiOperation(value = "设置图标")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id", dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "icon", value = "icon", dataType = "String", paramType = "form"),
    })
    @PostMapping("/setIcon")
    @SysUserLogInterface(description = "功能设置图标", type = SysUserLogInterface.TYPE_UPDATE)
    public ResultBean setIcon(Integer id, String icon) {
        return service.setIcon(id, icon);
    }

}
