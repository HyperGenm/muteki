package com.weiziplus.muteki.common.result;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * 统一返回结果
 *
 * @author wanglongwei
 * @date 2019/5/24 15:58
 */
@Getter
@ApiModel("统一返回结果")
@Component
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResultBean<T> implements Serializable {

    /**
     * 是否將异常详情展示给前端
     */
    private static Boolean RESPONSE_SHOW_RUNTIME_EXCEPTION;

    /**
     * 赋值
     *
     * @param exception
     */
    @Value("${global.response-show-runtime-exception:false}")
    private void setResponseShowRuntimeException(String exception) {
        ResultBean.RESPONSE_SHOW_RUNTIME_EXCEPTION = Boolean.valueOf(exception);
    }

    /**
     * 返回状态码code
     */
    @ApiModelProperty("状态码:200表示成功")
    private Integer code;

    /**
     * 返回提示信息
     */
    @ApiModelProperty("提示信息")
    private String msg;

    /**
     * 返回数据
     */
    @ApiModelProperty("数据")
    private T data;

    /**
     * 如果有异常，异常信息
     */
    @ApiModelProperty("如果有异常，异常信息")
    private Exception errorMsg;

    /**
     * 创建无参数ResultUtil对象---fastjson反序列化需要无参数
     */
    public ResultBean() {
    }

    /**
     * 成功
     *
     * @param data
     * @return
     */
    public static <T> ResultBean<T> success(T data) {
        ResultBean<T> resultBean = new ResultBean<>();
        resultBean.code = ResultEnum.SUCCESS.getValue();
        resultBean.msg = ResultEnum.SUCCESS.getMsg();
        resultBean.data = data;
        return resultBean;
    }

    /**
     * 成功
     *
     * @return
     */
    public static ResultBean<?> success() {
        return success(null);
    }

    /**
     * 异常
     *
     * @param resultEnum
     * @param msg
     * @param errorMsg
     * @return
     */
    private static <T> ResultBean<T> baseError(ResultEnum resultEnum, String msg, Exception errorMsg) {
        ResultBean<T> resultBean = new ResultBean<>();
        resultBean.code = resultEnum.getValue();
        resultBean.msg = msg;
        //如果将异常信息暴露给前端
        if (RESPONSE_SHOW_RUNTIME_EXCEPTION) {
            resultBean.errorMsg = errorMsg;
        }
        return resultBean;
    }

    /**
     * 自定义异常
     *
     * @param msg
     * @param errorMsg
     * @return
     */
    public static <T> ResultBean<T> error(ResultEnum resultEnum, String msg, Exception errorMsg) {
        return baseError(resultEnum, msg, errorMsg);
    }

    /**
     * 自定义异常
     *
     * @param msg
     * @return
     */
    public static <T> ResultBean<T> error(ResultEnum resultEnum, String msg) {
        return baseError(resultEnum, msg, null);
    }

    /**
     * 参数错误
     *
     * @param msg
     * @return
     */
    public static <T> ResultBean<T> errorParam(String msg) {
        return baseError(ResultEnum.ERROR_PARAM, msg, null);
    }

    /**
     * token异常
     *
     * @param msg
     * @return
     */
    public static <T> ResultBean<T> errorToken(String msg) {
        return baseError(ResultEnum.ERROR_TOKEN, msg, null);
    }

    /**
     * 失败
     *
     * @param msg
     * @return
     */
    public static <T> ResultBean<T> error(String msg) {
        return baseError(ResultEnum.ERROR, msg, null);
    }

    /**
     * 没有权限
     *
     * @param msg
     * @return
     */
    public static <T> ResultBean<T> errorRole(String msg) {
        return baseError(ResultEnum.ERROR_ROLE, msg, null);
    }

    /**
     * 系统异常
     *
     * @param msg
     * @param errorMsg
     * @return
     */
    public static <T> ResultBean<T> errorException(String msg, Exception errorMsg) {
        return baseError(ResultEnum.ERROR_EXCEPTION, msg, errorMsg);
    }

    private static final long serialVersionUID = 1L;
}
