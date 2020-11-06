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
                url: $global.url.system.userLoginLog.getPageList,
                data: {
                    createTimeSort: 'DESC'
                },
                columns: [
                    {title: '用户名', dataIndex: 'username', width: 120, fixed: 'left'},
                    {
                        title: '终端', dataIndex: 'terminal', type: 'tag',
                        element({terminal}) {
                            let result = {
                                '网页': 'blue',
                                '客户端': 'green',
                            };
                            return {
                                content: terminal,
                                color: result[terminal] || 'red'
                            }
                        }
                    },
                    {title: '省份', dataIndex: 'loginProvince'},
                    {title: '城市', dataIndex: 'loginCity'},
                    {title: 'ip', dataIndex: 'ipAddress'},
                    {
                        title: '结果', type: 'tag',
                        element({resultCode}) {
                            let result = {
                                200: {content: '成功', color: 'green'}
                            };
                            return result[resultCode] || {
                                content: '失败',
                                color: 'red'
                            }
                        }
                    },
                    {
                        title: '状态码', type: 'tag',
                        element({resultCode}) {
                            let result = {
                                200: {content: '200', color: 'green'}
                            };
                            return result[resultCode] || {
                                content: resultCode,
                                color: 'red'
                            }
                        }
                    },
                    {title: '响应信息', dataIndex: 'resultMsg'},
                    {title: '浏览器', dataIndex: 'borderName'},
                    {title: '操作系统', dataIndex: 'osName'},
                    {title: '创建时间', dataIndex: 'createTime', width: 120, sorter: true},
                ],
                headerSearchList: [
                    {type: 'input', prop: 'username', placeholder: '用户名'},
                    {
                        type: 'select', prop: 'terminal', allowCreate: true, placeholder: '终端',
                        options: [
                            {label: '网页', value: '网页'},
                            {label: '客户端', value: '客户端'},
                        ]
                    },
                    {type: 'input', prop: 'loginProvince', placeholder: '省份'},
                    {type: 'input', prop: 'loginCity', placeholder: '城市'},
                    {type: 'input', prop: 'ipAddress', placeholder: 'ip'},
                    {
                        type: 'select', prop: 'result', placeholder: '结果',
                        options: [
                            {label: '成功', value: '成功'},
                            {label: '失败', value: '失败'}
                        ]
                    },
                    {type: 'input', prop: 'osName', placeholder: '操作系统'},
                    {type: 'input', prop: 'borderName', placeholder: '浏览器'},
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
                        {
                            label: '终端', dataIndex: 'terminal', type: 'tag',
                            element() {
                                let result = {
                                    '网页': 'blue',
                                    '客户端': 'green',
                                };
                                return {
                                    content: row['terminal'],
                                    color: result[row['terminal']] || 'red'
                                }
                            }
                        },
                        {label: '省份', content: row['loginProvince']},
                        {label: '城市', content: row['loginCity']},
                        {label: 'ip', content: row['ipAddress']},
                        {
                            label: '结果', type: 'tag',
                            element() {
                                let result = {
                                    200: {content: '成功', color: 'green'}
                                };
                                return result[row['resultCode']] || {
                                    content: '失败',
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
                                    content: row['resultCode'],
                                    color: 'red'
                                }
                            }
                        },
                        {label: '响应信息', content: row['resultMsg']},
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