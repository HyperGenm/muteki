<template>
    <div id="index">
        <wei-form :formData="form" :formOptions="formOptions"
                  @closeDialog="$emit('closeDialog')" @submit="submit">
            <template v-slot:itemTail>
                <el-form-item label='图标'>
                    <el-button type="primary" @click="dialogIcons = true">选择图标
                    </el-button>
                    <i style="margin-left: 20px;font-size: 20px;" :class="choosedIcon"></i>
                </el-form-item>
            </template>
        </wei-form>
        <template>
            <el-dialog title="请选择图标" :visible.sync="dialogIcons">
                <div class="icons">
                    <div @click="chooseIcon(icon)" class="icon" v-for="icon in icons" :key="icon">
                        <i :class="icon"></i>
                    </div>
                </div>
            </el-dialog>
        </template>
    </div>
</template>

<script>

    //获取element-ui的图标
    import elementIcons from './icons.js'

    export default {
        name: "EditForm",
        components: {
            'wei-form': () => import('@/components/form/Index.vue')
        },
        props: {
            //操作类型
            editType: {
                type: String,
                default: 'add'
            },
            //表单数据
            formData: {
                type: Object
            },
            //是否展示
            isShow: {
                type: Boolean,
                default: false
            }
        },
        data() {
            return {
                formOptions: [
                    {type: 'input', label: '标题', prop: 'title', required: true, disabled: false},
                    {type: 'input', label: 'name', prop: 'name', required: true, disabled: false},
                    {
                        type: 'cascader', label: '上级', prop: 'parentId', required: true, disabled: false,
                        options: []
                    },
                    {type: 'input', label: '路径', prop: 'path', required: true},
                    {
                        type: 'radio', label: '类型', prop: 'type', required: true,
                        options: [
                            {label: '菜单', value: 1},
                            {label: '按钮', value: 2},
                        ]
                    },
                    {
                        type: 'radio', label: '专属', prop: 'superFlag', required: true,
                        options: [
                            {label: '普通', value: 1},
                            {label: 'vip', value: 2}
                        ]
                    },
                    {
                        type: 'radio', label: '内/外部', prop: 'externalFlag', required: true,
                        options: [
                            {label: '内部', value: 1},
                            {label: '外部超链接', value: 2}
                        ]
                    },
                    {type: 'input', label: '排序', prop: 'sort', inputType: 'number'},
                    {type: 'textarea', label: '备注', prop: 'description'},
                ],
                form: this.formData,
                //路由图标
                dialogIcons: false,
                //图标
                icons: elementIcons['icons'],
                //当前选中的图标
                choosedIcon: ''
            }
        },
        watch: {
            isShow(show) {
                if (show) {
                    this.changeFormOptions();
                }
            }
        },
        mounted() {
            console.warn(`当前页面可能提示
            ** [Vue warn]: Error in callback for watcher "value": "TypeError: Cannot read property 'level' of null" **
            不过并不影响使用，个人感觉是级联选择器问题`);
            this.changeFormOptions();
            let that = this;
            this.$axios({
                url: that.$global.URL.system.sysFunction.getTree,
                success(data) {
                    let options = [];
                    data.forEach(value => {
                        let children = that.handleChildren(value.children);
                        options.push({
                            label: value.title,
                            value: value.id,
                            children
                        });
                    });
                    that.formOptions[2]['options'] = [{
                        label: '最高级',
                        value: 0,
                        children: options
                    }];
                }
            });
        },
        methods: {
            /**
             * 处理子级
             */
            handleChildren(data) {
                if (null == data) {
                    return null;
                }
                let result = [];
                data.forEach(value => {
                    //跳过按钮
                    if (2 === value['type']) {
                        return;
                    }
                    let children = this.handleChildren(value.children);
                    result.push({
                        label: value.title,
                        value: value.id,
                        children
                    });
                });
                return result;
            },
            //编辑时，名称设置为禁用
            changeFormOptions() {
                this.formOptions[0]['disabled'] = 'add' !== this.editType;
                this.formOptions[1]['disabled'] = 'add' !== this.editType;
                this.formOptions[2]['disabled'] = 'add' !== this.editType;
                this.form = this.formData;
                this.choosedIcon = this.formData['icon'];
            },
            chooseIcon(icon) {
                this.choosedIcon = icon;
                this.dialogIcons = false;
            },
            submit(form) {
                //`el-select`级联选择器是个数组
                let {parentId} = form;
                if (Array.isArray(parentId)) {
                    let max = 0;
                    parentId.forEach(value => {
                        max = max > value ? max : value;
                    });
                    form['parentId'] = max;
                }
                let that = this;
                form['icon'] = this.choosedIcon;
                that.$axios({
                    url: that.$global.URL.system.sysFunction[that.editType],
                    method: 'post',
                    data: form,
                    success() {
                        that.$globalFun.successMsg('成功');
                        that.$emit('closeDialog');
                        that.$emit('renderTable');
                    }
                });
            }
        }
    }
</script>

<style lang="scss" scoped>
    .icons {
        overflow: hidden;

        .icon {
            float: left;
            width: 10%;
            box-sizing: border-box;
            border: 1px solid #666;
            height: 50px;
            line-height: 50px;
            text-align: center;
            font-size: 35px;
        }
    }
</style>