package com.weiziplus.muteki.core.pc.system.controller;

import com.weiziplus.muteki.common.result.ResultBean;
import com.weiziplus.muteki.common.util.PageUtils;
import com.weiziplus.muteki.core.pc.common.aop.SysUserLogInterface;
import com.weiziplus.muteki.core.pc.common.interceptor.PcSuperAdminAuth;
import com.weiziplus.muteki.core.pc.system.dto.SysUserLogQueryDto;
import com.weiziplus.muteki.core.pc.system.service.SysUserLogService;
import com.weiziplus.muteki.core.pc.system.vo.SysUserLogVo;
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
 * @date 2020/08/17 15/44
 */
@RestController
@PcSuperAdminAuth
@RequestMapping("/pc/sysUserLog")
@Api(tags = "系统用户日志")
public class SysUserLogController {

    @Autowired
    SysUserLogService service;

    @ApiOperation(value = "获取分页数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "pageNum", defaultValue = "1", dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "pageSize", defaultValue = "10", dataType = "Integer", paramType = "query"),
    })
    @GetMapping("/getPageList")
    @SysUserLogInterface(description = "获取系统用户日志分页数据")
    public ResultBean<PageUtils<SysUserLogVo>> getPageList(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            SysUserLogQueryDto sysUserLogQueryDto) {
        return service.getPageList(pageNum, pageSize, sysUserLogQueryDto);
    }

}
