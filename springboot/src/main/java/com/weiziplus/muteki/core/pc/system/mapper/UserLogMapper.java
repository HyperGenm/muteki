package com.weiziplus.muteki.core.pc.system.mapper;

import com.weiziplus.muteki.core.pc.system.vo.UserLogVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author wanglongwei
 * @date 2020/08/20 09/37
 */
@Mapper
public interface UserLogMapper {
    /**
     * 获取列表数据
     *
     * @param username
     * @param realName
     * @param type
     * @param resultCode
     * @param description
     * @param ipAddress
     * @param borderName
     * @param osName
     * @param startTime
     * @param endTime
     * @param createTimeSort
     * @return
     */
    List<UserLogVo> getListVo(String username, String realName, Integer type, Integer resultCode, String description, String ipAddress
            , String borderName, String osName, String startTime, String endTime, String createTimeSort);

}