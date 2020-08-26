package com.weiziplus.muteki.core.pc.system.controller;

import com.weiziplus.muteki.common.models.UserLogin;
import com.weiziplus.muteki.common.result.ResultBean;
import com.weiziplus.muteki.common.util.PageUtils;
import com.weiziplus.muteki.core.pc.common.aop.SysUserLogInterface;
import com.weiziplus.muteki.core.pc.common.interceptor.PcSuperAdminAuth;
import com.weiziplus.muteki.core.pc.system.dto.UserLoginLogQueryDto;
import com.weiziplus.muteki.core.pc.system.service.UserLoginLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wanglongwei
 * @date 2020/08/20 10/30
 */
@RestController
@PcSuperAdminAuth
@RequestMapping("/pc/userLoginLog")
@Api(tags = "用户登录日志")
public class UserLoginLogController {

    @Autowired
    UserLoginLogService service;

    @ApiOperation(value = "获取分页数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "pageNum", defaultValue = "1", dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "pageSize", defaultValue = "10", dataType = "Integer", paramType = "query"),
    })
    @GetMapping("/getPageList")
    @SysUserLogInterface(description = "获取用户登录日志分页数据")
    public ResultBean<PageUtils<UserLogin>> getPageList(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            UserLoginLogQueryDto userLoginLogQueryDto) {
        return service.getPageList(pageNum, pageSize, userLoginLogQueryDto);
    }

}
