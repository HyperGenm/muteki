package com.weiziplus.muteki.core.pc.common.enums;

import lombok.Getter;

/**
 * @author wanglongwei
 * @date 2020/08/04 09/32
 */
@Getter
public enum SysFunctionSuperEnum {

    /**
     * 超级管理员专属
     */
    NORMAL("普通", 1),
    VIP("超级管理员专属", 2);

    private final String name;
    private final Integer value;

    SysFunctionSuperEnum(String name, Integer value) {
        this.name = name;
        this.value = value;
    }

    /**
     * 是否存在
     *
     * @param superFlag
     * @return
     */
    public static boolean contains(Integer superFlag) {
        for (SysFunctionSuperEnum value : SysFunctionSuperEnum.values()) {
            if (value.getValue().equals(superFlag)) {
                return true;
            }
        }
        return false;
    }
}
