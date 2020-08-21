package com.weiziplus.muteki.core.pc.common.enums;

import lombok.Getter;

/**
 * 用户状态
 *
 * @author wanglongwei
 * @date 2020/08/04 08/57
 */
@Getter
public enum SysUserStatusEnum {

    /**
     * 状态
     */
    NORMAL("正常", 1),
    DISABLE("禁用", 2);

    private final String name;
    private final Integer value;

    SysUserStatusEnum(String name, Integer value) {
        this.name = name;
        this.value = value;
    }

    /**
     * 是否存在
     *
     * @param value
     * @return
     */
    public static boolean contains(Integer value) {
        for (SysUserStatusEnum sysUserStatusEnum : SysUserStatusEnum.values()) {
            if (sysUserStatusEnum.getValue().equals(value)) {
                return true;
            }
        }
        return false;
    }

}
