<template>
    <div id="wei-table" ref="weiTable">
        <template>
            <div class="content">
                <!--表格-->
                <el-table ref="table"
                          :data="tableData" height="5555px"
                          :empty-text="emptyText"
                          :max-height="maxHeight || 200"
                          :stripe="null == selection || 0 >= selection.length"
                          @header-click="headerClick"
                          @cell-click="cellClick"
                          @selection-change="selectionChange" :row-style="rowStyle"
                          :row-key="rowKey"
                          :default-expand-all="defaultExpandAll"
                          border highlight-current-row size="small">
                    <el-table-column v-if="showSelection" type="selection" width="40"></el-table-column>
                    <el-table-column label="序号" type="index" fixed="left" width="50"></el-table-column>
                    <slot name="startColumn" :data="tableData"></slot>
                    <el-table-column
                            v-for="column in tableColumns"
                            :key="column.prop"
                            :prop="column.prop"
                            :fixed="column.fixed"
                            :width="column.width"
                            min-width="80"
                            :sortable="column.sortable"
                            :show-overflow-tooltip="allShowOverflowTooltip && !column.hiddenOverflowTooltip">
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
                    <slot name="endColumn" :data="tableData"></slot>
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
        </template>
        <template>
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
    export default {
        name: "Simple",
        props: {
            //表格数据
            tableData: {
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
            //表格所有列当内容过长被隐藏时显示 tooltip
            allShowOverflowTooltip: {
                type: Boolean,
                default: true
            }
        },
        data() {
            return {
                //当前选中的行
                selection: [],
                //弹窗展示图片
                dialogShowImage: false,
                //弹窗展示图片的路径
                dialogImageUrl: '',
            }
        },
        methods: {
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
            }
        }
    }
</script>

<style lang="scss">
    #wei-table {
        overflow: hidden;

        .search {
            .el-form-item {
                margin-bottom: 3px;
            }
        }
    }
</style>

<style lang="scss">
    @import "@/assets/sass/element-variables.scss";

    #wei-table {
        /*没有数据，或者出错时显示的文字*/
        span.el-table__empty-text {
            color: $--color-primary !important;
        }

        /*表头背景颜色*/
        .el-table thead th {
            background-color: #ddeeff;
        }

        /*表格树形结构，箭头错位*/
        .el-table table tbody tr td:nth-child(3) .cell {
            display: flex;
        }

        /*表格树形结构，箭头错位*/
        .el-table table tbody tr td:nth-child(2) .cell {
            display: flex;
        }

        /*只显示一行，超出部分显示省略号*/
        .el-table .cell {
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
        }

    }
</style>

<style lang="scss" scoped>
    #wei-table {
        height: 100%;
        overflow: hidden;

        .header button {
            margin-bottom: 10px;
        }

        .pagination {
            float: right;
            margin-top: 7px;
            margin-right: 20px;
        }
    }
</style>