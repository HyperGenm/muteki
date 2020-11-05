//ant组件的常用方法
// @ts-ignore
import {message, Modal} from 'ant-design-vue';
// @ts-ignore
import {createVNode} from 'vue';

/**
 * 错误警告
 * @param content
 * @param duration
 */
function errorMsg(content: string = 'error', duration: number = 3) {
    message.warning(content, duration);
}

/**
 * 成功提示
 * @param content
 * @param duration
 */
function successMsg(content: string = 'success', duration: number = 3) {
    message.success(content, duration);
}

/**
 * 弹窗
 *
 * @param type   类型
 * @param centered 垂直居中展示 Modal
 * @param closable 是否显示右上角的关闭按钮
 * @param content 内容
 * @param mask 是否展示遮罩
 * @param maskClosable 点击蒙层是否允许关闭
 * @param keyboard 是否支持键盘 esc 关闭
 * @param okText 确认按钮文字
 * @param okType 确认按钮类型
 * @param title 标题
 * @param width 宽度
 * @param onOk 点击确定回调，参数为关闭函数，返回 promise 时 resolve 后自动关闭
 */
function alert(
    {
        type = 'info',
        centered = false,
        closable = false,
        content = '',
        mask = true,
        maskClosable = true,
        keyboard = true,
        okText = '确定',
        okType = 'primary',
        title = '标题',
        width = 416,
        onOk = () => {
        }
    } = {}) {
    return Modal[type]({
        centered,
        closable,
        content,
        mask,
        maskClosable,
        keyboard,
        okText,
        okType,
        title,
        width,
        onOk
    });
}

/**
 * 确认弹窗
 *
 * @param showInput 展示输入框
 * @param inputType 输入框原生type
 * @param cancelText 取消按钮文字
 * @param centered 垂直居中展示 Modal
 * @param content 内容
 * @param mask 是否展示遮罩
 * @param maskClosable 点击蒙层是否允许关闭
 * @param keyboard 是否支持键盘 esc 关闭
 * @param okText 确认按钮文字
 * @param okType 确认按钮类型
 * @param title 标题
 * @param width 宽度
 * @param onCancel 取消回调，参数为关闭函数，返回 promise 时 resolve 后自动关闭
 * @param onOk 点击确定回调，参数为关闭函数，返回 promise 时 resolve 后自动关闭
 */
function confirm(
    {
        showInput = false,
        inputType = 'text',
        cancelText = '取消',
        centered = false,
        content = '',
        mask = true,
        maskClosable = true,
        keyboard = true,
        okText = '确认',
        okType = 'primary',
        title = '警告',
        width = 416,
        onCancel = () => {

        },
        onOk = () => {

        }
    } = {}) {
    let _content: any = '';
    let id = '_modalInput' + new Date().getTime();
    //如果展示input输入框
    if (showInput) {
        _content = createVNode('div', {}, [
            createVNode('div', {
                color: '#666'
            }, content),
            createVNode('input', {
                class: 'ant-input',
                id,
                name: id,
                type: inputType
            })
        ]);
    } else {
        _content = content;
    }
    Modal.confirm({
        cancelText,
        centered,
        content: _content,
        mask,
        maskClosable,
        keyboard,
        okText,
        // @ts-ignore
        okType,
        title,
        width,
        onCancel,
        // @ts-ignore
        onOk(done: any) {
            if (!showInput) {
                done();
                onOk();
                return;
            }
            //如果有input输入框
            // @ts-ignore
            let value: any = document.getElementById(id).value;
            // @ts-ignore
            onOk(done, value);
        }
    });
}

//对外抛出方法
export default {
    errorMsg,
    successMsg,
    alert,
    confirm,
}