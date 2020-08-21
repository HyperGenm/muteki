<template>
    <div id="index">
        <wei-detail :rows="rows"></wei-detail>
    </div>
</template>

<script>
    export default {
        name: "Detail",
        components: {
            'wei-detail': () => import('@/components/detail/Index')
        },
        props: {
            formData: {
                type: Object
            }
        },
        data() {
            return {
                rows: []
            }
        },
        watch: {
            formData: {
                immediate: true,
                handler() {
                    this.initData();
                }
            }
        },
        methods: {
            initData() {
                let {formData: data} = this;
                this.rows = [
                    {label: '类名', prop: data['className']},
                    {label: '方法名', prop: data['methodName']},
                    {label: '行数', prop: data['lineNumber']},
                    {label: '详情', prop: data['content']},
                    {label: '备注', prop: data['remark']},
                    {
                        label: '创建时间',
                        prop: 'createTime',
                        type: 'icon',
                        element() {
                            return {
                                leftIcon: 'el-icon-time',
                                content: data['createTime']
                            };
                        }
                    }
                ];
            }
        }
    }
</script>