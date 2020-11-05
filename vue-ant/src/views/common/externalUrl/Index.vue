<template>
    <iframe id="iframe" :src="src" style="width:100%;height: 100%;"></iframe>
</template>
<!--主要用于嵌入外部页面-->
<script>
    export default {
        name: "ExternalUrl",
        computed: {
            src() {
                let {externalUrl} = this.$route.meta;
                return `${externalUrl}?__t=${new Date().getTime()}`;
            }
        }
    }
</script>