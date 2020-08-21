<template>
    <div id="leftNav" :style="`width:(${menuCollapse} ? 70 : 220)px`">
        <el-menu background-color="#545c64" text-color="#fff" active-text-color="#ffd04b"
                 router unique-opened
                 :default-active="$route.path" :collapse="menuCollapse">
            <div v-for="item in menuTree" :key="item.name">
                <template v-if="null == item.children || 0 >= item.children.length">
                    <!--如果是外部超链接-->
                    <el-menu-item :index="`/${2 === item['externalFlag'] ? item.name : item.path}`">
                        <i :class="item['icon'] || 'el-icon-info'"></i>
                        <span slot="title">{{item.title}}</span>
                    </el-menu-item>
                </template>
                <template v-else>
                    <el-submenu :index="`/${item.path}`">
                        <template slot="title">
                            <i :class="item['icon'] || 'el-icon-s-help'"></i>
                            <span>{{item.title}}</span>
                        </template>
                        <tree-menu :data="item" :parentPath="item.path"></tree-menu>
                    </el-submenu>
                </template>
            </div>
        </el-menu>
    </div>
</template>

<script>
    export default {
        name: "Index",
        components: {
            'tree-menu': () => import('./treeMenu.vue')
        },
        props: {
            menuCollapse: {
                type: Boolean,
                default: false
            }
        },
        data() {
            return {
                menuTree: []
            }
        },
        mounted() {
            this.menuTree = this.$globalFun.getSessionStorage('menuTree');
        }
    }
</script>

<style lang="scss" scoped>
    #leftNav {
        position: absolute;
        top: 61px;
        bottom: 30px;
        left: 0;
        overflow-y: scroll;
        overflow-x: hidden;

    }
</style>