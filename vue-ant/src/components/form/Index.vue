<template>
    <a-form ref="form"
            :model="form"
            :rules="rules"
            :hideRequiredMark="hideRequiredMark"
            :labelAlign="labelAlign"
            :layout="layout"
            :labelCol="labelCol"
            :wrapperCol="wrapperCol"
            :validateOnRuleChange="validateOnRuleChange"
            :scrollToFirstError="scrollToFirstError"
            :name="name"
            :validateTrigger="validateTrigger">
        <slot name="headerSlot"></slot>
        <template v-for="item in itemList" :key="item.prop">
            <template v-if="item.slot">
                <slot name="internalSlot"></slot>
            </template>
            <template v-else-if="item.divider">
                <a-divider :orientation="item.orientation || 'left'"
                           :dashed="item.dashed"
                           class="my-divider">
                    {{item.content}}
                </a-divider>
            </template>
            <template v-else>
                <!--代表一行多项-->
                <template v-if="null != item.items && 0 < item.items.length">
                    <div style="display: flex;">
                        <template v-for="i in item.items" :key="i.prop">
                            <wei-form-item :form="form"
                                           :item="i"></wei-form-item>
                        </template>
                    </div>
                </template>
                <template v-else>
                    <wei-form-item :form="form"
                                   :item="item"></wei-form-item>
                </template>
            </template>
        </template>
        <slot name="footerSlot"></slot>
        <a-form-item>
            <a-button type="primary" @click="onSubmit">
                提交
            </a-button>
            <a-button style="margin-left: 10px;">
                Cancel
            </a-button>
        </a-form-item>
    </a-form>
</template>

<!--ref目前没有成功绑定到ant-design-vue的组件上-->
<script>
    import {Form, Button, Divider} from 'ant-design-vue';
    import $ant from '@/utils/ant';
    import {defineAsyncComponent} from 'vue';

    export default {
        name: "WeiForm",
        components: {
            [Form.name]: Form,
            [Form.Item.name]: Form.Item,
            [Button.name]: Button,
            [Divider.name]: Divider,
            'wei-form-item': defineAsyncComponent(() => import('./Item.vue')),
        },
        props: {
            //表单数据对象
            form: {
                type: Object,
                default: () => ({})
            },
            //表单的项
            itemList: {
                type: Array,
                default: () => ([])
            },
            //表单验证规则
            rules: {
                type: Object,
                default: () => ({})
            },
            //隐藏所有表单项的必选标记
            hideRequiredMark: {
                type: Boolean,
                default: false
            },
            //label 标签的文本对齐方式
            labelAlign: {
                type: String,
                default: 'right'
            },
            //表单布局
            layout: {
                type: String,
                default: 'horizontal'
            },
            //label 标签布局，同 <Col> 组件，设置 span offset 值，如 {span: 3, offset: 12} 或 sm: {span: 3, offset: 12}
            labelCol: {
                type: Object,
                default: () => ({})
            },
            //需要为输入控件设置布局样式时，使用该属性，用法同 labelCol
            wrapperCol: {
                type: Object,
                default: () => ({})
            },
            //是否在 rules 属性改变后立即触发一次验证
            validateOnRuleChange: {
                type: Boolean,
                default: true
            },
            //提交失败自动滚动到第一个错误字段
            scrollToFirstError: {
                type: Boolean,
                default: true
            },
            //表单名称，会作为表单字段 id 前缀使用
            name: {
                type: String,
                default: ''
            },
            //统一设置字段校验规则
            validateTrigger: {},
            //必选时，空格是否会被视为错误
            whitespace: {
                type: Boolean,
                default: true
            }
        },
        watch: {
            form() {
                //表单值改变时
                //移除表单项的校验结果。传入待移除的表单项的 name 属性或者 name 组成的数组，如不传则移除整个表单的校验结果
                this.$refs['form'].clearValidate();
            }
        },
        mounted() {
            //对外抛出自身
            this.$emit('my-ref', this);
        },
        methods: {
            onSubmit() {
                this.$refs['form'].validate()
                    .then(() => {
                        let {itemList, form, whitespace} = this;
                        let formData = {};
                        for (let i = 0; i < itemList.length; i++) {
                            let {prop, label, required, hidden, disabled, items, slot} = itemList[i];
                            //如果隐藏本表单项
                            if (hidden) {
                                continue;
                            }
                            //如果禁用本表单
                            if (disabled) {
                                continue;
                            }
                            if (null == items || 0 >= items.length) {
                                //如果未指定prop
                                if (null == prop) {
                                    continue;
                                }
                                let value = form[prop];
                                //赋值
                                formData[prop] = form[prop];
                                //非必填
                                if (!required) {
                                    continue;
                                }
                                //如果必填并且值为空
                                if (null == value) {
                                    //滚动到对应字段位置
                                    this.$refs['form'].scrollToField();
                                    $ant.errorMsg(`${label}不能为空`);
                                    return;
                                }
                                //必选时，空格是否会被视为错误
                                if (whitespace) {
                                    //如果是空字符串
                                    if ('' === ('' + value).replace(/(^\s*)|(\s*$)/g, "")) {
                                        //滚动到对应字段位置
                                        this.$refs['form'].scrollToField();
                                        $ant.errorMsg(`${label}不能为空`);
                                        return;
                                    }
                                }
                                continue;
                            }
                            for (let j = 0; j < items.length; j++) {
                                let {prop, label, required, hidden, disabled} = items[j];
                                //如果隐藏本表单项
                                if (hidden) {
                                    continue;
                                }
                                //如果禁用本表单
                                if (disabled) {
                                    continue;
                                }
                                let value = form[prop];
                                //赋值
                                formData[prop] = form[prop];
                                //非必填
                                if (!required) {
                                    continue;
                                }
                                //如果必填并且值为空
                                if (null == value) {
                                    //滚动到对应字段位置
                                    this.$refs['form'].scrollToField();
                                    $ant.errorMsg(`${label}不能为空`);
                                    return;
                                }
                                //必选时，空格是否会被视为错误
                                if (whitespace) {
                                    //如果是空字符串
                                    if ('' === ('' + value).replace(/(^\s*)|(\s*$)/g, "")) {
                                        //滚动到对应字段位置
                                        this.$refs['form'].scrollToField();
                                        $ant.errorMsg(`${label}不能为空`);
                                        return;
                                    }
                                }
                            }
                        }
                        //如果有id
                        if (form.hasOwnProperty('id')) {
                            formData['id'] = form['id'];
                        }
                        try {
                            //对外抛出验证后的表单提交事件
                            this.$emit('onSubmit', formData);
                        } catch (e) {
                            console.error(e);
                        }
                    });
            }
        }
    }
</script>

<style lang="less" scoped>
    ::v-deep(.my-divider) {
        &:before {
            border-top: 1px solid #777;
        }

        &:after {
            border-top: 1px solid #777;
        }
    }
</style>