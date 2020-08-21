<template>
    <div id="dialogForm" style="overflow: hidden;">
        <el-form ref="form" size="mini" :label-width="labelWidth"
                 :model="formData || {}" :rules="formRules || {}">
            <slot name="itemHead"></slot>
            <template v-for="item in formOptions"
                      v-if="!item['hidden']">
                <!--一行展示多个-->
                <template v-if="null != item.items && 0 < item.items.length">
                    <div style="display: flex;">
                        <template v-for="i in item.items">
                            <!--station表示仅用作占位-->
                            <template v-if="i.station">
                                <el-form-item :style="`flex:${i.flex || 1}`"></el-form-item>
                            </template>
                            <template v-else>
                                <wei-item :item="i" :formData="formData"
                                          v-if="!i['hidden']"
                                          :style="`flex:${i.flex || 1}`"
                                          @inputFocus="$emit('inputFocus',{$event,i})"
                                          @selectChange="selectChange">>
                                </wei-item>
                            </template>
                        </template>
                    </div>
                </template>
                <!--一行展示一个-->
                <template v-else>
                    <wei-item :item="item" :formData="formData"
                              @inputFocus="$emit('inputFocus',{$event,item})"
                              @selectChange="selectChange"></wei-item>
                </template>
            </template>
            <slot name="itemTail"></slot>
        </el-form>
        <div v-if="showFooterButton" slot="footer">
            <div style="float: right;">
                <el-button v-if="showCancelButton" @click="$emit('closeDialog')">取 消</el-button>
                <slot name="button"></slot>
                <el-button v-if="showResetButton" type="warning" @click="resetForm">重置</el-button>
                <el-button type="primary" @click="submitForm">确 定</el-button>
            </div>
        </div>
    </div>
</template>

<script>
    export default {
        name: "Index",
        components: {
            'wei-item': () => import('./Item.vue')
        },
        props: {
            //form表单
            formData: {
                type: Object,
                default: () => {
                }
            },
            //form表单规则
            formRules: {
                type: Object,
                default: () => {
                }
            },
            //form表单组件
            formOptions: {
                type: Array,
                default: () => []
            },
            //表单左边的宽度
            labelWidth: {
                type: String,
                default: '7rem'
            },
            //是否展示底部操作按钮
            showFooterButton: {
                type: Boolean,
                default: true
            },
            //展示取消按钮
            showCancelButton: {
                type: Boolean,
                default: true
            },
            //展示重置按钮
            showResetButton: {
                type: Boolean,
                default: true
            }
        },
        methods: {
            //下拉框值改变触发事件
            selectChange(value, prop) {
                this.$emit('selectChange', value, prop);
            },
            //监听表单提交
            submitForm() {
                let that = this;
                this.$refs['form'].validate((valid) => {
                    if (!valid) {
                        return false;
                    }
                    let form = {};
                    for (let key in that.formData) {
                        if (!that.formData.hasOwnProperty(key)) {
                            break;
                        }
                        if ('id' === key) {
                            form['id'] = that.formData['id'];
                            continue;
                        }
                        for (let i = 0; i < that.formOptions.length; i++) {
                            let {prop, hidden, items} = that.formOptions[i];
                            //一行有一项
                            if (null == items || 0 >= items.length) {
                                //如果当前项隐藏
                                if (hidden) {
                                    continue;
                                }
                                if (key === prop) {
                                    form[key] = that.formData[key];
                                    break;
                                }
                                continue;
                            }
                            //一行有多项
                            for (let j = 0; j < items.length; j++) {
                                let {prop, hidden} = items[j];
                                //如果当前项隐藏
                                if (hidden) {
                                    continue;
                                }
                                if (key === prop) {
                                    form[key] = that.formData[key];
                                    i = that.formOptions.length;
                                    break;
                                }
                            }
                        }
                    }
                    that.$emit('submit', form);
                });
            },
            //重置表单
            resetForm() {
                this.$refs['form'].resetFields();
            }
        }
    }
</script>