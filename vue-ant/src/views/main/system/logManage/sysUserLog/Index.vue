<template>
    <my-table :url="table.url"
              :data="table.data"
              :columns="table.columns"
              :headerSearchList="table.headerSearchList"
              :rowActionButtons="table.rowActionButtons"
              :headerSearchOpen="true"></my-table>
    <template>
        <a-drawer width="67%"
                  v-model:visible="detail.visible">
            <my-descriptions :title="detail.title"
                             :itemList="detail.itemList"></my-descriptions>
        </a-drawer>
    </template>
</template>

<script>
    import {reactive, defineAsyncComponent} from 'vue';
    import $global from '@/utils/global';
    import {Drawer} from 'ant-design-vue';

    export default {
        name: "Index",
        components: {
            [Drawer.name]: Drawer,
            'my-table': defineAsyncComponent(() => import('@/components/table/Index.vue')),
            'my-descriptions': defineAsyncComponent(() => import('@/components/descriptions/Index.vue')),
        },
        setup() {
            let table = reactive({
                url: $global.url.system.sysUserLog.getPageList,
                data: {
                    createTimeSort: 'DESC'
                },
                columns: [
                    {title: '真实姓名', dataIndex: 'realName', width: 120, fixed: 'left'},
                    {title: '用户名', dataIndex: 'username', width: 120, fixed: 'left'},
                    {title: '请求路径', dataIndex: 'url', tooltip: true},
                    {title: '请求方法名', dataIndex: 'methodName'},
                    {title: '参数', dataIndex: 'param', tooltip: true},
                    {
                        title: '类型', dataIndex: 'type', type: 'tag',
                        element({type}) {
                            let result = [
                                null,
                                {content: '查询', color: 'blue'},
                                {content: '新增', color: 'green'},
                                {content: '修改', color: 'yellow'},
                                {content: '删除', color: 'red'},
                            ];
                            return result[type] || {
                                content: `未知类型,type:${type}`,
                                color: 'red'
                            }
                        }
                    },
                    {
                        title: '状态码', dataIndex: 'resultCode', type: 'tag',
                        element({resultCode}) {
                            let result = {
                                200: {content: '200', color: 'green'}
                            };
                            return result[resultCode] || {
                                content: `未知状态码 ${resultCode}`,
                                color: 'red'
                            }
                        }
                    },
                    {title: '响应信息', dataIndex: 'resultMsg'},
                    {
                        title: '请求耗时', dataIndex: 'timeConsuming', type: 'tag',
                        element({timeConsuming}) {
                            let color = '';
                            if (200 >= timeConsuming) {
                                color = 'green';
                            } else if (1000 >= timeConsuming) {
                                color = 'blue';
                            } else if (2000 >= timeConsuming) {
                                color = 'yellow';
                            } else {
                                color = 'red';
                            }
                            return {
                                content: `${timeConsuming}ms`,
                                color
                            }
                        }
                    },
                    {title: '操作', dataIndex: 'description', tooltip: true},
                    {title: 'ip地址', dataIndex: 'ipAddress'},
                    {title: '浏览器', dataIndex: 'borderName'},
                    {title: '操作系统', dataIndex: 'osName'},
                    {title: '创建时间', dataIndex: 'createTime', width: 120, sorter: true},
                ],
                headerSearchList: [
                    {type: 'input', prop: 'username', placeholder: '用户名'},
                    {type: 'input', prop: 'realName', placeholder: '真实姓名'},
                    {
                        type: 'select', prop: 'type', placeholder: '类型',
                        options: [
                            {label: '查询', value: 1},
                            {label: '新增', value: 2},
                            {label: '修改', value: 3},
                            {label: '删除', value: 4}
                        ]
                    },
                    {type: 'input', prop: 'resultCode', placeholder: '状态码'},
                    {type: 'input', prop: 'description', placeholder: '操作'},
                    {type: 'input', prop: 'ipAddress', placeholder: 'ip地址'},
                    {type: 'input', prop: 'borderName', placeholder: '浏览器'},
                    {type: 'input', prop: 'osName', placeholder: '操作系统'},
                    {type: 'datePicker', prop: 'startTime', placeholder: '起始时间'},
                    {type: 'datePicker', prop: 'endTime', placeholder: '截止时间'}
                ],
                rowActionButtons: [
                    {
                        name: '详情', icon: 'SearchOutlined', show: true,
                        handleClick(row) {
                            detail.showDetail(row);
                        }
                    }
                ]
            });
            let detail = reactive({
                visible: false,
                title: '',
                itemList: [],
                showDetail(row) {
                    detail.title = row['realName'];
                    detail.itemList = [
                        {label: '用户名', content: row['username']},
                        {label: '真实姓名', content: row['realName']},
                        {label: '请求路径', content: row['url']},
                        {label: '请求方法名', content: row['methodName']},
                        {label: '参数', content: row['param']},
                        {
                            label: '类型', type: 'tag',
                            element() {
                                let result = [
                                    null,
                                    {content: '查询', color: 'blue'},
                                    {content: '新增', color: 'green'},
                                    {content: '修改', color: 'yellow'},
                                    {content: '删除', color: 'red'},
                                ];
                                return result[row['type']] || {
                                    content: `未知类型,type:${row['type']}`,
                                    color: 'red'
                                }
                            }
                        },
                        {
                            label: '状态码', type: 'tag',
                            element() {
                                let result = {
                                    200: {content: '200', color: 'green'}
                                };
                                return result[row['resultCode']] || {
                                    content: `未知状态码 ${row['resultCode']}`,
                                    color: 'red'
                                }
                            }
                        },
                        {label: '响应信息', content: row['resultMsg']},
                        {
                            label: '请求耗时', type: 'tag',
                            element() {
                                let {timeConsuming} = row;
                                let color = '';
                                if (200 >= timeConsuming) {
                                    color = 'green';
                                } else if (1000 >= timeConsuming) {
                                    color = 'blue';
                                } else if (2000 >= timeConsuming) {
                                    color = 'yellow';
                                } else {
                                    color = 'red';
                                }
                                return {
                                    content: `${timeConsuming}ms`,
                                    color
                                }
                            }
                        },
                        {label: '操作', content: row['description']},
                        {label: 'ip地址', content: row['ipAddress']},
                        {label: '浏览器', content: row['borderName']},
                        {label: '操作系统', content: row['osName']},
                        {label: '创建时间', content: row['createTime']},
                    ];
                    detail.visible = true;
                }
            });
            return {
                table,
                detail
            }
        }
    }
</script>