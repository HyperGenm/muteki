package com.weiziplus.muteki.core.api.common.token;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * !!!!!!!!存放的数据只有用户重新登录后才会刷新
 *
 * @author wanglongwei
 * @date 2020/08/20 08/49
 */
@Data
@Accessors(chain = true)
public class WebJwtExpand implements Serializable {

    /**
     * 用户名
     */
    private String username;

    /**
     * 终端类型
     */
    private WebTerminalEnum terminalEnum;

    private static final long serialVersionUID = 1L;

}
