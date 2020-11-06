<template>
    <div id="index">
        <el-form class="container">
            <h3 class="title">系统登录</h3>
            <el-form-item>
                <el-input autocomplete placeholder="用户名"
                          v-model="form.username"></el-input>
            </el-form-item>
            <el-form-item>
                <el-input type="password" placeholder="密码"
                          v-model="form.password"></el-input>
            </el-form-item>
            <el-form-item>
                <el-row>
                    <el-col :span="12">
                        <el-input v-model="form.code" placeholder="验证码"
                                  @change="login"/>
                    </el-col>
                    <el-col :span="10" :offset="2">
                        <img alt="验证码"
                             :src="imgSrc"
                             @click="refreshCode">
                    </el-col>
                </el-row>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" style="width: 100%;"
                           :loading="loginLoading"
                           @click="login">登录
                </el-button>
            </el-form-item>
        </el-form>
    </div>
</template>

<script>
    export default {
        name: "Index",
        data() {
            let that = this;
            return {
                form: {
                    //用户名
                    username: '',
                    //密码
                    password: '',
                    //验证码
                    code: '',
                },
                //验证码base64
                imgSrc: '',
                //uuid
                uuid: that.$globalFun.createUUID(),
                //登录中
                loginLoading: false
            }
        },
        mounted() {
            /**
             * 判断是否是用户退出登录，如果是用户退出登录就重新刷新页面，解决路由bug
             * @type {string}
             */
            let status = sessionStorage.getItem("loginStatus");
            if (null != status && 'logout' === status) {
                sessionStorage.removeItem('loginStatus');
                window.location.reload();
                return;
            }
            this.refreshCode();
        },
        methods: {
            refreshCode() {
                let that = this;
                this.$axios({
                    url: that.$global.URL.loginValidateCode,
                    data: {
                        uuid: that.uuid
                    },
                    success(data) {
                        that.imgSrc = data;
                    }
                });
            },
            login() {
                let {username, password, code} = this.form;
                if (this.$globalFun.isBlank(username)
                    || 2 > username.length) {
                    this.$globalFun.errorMsg("用户名不能少于两位");
                    return;
                }
                if (this.$globalFun.isBlank(password)
                    || 6 > password.length) {
                    this.$globalFun.errorMsg("密码不能少于6位");
                    return;
                }
                if (this.$globalFun.isBlank(code)) {
                    this.$globalFun.errorMsg("验证码错误");
                    return;
                }
                this.loginLoading = true;
                let that = this;
                this.$axios({
                    allSuccess: true,
                    url: that.$global.URL.login,
                    method: 'post',
                    data: {
                        username, code,
                        uuid: that.uuid,
                        password: that.$cryptoJS.md5(password)
                    },
                    success(res) {
                        that.loginLoading = false;
                        if (200 !== res['code']) {
                            that.refreshCode();
                            that.$globalFun.errorMsg(res['msg']);
                            return;
                        }
                        that.handleSuccessLogin(res['data']);
                    },
                    fail() {
                        that.loginLoading = false;
                        that.refreshCode();
                    }
                });
            },
            /**
             * 处理成功登陆
             * @param data
             */
            handleSuccessLogin(data) {
                this.$globalFun.setSessionStorage(`token`, data['token']);
                let buttonMap = {};
                if (null != data['buttonSet']) {
                    data['buttonSet'].forEach(value => {
                        buttonMap[value] = true;
                    });
                }
                this.$globalFun.setSessionStorage('buttonMap', buttonMap);
                this.$globalFun.setSessionStorage('roleIds', data['roleIds']);
                this.$globalFun.setSessionStorage('userInfo', data['user']);
                let menuTree = data['menuTree'];
                if (null == menuTree || 0 >= menuTree.length) {
                    this.$globalFun.errorMsg("您还没有可用菜单，请联系管理员添加");
                    return;
                }
                this.$globalFun.setSessionStorage('menuTree', menuTree);
                this.handleRouter(menuTree);
            },
            /**
             * 处理动态路由
             */
            handleRouter(data) {
                let that = this;
                let routers = [];
                let haveHomePage = false;
                data.forEach((value) => {
                    //如果有首页优先展示首页
                    if ('home' === value['name']) {
                        haveHomePage = true;
                    }
                    let router = {
                        path: "/" + (2 === value['externalFlag'] ? value.name : value.path),
                        name: value.path,
                        meta: {
                            title: value.title,
                            icon: value.icon,
                            externalUrl: value['path']
                        }
                    };
                    let children = value.children;
                    //如果存在子级
                    if (null != children && 0 < children.length) {
                        router['components'] = require('@/views/common/station/Index');
                        router['redirect'] = `/${value.path}/${children[0]['path']}`;
                        router['children'] = that.childrenRouter(value.path, children);
                    } else {
                        //如果是外部超链接
                        if (2 === value['externalFlag']) {
                            router['components'] = require(`@/views/common/externalUrl/Index.vue`);
                        } else {
                            try {
                                router['components'] = require(`@/views/main/${value.path}/Index.vue`);
                            } catch (e) {
                                console.debug(value.path, '没有找到对应组件', '---详情', e);
                                router['components'] = require(`@/views/common/errorPage/404.vue`);
                            }
                        }
                    }
                    routers.push(router);
                });
                let parentRouters = [{
                    path: '/',
                    components: require('@/views/common/layout/Index.vue'),
                    name: 'index',
                    children: routers
                }];
                this.$router.addRoutes(parentRouters);
                this.$router.push(haveHomePage ? '/home' : routers[0]['path'] || '/');
            },
            childrenRouter(parentPath, data) {
                let routers = [];
                let that = this;
                data.forEach((value) => {
                    let router = {
                        path: (2 === value['externalFlag'] ? value.name : value.path),
                        name: value.path,
                        meta: {
                            title: value.title,
                            icon: value.icon,
                            externalUrl: value['path']
                        }
                    };
                    let children = value.children;
                    //如果存在子级
                    if (null != children && 0 < children.length) {
                        router['components'] = require('@/views/common/station/Index.vue');
                        router['redirect'] = `/${parentPath}/${value.path}/${children[0]['path']}`;
                        router['children'] = that.childrenRouter(parentPath + '/' + value.path, children);
                    } else {
                        //如果是外部超链接
                        if (2 === value['externalFlag']) {
                            router['components'] = require(`@/views/common/externalUrl/Index.vue`);
                        } else {
                            try {
                                router['components'] = require(`@/views/main/${parentPath}/${value.path}/Index.vue`);
                            } catch (e) {
                                console.debug(value.path, '没有找到对应组件', '---详情', e);
                                router['components'] = require(`@/views/common/errorPage/404.vue`);
                            }
                        }
                    }
                    routers.push(router);
                });
                return routers;
            }
        }
    }
</script>

<style lang="scss" scoped>
    #index {
        position: fixed;
        top: 0;
        width: 100%;
        bottom: 0;
        background: url("../../assets/loginBg.jpg") no-repeat;
        background-size: 100% 100%;
        display: flex;
        align-items: center;

        img {
            width: 100%;
        }

        .container {
            border-radius: 15px;
            margin: 0 auto;
            width: 350px;
            padding: 35px 35px 15px 35px;
            background: #fff;
            border: 1px solid #eaeaea;
            box-shadow: 0 0 7px #cac6c6;
        }

        .title {
            margin: 0 auto 40px auto;
            text-align: center;
            color: #505458;
        }

    }

</style>