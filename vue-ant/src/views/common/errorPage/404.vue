<template>
    <div style="width: 87%;margin: 0 auto;">
<!--        <img src="src/assets/404.png" alt="404">-->
        404
    </div>
</template>

<script>
    import $ant from '@/utils/ant';

    export default {
        name: "ErrorPage404",
        mounted() {
            $ant.errorMsg('请创建路径对应组件');
        }
    }
</script>