<template>
    <div id="index">
        <template class="wei-table">
            <wei-table ref="table"
                       :tableDataRequest="tableDataRequest"
                       :tableColumns="tableColumns"
                       :tableHeaderButtons="tableHeaderButtons"
                       :tableOperates="tableOperates"
                       :tableSearch="tableSearch"
                       :defaultSort="{prop:'createTime',order: 'descending'}"
                       @columnSwitchChange="columnSwitchChange"></wei-table>
        </template>
        <template>
            <el-dialog :title="'add' === editType ? '新增用户' : '编辑用户'"
                       :visible.sync="dialogEditForm">
                <edit-form :editType="editType" :formData="formData"
                           @closeDialog="dialogEditForm = false"
                           @renderTable="$refs.table.renderTable()"></edit-form>
            </el-dialog>
            <el-dialog :title="formData['realName'] || formData['username']"
                       :visible.sync="dialogDetail">
                <detail :formData="formData"></detail>
            </el-dialog>
            <el-dialog :title="formData['realName'] || formData['username']"
                       :visible.sync="dialogUpdateRole">
                <el-select v-model="chooseRoleIds" multiple placeholder="请选择角色">
                    <el-option v-for="item in roleList" :key="item.value"
                               :label="item.label" :value="item.value">
                    </el-option>
                </el-select>
                <el-button type="primary" style="margin-left: 20px;"
                           @click="saveRole">确定
                </el-button>
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
            'detail': () => import('./Detail.vue')
        },
        data() {
            let that = this;
            let {
                sysUser_add, sysUser_delete, sysUser_role, sysUser_resetPwd, sysUser_updatePhone, sysUser_updateStatus
            } = this.$globalFun.getSessionStorage('buttonMap');
            return {
                tableDataRequest: {
                    url: that.$global.URL.system.sysUser.getPageList,
                    data: {
                        createTimeSort: 'DESC'
                    }
                },
                tableColumns: [
                    {label: '用户名', prop: 'username', showOverflowTooltip: true, fixed: 'left'},
                    {label: '真实姓名', prop: 'realName', showOverflowTooltip: true, fixed: 'left'},
                    {label: '角色', prop: 'roleList', showOverflowTooltip: true},
                    {label: '手机号', prop: 'phone'},
                    {
                        label: '状态', type: 'switch',
                        element({status}) {
                            return {
                                value: 1 === status,
                                disabled: !sysUser_updateStatus
                            }
                        }
                    },
                    {
                        label: '头像', type: 'avatar', element({iconAllPath}) {
                            return {
                                src: iconAllPath
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
                //表格上面按钮
                tableHeaderButtons: [
                    {
                        name: '新增', icon: 'el-icon-plus', type: 'success', show: sysUser_add,
                        handleClick() {
                            that.editType = 'add';
                            that.formData = {
                                status: 1
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
                                name: '修改手机号码', type: 'success', show: sysUser_updatePhone,
                                handleClick(row) {
                                    that.updatePhone(row);
                                }
                            },
                            {
                                name: '修改角色', type: 'success', show: sysUser_role,
                                handleClick(row) {
                                    that.formData = row;
                                    that.dialogUpdateRole = true;
                                }
                            },
                            {
                                name: '重置密码', type: 'warning', show: sysUser_resetPwd,
                                handleClick(row) {
                                    that.resetPassword(row);
                                }
                            },
                            {
                                name: '删除', type: 'danger', show: sysUser_delete,
                                handleClick(row) {
                                    that.deleteUser(row);
                                }
                            },
                        ]
                },
                //表格顶部搜索
                tableSearch: [
                    {type: 'input', prop: 'username', placeholder: '用户名'},
                    {type: 'select', prop: 'roleId', placeholder: '角色', options: []},
                    {
                        type: 'select', prop: 'status', placeholder: '状态',
                        options: [
                            {label: '正常', value: 1},
                            {label: '禁止', value: 2}
                        ]
                    },
                    {type: 'datePicker', prop: 'lastActiveTime', placeholder: '最后活跃时间'},
                    {type: 'datePicker', prop: 'createTime', placeholder: '创建时间'}
                ],
                //操作类型
                editType: 'add',
                //编辑弹窗
                dialogEditForm: false,
                //表单数据
                formData: {},
                //详情弹窗
                dialogDetail: false,
                //修改角色
                dialogUpdateRole: false,
                //选中的角色
                chooseRoleIds: [],
                //角色列表
                roleList: []
            }
        },
        mounted() {
            let that = this;
            this.$axios({
                url: that.$global.URL.system.sysRole.getList,
                success(data) {
                    let roleList = [];
                    data.forEach(value => {
                        roleList.push({
                            label: value.name,
                            value: value.id
                        });
                    });
                    that.roleList = roleList;
                    that.tableSearch[1]['options'] = roleList;
                }
            })
        },
        methods: {
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
                    url: that.$global.URL.system.sysUser.updateStatus,
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
             * 修改手机号码
             * @param id
             * @param realName
             */
            updatePhone({id}) {
                let that = this;
                this.$globalFun.messageBoxInput({
                    message: `请输入手机号码`,
                    confirm(value, done) {
                        if (that.$globalFun.notPhone(value)) {
                            that.$globalFun.errorMsg('手机号码格式错误');
                            return;
                        }
                        that.$axios({
                            url: that.$global.URL.system.sysUser.updatePhone,
                            method: 'post',
                            data: {
                                id,
                                phone: value
                            },
                            success() {
                                that.$globalFun.successMsg('成功');
                                that.$refs['table'].renderTable();
                                done();
                            }
                        })
                    }
                });
            },
            /**
             * 保存角色
             */
            saveRole() {
                let {chooseRoleIds, formData} = this;
                let that = this;
                this.$axios({
                    url: that.$global.URL.system.sysUser.updateRole,
                    method: 'post',
                    data: {
                        id: formData['id'],
                        roleIds: chooseRoleIds
                    },
                    success() {
                        that.$globalFun.successMsg('成功');
                        that.$refs['table'].renderTable();
                        that.dialogUpdateRole = false;
                    }
                })
            },
            /**
             * 重置密码
             */
            resetPassword({id, realName}) {
                let that = this;
                this.$globalFun.messageBoxInput({
                    message: `请输入${realName}重置后的密码`,
                    inputType: 'password',
                    confirm(value, done) {
                        if (that.$globalFun.isBlank(value)
                            || 6 > value.length) {
                            that.$globalFun.errorMsg('密码不能少于6位');
                            return;
                        }
                        that.$axios({
                            url: that.$global.URL.system.sysUser.resetPwd,
                            method: 'post',
                            data: {
                                id,
                                password: that.$cryptoJS.md5(value)
                            },
                            success() {
                                that.$globalFun.successMsg('密码重置成功');
                                done();
                            }
                        })
                    }
                });
            },
            /**
             * 删除
             */
            deleteUser({id, realName}) {
                let that = this;
                this.$globalFun.messageBox({
                    message: `确定删除 ${realName} 该操作无法撤销`,
                    confirm() {
                        that.$axios({
                            url: that.$global.URL.system.sysUser.delete,
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
            }
        }
    }
</script>