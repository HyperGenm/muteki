package com.weiziplus.muteki.core.pc.system.service;

import com.github.pagehelper.PageHelper;
import com.weiziplus.muteki.common.base.BaseService;
import com.weiziplus.muteki.common.base.BaseWhere;
import com.weiziplus.muteki.common.base.BaseWhereEnum;
import com.weiziplus.muteki.common.base.BaseWhereModel;
import com.weiziplus.muteki.common.models.SysFunction;
import com.weiziplus.muteki.common.models.SysRole;
import com.weiziplus.muteki.common.models.SysRoleFunction;
import com.weiziplus.muteki.common.result.ResultBean;
import com.weiziplus.muteki.common.util.DateUtils;
import com.weiziplus.muteki.common.util.PageUtils;
import com.weiziplus.muteki.common.util.ToolUtils;
import com.weiziplus.muteki.common.util.RedisUtils;
import com.weiziplus.muteki.core.pc.common.config.PcGlobalConfig;
import com.weiziplus.muteki.core.pc.common.enums.SysFunctionSuperEnum;
import com.weiziplus.muteki.core.pc.common.enums.SysRoleStatusEnum;
import com.weiziplus.muteki.core.pc.system.mapper.SysRoleMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wanglongwei
 * @date 2020/08/04 17/45
 */
@Slf4j
@Service
public class SysRoleService extends BaseService {

    @Autowired
    SysRoleMapper mapper;

    /**
     * redis的key前缀
     */
    private final String REDIS_KEY_PREFIX = "pc:sysRole:";

    /**
     * 获取分页数据
     *
     * @param pageNum
     * @param pageSize
     * @param search
     * @return
     */
    public ResultBean<PageUtils<SysRole>> getPageList(Integer pageNum, Integer pageSize, String search) {
        String redisKey = createRedisKey(REDIS_KEY_PREFIX + "pageList:", pageNum, pageSize, search);
        Object object = RedisUtils.get(redisKey);
        if (null != object) {
            List<SysRole> sysFunctionList = ToolUtils.objectOfList(object, SysRole.class);
            PageUtils<SysRole> pageUtil = PageUtils.pageInfo(sysFunctionList);
            return ResultBean.success(pageUtil);
        }
        PageHelper.startPage(pageNum, pageSize);
        List<SysRole> sysRoleList = mapper.getList(search);
        RedisUtils.set(redisKey, sysRoleList);
        PageUtils<SysRole> pageUtil = PageUtils.pageInfo(sysRoleList);
        return ResultBean.success(pageUtil);
    }

    /**
     * 获取角色列表
     *
     * @return
     */
    public ResultBean<List<SysRole>> getList() {
        String redisKey = REDIS_KEY_PREFIX + "list:";
        Object object = RedisUtils.get(redisKey);
        if (null != object) {
            List<SysRole> sysRoleList = ToolUtils.objectOfList(object, SysRole.class);
            return ResultBean.success(sysRoleList);
        }
        BaseWhere<SysRole> baseWhere = new BaseWhere<>(SysRole.class)
                .where(new BaseWhereModel(SysRole.COLUMN_STATUS, SysRoleStatusEnum.NORMAL.getValue()))
                .where(new BaseWhereModel(SysRole.COLUMN_ID, BaseWhereEnum.NOT_EQUAL, PcGlobalConfig.SUPER_ADMIN_ROLE_ID))
                .ascOrderBy(SysRole.COLUMN_SORT)
                .descOrderBy(SysRole.COLUMN_CREATE_TIME);
        List<SysRole> sysRoleList = baseFindList(baseWhere);
        RedisUtils.set(redisKey, sysRoleList);
        return ResultBean.success(sysRoleList);
    }

    /**
     * 获取角色拥有的功能列表
     *
     * @param id
     * @return
     */
    public ResultBean<List<Integer>> getFunctionList(Integer id) {
        if (null == id || 0 >= id) {
            return ResultBean.error("id不能为空");
        }
        List<SysRoleFunction> sysRoleFunctionList = baseFindListByClassAndColumnAndValue(
                SysRoleFunction.class, SysRoleFunction.COLUMN_ROLE_ID, id);
        List<Integer> resultList = new ArrayList<>(ToolUtils.initialCapacity(sysRoleFunctionList.size()));
        for (SysRoleFunction sysRoleFunction : sysRoleFunctionList) {
            resultList.add(sysRoleFunction.getFunctionId());
        }
        return ResultBean.success(resultList);
    }

    /**
     * 新增角色
     *
     * @param sysRole
     * @return
     */
    public ResultBean add(SysRole sysRole) {
        if (ToolUtils.isBlank(sysRole.getName())) {
            return ResultBean.error("角色名称不能为空");
        }
        if (!SysRoleStatusEnum.contains(sysRole.getStatus())) {
            return ResultBean.error("状态错误");
        }
        SysRole sysRole1 = baseFindOneDataByClassAndColumnAndValue(
                SysRole.class, SysRole.COLUMN_NAME, sysRole.getName());
        if (null != sysRole1) {
            return ResultBean.error("角色名称已存在");
        }
        sysRole.setCreateTime(DateUtils.getNowDateTime());
        RedisUtils.setExpireDeleteLikeKey(REDIS_KEY_PREFIX);
        baseInsert(sysRole);
        RedisUtils.deleteLikeKey(REDIS_KEY_PREFIX);
        return ResultBean.success();
    }

    /**
     * 编辑角色
     *
     * @param sysRole
     * @return
     */
    public ResultBean update(SysRole sysRole) {
        if (null == sysRole.getId() || 0 >= sysRole.getId()) {
            return ResultBean.error("id不能为空");
        }
        if (PcGlobalConfig.SUPER_ADMIN_ROLE_ID.equals(sysRole.getId())) {
            return ResultBean.errorRole("禁止操作超级管理员");
        }
        if (!SysRoleStatusEnum.contains(sysRole.getStatus())) {
            return ResultBean.error("状态错误");
        }
        sysRole.setName(null)
                .setCreateTime(null);
        RedisUtils.setExpireDeleteLikeKey(REDIS_KEY_PREFIX);
        RedisUtils.setExpireDeleteLikeKey(SysRoleFunctionService.REDIS_KEY_PREFIX);
        baseUpdate(sysRole);
        RedisUtils.deleteLikeKey(REDIS_KEY_PREFIX);
        RedisUtils.deleteLikeKey(SysRoleFunctionService.REDIS_KEY_PREFIX);
        return ResultBean.success();
    }

    /**
     * 删除角色
     *
     * @param id
     * @return
     */
    public ResultBean delete(Integer id) {
        if (null == id || 0 >= id) {
            return ResultBean.error("id错误");
        }
        if (PcGlobalConfig.SUPER_ADMIN_ROLE_ID.equals(id)) {
            return ResultBean.errorRole("禁止操作超级管理员");
        }
        RedisUtils.setExpireDeleteLikeKey(REDIS_KEY_PREFIX);
        RedisUtils.setExpireDeleteLikeKey(SysRoleFunctionService.REDIS_KEY_PREFIX);
        baseDeleteByClassAndId(SysRole.class, id);
        RedisUtils.deleteLikeKey(REDIS_KEY_PREFIX);
        RedisUtils.deleteLikeKey(SysRoleFunctionService.REDIS_KEY_PREFIX);
        return ResultBean.success();
    }

    /**
     * 修改角色功能
     *
     * @param roleId
     * @param functionIds
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ResultBean updateRoleFunction(Integer roleId, Integer[] functionIds) {
        if (null == roleId || 0 >= roleId) {
            return ResultBean.error("角色id不能为空");
        }
        //为空，表示移除该角色的权限
        if (null == functionIds || 0 >= functionIds.length) {
            RedisUtils.setExpireDeleteLikeKey(REDIS_KEY_PREFIX);
            RedisUtils.setExpireDeleteLikeKey(SysRoleFunctionService.REDIS_KEY_PREFIX);
            mapper.deleteRoleFunctionByRoleId(roleId);
            RedisUtils.deleteLikeKey(REDIS_KEY_PREFIX);
            RedisUtils.deleteLikeKey(SysRoleFunctionService.REDIS_KEY_PREFIX);
            return ResultBean.success();
        }
        //不为空，重新赋值权限
        List<SysFunction> sysFunctionList = baseFindByClassAndIds(SysFunction.class, functionIds);
        for (SysFunction sysFunction : sysFunctionList) {
            if (SysFunctionSuperEnum.VIP.getValue().equals(sysFunction.getSuperFlag())) {
                return ResultBean.error("功能ids超出权限");
            }
        }
        List<SysRoleFunction> sysRoleFunctionList = new ArrayList<>(ToolUtils.initialCapacity(functionIds.length));
        for (Integer functionId : functionIds) {
            SysRoleFunction sysRoleFunction = new SysRoleFunction()
                    .setRoleId(roleId)
                    .setFunctionId(functionId);
            sysRoleFunctionList.add(sysRoleFunction);
        }
        RedisUtils.setExpireDeleteLikeKey(REDIS_KEY_PREFIX);
        RedisUtils.setExpireDeleteLikeKey(SysRoleFunctionService.REDIS_KEY_PREFIX);
        Object savepoint = TransactionAspectSupport.currentTransactionStatus().createSavepoint();
        try {
            mapper.deleteRoleFunctionByRoleId(roleId);
            baseInsertList(sysRoleFunctionList);
        } catch (Exception e) {
            log.warn("更新角色权限出错，详情:" + e);
            TransactionAspectSupport.currentTransactionStatus().rollbackToSavepoint(savepoint);
            return ResultBean.error("系统错误，请重试。Exception", e);
        }
        RedisUtils.deleteLikeKey(REDIS_KEY_PREFIX);
        RedisUtils.deleteLikeKey(SysRoleFunctionService.REDIS_KEY_PREFIX);
        return ResultBean.success();
    }

    /**
     * 修改角色状态
     *
     * @param id
     * @param status
     * @return
     */
    public ResultBean updateStatus(Integer id, Integer status) {
        if (null == id || 0 >= id) {
            return ResultBean.error("角色id不能为空");
        }
        if (PcGlobalConfig.SUPER_ADMIN_ROLE_ID.equals(id)) {
            return ResultBean.errorRole("禁止操作超级管理员");
        }
        SysRole sysRole = new SysRole()
                .setId(id)
                .setStatus(status);
        RedisUtils.setExpireDeleteLikeKey(REDIS_KEY_PREFIX);
        RedisUtils.setExpireDeleteLikeKey(SysRoleFunctionService.REDIS_KEY_PREFIX);
        baseUpdate(sysRole);
        RedisUtils.deleteLikeKey(REDIS_KEY_PREFIX);
        RedisUtils.deleteLikeKey(SysRoleFunctionService.REDIS_KEY_PREFIX);
        return ResultBean.success();
    }

}