<template>
    <my-table @myRef="table.myRef"
              :url="table.url"
              :data="table.data"
              :columns="table.columns"
              :headerButtons="table.headerButtons"
              :rowActionButtons="table.rowActionButtons"
              :defaultExpandAllRows="true" :showIndexColumn="false"></my-table>
    <template>
        <a-drawer width="67%"
                  v-model:visible="detail.visible">
            <my-descriptions :title="detail.title"
                             :itemList="detail.itemList"></my-descriptions>
        </a-drawer>
    </template>
    <template>
        <a-drawer width="67%"
                  :title="`${'add' === editFunction.editType ? '新增功能' : '编辑功能'}`"
                  v-model:visible="editFunction.visible">
            <my-form :form="editFunction.form"
                     :itemList="editFunction.itemList"
                     :rules="editFunction.rules"
                     @onSubmit="editFunction.onSubmit"></my-form>
        </a-drawer>
    </template>
</template>

<script>
    import {reactive, defineAsyncComponent, onMounted, getCurrentInstance, nextTick} from 'vue';
    import $global from '@/utils/global';
    import {Drawer} from 'ant-design-vue';
    import $function from '@/utils/function';
    import $axios from "../../../../utils/axios";
    import $ant from '@/utils/ant';

    export default {
        name: "Index",
        components: {
            [Drawer.name]: Drawer,
            'my-table': defineAsyncComponent(() => import('@/components/table/Index.vue')),
            'my-descriptions': defineAsyncComponent(() => import('@/components/descriptions/Index.vue')),
            'my-form': defineAsyncComponent(() => import('@/components/form/Index.vue')),
        },
        setup() {
            //获取当前实例
            let instance = getCurrentInstance();
            let {
                sysFunction_add, sysFunction_update, sysFunction_delete
            } = $function.getLocationStorage('buttonMap');
            let tableRef = null;
            //表格展示
            let table = reactive({
                myRef(ref) {
                    tableRef = ref;
                },
                url: $global.url.system.sysFunction.getPageList,
                data: {},
                columns: [
                    {title: '标题', dataIndex: 'title'},
                    {title: 'name', dataIndex: 'name'},
                    {
                        title: '图标', dataIndex: 'icon', type: 'icon',
                        element({icon}) {
                            return {
                                icon,
                                defaultIcon: 'InfoCircleOutlined'
                            }
                        }
                    },
                    {title: '路径', dataIndex: 'path', tooltip: true},
                    {
                        title: '类型', dataIndex: 'type', type: 'tag',
                        element({type}) {
                            let result = [
                                null,
                                {content: '菜单', color: 'blue'},
                                {content: '按钮', color: 'green'},
                            ];
                            return result[type] || {
                                content: `未知类型:${type}`,
                                color: 'red'
                            }
                        }
                    },
                    {
                        title: '专属', dataIndex: 'superFlag', type: 'tag',
                        element({superFlag}) {
                            let result = [
                                null,
                                {content: '普通'},
                                {content: 'vip', color: '#f5c36d'},
                            ];
                            return result[superFlag] || {
                                content: `未知类型:${superFlag}`,
                                color: 'red'
                            }
                        }
                    },
                    {
                        title: '内/外部链接', dataIndex: 'externalFlag', type: 'tag',
                        element({externalFlag}) {
                            let result = [
                                null,
                                {content: '内部', type: 'blue'},
                                {content: '外部超链接', color: 'green'},
                            ];
                            return result[externalFlag] || {
                                content: `未知类型:${externalFlag}`,
                                color: 'red'
                            }
                        }
                    },
                    {title: '排序', dataIndex: 'sort'},
                    {title: '拥有api', dataIndex: 'containApi', tooltip: true},
                    {title: '创建时间', dataIndex: 'createTime', width: 120},
                ],
                headerButtons: [
                    {
                        name: '新增', icon: 'add', show: sysFunction_add,
                        handleClick() {
                            editFunction.handleClick();
                        }
                    }
                ],
                rowActionButtons: [
                    {
                        name: '详情', icon: 'SearchOutlined', show: true,
                        handleClick(row) {
                            detail.showDetail(row);
                        }
                    },
                    {
                        name: '编辑', icon: 'edit', show: sysFunction_update,
                        handleClick(row, index) {
                            editFunction.handleEdit(row, index);
                        }
                    },
                    {
                        name: '删除', icon: 'delete', type: 'danger', show: sysFunction_delete,
                        handleClick(row, index) {
                            deleteFunction(row, index);
                        }
                    },
                ]
            });
            let detail = reactive({
                visible: false,
                title: '',
                itemList: [],
                showDetail(row) {
                    detail.title = row['title'];
                    detail.itemList = [
                        {label: '标题', content: row['title']},
                        {label: 'name', content: row['name']},
                        {
                            label: '图标', type: 'icon',
                            element() {
                                return {
                                    icon: row['icon'],
                                    defaultIcon: 'InfoCircleOutlined'
                                }
                            }
                        },
                        {label: '路径', content: row['path']},
                        {
                            label: '类型', type: 'tag',
                            element() {
                                let result = [
                                    null,
                                    {content: '菜单', color: 'blue'},
                                    {content: '按钮', color: 'green'},
                                ];
                                return result[row['type']] || {
                                    content: `未知类型:${row['type']}`,
                                    color: 'red'
                                }
                            }
                        },
                        {
                            label: '专属', type: 'tag',
                            element() {
                                let result = [
                                    null,
                                    {content: '普通'},
                                    {content: 'vip', color: '#f5c36d'},
                                ];
                                return result[row['superFlag']] || {
                                    content: `未知类型:${row['superFlag']}`,
                                    color: 'red'
                                }
                            }
                        },
                        {
                            label: '内/外部链接', type: 'tag',
                            element() {
                                let result = [
                                    null,
                                    {content: '内部', type: 'blue'},
                                    {content: '外部超链接', color: 'green'},
                                ];
                                return result[row['externalFlag']] || {
                                    content: `未知类型:${row['externalFlag']}`,
                                    color: 'red'
                                }
                            }
                        },
                        {label: '排序', content: row['sort']},
                        {label: '拥有api', content: row['containApi']},
                        {label: '创建时间', content: row['createTime']},
                    ];
                    detail.visible = true;
                }
            });
            let editFunction = reactive({
                visible: false,
                editType: 'add',
                form: {},
                itemList: [
                    {type: 'input', label: '标题', prop: 'title', required: true, disabled: false},
                    {type: 'input', label: 'name', prop: 'name', required: true, disabled: false},
                    {
                        type: 'cascader', label: '上级', prop: 'parentId',
                        fieldNames: {label: 'title', value: 'id', children: 'children'},
                        style: 'width:300px',
                        changeOnSelect: true,
                        options: []
                    },
                    {type: 'input', label: '路径', prop: 'path', required: true},
                    {
                        type: 'radio', label: '类型', prop: 'type',
                        options: [
                            {label: '菜单', value: 1},
                            {label: '按钮', value: 2}
                        ]
                    },
                    {
                        type: 'radio', label: '专属', prop: 'superFlag',
                        options: [
                            {label: '普通', value: 1},
                            {label: 'vip', value: 2}
                        ]
                    },
                    {
                        type: 'radio', label: '内/外部', prop: 'externalFlag',
                        options: [
                            {label: '内部', value: 1},
                            {label: '外部超链接', value: 2}
                        ]
                    },
                    {type: 'input', label: '排序', prop: 'sort', inputType: 'number'},
                    {type: 'textarea', label: '备注', prop: 'description'},
                ],
                handleClick() {
                    editFunction.editType = 'add';
                    editFunction.form = {
                        type: 1,
                        superFlag: 1,
                        externalFlag: 1,
                    };
                    editFunction.itemList[0]['disabled'] = false;
                    editFunction.itemList[1]['disabled'] = false;
                    editFunction.visible = true;
                },
                handleEdit(row, index) {
                    editFunction.editType = 'update';
                    editFunction.itemList[0]['disabled'] = true;
                    editFunction.itemList[1]['disabled'] = true;
                    //级联选择器是个数组
                    row['parentId'] = [row['parentId']];
                    editFunction.form = row;
                    editFunction.visible = true;
                },
                onSubmit(form) {
                    //`el-select`级联选择器是个数组
                    let {parentId} = form;
                    if (Array.isArray(parentId)) {
                        let max = 0;
                        parentId.forEach(value => {
                            max = max > value ? max : value;
                        });
                        form['parentId'] = max;
                    }
                    $axios({
                        url: $global.url.system.sysFunction[editFunction.editType],
                        method: 'post',
                        data: form,
                        success() {
                            tableRef.getTableData();
                            $ant.successMsg('成功');
                            editFunction.visible = false;
                        }
                    });
                }
            });
            //删除功能
            let deleteFunction = (row, index) => {
                $ant.confirm({
                    content: `确定删除 ${row['title']},该操作无法撤销`,
                    onOk() {
                        $axios({
                            url: $global.url.system.sysFunction.delete,
                            method: 'post',
                            data: {
                                id: row['id']
                            },
                            success() {
                                tableRef.getTableData();
                                $ant.successMsg('删除成功');
                                editFunction.visible = false;
                            }
                        })
                    }
                });
            }
            onMounted(() => {
                //获取功能树形结构
                $axios({
                    url: $global.url.system.sysFunction.getTree,
                    success(data) {
                        editFunction.itemList[2]['options'] = data;
                    }
                })
            });
            return {
                table,
                detail,
                editFunction
            }
        }
    }
</script>