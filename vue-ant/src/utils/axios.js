/**引入axios*/
import axios from "axios";
/**引入参数处理*/
import Qs from 'qs';
/**引入ant-design-vue组件*/
import {message} from 'ant-design-vue';
/*引入全局方法*/
import $function from './function'
/*引入ant方法*/
import $ant from './ant'
//引入router
import $router from '../router/index';
//请求拦截器
axios.interceptors.request.use(
    config => {
        return config;
    },
    error => {
        // 对请求错误的处理
        return Promise.reject(error);
    }
);

/**
 *
 * 封装axios请求
 *
 * @param allUrl 请求的url为完整url
 * @param allSuccess 返回所有成功回调,不包含status不是200的出错请求
 * @param url 请求地址
 * @param method 请求方式
 * @param contentType 请求头contentType
 * @param data 请求参数
 * @param timeout 请求超时时间---某些请求需要单独设置超时时间
 * @param timeShowLoadAnimation 多长时间之后显示加载中动画,单位毫秒
 * @param success 成功回调
 * @param fail 失败回调
 * @returns {Promise<any>}
 */
const $axios = function (
    {
        allUrl = false,
        allSuccess = false,
        url = '',
        method = 'get',
        contentType = 'application/x-www-form-urlencoded; charset=UTF-8',
        data = {},
        timeout = parseInt(process.env.VUE_APP_AXIOS_TIMEOUT || 20000),
        timeShowLoadAnimation = 1111,
        success = function () {
        },
        fail = function () {
        }
    } = {}) {
    /**timeShowLoadAnimation时间之后开启加载中动画*/
    let loading = null;
    let messageKey = $function.createUUID();
    let loadingTimer = setTimeout(() => {
        loading = true;
        message.loading({
            content: 'loading...',
            key: messageKey,
            duration: timeout / 1000
        });
    }, timeShowLoadAnimation);
    let _axios = {
        //请求的url是否为全部url
        url: allUrl ? url : (process.env.VUE_APP_URL + url),
        method,
        headers: {
            'Content-Type': contentType,
            'token': $function.getLocationStorage('token')
        },
        timeout
    };
    /**axios请求参数添加随机字符串*/
    data['__t'] = (new Date()).getTime();
    /**axios请求处理不同请求方式时的参数*/
    //如果是文件
    if (contentType.includes('multipart/form-data')) {
        _axios['data'] = data;
    } else {
        //不是文件
        if ('GET' === method.toUpperCase()) {
            _axios['params'] = data;
        } else {
            _axios['data'] = Qs.stringify(data, {indices: false});
        }
    }
    axios(_axios).then((res) => {
        /**关闭加载中动画*/
        clearTimeout(loadingTimer);
        if (loading) {
            message.info({
                content: 'loaded',
                key: messageKey,
                duration: 1
            });
        }
        /***请求的url如果是全部url的话,返回所有res['data']响应***/
        if (allUrl) {
            try {
                success(res['data']);
            } catch (e) {
                console.error(e);
            }
            return;
        }
        /**token过期处理*/
        if (401 === res.data.code) {
            $ant.errorMsg('登陆过期，自动登录中。。。');
            let timer = setTimeout(() => {
                clearTimeout(timer);
                $router.replace('/login');
            }, 3000);
            return;
        }
        /**返回所有成功回调,不包含status不是401的出错请求*/
        if (allSuccess) {
            try {
                success(res.data);
            } catch (e) {
                console.error(e);
            }
            return;
        }
        /**处理code不为0的出错请求*/
        if (200 !== res.data.code) {
            $ant.errorMsg(res.data.msg);
            consoleWarnTable(`请求出错url:${url}`, res['data']);
            return;
        }
        /**成功回调*/
        try {
            success(res.data.data);
        } catch (e) {
            console.error(e);
        }
    }).catch((error) => {
        /**关闭加载中动画*/
        clearTimeout(loadingTimer);
        if (loading) {
            message.warn({
                content: 'error',
                key: messageKey,
                duration: 1
            });
        }
        // 如果请求被取消则进入该方法
        if (axios.isCancel(error)) {
            try {
                fail(error);
            } catch (e) {
                console.error(e);
            }
            return;
        }
        $ant.errorMsg(`请求失败，详情:${JSON.stringify(error)}`);
        console.warn(`请求失败url:${url}`, error);
        try {
            fail(error);
        } catch (e) {
            console.error(e);
        }
    });
}

/**
 * 打印
 * @param msg
 * @param object
 */
function consoleWarnTable(msg, object = {}) {
    console.warn(msg);
    try {
        if (object instanceof Object) {
            console.table(object);
        } else {
            console.log(object);
        }
    } catch (e) {
        console.log('此浏览器不支持console.table()', e, '---错误详情:', object);
    }
    console.warn('↑↑以上为错误详情↑↑↑↑↑');
}

/**封装axios下载请求
 *
 * @param url
 * @param method
 * @param data
 * @param filename 文件名字
 * @param timeout 请求超时时间
 * @param timeShowLoadAnimation 多长时间之后显示加载中动画，单位毫秒
 * @param success
 * @param fail
 * @returns {Promise<any>}
 */
const $axiosDown = function (
    {
        url = '',
        method = 'post',
        data = {},
        filename = '新建文件',
        timeout = 20000,
        timeShowLoadAnimation = 1111,
        success = function () {
        },
        fail = function () {
        }
    } = {}) {
    /**timeShowLoadAnimation时间之后开启加载中动画*/
    let loading = null;
    let messageKey = $function.createUUID();
    let loadingTimer = setTimeout(() => {
        loading = true;
        message.loading({
            content: '下载打包中...',
            key: messageKey,
            duration: timeout / 1000
        });
    }, timeShowLoadAnimation);
    let _axios = {
        //请求的url是否为全部url
        url: process.env.VUE_APP_URL + url,
        method,
        responseType: 'blob',
        headers: {
            'token': $function.getLocationStorage('token')
        },
        timeout
    };
    /**axios请求参数添加随机字符串*/
    data['__t'] = (new Date()).getTime();
    /**axios请求处理不同请求方式时的参数*/
    method = method.toUpperCase();
    if (method === 'GET') {
        _axios['params'] = data;
    } else {
        _axios['data'] = Qs.stringify(data, {indices: false});
    }
    axios(_axios).then((res) => {
        /**关闭加载中动画*/
        clearTimeout(loadingTimer);
        if (loading) {
            message.success({
                content: 'success',
                key: messageKey,
                duration: 1
            });
        }

        let {data} = res;
        let fileReader = new FileReader();
        fileReader.readAsText(data);
        fileReader.onload = function () {
            // 如果JSON.parse(this.result)不报错，说明this.result是json字符串，是下载报错情况的返回值，弹框提示
            // 如果JSON.parse(this.result)报错，说明下载成功，进入catch
            try {
                let resData = JSON.parse(this.result);
                if (resData && resData['code']) {
                    /**token过期处理*/
                    if (401 === resData.code) {
                        $ant.errorMsg('登陆过期，自动登录中。。。');
                        let timer = setTimeout(() => {
                            clearTimeout(timer);
                            $router.replace('/login');
                        }, 3000);
                        return;
                    }
                    /**处理code不为0的出错请求*/
                    if (200 !== resData.code) {
                        $ant.errorMsg(resData.msg);
                        consoleWarnTable(`请求出错url:${url}`, resData['data']);
                        return;
                    }
                    /**成功回调*/
                    try {
                        success(resData);
                    } catch (e) {
                        console.error(e);
                    }
                }
            } catch (error) {
                let blob = new Blob([data]);
                // 兼容ie11
                if (window.navigator.msSaveOrOpenBlob) {
                    window.navigator.msSaveOrOpenBlob(blob, filename);
                } else {
                    let downloadElement = document.createElement('a');
                    //创建下载的链接
                    let href = window.URL.createObjectURL(blob);
                    downloadElement.href = href;
                    //下载后文件名
                    downloadElement.download = filename;
                    document.body.appendChild(downloadElement);
                    //点击下载
                    downloadElement.click();
                    //下载完成移除元素
                    document.body.removeChild(downloadElement);
                    //释放掉blob对象
                    window.URL.revokeObjectURL(href);
                }
            }
        };
    }).catch((error) => {
        /**关闭加载中动画*/
        clearTimeout(loadingTimer);
        if (loading) {
            message.warn({
                content: 'fail',
                key: messageKey,
                duration: 1
            });
        }
        // 如果请求被取消则进入该方法
        if (axios.isCancel(error)) {
            try {
                fail(error);
            } catch (e) {
                console.error(e);
            }
            return;
        }
        $ant.errorMsg(`文件下载失败，详情:${JSON.stringify(error)}`);
        console.warn(`文件下载失败url:${url}`, error);
        try {
            fail(error);
        } catch (e) {
            console.error(e);
        }
    });
}

export {
    $axiosDown
}

/**
 * 对外抛出
 */
export default $axios;