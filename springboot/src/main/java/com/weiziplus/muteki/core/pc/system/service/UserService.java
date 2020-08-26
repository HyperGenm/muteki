package com.weiziplus.muteki.core.pc.system.service;

import com.github.pagehelper.PageHelper;
import com.weiziplus.muteki.common.base.BaseService;
import com.weiziplus.muteki.common.models.User;
import com.weiziplus.muteki.common.result.ResultBean;
import com.weiziplus.muteki.common.util.PageUtils;
import com.weiziplus.muteki.core.api.common.enums.UserStatusEnum;
import com.weiziplus.muteki.core.api.common.token.WebTokenUtils;
import com.weiziplus.muteki.core.pc.system.mapper.UserMapper;
import com.weiziplus.muteki.core.pc.system.vo.UserVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author wanglongwei
 * @date 2020/08/21 15/09
 */
@Slf4j
@Service
public class UserService extends BaseService {

    @Autowired
    UserMapper mapper;

    /**
     * 获取分页数据
     *
     * @param pageNum
     * @param pageSize
     * @param username
     * @param status
     * @param lastActiveTime
     * @param createTime
     * @param lastActiveTimeSort
     * @param createTimeSort
     * @return
     */
    public ResultBean<PageUtils<UserVo>> getPageList(Integer pageNum, Integer pageSize
            , String username, Integer status, String lastActiveTime, String createTime
            , String lastActiveTimeSort, String createTimeSort) {
        if (!containsOrderByType(lastActiveTimeSort, createTimeSort)) {
            return ResultBean.error("排序类型错误");
        }
        PageHelper.startPage(pageNum, pageSize);
        PageUtils<UserVo> pageUtil = PageUtils.pageInfo(mapper.getListVo(
                username, status, lastActiveTime, createTime,
                lastActiveTimeSort, createTimeSort));
        return ResultBean.success(pageUtil);
    }

    /**
     * 禁用用户
     *
     * @param id
     * @return
     */
    public ResultBean disableUser(Long id) {
        if (null == id || 0 >= id) {
            return ResultBean.error("id不能为空");
        }
        User user = baseFindByClassAndId(User.class, id);
        if (null == user) {
            return ResultBean.error("id错误");
        }
        if (!UserStatusEnum.NORMAL.getValue().equals(user.getStatus())) {
            return ResultBean.error("当前用户为 " + UserStatusEnum.getName(user.getStatus()) + " 状态");
        }
        user.setStatus(UserStatusEnum.DISABLE.getValue());
        WebTokenUtils.deleteAllToken(id);
        baseUpdate(user);
        return ResultBean.success();
    }

    /**
     * 启用web账户
     *
     * @param id
     * @return
     */
    public ResultBean enableUser(Long id) {
        if (null == id || 0 >= id) {
            return ResultBean.error("id不能为空");
        }
        User user = baseFindByClassAndId(User.class, id);
        if (null == user) {
            return ResultBean.error("id错误");
        }
        if (!UserStatusEnum.DISABLE.getValue().equals(user.getStatus())) {
            return ResultBean.error("当前用户为 " + UserStatusEnum.getName(user.getStatus()) + " 状态");
        }
        user.setStatus(UserStatusEnum.NORMAL.getValue());
        baseUpdate(user);
        return ResultBean.success();
    }
}
