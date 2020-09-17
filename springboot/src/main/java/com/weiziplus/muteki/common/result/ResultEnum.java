package com.weiziplus.muteki.common.result;

import lombok.Getter;

/**
 * 返回结果状态码
 *
 * @author wanglongwei
 * @date 2020/07/30 14/27
 */
@Getter
public enum ResultEnum {

    /**
     * 类型
     */
    SUCCESS("success", 200),
    ERROR_PARAM("参数错误", 400),
    ERROR_TOKEN("token错误", 401),
    ERROR("error", 402),
    ERROR_ROLE("没有权限", 403),
    ERROR_FAST("访问频率过快", 429),
    ERROR_ALI("调用ali接口出错", 420),
    ERROR_BAIDU("调用百度接口出错", 430),
    ERROR_EXCEPTION("系统异常", 500),
    ERROR_ASPECT("aop异常异常", 501);

    private final String msg;
    private final Integer value;

    ResultEnum(String msg, Integer value) {
        this.msg = msg;
        this.value = value;
    }

}
