<template>
    <div id="rightNav">
        <div class="header" ref="header">
            <div ref="collapse" class="collapse" @click="$emit('collapseChange')">
                <i v-if="menuCollapse" class="el-icon-s-unfold"></i>
                <i v-else class="el-icon-s-fold"></i>
            </div>
            <div class="tabs" :style="`max-width:${tabsMaxWidth}px`">
                <wei-tabs></wei-tabs>
            </div>
            <div ref="right" class="right">
                <div class="more">
                    <i class="el-icon-full-screen" style="font-size: 20px;" @click="changeSize"></i>
                    <div style="border-radius: 50%;overflow: hidden;border: 1px solid #f7f61a;display: flex;align-items: center;justify-content: center;padding: 5px;margin: 0 5px;"
                         @click="updateIcon">
                        <el-image style="width: 30px;height: 30px;border-radius: 50%;"
                                  :src="userInfo['icon']">
                            <div slot="error">
                                <i style="font-size: 21px;" class="el-icon-picture-outline"></i>
                            </div>
                        </el-image>
                    </div>
                </div>
                <div class="user">
                    <el-dropdown @command="handleCommand">
                        <span style="color: #954ae0">Hi,{{userInfo['realName'] || userInfo['username']}}</span>
                        <el-dropdown-menu slot="dropdown">
                            <el-dropdown-item v-for="item in dropdownItems" :key="item.title"
                                              :command="item.command"> {{item.title}}
                            </el-dropdown-item>
                        </el-dropdown-menu>
                    </el-dropdown>
                </div>
            </div>
        </div>
        <template>
            <el-dialog title="修改密码" :visible.sync="dialogEditForm">
                <edit-form @closeDialog="dialogEditForm = false"></edit-form>
            </el-dialog>
            <el-dialog title="修改头像" :visible.sync="dialogEditIcon">
                <wei-upload :action="$global.URL.system.sysUser.updateIcon"
                            tip="建议长宽比1:1"
                            :fileList.sync="fileList"
                            @success="iconSuccess"></wei-upload>
            </el-dialog>
        </template>
    </div>
</template>

<script>
    export default {
        name: "Index",
        components: {
            'wei-tabs': () => import('./Tabs.vue'),
            'edit-form': () => import('./EditForm.vue'),
            'wei-upload': () => import('@/components/upload/Index.vue')
        },
        props: {
            menuCollapse: {
                type: Boolean,
                default: false
            }
        },
        data() {
            const userInfo = this.$globalFun.getSessionStorage('userInfo');
            return {
                userInfo,
                dialogEditForm: false,
                fullscreen: false,
                dropdownItems: [
                    {title: '修改密码', command: 'updatePassword'},
                    {title: '安全退出', command: 'logout'}
                ],
                dialogEditIcon: false,
                tabsMaxWidth: 0,
                //头像
                fileList: []
            }
        },
        watch: {
            menuCollapse() {
                this.initTabsWidth();
            }
        },
        mounted() {
            this.initTabsWidth();
        },
        methods: {
            initTabsWidth() {
                //初始化tabs的最大宽度
                let that = this;
                this.$nextTick(() => {
                    let headerWidth = that.$refs['header'].getBoundingClientRect().width;
                    let collapseWidth = that.$refs['collapse'].getBoundingClientRect().width;
                    let rightWidth = that.$refs['right'].getBoundingClientRect().width;
                    that.tabsMaxWidth = headerWidth - collapseWidth - rightWidth - 30;
                });
            },
            handleCommand(command) {
                switch (command) {
                    case 'logout': {
                        this.logout();
                    }
                        break;
                    case 'updatePassword': {
                        this.updatePassword();
                    }
                        break;
                    default: {

                    }
                }
            },
            logout() {
                const that = this;
                this.$axios({
                    url: that.$global.URL.logout,
                    method: 'post',
                    success() {
                        that.$globalFun.successMsg('注销成功，即将返回登录页面');
                        sessionStorage.setItem('loginStatus', 'logout');
                        let timer = setTimeout(() => {
                            clearTimeout(timer);
                            that.$router.replace('/login');
                        }, 3000);
                    }
                });
            },
            updatePassword() {
                let that = this;
                this.$globalFun.messageBox({
                    message: '确定修改密码!!!',
                    confirm() {
                        that.dialogEditForm = true;
                    }
                });
            },
            updateIcon() {
                let that = this;
                this.$globalFun.messageBox({
                    message: '确定修改头像!!!',
                    confirm() {
                        that.dialogEditIcon = true;
                    }
                });
            },
            iconSuccess(list) {
                let src = list[0]['response']['data'];
                let userInfo = this.$globalFun.getSessionStorage('userInfo');
                userInfo['icon'] = src;
                this.userInfo = userInfo;
                this.$globalFun.setSessionStorage('userInfo', userInfo);
                this.dialogEditIcon = false;
            },
            changeSize() {
                let element = document.documentElement;
                // 判断是否已经是全屏
                // 如果是全屏，退出
                if (this.fullscreen) {
                    if (document.exitFullscreen) {
                        document.exitFullscreen();
                    } else if (document.webkitCancelFullScreen) {
                        document.webkitCancelFullScreen();
                    } else if (document.mozCancelFullScreen) {
                        document.mozCancelFullScreen();
                    } else if (document.msExitFullscreen) {
                        document.msExitFullscreen();
                    }
                } else {    // 否则，进入全屏
                    if (element.requestFullscreen) {
                        element.requestFullscreen();
                    } else if (element.webkitRequestFullScreen) {
                        element.webkitRequestFullScreen();
                    } else if (element.mozRequestFullScreen) {
                        element.mozRequestFullScreen();
                    } else if (element.msRequestFullscreen) {
                        // IE11
                        element.msRequestFullscreen();
                    }
                }
                // 改变当前全屏状态
                this.fullscreen = !this.fullscreen;
            }
        }
    }
</script>

<style lang="scss" scoped>

    $height: 44px;

    #rightNav {
        .header {
            overflow: hidden;
            border-bottom: 1px solid #e2e2e2;
            height: $height;
            line-height: $height;
            color: #954ae0;

            .collapse {
                float: left;
                width: 50px;
                font-size: 37px;
                text-align: center;
            }

            .tabs {
                float: left;
            }

            .right {
                float: right;
                display: flex;
                height: $height;
                line-height: $height;

                .more {
                    display: flex;
                    align-items: center;
                    justify-content: center;
                }

                .user {
                    margin: auto 5px;
                }
            }
        }
    }
</style>