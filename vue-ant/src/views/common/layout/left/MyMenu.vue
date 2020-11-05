<template>
    <template v-if="null == menu.children || 0 >= menu.children">
        <a-menu-item :key="parentPath + menu.path">
            <wei-icon :icon="menu.icon" defaultIcon="InfoCircleOutlined"></wei-icon>
            <span>{{menu.title}}</span>
        </a-menu-item>
    </template>
    <template v-else>
        <a-sub-menu :key="menu.path">
            <template v-slot:title>
                <wei-icon :icon="menu.icon" defaultIcon="InfoCircleOutlined"></wei-icon>
                <span>{{menu.title}}</span>
            </template>
            <template v-for="m in menu.children" :key="m.name">
                <my-menu :menu="m" :parentPath="parentPath + menu.path"></my-menu>
            </template>
        </a-sub-menu>
    </template>
</template>

<script>
    import {Menu} from 'ant-design-vue';
    import {defineAsyncComponent} from 'vue';

    export default {
        name: "MyMenu",
        components: {
            [Menu.name]: Menu,
            [Menu.Item.name]: Menu.Item,
            [Menu.SubMenu.name]: Menu.SubMenu,
            'wei-icon': defineAsyncComponent(() => import('@/components/icon/Index.vue')),
        },
        props: {
            //菜单
            menu: {
                type: Object,
                default: () => {
                }
            },
            //上级菜单叠加路径
            parentPath: {
                type: String,
                default: ''
            }
        }
    }
</script>