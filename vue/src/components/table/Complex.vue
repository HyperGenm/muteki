<template>
    <div id="wei-table" ref="weiTable">
        <template>
            <div class="search" v-show="tableSearch && 0 < tableSearch.length" ref="search">
                <!--表格查询-->
                <el-form inline size="mini">
                    <el-form-item v-for="item in tableSearch" :key="item.prop">
                        <template v-if="'input' === item.type">
                            <el-input v-model="tableDataRequest.data[item.prop]" :type="item.inputType || 'text'"
                                      :placeholder="item.placeholder" clearable
                                      :disabled="item.disabled"></el-input>
                        </template>
                        <template v-else-if="'select' === item.type">
                            <el-select v-model="tableDataRequest.data[item.prop]"
                                       clearable filterable :disabled="item.disabled"
                                       :allow-create="item.allowCreate"
                                       :placeholder="item.placeholder || '请选择'">
                                <el-option v-for="option in item.options" :key="option.value"
                                           :label="option.label" :value="option.value"
                                           :disabled="option.disabled"></el-option>
                            </el-select>
                        </template>
                        <template v-else-if="'datePicker' === item.type">
                            <el-date-picker :type="item['dateType'] || 'date'" :placeholder="item.placeholder || '选择日期'"
                                            v-model="tableDataRequest.data[item.prop]"
                                            :value-format="item.valueFormat || 'yyyy-MM-dd'"
                                            :disabled="item.disabled"></el-date-picker>
                        </template>
                        <template v-else-if="'timePicker' === item.type">
                            <el-date-picker :placeholder="item.placeholder || '选择日期'"
                                            v-model="tableDataRequest.data[item.prop]"
                                            :disabled="item.disabled"></el-date-picker>
                        </template>
                        <template v-else-if="'dateTimePicker' === item.type">
                            <el-date-picker type="datetime" :placeholder="item.placeholder || '选择时间'"
                                            v-model="tableDataRequest.data[item.prop]"
                                            :value-format="item.valueFormat || 'yyyy-MM-dd HH:mm:ss'"
                                            :disabled="item.disabled"></el-date-picker>
                        </template>
                        <template v-else>
                            {{item.label}}没有指定type
                        </template>
                    </el-form-item>
                    <slot name="search"></slot>
                    <el-button type="warning" size="mini" icon="el-icon-refresh" style="margin-bottom: 10px;"
                               @click="resetSearch">
                        重置
                    </el-button>
                    <el-button type="primary" size="mini" icon="el-icon-refresh" style="margin-bottom: 10px;"
                               @click="renderTable">
                        查询
                    </el-button>
                </el-form>
            </div>
            <div class="header" ref="header">
                <!--表格头部按钮组-->
                <el-row>
                    <el-button type="primary" size="mini" icon="el-icon-refresh" @click="renderTable">刷新</el-button>
                    <el-button type="primary" size="mini" icon="el-icon-s-tools"
                               @click="columnChangeDialog = true">字段
                    </el-button>
                    <el-button v-for="btn in tableHeaderButtons" :key="btn.name"
                               v-if="btn['show']"
                               :type="btn['type'] || 'primary'" size="mini"
                               :icon="btn.icon" @click="btn.handleClick(JSON.parse(JSON.stringify(selection)))"
                    >{{btn.name}}
                    </el-button>
                    <el-input v-if="showHeaderSearch" clearable
                              style="float: right;width: 30%;" size="mini"
                              placeholder="请输入搜索内容" prefix-icon="el-icon-search"
                              v-model="tableDataRequest.data[headerSearchColumn]"
                              @change="renderTable()">
                    </el-input>
                </el-row>
            </div>
            <div class="content">
                <!--表格-->
                <el-table ref="table" :show-summary="tableShowSummary" :summary-method="summaryMethod"
                          :data="tableData" height="7777px"
                          :max-height="maxHeight || tableMaxHeight"
                          v-loading="loading" :empty-text="actualEmptyText"
                          :stripe="null == selection || 0 >= selection.length"
                          @header-click="headerClick"
                          @cell-click="cellClick"
                          @selection-change="selectionChange" :row-style="rowStyle"
                          @sort-change="sortChange"
                          :header-cell-class-name="headerCellClassName"
                          :row-key="rowKey"
                          :default-sort="defaultSort"
                          :default-expand-all="defaultExpandAll"
                          border highlight-current-row size="small">
                    <el-table-column v-if="showSelection" type="selection" width="40"></el-table-column>
                    <el-table-column label="序号" type="index" fixed="left" width="50"></el-table-column>
                    <slot name="startColumn"></slot>
                    <el-table-column
                            v-for="column in tableShowColumns"
                            :key="column.prop"
                            :prop="column.prop"
                            :fixed="column.fixed"
                            :width="column.width"
                            min-width="80"
                            :class-name="column.className"
                            :sortable="column.sortable"
                            :show-overflow-tooltip="allShowOverflowTooltip || column.showOverflowTooltip">
                        <template slot="header" slot-scope="scope">
                            <template v-if="null != column.label && 3 < column.label.length">
                                <el-tooltip effect="dark" :content="column.label" placement="top">
                                    <span style="white-space: nowrap;overflow: hidden;">{{column.label}}</span>
                                </el-tooltip>
                            </template>
                            <template v-else>
                                <span>{{column.label}}</span>
                            </template>
                        </template>
                        <template slot-scope="scope">
                            <!--自定义显示element-ui组件，属性详情请看element-ui官网-->
                            <template v-if="column.element">
                                <template v-if="'tag' === column.type">
                                    <el-tag :type="column.element(scope.row)['type'] || ''"
                                            :size="column.element(scope.row)['size'] || 'medium'"
                                            :effect="column.element(scope.row)['effect'] || 'dark'">
                                        {{column.element(scope.row)['content'] || scope.row[column.prop]}}
                                    </el-tag>
                                </template>
                                <template v-else-if="'link' === column.type">
                                    <el-link :target="column.element(scope.row)['target'] || '_blank'"
                                             :href="column.element(scope.row)['href'] || null"
                                             :type="column.element(scope.row)['type'] || ''"
                                             :icon="column.element(scope.row)['icon'] || ''"
                                             :underline="column.element(scope.row)['underline']">
                                        {{column.element(scope.row)['content'] || scope.row[column.prop]}}
                                    </el-link>
                                </template>
                                <template v-else-if="'switch' === column.type">
                                    <el-switch style="cursor:pointer;"
                                               @change="columnSwitchChange($event,scope)"
                                               :value="column.element(scope.row)['value']"
                                               :disabled="column.element(scope.row)['disabled']"
                                               :activeColor="column.element(scope.row)['activeColor'] || '#13ce66'"
                                               :inactiveColor="column.element(scope.row)['inactiveColor'] || '#ff4949'"
                                               :activeText="column.element(scope.row)['activeText'] || ''"
                                               :inactiveText="column.element(scope.row)['inactiveText'] || ''"></el-switch>
                                </template>
                                <template v-else-if="'icon' === column.type">
                                    <i :class="column.element(scope.row)['leftIcon'] || ''"></i>
                                    <span style="margin-left: 5px">{{column.element(scope.row)['content'] || scope.row[column.prop]}}</span>
                                    <i :class="column.element(scope.row)['rightIcon'] || ''"></i>
                                </template>
                                <template v-else-if="'avatar' === column.type">
                                    <div @click="avatarClick(column.element(scope.row)['src'])">
                                        <el-image :src="column.element(scope.row)['src']"
                                                  :lazy="!column.element(scope.row)['notLazy']"
                                                  :alt="column.element(scope.row)['alt'] || ''"
                                                  :fit="column.element(scope.row)['fit'] || 'cover'"
                                                  :style="column.element(scope.row)['style'] || 'width:30px;height:30px'">
                                            <div slot="error">
                                                <i style="font-size: 21px;" class="el-icon-picture-outline"></i>
                                            </div>
                                        </el-image>
                                    </div>
                                </template>
                                <template v-else><h1 style="color: #ff4949;">{{column.label}}没有指定type</h1></template>
                            </template>
                            <template v-else>
                                <!--需要处理元素———:formatter=""-->
                                <template v-if="column.formatter">
                                    <div v-html="column.formatter(scope.row)"></div>
                                </template>
                                <!--表格普通元素-->
                                <template v-else>
                                    <div>{{scope.row[column.prop]}}</div>
                                </template>
                            </template>
                        </template>
                    </el-table-column>
                    <slot name="endColumn"></slot>
                    <!--表格中的操作按钮组-->
                    <el-table-column label="操作" fixed="right" prop="tableEditColumn"
                                     v-if="tableOperates && tableOperates.buttons && 0 < tableOperates.buttons.length"
                                     :width="tableOperates.width || 100">
                        <template slot-scope="scope">
                            <div v-if="isShowTableOperatesPopover(tableOperates['buttons'])">
                                <el-button v-for="btn in tableOperates.buttons" :key="btn.name"
                                           v-if="( btn['showFormatter'] && btn['showFormatter'](JSON.parse(JSON.stringify(scope.row))))
                                       || btn['show']"
                                           @click="btn.handleClick(JSON.parse(JSON.stringify(scope.row)),scope['$index'])"
                                           size="mini" :type="btn.type">{{btn.name}}
                                </el-button>
                            </div>
                            <div v-else>
                                <el-popover placement="left"
                                            :width="tableOperates.width || 150"
                                            trigger="click">
                                    <div style="margin: 10px auto;text-align: center;"
                                         v-for="(btn, index) in tableOperates.buttons" :key="index"
                                         v-if="( btn['showFormatter'] && btn['showFormatter'](JSON.parse(JSON.stringify(scope.row))))
                                       || btn['show']">
                                        <el-button size="mini" :type="btn.type"
                                                   @click="btn.handleClick(JSON.parse(JSON.stringify(scope.row)),scope['$index'])">
                                            {{btn.name}}
                                        </el-button>
                                    </div>
                                    <el-button slot="reference" style="padding: 7px 15px;">
                                        <span style="margin-right: 5px;">操作</span>
                                        <i class="el-icon-arrow-down"></i>
                                    </el-button>
                                </el-popover>
                            </div>
                        </template>
                    </el-table-column>
                </el-table>
            </div>
            <div class="pagination" ref="pagination">
                <!--表格分页-->
                <el-pagination background layout="total, sizes, prev, pager, next, jumper"
                               :page-sizes="[ 20, 50, 100]" :page-size="pageSize" :total="total"
                               @size-change="handleSizeChange"
                               @current-change="handleCurrentChange"
                ></el-pagination>
            </div>
        </template>
        <template>
            <!--表格上面切换隐藏字段显示-->
            <div class="columnChoose">
                <el-dialog :visible.sync="columnChangeDialog">
                    <el-checkbox-group v-model="columnCheckBox">
                        <el-checkbox v-for="item in tableColumns" :key="item.prop"
                                     style="margin-bottom: 10px;"
                                     :label="item.label" border></el-checkbox>
                    </el-checkbox-group>
                    <div style="overflow: hidden;margin-bottom: 20px;">
                        <el-button type="primary" style="float: right;margin-top: 20px;"
                                   @click="changeColumn">保存
                        </el-button>
                    </div>
                </el-dialog>
            </div>
            <!--展示图片-->
            <div class="show">
                <el-dialog :visible.sync="dialogShowImage">
                    <img width="100%" :src="dialogImageUrl">
                </el-dialog>
            </div>
        </template>
    </div>
</template>

<script>
    /**引入axios*/
    import axios from "axios";
    /**引入参数处理*/
    import Qs from 'qs';

    export default {
        name: "Complex",
        props: {
            // 表格数据请求
            tableDataRequest: {
                type: Object,
                default: () => {
                    return {
                        url: '',
                        data: {}
                    }
                }
            },
            //表格顶部搜索条件
            tableSearch: {
                type: Array,
                default: () => []
            },
            //表格头部按钮
            tableHeaderButtons: {
                type: Array,
                default: () => []
            },
            // 表格的字段展示
            tableColumns: {
                type: Array,
                default: () => []
            },
            // 表格行的操作按钮
            tableOperates: {
                type: Object,
                default: () => {
                }
            },
            // 是否展示合计行
            tableShowSummary: {
                type: Boolean,
                default: false
            },
            //空数据时显示的内容
            emptyText: {
                type: String,
                default: '暂无数据'
            },
            //行数据的 Key，用来优化 Table 的渲染
            rowKey: {
                type: String,
                default: 'id'
            },
            //是否默认展开所有行，当 Table 包含展开行存在或者为树形表格时有效
            defaultExpandAll: {
                type: Boolean,
                default: true
            },
            //展示表格左边多选
            showSelection: {
                type: Boolean,
                default: false
            },
            //表格最大高度
            maxHeight: {
                type: Number
            },
            //展示表格上面搜索输入框
            showHeaderSearch: {
                type: Boolean,
                default: false
            },
            //表格上面搜索输入框字段
            headerSearchColumn: {
                type: String,
                default: 'search'
            },
            //表格所有列当内容过长被隐藏时显示 tooltip
            allShowOverflowTooltip: {
                type: Boolean,
                default: false
            },
            //默认排序
            defaultSort: {
                type: Object
            }
        },
        data() {
            let that = this;
            return {
                //当前表格展示数据
                tableData: [],
                //每页展示数量
                pageSize: 20,
                //页码
                pageNum: 1,
                //总数
                total: 0,
                //表格最大高度，动态计算
                tableMaxHeight: 0,
                //表格加载中动画
                loading: false,
                //实际空数据时显示的内容
                actualEmptyText: '',
                //当前选中行
                selection: [],
                //表格展示的表头
                tableShowColumns: that.tableColumns,
                //选择展示字段
                columnChangeDialog: false,
                //当前选中的表格字段
                columnCheckBox: that.tableColumns.map(value => value['label']),
                //弹窗展示图片
                dialogShowImage: false,
                //弹窗展示图片的路径
                dialogImageUrl: '',
                //表头排序字段
                headerSort: {}
            }
        },
        mounted() {
            //获取数据
            this.getTableList();
            let columns = localStorage.getItem(this.getTableColumnLocalStorageKey());
            //如果本地有展示的字段
            if (null != columns && 0 < columns.length) {
                //json字符串反序列化，字符串方法还原
                this.tableShowColumns = JSON.parse(columns, function (k, v) {
                    if (v.indexOf && v.indexOf('function') > -1) {
                        return eval("(function(){return " + v + " })()")
                    }
                    return v;
                });
            }
            //初始化表格高度
            this.initTableMaxHeight();
        },
        methods: {
            //表格字段本地存放的key
            getTableColumnLocalStorageKey() {
                let {url} = this.tableDataRequest;
                let {id} = this.$globalFun.getSessionStorage('userInfo');
                let href = window.location.href;
                return this.$cryptoJS.md5(`v1-${href}-${url}-${id}`);
            },
            initTableMaxHeight() {
                let that = this;
                this.$nextTick(() => {
                    let weiTableHeight = that.$refs['weiTable'].getBoundingClientRect().height;
                    let searchHeight = that.$refs['search'].getBoundingClientRect().height;
                    let headerHeight = that.$refs['header'].getBoundingClientRect().height;
                    let paginationHeight = that.$refs['pagination'].getBoundingClientRect().height;
                    that.tableMaxHeight = weiTableHeight - searchHeight - headerHeight - paginationHeight - 20;
                });
            },
            // 获取数据
            getTableList() {
                /**开启加载中动画*/
                this.loading = true;
                /**重置表格数据*/
                this.tableData = [];
                let that = this;
                let _url = that.tableDataRequest.url;
                let _method = that.tableDataRequest.method || 'get';
                let _axios = {
                    url: that.$global.GLOBAL.baseUrl + _url,
                    method: that.tableDataRequest.method || 'get',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8',
                        [that.$global.GLOBAL.token]: that.$globalFun.getSessionStorage('token')
                    },
                    timeout: that.tableDataRequest.timeout || parseInt(process.env.VUE_APP_AXIOS_TIMEOUT)
                };
                let _data = that.tableDataRequest.data || {};
                _data['pageNum'] = that.pageNum;
                _data['pageSize'] = that.pageSize;
                _data['__t'] = (new Date()).getTime();
                _method = _method.toUpperCase();
                if (_method === 'GET') {
                    _axios['params'] = _data;
                } else {
                    _axios['data'] = Qs.stringify(_data, {indices: false});
                }
                axios(_axios).then((res) => {
                    /**关闭加载中动画*/
                    that.loading = false;
                    /**token过期处理*/
                    if (401 === res.data.code) {
                        that.$globalFun.errorMsg('登陆过期，即将跳转到登录页面');
                        that.$store.dispatch('resetState');
                        sessionStorage.setItem('loginStatus', 'logout');
                        let timer = setTimeout(() => {
                            clearTimeout(timer);
                            that.$router.replace('/login');
                        }, 3000);
                        return;
                    }
                    /**处理code不为200的出错请求*/
                    if (200 !== res.data.code) {
                        that.$globalFun.errorMsg(res.data['msg']);
                        that.actualEmptyText = JSON.stringify(res['data']);
                        that.$globalFun.consoleWarnTable(`请求出错url:${_url}`, res['data']);
                        return;
                    }
                    /**判断返回格式是否正确*/
                    if (null == res.data.data.list) {
                        that.actualEmptyText = '返回格式出错。示例:{"list":[],"pageNum":1,"pageSize":10}';
                        return;
                    }
                    /**展示数据*/
                    that.actualEmptyText = that.emptyText;
                    that.tableData = res.data.data.list;
                    that.total = res.data.data.total;
                }).catch((error) => {
                    /**关闭加载中动画*/
                    that.loading = false;
                    // 如果请求被取消则进入该方法
                    if (axios.isCancel(error)) {
                        return;
                    }
                    that.actualEmptyText = `系统错误,请联系管理员${error}`;
                    that.$globalFun.consoleWarnTable(`表格请求失败url:${_url}`, error);
                });
            },
            //pageSize改变触发
            handleSizeChange(pageSize) {
                this.pageNum = 1;
                this.pageSize = pageSize;
                this.getTableList();
            },
            //pageNum改变触发
            handleCurrentChange(pageNum) {
                this.pageNum = pageNum;
                this.getTableList();
            },
            //刷新表格数据
            renderTable() {
                this.getTableList();
            },
            //字段展示---选择部分字段展示
            changeColumn() {
                //判断当前环境是不是生产环境
                if ('production' !== process.env.NODE_ENV) {
                    this.$globalFun.errorMsg('开发环境下进行部分字段操作容易因为缓存问题，字段的修改无法正常显示，请谨慎使用', 10000);
                    console.warn('开发环境下进行部分字段操作容易因为缓存问题，字段的修改无法正常显示，请谨慎使用');
                }
                let columns = [];
                let {columnCheckBox, tableColumns} = this;
                for (let i = 0; i < tableColumns.length; i++) {
                    if (columnCheckBox.includes(tableColumns[i]['label'])) {
                        columns.push(tableColumns[i]);
                    }
                }
                if (null == columns || 0 >= columns.length) {
                    this.$globalFun.errorMsg("至少要有一个展示的字段");
                    return;
                }
                this.tableShowColumns = columns;
                //如果全部显示，移除本地缓存
                if (tableColumns.length === columns.length) {
                    localStorage.removeItem(this.getTableColumnLocalStorageKey());
                    this.columnChangeDialog = false;
                    return;
                }
                //将展示的字段放入本地
                //json对象序列化，并将里面的方法转为字符串
                localStorage.setItem(this.getTableColumnLocalStorageKey(), JSON.stringify(columns, function (key, val) {
                        if (typeof val === 'function') {
                            return val + '';
                        }
                        return val;
                    }
                ));
                this.columnChangeDialog = false;
            },
            //表格内部操作按钮是否折叠展示
            isShowTableOperatesPopover(buttons) {
                let num = 0;
                buttons.forEach(value => {
                    let {show, showFormatter} = value;
                    if (show) {
                        num++;
                    } else if (showFormatter && showFormatter()) {
                        num++;
                    }
                });
                return 2 > num;
            },
            //求和
            summaryMethod(param) {
                let sums = [];
                let that = this;
                this.$emit('summaryMethod', {
                    param,
                    total: that.total
                }, function (res) {
                    sums = res;
                });
                return sums;
            },
            //表头点击事件
            headerClick(column, event) {
                this.$emit('headerClick', column);
            },
            //当选择项发生变化时会触发该事件
            selectionChange(selection) {
                this.selection = selection;
            },
            //表格行样式
            rowStyle({row, rowIndex}) {
                if (this.selection.includes(row)) {
                    return {'background-color': 'rgba(185, 221, 249, 0.75) !important'};
                }
            },
            //当表格的排序条件发生变化的时候会触发该事件
            sortChange({column, prop, order}) {
                this.headerSort[prop] = order;
                let value = null;
                if (this.$globalFun.isBlank(order)) {
                    value = null;
                } else {
                    value = order.replace('ending', '').toUpperCase();
                }
                this.tableDataRequest.data[`${prop}Sort`] = value;
                this.getTableList();
            },
            /**
             * 表头样式,配合排序使用
             * @param row
             * @param column
             * @param rowIndex
             * @param columnIndex
             */
            headerCellClassName({row, column, rowIndex, columnIndex}) {
                let {headerSort} = this;
                let {sortable, property} = column;
                if ("custom" !== sortable) {
                    return '';
                }
                return headerSort[property];
            },
            //switch状态改变时触发
            columnSwitchChange(value, {$index, column, row}) {
                this.$emit('columnSwitchChange', {
                    index: $index,//在表格中的行数-1
                    prop: column['property'],//当前prop
                    row,//当前行的值
                    value//改变后得值
                });
            },
            //展示图片
            avatarClick(src) {
                this.dialogImageUrl = src;
                this.dialogShowImage = true;
            },
            //表格行被点击
            cellClick(row, column, cell, event) {
                let {property} = column;
                //如果是操作列
                if ('tableEditColumn' === property) {
                    return;
                }
                if (this.$globalFun.isBlank(property)) {
                    console.debug(`未找到property字段，请检查 tableColumns 中prop字段设置,如果不需要表格行点击事件，请忽略该提示`);
                }
                this.$emit('cellClick', {row, column: column['property'], cell, event});
            },
            /**
             * 重置搜索表单
             */
            resetSearch() {
                let {tableSearch = [], tableDataRequest: {data = {}}} = this;
                tableSearch.forEach(value => {
                    data[value['prop']] = null;
                });
                this.tableDataRequest.data = JSON.parse(JSON.stringify(data));
            }
        }
    }
</script>

<style lang="scss" scoped>

    @import "@/assets/sass/element-variables.scss";

    #wei-table {
        height: 100%;
        overflow: hidden;

        .header button {
            margin-bottom: 10px;
        }

        .search {
            .el-form-item {
                margin-bottom: 3px;
            }
        }

        .pagination {
            float: right;
            margin-top: 7px;
            margin-right: 20px;
        }
    }

    #wei-table {
        /*没有数据，或者出错时显示的文字*/
        ::v-deep span.el-table__empty-text {
            color: $--color-primary !important;
        }

        ::v-deep .el-table {
            thead th {
                background-color: #ddeeff;
            }

            table tbody tr td:nth-child(2) .cell {
                display: flex;
            }

            table tbody tr td:nth-child(3) .cell {
                display: flex;
            }

            .cell {
                overflow: hidden;
                text-overflow: ellipsis;
                white-space: nowrap;
            }
        }

    }
</style>