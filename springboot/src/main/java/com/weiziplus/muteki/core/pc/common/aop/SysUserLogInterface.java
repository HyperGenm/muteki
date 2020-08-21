package com.weiziplus.muteki.core.pc.common.aop;

import com.weiziplus.muteki.core.pc.common.interceptor.PcAuthToken;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 系统用户日志
 *
 * @author wanglongwei
 * @date 2020/07/30 16/01
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@PcAuthToken
public @interface SysUserLogInterface {
    //操作描述
    String description() default "";

    //操作类型  1:查询,2:新增,3:修改,4:删除
    int type() default TYPE_SELECT;

    /**
     * 1:查询
     */
    int TYPE_SELECT = 1;

    /**
     * 2:新增
     */
    int TYPE_INSERT = 2;

    /**
     * 3:修改
     */
    int TYPE_UPDATE = 3;

    /**
     * 4:删除
     */
    int TYPE_DELETE = 4;

    //忽略的参数
    String paramIgnore() default "password,__t";
}
