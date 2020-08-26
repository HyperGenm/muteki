package com.weiziplus.muteki.core.pc.system.service;

import com.github.pagehelper.PageHelper;
import com.weiziplus.muteki.common.base.BaseService;
import com.weiziplus.muteki.common.result.ResultBean;
import com.weiziplus.muteki.common.util.PageUtils;
import com.weiziplus.muteki.core.pc.system.dto.SysUserLogQueryDto;
import com.weiziplus.muteki.core.pc.system.mapper.SysUserLogMapper;
import com.weiziplus.muteki.core.pc.system.vo.SysUserLogVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author wanglongwei
 * @date 2020/08/17 15/44
 */
@Slf4j
@Service
public class SysUserLogService extends BaseService {

    @Autowired
    SysUserLogMapper mapper;

    /**
     * 获取分页数据
     *
     * @param pageNum
     * @param pageSize
     * @param sysUserLogQueryDto
     * @return
     */
    public ResultBean<PageUtils<SysUserLogVo>> getPageList(Integer pageNum, Integer pageSize, SysUserLogQueryDto sysUserLogQueryDto) {
        if (!containsOrderByType(sysUserLogQueryDto.getCreateTimeSort())) {
            return ResultBean.error("排序字段错误");
        }
        PageHelper.startPage(pageNum, pageSize);
        PageUtils<SysUserLogVo> pageUtil = PageUtils.pageInfo(mapper.getListVo(sysUserLogQueryDto));
        return ResultBean.success(pageUtil);
    }

}
