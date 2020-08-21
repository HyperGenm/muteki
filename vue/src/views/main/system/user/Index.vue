<template>
    <div id="index">
        <template>
            <wei-table ref="table"
                       :tableDataRequest="tableDataRequest"
                       :tableColumns="tableColumns"
                       :tableOperates="tableOperates"
                       :tableSearch="tableSearch"
                       :defaultSort="{prop:'createTime',order: 'descending'}"></wei-table>
        </template>
        <template>
            <el-dialog :title="formData['realName'] || formData['username']"
                       :visible.sync="dialogDetail">
                <detail :formData="formData"></detail>
            </el-dialog>
        </template>

    </div>
</template>

<script>
    export default {
        name: "Index",
        components: {
            'wei-table': () => import('@/components/table/Complex.vue'),
            'detail': () => import('./Detail.vue')
        },
        data() {
            let that = this;
            let {
                user_disable, user_enable
            } = this.$globalFun.getSessionStorage('buttonMap');
            return {
                tableDataRequest: {
                    url: that.$global.URL.system.user.getPageList,
                    data: {
                        createTimeSort: 'DESC'
                    }
                },
                tableColumns: [
                    {label: '用户名', prop: 'username', showOverflowTooltip: true, fixed: 'left'},
                    {label: '真实姓名', prop: 'realName', showOverflowTooltip: true, fixed: 'left'},
                    {
                        label: '状态', type: 'tag',
                        element({status}) {
                            let result = [
                                null,
                                {content: '正常', type: 'success'},
                                {content: '禁用', type: 'warning'},
                                {content: '封号', type: 'danger'},
                            ];
                            return result[status] || {
                                content: `未知状态 ${status}`,
                                type: 'danger'
                            }
                        }
                    },
                    {label: '用户最后活跃ip地址', prop: 'lastIpAddress', showOverflowTooltip: true},
                    {
                        label: '用户最后活跃时间',
                        prop: 'lastActiveTime',
                        width: 150,
                        sortable: 'custom',
                        type: 'icon',
                        showOverflowTooltip: true,
                        element(row) {
                            return {
                                leftIcon: 'el-icon-time',
                                content: row['lastActiveTime']
                            };
                        }
                    },
                    {
                        label: '用户创建时间',
                        prop: 'createTime',
                        width: 120,
                        sortable: 'custom',
                        type: 'icon',
                        element(row) {
                            return {
                                leftIcon: 'el-icon-time',
                                content: row['createTime']
                            };
                        }
                    }
                ],
                //表格对应每一行按钮
                tableOperates: {
                    //按钮数组
                    buttons:
                        [
                            {
                                name: '查看', type: 'primary', show: true,
                                handleClick(row) {
                                    that.formData = row;
                                    that.dialogDetail = true;
                                }
                            },
                            {
                                name: '禁用', type: 'warning',
                                showFormatter(row) {
                                    if (!user_disable) {
                                        return false;
                                    }
                                    if (null == row) {
                                        return true;
                                    }
                                    return 1 === row['status'];
                                },
                                handleClick(row) {
                                    that.disableUser(row);
                                }
                            },
                            {
                                name: '启用', type: 'success',
                                showFormatter(row) {
                                    if (!user_enable) {
                                        return false;
                                    }
                                    if (null == row) {
                                        return true;
                                    }
                                    return 2 === row['status'];
                                },
                                handleClick(row) {
                                    that.enableUser(row);
                                }
                            },
                        ]
                },
                //表格顶部搜索
                tableSearch: [
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
                //详情弹窗
                dialogDetail: false,
                //表单数据
                formData: {}
            }
        },
        methods: {
            disableUser({id, username}) {
                let that = this;
                this.$globalFun.messageBox({
                    message: `确定禁用 ${username} `,
                    confirm() {
                        that.$axios({
                            method: 'post',
                            url: that.$global.URL.system.user.disableUser,
                            data: {
                                id
                            },
                            success() {
                                that.$globalFun.successMsg('操作成功');
                                that.$refs['table'].renderTable();
                            }
                        });
                    }
                })
            },
            enableUser({id, username}) {
                let that = this;
                this.$globalFun.messageBox({
                    message: `确定启用 ${username} `,
                    confirm() {
                        that.$axios({
                            method: 'post',
                            url: that.$global.URL.system.user.enableUser,
                            data: {
                                id
                            },
                            success() {
                                that.$globalFun.successMsg('操作成功');
                                that.$refs['table'].renderTable();
                            }
                        });
                    }
                })
            }
        }
    }
</script>