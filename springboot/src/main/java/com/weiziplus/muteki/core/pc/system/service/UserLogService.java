package com.weiziplus.muteki.core.pc.system.service;

import com.github.pagehelper.PageHelper;
import com.weiziplus.muteki.common.base.BaseService;
import com.weiziplus.muteki.common.result.ResultBean;
import com.weiziplus.muteki.common.util.PageUtils;
import com.weiziplus.muteki.core.pc.system.mapper.UserLogMapper;
import com.weiziplus.muteki.core.pc.system.vo.UserLogVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author wanglongwei
 * @date 2020/08/20 09/33
 */
@Slf4j
@Service
public class UserLogService extends BaseService {

    @Autowired
    UserLogMapper mapper;

    /**
     * 获取分页数据
     *
     * @param pageNum
     * @param pageSize
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
    public ResultBean<PageUtils<UserLogVo>> getPageList(Integer pageNum, Integer pageSize
            , String username, String realName, Integer type, Integer resultCode, String description, String ipAddress
            , String borderName, String osName, String startTime, String endTime
            , String createTimeSort) {
        if (!containsOrderByType(createTimeSort)) {
            return ResultBean.error("排序字段错误");
        }
        PageHelper.startPage(pageNum, pageSize);
        PageUtils<UserLogVo> pageUtil = PageUtils.pageInfo(mapper.getListVo(
                username, realName, type, resultCode, description, ipAddress, borderName, osName, startTime, endTime,
                createTimeSort));
        return ResultBean.success(pageUtil);

    }

}