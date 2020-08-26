package com.weiziplus.muteki.core.pc.system.controller;

import com.weiziplus.muteki.common.result.ResultBean;
import com.weiziplus.muteki.common.util.PageUtils;
import com.weiziplus.muteki.core.pc.common.aop.SysUserLogInterface;
import com.weiziplus.muteki.core.pc.common.interceptor.PcSuperAdminAuth;
import com.weiziplus.muteki.core.pc.system.dto.UserLogQueryDto;
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
    })
    @GetMapping("/getPageList")
    @SysUserLogInterface(description = "获取web用户日志分页数据")
    public ResultBean<PageUtils<UserLogVo>> getPageList(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            UserLogQueryDto userLogQueryDto) {
        return service.getPageList(pageNum, pageSize,userLogQueryDto);
    }

}
