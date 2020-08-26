package com.weiziplus.muteki.core.api.user.controller;

import com.weiziplus.muteki.common.result.ResultBean;
import com.weiziplus.muteki.core.api.common.aop.UserLogInterface;
import com.weiziplus.muteki.core.api.common.interceptor.WebAuthToken;
import com.weiziplus.muteki.core.api.user.service.LoginService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * @author wanglongwei
 * @date 2020/08/20 09/14
 */
@Validated
@Api(tags = "登录")
@RestController
@RequestMapping("/api")
public class LoginController {

    @Autowired
    LoginService service;


    @ApiOperation(value = "登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "password", value = "密码", dataType = "String", paramType = "form"),
    })
    @PostMapping("/login")
    public ResultBean<String> login(
            @NotBlank(message = "用户名不能为空")
            @Size(min = 2, message = "用户名最少两位") String username,
            @NotBlank(message = "密码不能为空")
            @Size(min = 2, message = "密码最少6位") String password) {
        return service.login(username, password);
    }


    @ApiOperation(value = "app登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "password", value = "密码", dataType = "String", paramType = "form"),
    })
    @PostMapping("/appLogin")
    public ResultBean<String> appLogin(
            @NotBlank(message = "用户名不能为空")
            @Size(min = 2, message = "用户名最少两位") String username,
            @NotBlank(message = "密码不能为空")
            @Size(min = 2, message = "密码最少6位") String password) {
        return service.appLogin(username, password);
    }

    @ApiOperation(value = "用户退出登录")
    @WebAuthToken
    @PostMapping("/logout")
    @UserLogInterface(description = "用户退出登录", type = UserLogInterface.TYPE_UPDATE)
    public ResultBean logout() {
        return service.logout();
    }

}
