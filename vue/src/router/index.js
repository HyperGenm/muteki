import Vue from 'vue'
import VueRouter from 'vue-router'

/**
 * 引入全局方法
 */
import globalFunction from '../utils/global_function';

/*浏览器上面进度条*/
import NProgress from 'nprogress'
import 'nprogress/nprogress.css'

NProgress.configure({
    showSpinner: false, // 是否显示加载ico
});

/**
 * 生产环境不打包vueRouter
 */
if ('production' !== process.env.NODE_ENV) {
    Vue.use(VueRouter);
    //解决点击当前路由报错问题
    const originalPush = VueRouter.prototype.push;
    VueRouter.prototype.push = function push(location) {
        return originalPush.call(this, location).catch(err => err)
    };
}

/**
 * 默认只加载登录
 * @type {Router}
 */
const router = new VueRouter({
    // mode: 'history',
    base: process.env.BASE_URL,
    routes: [
        {
            path: '/login',
            name: 'login',
            component: () => import('@/views/login/Index')
        }
    ]
});

/**
 *取消上一个页面的请求
 */
function clearAxiosCancelToken() {
    let cancelArray = window['_axiosCancelToken'] || [];
    cancelArray.forEach(value => {
        value();
    });
    window['_axiosCancelToken'] = [];
}

/**
 * 重新生成路由
 * @returns {{children: Array, haveHomePage: boolean}}
 */
function handleRouterChildren() {
    let menuTree = globalFunction.getSessionStorage('menuTree');
    let routers = [];
    let haveHomePage = false;
    menuTree.forEach((value) => {
        if ('home' === value['name']) {
            haveHomePage = true;
        }
        let router = {
            path: "/" + (2 === value['externalFlag'] ? value.name : value.path),
            name: value.path,
            meta: {
                title: value.title,
                icon: value.icon,
                externalUrl: value['path']
            }
        };
        let children = value.children;
        //如果存在子级
        if (null != children && 0 < children.length) {
            router['components'] = require('@/views/common/station/Index.vue');
            router['redirect'] = `/${value.path}/${children[0]['path']}`;
            router['children'] = childrenRouter(value.path, children);
        } else {
            //如果是外部超链接
            if (2 === value['externalFlag']) {
                router['components'] = require(`@/views/common/externalUrl/Index.vue`);
            } else {
                try {
                    router['components'] = require(`@/views/main/${value.path}/Index.vue`);
                } catch (e) {
                    console.debug(value.path, '没有找到对应组件', '---详情', e);
                    router['components'] = require(`@/views/common/errorPage/404.vue`);
                }
            }
        }
        routers.push(router);
    });
    return {
        haveHomePage,
        children: routers
    };
}

function childrenRouter(parentPath, data) {
    let routers = [];
    data.forEach((value) => {
        let router = {
            path: (2 === value['externalFlag'] ? value.name : value.path),
            name: value.path,
            meta: {
                title: value.title,
                icon: value.icon,
                externalUrl: value['path']
            }
        };
        let children = value.children;
        //如果存在子级
        if (null != children && 0 < children.length) {
            router['components'] = require('@/views/common/station/Index.vue');
            router['redirect'] = `/${parentPath}/${value.path}/${children[0]['path']}`;
            router['children'] = childrenRouter(parentPath + '/' + value.path, children);
        } else {
            //如果是外部超链接
            if (2 === value['externalFlag']) {
                router['components'] = require(`@/views/common/externalUrl/Index.vue`);
            } else {
                try {
                    router['components'] = require(`@/views/main/${parentPath}/${value.path}/Index.vue`);
                } catch (e) {
                    console.debug(value.path, '没有找到对应组件', '---详情', e);
                    router['components'] = require(`@/views/common/errorPage/404.vue`);
                }
            }
        }
        routers.push(router);
    });
    return routers;
}

router.beforeEach((to, from, next) => {
    clearAxiosCancelToken();
    //浏览器上方显示进度条
    NProgress.start();
    const token = globalFunction.getSessionStorage(`token`);
    if ((null == token || '' === token) && 'login' !== to.name) {
        next('/login');
        return;
    }
    //刷新页面，重新初始化路由
    if ((null == from.name || '' === from.name) && (null == to.name || '' === to.name)) {
        let {children, haveHomePage} = handleRouterChildren();
        let parentRouters = [{
            path: '/',
            components: require('@/views/common/layout/Index.vue'),
            name: 'index',
            children
        }];
        router.addRoutes(parentRouters);
        let lastToPath = globalFunction.getSessionStorage('lastToPath');
        if (null != lastToPath && '' !== lastToPath) {
            next(lastToPath);
            return;
        }
        next(haveHomePage ? '/home' : children[0]['path'] || '/');
        return;
    }
    globalFunction.setSessionStorage('lastToPath', to.path);
    //正常放行
    next();
});

router.afterEach(() => {
    //关闭浏览器上方的进度条
    NProgress.done();
});

export default router;