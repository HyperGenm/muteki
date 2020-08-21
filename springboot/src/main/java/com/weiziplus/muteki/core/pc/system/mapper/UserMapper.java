package com.weiziplus.muteki.core.pc.system.mapper;

import com.weiziplus.muteki.core.pc.system.vo.UserVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author wanglongwei
 * @date 2020/08/21 15/12
 */
@Mapper
public interface UserMapper {

    /**
     * 获取列表数据
     *
     * @param username
     * @param status
     * @param lastActiveTime
     * @param createTime
     * @param lastActiveTimeSort
     * @param createTimeSort
     * @return
     */
    List<UserVo> getListVo(String username, Integer status
            , String lastActiveTime, String createTime, String lastActiveTimeSort, String createTimeSort);

}
