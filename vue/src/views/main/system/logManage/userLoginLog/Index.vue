<template>
    <div id="index">
        <template class="wei-table">
            <wei-table ref="table"
                       :tableDataRequest="tableDataRequest"
                       :tableColumns="tableColumns"
                       :tableOperates="tableOperates"
                       :defaultSort="{prop:'createTime',order: 'descending'}"
                       :tableSearch="tableSearch"></wei-table>
        </template>
        <template>
            <el-dialog title="操作详情"
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
            return {
                tableDataRequest: {
                    url: that.$global.URL.system.userLoginLog.getPageList,
                    data: {
                        createTimeSort: 'DESC'
                    }
                },
                tableColumns: [
                    {label: '用户名', prop: 'username', showOverflowTooltip: true, fixed: 'left'},
                    {
                        label: '终端', type: 'tag',
                        element({terminal}) {
                            let result = {
                                "网页": 'success'
                            };
                            return {
                                content: terminal,
                                type: result[terminal] || 'warning'
                            }
                        }
                    },
                    {label: '省份', prop: 'loginProvince', showOverflowTooltip: true, fixed: 'left'},
                    {label: '城市', prop: 'loginCity', showOverflowTooltip: true, fixed: 'left'},
                    {label: 'ip', prop: 'ipAddress'},
                    {
                        label: '结果', type: 'tag',
                        element({resultCode}) {
                            let result = {
                                200: {content: '成功', type: 'success'}
                            };
                            return result[resultCode] || {
                                content: '失败',
                                type: 'warning'
                            }
                        }
                    },
                    {
                        label: '状态码', type: 'tag',
                        element({resultCode}) {
                            let result = {
                                200: {content: '200', type: 'success'}
                            };
                            return result[resultCode] || {
                                content: resultCode,
                                type: 'warning'
                            }
                        }
                    },
                    {label: '响应信息', prop: 'resultMsg', showOverflowTooltip: true},
                    {label: '浏览器', prop: 'borderName'},
                    {label: '操作系统', prop: 'osName'},
                    {
                        label: '创建时间',
                        prop: 'createTime',
                        width: 120,
                        sortable: 'custom',
                        type: 'icon',
                        showOverflowTooltip: true,
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
                            }
                        ]
                },
                //表格顶部搜索
                tableSearch: [
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
                //表单数据
                formData: {},
                //详情弹窗
                dialogDetail: false,
            }
        }
    }
</script>