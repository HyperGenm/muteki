<template>
    <div id="index">
        <div class="box">
            <div class="title">
                <h1>WeiziPlus</h1>
            </div>
            <div class="row">
                <a-input placeholder="用户名" size="large"
                         v-model:value="form.username">
                    <template v-slot:prefix>
                        <user-outlined/>
                    </template>
                </a-input>
            </div>
            <div class="row">
                <a-input-password placeholder="密码" size="large"
                                  v-model:value="form.password">
                    <template v-slot:prefix>
                        <UnlockOutlined/>
                    </template>
                </a-input-password>
            </div>
            <div class="row code">
                <div class="input">
                    <a-input placeholder="验证码" size="large"
                             v-model:value="form.code"/>
                </div>
                <div class="img"
                     @click="getValidateCode">
                    <img :src="imgSrc">
                </div>
            </div>
            <a-button type="primary" block size="large"
                      @click="onSubmit">登录
            </a-button>
        </div>
    </div>
</template>

<script>
    import {ref, reactive, onMounted} from 'vue';
    import {UserOutlined, UnlockOutlined} from '@ant-design/icons-vue';
    import $function from '@/utils/function';
    import $global from '@/utils/global';
    import $axios from '@/utils/axios';
    import $ant from '@/utils/ant';
    import MyRouter from "../../router/MyRouter";
    import {Input, Button} from 'ant-design-vue';

    export default {
        name: "Login",
        components: {
            [UserOutlined.name]: UserOutlined,
            [UnlockOutlined.name]: UnlockOutlined,
            [Button.name]: Button,
            [Input.name]: Input,
            [Input.Password.name]: Input.Password,
        },
        setup() {
            //创建uuid
            const uuid = $function.createUUID();
            let form = reactive({
                username: '',
                password: '',
                code: ''
            });
            //验证码图片
            let imgSrc = ref('');
            //获取验证码
            let getValidateCode = () => {
                $axios({
                    url: $global.url.getValidateCode,
                    data: {
                        uuid
                    },
                    success(data) {
                        imgSrc.value = data;
                    }
                });
            }
            //点击登录
            let onSubmit = () => {
                let {username, password, code} = form;
                if ($function.isBlank(username)) {
                    $ant.errorMsg('用户名不能为空');
                    return;
                }
                if ($function.isBlank(password)) {
                    $ant.errorMsg('密码不能为空');
                    return;
                }
                if ($function.isBlank(code)) {
                    $ant.errorMsg('验证码不能为空');
                    return;
                }
                $axios({
                    url: $global.url.login,
                    method: 'post',
                    data: {
                        uuid,
                        code,
                        password,
                        username
                    },
                    success(data) {
                        $function.setLocationStorage('user', data['user']);
                        $function.setLocationStorage('menuTree', data['menuTree']);
                        $function.setLocationStorage('token', data['token']);
                        let buttonMap = {};
                        if (null != data['buttonSet']) {
                            data['buttonSet'].forEach(value => {
                                buttonMap[value] = true;
                            });
                        }
                        $function.setLocationStorage('buttonMap', buttonMap);
                        $function.setLocationStorage('roleIds', data['roleIds']);
                        MyRouter.initRouter();
                    }
                });
            }
            onMounted(() => {
                getValidateCode();
            });
            return {
                form,
                onSubmit,
                imgSrc,
                getValidateCode
            }
        }
    }
</script>

<style lang="less" scoped>
    #index {
        width: 100%;
        height: 100vh;
        display: flex;
        align-items: center;
        justify-content: center;
        background: url("../../assets/loginBg.jpg") no-repeat;
        background-size: 100% 100%;

        .box {
            width: 450px;
            padding: 30px 20px;
            border-radius: 15px;
            background: #fff;
            border: 1px solid #eaeaea;
            box-shadow: 0 0 7px #cac6c6;

            .title {
                text-align: center;
                margin: 0 0 20px;
            }

            .row {
                margin: 20px 0;
            }

            .code {
                display: flex;
                align-items: center;
                position: relative;

                .input {
                    width: 50%;
                }

                .img {
                    position: absolute;
                    top: 0;
                    right: 0;
                    height: 100%;
                    min-width: 120px;
                    background-color: #f5f5f5;

                    img {
                        height: 100%;
                    }
                }
            }
        }
    }
</style>