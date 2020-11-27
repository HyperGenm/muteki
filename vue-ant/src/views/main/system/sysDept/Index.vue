<template>
    <div class="box">
        <div class="list">
            <a-tree ref="treeRef"
                    defaultExpandAll
                    :tree-data="tree.treeData"
                    :replaceFields="tree.replaceFields"
                    v-model:checkedKeys="tree.checkedKeys"
                    @select="tree.select">
            </a-tree>
        </div>
        <div class="table">
            <my-table @myRef="table.myRef"
                      :url="table.url"
                      :data="table.data"
                      :columns="table.columns"
                      :headerButtons="table.headerButtons"
                      :rowActionButtons="table.rowActionButtons"></my-table>
        </div>
    </div>
    <template>
        <a-drawer width="67%"
                  :title="`${'add' === editDept.type ? '新增' : '编辑'}`"
                  v-model:visible="editDept.visible">
            <my-form :form="editDept.form"
                     :itemList="editDept.itemList"
                     :rules="editDept.rules"
                     @onSubmit="editDept.onSubmit">
                <template v-slot:internalSlot>
                    <a-form-item label="上级菜单">
                        <a-cascader v-model:value="editDept.chooseParentIds"
                                    allowClear showSearch changeOnSelect
                                    :options="tree.treeData"
                                    :fieldNames="{label:'name',value:'id'}"
                                    placeholder="请选择"/>
                    </a-form-item>
                </template>
            </my-form>
        </a-drawer>
    </template>
</template>

<script>
    import {reactive, onMounted, defineAsyncComponent} from 'vue';
    import $global from '@/utils/global';
    import {Divider, Tree, Button, Drawer, Descriptions, Cascader, Form} from 'ant-design-vue';
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
            [Form.Item.name]: Form.Item,
            [Cascader.name]: Cascader,
            'my-table': defineAsyncComponent(() => import('@/components/table/Index.vue')),
            'my-form': defineAsyncComponent(() => import('@/components/form/Index.vue')),
        },
        setup() {
            let {
                sysDept_add, sysDept_update, sysDept_delete
            } = $function.getLocationStorage('buttonMap');
            let $ref = {};
            let table = reactive({
                myRef(ref) {
                    $ref['table'] = ref;
                },
                url: $global.url.system.sysDept.getPageList,
                data: {},
                columns: [
                    {title: '名称', dataIndex: 'name', width: 80, tooltip: true, fixed: 'left'},
                    {title: '上级', dataIndex: 'parentName'},
                    {title: '备注', dataIndex: 'remark', width: 120, tooltip: true},
                    {title: '排序', dataIndex: 'sort'},
                    {title: '编辑人', dataIndex: 'editRealName'},
                    {title: '创建时间', dataIndex: 'createTime', width: 120},
                ],
                headerButtons: [
                    {
                        name: '新增', icon: 'add', show: sysDept_add,
                        handleClick() {
                            editDept.addClick();
                        }
                    }
                ],
                rowActionButtons: [
                    {
                        name: '修改', icon: 'edit', show: sysDept_update,
                        handleClick(row, index) {
                            editDept.updateClick(row, index);
                        }
                    },
                    {
                        name: '删除', icon: 'delete', type: 'danger', show: sysDept_delete,
                        handleClick(row) {
                            $ant.confirm({
                                content: `确定删除 ${row['name']},该操作无法撤销`,
                                onOk() {
                                    $axios({
                                        url: $global.url.system.sysDept.delete,
                                        method: 'post',
                                        data: {
                                            id: row['id']
                                        },
                                        success() {
                                            getDeptTree();
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
            //左侧的角色树
            let tree = reactive({
                ref(ref) {
                    $ref['tree'] = ref;
                },
                //树形数据
                treeData: [],
                //替换 treeNode 中 title,key,children 字段为 treeData 中对应的字段
                replaceFields: {
                    key: 'id',
                    title: 'name'
                },
                //点击复选框触发
                select(selectedKeys, e) {
                    table['data']['parentId'] = selectedKeys[0];
                    $ref.table.getTableData();
                }
            });
            //编辑
            let editDept = reactive({
                visible: false,
                type: 'add',
                form: {},
                rules: {},
                //选中的上级id
                chooseParentIds: [],
                itemList: [
                    {type: 'input', label: '名称', prop: 'name', required: true},
                    {slot: true},
                    {type: 'input', label: '排序', prop: 'sort', inputType: 'number'},
                    {type: 'input', label: '备注', prop: 'remark'},
                ],
                addClick() {
                    editDept.type = 'add';
                    editDept.form = {
                        sort: 0,
                        parentId: [0],
                    };
                    editDept.visible = true;
                },
                updateClick(row, index) {
                    editDept.type = 'update';
                    editDept.form = row;
                    editDept.chooseParentIds = [row['id']];
                    editDept.visible = true;
                },
                /**
                 * 提交
                 */
                onSubmit(form) {
                    form['parentId'] = editDept.chooseParentIds[0];
                    $axios({
                        url: $global.url.system.sysDept[editDept.type],
                        method: 'post',
                        data: form,
                        success() {
                            getDeptTree();
                            $ref.table.getTableData();
                            $ant.successMsg('操作成功');
                            editDept.visible = false;
                        }
                    })
                }
            });
            //获取部门树
            let getDeptTree = () => {
                //获取树
                $axios({
                    url: $global.url.system.sysDept.getTree,
                    success(data) {
                        tree.treeData = [{
                            id: 0,
                            name: '顶级',
                            children: data
                        }];
                    }
                });
            }
            onMounted(() => {
                getDeptTree();
            })
            ;
            return {
                table,
                tree,
                editDept
            }
        }
    }
</script>

<style lang="less" scoped>
    .box {
        display: flex;
        height: 100%;

        .list {
            flex: 2;
            margin-left: 10px;
            border-right: 1px solid #777;
        }

        .table {
            flex: 3;
        }

    }
</style>