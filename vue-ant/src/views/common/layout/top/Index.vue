<template>
    <div id="top" ref="top">
        <div class="left-icon" ref="leftIcon"
             @click="leftIconChange">
            <template v-if="menuInlineCollapsed">
                <wei-icon icon="MenuFoldOutlined"></wei-icon>
            </template>
            <template v-else>
                <wei-icon icon="MenuUnfoldOutlined"></wei-icon>
            </template>
        </div>
        <!--<div class="tags" :style="`width:${tagsWidth}px`">
            <a-tabs size="large"
                    v-model:activeKey="tabActiveKey"
                    @change="tabChange">
                <a-tab-pane v-for="item in tabList"
                            :key="item.path" :tab="item.title"/>
            </a-tabs>
        </div>-->
        <div class="right" ref="right">
            <div class="locale"
                 @click="locale.change">
                <span>{{locale.title}}</span>
            </div>
            <div class="badge">
                <a-badge count="5">
                    <wei-icon icon="BellOutlined"></wei-icon>
                </a-badge>
            </div>
            <div class="icon">
                <a-avatar :src="userInfo['avatarSrc']">
                    <template v-slot:icon>
                        <wei-icon icon="UserOutlined"></wei-icon>
                    </template>
                </a-avatar>
            </div>
            <div class="user">
                <a-dropdown>
                    <span>{{userInfo['realName']}}</span>
                    <template v-slot:overlay>
                        <a-menu>
                            <template v-for="(menu,index) in menuList" :key="index">
                                <a-menu-item @click="menu.click">
                                    <div>{{menu.title}}</div>
                                </a-menu-item>
                            </template>
                        </a-menu>
                    </template>
                </a-dropdown>
            </div>
        </div>
        <template>
            <a-drawer width="67%"
                      title="修改头像"
                      v-model:visible="updateIcon.visible">
                <my-icon :action="updateIcon.action"
                         :fileList="updateIcon.fileList"
                         @success="updateIcon.success"></my-icon>
            </a-drawer>
        </template>
        <template>
            <a-drawer width="67%"
                      title="修改密码"
                      v-model:visible="updatePwd.visible">
                <my-form :form="updatePwd.form"
                         :itemList="updatePwd.itemList"
                         :rules="updatePwd.rules"
                         @onSubmit="updatePwd.onSubmit"></my-form>
            </a-drawer>
        </template>
    </div>
</template>

<script>
    import {Tabs, Badge, Avatar, Dropdown, Menu, Drawer} from 'ant-design-vue';
    import {ref, reactive, nextTick, onMounted, inject, getCurrentInstance, defineAsyncComponent} from 'vue';
    import {useRoute, useRouter} from 'vue-router';
    import $function from '@/utils/function';
    import $ant from '@/utils/ant';
    import $global from '@/utils/global';
    import $cryptoJS from '@/utils/cryptoJS';
    import WeiIcon from '@/components/icon/Index';
    import $axios from "../../../../utils/axios";

    export default {
        name: "Top",
        components: {
            'wei-icon': WeiIcon,
            [Tabs.name]: Tabs,
            [Tabs.TabPane.name]: Tabs.TabPane,
            [Badge.name]: Badge,
            [Avatar.name]: Avatar,
            [Dropdown.name]: Dropdown,
            [Menu.name]: Menu,
            [Menu.Item.name]: Menu.Item,
            [Drawer.name]: Drawer,
            'my-form': defineAsyncComponent(() => import('@/components/form/Index.vue')),
            'my-icon': defineAsyncComponent(() => import('@/components/upload/Index.vue')),
        },
        props: {
            //菜单是否折叠
            menuInlineCollapsed: {
                type: Boolean
            }
        },
        setup(props, {emit}) {
            //获取当前实例
            let instance = getCurrentInstance();
            let $route = useRoute();
            let $router = useRouter();
            //左边图标点击,收缩/展开侧边栏
            let leftIconChange = () => {
                emit('changeMenuInlineCollapsed');
            };
            //当前活跃的tab
            let tabActiveKey = ref(0);
            //tab列表
            let tabList = ref([]);
            //tab改变
            const tabChange = (activeKey) => {
                $router.push(activeKey);
            };
            //用户信息
            let userInfo = reactive({});
            //tags的宽度
            let tagsWidth = ref(300);
            onMounted(() => {
                //默认当前页面加入tab列表
                tabList.value.push({
                    title: $route['meta']['title'],
                    name: $route['name'],
                    path: $route['path'],
                });
                tabActiveKey.value = $route['path'];
                //计算tags的宽度
                nextTick(() => {
                    let {top, leftIcon, right} = instance.refs;
                    let topWidth = top.getBoundingClientRect().width;
                    let leftIconWidth = leftIcon.getBoundingClientRect().width;
                    let rightWidth = right.getBoundingClientRect().width;
                    tagsWidth.value = topWidth - leftIconWidth - rightWidth - 20;
                });
                let user = $function.getLocationStorage('user') || {};
                userInfo['realName'] = user['realName'];
                userInfo['avatarSrc'] = user['icon'];
            });
            //监听路由变化
            /*watch($route, (to) => {
                if ('/' === to.path) {
                    return;
                }
                if ('/404' === to.path) {
                    return;
                }
                for (let i = 0; i < tabList.value.length; i++) {
                    if (to['path'] === tabList.value[i]['path']) {
                        //设置当前选中的
                        tabActiveKey.value = to['path'];
                        return;
                    }
                }
                tabList.value.push({
                    title: to['meta']['title'],
                    name: to['name'],
                    path: to['path'],
                });
                tabActiveKey.value = to['path'];
            });*/
            //引入App.vue设置的国际化
            let localeInject = inject('locale');
            let localeChange = inject('localeChange');
            //语言改变
            let locale = reactive({
                title: localeInject['title'],
                change() {
                    localeChange();
                    //显示要切换为的语言
                    locale.title = localeInject['title'];
                }
            });
            let menuList = ref([
                {
                    title: '修改头像',
                    click() {
                        updateIcon.visible = true;
                    }
                },
                {
                    title: '修改密码',
                    click() {
                        $ant.confirm({
                            content: '确定要修改密码',
                            onOk() {
                                updatePwd.visible = true;
                            }
                        });
                    }
                },
                {
                    title: '安全退出',
                    click() {
                        $ant.confirm({
                            content: '确定退出',
                            onOk() {
                                $axios({
                                    url: $global.url.logout,
                                    method: 'post',
                                    success() {
                                        $ant.successMsg('注销成功，即将返回登录页面');
                                        setTimeout(() => {
                                            $router.replace('/login');
                                        }, 3000);
                                    }
                                })
                            }
                        });
                    }
                }
            ]);
            //修改头像
            let updateIcon = reactive({
                visible: false,
                action: $global.url.system.sysUser.updateIcon,
                fileList: [],
                success(file, fileList) {
                    let {data} = file['response'];
                    userInfo['avatarSrc'] = data;
                    let user = $function.getLocationStorage('user');
                    user['icon'] = data;
                    $function.setLocationStorage('user', user);
                    $ant.successMsg('修改成功');
                    updateIcon.visible = false;
                }
            });
            //修改密码
            let updatePwd = reactive({
                visible: false,
                form: {},
                itemList: [
                    {type: 'input', label: '旧密码', prop: 'oldPwd', inputType: 'password', required: true},
                    {type: 'input', label: '新密码', prop: 'newPwd', inputType: 'password', required: true},
                    {type: 'input', label: '确认密码', prop: 'againPwd', inputType: 'password', required: true},
                ],
                rules: {},
                onSubmit(form) {
                    //如果密码不一样
                    if (form['newPwd'] !== form['againPwd']) {
                        $ant.errorMsg('两次密码不一致');
                        return;
                    }
                    $axios({
                        url: $global.url.system.sysUser.updatePwd,
                        method: 'post',
                        data: {
                            oldPwd: $cryptoJS.md5(form['oldPwd']),
                            newPwd: $cryptoJS.md5(form['newPwd']),
                        },
                        success() {
                            $ant.successMsg('密码修改成功');
                            updatePwd.visible = false;
                        }
                    })
                }
            });
            return {
                leftIconChange,
                tagsWidth,
                userInfo,
                tabActiveKey,
                tabList,
                tabChange,
                locale,
                menuList,
                updateIcon,
                updatePwd
            }
        }
    }
</script>

<style lang="less" scoped>
    #top {
        height: 64px;
        line-height: 64px;
        background-color: #fff;
        display: flex;
        align-items: center;
        position: relative;

        .left-icon {
            width: 60px;
            text-align: center;

            ::v-deep(span) {
                & > svg {
                    font-size: 26px;
                }
            }

            &:hover {
                cursor: pointer;
                background-color: #f0f2f5;
            }
        }

        .tags {
            ::v-deep(.ant-tabs-bar) {
                margin: 0 0 10px 0;
                border-bottom: 0;
            }
        }

        .right {
            position: absolute;
            right: 0;
            display: flex;
            align-items: center;

            & > div {
                display: flex;
                align-items: center;
            }

            .locale {
                padding: 0 10px;

                &:hover {
                    cursor: pointer;
                    background-color: #f0f2f5;
                }
            }

            .badge {
                padding: 0 20px;

                ::v-deep(span) {
                    & > svg {
                        font-size: 26px;
                    }
                }

                &:hover {
                    cursor: pointer;
                    background-color: #f0f2f5;
                }
            }

            .user {
                padding: 0 20px;

                &:hover {
                    cursor: pointer;
                    background-color: #f0f2f5;
                }
            }
        }
    }
</style>