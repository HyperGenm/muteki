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
    <template v-else-if="'InfoCircleOutlined' === icon">
        <InfoCircleOutlined/>
    </template>
    <template v-else-if="'UndoOutlined' === icon">
        <UndoOutlined/>
    </template>
    <template v-else-if="'RedoOutlined' === icon">
        <RedoOutlined/>
    </template>
    <template v-else-if="'MenuFoldOutlined' === icon">
        <MenuFoldOutlined/>
    </template>
    <template v-else-if="'MenuUnfoldOutlined' === icon">
        <MenuUnfoldOutlined/>
    </template>
    <template v-else-if="'BellOutlined' === icon">
        <BellOutlined/>
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
    import SearchOutlined from '@ant-design/icons-vue/SearchOutlined';
    import PlusOutlined from '@ant-design/icons-vue/PlusOutlined';
    import DeleteOutlined from '@ant-design/icons-vue/DeleteOutlined';
    import EditOutlined from '@ant-design/icons-vue/EditOutlined';
    import VerticalAlignBottomOutlined from '@ant-design/icons-vue/VerticalAlignBottomOutlined';
    import HomeOutlined from '@ant-design/icons-vue/HomeOutlined';
    import UserOutlined from '@ant-design/icons-vue/UserOutlined';
    import InfoCircleOutlined from '@ant-design/icons-vue/InfoCircleOutlined';
    import UndoOutlined from '@ant-design/icons-vue/UndoOutlined';
    import RedoOutlined from '@ant-design/icons-vue/RedoOutlined';
    import MenuFoldOutlined from '@ant-design/icons-vue/MenuFoldOutlined';
    import MenuUnfoldOutlined from '@ant-design/icons-vue/MenuUnfoldOutlined';
    import BellOutlined from '@ant-design/icons-vue/BellOutlined';
    import WeiIcon from '@/components/icon/Index.vue';

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
            'wei-icon': WeiIcon,
            SearchOutlined,
            PlusOutlined,
            DeleteOutlined,
            EditOutlined,
            VerticalAlignBottomOutlined,
            HomeOutlined,
            UserOutlined,
            InfoCircleOutlined,
            UndoOutlined,
            RedoOutlined,
            MenuFoldOutlined,
            MenuUnfoldOutlined,
            BellOutlined,
        }
    }
</script>