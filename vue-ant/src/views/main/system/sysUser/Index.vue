<template>
    <my-table @myRef="table.myRef"
              :url="table.url"
              :data="table.data"
              :columns="table.columns"
              :headerButtons="table.headerButtons"
              :headerSearchList="table.headerSearchList"
              :rowActionButtons="table.rowActionButtons"></my-table>
    <template>
        <a-drawer width="67%"
                  v-model:visible="detail.visible">
            <my-descriptions :title="detail.title"
                             :itemList="detail.itemList"></my-descriptions>
        </a-drawer>
    </template>
    <template>
        <a-drawer width="67%"
                  title="新增用户"
                  v-model:visible="addUser.visible">
            <my-form :form="addUser.form"
                     :itemList="addUser.itemList"
                     :rules="addUser.rules"
                     @onSubmit="addUser.onSubmit"></my-form>
        </a-drawer>
    </template>
    <template>
        <a-modal title="修改角色"
                 v-model:visible="role.visible"
                 @ok="role.ok">
            <a-select v-model:value="role.value"
                      style="width: 240px;"
                      allowClear mode="multiple">
                <Option v-for="option in role.options"
                        :key="option.value"
                        :value="option.value">
                    {{option.label || option.value}}
                </Option>
            </a-select>
        </a-modal>
    </template>
</template>

<script>
    import {reactive, onMounted, defineAsyncComponent} from 'vue';
    import $global from '@/utils/global';
    import $axios from "../../../../utils/axios";
    import $function from '@/utils/function';
    import $ant from '@/utils/ant';
    import {Drawer, Modal, Input, Select} from 'ant-design-vue';
    import $cryptoJS from '@/utils/cryptoJS';

    export default {
        components: {
            [Drawer.name]: Drawer,
            [Modal.name]: Modal,
            [Input.name]: Input,
            [Select.name]: Select,
            [Select.Option.name]: Select.Option,
            'my-table': defineAsyncComponent(() => import('@/components/table/Index.vue')),
            'my-descriptions': defineAsyncComponent(() => import('@/components/descriptions/Index.vue')),
            'my-form': defineAsyncComponent(() => import('@/components/form/Index.vue')),
        },
        setup() {
            let {
                sysUser_add, sysUser_delete, sysUser_role, sysUser_resetPwd, sysUser_updatePhone, sysUser_updateStatus
            } = $function.getLocationStorage('buttonMap');
            //更新用户状态
            const updateStatus = (checked, row, index) => {
                let status = checked ? 1 : 2;
                $axios({
                    url: $global.url.system.sysUser.updateStatus,
                    method: 'post',
                    data: {
                        id: row['id'],
                        status
                    },
                    success() {
                        $ant.successMsg('更新成功');
                        tableRef['dataSource'][index]['status'] = status;
                    }
                });
            }
            //删除用户
            const deleteUser = (row) => {
                $ant.confirm({
                    content: `确定删除 ${row['realName']} ，该操作无法撤销`,
                    onOk() {
                        $axios({
                            url: $global.url.system.sysUser.delete,
                            method: 'post',
                            data: {
                                id: row['id']
                            },
                            success() {
                                tableRef.getTableData();
                                $ant.successMsg('删除成功');
                            }
                        })
                    }
                });
            };
            //更换手机号
            const updatePhone = (row, index) => {
                $ant.confirm({
                    content: `请输入${row['realName']}要更改的手机号码`,
                    showInput: true,
                    onOk(done, value) {
                        if ($function.notPhone(value)) {
                            $ant.errorMsg('手机号码格式错误');
                            return;
                        }
                        $axios({
                            url: $global.url.system.sysUser.updatePhone,
                            method: 'post',
                            data: {
                                id: row['id'],
                                phone: value
                            },
                            success() {
                                tableRef['dataSource'][index]['phone'] = value;
                                done();
                                $ant.successMsg('修改成功');
                            }
                        })
                    }
                });
            };
            //重置密码
            const resetPassword = (row) => {
                $ant.confirm({
                    content: `请输入${row['realName']}重置后的密码`,
                    showInput: true,
                    inputType: 'password',
                    onOk(done, value) {
                        if ($function.isBlank(value) || 6 > value.length) {
                            $ant.errorMsg('密码不能少于6位');
                            return;
                        }
                        $axios({
                            url: $global.url.system.sysUser.resetPwd,
                            method: 'post',
                            data: {
                                id: row['id'],
                                password: $cryptoJS.md5(value)
                            },
                            success() {
                                done();
                                $ant.successMsg('重置成功');
                            }
                        })
                    }
                });
            }
            //table的ref
            let tableRef = null;
            //表格展示
            let table = reactive({
                myRef(ref) {
                    tableRef = ref;
                },
                url: $global.url.system.sysUser.getPageList,
                data: {
                    createTimeSort: 'DESC'
                },
                columns: [
                    {title: '真实姓名', dataIndex: 'realName', width: 120, fixed: 'left'},
                    {title: '用户名', dataIndex: 'username', width: 120, fixed: 'left'},
                    {title: '角色', dataIndex: 'roleList', width: 120, fixed: 'left'},
                    {title: '手机号', dataIndex: 'phone', width: 120},
                    {
                        title: '状态', type: 'switch',
                        element({status}) {
                            return {
                                checked: 1 === status,
                                disabled: !sysUser_updateStatus,
                                change(checked, row, index) {
                                    updateStatus(checked, row, index);
                                }
                            }
                        },
                    },
                    {
                        title: '头像', dataIndex: 'avatar', type: 'avatar',
                        element({iconAllPath}) {
                            return {
                                src: iconAllPath
                            }
                        }
                    },
                    {title: '用户最后活跃ip地址', dataIndex: 'lastIpAddress'},
                    {title: '用户最后活跃时间', dataIndex: 'lastActiveTime', tooltip: true, sorter: true},
                    {title: '用户创建时间', dataIndex: 'createTime', tooltip: true, sorter: true},
                ],
                headerButtons: [
                    {
                        name: '新增', icon: 'add', show: sysUser_add,
                        handleClick() {
                            addUser.handleClick();
                        }
                    }
                ],
                headerSearchList: [
                    {type: 'input', prop: 'username', placeholder: '用户名'},
                    {
                        type: 'select', prop: 'roleId', placeholder: '角色',
                        options: []
                    },
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
                rowActionButtons: [
                    {
                        name: '详情', icon: 'SearchOutlined', show: true,
                        handleClick(row) {
                            detail.showDetail(row);
                        }
                    },
                    {
                        name: '修改手机号码', icon: 'edit', show: sysUser_updatePhone,
                        handleClick(row, index) {
                            updatePhone(row, index);
                        }
                    },
                    {
                        name: '修改角色', icon: 'edit', show: sysUser_role,
                        handleClick(row, index) {
                            role.nowRow = row;
                            role.visible = true;
                        }
                    },
                    {
                        name: '重置密码', icon: 'edit', show: sysUser_resetPwd,
                        handleClick(row, index) {
                            resetPassword(row, index);
                        }
                    },
                    {
                        name: '删除', icon: 'delete', type: 'danger', show: sysUser_delete,
                        handleClick(row) {
                            deleteUser(row);
                        }
                    }
                ]
            });
            //详情展示
            let detail = reactive({
                visible: false,
                title: '',
                itemList: [],
                form: {},
                showDetail(row) {
                    detail.title = row['realName'];
                    detail.itemList = [
                        {label: '用户名', content: row['username']},
                        {label: '真实姓名', content: row['realName']},
                        {label: '角色', content: row['roleList']},
                        {label: '手机号', content: row['phone']},
                        {label: '状态', content: (1 === row['status'] ? '正常' : '禁用')},
                        {
                            label: '头像', type: 'avatar',
                            element() {
                                return {
                                    src: row['iconAllPath']
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
            //新增
            let addUser = reactive({
                visible: false,
                form: {},
                rules: {
                    password: [
                        {required: true, message: '密码不能为空', trigger: 'blur'},
                        {min: 6, message: '密码不能少于6位', trigger: 'blur'},
                    ]
                },
                itemList: [
                    {type: 'input', label: '用户名', prop: 'username', required: true},
                    {type: 'input', label: '密码', prop: 'password', inputType: 'password', required: true},
                    {type: 'input', label: '真实姓名', prop: 'realName', required: true},
                    {type: 'input', label: '手机号', prop: 'phone', required: true},
                    {
                        type: 'radio', label: '状态', prop: 'status',
                        options: [
                            {label: '正常', value: 1},
                            {label: '禁用', value: 2}
                        ]
                    },
                    {
                        type: 'select', label: '角色', prop: 'roleIds', multiple: true,
                        options: []
                    },
                ],
                handleClick() {
                    addUser.form = {
                        status: 1
                    };
                    addUser.visible = true;
                },
                onSubmit(form) {
                    form['password'] = $cryptoJS.md5(form['password']);
                    $axios({
                        url: $global.url.system.sysUser['add'],
                        method: 'post',
                        data: form,
                        success() {
                            tableRef.getTableData();
                            $ant.successMsg('成功');
                            addUser.visible = false;
                        }
                    });
                }
            });
            //修改角色
            let role = reactive({
                visible: false,
                nowRow: {},
                //当前选中的值
                value: [],
                options: [],
                ok() {
                    $axios({
                        url: $global.url.system.sysUser.updateRole,
                        method: 'post',
                        data: {
                            id: role['nowRow']['id'],
                            roleIds: role.value
                        },
                        success() {
                            tableRef.getTableData();
                            $ant.successMsg('成功');
                            role.visible = false;
                        }
                    })
                }
            });
            onMounted(() => {
                $axios({
                    url: $global.url.system.sysRole.getList,
                    success(data) {
                        let roleList = [];
                        data.forEach(value => {
                            roleList.push({
                                label: value.name,
                                value: value.id
                            });
                        });
                        table['headerSearchList'][1]['options'] = roleList;
                        addUser['itemList'][5]['options'] = roleList;
                        role['options'] = roleList;
                    }
                })
            });
            return {
                table,
                detail,
                addUser,
                role
            }
        }
    };
</script>