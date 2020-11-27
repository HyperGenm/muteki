package com.weiziplus.muteki.core.pc.system.mapper;

import com.weiziplus.muteki.common.models.SysDept;
import com.weiziplus.muteki.core.pc.system.vo.SysDeptVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author wanglongwei
 * @date 2020/11/26 11/35
 */
@Mapper
public interface SysDeptMapper {

    /**
     * 获取最小的上级id
     *
     * @return
     */
    @Select("" +
            "SELECT MIN(`parent_id`) " +
            "FROM sys_dept " +
            "WHERE delete_time = 0 " +
            "GROUP BY `parent_id` " +
            "LIMIT 1 ")
    Integer getMinParentId();

    /**
     * 获取列表
     *
     * @return
     */
    @Select("" +
            "SELECT `id`,`parent_id`,`name` " +
            "FROM sys_dept " +
            "WHERE delete_time = 0 " +
            "ORDER BY `sort` DESC,`create_time` ASC ")
    List<SysDeptVo> getList();

    /**
     * 根据父级id获取列表
     *
     * @param parentId
     * @return
     */
    @Select("" +
            "SELECT sd.*,sd2.name parentName " +
            "FROM sys_dept sd " +
            "LEFT JOIN sys_dept sd2 ON sd.parent_id = sd2.id " +
            "WHERE sd.parent_id = #{parentId} AND sd.delete_time = 0 " +
            "ORDER BY sd.sort DESC,sd.create_time ASC ")
    List<SysDept> getListByParentId(Integer parentId);

}
