package com.weiziplus.muteki.core.pc.common.enums;

import lombok.Getter;

/**
 * @author wanglongwei
 * @date 2020/08/20 11/21
 */
@Getter
public enum SysFunctionExternalEnum {

    /**
     * 外部链接
     */
    INTERNAL("内部", 1),
    EXTERNAL("外部超链接", 2);

    private final String name;
    private final Integer value;

    SysFunctionExternalEnum(String name, Integer value) {
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
        for (SysFunctionExternalEnum externalEnum : SysFunctionExternalEnum.values()) {
            if (externalEnum.getValue().equals(value)) {
                return true;
            }
        }
        return false;
    }
}
