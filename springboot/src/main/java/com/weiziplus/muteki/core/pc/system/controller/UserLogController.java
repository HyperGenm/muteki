package com.weiziplus.muteki.core.pc.system.controller;

import com.weiziplus.muteki.common.result.ResultBean;
import com.weiziplus.muteki.common.util.PageUtils;
import com.weiziplus.muteki.core.pc.common.aop.SysUserLogInterface;
import com.weiziplus.muteki.core.pc.common.interceptor.PcSuperAdminAuth;
import com.weiziplus.muteki.core.pc.system.service.UserLogService;
import com.weiziplus.muteki.core.pc.system.vo.UserLogVo;
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
 * @date 2020/08/20 09/33
 */
@RestController
@PcSuperAdminAuth
@RequestMapping("/pc/userLog")
@Api(tags = "用户日志")
public class UserLogController {

    @Autowired
    UserLogService service;

    @ApiOperation(value = "获取分页数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "pageNum", defaultValue = "1", dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "pageSize", defaultValue = "10", dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "username", value = "用户名", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "realName", value = "真实姓名", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "type", value = "类型", dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "resultCode", value = "状态码", dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "description", value = "操作", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "ipAddress", value = "ip地址", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "borderName", value = "浏览器", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "osName", value = "操作系统", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "startTime", value = "起始时间", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "endTime", value = "截止时间", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "createTimeSort", value = "创建时间排序", dataType = "String", paramType = "query"),
    })
    @GetMapping("/getPageList")
    @SysUserLogInterface(description = "获取web用户日志分页数据")
    public ResultBean<PageUtils<UserLogVo>> getPageList(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            String username, String realName, Integer type, Integer resultCode, String description, String ipAddress,
            String borderName, String osName, String startTime, String endTime,
            @RequestParam(defaultValue = "DESC") String createTimeSort) {
        return service.getPageList(pageNum, pageSize,
                username, realName, type, resultCode, description, ipAddress, borderName, osName, startTime, endTime,
                createTimeSort);
    }

}
