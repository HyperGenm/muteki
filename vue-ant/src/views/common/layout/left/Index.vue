<template>
    <div id="left"
         :style="`width:${config.inlineCollapsed ? 80 : 256}px`">
        <div class="top">
            <h1 style="color: #ffffff;">WeiziPlus</h1>
        </div>
        <div class="menu"
             :style="`width:${config.inlineCollapsed ? 100 : 276}px`">
            <a-menu mode="inline"
                    :style="`width:${config.inlineCollapsed ? 80 : 256}px`"
                    :theme="config.theme"
                    :inlineCollapsed="config.inlineCollapsed"
                    @click="menuClick">
                <template v-for="menu in menuList" :key="`${2 === menu.externalFlag ? menu.name : menu.path}`">
                    <my-menu :menu="menu"></my-menu>
                </template>
            </a-menu>
        </div>
    </div>
</template>

<script>
    import {Menu} from 'ant-design-vue';
    import {ref, reactive, onMounted} from 'vue';
    import MyMenu from "./MyMenu"
    import {useRouter} from 'vue-router';
    import $function from '@/utils/function';

    export default {
        name: "Left",
        components: {
            [Menu.name]: Menu,
            MyMenu
        },
        props: {
            config: {
                type: Object,
                default: () => {

                }
            }
        },
        setup() {
            const $router = useRouter();
            //菜单列表
            let menuList = ref([]);
            //点击某一项item
            const menuClick = ({key, keyPath}) => {
                let path = '';
                keyPath.reverse().forEach(value => {
                    path += `/${value}`;
                });
                $router.push(path);
            };
            onMounted(() => {
                //渲染左边menu菜单
                menuList.value = $function.getLocationStorage('menuTree') || [];
            });
            return {
                menuList,
                menuClick
            }
        }
    }
</script>

<style lang="less" scoped>
    #left {
        background-color: #001529;
        height: 100vh;
        overflow: hidden;

        .top {
            height: 65px;
            display: flex;
            align-items: center;
            justify-content: center;
        }

        .menu {
            height: 87%;
            overflow-y: scroll;
            overflow-x: hidden;
        }
    }
</style>