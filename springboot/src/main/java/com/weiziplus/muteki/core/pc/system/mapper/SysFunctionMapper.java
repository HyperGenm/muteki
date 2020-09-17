package com.weiziplus.muteki.core.pc.system.mapper;

import com.weiziplus.muteki.common.models.SysFunction;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Set;

/**
 * @author wanglongwei
 * @date 2020/08/04 10/12
 */
@Mapper
public interface SysFunctionMapper {

    /**
     * 根据角色ids获取不是超级管理员专属的最小的上级id
     *
     * @param roleIds
     * @return
     */
    Integer getMinNotSuperMenuParentIdByRoleIds(@Param("roleIds") Set<Integer> roleIds);

    /**
     * 根据角色ids获取不是超级管理员专属的菜单列表
     *
     * @param roleIds
     * @return
     */
    List<SysFunction> getNotSuperMenuListByRoleIds(@Param("roleIds") Set<Integer> roleIds);

    /**
     * 根据角色ids获取不是超级管理员专属的按钮列表
     *
     * @param roleIds
     * @return
     */
    Set<String> getNotSuperButtonSetByRoleIds(@Param("roleIds") Set<Integer> roleIds);

    /**
     * 获取最小的上级id
     *
     * @return
     */
    @Select("" +
            "SELECT MIN(parent_id) " +
            "FROM `sys_function` ")
    int getMinParentId();

    /**
     * 根据上级id获取功能列表
     *
     * @param parentId
     * @return
     */
    @Select("" +
            "SELECT * " +
            "FROM `sys_function` " +
            "WHERE `parent_id` = #{parentId} " +
            "ORDER BY `sort` ASC,`create_time` DESC ")
    List<SysFunction> getFunctionListByParentId(Integer parentId);

    /**
     * 获取所有功能列表
     *
     * @return
     */
    @Select("" +
            "SELECT * " +
            "FROM `sys_function` " +
            "WHERE `super_flag` = 1 " +
            "ORDER BY `sort` ASC,`create_time` DESC ")
    List<SysFunction> getAllFunctionListNotVip();
}
