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
                    {label: '真实姓名', prop: data['realName']},
                    {label: '请求路径', prop: data['url']},
                    {label: '请求方法名', prop: data['methodName']},
                    {
                        label: '参数', type: 'table',
                        element() {
                            let {param} = data;
                            let tableData = [];
                            let height = 90;
                            try {
                                let paramMap = JSON.parse(param);
                                for (let key in paramMap) {
                                    if (!paramMap.hasOwnProperty(key)) {
                                        return;
                                    }
                                    tableData.push({
                                        label: key,
                                        value: paramMap[key]
                                    });
                                }
                            } catch (e) {
                                tableData.push({
                                    label: '参数不是json对象',
                                    value: e
                                });
                            }
                            let length = tableData.length;
                            length = 5 < length ? 5 : length;
                            length = 1 > length ? 1 : length;
                            height += 42 * (length - 1);
                            return {
                                height,
                                tableData,
                                tableColumns: [
                                    {label: '参数名', prop: 'label'},
                                    {label: '值', prop: 'value'},
                                ]
                            }
                        }
                    },
                    {
                        label: '类型', type: 'tag',
                        element() {
                            let result = [
                                null,
                                {content: '查询'},
                                {content: '新增', type: 'success'},
                                {content: '修改', type: 'warning'},
                                {content: '删除', type: 'danger'},
                            ];
                            return result[data['type']] || {
                                content: `未知类型,type:${data['type']}`,
                                type: 'danger'
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
                    {label: '响应信息', prop: 'resultMsg',},
                    {
                        label: '请求耗时', type: 'tag',
                        element() {
                            let {timeConsuming} = data;
                            let type = '';
                            if (200 >= timeConsuming) {
                                type = 'success';
                            } else if (1000 >= timeConsuming) {
                                type = 'primary';
                            } else if (2000 >= timeConsuming) {
                                type = 'warning';
                            } else {
                                type = 'danger';
                            }
                            return {
                                content: `${timeConsuming}ms`,
                                type
                            }
                        }
                    },
                    {label: '操作', prop: data['description']},
                    {label: 'ip地址', prop: data['ipAddress']},
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