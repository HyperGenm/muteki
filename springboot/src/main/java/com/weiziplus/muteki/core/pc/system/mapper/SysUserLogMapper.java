package com.weiziplus.muteki.core.pc.system.mapper;

import com.weiziplus.muteki.core.pc.system.dto.SysUserLogQueryDto;
import com.weiziplus.muteki.core.pc.system.vo.SysUserLogVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author wanglongwei
 * @date 2020/08/17 15/47
 */
@Mapper
public interface SysUserLogMapper {
    /**
     * 获取列表数据
     *
     * @param sysUserLogQueryDto
     * @return
     */
    List<SysUserLogVo> getListVo(SysUserLogQueryDto sysUserLogQueryDto);

}