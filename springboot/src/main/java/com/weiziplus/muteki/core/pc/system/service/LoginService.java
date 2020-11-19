package com.weiziplus.muteki.core.pc.system.service;

import com.weiziplus.muteki.amap.ip.AmapIp;
import com.weiziplus.muteki.amap.ip.AmapIpApi;
import com.weiziplus.muteki.common.async.LogAsync;
import com.weiziplus.muteki.common.base.BaseService;
import com.weiziplus.muteki.common.base.BaseWhere;
import com.weiziplus.muteki.common.base.BaseWhereModel;
import com.weiziplus.muteki.common.config.GlobalConfig;
import com.weiziplus.muteki.common.models.*;
import com.weiziplus.muteki.common.result.ResultEnum;
import com.weiziplus.muteki.common.util.*;
import com.weiziplus.muteki.core.pc.common.enums.SysFunctionTypeEnum;
import com.weiziplus.muteki.common.result.ResultBean;
import com.weiziplus.muteki.common.util.RedisUtils;
import com.weiziplus.muteki.core.pc.common.config.PcGlobalConfig;
import com.weiziplus.muteki.core.pc.common.enums.SysUserStatusEnum;
import com.weiziplus.muteki.core.pc.common.token.PcJwtExpand;
import com.weiziplus.muteki.core.pc.common.token.PcTokenUtils;
import com.weiziplus.muteki.core.pc.system.dto.LoginDto;
import com.weiziplus.muteki.core.pc.system.vo.LoginVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

/**
 * @author wanglongwei
 * @date 2020/07/30 11/30
 */
@Slf4j
@Service
public class LoginService extends BaseService {

    @Autowired
    SysFunctionService sysFunctionService;

    @Autowired
    LogAsync logAsync;

    /**
     * 验证码redis的key
     */
    private final static String VALIDATE_CODE_REDIS_KEY = "pc:login:validateCode:";

    /**
     * 获取验证码
     *
     * @param uuid
     * @return
     */
    public ResultBean<String> getValidateCode(String uuid) {
        if (ToolUtils.isBlank(uuid)) {
            return ResultBean.error("uuid不能为空");
        }
        HttpServletRequest request = getRequest();
        String unknown = "Unknown";
        String borderName = UserAgentUtils.getBorderName(request);
        //代表没有获取到浏览器信息
        if (ToolUtils.isBlank(borderName) || unknown.equalsIgnoreCase(borderName)) {
            return ResultBean.errorRole("限制访问，如有疑问请联系管理员");
        }
        //同一ip和userAgent半分钟内只能获取10次验证码
        String ipRedisKey = "pc:login:validateCode:ip:" + Md5Utils.encode16(
                HttpRequestUtils.getIpAddress(request) + HttpRequestUtils.getUserAgent());
        Object object = RedisUtils.get(ipRedisKey);
        int num = 0;
        if (null != object) {
            num = (int) object;
        }
        num++;
        int getMaxNum = 10;
        if (getMaxNum < num) {
            return ResultBean.error("访问频率过快，请稍后重试");
        }
        RedisUtils.set(ipRedisKey, num, 30L);
        Map<String, Object> validateCode = ImageValidateCodeUtils.getValidateCode();
        String redisKey = VALIDATE_CODE_REDIS_KEY + uuid;
        RedisUtils.set(redisKey, validateCode.get("random"), 60L);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            ImageIO.write((BufferedImage) validateCode.get("image"), "png", os);
        } catch (IOException e) {
            log.warn("系统用户登录，图片验证码处理出错，详情:" + e);
            return ResultBean.errorException("系统错误，请重试。IOException", e);
        }
        Base64.Encoder encoder = Base64.getEncoder();
        byte[] bytes = os.toByteArray();
        String encode = "data:image/png;base64," + encoder.encodeToString(bytes);
        return ResultBean.success(encode);
    }

    /**
     * 登录
     *
     * @param loginDto
     * @return
     */
    public ResultBean<LoginVo> login(LoginDto loginDto) {
        String redisKey = VALIDATE_CODE_REDIS_KEY + loginDto.getUuid();
        Object object = RedisUtils.get(redisKey);
        if (null == object) {
            return ResultBean.error("验证码已过期");
        }
        RedisUtils.delete(redisKey);
        if (!loginDto.getCode().equalsIgnoreCase(String.valueOf(object))) {
            return ResultBean.error("验证码错误");
        }
        //根据用户名获取删除时间等于0的用户信息
        BaseWhere<SysUser> baseWhere = new BaseWhere<>(SysUser.class)
                .where(new BaseWhereModel(SysUser.COLUMN_USERNAME, loginDto.getUsername()))
                .where(new BaseWhereModel(SysUser.COLUMN_DELETE_TIME, 0));
        SysUser sysUser = baseFindOneData(baseWhere);
        SysUserLogin sysUserLogin = new SysUserLogin()
                .setUsername(loginDto.getUsername());
        if (null == sysUser) {
            sysUserLogin.setResultCode(ResultEnum.ERROR.getValue())
                    .setResultMsg("用户名错误");
            saveLoginLog(sysUserLogin);
            return ResultBean.error("用户名错误");
        }
        if (!sysUser.getPassword().equals(Md5Utils.encode(loginDto.getPassword()))) {
            sysUserLogin.setResultCode(ResultEnum.ERROR.getValue())
                    .setResultMsg("密码错误");
            saveLoginLog(sysUserLogin);
            return ResultBean.error("密码错误");
        }
        if (SysUserStatusEnum.DISABLE.getValue().equals(sysUser.getStatus())) {
            sysUserLogin.setResultCode(ResultEnum.ERROR.getValue())
                    .setResultMsg("您的账号已停用，如有疑问请联系管理员");
            saveLoginLog(sysUserLogin);
            return ResultBean.error("您的账号已停用，如有疑问请联系管理员");
        }
        //获取用户的角色列表
        List<SysUserRole> sysUserRoleList = baseFindListByClassAndColumnAndValue(
                SysUserRole.class, SysUserRole.COLUMN_USER_ID, sysUser.getId());
        if (null == sysUserRoleList || 0 >= sysUserRoleList.size()) {
            sysUserLogin.setResultCode(ResultEnum.ERROR.getValue())
                    .setResultMsg("您还没有角色，请联系管理员添加");
            saveLoginLog(sysUserLogin);
            return ResultBean.error("您还没有角色，请联系管理员添加");
        }
        Set<Integer> roleIds = new HashSet<>(ToolUtils.initialCapacity(sysUserRoleList.size()));
        for (SysUserRole sysUserRole : sysUserRoleList) {
            roleIds.add(sysUserRole.getRoleId());
        }
        HttpServletRequest request = getRequest();
        PcJwtExpand expand = new PcJwtExpand()
                .setRoleIds(roleIds);
        String token = PcTokenUtils.createToken(sysUser.getId(), request, expand);
        sysUser.setPassword(null)
                .setIcon(GlobalConfig.getMybatisFilePathPrefix() + sysUser.getIcon());
        //拥有的菜单列表
        List<SysFunction> menuTreeByRoleIds;
        //拥有的按钮列表
        Set<String> buttonNameSetByRoleIds;
        //如果当前用户是超级管理员,拥有所有的功能
        if (PcGlobalConfig.SUPER_ADMIN_USER_ID.equals(sysUser.getId())) {
            menuTreeByRoleIds = sysFunctionService.getAllMenuTree();
            List<SysFunction> sysFunctionList = baseFindListByClassAndColumnAndValue(
                    SysFunction.class, SysFunction.COLUMN_TYPE, SysFunctionTypeEnum.BUTTON.getValue());
            buttonNameSetByRoleIds = new HashSet<>(ToolUtils.initialCapacity(sysFunctionList.size()));
            for (SysFunction sysFunction : sysFunctionList) {
                buttonNameSetByRoleIds.add(sysFunction.getName());
            }
        } else {
            menuTreeByRoleIds = sysFunctionService.getMenuTreeByRoleIds(roleIds);
            buttonNameSetByRoleIds = sysFunctionService.getButtonSetByRoleIds(roleIds);
        }
        LoginVo vo = new LoginVo()
                .setToken(token)
                .setUser(sysUser)
                .setRoleIds(roleIds)
                .setMenuTree(menuTreeByRoleIds)
                .setButtonSet(buttonNameSetByRoleIds);
        sysUserLogin.setResultCode(ResultEnum.SUCCESS.getValue())
                .setResultMsg("success");
        saveLoginLog(sysUserLogin);
        return ResultBean.success(vo);
    }

    /**
     * 保存登录日志
     *
     * @param login
     */
    private void saveLoginLog(SysUserLogin login) {
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
        Integer userId = PcTokenUtils.getUserId();
        PcTokenUtils.deleteToken(userId);
        return ResultBean.success();
    }

}