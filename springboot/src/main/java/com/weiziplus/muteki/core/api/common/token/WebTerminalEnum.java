package com.weiziplus.muteki.core.api.common.token;

import lombok.Getter;

import java.io.Serializable;

/**
 * @author wanglongwei
 * @date 2020/08/25 16/20
 */
@Getter
public enum WebTerminalEnum implements Serializable {

    /**
     * 用户的终端类型
     */
    WEB("网页", 60L * 60 * 10),
    APP("客户端", 60L * 60 * 24);

    private final String name;
    /**
     * 过期时间，单位 秒
     */
    private final Long expireTime;

    WebTerminalEnum(String name, Long expireTime) {
        this.name = name;
        this.expireTime = expireTime;
    }

    private static final long serialVersionUID = 1L;

}
