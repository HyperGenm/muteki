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
                    {label: '标题', prop: data['title']},
                    {label: 'name', prop: data['name']},
                    {
                        label: '图标', prop: 'icon', formatter() {
                            return `<i class="${data.icon}"></i>`;
                        }
                    },
                    {label: '路径', prop: data['path']},
                    {
                        label: '类型', type: 'tag',
                        element() {
                            let result = [
                                null,
                                {content: '菜单'},
                                {content: '按钮', type: 'success'},
                            ];
                            return result[data['type']] || {
                                content: '未知类型',
                                type: 'danger'
                            }
                        }
                    },
                    {
                        label: '专属', type: 'tag',
                        element() {
                            let result = [
                                null,
                                {content: '普通', type: 'info'},
                                {content: 'vip', type: 'success'},
                            ];
                            return result[data['superFlag']] || {
                                content: `未知类型,superFlag:${data['superFlag']}`,
                                type: 'danger'
                            }
                        }
                    },
                    {label: '排序', prop: data['sort']},
                    {
                        label: '拥有api', prop: data['containApi'], formatter() {
                            if (null == data['containApi']) {
                                return;
                            }
                            let dom = '';
                            JSON.parse(data['containApi']).forEach((value, index) => {
                                dom += `<div style="border-top: ${0 === index ? 0 : 1}px dashed #777;">${value}</div>`;
                            });
                            return dom;
                        }
                    },
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