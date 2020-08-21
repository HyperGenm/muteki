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
            }
        },
        data() {
            return {
                formOptions: [
                    {type: 'input', label: '用户名', prop: 'username', required: true},
                    {type: 'input', label: '密码', prop: 'password', inputType: 'password', required: true},
                    {type: 'input', label: '真实姓名', prop: 'realName', required: true},
                    {type: 'input', label: '手机号', prop: 'phone', required: true},
                    {
                        type: 'radio', label: '状态', prop: 'status', required: true,
                        options: [
                            {label: '正常', value: 1},
                            {label: '禁用', value: 2}
                        ]
                    },
                    {
                        type: 'select', label: '角色', prop: 'roleIds', multiple: true,
                        options: []
                    },
                ],
                form: this.formData
            }
        },
        mounted() {
            let that = this;
            this.$axios({
                url: that.$global.URL.system.sysRole.getList,
                success(data) {
                    let options = [];
                    data.forEach(value => {
                        options.push({
                            label: value.name,
                            value: value.id
                        })
                    });
                    that.formOptions[5]['options'] = options;
                }
            })
        },
        methods: {
            submit(form) {
                let that = this;
                let {password} = form;
                if (6 > password.length) {
                    this.$globalFun.errorMsg('密码不能少于6位');
                    return;
                }
                form['password'] = this.$cryptoJS.md5(form['password']);
                that.$axios({
                    url: that.$global.URL.system.sysUser[that.editType],
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