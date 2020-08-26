package com.weiziplus.muteki.core.pc.system.mapper;

import com.weiziplus.muteki.core.pc.system.dto.UserLogQueryDto;
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
     * @param userLogQueryDto
     * @return
     */
    List<UserLogVo> getListVo(UserLogQueryDto userLogQueryDto);

}