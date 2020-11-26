<template>
    <!--富文本输入框,必须绑定 html ,具体属性参考 /src/components/wangEditor/Index.vue-->
    <wang-editor v-model:html="html"></wang-editor>
    <a-button @click="handleClick">展示输入框内容</a-button>
</template>

<script>
    import {ref, defineAsyncComponent} from 'vue';
    import {Button} from 'ant-design-vue';
    import $ant from '@/utils/ant';

    export default {
        name: "Index",
        components: {
            [Button.name]: Button,
            'wang-editor': defineAsyncComponent(() => import('@/components/wangEditor/Index.vue'))
        },
        setup() {
            let html = ref(`<h1 style="color: #666;">hello wangEditor</h1>`);
            let handleClick = () => {
                $ant.alert({
                    content: html.value
                });
            }
            return {
                html,
                handleClick
            }
        }
    }
</script>