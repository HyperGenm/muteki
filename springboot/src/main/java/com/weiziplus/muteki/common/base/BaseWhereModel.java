package com.weiziplus.muteki.common.base;

import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.Collection;

/**
 * sql字段与条件和值
 *
 * @author wanglongwei
 * @date 2020/06/03 14/57
 */
@Slf4j
@Getter
@Accessors(chain = true)
public class BaseWhereModel implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 数据库字段
     */
    private String column;

    /**
     * where条件,默认等于=
     */
    private BaseWhereEnum where = BaseWhereEnum.EQUAL;

    /**
     * 值
     */
    private Object value;

    private BaseWhereModel() {

    }

    /**
     * 创建where
     *
     * @param column
     * @param where  默认为 等于=
     * @param value
     */
    public BaseWhereModel(String column, BaseWhereEnum where, Object value) {
        this.column = column;
        this.where = where;
        this.value = value;
    }

    /**
     * 创建where,默认为等于
     *
     * @param column
     * @param value
     */
    public BaseWhereModel(String column, Object value) {
        this.column = column;
        this.value = value;
    }

    /**
     * 等于
     *
     * @param column
     * @param value
     * @return
     */
    public static BaseWhereModel eq(String column, Object value) {
        return new BaseWhereModel(column, BaseWhereEnum.EQUAL, value);
    }

    /**
     * 不等于
     *
     * @param column
     * @param value
     * @return
     */
    public static BaseWhereModel notEq(String column, Object value) {
        return new BaseWhereModel(column, BaseWhereEnum.NOT_EQUAL, value);
    }

    /**
     * 大于
     *
     * @param column
     * @param value
     * @return
     */
    public static BaseWhereModel moreThan(String column, Object value) {
        return new BaseWhereModel(column, BaseWhereEnum.MORE_THAN, value);
    }

    /**
     * 小于
     *
     * @param column
     * @param value
     * @return
     */
    public static BaseWhereModel lessThan(String column, Object value) {
        return new BaseWhereModel(column, BaseWhereEnum.LESS_THAN, value);
    }

    /**
     * 大于等于
     *
     * @param column
     * @param value
     * @return
     */
    public static BaseWhereModel moreThanEqual(String column, Object value) {
        return new BaseWhereModel(column, BaseWhereEnum.MORE_THAN_EQUAL, value);
    }

    /**
     * 小于等于
     *
     * @param column
     * @param value
     * @return
     */
    public static BaseWhereModel lessThanEqual(String column, Object value) {
        return new BaseWhereModel(column, BaseWhereEnum.LESS_THAN_EQUAL, value);
    }

    /**
     * in
     *
     * @param column
     * @param value
     * @return
     */
    public static BaseWhereModel in(String column, Collection<?> value) {
        return new BaseWhereModel(column, BaseWhereEnum.IN, value);
    }

    /**
     * notIn
     *
     * @param column
     * @param value
     * @return
     */
    public static BaseWhereModel notIn(String column, Collection<?> value) {
        return new BaseWhereModel(column, BaseWhereEnum.NOT_IN, value);
    }

    /**
     * like
     *
     * @param column
     * @param value
     * @return
     */
    public static BaseWhereModel like(String column, String value) {
        String keyWord = "%";
        //如果没有设置%
        if (!value.startsWith(keyWord) && !value.endsWith(keyWord)) {
            value = keyWord + value + keyWord;
        }
        return new BaseWhereModel(column, BaseWhereEnum.LIKE, value);
    }

}