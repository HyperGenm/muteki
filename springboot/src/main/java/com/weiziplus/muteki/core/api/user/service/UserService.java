package com.weiziplus.muteki.core.api.user.service;

import com.weiziplus.muteki.common.base.BaseService;
import com.weiziplus.muteki.common.models.User;
import com.weiziplus.muteki.common.result.ResultBean;
import com.weiziplus.muteki.common.util.ToolUtils;
import com.weiziplus.muteki.core.api.common.token.WebTerminalEnum;
import com.weiziplus.muteki.core.api.common.token.WebTokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
 * @author wanglongwei
 * @date 2020/08/25 17/03
 */
@Slf4j
@Service
public class UserService extends BaseService {

    /**
     * 获取用户信息
     *
     * @return
     */
    public ResultBean getInfo() {
        Long userId = WebTokenUtils.getUserId();
        User user = baseFindByClassAndId(User.class, userId);
        WebTerminalEnum terminalEnum = WebTokenUtils.getExpand().getTerminalEnum();
        return ResultBean.success(new HashMap<String, Object>(ToolUtils.initialCapacity(2)) {{
            put("user", user);
            put("terminal", terminalEnum.getName());
        }});
    }
}
