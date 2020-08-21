package com.weiziplus.muteki.core.api.common.enums;

import lombok.Getter;

/**
 * @author wanglongwei
 * @date 2020/08/20 09/22
 */
@Getter
public enum UserStatusEnum {

    /**
     * 状态
     */
    NORMAL("正常", 1),
    DISABLE("禁用", 2),
    SEAL("封号", 3);

    private final String name;
    private final Integer value;

    UserStatusEnum(String name, Integer value) {
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
        for (UserStatusEnum statusEnum : UserStatusEnum.values()) {
            if (statusEnum.getValue().equals(value)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 根据value获取name值
     *
     * @param value
     * @return
     */
    public static String getName(Integer value) {
        for (UserStatusEnum statusEnum : UserStatusEnum.values()) {
            if (statusEnum.getValue().equals(value)) {
                return statusEnum.getName();
            }
        }
        return "未知状态";
    }

}
