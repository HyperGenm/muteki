<template>
    <div class="treeMenu">
        <template v-for="item in data.children">
            <template v-if="null == item.children || 0 >= item.children.length">
                <!--如果是外部超链接-->
                <el-menu-item :index="`/${parentPath}/${2 === item['externalFlag'] ? item.name : item.path}`">
                    <el-tooltip :content="item.title" placement="right">
                        <div>
                            <i :class="item['icon'] || 'el-icon-s-help'"></i>
                            <span>{{item.title}}</span>
                        </div>
                    </el-tooltip>
                </el-menu-item>
            </template>
            <template v-else>
                <el-submenu :index="`/${parentPath}/${item.path}`">
                    <template slot="title">
                        <el-tooltip :content="item.title" placement="right">
                            <div>
                                <i :class="item['icon'] || 'el-icon-s-help'"></i>
                                <span>{{item.title}}</span>
                            </div>
                        </el-tooltip>
                    </template>
                    <tree-menu :data="item" :parentPath="`${parentPath}/${item.path}`"></tree-menu>
                </el-submenu>
            </template>
        </template>
    </div>
</template>

<script>
    export default {
        name: "treeMenu",
        props: {
            data: {
                type: Object,
                default: () => {
                }
            },
            parentPath: {
                type: String,
                default: () => {

                }
            }
        }
    }
</script>