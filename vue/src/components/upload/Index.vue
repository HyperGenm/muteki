<template>
    <div class="upload">
        <el-upload
                ref="upload"
                action="这里不写会报错，本组件使用的是自定义请求上传的方式"
                :multiple="multiple"
                :show-file-list="showFileList"
                :on-preview="onPreview"
                :on-remove="onRemove"
                :on-change="onChange"
                :before-upload="beforeUpload"
                :list-type="listType"
                :auto-upload="autoUpload"
                :file-list="nowFileList"
                :limit="limit"
                :on-exceed="onExceed"
                :accept="accept"
                :http-request="httpRequest">
            <el-button size="small" type="primary">选择文件</el-button>
            <el-button v-if="!autoUpload" size="small" type="success"
                       @click.stop="submitUpload">上传到服务器
            </el-button>
        </el-upload>
        <div slot="tip" class="el-upload__tip">{{tip}}</div>
        <el-dialog :visible.sync="dialogVisible">
            <img width="100%" :src="dialogImageUrl">
        </el-dialog>
    </div>
</template>

<script>
    //引入axios
    import axios from "axios";
    /**引入element-ui组件*/
    import {Loading} from 'element-ui';

    export default {
        name: "Index",
        props: {
            //上传的地址
            action: {
                type: String
            },
            //额外参数
            data: {
                type: Object,
                default: () => {
                }
            },
            //是否支持多文件上传
            multiple: {
                type: Boolean,
                default: false
            },
            //是否展示上传的文件列表
            showFileList: {
                type: Boolean,
                default: true
            },
            //文件列表的类型
            listType: {
                type: String,
                default: 'picture'
            },
            //是否在文件选择后立即上传
            autoUpload: {
                type: Boolean,
                default: false
            },
            //允许的最大上传个数
            limit: {
                type: Number,
                default: 1
            },
            //文件上传提示
            tip: {
                type: String,
                default: ''
            },
            //文件类型
            accept: {
                type: String,
                default: 'image/*'
            },
            //最大尺寸---默认2M
            maxSize: {
                type: Number,
                default: 2097152
            },
            //展示的文件列表
            fileList: {
                type: Array
            }
        },
        data() {
            let that = this;
            return {
                //文件展示列表
                nowFileList: that.fileList,
                dialogImageUrl: '',
                dialogVisible: false
            }
        },
        watch: {
            nowFileList(list) {
                this.$emit('update:fileList', list);
            },
            fileList: {
                deep: true,
                immediate: true,
                handler(list) {
                    this.nowFileList = list;
                }
            }
        },
        methods: {
            /**
             * 点击上传的文件--如果是图片显示缩略图
             * @param file
             */
            onPreview(file) {
                try {
                    this.dialogImageUrl = file['url'];
                    this.dialogVisible = true;
                } catch (e) {
                    console.debug('当前显示不是图片，此消息可以忽略,详情:', e);
                }
            },
            /**
             * 文件列表删除时触发的事件
             * @param file
             * @param fileList
             */
            onRemove(file, fileList) {
                this.nowFileList = fileList;
            },
            /**
             * 文件状态改变时的钩子，添加文件、上传成功和上传失败时都会被调用
             * @param file
             * @param fileList
             */
            onChange(file, fileList) {
                let {raw} = file;
                if (!this.beforeValidateFileSize(raw)) {
                    this.nowFileList = fileList.filter(value => value['uid'] !== file['uid']);
                    this.$globalFun.errorMsg('文件超出最大尺寸');
                    this.$globalFun.consoleWarnTable('上传文件超出最大尺寸file:', file);
                    return;
                }
                if (!this.beforeValidateFileType(raw)) {
                    this.$globalFun.errorMsg('文件格式不正确');
                    this.nowFileList = fileList.filter(value => value['uid'] !== file['uid']);
                    this.$globalFun.consoleWarnTable('上传文件格式不正确file:', file);
                    return;
                }
                this.nowFileList = fileList;
            },
            /**
             * 文件尺寸是否正确
             * @param file
             * @param fileList
             */
            beforeValidateFileSize(file) {
                let {maxSize} = this;
                let {size} = file;
                //判断文件是否超出尺寸
                return size <= maxSize;
            },
            /**
             * 检验文件格式
             * @param file
             */
            beforeValidateFileType(file) {
                let {accept} = this;
                let {type} = file;
                //默认文件格式不正确
                let flag = false;
                //如果为全部类型，不做处理
                if ("*" === accept) {
                    flag = true;
                }
                //无法识别文件类型
                if (null == type || '' === type) {
                    return false;
                }
                //如果存在该类型
                if (!flag && accept.includes(type)) {
                    flag = true;
                }
                //如果有图片类型，并且是所有图片类型
                if (!flag && accept.includes("image/*")) {
                    if (type.startsWith("image/")) {
                        flag = true;
                    }
                }
                //如果有音频类型，并且是所有音频类型
                if (!flag && accept.includes("audio/*")) {
                    if (type.startsWith("audio/")) {
                        flag = true;
                    }
                }
                //如果有视频类型，并且是所有视频类型
                if (!flag && accept.includes("video/*")) {
                    if (type.startsWith("video/")) {
                        flag = true;
                    }
                }
                //以下为自定义类型
                if (!flag && accept.includes("application/*")) {
                    if (type.startsWith("application/")) {
                        flag = true;
                    }
                }
                if (!flag && accept.includes("text/*")) {
                    if (type.startsWith("text/")) {
                        flag = true;
                    }
                }
                return flag;
            },
            /**
             * 文件上传之前
             * @param file
             */
            beforeUpload(file) {
                //尺寸是否正确
                if (!this.beforeValidateFileSize(file)) {
                    this.$globalFun.errorMsg('文件超出最大尺寸');
                    this.$globalFun.consoleWarnTable('文件超出最大尺寸:', file);
                    return false;
                }
                //如果文件格式不正确
                if (!this.beforeValidateFileType(file)) {
                    this.$globalFun.errorMsg('文件格式不正确');
                    this.$globalFun.consoleWarnTable('上传文件格式不正确file:', file);
                    return false;
                }
                let flag = true;
                //向父组件抛出beforeUpload---接受回调
                this.$emit('beforeUpload', file, function (res) {
                    //根据回调判断是否继续上传
                    flag = null == res ? true : res;
                });
                return flag;
            },
            /**
             * 开始上传
             */
            submitUpload() {
                this.$refs.upload.submit();
            },
            /**
             * 超出最大数量时
             * @param files
             * @param fileList
             */
            onExceed(files, fileList) {
                this.$globalFun.errorMsg("超出最大数量限制");
                console.warn("文件上传超出数量限制---files:", files, '-----fileList:', fileList);
            },
            /**
             * 自定义上传请求
             * @param item
             */
            httpRequest(item) {
                /**timeShowLoadAnimation时间之后开启加载中动画*/
                let loading = null;
                let loadingTimer = setTimeout(() => {
                    loading = Loading.service({
                        lock: true,
                        text: '上传中...',
                        spinner: 'none'
                    });
                }, 555);
                let that = this;
                let formData = new FormData();
                formData.append('file', item['file']);
                for (let key in that.data) {
                    if (!that.data.hasOwnProperty(key)) {
                        continue;
                    }
                    formData.append(key, that.data[key]);
                }
                axios({
                    headers: {
                        'Content-Type': 'multipart/form-data',
                        [that.$global.GLOBAL.token]: that.$globalFun.getSessionStorage('token')
                    },
                    method: 'post',
                    url: `${that.$global.GLOBAL.baseUrl}${that.action}?__t=${new Date().getTime()}`,
                    data: formData,
                }).then(res => {
                    /**关闭加载中动画*/
                    clearTimeout(loadingTimer);
                    if (null != loading) {
                        loading.close();
                    }
                    let {data} = res;
                    /**token过期处理*/
                    if (401 === data['code']) {
                        that.$globalFun.errorMsg('登陆过期，即将跳转到登录页面');
                        sessionStorage.setItem('loginStatus', 'logout');
                        let timer = setTimeout(() => {
                            that.$router.replace('/login');
                            clearTimeout(timer);
                        }, 3000);
                        return;
                    }
                    /**code不是200*/
                    if (200 !== data['code']) {
                        item.onError();
                        that.$globalFun.errorMsg(data['msg']);
                        that.$globalFun.consoleWarnTable(`文件上传失败url:${that.action}`, data);
                        return;
                    }
                    that.$globalFun.successMsg('上传成功');
                    item.onSuccess();
                    let {nowFileList} = that;
                    for (let i = 0; i < nowFileList.length; i++) {
                        if (item['file']['uid'] === nowFileList[i]['uid']) {
                            nowFileList[i]['response'] = data;
                            break;
                        }
                    }
                    that.nowFileList = nowFileList;
                    that.$emit('success', nowFileList);
                }).catch(error => {
                    /**关闭加载中动画*/
                    clearTimeout(loadingTimer);
                    if (null != loading) {
                        loading.close();
                    }
                    item.onError();
                    // 如果请求被取消则进入该方法
                    if (axios.isCancel(error)) {
                        return;
                    }
                    that.$globalFun.errorMsg(`<p>系统错误，请联系管理员</p>${error}`, 3000, true);
                    that.$globalFun.consoleWarnTable(`文件上传失败url:${that.action}`, error);
                });
            }
        }
    }
</script>