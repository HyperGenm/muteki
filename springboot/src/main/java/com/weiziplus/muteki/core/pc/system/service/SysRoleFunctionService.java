package com.weiziplus.muteki.core.pc.system.service;

import com.alibaba.fastjson.JSONArray;
import com.weiziplus.muteki.common.base.BaseService;
import com.weiziplus.muteki.common.models.SysFunction;
import com.weiziplus.muteki.common.util.ToolUtils;
import com.weiziplus.muteki.common.util.RedisUtils;
import com.weiziplus.muteki.core.pc.system.mapper.SysRoleFunctionMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author wanglongwei
 * @date 2020/08/18 08/33
 */
@Slf4j
@Service
public class SysRoleFunctionService extends BaseService {

    @Autowired
    SysRoleFunctionMapper mapper;

    /**
     * redis的key前缀
     */
    public final static String REDIS_KEY_PREFIX = "pc:sysRoleFunction:";

    /**
     * 获取所有的功能api
     *
     * @return
     */
    public Set<String> getAllFunContainApi() {
        String redisKey = REDIS_KEY_PREFIX + "allFunContainApi";
        Object object = RedisUtils.get(redisKey);
        if (null != object) {
            return ToolUtils.objectOfSet(object, String.class);
        }
        List<SysFunction> sysFunctionList = baseFindAllByClass(SysFunction.class);
        Set<String> functionSet = new HashSet<>();
        for (SysFunction sysFunction : sysFunctionList) {
            if (ToolUtils.isBlank(sysFunction.getContainApi())) {
                continue;
            }
            List<String> strings = JSONArray.parseArray(sysFunction.getContainApi(), String.class);
            functionSet.addAll(strings);
        }
        //缓存一小时
        RedisUtils.set(redisKey, functionSet);
        return functionSet;
    }

    /**
     * 根据roleIds获取拥有的功能api
     *
     * @param roleIds
     * @return
     */
    public Set<String> getFunContainApiByRoleIds(Set<Integer> roleIds) {
        Set<String> funContainApiSet = new HashSet<>();
        if (null == roleIds || 0 >= roleIds.size()) {
            return funContainApiSet;
        }
        String redisKey = createRedisKey(REDIS_KEY_PREFIX + "funContainApiByRoleIds:", roleIds);
        Object object = RedisUtils.get(redisKey);
        if (null != object) {
            return ToolUtils.objectOfSet(object, String.class);
        }
        Set<String> funContainApiByRoleIds = mapper.getFunContainApiByRoleIds(roleIds);
        if (null == funContainApiByRoleIds || 0 >= funContainApiByRoleIds.size()) {
            return funContainApiSet;
        }
        for (String funContainApiByRoleId : funContainApiByRoleIds) {
            if (ToolUtils.isBlank(funContainApiByRoleId)) {
                continue;
            }
            List<String> strings = JSONArray.parseArray(funContainApiByRoleId, String.class);
            funContainApiSet.addAll(strings);
        }
        RedisUtils.set(redisKey, funContainApiSet);
        return funContainApiSet;
    }

}
