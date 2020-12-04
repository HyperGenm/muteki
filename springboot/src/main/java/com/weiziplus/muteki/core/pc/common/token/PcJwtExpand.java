package com.weiziplus.muteki.core.pc.common.token;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Set;

/**
 * !!!!!!!!存放的数据只有用户重新登录后才会刷新
 *
 * @author wanglongwei
 * @date 2020/07/30 15/31
 */
@Data
@Accessors(chain = true)
public class PcJwtExpand {

    /**
     * 系统用户的角色ids
     */
    private Set<Integer> roleIds;

    /**
     * 系统用户的用户名
     */
    private String username;

    /**
     * 系统用户的真实姓名
     */
    private String realName;
}
