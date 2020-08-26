package com.weiziplus.muteki.core.pc.system.mapper;

import com.weiziplus.muteki.common.models.UserLogin;
import com.weiziplus.muteki.core.pc.system.dto.UserLoginLogQueryDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author wanglongwei
 * @date 2020/08/20 10/34
 */
@Mapper
public interface UserLoginLogMapper {

    /**
     * 获取列表数据
     *
     * @param userLoginLogQueryDto
     * @return
     */
    List<UserLogin> getList(UserLoginLogQueryDto userLoginLogQueryDto);

}
