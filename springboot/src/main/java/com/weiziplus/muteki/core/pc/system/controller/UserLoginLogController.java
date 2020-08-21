package com.weiziplus.muteki.core.pc.system.controller;

import com.weiziplus.muteki.common.models.UserLogin;
import com.weiziplus.muteki.common.result.ResultBean;
import com.weiziplus.muteki.common.util.PageUtils;
import com.weiziplus.muteki.core.pc.common.aop.SysUserLogInterface;
import com.weiziplus.muteki.core.pc.common.interceptor.PcSuperAdminAuth;
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
            @ApiImplicitParam(name = "username", value = "用户名", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "loginProvince", value = "省份", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "loginCity", value = "城市", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "ipAddress", value = "ip", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "result", value = "结果", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "osName", value = "操作系统", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "borderName", value = "浏览器", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "startTime", value = "起始时间", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "endTime", value = "截止时间", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "createTimeSort", value = "创建时间排序", dataType = "String", paramType = "query"),
    })
    @GetMapping("/getPageList")
    @SysUserLogInterface(description = "获取用户登录日志分页数据")
    public ResultBean<PageUtils<UserLogin>> getPageList(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            String username, String loginProvince, String loginCity, String ipAddress, String result,
            String osName, String borderName, String startTime, String endTime,
            String createTimeSort) {
        return service.getPageList(pageNum, pageSize,
                username, loginProvince, loginCity, ipAddress, result,
                osName, borderName, startTime, endTime,
                createTimeSort);
    }

}
