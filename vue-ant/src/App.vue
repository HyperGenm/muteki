<template>
    <a-config-provider :locale="locale.value">
        <router-view/>
    </a-config-provider>
</template>

<script>
    //默认中文显示
    import {ConfigProvider} from 'ant-design-vue';
    import zhCN from 'ant-design-vue/es/locale-provider/zh_CN';
    import enUS from 'ant-design-vue/es/locale-provider/en_US';
    import {provide, reactive} from 'vue';

    export default {
        name: "App",
        components: {
            [ConfigProvider.name]: ConfigProvider
        },
        setup() {
            //默认语言中文，标题显示要切换为英文
            let locale = reactive({
                title: 'English',
                value: zhCN
            });
            provide('locale', locale);
            provide('localeChange', () => {
                if ('English' === locale.title) {
                    locale.value = enUS;
                    locale.title = '中文';
                } else {
                    locale.value = zhCN;
                    locale.title = 'English';
                }
            });
            return {
                locale
            }
        }
    }
</script>

<style lang="less">
    html body {
        margin: 0;
        padding: 0;
        background-color: #f0f2f5;
    }

</style>