<template>
    <a-upload :accept="accept"
              :action="realAction"
              :method="method"
              v-model:data="data"
              :disabled="disabled"
              v-model:fileList="fileList"
              :headers="headers"
              :listType="listType"
              :multiple="multiple"
              :name="name"
              :withCredentials="withCredentials"
              :beforeUpload="beforeUpload"
              @change="change">
        <template v-if="slot">
            <slot></slot>
        </template>
        <template v-else>
            <a-button>
                <upload-outlined/>
                {{buttonTexts}}
            </a-button>
        </template>
    </a-upload>
</template>

<script>
    import {UploadOutlined} from '@ant-design/icons-vue';
    import {Upload, Button} from 'ant-design-vue';
    import $ant from '@/utils/ant';
    import $function from '@/utils/function';
    import {ref} from 'vue';
    //引入router
    import {useRouter} from 'vue-router';

    export default {
        components: {
            UploadOutlined,
            [Upload.name]: Upload,
            [Button.name]: Button,
        },
        props: {
            //自定义slot
            slot: {
                type: Boolean,
                default: false
            },
            //上传按钮文字
            buttonTexts: {
                type: String,
                default: '上传'
            },
            //接受上传的文件类型, 详见 https://developer.mozilla.org/en-US/docs/Web/HTML/Element/input/file#accept
            accept: {
                type: String
            },
            //上传的地址
            action: {
                type: String
            },
            //上传请求的 http method
            method: {
                type: String,
                default: 'post'
            },
            //上传所需参数或返回上传参数的方法
            data: {
                type: Object,
                default: ({
                    __t: new Date().getTime()
                })
            },
            //是否禁用
            disabled: {
                type: Boolean,
                default: false
            },
            //已经上传的文件列表（受控）
            fileList: {
                type: Array,
                default: ([])
            },
            //设置上传的请求头部，IE10 以上有效
            headers: {
                type: Object,
                default: ({
                    token: $function.getLocationStorage('token')
                })
            },
            //上传列表的内建样式，支持三种基本样式 text, picture 和 picture-card
            listType: {
                type: String,
                default: 'picture'
            },
            //是否支持多选文件，ie10+ 支持。开启后按住 ctrl 可选择多个文件。
            multiple: {
                type: Boolean,
                default: false
            },
            //发到后台的文件参数名
            name: {
                type: String,
                default: 'file'
            },
            //上传请求时是否携带 cookie
            withCredentials: {
                type: Boolean,
                default: false
            },
            //最大上传数量
            maxNum: {
                type: Number,
                default: 1
            }
        },
        mounted() {
            //对外抛出自身
            this.$emit('my-ref', this);
        },
        setup(props, {emit: $emit}) {
            const router = useRouter();
            /**
             * beforeUpload
             */
            const beforeUpload = (file, fileList) => {
                //如果超过最大上传数量
                if (props['maxNum'] < (props['fileList'].length + fileList.length)) {
                    $ant.errorMsg(`超出最大数量上限`);
                    return false;
                }
                $emit('before-upload', file, fileList);
            }
            /**
             * 上传中、完成、失败都会调用这个函数。
             */
            const change = ({file, event}) => {
                if (null == file.status) {
                    for (let i = 0; i < props['fileList'].length; i++) {
                        if (file['uid'] === props['fileList'][i]['uid']) {
                            props['fileList'].splice(i, 1);
                            break;
                        }
                    }
                    return;
                }
                if ('removed' === file.status) {
                    return;
                }
                //上传过程中
                if ('uploading' === file.status) {
                    return;
                }
                //如果上传失败
                if ('error' === file.status) {
                    for (let i = 0; i < props['fileList'].length; i++) {
                        if (file['uid'] === props['fileList'][i]['uid']) {
                            props['fileList'].splice(i, 1);
                            break;
                        }
                    }
                    $ant.errorMsg('文件上传失败，请重试', 5);
                    console.warn(`${props['action']} 文件上传失败，response: ${JSON.stringify(file['response'])} ,详情:`, file);
                    return;
                }
                //如果没有响应
                if (null == file['response']) {
                    $ant.errorMsg('文件上传失败，请重试', 5);
                    console.warn(`${props['action']} 文件上传失败，response: 无数据 ,详情:`, file);
                    return;
                }
                let {status, error, code, msg} = file['response'];
                if (null != status && 200 !== status) {
                    for (let i = 0; i < props['fileList'].length; i++) {
                        if (file['uid'] === props['fileList'][i]['uid']) {
                            props['fileList'].splice(i, 1);
                            break;
                        }
                    }
                    $ant.errorMsg(`文件上传失败,error:`, error);
                    console.warn(`${props['action']} 文件上传失败，response: ${JSON.stringify(file['response'])} ,详情:`, file);
                    return;
                }
                //如果token过期
                if (401 === code) {
                    $ant.errorMsg('登陆过期，即将跳转到登录页面');
                    setTimeout(() => {
                        router.replace('/login');
                    }, 3000);
                    return;
                }
                //如果请求出错
                if (200 !== code) {
                    for (let i = 0; i < props['fileList'].length; i++) {
                        if (file['uid'] === props['fileList'][i]['uid']) {
                            props['fileList'].splice(i, 1);
                            break;
                        }
                    }
                    $ant.errorMsg(msg);
                    console.warn(`${props['action']} 文件上传失败，response: ${JSON.stringify(file['response'])} ,详情:`, file);
                    return;
                }
                //对外抛出上传成功的图片
                $emit('success', file, props['fileList']);
            }
            //上传的地址
            let realAction = ref(process.env.VUE_APP_URL + props['action']);

            return {
                realAction,
                beforeUpload,
                change
            }
        }
    };
</script>