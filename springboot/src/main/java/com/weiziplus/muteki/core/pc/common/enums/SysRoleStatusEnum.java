package com.weiziplus.muteki.core.pc.common.enums;

import lombok.Getter;

/**
 * @author wanglongwei
 * @date 2020/08/04 17/58
 */
@Getter
public enum SysRoleStatusEnum {

    /**
     * 状态
     */
    NORMAL("正常", 1),
    DISABLE("禁用", 2);

    private final String name;
    private final Integer value;

    SysRoleStatusEnum(String name, Integer value) {
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
        for (SysRoleStatusEnum sysRoleStatusEnum : SysRoleStatusEnum.values()) {
            if (sysRoleStatusEnum.getValue().equals(value)) {
                return true;
            }
        }
        return false;
    }

}