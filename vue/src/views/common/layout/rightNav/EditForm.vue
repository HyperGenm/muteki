<template>
    <div id="form">
        <el-form ref="form" :model="form" :rules="rules" label-width="80px">
            <el-form-item label="旧密码" prop="oldPwd" required>
                <el-input v-model="form.oldPwd" show-password></el-input>
            </el-form-item>
            <el-form-item label="新密码" prop="newPwd" required>
                <el-input v-model="form.newPwd" show-password></el-input>
            </el-form-item>
            <el-form-item label="重复密码" prop="againPwd" required>
                <el-input v-model="form.againPwd" show-password></el-input>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" @click="submit">提交</el-button>
                <el-button @closeDialog="$emit('closeDialog')">取消</el-button>
            </el-form-item>
        </el-form>
    </div>
</template>

<script>
    export default {
        name: "EditForm",
        data() {
            let that = this;
            return {
                rules: {
                    oldPwd: [
                        {required: true, message: '请输入原密码', trigger: 'blur'},
                        {min: 6, message: '密码最少6位', trigger: 'blur'}
                    ],
                    newPwd: [
                        {required: true, message: '请输入新密码', trigger: 'blur'},
                        {min: 6, message: '密码最少6位', trigger: 'blur'}
                    ],
                    againPwd: [
                        {
                            validator(rule, value, callback) {
                                let newPwd = that.form['newPwd'];
                                if (newPwd !== value) {
                                    callback(new Error('两次输入密码不一致!'));
                                    return;
                                }
                                callback();
                            }, trigger: 'blur'
                        }
                    ]
                },
                form: {
                    oldPwd: '',
                    newPwd: ''
                }
            }
        },
        methods: {
            submit(form) {
                let that = this;
                this.$refs['form'].validate((valid) => {
                    if (!valid) {
                        alert('submit!');
                    }
                    let {oldPwd, newPwd} = form;
                    that.$axios({
                        url: that.$global.URL.system.sysUser.updatePwd,
                        method: 'post',
                        data: {
                            oldPwd: that.$cryptoJS.md5(oldPwd),
                            newPwd: that.$cryptoJS.md5(newPwd),
                        },
                        success() {
                            that.$globalFun.successMsg('成功');
                            that.$emit('closeDialog');
                        }
                    });
                });
            }
        }
    }
</script>