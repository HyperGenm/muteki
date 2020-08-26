package com.weiziplus.muteki.core.pc.system.controller;

import com.weiziplus.muteki.common.result.ResultBean;
import com.weiziplus.muteki.common.util.PageUtils;
import com.weiziplus.muteki.core.pc.common.aop.SysUserLogInterface;
import com.weiziplus.muteki.core.pc.common.interceptor.PcAuthToken;
import com.weiziplus.muteki.core.pc.system.dto.UserQueryDto;
import com.weiziplus.muteki.core.pc.system.service.UserService;
import com.weiziplus.muteki.core.pc.system.vo.UserVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author wanglongwei
 * @date 2020/08/21 15/08
 */
@RestController
@PcAuthToken
@RequestMapping("/pc/user")
@Api(tags = "web用户")
public class UserController {

    @Autowired
    UserService service;

    @ApiOperation(value = "获取分页数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "pageNum", defaultValue = "1", dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "pageSize", defaultValue = "10", dataType = "Integer", paramType = "query"),
    })
    @GetMapping("/getPageList")
    @SysUserLogInterface(description = "获取web用户分页数据")
    public ResultBean<PageUtils<UserVo>> getPageList(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            UserQueryDto userQueryDto) {
        return service.getPageList(pageNum, pageSize, userQueryDto);
    }

    @ApiOperation(value = "禁用账户")
    @PostMapping("/disableUser")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "Long", paramType = "form"),
    })
    @SysUserLogInterface(description = "禁用web用户", type = SysUserLogInterface.TYPE_UPDATE)
    public ResultBean disableUser(Long id) {
        return service.disableUser(id);
    }

    @ApiOperation(value = "启用账户")
    @PostMapping("/enableUser")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "Long", paramType = "form"),
    })
    @SysUserLogInterface(description = "启用web用户", type = SysUserLogInterface.TYPE_UPDATE)
    public ResultBean enableUser(Long id) {
        return service.enableUser(id);
    }


}
