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
    import {reactive, defineAsyncComponent, defineComponent} from 'vue';

    export default {
        name: "Layout",
        components: {
            'my-left': defineAsyncComponent(() => import('./left/Index.vue')),
            'my-top': defineAsyncComponent(() => import('./top/Index.vue')),
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
                height: 90vh;
                box-sizing: border-box;
                padding: 20px;
                overflow-y: scroll;

                & > div {
                    height: 100%;
                }
            }

            .footer {
                position: fixed;
                bottom: 0;
                left: 0;
                width: 100%;
                height: 37px;
                text-align: center;
                font-size: 12px;
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
