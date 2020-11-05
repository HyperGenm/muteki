<template>
    <a-timeline>
        <a-timeline-item v-for="item in fileList" :key="item.name">
            <div>{{item.name}}</div>
            <a-table :columns="columns"
                     :data-source="item.children"
                     size="small" rowKey="name" bordered
                     :pagination="false">
                <template v-slot:action="{ text, record }">
                    <a-button type="primary" size="small"
                              @click="handleClick(record,item)">下载
                    </a-button>
                </template>
            </a-table>
        </a-timeline-item>
    </a-timeline>
</template>

<script>
    import {Timeline, Table, Button} from 'ant-design-vue';
    import {onMounted, ref} from 'vue';
    import $axios from '@/utils/axios';
    import {$axiosDown} from "@/utils/axios";
    import $ant from '@/utils/ant';
    import $global from '@/utils/global';

    export default {
        name: "Index",
        components: {
            [Table.name]: Table,
            [Timeline.name]: Timeline,
            [Timeline.Item.name]: Timeline.Item,
            [Button.name]: Button,
        },
        setup() {
            let fileList = ref([]);
            let columns = ref([
                {title: '文件名', dataIndex: 'name'},
                {title: '大小(字节)', dataIndex: 'length'},
                {
                    title: '操作', dataIndex: '_index',
                    slots: {customRender: 'action'}
                },
            ]);
            onMounted(() => {
                $axios({
                    url: $global.url.system.sysFile.getLogFile,
                    success(data = []) {
                        data.sort((a, b) => {
                            return a.name < b.name ? 1 : -1;
                        });
                        fileList.value = data.concat(data);
                    }
                });
            });
            let handleClick = ({name}, item) => {
                $ant.confirm({
                    content: `确定下载 ${name}`,
                    onOk() {
                        $axiosDown({
                            url: $global.url.system.sysFile.downLogFile,
                            data: {
                                dir: item.name,
                                name
                            },
                            filename: name
                        })
                    }
                });
            }
            return {
                fileList,
                columns,
                handleClick
            }
        }
    }
</script>