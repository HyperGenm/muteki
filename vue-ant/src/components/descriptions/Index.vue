<template>
    <a-descriptions :title="title" :bordered="bordered"
                    :column="column" :size="size">
        <slot name="startSlot"></slot>
        <a-descriptions-item v-for="item in itemList" :key="item.label"
                             :label="item.label"
                             :span="item.span">
            <template v-if="'avatar' === item.type">
                <a-avatar :src="item.element()['src']"
                          :style="item.element()['style']"
                          :shape="item.element()['shape']"
                          :size="item.element()['size'] || 'large'"
                          :alt="item.element()['alt']">
                    {{item.element()['content']}}
                </a-avatar>
            </template>
            <template v-else-if="'tag' === item.type">
                <a-tag :color="item.element()['color']">
                    {{item.element()['content']}}
                </a-tag>
            </template>
            <template v-else-if="'icon' === item.type">
                <wei-icon :icon="item.element()['icon']"
                          :defaultIcon="item.element()['defaultIcon']"></wei-icon>
            </template>
            <template v-else>
                <div v-html="item.content"></div>
            </template>
        </a-descriptions-item>
        <slot name="endSlot"></slot>
    </a-descriptions>
</template>

<script>
    import {Avatar, Descriptions, Tag} from 'ant-design-vue';
    import {defineAsyncComponent} from 'vue';

    export default {
        name: "WeiDescriptions",
        components: {
            [Avatar.name]: Avatar,
            [Descriptions.name]: Descriptions,
            [Descriptions.Item.name]: Descriptions.Item,
            [Tag.name]: Tag,
            'wei-icon': defineAsyncComponent(() => import('@/components/icon/Index.vue')),
        },
        props: {
            //展示的item列表
            itemList: {
                type: Array,
                default: () => ([])
            },
            //	描述列表的标题，显示在最顶部
            title: {
                type: String,
                default: '标题'
            },
            //是否展示边框
            bordered: {
                type: Boolean,
                default: true
            },
            //一行的 DescriptionItems 数量，可以写成像素值或支持响应式的对象写法 { xs: 8, sm: 16, md: 24}
            column: {
                default: 1
            },
            //设置列表的大小。可以设置为 middle 、small, 或不填（只有设置 bordered={true} 生效）
            size: {
                type: String
            }
        },
        mounted() {
            //对外抛出自身
            this.$emit('my-ref', this);
        },
    }
</script>