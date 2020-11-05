<template>
    <div id="wei-station">
        <router-view/>
    </div>
</template>

<!--仅做多级路由站位-->
<script>
    export default {
        name: "weiStation"
    }
</script>

<style lang="less" scoped>
    #wei-station {
        & > div {
            height: 100%;
        }
    }
</style>