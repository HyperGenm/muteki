package com.weiziplus.muteki.core.api.user.service;

import com.weiziplus.muteki.amap.ip.AmapIp;
import com.weiziplus.muteki.amap.ip.AmapIpApi;
import com.weiziplus.muteki.common.async.LogAsync;
import com.weiziplus.muteki.common.base.BaseService;
import com.weiziplus.muteki.common.models.User;
import com.weiziplus.muteki.common.models.UserLogin;
import com.weiziplus.muteki.common.result.ResultBean;
import com.weiziplus.muteki.common.result.ResultEnum;
import com.weiziplus.muteki.common.util.DateUtils;
import com.weiziplus.muteki.common.util.HttpRequestUtils;
import com.weiziplus.muteki.common.util.UserAgentUtils;
import com.weiziplus.muteki.core.api.common.enums.UserStatusEnum;
import com.weiziplus.muteki.core.api.common.token.WebJwtExpand;
import com.weiziplus.muteki.core.api.common.token.WebTerminalEnum;
import com.weiziplus.muteki.core.api.common.token.WebTokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 * @author wanglongwei
 * @date 2020/08/20 09/15
 */
@Slf4j
@Service
public class LoginService extends BaseService {

    @Autowired
    LogAsync logAsync;

    /**
     * 登录
     *
     * @param username
     * @param password
     * @return
     */
    public ResultBean<String> login(String username, String password) {
        User user = baseFindOneDataByClassAndColumnAndValue(
                User.class, User.COLUMN_USERNAME, username);
        UserLogin userLogin = new UserLogin()
                .setUsername(username)
                .setTerminal(WebTerminalEnum.WEB.getName());
        if (null == user) {
            userLogin.setResultCode(ResultEnum.ERROR.getValue())
                    .setResultMsg("用户名或密码错误");
            saveLoginLog(userLogin);
            return ResultBean.error("用户名或密码错误");
        }
        /*if (!user.getPassword().equals(Md5Utils.encode(password))) {
            userLogin.setResultCode(ResultEnum.ERROR.getValue())
                    .setResultMsg("用户名或密码错误");
            saveLoginLog(userLogin);
            return ResultBean.error("用户名或密码错误");
        }*/
        //不是正常状态
        if (!UserStatusEnum.NORMAL.getValue().equals(user.getStatus())) {
            userLogin.setResultCode(ResultEnum.ERROR.getValue())
                    .setResultMsg("您的账户已"
                            + UserStatusEnum.getName(user.getStatus()));
            saveLoginLog(userLogin);
            return ResultBean.error("您的账户已"
                    + UserStatusEnum.getName(user.getStatus()));
        }
        HttpServletRequest request = getRequest();
        WebJwtExpand expand = new WebJwtExpand()
                .setUsername(user.getUsername())
                .setTerminalEnum(WebTerminalEnum.WEB);
        String token = WebTokenUtils.createToken(
                WebTerminalEnum.WEB, user.getId(), request, expand);
        userLogin.setResultCode(ResultEnum.SUCCESS.getValue())
                .setResultMsg("success");
        saveLoginLog(userLogin);
        return ResultBean.success(token);
    }

    /**
     * app登录
     *
     * @param username
     * @param password
     * @return
     */
    public ResultBean<String> appLogin(String username, String password) {
        User user = baseFindOneDataByClassAndColumnAndValue(
                User.class, User.COLUMN_USERNAME, username);
        HttpServletRequest request = getRequest();
        WebJwtExpand expand = new WebJwtExpand()
                .setUsername(user.getUsername())
                .setTerminalEnum(WebTerminalEnum.APP);
        String token = WebTokenUtils.createToken(
                WebTerminalEnum.APP, user.getId(), request, expand);
        UserLogin userLogin = new UserLogin()
                .setUsername(username)
                .setTerminal(WebTerminalEnum.WEB.getName())
                .setResultCode(ResultEnum.SUCCESS.getValue())
                .setResultMsg("success");
        saveLoginLog(userLogin);
        return ResultBean.success(token);
    }

    /**
     * 保存登录日志
     *
     * @param login
     */
    private void saveLoginLog(UserLogin login) {
        HttpServletRequest request = getRequest();
        String ipAddress = HttpRequestUtils.getIpAddress(request);
        ResultBean<AmapIp> location = AmapIpApi.getLocation(ipAddress);
        //如果请求成功
        if (ResultEnum.SUCCESS.getValue().equals(location.getCode())) {
            login.setLoginProvince(location.getData().getProvince())
                    .setLoginCity(location.getData().getCity());
        } else {
            login.setLoginProvince("未知")
                    .setLoginCity("未知");
        }
        login.setBorderName(UserAgentUtils.getBorderName(request))
                .setOsName(UserAgentUtils.getOsName(request))
                .setIpAddress(ipAddress)
                .setCreateTime(DateUtils.getNowDateTime());
        logAsync.saveLoginLog(login);
    }

    /**
     * 退出登录
     *
     * @return
     */
    public ResultBean logout() {
        Long userId = WebTokenUtils.getUserId();
        WebTerminalEnum terminalEnum = WebTokenUtils.getExpand().getTerminalEnum();
        WebTokenUtils.deleteToken(terminalEnum, userId);
        return ResultBean.success();
    }

}
