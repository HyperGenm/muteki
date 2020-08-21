<template>
    <div id="layout">
        <!--左上角，可以放logo-->
        <div class="leftNav" :style="`width:${leftWidth}px`">
            <div style="text-align: center;margin-top: 7px;">
                <div v-if="menuCollapse">
                    <img style="width: 80%;" src="../../../assets/logo_small.png">
                </div>
                <div v-else>
                    <img style="width: 80%;" src="../../../assets/logo.svg">
                </div>
            </div>
            <left-nav :menuCollapse="menuCollapse"></left-nav>
        </div>
        <div class="rightNav" :style="`margin-left:${leftWidth}px`">
            <right-nav :menuCollapse="menuCollapse" @collapseChange="collapseChange"></right-nav>
            <div id="mainApp">
                <router-view/>
                <!--返回顶部-->
                <div class="my-backtop">
                    <el-backtop target="#mainApp" :visibility-height="70"></el-backtop>
                </div>
            </div>
            <div class="footer">
                <span>Copyright ©{{nowYear}}</span>
                <el-link class="link" type="primary" target="_blank"
                         href="https://gitee.com/WeiziPlus/muteki">
                    WeiziPlus-Gitee
                </el-link>
            </div>
        </div>
    </div>
</template>

<script>
    export default {
        name: "Index",
        components: {
            'left-nav': () => import('./leftNav/Index'),
            'right-nav': () => import('./rightNav/Index')
        },
        data() {
            return {
                menuCollapse: false,
                nowYear: 2020
            }
        },
        computed: {
            leftWidth() {
                return this.menuCollapse ? 50 : 200;
            }
        },
        mounted() {
            if (666 > window.screen.width) {
                this.menuCollapse = true;
            }
            let date = new Date();
            this.nowYear = date.getFullYear();
        },
        methods: {
            collapseChange() {
                this.menuCollapse = !this.menuCollapse;
            }
        }
    }
</script>

<style lang="scss" scoped>

    #layout {
        overflow: hidden;

        .leftNav {
            position: fixed;
            top: 0;
            bottom: 0;
            left: 0;
            height: 100%;
            overflow: hidden;
            background-color: #545c64;

            h2 {
                text-align: center;
            }
        }

        .rightNav {
            height: 100vh;

            #mainApp {
                clear: both;
                overflow-y: scroll;
                height: calc(100vh - 88px);
                -webkit-box-sizing: border-box;
                -moz-box-sizing: border-box;
                box-sizing: border-box;
                position: relative;

                & > div {
                    position: absolute;
                    top: 1%;
                    right: 1%;
                    bottom: 1%;
                    left: 1%;
                }

                .my-backtop {
                    width: 0;
                    height: 0;

                    .el-backtop {
                        position: fixed;
                        border: 1px solid;
                        margin-bottom: 30px;
                    }
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