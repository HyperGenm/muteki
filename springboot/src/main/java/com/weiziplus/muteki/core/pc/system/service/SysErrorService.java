package com.weiziplus.muteki.core.pc.system.service;

import com.github.pagehelper.PageHelper;
import com.weiziplus.muteki.common.base.BaseService;
import com.weiziplus.muteki.common.models.SysError;
import com.weiziplus.muteki.common.result.ResultBean;
import com.weiziplus.muteki.common.util.PageUtils;
import com.weiziplus.muteki.core.pc.system.mapper.SysErrorMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author wanglongwei
 * @date 2020/08/17 15/35
 */
@Slf4j
@Service
public class SysErrorService extends BaseService {

    @Autowired
    SysErrorMapper mapper;

    /**
     * 获取分页数据
     *
     * @param pageNum
     * @param pageSize
     * @param search
     * @param createTimeSort
     * @return
     */
    public ResultBean<PageUtils<SysError>> getPageList(Integer pageNum, Integer pageSize, String search, String createTimeSort) {
        if (!containsOrderByType(createTimeSort)) {
            return ResultBean.error("排序字段错误");
        }
        PageHelper.startPage(pageNum, pageSize);
        PageUtils<SysError> pageUtil = PageUtils.pageInfo(mapper.getList(
                search, createTimeSort));
        return ResultBean.success(pageUtil);
    }

}
