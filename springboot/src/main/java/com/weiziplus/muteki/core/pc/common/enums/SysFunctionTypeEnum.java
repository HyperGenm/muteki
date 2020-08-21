package com.weiziplus.muteki.core.pc.common.enums;

import lombok.Getter;

/**
 * @author wanglongwei
 * @date 2020/08/04 09/30
 */
@Getter
public enum SysFunctionTypeEnum {

    /**
     * 功能类型
     */
    MENU("菜单", 1),
    BUTTON("按钮", 2);

    private final String name;
    private final Integer value;

    SysFunctionTypeEnum(String name, Integer value) {
        this.name = name;
        this.value = value;
    }

    /**
     * 是否存在
     *
     * @param type
     * @return
     */
    public static boolean contains(Integer type) {
        for (SysFunctionTypeEnum value : SysFunctionTypeEnum.values()) {
            if (value.getValue().equals(type)) {
                return true;
            }
        }
        return false;
    }
}
