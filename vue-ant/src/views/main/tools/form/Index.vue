<template>
    <my-form :form="form.form"
             :itemList="form.itemList"
             :rules="form.rules"
             @onSubmit="form.onSubmit">
        <template v-slot:headerSlot>
            <div style="color: #f40;">header插槽---可以自定义任何内容</div>
        </template>
        <template v-slot:internalSlot>
            <a-form-item label="内部插槽">
                <a-checkbox-group v-model:value="form.form.type">
                    <a-checkbox value="1" name="type">
                        Online
                    </a-checkbox>
                    <a-checkbox value="2" name="type">
                        Promotion
                    </a-checkbox>
                    <a-checkbox value="3" name="type">
                        Offline
                    </a-checkbox>
                </a-checkbox-group>
            </a-form-item>
        </template>
        <template v-slot:footerSlot>
            <div style="color: #ff4400;">footer插槽---可以自定义任何内容</div>
        </template>
    </my-form>
</template>

<script>
    import {ref, defineAsyncComponent, reactive} from 'vue';
    import {Form, Checkbox} from 'ant-design-vue';
    import $ant from '@/utils/ant';

    export default {
        name: "Index",
        components: {
            [Form.Item.name]: Form.Item,
            [Checkbox.name]: Checkbox,
            [Checkbox.Group.name]: Checkbox.Group,
            'my-form': defineAsyncComponent(() => import('@/components/form/Index.vue'))
        },
        setup() {

            let form = reactive({
                form: {},
                itemList: [
                    //分割线
                    {divider: true, content: '基本信息'},
                    {type: 'input', label: '用户名', prop: 'username', required: true},
                    {
                        items: [
                            {type: 'input', label: '手机号', prop: 'phone', required: true},
                            {type: 'input', label: '邮箱', prop: 'email', flex: 2},
                        ]
                    },
                    {divider: true, content: '详情'},
                    {type: 'input', label: '地址', prop: 'address'},
                    {
                        items: [
                            {
                                type: 'radio', label: '性别', prop: 'sex', required: true,
                                options: [
                                    {label: '男', value: '男'},
                                    {label: '女', value: '女'},
                                ]
                            },
                            {
                                type: 'select', label: '下拉框', prop: 'select', required: true,
                                options: [
                                    {label: '男', value: '男'},
                                    {label: '女', value: '女'},
                                    {label: '选项三', value: '选项三'},
                                ]
                            },
                            {type: 'datePicker', label: '生日', prop: 'birthDay'},
                        ]
                    },
                    {slot: true},
                    {type: 'textarea', label: '简介', prop: 'remark'},
                ],
                rules: {},
                onSubmit(form) {
                    $ant.alert({
                        content: JSON.stringify(form)
                    });
                }
            });
            return {
                form
            }
        }
    }
</script>

<style lang="less" scoped>

    ::v-deep(.ant-form-item-label) {
        width: 7rem;
    }

</style>