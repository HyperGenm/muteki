<template>
    <a-form-item v-if="!item.hidden"
                 :style="`flex: ${item.flex || 1};`"
                 :name="item.prop"
                 :rules="item.rules"
                 :autoLink="null != item.autoLink ? item.autoLink : true"
                 :colon="item.colon"
                 :extra="item.extra"
                 :hasFeedback="item.hasFeedback"
                 :help="item.help"
                 :label="item.label"
                 :labelAlign="item.labelAlign"
                 :required="item.required"
                 :validateStatus="item.validateStatus"
                 :wrapperCol="item.wrapperCol || {flex:1}"
                 :validateFirst="item.validateFirst"
                 :validateTrigger="item.validateTrigger">
        <template v-if="'input' === item.type">
            <a-input v-model:value="form[item.prop]" allowClear
                     :placeholder="item.placeholder || '请输入'"
                     :type="item.inputType"
                     :disabled="item.disabled"/>
        </template>
        <template v-else-if="'textarea' === item.type">
            <a-textarea v-model:value="form[item.prop]"
                        allowClear autoSize
                        :showCount="item.showCount"
                        :style="`width:${item.width || '100%'}`"
                        :placeholder="item.placeholder || '请输入'"
                        :maxlength="item.maxlength"
                        :disabled="item.disabled"/>
        </template>
        <template v-else-if="'select' === item.type">
            <a-select v-model:value="form[item.prop]"
                      :placeholder="item.placeholder || '请选择'"
                      allowClear showSearch
                      :style="item.style || 'width:200px;'"
                      :mode="item.mode"
                      :disabled="item.disabled">
                <Option v-for="option in item.options"
                        :value="option.value">
                    {{option.label || option.value}}
                </Option>
            </a-select>
        </template>
        <template v-else-if="'cascader' === item.type">
            <a-cascader v-model:value="form[item.prop]" showSearch
                        :style="item.style || 'width:200px;'"
                        :options="item.options"
                        :changeOnSelect="item.changeOnSelect"
                        :disabled="item.disabled"
                        :fieldNames="item.fieldNames"
                        :placeholder="item.placeholder || '请选择'"/>
        </template>
        <template v-else-if="'datePicker' === item.type">
            <a-date-picker v-model:value="form[item.prop]"
                           :placeholder="item.placeholder || '请选择'"
                           :valueFormat="item.valueFormat || 'YYYY-MM-DD'"
                           allowClear
                           :disabled="item.disabled"/>
        </template>
        <template v-else-if="'timePicker' === item.type">
            <a-time-picker v-model:value="form[item.prop]"
                           :placeholder="item.placeholder || '请选择'"
                           :valueFormat="item.valueFormat || 'HH:mm:ss'"
                           allowClear
                           :disabled="item.disabled"/>
        </template>
        <template v-else-if="'radio' === item.type">
            <a-radio-group v-model:value="form[item.prop]"
                           :size="item.size"
                           :name="item.name"
                           :buttonStyle="item.buttonStyle"
                           :disabled="item.disabled">
                <template v-for="option in item.options"
                          :key="option.value">
                    <a-radio :value="option.value">
                        {{option.label || option.value}}
                    </a-radio>
                </template>
            </a-radio-group>
        </template>
        <template v-else>
            {{item.label}}没有指定type
        </template>
    </a-form-item>
</template>

<script>
    import {
        Form,
        Switch,
        DatePicker,
        TimePicker,
        Radio,
        Button,
        Input,
        Select,
        Cascader,
    } from 'ant-design-vue';

    export default {
        name: "WeiFormItem",
        components: {
            [Form.name]: Form,
            [Form.Item.name]: Form.Item,
            [Switch.name]: Switch,
            [DatePicker.name]: DatePicker,
            [TimePicker.name]: TimePicker,
            [Radio.name]: Radio,
            [Radio.Group.name]: Radio.Group,
            [Button.name]: Button,
            [Input.name]: Input,
            [Input.TextArea.name]: Input.TextArea,
            [Select.name]: Select,
            [Select.Option.name]: Select.Option,
            [Cascader.name]: Cascader,
        },
        props: {
            //表单值
            form: {
                type: Object,
                default: ({})
            },
            //表单项
            item: {
                type: Object,
                default: ({})
            },
        }
    }
</script>

<style lang="less" scoped>

    ::v-deep(.ant-form-item-label) {
        width: 7rem;
    }

    ::v-deep(.ant-form-item-control-wrapper) {
        flex: 1;
    }

</style>