package com.weiziplus.muteki.core.pc.system.controller;

import com.weiziplus.muteki.common.models.SysRole;
import com.weiziplus.muteki.common.result.ResultBean;
import com.weiziplus.muteki.common.util.PageUtils;
import com.weiziplus.muteki.core.pc.common.interceptor.PcAuthToken;
import com.weiziplus.muteki.core.pc.common.aop.SysUserLogInterface;
import com.weiziplus.muteki.core.pc.system.service.SysRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author wanglongwei
 * @date 2020/08/04 17/44
 */
@RestController
@PcAuthToken
@RequestMapping("/pc/sysRole")
@Api(tags = "系统角色")
public class SysRoleController {

    @Autowired
    SysRoleService service;

    @ApiOperation(value = "获取分页数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "pageNum", defaultValue = "1", dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "pageSize", defaultValue = "10", dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "search", value = "搜索", dataType = "String", paramType = "query"),
    })
    @GetMapping("/getPageList")
    @SysUserLogInterface(description = "获取系统角色分页数据")
    public ResultBean<PageUtils<SysRole>> getPageList(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            String search) {
        return service.getPageList(pageNum, pageSize, search);
    }

    @ApiOperation(value = "获取列表")
    @GetMapping("/getList")
    @SysUserLogInterface(description = "获取角色列表")
    public ResultBean<List<SysRole>> getList() {
        return service.getList();
    }

    @ApiOperation(value = "获取角色拥有的功能列表")
    @GetMapping("/getFunctionList")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "Integer", paramType = "query"),
    })
    @SysUserLogInterface(description = "获取角色拥有的功能列表")
    public ResultBean<List<Integer>> getFunctionList(Integer id) {
        return service.getFunctionList(id);
    }

    @ApiOperation(value = "新增角色")
    @PostMapping("/add")
    @SysUserLogInterface(description = "新增角色", type = SysUserLogInterface.TYPE_INSERT)
    public ResultBean add(SysRole sysRole) {
        return service.add(sysRole);
    }

    @ApiOperation(value = "编辑角色")
    @PostMapping("/update")
    @SysUserLogInterface(description = "编辑角色", type = SysUserLogInterface.TYPE_UPDATE)
    public ResultBean update(SysRole sysRole) {
        return service.update(sysRole);
    }

    @ApiOperation(value = "删除角色")
    @PostMapping("/delete")
    @SysUserLogInterface(description = "删除角色", type = SysUserLogInterface.TYPE_DELETE)
    public ResultBean delete(Integer id) {
        return service.delete(id);
    }

    @ApiOperation(value = "修改角色功能")
    @PostMapping("/updateRoleFunction")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleId", value = "角色id", required = true, dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "functionIds", value = "功能ids", required = true, dataType = "Integer[]", paramType = "form"),
    })
    @SysUserLogInterface(description = "修改角色功能", type = SysUserLogInterface.TYPE_UPDATE)
    public ResultBean updateRoleFunction(Integer roleId, Integer[] functionIds) {
        return service.updateRoleFunction(roleId, functionIds);
    }

    @ApiOperation(value = "修改角色状态")
    @PostMapping("/updateStatus")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "status", value = "状态", required = true, dataType = "Integer", paramType = "form"),
    })
    @SysUserLogInterface(description = "修改角色状态", type = SysUserLogInterface.TYPE_UPDATE)
    public ResultBean updateStatus(Integer id, Integer status) {
        return service.updateStatus(id, status);
    }

}
