<template>
    <div id="index">
        <my-left :config="leftConfig"></my-left>
        <div class="right">
            <my-top :menuInlineCollapsed="leftConfig.inlineCollapsed"
                    @changeMenuInlineCollapsed="leftConfig.inlineCollapsed = ! leftConfig.inlineCollapsed"></my-top>
            <div id="my-app">
                <router-view/>
            </div>
            <div class="footer">
                <span>Copyright ©{{nowYear}}</span>
                <a class="link" href="https://gitee.com/WeiziPlus/muteki">WeiziPlus-Gitee</a>
            </div>
        </div>
    </div>
</template>

<script>
    import MyLeft from './left/Index.vue';
    import MyTop from './top/Index.vue';
    import {ref, reactive} from 'vue';

    export default {
        name: "Layout",
        components: {
            MyLeft,
            MyTop,
        },
        setup() {
            //menu菜单配置
            let leftConfig = reactive({
                //菜单展开折叠
                inlineCollapsed: false,
                //主题
                theme: 'dark',
            });
            let nowYear = new Date().getFullYear();
            return {
                leftConfig,
                nowYear,
            }
        }
    }
</script>

<style lang="less" scoped>
    #index {
        display: flex;
        box-sizing: border-box;

        .right {
            flex: 1;

            #my-app {
                height: calc(100vh - 108px);
                box-sizing: border-box;
                padding: 20px;
                overflow-y: scroll;

                & > div {
                    height: 100%;
                }
            }

            .footer {
                margin-top: 7px;
                text-align: center;
                font-size: 12px;
                height: 37px;
                background-color: #e2e2e2;
                display: flex;
                align-items: center;
                justify-content: center;

                .link {
                    margin-left: 10px;
                }
            }
        }
    }
</style>