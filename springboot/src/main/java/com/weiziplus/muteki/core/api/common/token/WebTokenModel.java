package com.weiziplus.muteki.core.api.common.token;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author wanglongwei
 * @date 2020/08/25 16/29
 */
@Data
@Accessors(chain = true)
public class WebTokenModel implements Serializable {

    /**
     * token
     */
    private String token;

    /**
     * 上次最后操作的时间戳
     */
    private Long lastTimeStamp;

    private static final long serialVersionUID = 1L;

}
