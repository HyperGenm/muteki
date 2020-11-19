package com.weiziplus.muteki.core.pc.system.service;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.weiziplus.muteki.common.base.BaseService;
import com.weiziplus.muteki.common.base.BaseWhere;
import com.weiziplus.muteki.common.base.BaseWhereModel;
import com.weiziplus.muteki.common.result.ResultBean;
import com.weiziplus.muteki.common.util.DateUtils;
import com.weiziplus.muteki.common.util.PageUtils;
import com.weiziplus.muteki.core.pc.common.enums.SysFunctionExternalEnum;
import com.weiziplus.muteki.core.pc.common.enums.SysFunctionSuperEnum;
import com.weiziplus.muteki.core.pc.common.enums.SysFunctionTypeEnum;
import com.weiziplus.muteki.common.models.SysFunction;
import com.weiziplus.muteki.common.util.ToolUtils;
import com.weiziplus.muteki.common.util.RedisUtils;
import com.weiziplus.muteki.core.pc.common.config.PcGlobalConfig;
import com.weiziplus.muteki.core.pc.system.dto.SysFunctionDto;
import com.weiziplus.muteki.core.pc.system.mapper.SysFunctionMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author wanglongwei
 * @date 2020/08/04 09/07
 */
@Slf4j
@Service
public class SysFunctionService extends BaseService {

    @Autowired
    SysFunctionMapper mapper;

    /**
     * redis的key前缀
     */
    private final String REDIS_KEY_PREFIX = "pc:sysFunction:";

    /**
     * 获取所有的菜单列表
     *
     * @return
     */
    private List<SysFunction> getAllMenuList() {
        String redisKey = REDIS_KEY_PREFIX + "allMenuList";
        Object object = RedisUtils.get(redisKey);
        if (null != object) {
            return ToolUtils.objectOfList(object, SysFunction.class);
        }
        BaseWhere<SysFunction> baseWhere = new BaseWhere<>(SysFunction.class)
                .where(new BaseWhereModel(SysFunction.COLUMN_TYPE, SysFunctionTypeEnum.MENU.getValue()))
                .ascOrderBy(SysFunction.COLUMN_SORT)
                .descOrderBy(SysFunction.COLUMN_CREATE_TIME);
        List<SysFunction> sysFunctionList = baseFindList(baseWhere);
        //缓存一小时
        RedisUtils.set(redisKey, sysFunctionList);
        return sysFunctionList;
    }

    /**
     * 获取下级列表
     *
     * @param parentId
     * @param functionList
     * @return
     */
    private List<SysFunction> getChildrenList(Integer parentId, List<SysFunction> functionList) {
        List<SysFunction> result = new ArrayList<>();
        for (SysFunction sysFunction : functionList) {
            if (!sysFunction.getParentId().equals(parentId)) {
                continue;
            }
            List<SysFunction> childrenList = getChildrenList(sysFunction.getId(), functionList);
            sysFunction.setChildren(childrenList);
            result.add(sysFunction);
        }
        return result;
    }

    /**
     * 获取所有的菜单树
     *
     * @return
     */
    public List<SysFunction> getAllMenuTree() {
        List<SysFunction> allMenuList = getAllMenuList();
        List<SysFunction> result = new ArrayList<>();
        for (SysFunction sysFunction : allMenuList) {
            if (!PcGlobalConfig.SYS_FUNCTION_DEFAULT_PARENT_ID.equals(sysFunction.getParentId())) {
                continue;
            }
            List<SysFunction> childrenList = getChildrenList(sysFunction.getId(), allMenuList);
            sysFunction.setChildren(childrenList);
            result.add(sysFunction);
        }
        return result;
    }

    /**
     * 根据角色列表获取拥有的菜单树
     *
     * @return
     */
    public List<SysFunction> getMenuTreeByRoleIds(Set<Integer> roleIds) {
        List<SysFunction> result = new ArrayList<>();
        String redisKey = REDIS_KEY_PREFIX + "menuTreeByRoleIds:" + roleIds;
        Object object = RedisUtils.get(redisKey);
        if (null != object) {
            return ToolUtils.objectOfList(object, SysFunction.class);
        }
        Integer minParentId = mapper.getMinNotSuperMenuParentIdByRoleIds(roleIds);
        if (null == minParentId) {
            return result;
        }
        List<SysFunction> notSuperMenuListByRoleIds = mapper.getNotSuperMenuListByRoleIds(roleIds);
        if (null == notSuperMenuListByRoleIds) {
            return result;
        }
        for (SysFunction sysFunction : notSuperMenuListByRoleIds) {
            if (!minParentId.equals(sysFunction.getParentId())) {
                continue;
            }
            List<SysFunction> childrenList = getChildrenList(sysFunction.getId(), notSuperMenuListByRoleIds);
            sysFunction.setChildren(childrenList);
            result.add(sysFunction);
        }
        RedisUtils.set(redisKey, redisKey);
        return result;
    }

    /**
     * 根据角色列表获取拥有的按钮列表
     *
     * @return
     */
    public Set<String> getButtonSetByRoleIds(Set<Integer> roleIds) {
        String redisKey = REDIS_KEY_PREFIX + "buttonSetByRoleIds:" + roleIds;
        Object object = RedisUtils.get(redisKey);
        if (null != object) {
            return ToolUtils.objectOfSet(object, String.class);
        }
        Set<String> notSuperButtonSetByRoleIds = mapper.getNotSuperButtonSetByRoleIds(roleIds);
        RedisUtils.set(redisKey, notSuperButtonSetByRoleIds);
        return notSuperButtonSetByRoleIds;
    }

    /**
     * 获取分页数据
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    public ResultBean<PageUtils<SysFunction>> getPageList(Integer pageNum, Integer pageSize) {
        String redisKey = createRedisKey(REDIS_KEY_PREFIX + "pageList:", pageNum, pageSize);
        Object object = RedisUtils.get(redisKey);
        if (null != object) {
            List<SysFunction> sysFunctionList = ToolUtils.objectOfList(object, SysFunction.class);
            PageUtils<SysFunction> pageUtil = PageUtils.pageInfo(sysFunctionList);
            return ResultBean.success(pageUtil);
        }
        Integer minParentId = mapper.getMinParentId();
        PageHelper.startPage(pageNum, pageSize);
        List<SysFunction> functionListByParentId = mapper.getFunctionListByParentId(minParentId);
        List<SysFunction> sysFunctionList = baseFindAllByClass(SysFunction.class);
        for (SysFunction sysFunction : functionListByParentId) {
            List<SysFunction> childrenListByParentId = getChildrenList(sysFunction.getId(), sysFunctionList);
            sysFunction.setChildren(childrenListByParentId);
        }
        RedisUtils.set(redisKey, functionListByParentId);
        PageUtils<SysFunction> pageUtil = PageUtils.pageInfo(functionListByParentId);
        return ResultBean.success(pageUtil);
    }

    /**
     * 获取树形结构
     *
     * @return
     */
    public ResultBean<List<SysFunction>> getTree() {
        String redisKey = createRedisKey(REDIS_KEY_PREFIX + "getTree:");
        Object object = RedisUtils.get(redisKey);
        if (null != object) {
            List<SysFunction> sysFunctionList = ToolUtils.objectOfList(object, SysFunction.class);
            return ResultBean.success(sysFunctionList);
        }
        Integer minParentId = mapper.getMinParentId();
        List<SysFunction> allFunctionList = mapper.getAllFunctionListNotVip();
        List<SysFunction> resultList = new ArrayList<>();
        for (SysFunction sysFunction : allFunctionList) {
            if (!minParentId.equals(sysFunction.getParentId())) {
                continue;
            }
            List<SysFunction> childrenListByParentId = getChildrenList(sysFunction.getId(), allFunctionList);
            sysFunction.setChildren(childrenListByParentId);
            resultList.add(sysFunction);
        }
        RedisUtils.set(redisKey, resultList);
        return ResultBean.success(resultList);
    }

    /**
     * 新增功能
     *
     * @param sysFunctionDto
     * @return
     */
    public ResultBean add(SysFunctionDto sysFunctionDto) {
        if (!SysFunctionTypeEnum.contains(sysFunctionDto.getType())) {
            return ResultBean.error("类型错误");
        }
        if (!SysFunctionSuperEnum.contains(sysFunctionDto.getSuperFlag())) {
            return ResultBean.error("专属类型错误");
        }
        if (!SysFunctionExternalEnum.contains(sysFunctionDto.getExternalFlag())) {
            return ResultBean.error("内/外部链接错误");
        }
        SysFunction sysFunction = baseFindOneDataByClassAndColumnAndValue(
                SysFunction.class, SysFunction.COLUMN_NAME, sysFunctionDto.getName());
        if (null != sysFunction) {
            return ResultBean.error("功能名已存在");
        }
        sysFunction = new SysFunction();
        BeanUtils.copyProperties(sysFunctionDto, sysFunction);
        sysFunction.setCreateTime(DateUtils.getNowDateTime());
        RedisUtils.setExpireDeleteLikeKey(REDIS_KEY_PREFIX);
        baseInsert(sysFunction);
        RedisUtils.deleteLikeKey(REDIS_KEY_PREFIX);
        return ResultBean.success();
    }

    /**
     * 修改功能
     *
     * @param sysFunctionDto
     * @return
     */
    public ResultBean update(SysFunctionDto sysFunctionDto) {
        if (null == sysFunctionDto.getId() || 0 >= sysFunctionDto.getId()) {
            return ResultBean.error("id不能为空");
        }
        if (!SysFunctionTypeEnum.contains(sysFunctionDto.getType())) {
            return ResultBean.error("类型错误");
        }
        if (!SysFunctionSuperEnum.contains(sysFunctionDto.getSuperFlag())) {
            return ResultBean.error("专属类型错误");
        }
        if (!SysFunctionExternalEnum.contains(sysFunctionDto.getExternalFlag())) {
            return ResultBean.error("内/外部链接错误");
        }
        //如果修改为按钮
        if (SysFunctionTypeEnum.BUTTON.getValue().equals(sysFunctionDto.getType())) {
            SysFunction sysFunction1 = baseFindOneDataByClassAndColumnAndValue(
                    SysFunction.class, SysFunction.COLUMN_PARENT_ID, sysFunctionDto.getId());
            if (null != sysFunction1) {
                return ResultBean.error("存在下级，不能修改为按钮");
            }
        }
        SysFunction sysFunction = new SysFunction();
        BeanUtils.copyProperties(sysFunctionDto, sysFunction);
        sysFunction.setTitle(null)
                .setName(null)
                .setParentId(null);
        //如果修改了功能对应接口
        RedisUtils.setExpireDeleteLikeKey(REDIS_KEY_PREFIX);
        baseUpdate(sysFunction);
        RedisUtils.deleteLikeKey(REDIS_KEY_PREFIX);
        return ResultBean.success();
    }

    /**
     * 删除功能
     *
     * @param id
     * @return
     */
    public ResultBean delete(Integer id) {
        if (null == id || 0 >= id) {
            return ResultBean.error("id错误");
        }
        SysFunction sysFunction = baseFindOneDataByClassAndColumnAndValue(
                SysFunction.class, SysFunction.COLUMN_PARENT_ID, id);
        if (null != sysFunction) {
            return ResultBean.error("存在下级，不能删除");
        }
        RedisUtils.setExpireDeleteLikeKey(REDIS_KEY_PREFIX);
        RedisUtils.setExpireDeleteLikeKey(SysRoleFunctionService.REDIS_KEY_PREFIX);
        baseDeleteByClassAndId(SysFunction.class, id);
        RedisUtils.deleteLikeKey(REDIS_KEY_PREFIX);
        RedisUtils.deleteLikeKey(SysRoleFunctionService.REDIS_KEY_PREFIX);
        return ResultBean.success();
    }

    /**
     * 修改拥有的api列表
     *
     * @param id
     * @param apiList
     * @return
     */
    public ResultBean updateContainApi(Integer id, String[] apiList) {
        if (null == id || 0 >= id) {
            return ResultBean.error("id错误");
        }
        if (null == apiList) {
            apiList = new String[]{};
        }
        SysFunction function = new SysFunction()
                .setId(id)
                .setContainApi(JSON.toJSONString(apiList));
        RedisUtils.setExpireDeleteLikeKey(REDIS_KEY_PREFIX);
        RedisUtils.setExpireDeleteLikeKey(SysRoleFunctionService.REDIS_KEY_PREFIX);
        baseUpdate(function);
        RedisUtils.deleteLikeKey(REDIS_KEY_PREFIX);
        RedisUtils.deleteLikeKey(SysRoleFunctionService.REDIS_KEY_PREFIX);
        return ResultBean.success();
    }

    /**
     * 设置图标
     *
     * @param id
     * @param icon
     * @return
     */
    public ResultBean setIcon(Integer id, String icon) {
        if (null == id || 0 >= id) {
            return ResultBean.error("id错误");
        }
        SysFunction sysFunction = new SysFunction()
                .setId(id)
                .setIcon(icon);
        RedisUtils.setExpireDeleteLikeKey(REDIS_KEY_PREFIX);
        RedisUtils.setExpireDeleteLikeKey(SysRoleFunctionService.REDIS_KEY_PREFIX);
        baseUpdate(sysFunction);
        RedisUtils.deleteLikeKey(REDIS_KEY_PREFIX);
        RedisUtils.deleteLikeKey(SysRoleFunctionService.REDIS_KEY_PREFIX);
        return ResultBean.success();
    }

}