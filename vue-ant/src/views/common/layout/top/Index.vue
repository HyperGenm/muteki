<template>
    <div id="top" :ref="refs.top">
        <div class="left-icon" :ref="refs.leftIcon"
             @click="leftIconChange">
            <template v-if="menuInlineCollapsed">
                <MenuFoldOutlined/>
            </template>
            <template v-else>
                <MenuUnfoldOutlined/>
            </template>
        </div>
        <div class="tags" :style="`width:${tagsWidth}px`">
            <a-tabs size="large"
                    v-model:activeKey="tabActiveKey"
                    @change="tabChange">
                <a-tab-pane v-for="item in tabList"
                            :key="item.path" :tab="item.title"/>
            </a-tabs>
        </div>
        <div class="right" :ref="refs.right">
            <div class="locale"
                 @click="locale.change">
                <span>{{locale.title}}</span>
            </div>
            <div class="badge">
                <a-badge count="5">
                    <BellOutlined/>
                </a-badge>
            </div>
            <div class="icon">
                <a-avatar :src="userInfo['avatarSrc']">
                    <template v-slot:icon>
                        <BellOutlined/>
                    </template>
                </a-avatar>
            </div>
            <div class="user">
                <a-dropdown>
                    <span>{{userInfo['realName']}}</span>
                    <template v-slot:overlay>
                        <a-menu>
                            <a-menu-item>
                                <a href="javascript:;">1st menu item</a>
                            </a-menu-item>
                            <a-menu-item>
                                <a href="javascript:;">2nd menu item</a>
                            </a-menu-item>
                            <a-menu-item>
                                <a href="javascript:;">3rd menu item</a>
                            </a-menu-item>
                        </a-menu>
                    </template>
                </a-dropdown>
            </div>
        </div>
    </div>
</template>

<script>
    import {MenuFoldOutlined, MenuUnfoldOutlined, BellOutlined, UserOutlined} from '@ant-design/icons-vue';
    import {Tabs, Badge, Avatar, Dropdown, Menu} from 'ant-design-vue';
    import {ref, reactive, nextTick, onMounted, watch, computed, inject} from 'vue';
    import {useRoute, useRouter} from 'vue-router';
    import $function from '@/utils/function';

    export default {
        name: "Top",
        components: {
            [MenuFoldOutlined.name]: MenuFoldOutlined,
            [MenuUnfoldOutlined.name]: MenuUnfoldOutlined,
            [Tabs.name]: Tabs,
            [Tabs.TabPane.name]: Tabs.TabPane,
            [Badge.name]: Badge,
            [BellOutlined.name]: BellOutlined,
            [Avatar.name]: Avatar,
            [UserOutlined.name]: UserOutlined,
            [Dropdown.name]: Dropdown,
            [Menu.name]: Menu,
            [Menu.Item.name]: Menu.Item,
        },
        props: {
            //菜单是否折叠
            menuInlineCollapsed: {
                type: Boolean
            }
        },
        setup(props, {emit}) {
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
            let $refs = {};
            let refs = reactive({
                top: (ref) => {
                    $refs.top = ref;
                },
                leftIcon: (ref) => {
                    $refs.leftIcon = ref;
                },
                right: (ref) => {
                    $refs.right = ref;
                }
            });
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
                    let {top, leftIcon, right} = $refs;
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
            watch($route, (to) => {
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
            });
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
            return {
                leftIconChange,
                refs,
                tagsWidth,
                userInfo,
                tabActiveKey,
                tabList,
                tabChange,
                locale
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