package com.weiziplus.muteki.core.pc.system.mapper;

import com.weiziplus.muteki.common.models.SysUserLogin;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author wanglongwei
 * @date 2020/08/18 10/57
 */
@Mapper
public interface SysUserLoginLogMapper {

    /**
     * 获取列表数据
     *
     * @param username
     * @param loginProvince
     * @param loginCity
     * @param ipAddress
     * @param result
     * @param osName
     * @param borderName
     * @param startTime
     * @param endTime
     * @param createTimeSort
     * @return
     */
    List<SysUserLogin> getList(String username, String loginProvince, String loginCity, String ipAddress
            , String result, String osName, String borderName, String startTime, String endTime
            , String createTimeSort);

}
