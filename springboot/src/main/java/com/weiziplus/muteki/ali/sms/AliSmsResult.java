package com.weiziplus.muteki.ali.sms;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author wanglongwei
 * @date 2020/08/21 11/43
 */
@Getter
@Setter
public class AliSmsResult implements Serializable {

    /**
     * 发送回执ID，可根据该ID在接口QuerySendDetails中查询具体的发送状态。
     */
    private String bizId;

    /**
     * 请求状态码。
     * <p>
     * 返回OK代表请求成功。
     * 其他错误码详见错误码列表 https://help.aliyun.com/document_detail/101346.html
     */
    private String code;

    /**
     * 状态码的描述。
     */
    private String message;

    /**
     * 请求id
     */
    private String requestId;

    private static final long serialVersionUID = 1L;

}
