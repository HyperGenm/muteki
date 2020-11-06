<template>
    <my-table :url="table.url"
              :data="table.data"
              :columns="table.columns"
              :rowActionButtons="table.rowActionButtons"
              :headerSearchOpen="true"></my-table>
    <template>
        <a-drawer width="67%"
                  v-model:visible="detail.visible">
            <my-descriptions :title="detail.title"
                             :itemList="detail.itemList"></my-descriptions>
        </a-drawer>
    </template>
</template>

<script>
    import {reactive, defineAsyncComponent} from 'vue';
    import $global from '@/utils/global';
    import {Drawer} from 'ant-design-vue';

    export default {
        name: "Index",
        components: {
            [Drawer.name]: Drawer,
            'my-table': defineAsyncComponent(() => import('@/components/table/Index.vue')),
            'my-descriptions': defineAsyncComponent(() => import('@/components/descriptions/Index.vue')),
        },
        setup() {
            let table = reactive({
                url: $global.url.system.sysError.getPageList,
                data: {},
                columns: [
                    {title: '类名', dataIndex: 'className'},
                    {title: '方法名', dataIndex: 'methodName'},
                    {title: '行数', dataIndex: 'lineNumber'},
                    {title: '详情', dataIndex: 'content', tooltip: true},
                    {title: '备注', dataIndex: 'remark', tooltip: true},
                    {title: '创建时间', dataIndex: 'createTime', width: 120, sorter: true},
                ],
                rowActionButtons: [
                    {
                        name: '详情', icon: 'SearchOutlined', show: true,
                        handleClick(row) {
                            detail.showDetail(row);
                        }
                    }
                ]
            });
            let detail = reactive({
                visible: false,
                title: '',
                itemList: [],
                showDetail(row) {
                    detail.title = row['className'];
                    detail.itemList = [
                        {label: '类名', content: row['className']},
                        {label: '方法名', content: row['methodName']},
                        {label: '行数', content: row['lineNumber']},
                        {label: '详情', content: row['content']},
                        {label: '备注', content: row['remark']},
                        {label: '创建时间', content: row['createTime']},
                    ];
                    detail.visible = true;
                }
            });
            return {
                table,
                detail
            }
        }
    }
</script>