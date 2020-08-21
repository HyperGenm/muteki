<template>
    <div id="index">
        <template class="wei-table">
            <wei-table ref="table"
                       :tableDataRequest="tableDataRequest"
                       :tableColumns="tableColumns"
                       :tableHeaderButtons="tableHeaderButtons"
                       :tableOperates="tableOperates"></wei-table>
        </template>
        <template>
            <el-dialog title="操作详情"
                       :visible.sync="dialogDetail">
                <detail :formData="formData"></detail>
            </el-dialog>
            <el-dialog :title="'add' === editType ? '新增功能' : '编辑功能'"
                       :visible.sync="dialogEditForm">
                <edit-form :editType="editType" :formData="formData" :isShow="dialogEditForm"
                           @closeDialog="dialogEditForm = false"
                           @renderTable="$refs.table.renderTable()"></edit-form>
            </el-dialog>
            <el-dialog :title="`${formData['title']} 拥有api`"
                       :visible.sync="dialogContainApi">
                <el-form label-width="100px" size="mini">
                    <el-form-item v-for="(item, index) in containApiList"
                                  :label="`api  ${index + 1}`">
                        <el-input v-model="item.value"></el-input>
                        <el-button type="warning" @click.prevent="removeContainApi(index)">删除</el-button>
                    </el-form-item>
                    <el-form-item>
                        <el-button @click="addContainApi" type="primary">新增api</el-button>
                        <el-button @click="submitContainApi" type="success">提交</el-button>
                    </el-form-item>
                </el-form>
            </el-dialog>
        </template>
    </div>
</template>

<script>
    export default {
        name: "Index",
        components: {
            'wei-table': () => import('@/components/table/Complex.vue'),
            'detail': () => import('./Detail.vue'),
            'edit-form': () => import('./EditForm.vue')
        },
        data() {
            let that = this;
            let {
                sysFunction_add, sysFunction_update, sysFunction_delete
            } = this.$globalFun.getSessionStorage('buttonMap');
            return {
                tableDataRequest: {
                    url: that.$global.URL.system.sysFunction.getPageList,
                    data: {}
                },
                tableColumns: [
                    {label: '标题', prop: 'title', className: 'tree', width: 127},
                    {label: 'name', prop: 'name', showOverflowTooltip: true},
                    {
                        label: '图标', prop: 'icon', formatter(row) {
                            return `<i class="${row.icon}"></i>`;
                        }
                    },
                    {label: '路径', prop: 'path', showOverflowTooltip: true},
                    {
                        label: '类型', type: 'tag',
                        element({type}) {
                            let result = [
                                null,
                                {content: '菜单'},
                                {content: '按钮', type: 'success'},
                            ];
                            return result[type] || {
                                content: `未知类型:${type}`,
                                type: 'danger'
                            }
                        }
                    },
                    {
                        label: '专属', type: 'tag',
                        element({superFlag}) {
                            let result = [
                                null,
                                {content: '普通', type: 'info'},
                                {content: 'vip', type: 'success'},
                            ];
                            return result[superFlag] || {
                                content: `未知类型:${superFlag}`,
                                type: 'danger'
                            }
                        }
                    },
                    {
                        label: '内/外部链接', type: 'tag',
                        element({externalFlag}) {
                            let result = [
                                null,
                                {content: '内部', type: 'success'},
                                {content: '外部超链接'},
                            ];
                            return result[externalFlag] || {
                                content: `未知类型:${externalFlag}`,
                                type: 'danger'
                            }
                        }
                    },
                    {label: '排序', prop: 'sort'},
                    {label: '拥有api', prop: 'containApi', showOverflowTooltip: true},
                    {
                        label: '创建时间',
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
                //表格上面按钮
                tableHeaderButtons: [
                    {
                        name: '新增', icon: 'el-icon-plus', type: 'success', show: sysFunction_add,
                        handleClick() {
                            that.editType = 'add';
                            that.formData = {
                                type: 1,
                                superFlag: 1,
                                externalFlag: 1
                            };
                            that.dialogEditForm = true;
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
                                name: '编辑', type: 'warning', show: sysFunction_update,
                                handleClick(row) {
                                    that.editType = 'update';
                                    that.formData = row;
                                    that.dialogEditForm = true;
                                }
                            },
                            {
                                name: '删除', type: 'danger', show: sysFunction_delete,
                                handleClick(row) {
                                    that.deleteFunction(row);
                                }
                            },
                            {
                                name: '编辑拥有api', type: 'warning', show: true,
                                handleClick(row) {
                                    that.formData = row;
                                    let {containApi} = row;
                                    let array = [];
                                    if (null != containApi && 0 < containApi.length) {
                                        JSON.parse(containApi).forEach(value => {
                                            array.push({
                                                value
                                            });
                                        });
                                    } else {
                                        array = [
                                            {value: ''}
                                        ];
                                    }
                                    that.containApiList = array;
                                    that.dialogContainApi = true;
                                }
                            }
                        ]
                },
                //操作类型
                editType: 'add',
                //编辑弹窗
                dialogEditForm: false,
                //表单数据
                formData: {},
                //详情弹窗
                dialogDetail: false,
                //拥有api弹窗
                dialogContainApi: false,
                //拥有api拉列表
                containApiList: [
                    {value: ''}
                ]
            }
        },
        methods: {
            /**
             * 删除
             * @param id
             * @param title
             */
            deleteFunction({id, title}) {
                let that = this;
                this.$globalFun.messageBox({
                    message: `确定删除 ${title} 该操作无法撤销`,
                    confirm() {
                        that.$axios({
                            url: that.$global.URL.system.sysFunction.delete,
                            method: 'post',
                            data: {
                                id
                            },
                            success() {
                                that.$globalFun.successMsg('成功');
                                that.$refs['table'].renderTable();
                            }
                        })
                    }
                });
            },
            removeContainApi(index) {
                this.containApiList.splice(index, 1);
            },
            addContainApi() {
                this.containApiList.push({
                    value: ''
                });
            },
            submitContainApi() {
                let apiList = [];
                this.containApiList.forEach(value => {
                    apiList.push(value.value);
                });
                let that = this;
                this.$axios({
                    url: that.$global.URL.system.sysFunction.updateContainApi,
                    method: 'post',
                    data: {
                        id: that.formData['id'],
                        apiList
                    },
                    success() {
                        that.$globalFun.successMsg('更新成功');
                        that.$refs['table'].renderTable();
                        that.dialogContainApi = false;
                    }
                })
            }
        }
    }
</script>