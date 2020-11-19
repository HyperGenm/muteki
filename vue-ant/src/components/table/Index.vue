<template>
    <div id="wei-table"
         :ref="refs.box">
        <div class="header-search-list"
             :ref="refs.headerSearchList"
             v-if="null != headerSearchList && 0 < headerSearchList.length">
            <div v-for="item in headerSearchList" :key="item.prop"
                 class="item">
                <template v-if="'slot' === item.type">
                    <slot name="internalHeaderSearchList"></slot>
                </template>
                <template v-else>
                    <a-form-item :label="item.label">
                        <template v-if="'input' === item.type">
                            <a-input v-model:value="data[item.prop]" allowClear
                                     :placeholder="item.placeholder || '请输入'"/>
                        </template>
                        <template v-else-if="'select' === item.type">
                            <a-select v-model:value="data[item.prop]"
                                      :placeholder="item.placeholder || '请选择'"
                                      allowClear showSearch
                                      :style="item.style || 'width:200px;'"
                                      :mode="item.mode">
                                <!--使用 a-select-option 会爆警告-->
                                <Option v-for="option in item.options"
                                        :value="option.value">
                                    {{option.label || option.value}}
                                </Option>
                            </a-select>
                        </template>
                        <template v-else-if="'datePicker' === item.type">
                            <a-date-picker v-model:value="data[item.prop]"
                                           :placeholder="item.placeholder || '请选择'"
                                           :valueFormat="item.valueFormat || 'YYYY-MM-DD'"
                                           allowClear/>
                        </template>
                        <template v-else-if="'timePicker' === item.type">
                            <a-time-picker v-model:value="data[item.prop]"
                                           :placeholder="item.placeholder || '请选择'"
                                           :valueFormat="item.valueFormat || 'HH:mm:ss'"
                                           allowClear/>
                        </template>
                        <template v-else>
                            <div style="color: #f40;">{{item.label}}没有指定type</div>
                        </template>
                    </a-form-item>
                </template>
            </div>
            <div class="item">
                <a-button style="margin-right: 20px;"
                          shape="round" type="primary"
                          @click="resetHeaderForm">
                    <template v-slot:icon>
                        <wei-icon icon="UndoOutlined"></wei-icon>
                    </template>
                    重置
                </a-button>
            </div>
            <div class="item">
                <a-button shape="round" type="primary"
                          @click="getTableData">
                    <template v-slot:icon>
                        <wei-icon icon="RedoOutlined"></wei-icon>
                    </template>
                    刷新
                </a-button>
            </div>
        </div>
        <div class="header-button"
             :ref="refs.headerButton">
            <div class="btn">
                <a-button shape="round" type="primary"
                          @click="getTableData">
                    <template v-slot:icon>
                        <wei-icon icon="RedoOutlined"></wei-icon>
                    </template>
                    刷新
                </a-button>
                <a-button v-for="btn in realHeaderButtons" :key="btn.name"
                          :shape="btn.shape || 'round'"
                          :type="btn.type || 'primary'"
                          :style="`color:${btn.color};`"
                          @click="btn.handleClick(JSON.parse(JSON.stringify(selectedRows)))">
                    <!--如果有图标,图标自定义需手动引入-->
                    <template v-slot:icon>
                        <template v-if="'add' === btn.icon">
                            <wei-icon icon="PlusOutlined"></wei-icon>
                        </template>
                        <template v-else-if="'edit' === btn.icon">
                            <wei-icon icon="EditOutlined"></wei-icon>
                        </template>
                        <template v-else-if="'DeleteOutlined' === btn.icon">
                            <wei-icon icon="DeleteOutlined"></wei-icon>
                        </template>
                        <template v-else>
                            <wei-icon :icon="btn.icon" defaultIcon="InfoCircleOutlined"></wei-icon>
                        </template>
                    </template>
                    {{btn.name}}
                </a-button>
            </div>
            <div class="search">
                <a-input-search v-if="headerSearchOpen"
                                v-model:value="headerSearch.value"
                                placeholder="请输入要搜索的内容"
                                enter-button allowClear
                                @search="headerSearch.search"/>
            </div>
        </div>
        <a-table :style="`overflow-y: scroll;height: ${tableMaxHeight}px;`"
                 :data-source="dataSource"
                 :bordered="bordered"
                 :childrenColumnName="childrenColumnName"
                 :defaultExpandAllRows="defaultExpandAllRows"
                 :size="size"
                 :pagination="false"
                 :loading="loading"
                 :locale="locale"
                 :rowKey="rowKey"
                 :scroll="{x:true}"
                 :rowSelection="realRowSelection"
                 @change="change">
            <!--表格header-->
            <template v-slot:title="currentPageData">
                <slot name="title" :rows="currentPageData"></slot>
            </template>
            <a-table-column v-if="showIndexColumn"
                            data-index="_index" fixed="left"
                            title="序号" width="50px">
                <template v-slot="{index}">
                    {{index + 1}}
                </template>
            </a-table-column>
            <!--表格内容-->
            <template v-for="column in columns" :key="column.key">
                <!--单元格某一列中插入插槽-->
                <template v-if="'slot' === column.key">
                    <slot name="internalColumn"></slot>
                </template>
                <template v-else>
                    <a-table-column :align="column.align"
                                    :ellipsis="null != column.ellipsis ? columns.ellipsis : true"
                                    :data-index="column.dataIndex"
                                    :fixed="column.fixed"
                                    :key="column.key"
                                    :sorter="column.sorter"
                                    :sortOrder="column.sortOrder"
                                    :class="column.class"
                                    :width="column.width">
                        <!--表头，添加文字提示-->
                        <template #title>
                            <a-tooltip :title="column.title" arrow-point-at-center>
                                <span>{{column.title}}</span>
                            </a-tooltip>
                        </template>
                        <!--单元格-->
                        <template v-slot="{ text, record, index }">
                            <!--使用自定义属性时必须返回 column.element() 对象({})
                            属性详情请看官网: https://2x.antdv.com/-->
                            <template v-if="'avatar' === column.type">
                                <a-avatar :src="column.element(record)['src']"
                                          :style="column.element(record)['style']"
                                          :shape="column.element(record)['shape']"
                                          :size="column.element(record)['size']"
                                          :alt="column.element(record)['alt']">
                                    {{column.element(record)['content']}}
                                </a-avatar>
                            </template>
                            <template v-else-if="'tag' === column.type">
                                <a-tag :color="column.element(record)['color']">
                                    {{column.element(record)['content']}}
                                </a-tag>
                            </template>
                            <template v-else-if="'icon' === column.type">
                                <wei-icon :icon="column.element(record)['icon']"
                                          :defaultIcon="column.element(record)['defaultIcon']"></wei-icon>
                            </template>
                            <template v-else-if="'statistic' === column.type">
                                <a-statistic :title="column.element(record)['title']"
                                             :value="column.element(record)['value']"
                                             :style="column.element(record)['style']"
                                             :prefix="column.element(record)['prefix']"
                                             :suffix="column.element(record)['suffix']"
                                             :valueStyle="column.element(record)['valueStyle']"/>
                            </template>
                            <template v-else-if="'switch' === column.type">
                                <a-switch :checked="column.element(record)['checked']"
                                          :disabled="column.element(record)['disabled']"
                                          :checked-children="column.element(record)['checkedChildren']"
                                          :un-checked-children="column.element(record)['unCheckedChildren']"
                                          :size="column.element(record)['size']"
                                          @change="column.element(record)['change']($event,record,index)"/>
                            </template>
                            <template v-else>
                                <!--需要处理元素———:formatter=""-->
                                <template v-if="column.formatter">
                                    <template v-if="column.tooltip">
                                        <a-tooltip autoAdjustOverflow
                                                   :title="column.formatter(record)">
                                            <div v-html="column.formatter(record)"></div>
                                        </a-tooltip>
                                    </template>
                                    <template v-else>
                                        <div v-html="column.formatter(record)"></div>
                                    </template>
                                </template>
                                <!--普通文字-->
                                <template v-else>
                                    <!--文字气泡展示-->
                                    <template v-if="column.tooltip">
                                        <a-tooltip autoAdjustOverflow
                                                   :title="text">
                                            {{text}}
                                        </a-tooltip>
                                    </template>
                                    <template v-else>
                                        {{text}}
                                    </template>
                                </template>
                            </template>
                        </template>
                    </a-table-column>
                </template>
            </template>
            <!--表格行操作-->
            <a-table-column v-if="null != rowActionButtons && 0 < rowActionButtons.length"
                            key="action" title="操作" size="small"
                            width="120px" fixed="right">
                <template v-slot="{ text, record, index }">
                    <template v-if="isShowRowActionButtonsPopover(record)">
                        <a-button v-for="btn in realRowActionButtons(record)" :key="btn.name"
                                  :shape="btn.shape || 'round'"
                                  :type="btn.type || 'primary'"
                                  @click="btn.handleClick(JSON.parse(JSON.stringify(record)),index)">
                            <!--如果有图标,图标自定义需手动引入-->
                            <template v-slot:icon>
                                <template v-if="'add' === btn.icon">
                                    <wei-icon icon="PlusOutlined"></wei-icon>
                                </template>
                                <template v-else-if="'edit' === btn.icon">
                                    <wei-icon icon="EditOutlined"></wei-icon>
                                </template>
                                <template v-else-if="'DeleteOutlined' === btn.icon">
                                    <wei-icon icon="DeleteOutlined"></wei-icon>
                                </template>
                                <template v-else>
                                    <wei-icon :icon="btn.icon" defaultIcon="InfoCircleOutlined"></wei-icon>
                                </template>
                            </template>
                            {{btn.name}}
                        </a-button>
                    </template>
                    <template v-else>
                        <a-popover placement="left">
                            <template v-slot:content>
                                <div v-for="btn in realRowActionButtons(record)" :key="btn.name"
                                     style="margin: 10px auto;">
                                    <a-button :shape="btn.shape || 'round'"
                                              :type="btn.type || 'primary'"
                                              @click="btn.handleClick(JSON.parse(JSON.stringify(record)),index)">
                                        <!--如果有图标,图标自定义需手动引入-->
                                        <template v-slot:icon>
                                            <template v-if="'add' === btn.icon">
                                                <wei-icon icon="PlusOutlined"></wei-icon>
                                            </template>
                                            <template v-else-if="'edit' === btn.icon">
                                                <wei-icon icon="EditOutlined"></wei-icon>
                                            </template>
                                            <template v-else-if="'DeleteOutlined' === btn.icon">
                                                <wei-icon icon="DeleteOutlined"></wei-icon>
                                            </template>
                                            <template v-else>
                                                <wei-icon :icon="btn.icon" defaultIcon="InfoCircleOutlined"></wei-icon>
                                            </template>
                                        </template>
                                        {{btn.name}}
                                    </a-button>
                                </div>
                            </template>
                            <a-button type="primary">
                                更多
                            </a-button>
                        </a-popover>
                    </template>
                </template>
            </a-table-column>
            <!--表格footer-->
            <template v-slot:footer="currentPageData">
                <slot name="footer" :rows="currentPageData"></slot>
            </template>
        </a-table>
        <div :ref="refs.pagination" class="pagination">
            <a-pagination v-model:current="pagination.current"
                          v-model:pageSize="pagination.pageSize"
                          :total="pagination.total"
                          show-size-changer show-quick-jumper
                          @change="pagination.change"
                          @showSizeChange="pagination.showSizeChange"/>
        </div>
    </div>
</template>

<script>
    import {
        Table,
        Tooltip,
        Pagination,
        Avatar,
        Statistic,
        Switch,
        Popover,
        Space,
        DatePicker,
        TimePicker,
        Button,
        Select,
        Input,
        Form,
        Tag,
    } from 'ant-design-vue';
    import $axios from 'axios';
    /**引入参数处理*/
    import Qs from 'qs';
    import {ref, reactive, onMounted, nextTick, computed, defineAsyncComponent} from 'vue';
    import $ant from "../../utils/ant";
    //引入router
    import {useRouter} from 'vue-router';
    import $function from "../../utils/function";

    export default {
        name: "WeiTable",
        components: {
            [Table.name]: Table,
            [Table.Column.name]: Table.Column,
            [Tooltip.name]: Tooltip,
            [Pagination.name]: Pagination,
            [Avatar.name]: Avatar,
            [Statistic.name]: Statistic,
            [Switch.name]: Switch,
            [Popover.name]: Popover,
            [Space.name]: Space,
            [DatePicker.name]: DatePicker,
            [TimePicker.name]: TimePicker,
            [Button.name]: Button,
            [Select.name]: Select,
            [Select.Option.name]: Select.Option,
            [Input.name]: Input,
            [Input.Search.name]: Input.Search,
            [Form.Item.name]: Form.Item,
            [Tag.name]: Tag,
            'wei-icon': defineAsyncComponent(() => import('@/components/icon/Index.vue')),
        },
        props: {
            //表格列的配置描述
            columns: {
                type: Array,
                default: () => ([])
            },
            //请求的地址
            url: {
                type: String
            },
            //请求方式
            method: {
                type: String,
                default: 'get',
            },
            //请求的参数
            data: {
                type: Object,
                default: () => ({})
            },
            //请求头
            contentType: {
                type: String,
                default: 'application/x-www-form-urlencoded; charset=UTF-8'
            },
            //表格顶部搜索条件
            //要使用此条件，需设置 响应式的 props['data']
            headerSearchList: {
                type: Array,
                default: () => ([])
            },
            //表格顶部按钮
            headerButtons: {
                type: Array,
                default: () => ([])
            },
            //顶部搜索框
            headerSearchOpen: {
                type: Boolean,
                default: false
            },
            //行操作按钮
            rowActionButtons: {
                type: Array,
                default: () => ([])
            },
            //表格行 key 的取值，可以是字符串或一个函数
            rowKey: {
                type: String,
                default: 'id'
            },
            //是否展示外边框和列边框
            bordered: {
                type: Boolean,
                default: true
            },
            //指定树形结构的列名
            childrenColumnName: {
                type: String,
                default: 'children'
            },
            //初始时，是否展开所有行
            defaultExpandAllRows: {
                type: Boolean,
                default: false
            },
            //表格大小
            size: {
                type: String,
                default: 'small'
            },
            //表格左侧 选择功能的配置。
            rowSelection: {
                type: Object,
                default: ({})
            },
            //左侧的索引
            showIndexColumn: {
                type: Boolean,
                default: true
            }
        },
        mounted() {
            //对外抛出自身
            this.$emit('my-ref', this);
        },
        setup(props, content) {
            const $router = useRouter();
            //展示的数据
            let dataSource = ref([]);
            //分页
            let pagination = reactive({
                //当前页数
                current: 1,
                //每条页数
                pageSize: 10,
                //指定每页可以显示多少条
                pageSizeOptions: ['10', '20', '30', '40'],
                //是否可以改变 pageSize
                showSizeChanger: false,
                //数据总数
                total: 0,
                //页码改变时触发
                change(page, pageSize) {
                    pagination.current = page;
                    pagination.pageSize = pageSize;
                    getTableData();
                },
                //每页条数改变时触发
                showSizeChange(current, pageSize) {
                    pagination.current = 1;
                    pagination.pageSize = pageSize;
                    getTableData();
                }
            });
            //加载中
            let loading = ref(false);
            //默认文案设置，目前包括排序、过滤、空数据文案
            let locale = reactive({
                emptyText: '暂无数据'
            });
            //获取表格数据
            const getTableData = () => {
                //开启加载中动画
                loading.value = true;
                let data = props['data'] || {};
                data['pageNum'] = pagination.current;
                data['pageSize'] = pagination.pageSize;
                data['__t'] = (new Date()).getTime();
                let axios = {
                    url: (process.env.VUE_APP_URL + props['url']),
                    method: props['method'],
                    headers: {
                        'Content-Type': props['contentType'],
                        'token': $function.getLocationStorage('token')
                    },
                    timeout: 20000
                }
                if ('GET' === props['method'].toUpperCase()) {
                    axios['params'] = data;
                } else {
                    axios['data'] = Qs.stringify(data, {indices: false});
                }
                $axios(axios).then((res) => {
                    loading.value = false;
                    let {code, msg} = res['data'];
                    if (401 === code) {
                        $ant.errorMsg('登陆过期，自动登录中。。。');
                        let timer = setTimeout(() => {
                            clearTimeout(timer);
                            $router.replace('/login');
                        }, 3000);
                        return;
                    }
                    if (200 !== code) {
                        $ant.errorMsg(msg);
                        try {
                            let res = res['data'];
                            res['url'] = props['url'];
                            console.table(res);
                        } catch (e) {
                            console.log(`表格请求出错:url:${props['url']},详情:${JSON.stringify(res['data'])}`);
                        }
                        locale.emptyText = JSON.stringify(res['data']);
                        return;
                    }
                    try {
                        let {list, pageNum, pageSize, total} = res['data']['data'];
                        dataSource.value = list;
                        //如果没有数据
                        if (null == list) {
                            locale.emptyText = '返回格式错误，示例:{"list":[],"pageNum":1,"pageSize":10}';
                        }
                        if (0 >= list.length) {
                            locale.emptyText = '暂无数据';
                            return;
                        }
                        pagination['current'] = pageNum;
                        pagination['total'] = total;
                    } catch (e) {
                        locale.emptyText = '返回格式错误，示例:{"list":[],"pageNum":1,"pageSize":10}';
                    }
                }).catch(error => {
                    loading.value = false;
                    locale.emptyText = error['message'];
                    console.warn(`表格请求异常:url:${props['url']},详情:`, error);
                });
            };
            //左侧多选按钮
            const realRowSelection = {
                //自定义列表选择框宽度
                columnWidth: props['rowSelection']['columnWidth'] || 50,
                //把选择框列固定在左边
                fixed: null != props['rowSelection']['fixed'] ? props['rowSelection']['fixed'] : true,
                //多选/单选，checkbox or radio
                type: props['rowSelection']['type'] || 'checkbox',
                //选中项发生变化时的回调
                onChange(keys, rows) {
                    selectedRows['ids'] = keys;
                    selectedRows['rows'] = rows;
                    if (props['rowSelection']['onChange']) {
                        props['rowSelection']['onChange'](keys, rows);
                    }
                }
            };
            //当前选中的行
            let selectedRows = reactive({});
            //ref,获取dom
            let $refs = {};
            let refs = reactive({
                box: (ref) => {
                    $refs['box'] = ref;
                },
                headerButton: (ref) => {
                    $refs['headerButton'] = ref;
                },
                headerSearchList: (ref) => {
                    $refs['headerSearchList'] = ref;
                },
                pagination: (ref) => {
                    $refs['pagination'] = ref;
                }
            });
            //表格最大高度
            let tableMaxHeight = ref(1000);
            onMounted(() => {
                nextTick(() => {
                    let {box, pagination, headerButton, headerSearchList} = $refs;
                    let boxHeight = null != box ? box.getBoundingClientRect().height : 0;
                    let headerButtonHeight = null != headerButton ? headerButton.getBoundingClientRect().height : 0;
                    let headerSearchListHeight = null != headerSearchList ? headerSearchList.getBoundingClientRect().height : 0;
                    let paginationHeight = null != pagination ? pagination.getBoundingClientRect().height : 0;
                    tableMaxHeight.value = boxHeight - headerButtonHeight - headerSearchListHeight - paginationHeight;
                });
                //如果参数中有排序，显示表格的蓝色排序小箭头
                for (let key in props['data']) {
                    if (!props['data'].hasOwnProperty(key)) {
                        continue;
                    }
                    if (!key.endsWith('Sort')
                        || null == props['data'][key]) {
                        continue;
                    }
                    for (let i = 0; i < props['columns'].length; i++) {
                        let {dataIndex} = props['columns'][i];
                        if (key !== (dataIndex + 'Sort')) {
                            continue;
                        }
                        if ('ASC' === props['data'][key].toUpperCase()) {
                            props['columns'][i]['sortOrder'] = 'ASC';
                            props['columns'][i]['class'] = 'wei-sorter-up-on';
                        } else if ('DESC' === props['data'][key].toUpperCase()) {
                            props['columns'][i]['sortOrder'] = 'DESC';
                            props['columns'][i]['class'] = 'wei-sorter-down-on';
                        }
                        break;
                    }
                }
                getTableData();
            });
            //顶部搜索框
            let headerSearch = reactive({
                value: '',
                search(value) {
                    props['data']['searchValue'] = value;
                    getTableData();
                }
            });
            //实际展示的表格头部header按钮
            let realHeaderButtons = computed(() => {
                let buttons = [];
                props['headerButtons'].forEach(value => {
                    if (value['show']) {
                        buttons.push(value);
                    }
                });
                return buttons;
            });
            //表格行内部按钮是否折叠展示 popover 大于等于两个按钮
            //showFormatter优先级高于show
            let isShowRowActionButtonsPopover = (row) => {
                let num = 0;
                props['rowActionButtons'].forEach(value => {
                    let {show, showFormatter} = value;
                    if (showFormatter) {
                        if (showFormatter(row)) {
                            num++;
                        }
                        return;
                    }
                    if (show) {
                        num++;
                    }
                });
                return 2 > num;
            };
            //实际展示的表格内部按钮
            //showFormatter优先级高于show
            let realRowActionButtons = (row) => {
                let buttons = [];
                props['rowActionButtons'].forEach(value => {
                    let {show, showFormatter} = value;
                    if (showFormatter) {
                        if (showFormatter(row)) {
                            buttons.push(value);
                        }
                        return;
                    }
                    if (show) {
                        buttons.push(value);
                    }
                });
                return buttons;
            };
            //重置表格头部表单提交
            let resetHeaderForm = () => {
                props['headerSearchList'].forEach(value => {
                    delete props['data'][value['prop']];
                });
            };
            //分页、排序、筛选变化时触发
            let change = (pagination, filters, sorter, content) => {
                //只处理排序
                if (null == sorter) {
                    return;
                }
                let {order, column, field} = sorter;
                let sorterIndex = null;
                for (let i = 0; i < props['columns'].length; i++) {
                    let {dataIndex} = props['columns'][i];
                    if (field === dataIndex) {
                        sorterIndex = i;
                        break;
                    }
                }
                if (null == sorterIndex) {
                    console.warn(`如果不需要排序，请忽略此提示。 dataIndex字段未设置,请检查 columns 配置`);
                    return;
                }
                let orderValue = props['data'][`${field}Sort`];
                //如果原来为升序
                if ('ASC' === orderValue) {
                    props['columns'][sorterIndex]['sortOrder'] = 'DESC';
                    props['columns'][sorterIndex]['class'] = 'wei-sorter-down-on';
                    props['data'][`${field}Sort`] = 'DESC';
                } else if ('DESC' === orderValue) {
                    //如果原来为降序
                    props['columns'][sorterIndex]['sortOrder'] = false;
                    props['columns'][sorterIndex]['class'] = '';
                    delete props['data'][`${field}Sort`];
                } else {
                    //如果原来未设置
                    props['columns'][sorterIndex]['sortOrder'] = 'ASC';
                    props['columns'][sorterIndex]['class'] = 'wei-sorter-up-on';
                    props['data'][`${field}Sort`] = 'ASC';
                }
                getTableData();
            }
            return {
                getTableData,
                dataSource,
                pagination,
                loading,
                locale,
                realRowSelection,
                refs,
                headerSearch,
                selectedRows,
                realHeaderButtons,
                isShowRowActionButtonsPopover,
                realRowActionButtons,
                resetHeaderForm,
                tableMaxHeight,
                change
            }
        }
    }
</script>

<style lang="less" scoped>
    #wei-table {
        height: 100%;

        .header-button {
            overflow: hidden;
            padding-bottom: 20px;

            .btn {
                float: left;

                button {
                    margin-right: 20px;
                }
            }

            .search {
                float: right;
            }
        }

        .header-search-list {
            overflow: hidden;

            .item {
                float: left;
                margin-right: 20px;
                height: 50px;

                ::v-deep(.ant-form-item) {
                    display: flex;
                    margin-bottom: 10px;
                }
            }

        }

        .pagination {
            margin-top: 20px;
        }

        //配合表格排序显示
        ::v-deep(.wei-sorter-up-on) {
            .ant-table-column-sorters .ant-table-column-sorter-inner {
                & > span.anticon.anticon-caret-up {
                    color: #1890ff;
                }
            }
        }

        //配合表格排序显示
        ::v-deep(.wei-sorter-down-on) {
            .ant-table-column-sorters .ant-table-column-sorter-inner {
                & > span.anticon.anticon-caret-down {
                    color: #1890ff;
                }
            }
        }

    }
</style>