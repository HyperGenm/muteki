package com.weiziplus.muteki.core.pc.system.mapper;

import com.weiziplus.muteki.common.models.SysRole;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author wanglongwei
 * @date 2020/08/17 11/36
 */
@Mapper
public interface SysRoleMapper {

    /**
     * 获取列表数据
     *
     * @param search
     * @return
     */
    List<SysRole> getList(String search);

    /**
     * 根据角色id删除角色拥有的功能
     *
     * @param roleId
     * @return
     */
    @Delete("" +
            "DELETE FROM `sys_role_function` " +
            "WHERE `role_id` = #{roleId} ")
    int deleteRoleFunctionByRoleId(Integer roleId);
}
