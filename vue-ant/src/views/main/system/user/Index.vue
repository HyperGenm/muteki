<template>
    <my-table :ref="table.ref"
              :url="table.url"
              :data="table.data"
              :columns="table.columns"
              :headerSearchList="table.headerSearchList"
              :rowActionButtons="table.rowActionButtons"></my-table>
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
    import $function from '@/utils/function';
    import $axios from '@/utils/axios';
    import $ant from '@/utils/ant';
    import {Drawer} from 'ant-design-vue';

    export default {
        name: "Index",
        components: {
            [Drawer.name]: Drawer,
            'my-table': defineAsyncComponent(() => import('@/components/table/Index.vue')),
            'my-descriptions': defineAsyncComponent(() => import('@/components/descriptions/Index.vue')),
        },
        setup() {
            let {
                user_disable, user_enable
            } = $function.getLocationStorage('buttonMap');
            //table的ref
            let $ref = null;
            //表格展示
            let table = reactive({
                ref(ref) {
                    $ref = ref;
                },
                url: $global.url.system.user.getPageList,
                data: {
                    createTimeSort: 'DESC'
                },
                columns: [
                    {title: '真实姓名', dataIndex: 'realName', width: 120, fixed: 'left'},
                    {title: '用户名', dataIndex: 'username', width: 120, fixed: 'left'},
                    {
                        title: '状态', dataIndex: 'status', type: 'tag',
                        element({status}) {
                            let result = [
                                null,
                                {content: '正常', color: 'blue'},
                                {content: '禁用', color: '#666'},
                                {content: '封号', color: 'red'},
                            ];
                            return result[status] || {
                                content: `未知状态 ${status}`,
                                color: 'red'
                            }
                        }
                    },
                    {title: '用户最后活跃ip地址', dataIndex: 'lastIpAddress'},
                    {title: '用户最后活跃时间', dataIndex: 'lastActiveTime'},
                    {title: '用户创建时间', dataIndex: 'createTime', width: 120},
                ],
                headerSearchList: [
                    {type: 'input', prop: 'username', placeholder: '用户名'},
                    {
                        type: 'select', prop: 'status', placeholder: '状态',
                        options: [
                            {label: '正常', value: 1},
                            {label: '禁用', value: 2},
                            {label: '封号', value: 3},
                        ]
                    },
                    {type: 'datePicker', prop: 'lastActiveTime', placeholder: '最后活跃时间'},
                    {type: 'datePicker', prop: 'createTime', placeholder: '创建时间'}
                ],
                rowActionButtons: [
                    {
                        name: '详情', icon: 'SearchOutlined', show: true,
                        handleClick(row) {
                            detail.showDetail(row);
                        }
                    },
                    {
                        name: '禁用', icon: 'edit', type: 'danger',
                        showFormatter(row) {
                            if (!user_disable) {
                                return false;
                            }
                            if (null == row) {
                                return true;
                            }
                            return 1 === row['status'];
                        },
                        handleClick(row, index) {
                            disableUser(row, index);
                        }
                    },
                    {
                        name: '启用', icon: 'edit',
                        showFormatter(row) {
                            if (!user_enable) {
                                return false;
                            }
                            if (null == row) {
                                return true;
                            }
                            return 2 === row['status'];
                        },
                        handleClick(row, index) {
                            enableUser(row, index);
                        }
                    },
                ]
            });
            let detail = reactive({
                visible: false,
                title: '',
                itemList: [],
                showDetail(row) {
                    detail.title = row['realName'] || row['username'];
                    detail.itemList = [
                        {label: '真实姓名', content: row['realName']},
                        {label: '用户名', content: row['username']},
                        {
                            label: '状态', type: 'tag',
                            element() {
                                let result = [
                                    null,
                                    {content: '正常', color: 'blue'},
                                    {content: '禁用', color: '#666'},
                                    {content: '封号', color: 'red'},
                                ];
                                return result[row['status']] || {
                                    content: `未知状态 ${row['status']}`,
                                    color: 'red'
                                }
                            }
                        },
                        {label: '用户最后活跃ip地址', content: row['lastIpAddress']},
                        {label: '用户最后活跃时间', content: row['lastActiveTime']},
                        {label: '用户创建时间', content: row['createTime']},
                    ];
                    detail.visible = true;
                }
            });
            //禁用用户
            let disableUser = (row, index) => {
                $ant.confirm({
                    content: `确定禁用 ${row['realName']}`,
                    onOk() {
                        $axios({
                            url: $global.url.system.user.disableUser,
                            method: 'post',
                            data: {
                                id: row['id']
                            },
                            success() {
                                $ref.getTableData();
                                $ant.successMsg('禁用成功');
                            }
                        });
                    }
                });
            }
            //启用用户
            let enableUser = (row, index) => {
                $ant.confirm({
                    content: `确定启用 ${row['realName']}`,
                    onOk() {
                        $axios({
                            url: $global.url.system.user.enableUser,
                            method: 'post',
                            data: {
                                id: row['id']
                            },
                            success() {
                                $ref.getTableData();
                                $ant.successMsg('启用成功');
                            }
                        });
                    }
                });
            }
            return {
                table,
                detail
            }
        }
    }
</script>