<template>
    <div id="index" style="display: flex;">
        <div class="wei-table" style="flex: 3;">
            <wei-table ref="table" :showHeaderSearch="true"
                       :tableDataRequest="tableDataRequest"
                       :tableColumns="tableColumns"
                       :tableHeaderButtons="tableHeaderButtons"
                       :tableOperates="tableOperates"
                       @columnSwitchChange="columnSwitchChange"
                       @cellClick="cellClick"></wei-table>
        </div>
        <div class="function" style="flex: 2;overflow-y: scroll;position:relative;">
            <div class="button" style="position: fixed;top: 70px;right: 50px;z-index: 1;">
                <el-button v-if="haveSysRoleSaveRole"
                           type="primary" size="small"
                           @click="saveFunction">保存
                </el-button>
            </div>
            <el-divider content-position="left">拥有权限</el-divider>
            <el-tree ref="functionTree"
                     show-checkbox default-expand-all
                     node-key="id" :props="{label:'title'}"
                     :data="functionTree">
            </el-tree>
        </div>
        <template>
            <el-dialog :title="'add' === editType ? '新增用户' : '编辑用户'"
                       :visible.sync="dialogEditForm">
                <edit-form :editType="editType" :formData="formData" :isShow="dialogEditForm"
                           @closeDialog="dialogEditForm = false"
                           @renderTable="$refs.table.renderTable()"></edit-form>
            </el-dialog>
        </template>
    </div>
</template>

<script>
    export default {
        name: "Index",
        components: {
            'wei-table': () => import('@/components/table/Complex.vue'),
            'edit-form': () => import('./EditForm.vue'),
        },
        data() {
            let that = this;
            let {
                sysRole_add, sysRole_update, sysRole_delete, sysRole_save, sysRole_status
            } = this.$globalFun.getSessionStorage('buttonMap');
            return {
                //是否有保存功能权限
                haveSysRoleSaveRole: sysRole_save,
                tableDataRequest: {
                    url: that.$global.URL.system.sysRole.getPageList,
                    data: {}
                },
                tableColumns: [
                    {label: '名称', prop: 'name', showOverflowTooltip: true, fixed: 'left'},
                    {
                        label: '状态', prop: 'status', type: 'switch',
                        element({status}) {
                            return {
                                value: 1 === status,
                                disabled: !sysRole_status
                            }
                        }
                    },
                    {label: '排序', prop: 'sort'},
                    {label: '备注', prop: 'remark', showOverflowTooltip: true},
                    {
                        label: '创建时间', prop: 'createTime', showOverflowTooltip: true, type: 'icon', element(row) {
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
                        name: '新增', icon: 'el-icon-plus', type: 'success', show: sysRole_add,
                        handleClick() {
                            that.editType = 'add';
                            that.formData = {
                                status: 1,
                                sort: 77,
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
                                name: '编辑', type: 'warning', show: sysRole_update,
                                handleClick(row) {
                                    that.editType = 'update';
                                    that.formData = row;
                                    that.dialogEditForm = true;
                                }
                            },
                            {
                                name: '删除', type: 'danger', show: sysRole_delete,
                                handleClick(row) {
                                    that.deleteRole(row);
                                }
                            },
                        ]
                },
                //操作类型
                editType: 'add',
                //编辑弹窗
                dialogEditForm: false,
                //表单数据
                formData: {},
                //功能树形结构
                functionTree: [],
                //当前点击的roleId
                nowClickRoleId: null,
                //父级id列表
                parentFunctionList: []
            }
        },
        mounted() {
            let that = this;
            this.$axios({
                url: that.$global.URL.system.sysFunction.getTree,
                success(data) {
                    //找到所有的父级节点id
                    let parentFunctionList = [];
                    data.forEach(value => {
                        if (null !== value.children && 0 < value.children.length) {
                            parentFunctionList.push(value.id);
                            let children = that.findParentFunctionList(value['children']);
                            parentFunctionList = parentFunctionList.concat(children);
                        }
                    });
                    that.parentFunctionList = parentFunctionList;
                    that.functionTree = data;
                }
            })
        },
        methods: {
            /**
             *找到所有的父级节点id
             */
            findParentFunctionList(data) {
                let result = [];
                data.forEach(value => {
                    if (null !== value.children && 0 < value.children.length) {
                        result.push(value['id']);
                        let children = this.findParentFunctionList(value['children']);
                        result = result.concat(children);
                    }
                });
                return result;
            },
            /**
             * 改变用户状态
             * @param index
             * @param prop
             * @param row
             * @param value
             */
            columnSwitchChange({index, prop, row, value}) {
                let that = this;
                let status = value ? 1 : 2;
                this.$axios({
                    url: that.$global.URL.system.sysRole.updateStatus,
                    method: 'post',
                    data: {
                        id: row['id'],
                        status
                    },
                    success() {
                        that.$refs['table']['tableData'][index]['status'] = status;
                    }
                })
            },
            /**
             * 行被点击
             * @param row
             * @param column
             * @param cell
             * @param event
             */
            cellClick({row, column, cell, event}) {
                let {id} = row;
                let that = this;
                this.$axios({
                    url: that.$global.URL.system.sysRole.getFunctionList,
                    data: {
                        id
                    },
                    success(data) {
                        let {parentFunctionList} = that;
                        for (let i = 0; i < data.length; i++) {
                            for (let j = 0; j < parentFunctionList.length; j++) {
                                if (data[i] !== parentFunctionList[j]) {
                                    continue;
                                }
                                data.splice(i, 1);
                                i--;
                                break;
                            }
                        }
                        that.$refs['functionTree'].setCheckedKeys(data);
                        that.nowClickRoleId = id;
                    }
                })
            },
            /**
             * 保存权限
             */
            saveFunction() {
                let getCheckedKeys = this.$refs['functionTree'].getCheckedKeys();
                let getHalfCheckedKeys = this.$refs['functionTree'].getHalfCheckedKeys();
                let funIds = getCheckedKeys.concat(getHalfCheckedKeys);
                let that = this;
                this.$axios({
                    url: that.$global.URL.system.sysRole.updateRoleFunction,
                    method: 'post',
                    data: {
                        roleId: that.nowClickRoleId,
                        functionIds: funIds
                    },
                    success() {
                        that.$globalFun.successMsg('角色保存成功');
                    }
                })
            },
            /**
             * 删除
             */
            deleteRole({id, name}) {
                let that = this;
                this.$globalFun.messageBox({
                    message: `确定删除 ${name} 该操作无法撤销`,
                    confirm() {
                        that.$axios({
                            url: that.$global.URL.system.sysRole.delete,
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
                })
            }
        }
    }
</script>