package com.weiziplus.muteki.core.pc.system.service;

import com.github.pagehelper.PageHelper;
import com.weiziplus.muteki.common.base.BaseService;
import com.weiziplus.muteki.common.models.SysUserLogin;
import com.weiziplus.muteki.common.result.ResultBean;
import com.weiziplus.muteki.common.util.PageUtils;
import com.weiziplus.muteki.core.pc.system.dto.SysUserLoginLogQueryDto;
import com.weiziplus.muteki.core.pc.system.mapper.SysUserLoginLogMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author wanglongwei
 * @date 2020/08/18 10/53
 */
@Slf4j
@Service
public class SysUserLoginLogService extends BaseService {

    @Autowired
    SysUserLoginLogMapper mapper;

    /**
     * 系统用户登录日志
     *
     * @param pageNum
     * @param pageSize
     * @param sysUserLoginLogQueryDto
     * @return
     */
    public ResultBean<PageUtils<SysUserLogin>> getPageList(Integer pageNum, Integer pageSize, SysUserLoginLogQueryDto sysUserLoginLogQueryDto) {
        if (!containsOrderByType(sysUserLoginLogQueryDto.getCreateTimeSort())) {
            return ResultBean.error("排序字段错误");
        }
        PageHelper.startPage(pageNum, pageSize);
        PageUtils<SysUserLogin> pageUtil = PageUtils.pageInfo(mapper.getList(sysUserLoginLogQueryDto));
        return ResultBean.success(pageUtil);
    }

}
