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
                    {
                        label: '终端', type: 'tag',
                        element() {
                            let {terminal} = data;
                            let result = {
                                "网页": 'success'
                            };
                            return {
                                content: terminal,
                                type: result[terminal] || 'warning'
                            }
                        }
                    },
                    {label: '省份', prop: data['loginProvince']},
                    {label: '城市', prop: data['loginCity']},
                    {label: 'ip', prop: data['ipAddress']},
                    {
                        label: '结果', type: 'tag',
                        element() {
                            let result = {
                                200: {content: '成功', type: 'success'}
                            };
                            return result[data['resultCode']] || {
                                content: '失败',
                                type: 'warning'
                            }
                        }
                    },
                    {
                        label: '状态码', type: 'tag',
                        element() {
                            let result = {
                                200: {content: '200', type: 'success'}
                            };
                            return result[data['resultCode']] || {
                                content: data['resultCode'],
                                type: 'warning'
                            }
                        }
                    },
                    {label: '响应信息', prop: data['resultMsg']},
                    {label: '浏览器', prop: data['borderName']},
                    {label: '操作系统', prop: data['osName']},
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