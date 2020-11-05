<template>
    <div class="box">
        <div class="table">
            <my-table :ref="table.ref"
                      :rowSelection="table.rowSelection"
                      :url="table.url"
                      :data="table.data"
                      :columns="table.columns"
                      :headerButtons="table.headerButtons"
                      :rowActionButtons="table.rowActionButtons"
                      :headerSearchOpen="true"></my-table>
        </div>
        <div class="list">
            <a-divider orientation="left">拥有权限</a-divider>
            <a-button type="primary"
                      style="float: right;"
                      @click="saveRole">保存
            </a-button>
            <a-tree ref="treeRef"
                    checkable defaultExpandAll
                    :tree-data="tree.treeData"
                    :replaceFields="tree.replaceFields"
                    v-model:checkedKeys="tree.checkedKeys"
                    @check="tree.check">
            </a-tree>
        </div>
    </div>
    <template>
        <a-drawer width="67%"
                  :title="`${'add' === editRole.editType ? '新增角色' : '编辑角色'}`"
                  v-model:visible="editRole.visible">
            <my-form :form="editRole.form"
                     :itemList="editRole.itemList"
                     @onSubmit="editRole.onSubmit"></my-form>
        </a-drawer>
    </template>
</template>

<script>
    import {reactive, onMounted, ref, defineAsyncComponent, getCurrentInstance} from 'vue';
    import $global from '@/utils/global';
    import {Divider, Tree, Button, Drawer, Descriptions} from 'ant-design-vue';
    import $axios from "../../../../utils/axios";
    import $ant from '@/utils/ant';
    import $function from '@/utils/function';

    export default {
        name: "Index",
        components: {
            [Divider.name]: Divider,
            [Tree.name]: Tree,
            [Button.name]: Button,
            [Drawer.name]: Drawer,
            [Descriptions.name]: Descriptions,
            [Descriptions.Item.name]: Descriptions.Item,
            'my-table': defineAsyncComponent(() => import('@/components/table/Index.vue')),
            'my-form': defineAsyncComponent(() => import('@/components/form/Index.vue')),
        },
        setup() {
            let {
                sysRole_add, sysRole_update, sysRole_delete, sysRole_save, sysRole_status
            } = $function.getLocationStorage('buttonMap');
            //更新角色状态
            const updateStatus = (checked, row, index) => {
                let status = checked ? 1 : 2;
                $axios({
                    url: $global.url.system.sysRole.updateStatus,
                    method: 'post',
                    data: {
                        id: row['id'],
                        status
                    },
                    success() {
                        $ant.successMsg('更新成功');
                        $ref['table']['dataSource'][index]['status'] = status;
                    }
                });
            }
            let $ref = {};
            let table = reactive({
                ref(ref) {
                    $ref['table'] = ref;
                },
                //当前选择的角色
                chooseRole: {},
                rowSelection: {
                    type: 'radio',
                    nowRoleId: null,
                    onChange(keys, rows) {
                        table['chooseRole'] = rows[0];
                        $axios({
                            url: $global.url.system.sysRole.getFunctionList,
                            data: {
                                id: keys[0]
                            },
                            success(data) {
                                //排除掉父级节点id
                                let {parentFunctionList} = tree;
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
                                tree.checkedKeys = data;
                                console.log(`如果提示  Warning: '24' does not exist in the tree 请忽略,应该不影响使用`);
                            }
                        });
                    }
                },
                url: $global.url.system.sysRole.getPageList,
                data: {},
                columns: [
                    {title: '名称', dataIndex: 'name', tooltip: true, fixed: 'left'},
                    {
                        title: '状态', dataIndex: 'status', type: 'switch',
                        element({status}) {
                            return {
                                checked: 1 === status,
                                disabled: !sysRole_status,
                                change(checked, row, index) {
                                    updateStatus(checked, row, index);
                                }
                            }
                        }
                    },
                    {title: '排序', dataIndex: 'sort'},
                    {title: '备注', dataIndex: 'remark', width: 120, tooltip: true},
                    {title: '创建时间', dataIndex: 'createTime', width: 120},
                ],
                headerButtons: [
                    {
                        name: '新增', icon: 'add', show: sysRole_add,
                        handleClick() {
                            editRole.addClick();
                        }
                    }
                ],
                rowActionButtons: [
                    {
                        name: '编辑', icon: 'edit', show: sysRole_update,
                        handleClick(row, index) {
                            editRole.editClick(row);
                        }
                    },
                    {
                        name: '删除', icon: 'delete', type: 'danger', show: sysRole_delete,
                        handleClick(row) {
                            $ant.confirm({
                                content: `确定删除 ${row['name']},该操作无法撤销`,
                                onOk() {
                                    $axios({
                                        url: $global.url.system.sysRole.delete,
                                        method: 'post',
                                        data: {
                                            id: row['id']
                                        },
                                        success() {
                                            $ref.table.getTableData();
                                            $ant.successMsg('删除成功');
                                        }
                                    })
                                }
                            });
                        }
                    }
                ]
            });
            //右侧的角色树
            let tree = reactive({
                ref(ref) {
                    $ref['tree'] = ref;
                },
                //树形数据
                treeData: [],
                //所有的父级节点id
                parentFunctionList: [],
                //替换 treeNode 中 title,key,children 字段为 treeData 中对应的字段
                replaceFields: {
                    key: 'id'
                },
                //选中的
                checkedKeys: [],
                //当前选中的父级节点
                halfCheckedKeys: [],
                //点击复选框触发
                check(checkedKeys, event) {
                    tree.halfCheckedKeys = event.halfCheckedKeys;
                }
            });
            //保存权限
            const saveRole = () => {
                if (!sysRole_save) {
                    $ant.errorMsg('您没有权限');
                    return;
                }
                let functionIds = tree.checkedKeys.concat(tree.halfCheckedKeys);
                $axios({
                    url: $global.url.system.sysRole.updateRoleFunction,
                    method: 'post',
                    data: {
                        roleId: table['chooseRole']['id'],
                        functionIds
                    },
                    success() {
                        $ant.successMsg('修改成功');
                    }
                });
            };
            //新增角色
            let editRole = reactive({
                visible: false,
                editType: 'add',
                form: {},
                itemList: [
                    {type: 'input', label: '名称', prop: 'name', required: true, disabled: true},
                    {
                        type: 'radio', label: '状态', prop: 'status',
                        options: [
                            {label: '正常', value: 1},
                            {label: '禁用', value: 2}
                        ]
                    },
                    {type: 'input', label: '排序', prop: 'sort'},
                    {type: 'textarea', label: '备注', prop: 'remark'},
                ],
                addClick() {
                    editRole.editType = 'add';
                    editRole.form = {
                        status: 1
                    };
                    editRole.itemList[0]['disabled'] = false;
                    editRole.visible = true;
                },
                editClick(row) {
                    editRole.editType = 'update';
                    editRole.form = row;
                    editRole.itemList[0]['disabled'] = true;
                    editRole.visible = true;
                },
                onSubmit(form) {
                    $axios({
                        url: $global.url.system.sysRole[editRole.editType],
                        method: 'post',
                        data: form,
                        success() {
                            $ref.table.getTableData();
                            $ant.successMsg('添加成功');
                        }
                    })
                }
            });
            onMounted(() => {
                    /**
                     *找到所有的父级节点id
                     */
                    const findParentFunctionList = (data) => {
                        let result = [];
                        data.forEach(value => {
                            if (null !== value.children && 0 < value.children.length) {
                                result.push(value['id']);
                                let children = findParentFunctionList(value['children']);
                                result = result.concat(children);
                            }
                        });
                        return result;
                    };
                    //获取树
                    $axios({
                        url: $global.url.system.sysFunction.getTree,
                        success(data) {
                            //找到所有的父级节点id
                            let parentFunctionList = [];
                            data.forEach(value => {
                                if (null !== value.children && 0 < value.children.length) {
                                    parentFunctionList.push(value.id);
                                    let children = findParentFunctionList(value['children']);
                                    parentFunctionList = parentFunctionList.concat(children);
                                }
                            });
                            tree['treeData'] = data;
                            tree['parentFunctionList'] = parentFunctionList;
                        }
                    });
                }
            )
            ;
            return {
                table,
                tree,
                saveRole,
                editRole
            }
        }
    }
</script>

<style lang="less" scoped>
    .box {
        display: flex;
        height: 100%;

        .table {
            flex: 3;
        }

        .list {
            border-left: 1px solid #777;
            flex: 2;
            margin-left: 10px;
        }
    }
</style>