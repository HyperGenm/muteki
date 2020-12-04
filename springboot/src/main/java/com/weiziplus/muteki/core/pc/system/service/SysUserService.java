package com.weiziplus.muteki.core.pc.system.service;

import com.github.pagehelper.PageHelper;
import com.weiziplus.muteki.common.base.BaseService;
import com.weiziplus.muteki.common.base.BaseWhere;
import com.weiziplus.muteki.common.base.BaseWhereModel;
import com.weiziplus.muteki.common.config.GlobalConfig;
import com.weiziplus.muteki.common.models.SysUser;
import com.weiziplus.muteki.common.models.SysUserDept;
import com.weiziplus.muteki.common.models.SysUserRole;
import com.weiziplus.muteki.common.result.ResultBean;
import com.weiziplus.muteki.common.result.ResultEnum;
import com.weiziplus.muteki.common.util.*;
import com.weiziplus.muteki.core.pc.common.config.PcGlobalConfig;
import com.weiziplus.muteki.core.pc.common.enums.SysUserStatusEnum;
import com.weiziplus.muteki.core.pc.common.token.PcTokenUtils;
import com.weiziplus.muteki.core.pc.system.dto.SysUserDto;
import com.weiziplus.muteki.core.pc.system.dto.SysUserQueryDto;
import com.weiziplus.muteki.core.pc.system.mapper.SysUserMapper;
import com.weiziplus.muteki.core.pc.system.vo.SysUserVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wanglongwei
 * @date 2020/08/04 17/01
 */
@Slf4j
@Service
public class SysUserService extends BaseService {

    @Autowired
    SysUserMapper mapper;

    /**
     * 获取分页数据
     *
     * @param pageNum
     * @param pageSize
     * @param sysUserQueryDto
     * @return
     */
    public ResultBean<PageUtils<SysUserVo>> getPageList(Integer pageNum, Integer pageSize, SysUserQueryDto sysUserQueryDto) {
        if (!containsOrderByType(sysUserQueryDto.getCreateTimeSort(), sysUserQueryDto.getLastActiveTimeSort())) {
            return ResultBean.error("排序类型错误");
        }
        PageHelper.startPage(pageNum, pageSize);
        PageUtils<SysUserVo> pageUtil = PageUtils.pageInfo(mapper.getListVo(sysUserQueryDto));
        return ResultBean.success(pageUtil);
    }

    /**
     * 新增用户
     *
     * @param sysUserDto
     * @param roleIds
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ResultBean add(SysUserDto sysUserDto, Integer[] roleIds) {
        if (!SysUserStatusEnum.contains(sysUserDto.getStatus())) {
            return ResultBean.error("状态错误");
        }
        //根据用户名获取删除时间等于0的用户信息
        BaseWhere<SysUser> baseWhere = new BaseWhere<>(SysUser.class)
                .where(new BaseWhereModel(SysUser.COLUMN_USERNAME, sysUserDto.getUsername()))
                .where(new BaseWhereModel(SysUser.COLUMN_DELETE_TIME, 0));
        SysUser sysUser = baseFindOneData(baseWhere);
        if (null != sysUser) {
            return ResultBean.error("用户名已存在");
        }
        SysUser newSysUser = new SysUser();
        BeanUtils.copyProperties(sysUserDto, newSysUser);
        newSysUser.setPassword(Md5Utils.encode(sysUserDto.getPassword()))
                .setCreateTime(DateUtils.getNowDateTime());
        //如果没有指定角色
        if (null == roleIds || 0 >= roleIds.length) {
            baseInsert(newSysUser);
            return ResultBean.success();
        }
        List<SysUserRole> sysUserRoleList = new ArrayList<>(ToolUtils.initialCapacity(roleIds.length));
        //指定了角色
        Object savepoint = TransactionAspectSupport.currentTransactionStatus().createSavepoint();
        try {
            baseInsert(newSysUser, true);
            for (Integer roleId : roleIds) {
                if (PcGlobalConfig.SUPER_ADMIN_ROLE_ID.equals(roleId)) {
                    TransactionAspectSupport.currentTransactionStatus().rollbackToSavepoint(savepoint);
                    return ResultBean.error("超级管理员只能有一个");
                }
                SysUserRole sysUserRole = new SysUserRole()
                        .setUserId(newSysUser.getId())
                        .setRoleId(roleId);
                sysUserRoleList.add(sysUserRole);
            }
            baseInsertList(sysUserRoleList);
        } catch (Exception e) {
            log.warn("新增系统用户出错，详情:" + e);
            TransactionAspectSupport.currentTransactionStatus().rollbackToSavepoint(savepoint);
            return ResultBean.errorException("系统错误，请重试。Exception", e);
        }
        return ResultBean.success();
    }

    /**
     * 修改手机号
     *
     * @param id
     * @param phone
     * @return
     */
    public ResultBean updatePhone(Integer id, String phone) {
        if (null == id || 0 >= id) {
            return ResultBean.error("id错误");
        }
        if (ToolUtils.isBlank(phone)) {
            return ResultBean.error("手机号码不能为空");
        }
        SysUser sysUser = new SysUser()
                .setId(id)
                .setPhone(phone);
        baseUpdate(sysUser);
        return ResultBean.success();
    }

    /**
     * 修改用户状态
     *
     * @param id
     * @param status
     * @return
     */
    public ResultBean updateStatus(Integer id, Integer status) {
        if (null == id || 0 >= id) {
            return ResultBean.error("id错误");
        }
        if (PcGlobalConfig.SUPER_ADMIN_USER_ID.equals(id)) {
            return ResultBean.errorRole("禁止操作超级管理员");
        }
        if (!SysUserStatusEnum.contains(status)) {
            return ResultBean.error("状态错误");
        }
        SysUser sysUser = new SysUser()
                .setId(id)
                .setStatus(status);
        if (SysUserStatusEnum.NORMAL.getValue().equals(status)) {
            baseUpdate(sysUser);
            return ResultBean.success();
        }
        //如果是禁用状态,强制用户下线
        PcTokenUtils.deleteToken(id);
        baseUpdate(sysUser);
        return ResultBean.success();
    }

    /**
     * 修改用户角色
     *
     * @param id
     * @param roleIds
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ResultBean updateRole(Integer id, Integer[] roleIds) {
        if (null == id || 0 >= id) {
            return ResultBean.error("id错误");
        }
        if (PcGlobalConfig.SUPER_ADMIN_USER_ID.equals(id)) {
            return ResultBean.errorRole("禁止操作超级管理员");
        }
        if (null == roleIds || 0 >= roleIds.length) {
            mapper.deleteUserRoleByUserId(id);
            return ResultBean.success();
        }
        List<SysUserRole> sysUserRoleList = new ArrayList<>(ToolUtils.initialCapacity(roleIds.length));
        for (Integer roleId : roleIds) {
            if (PcGlobalConfig.SUPER_ADMIN_ROLE_ID.equals(roleId)) {
                return ResultBean.error("超级管理员只能有一个");
            }
            SysUserRole sysUserRole = new SysUserRole()
                    .setUserId(id)
                    .setRoleId(roleId);
            sysUserRoleList.add(sysUserRole);
        }
        //强制用户下线
        PcTokenUtils.deleteToken(id);
        Object savepoint = TransactionAspectSupport.currentTransactionStatus().createSavepoint();
        try {
            mapper.deleteUserRoleByUserId(id);
            baseInsertList(sysUserRoleList);
        } catch (Exception e) {
            log.warn("修改用户角色出错，详情:" + e);
            TransactionAspectSupport.currentTransactionStatus().rollbackToSavepoint(savepoint);
            return ResultBean.errorException("系统错误，请重试。Exception", e);
        }
        return ResultBean.success();
    }

    /**
     * 修改用户部门
     *
     * @param id
     * @param deptIds
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ResultBean updateDept(Integer id, Integer[] deptIds) {
        if (null == id || 0 >= id) {
            return ResultBean.error("id错误");
        }
        if (PcGlobalConfig.SUPER_ADMIN_USER_ID.equals(id)) {
            return ResultBean.errorRole("禁止操作超级管理员");
        }
        if (null == deptIds || 0 >= deptIds.length) {
            mapper.deleteUserDeptByUserId(id);
            return ResultBean.success();
        }
        List<SysUserDept> list = new ArrayList<>(ToolUtils.initialCapacity(deptIds.length));
        for (Integer deptId : deptIds) {
            SysUserDept sysUserDept = new SysUserDept()
                    .setUserId(id)
                    .setDeptId(deptId);
            list.add(sysUserDept);
        }
        //强制用户下线
        PcTokenUtils.deleteToken(id);
        Object savepoint = TransactionAspectSupport.currentTransactionStatus().createSavepoint();
        try {
            mapper.deleteUserDeptByUserId(id);
            baseInsertList(list);
        } catch (Exception e) {
            log.warn("修改用户部门出错，详情:" + e);
            TransactionAspectSupport.currentTransactionStatus().rollbackToSavepoint(savepoint);
            return ResultBean.errorException("系统错误，请重试。Exception", e);
        }
        return ResultBean.success();
    }

    /**
     * 删除用户
     *
     * @param id
     * @return
     */
    public ResultBean delete(Integer id) {
        if (null == id || 0 >= id) {
            return ResultBean.error("id错误");
        }
        if (PcGlobalConfig.SUPER_ADMIN_USER_ID.equals(id)) {
            return ResultBean.errorRole("禁止操作超级管理员");
        }
        //用户有过操作记录，停用用户，并强制用户下线
        SysUser sysUser = new SysUser()
                .setId(id)
                .setDeleteTime(DateUtils.getNowDateTimeNum());
        PcTokenUtils.deleteToken(id);
        baseUpdate(sysUser);
        return ResultBean.success();
    }

    /**
     * 修改密码
     *
     * @param oldPwd
     * @param newPwd
     * @return
     */
    public ResultBean updatePwd(String oldPwd, String newPwd) {
        if (ToolUtils.isBlank(oldPwd)) {
            return ResultBean.error("旧密码不能为空");
        }
        if (ToolUtils.isBlank(newPwd)) {
            return ResultBean.error("新密码不能为空");
        }
        Integer userId = PcTokenUtils.getUserId();
        SysUser sysUser = baseFindByClassAndId(SysUser.class, userId);
        if (!sysUser.getPassword().equals(Md5Utils.encode(oldPwd))) {
            return ResultBean.error("原密码错误");
        }
        sysUser.setPassword(Md5Utils.encode(newPwd));
        baseUpdate(sysUser);
        PcTokenUtils.deleteToken(userId);
        return ResultBean.success();
    }

    /**
     * 重置密码
     *
     * @param id
     * @param password
     * @return
     */
    public ResultBean resetPwd(Integer id, String password) {
        if (null == id || 0 >= id) {
            return ResultBean.error("id错误");
        }
        if (PcGlobalConfig.SUPER_ADMIN_USER_ID.equals(id)) {
            return ResultBean.errorRole("禁止操作超级管理员");
        }
        if (ToolUtils.isBlank(password)) {
            return ResultBean.error("密码不能为空");
        }
        SysUser sysUser = new SysUser()
                .setId(id)
                .setPassword(Md5Utils.encode(password));
        baseUpdate(sysUser);
        PcTokenUtils.deleteToken(id);
        return ResultBean.success();
    }

    /**
     * 修改头像
     *
     * @param file
     * @return
     */
    public ResultBean updateIcon(MultipartFile file) {
        BufferedImage image = FileUtils.getImage(file);
        if (null == image) {
            return ResultBean.error("请上传图片");
        }
        //长宽比
        float minScale = 0.7F;
        float maxScale = 1.2F;
        double scale = 1F;
        if (0 != image.getWidth()) {
            scale = (float) image.getHeight() / image.getWidth();
        }
        if (scale < minScale || scale > maxScale) {
            return ResultBean.error("头像建议长宽比1:1");
        }
        ResultBean<String> stringResultBean = FileUtils.upImage(file, "user/icon");
        if (!ResultEnum.SUCCESS.getValue().equals(stringResultBean.getCode())) {
            return ResultBean.error(stringResultBean.getMsg());
        }
        Integer userId = PcTokenUtils.getUserId();
        SysUser sysUser = new SysUser()
                .setId(userId)
                .setIcon(stringResultBean.getData());
        baseUpdate(sysUser);
        return ResultBean.success(GlobalConfig.getMybatisFilePathPrefix() + stringResultBean.getData());
    }
}