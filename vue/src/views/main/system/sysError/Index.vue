<template>
    <div id="index">
        <template class="wei-table">
            <wei-table ref="table" :showHeaderSearch="true"
                       :tableDataRequest="tableDataRequest"
                       :tableColumns="tableColumns"
                       :tableOperates="tableOperates"></wei-table>
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
                    url: that.$global.URL.system.sysError.getPageList,
                    data: {}
                },
                tableColumns: [
                    {label: '类名', prop: 'className'},
                    {label: '方法名', prop: 'methodName'},
                    {label: '行数', prop: 'lineNumber'},
                    {label: '详情', prop: 'content', showOverflowTooltip: true},
                    {label: '备注', prop: 'remark', showOverflowTooltip: true},
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
                //表单数据
                formData: {},
                //详情弹窗
                dialogDetail: false,
            }
        }
    }
</script>