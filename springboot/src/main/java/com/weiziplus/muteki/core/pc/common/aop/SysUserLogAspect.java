package com.weiziplus.muteki.core.pc.common.aop;

import com.alibaba.fastjson.JSON;
import com.weiziplus.muteki.common.async.LogAsync;
import com.weiziplus.muteki.common.base.BaseService;
import com.weiziplus.muteki.common.models.SysUserLog;
import com.weiziplus.muteki.common.result.ResultBean;
import com.weiziplus.muteki.common.result.ResultEnum;
import com.weiziplus.muteki.common.util.DateUtils;
import com.weiziplus.muteki.common.util.HttpRequestUtils;
import com.weiziplus.muteki.common.util.UserAgentUtils;
import com.weiziplus.muteki.core.pc.common.token.PcTokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 系统用户操作日志
 *
 * @author wanglongwei
 * @date 2020/08/13 15/53
 */
@Slf4j
@Aspect
@Component
public class SysUserLogAspect extends BaseService {

    /**
     * 统计请求的处理时间
     */
    private final ThreadLocal<Long> startTime = new ThreadLocal<>();

    @Autowired
    LogAsync logAsync;

    /**
     * 日志注解
     */
    @Pointcut("@annotation(com.weiziplus.muteki.core.pc.common.aop.SysUserLogInterface)")
    public void log() {

    }

    /**
     * 切点前
     *
     * @param joinPoint
     */
    @Before("log()")
    public void doBefore(JoinPoint joinPoint) {
        //设置访问开始时间
        startTime.set(System.currentTimeMillis());
    }

    /**
     * 正常返回处理
     *
     * @param joinPoint 连接点
     * @param result    返回结果
     */
    @AfterReturning(pointcut = "log()", returning = "result")
    public void afterReturn(JoinPoint joinPoint, Object result) {
        handlerAfter(joinPoint, result, null);
    }

    /**
     * 抛出异常的处理
     *
     * @param joinPoint
     * @param ex
     */
    @AfterThrowing(pointcut = "log()", throwing = "ex")
    public void afterThrowing(JoinPoint joinPoint, Throwable ex) {
        handlerAfter(joinPoint, null, ex);
    }

    /**
     * 处理after
     *
     * @param joinPoint
     * @param result    响应结果
     * @param ex        异常
     */
    private void handlerAfter(JoinPoint joinPoint, Object result, Throwable ex) {
        HttpServletRequest request = getRequest();
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        //查看是否有日志注解，有的话将日志信息放入数据库
        SysUserLogInterface logInterface = method.getAnnotation(SysUserLogInterface.class);
        if (null == logInterface) {
            return;
        }
        ResultBean resultBean;
        //响应状态码
        int resultCode = 200;
        //响应提示信息
        String resultMsg;
        if (null != result) {
            resultBean = (ResultBean) result;
            resultCode = resultBean.getCode();
            resultMsg = resultBean.getMsg();
        } else {
            //如果没有抛异常,返回的是流
            if (null == ex) {
                resultCode = ResultEnum.SUCCESS.getValue();
                resultMsg = "输出流";
            } else {
                resultCode = ResultEnum.ERROR_ASPECT.getValue();
                resultMsg = "系统日志aop异常,详情:" + ex.getMessage();
            }
        }
        //查看是否存在忽略参数
        String paramIgnore = logInterface.paramIgnore();
        Map<String, String[]> parameterMap = new HashMap<>(request.getParameterMap());
        //使用迭代器的remove()方法删除元素
        parameterMap.keySet().removeIf(paramIgnore::contains);
        SysUserLog sysUserLog = new SysUserLog()
                .setUserId(PcTokenUtils.getUserId())
                .setUrl(request.getRequestURI())
                .setMethodName(joinPoint.getTarget().getClass().getName() + method.getName())
                .setParam(JSON.toJSONString(parameterMap))
                .setType(logInterface.type())
                .setResultCode(resultCode)
                .setResultMsg(resultMsg)
                .setTimeConsuming((int) (System.currentTimeMillis() - startTime.get()))
                .setDescription(logInterface.description())
                .setIpAddress(HttpRequestUtils.getIpAddress(request))
                .setBorderName(UserAgentUtils.getBorderName(request))
                .setOsName(UserAgentUtils.getOsName(request))
                .setCreateTime(DateUtils.getNowDateTime());
        logAsync.saveLog(sysUserLog);
        startTime.remove();
    }

}