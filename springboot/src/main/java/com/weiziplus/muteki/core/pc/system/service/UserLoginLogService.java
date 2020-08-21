package com.weiziplus.muteki.core.pc.system.service;

import com.github.pagehelper.PageHelper;
import com.weiziplus.muteki.common.base.BaseService;
import com.weiziplus.muteki.common.models.UserLogin;
import com.weiziplus.muteki.common.result.ResultBean;
import com.weiziplus.muteki.common.util.PageUtils;
import com.weiziplus.muteki.core.pc.system.mapper.UserLoginLogMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author wanglongwei
 * @date 2020/08/20 10/30
 */
@Slf4j
@Service
public class UserLoginLogService extends BaseService {

    @Autowired
    UserLoginLogMapper mapper;

    /**
     * 获取分页数据
     *
     * @param pageNum
     * @param pageSize
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
    public ResultBean<PageUtils<UserLogin>> getPageList(Integer pageNum, Integer pageSize
            , String username, String loginProvince, String loginCity, String ipAddress
            , String result, String osName, String borderName, String startTime, String endTime
            , String createTimeSort) {
        if (!containsOrderByType(createTimeSort)) {
            return ResultBean.error("排序字段错误");
        }
        PageHelper.startPage(pageNum, pageSize);
        PageUtils<UserLogin> pageUtil = PageUtils.pageInfo(mapper.getList(
                username, loginProvince, loginCity, ipAddress, result,
                osName, borderName, startTime, endTime,
                createTimeSort));
        return ResultBean.success(pageUtil);
    }

}