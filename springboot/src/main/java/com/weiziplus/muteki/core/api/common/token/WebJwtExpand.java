package com.weiziplus.muteki.core.api.common.token;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author wanglongwei
 * @date 2020/08/20 08/49
 */
@Data
@Accessors(chain = true)
public class WebJwtExpand {

    /**
     * 用户名
     */
    private String username;

}
