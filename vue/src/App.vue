<template>
    <div id="app">
        <router-view/>
    </div>
</template>

<style lang="scss">

    html, body {
        margin: 0;
        padding: 0;
        font-family: Avenir, Helvetica, Arial, sans-serif;
        -webkit-font-smoothing: antialiased;
        -moz-osx-font-smoothing: grayscale;
    }

    .wei-table {
        height: 100%;
    }

    /*指令加载中动画*/
    .el-loading-mask.is-fullscreen {
        background-color: #3b2d47 !important;
        opacity: 0.7;

        .el-loading-spinner {
            top: 0;
            right: 0;
            bottom: 0;
            left: 0;

            i {
                background: url("./assets/image/loading.gif") no-repeat;
                background-size: 100% 100%;
                position: absolute;
                left: 0;
                top: 0;
                width: 100%;
                height: 100%;

                &:before {
                    content: none;
                }
            }

            p {
                position: absolute;
                top: 72%;
                width: 100%;
                font-size: 17px;
            }
        }
    }

</style>