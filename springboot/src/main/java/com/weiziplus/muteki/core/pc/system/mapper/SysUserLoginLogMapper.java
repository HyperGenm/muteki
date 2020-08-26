package com.weiziplus.muteki.core.pc.system.mapper;

import com.weiziplus.muteki.common.models.SysUserLogin;
import com.weiziplus.muteki.core.pc.system.dto.SysUserLoginLogQueryDto;
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
     * @param sysUserLoginLogQueryDto
     * @return
     */
    List<SysUserLogin> getList(SysUserLoginLogQueryDto sysUserLoginLogQueryDto);

}
