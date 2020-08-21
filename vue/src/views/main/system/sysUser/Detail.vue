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
                    {label: '角色', prop: data['roleList']},
                    {label: '手机号', prop: data['phone']},
                    {
                        label: '状态', type: 'switch',
                        element() {
                            return {
                                value: 1 === data['status']
                            }
                        }
                    },
                    {
                        label: '头像', type: 'avatar', element() {
                            return {
                                src: data['iconAllPath']
                            }
                        }
                    },
                    {label: '用户最后活跃ip地址', prop: data['laseIpAddress']},
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