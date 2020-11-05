<template>
    <template v-if="'SearchOutlined' === icon">
        <SearchOutlined/>
    </template>
    <template v-else-if="'PlusOutlined' === icon">
        <PlusOutlined/>
    </template>
    <template v-else-if="'DeleteOutlined' === icon">
        <DeleteOutlined/>
    </template>
    <template v-else-if="'EditOutlined' === icon">
        <EditOutlined/>
    </template>
    <template v-else-if="'VerticalAlignBottomOutlined' === icon">
        <VerticalAlignBottomOutlined/>
    </template>
    <template v-else-if="'HomeOutlined' === icon">
        <HomeOutlined/>
    </template>
    <template v-else-if="'UserOutlined' === icon">
        <UserOutlined/>
    </template>
    <template v-else-if="'RedoOutlined' === icon">
        <RedoOutlined/>
    </template>
    <template v-else-if="'InfoCircleOutlined' === icon">
        <InfoCircleOutlined/>
    </template>
    <template v-else-if="'UndoOutlined' === icon">
        <UndoOutlined />
    </template>
    <template v-else>
        <!--没找到展示的默认icon-->
        <template v-if="null != defaultIcon && '' !== defaultIcon">
            <wei-icon :icon="defaultIcon"></wei-icon>
        </template>
        <template v-else>
            <template v-if="showTip">
                <span style="color: #ff4400;">icon不存在，请在 @/components/icon/Index.vue 组件中自行引入/设置默认icon defaultIcon ,如需忽略此提示，请设置 :showTip="false"</span>
            </template>
        </template>
    </template>
</template>
<!--方便使用ant的图标-->
<script>
    import {
        SearchOutlined,
        PlusOutlined,
        DeleteOutlined,
        EditOutlined,
        VerticalAlignBottomOutlined,
        HomeOutlined,
        UserOutlined,
        RedoOutlined,
        InfoCircleOutlined,
        UndoOutlined,
    } from '@ant-design/icons-vue';
    import {defineAsyncComponent} from 'vue';

    export default {
        name: "WeiIcon",
        props: {
            //图标
            icon: {
                type: String
            },
            //默认图标
            defaultIcon: {
                type: String
            },
            //展示icon没找到的提示
            showTip: {
                type: Boolean,
                default: true,
            }
        },
        components: {
            'wei-icon': defineAsyncComponent(() => import('@/components/icon/Index.vue')),
            [SearchOutlined.name]: SearchOutlined,
            [PlusOutlined.name]: PlusOutlined,
            [DeleteOutlined.name]: DeleteOutlined,
            [EditOutlined.name]: EditOutlined,
            [VerticalAlignBottomOutlined.name]: VerticalAlignBottomOutlined,
            [HomeOutlined.name]: HomeOutlined,
            [UserOutlined.name]: UserOutlined,
            [RedoOutlined.name]: RedoOutlined,
            [InfoCircleOutlined.name]: InfoCircleOutlined,
            [UndoOutlined.name]: UndoOutlined,
        }
    }
</script>