<template>
    <div id="index">
        <wei-form :formData="form" :formOptions="formOptions"
                  @closeDialog="$emit('closeDialog')" @submit="submit">
        </wei-form>
    </div>
</template>

<script>
    export default {
        name: "EditForm",
        components: {
            'wei-form': () => import('@/components/form/Index.vue')
        },
        props: {
            //操作类型
            editType: {
                type: String,
                default: 'add'
            },
            //表单数据
            formData: {
                type: Object
            },
            //是否展示
            isShow: {
                type: Boolean,
                default: false
            }
        },
        data() {
            return {
                formOptions: [
                    {type: 'input', label: '名称', prop: 'name', required: true, disabled: false},
                    {
                        type: 'radio', label: '状态', prop: 'status', required: true,
                        options: [
                            {label: '正常', value: 1},
                            {label: '禁用', value: 2}
                        ]
                    },
                    {type: 'input', label: '排序', prop: 'sort', inputType: 'number'},
                    {type: 'textarea', label: '备注', prop: 'remark'},
                ],
                form: this.formData
            }
        },
        watch: {
            isShow(show) {
                if (show) {
                    this.changeFormOptions();
                }
            }
        },
        mounted() {
            this.changeFormOptions();
        },
        methods: {
            //编辑时，名称设置为禁用
            changeFormOptions() {
                this.formOptions[0]['disabled'] = 'add' !== this.editType;
                this.form = this.formData;
            },
            submit(form) {
                let that = this;
                that.$axios({
                    url: that.$global.URL.system.sysRole[that.editType],
                    method: 'post',
                    data: form,
                    success() {
                        that.$globalFun.successMsg('成功');
                        that.$emit('closeDialog');
                        that.$emit('renderTable');
                    }
                });
            }
        }
    }
</script>