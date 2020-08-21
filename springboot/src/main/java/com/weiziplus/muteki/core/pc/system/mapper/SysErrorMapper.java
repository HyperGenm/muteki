package com.weiziplus.muteki.core.pc.system.mapper;

import com.weiziplus.muteki.common.models.SysError;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author wanglongwei
 * @date 2020/08/17 15/37
 */
@Mapper
public interface SysErrorMapper {

    /**
     * 获取列表数据
     *
     * @param search
     * @param createTimeSort
     * @return
     */
    List<SysError> getList(String search, String createTimeSort);

}
