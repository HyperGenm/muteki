package com.weiziplus.muteki.core.api.user.controller;

import com.weiziplus.muteki.common.result.ResultBean;
import com.weiziplus.muteki.core.api.common.aop.UserLogInterface;
import com.weiziplus.muteki.core.api.common.interceptor.WebAuthToken;
import com.weiziplus.muteki.core.api.user.service.UserService;
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
 * @date 2020/08/25 17/02
 */
@Validated
@WebAuthToken
@Api(tags = "用户管理")
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    UserService service;

    @ApiOperation(value = "获取用户信息")
    @GetMapping("/getInfo")
    @UserLogInterface(description = "获取用户信息")
    public ResultBean getInfo() {
        return service.getInfo();
    }

}
