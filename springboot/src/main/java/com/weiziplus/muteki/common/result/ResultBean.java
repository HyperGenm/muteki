package com.weiziplus.muteki.common.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

import java.io.Serializable;

/**
 * 统一返回结果
 *
 * @author wanglongwei
 * @date 2019/5/24 15:58
 */
@Getter
@ApiModel("统一返回结果")
public class ResultBean<T> implements Serializable {

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
     * 创建无参数ResultUtil对象---fastjson反序列化需要无参数
     */
    public ResultBean() {
    }

    /**
     * 创建ResultUtil对象
     *
     * @param code
     * @param msg
     * @param data
     */
    private ResultBean(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
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
     * @return
     */
    private static <T> ResultBean<T> baseError(ResultEnum resultEnum, String msg) {
        return new ResultBean<>(resultEnum.getValue(), msg, null);
    }

    /**
     * 自定义异常
     *
     * @param msg
     * @return
     */
    public static <T> ResultBean<T> error(ResultEnum resultEnum, String msg) {
        return baseError(resultEnum, msg);
    }

    /**
     * 参数错误
     *
     * @param msg
     * @return
     */
    public static <T> ResultBean<T> errorParam(String msg) {
        return baseError(ResultEnum.ERROR_PARAM, msg);
    }

    /**
     * token异常
     *
     * @param msg
     * @return
     */
    public static <T> ResultBean<T> errorToken(String msg) {
        return baseError(ResultEnum.ERROR_TOKEN, msg);
    }

    /**
     * 失败
     *
     * @param msg
     * @return
     */
    public static <T> ResultBean<T> error(String msg) {
        return baseError(ResultEnum.ERROR, msg);
    }

    /**
     * 没有权限
     *
     * @param msg
     * @return
     */
    public static <T> ResultBean<T> errorRole(String msg) {
        return baseError(ResultEnum.ERROR_ROLE, msg);
    }

    /**
     * 系统异常
     *
     * @param msg
     * @return
     */
    public static <T> ResultBean<T> errorException(String msg) {
        return baseError(ResultEnum.ERROR_EXCEPTION, msg);
    }

    private static final long serialVersionUID = 1L;
}
