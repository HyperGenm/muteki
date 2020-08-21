package com.weiziplus.muteki.core.pc.system.controller;

import com.weiziplus.muteki.common.result.ResultBean;
import com.weiziplus.muteki.core.pc.common.interceptor.PcAuthToken;
import com.weiziplus.muteki.core.pc.common.aop.SysUserLogInterface;
import com.weiziplus.muteki.core.pc.system.dto.LoginDto;
import com.weiziplus.muteki.core.pc.system.service.LoginService;
import com.weiziplus.muteki.core.pc.system.vo.LoginVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author wanglongwei
 * @date 2020/07/30 16/05
 */
@Api(tags = "登录")
@RestController
@RequestMapping("/pc")
public class LoginController {

    @Autowired
    LoginService service;

    @ApiOperation(value = "获取图片验证码")
    @GetMapping("/getValidateCode")
    public ResultBean<String> getValidateCode(String uuid) {
        return service.getValidateCode(uuid);
    }

    @ApiOperation(value = "登录")
    @PostMapping("/login")
    public ResultBean<LoginVo> login(@Validated LoginDto loginDto) {
        return service.login(loginDto);
    }

    @ApiOperation(value = "用户退出登录")
    @PcAuthToken
    @PostMapping("/logout")
    @SysUserLogInterface(description = "用户退出登录", type = SysUserLogInterface.TYPE_UPDATE)
    public ResultBean logout() {
        return service.logout();
    }

}