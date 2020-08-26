package com.weiziplus.muteki.core.pc.system.mapper;

import com.weiziplus.muteki.core.pc.system.dto.SysUserQueryDto;
import com.weiziplus.muteki.core.pc.system.vo.SysUserVo;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;
import java.util.List;

/**
 * @author wanglongwei
 * @date 2020/08/04 17/11
 */
@Mapper
public interface SysUserMapper {

    /**
     * 获取列表数据
     *
     * @param sysUserQueryDto
     * @return
     */
    List<SysUserVo> getListVo(SysUserQueryDto sysUserQueryDto);

    /**
     * 根据用户id删除用户角色信息
     *
     * @param id
     * @return
     */
    @Delete("" +
            "DELETE FROM `sys_user_role` " +
            "WHERE `user_id` = #{id} ")
    int deleteUserRoleByUserId(Integer id);
}
