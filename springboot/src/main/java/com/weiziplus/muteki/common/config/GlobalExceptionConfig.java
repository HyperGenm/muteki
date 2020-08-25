package com.weiziplus.muteki.common.config;

import com.weiziplus.muteki.common.async.ErrorAsync;
import com.weiziplus.muteki.common.result.ResultBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;

/**
 * 全局异常处理
 *
 * @author wanglongwei
 * @date 2019/5/6 15:50
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionConfig {

    @Autowired
    ErrorAsync errorAsync;

    /**
     * 捕获运行时异常
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(RuntimeException.class)
    public ResultBean runtimeException(RuntimeException ex) {
        log.warn("全局捕获运行时异常" + ex.getStackTrace()[0] + ":" + ex);
        errorAsync.saveError(ex, "系统捕获运行时异常" + ex.getStackTrace()[0]);
        return ResultBean.errorException("系统异常，请重试。RuntimeException", ex);
    }

    /**
     * 捕获空指针异常
     * 因为打印信息较少，单独处理
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(NullPointerException.class)
    public ResultBean nullPointerException(NullPointerException ex) {
        log.warn("系统捕获空指针异常NullPointerException,详情:", ex);
        errorAsync.saveError(ex, "系统捕获空指针异常NullPointerException" + ex.getStackTrace()[0]);
        return ResultBean.errorException("系统异常，请重试。NullPointerException", ex);
    }

    /**
     * 捕获所有异常
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(Exception.class)
    public ResultBean exception(Exception ex) {
        log.warn("全局捕获所有异常" + ex.getStackTrace()[0] + ":" + ex);
        errorAsync.saveError(ex, "系统捕获异常"+ ex.getStackTrace()[0]);
        return ResultBean.errorException("系统异常，请重试。Exception", ex);
    }

    /**
     * 405异常
     *
     * @return
     */
    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    public ResultBean httpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        return ResultBean.error("请使用" + ex.getSupportedHttpMethods() + "方法请求,详情:" + ex.getMessage());
    }

    /**
     * post请求参数校验抛出的异常
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResultBean methodArgumentNotValidException(MethodArgumentNotValidException ex) {
        return ResultBean.errorParam(ex.getBindingResult().getFieldError().getDefaultMessage());
    }

    /**
     * get请求参数校验抛出的异常
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(BindException.class)
    public ResultBean bindException(BindException ex) {
        return ResultBean.errorParam(ex.getBindingResult().getFieldError().getDefaultMessage());
    }

    /**
     * 请求方法中校验抛出的异常
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResultBean constraintViolationException(ConstraintViolationException ex) {
        return ResultBean.errorParam(ex.getConstraintViolations().iterator().next().getMessage());
    }

}
