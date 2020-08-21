package com.weiziplus.muteki.core.pc.system.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.Set;

/**
 * @author wanglongwei
 * @date 2020/08/18 08/32
 */
@Mapper
public interface SysRoleFunctionMapper {

    /**
     * 根据roleIds获取拥有的功能api
     *
     * @param roleIds
     * @return
     */
    Set<String> getFunContainApiByRoleIds(Set<Integer> roleIds);

}
