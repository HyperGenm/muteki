<template>
    <div id="editor" ref="editor"></div>
</template>

<script>
    import E from 'wangeditor';
    import {reactive, onMounted} from 'vue';

    export default {
        name: "Index",
        props: {
            html: {
                type: String
            },
            //自定义菜单配置
            menus: {
                type: Array,
                default: () => [
                    'head',  // 标题
                    'bold',  // 粗体
                    'fontSize',  // 字号
                    'fontName',  // 字体
                    'italic',  // 斜体
                    'underline',  // 下划线
                    'strikeThrough',  // 删除线
                    'foreColor',  // 文字颜色
                    'backColor',  // 背景颜色
                    'link',  // 插入链接
                    'list',  // 列表
                    'justify',  // 对齐方式
                    'quote',  // 引用
                    // 'emoticon',  // 表情
                    'image',  // 插入图片
                    'table',  // 表格
                    // 'video',  // 插入视频
                    // 'code',  // 插入代码
                    'undo',  // 撤销
                    'redo'  // 重复
                ]
            },
            // 配置编辑区域的 z-index
            zIndex: {
                type: Number,
                default: 100
            },
            //关闭粘贴样式的过滤
            pasteFilterStyle: {
                type: Boolean,
                default: false
            },
            //忽略粘贴内容中的图片
            pasteIgnoreImg: {
                type: Boolean,
                default: false
            },
            //自定义配置颜色（字体颜色、背景色）
            colors: {
                type: Array,
                default: () => [
                    '#000000',
                    '#eeece0',
                    '#1c487f',
                    '#4d80bf',
                    '#c24f4a',
                    '#8baa4a',
                    '#7b5ba1',
                    '#46acc8',
                    '#f9963b',
                    '#ffffff'
                ]
            },
            //自定义字体
            fontNames: {
                type: Array,
                default: () => [
                    '宋体',
                    '微软雅黑',
                    'Arial',
                    'Tahoma',
                    'Verdana'
                ]
            },
            //使用 base64 保存图片---使用接口请看mounted()方法配置
            uploadImgShowBase64: {
                type: Boolean,
                default: true
            }
        },
        mounted() {
            //对外抛出自身
            this.$emit('my-ref', this);
        },
        setup(props, {emit: $emit}) {
            let editor = null;
            onMounted(() => {
                editor = new E('#editor');
                editor.config.onchange = (htmlValue) => {
                    //更新html的值
                    $emit('update:html', htmlValue);
                };
                // 自定义菜单配置
                editor.config.menus = props['menus'];
                //开发环境开启debug---debug模式下，有 JS 错误会以throw Error方式提示出来。
                editor.config.debug = process.env.NODE_ENV !== 'production';
                //配置编辑区域的 z-index
                editor.config.zIndex = props['zIndex'];
                // 关闭粘贴样式的过滤
                editor.config.pasteFilterStyle = props['pasteFilterStyle'];
                // 忽略粘贴内容中的图片
                editor.config.pasteIgnoreImg = props['pasteIgnoreImg'];
                //配置onfocus函数之后，用户点击富文本区域会触发onfocus函数执行。
                editor.config.onfocus = () => {
                    $emit('onfocus');
                };
                //如果当前有手动获取焦点的富文本并且鼠标点击富文本以外的区域，则会触发onblur函数执行。
                editor.config.onblur = (html) => {
                    $emit('onblur', html);
                };
                // 自定义配置颜色（字体颜色、背景色）
                editor.config.colors = props['colors'];
                // 自定义字体
                editor.config.fontNames = props['fontNames'];
                // 下面两个配置，使用其中一个即可显示“上传图片”的tab。但是两者不要同时使用！！！
                // 使用 base64 保存图片
                editor.config.uploadImgShowBase64 = props['uploadImgShowBase64'];
                // 上传图片到服务器
                // editor.customConfig.uploadImgServer = '/upload'
                editor.create();
                //设置内容
                editor.txt.html(props['html']);
            });
            let data = reactive({
                editor
            })
            return {
                data
            }
        }
    }
</script>