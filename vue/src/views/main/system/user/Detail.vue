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
                    {label: '用户名', prop: data['username']},
                    {label: '真实姓名', prop: data['realName']},
                    {
                        label: '状态', type: 'tag',
                        element() {
                            let result = [
                                null,
                                {content: '正常', type: 'success'},
                                {content: '禁用', type: 'warning'},
                                {content: '封号', type: 'danger'},
                            ];
                            return result[data['status']] || {
                                content: `未知状态 ${data['status']}`,
                                type: 'danger'
                            }
                        }
                    },
                    {label: '用户最后活跃ip地址', prop: data['lastIpAddress']},
                    {
                        label: '用户最后活跃时间', prop: 'lastActiveTime', type: 'icon', element() {
                            return {
                                leftIcon: 'el-icon-time',
                                content: data['lastActiveTime']
                            };
                        }
                    },
                    {
                        label: '用户创建时间', prop: 'createTime', type: 'icon', element() {
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