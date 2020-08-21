package com.weiziplus.muteki.core.pc.common.interceptor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 由此注解的接口只有超级管理员才可以访问
 *
 * @author wanglongwei
 * @date 2020/08/18 17/06
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@PcAuthToken
public @interface PcSuperAdminAuth {
}
