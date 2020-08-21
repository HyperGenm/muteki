package com.weiziplus.muteki.core.pc.system.controller;

import com.weiziplus.muteki.common.result.ResultBean;
import com.weiziplus.muteki.core.pc.common.aop.SysUserLogInterface;
import com.weiziplus.muteki.core.pc.common.interceptor.PcSuperAdminAuth;
import com.weiziplus.muteki.core.pc.system.service.SysFileService;
import com.weiziplus.muteki.core.pc.system.vo.LogFileVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author wanglongwei
 * @date 2020/08/18 17/20
 */
@PcSuperAdminAuth
@Api(tags = "系统文件")
@RestController
@RequestMapping("/pc/sysFile")
public class SysFileController {

    @Autowired
    SysFileService service;

    @ApiOperation(value = "获取日志文件")
    @GetMapping("/getLogFile")
    @SysUserLogInterface(description = "获取日志文件")
    public ResultBean<List<LogFileVo>> getLogFile() {
        return service.getLogFile();
    }

    @ApiOperation(value = "下载日志文件")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dir", value = "目录", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "name", value = "文件名", required = true, dataType = "String", paramType = "form"),
    })
    @PostMapping("/downLogFile")
    @SysUserLogInterface(description = "下载日志文件")
    public void downLogFile(String dir, String name) {
        service.downLogFile(dir, name);
    }

}
