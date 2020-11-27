package com.weiziplus.muteki.core.pc.system.controller;

import com.weiziplus.muteki.common.result.ResultBean;
import com.weiziplus.muteki.common.util.PageUtils;
import com.weiziplus.muteki.core.pc.common.interceptor.PcAuthToken;
import com.weiziplus.muteki.core.pc.common.aop.SysUserLogInterface;
import com.weiziplus.muteki.core.pc.system.dto.SysUserDto;
import com.weiziplus.muteki.core.pc.system.dto.SysUserQueryDto;
import com.weiziplus.muteki.core.pc.system.service.SysUserService;
import com.weiziplus.muteki.core.pc.system.vo.SysUserVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Min;

/**
 * @author wanglongwei
 * @date 2020/07/30 11/30
 */
@Validated
@RestController
@PcAuthToken
@RequestMapping("/pc/sysUser")
@Api(tags = "系统用户")
public class SysUserController {

    @Autowired
    SysUserService service;

    @ApiOperation(value = "获取分页数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "pageNum", defaultValue = "1", dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "pageSize", defaultValue = "10", dataType = "Integer", paramType = "query"),
    })
    @GetMapping("/getPageList")
    @SysUserLogInterface(description = "获取系统用户分页数据")
    public ResultBean<PageUtils<SysUserVo>> getPageList(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            SysUserQueryDto sysUserQueryDto) {
        return service.getPageList(pageNum, pageSize, sysUserQueryDto);
    }

    @ApiOperation(value = "新增用户")
    @PostMapping("/add")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleIds", value = "角色ids", dataType = "Integer[]", paramType = "form"),
    })
    @SysUserLogInterface(description = "新增用户", type = SysUserLogInterface.TYPE_INSERT, paramIgnore = "__t,password")
    public ResultBean add(@Validated SysUserDto sysUserDto, Integer[] roleIds) {
        return service.add(sysUserDto, roleIds);
    }

    @ApiOperation(value = "修改用户手机号")
    @PostMapping("/updatePhone")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "phone", value = "手机号", required = true, dataType = "String", paramType = "form"),
    })
    @SysUserLogInterface(description = "修改用户手机号", type = SysUserLogInterface.TYPE_UPDATE)
    public ResultBean updatePhone(Integer id, String phone) {
        return service.updatePhone(id, phone);
    }

    @ApiOperation(value = "修改用户状态")
    @PostMapping("/updateStatus")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "status", value = "状态", required = true, dataType = "Integer", paramType = "form"),
    })
    @SysUserLogInterface(description = "修改用户状态", type = SysUserLogInterface.TYPE_UPDATE)
    public ResultBean updateStatus(Integer id, Integer status) {
        return service.updateStatus(id, status);
    }

    @ApiOperation(value = "修改用户角色")
    @PostMapping("/updateRole")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "roleIds", value = "角色ids", dataType = "Integer[]", paramType = "form"),
    })
    @SysUserLogInterface(description = "修改用户角色", type = SysUserLogInterface.TYPE_UPDATE)
    public ResultBean updateRole(Integer id, Integer[] roleIds) {
        return service.updateRole(id, roleIds);
    }

    @ApiOperation(value = "修改用户部门")
    @PostMapping("/updateDept")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "deptIds", value = "部门ids", dataType = "Integer[]", paramType = "form"),
    })
    @SysUserLogInterface(description = "修改用户部门", type = SysUserLogInterface.TYPE_UPDATE)
    public ResultBean updateDept(Integer id, Integer[] deptIds) {
        return service.updateDept(id, deptIds);
    }

    @ApiOperation(value = "删除用户")
    @PostMapping("/delete")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "Integer", paramType = "form"),
    })
    @SysUserLogInterface(description = "删除用户", type = SysUserLogInterface.TYPE_DELETE)
    public ResultBean delete(Integer id) {
        return service.delete(id);
    }

    @ApiOperation(value = "修改密码")
    @PostMapping("/updatePwd")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "oldPwd", value = "旧密码", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "newPwd", value = "新密码", required = true, dataType = "String", paramType = "form"),
    })
    @SysUserLogInterface(description = "修改密码", type = SysUserLogInterface.TYPE_UPDATE, paramIgnore = "__t,oldPwd,newPwd")
    public ResultBean updatePwd(String oldPwd, String newPwd) {
        return service.updatePwd(oldPwd, newPwd);
    }

    @ApiOperation(value = "重置密码")
    @PostMapping("/resetPwd")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "password", value = "新密码", required = true, dataType = "String", paramType = "form"),
    })
    @SysUserLogInterface(description = "重置密码", type = SysUserLogInterface.TYPE_UPDATE, paramIgnore = "__t,password")
    public ResultBean resetPwd(Integer id, String password) {
        return service.resetPwd(id, password);
    }

    @ApiOperation(value = "修改头像")
    @PostMapping("/updateIcon")
    @SysUserLogInterface(description = "修改头像", type = SysUserLogInterface.TYPE_UPDATE)
    public ResultBean updateIcon(MultipartFile file) {
        return service.updateIcon(file);
    }

}
