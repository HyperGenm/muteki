// @ts-ignore
import {RouteRecordRaw} from 'vue-router';
import $function from "@/utils/function";
import router from "@/router/index";
import {defineComponent, defineAsyncComponent} from 'vue';

/**
 * 处理路由
 * @param value
 * @param parentPath
 */
const handleRouter = (value: any, parentPath = '') => {
    let router: any = {
        path: 2 === value['externalFlag'] ? value['name'] : value['path'],
        name: value['name'],
        meta: {
            title: value['title'],
            id: value['id'],
            externalUrl: value['path'],
            icon: value['icon']
        }
    };
    let {children} = value;
    //没有子级路由
    if (null == children || 0 >= children.length) {
        if (2 === value['externalFlag']) {
            router['component'] = () => import(`@/views/common/externalUrl/Index.vue`);
        } else {
            router['component'] = () => import(`@/views/main/${parentPath}${value['path']}/Index.vue`);
        }
    } else {
        router['component'] = () => import(`@/views/common/station/Index.vue`);
        router['redirect'] = `${parentPath}/${value.path}/${children[0]['path']}`;
        router['children'] = handleChildrenRouters(value['children'], `${parentPath}${value['path']}/`);
    }
    return router;
}

/**
 * 处理子级路由
 */
const handleChildrenRouters = (children: Array<any>, parentPath = '') => {
    let routes: Array<RouteRecordRaw> = [];
    children.forEach(value => {
        routes.push(handleRouter(value, parentPath));
    });
    return routes;
};

/**
 * 初始化路由
 */
const initRouter = () => {
    let menuTree = $function.getLocationStorage('menuTree') || [];
    //生成路由列表
    let routers = handleChildrenRouters(menuTree, '');
    //添加404页面
    routers.push({
        path: '/:pathMatch(.*)*',
        name: '404',
        component: () => import('@/views/common/errorPage/404.vue')
    });
    router.addRoute({
        path: '/',
        name: 'index',
        component: defineAsyncComponent(() => import(`@/views/common/layout/Index.vue`)),
        children: routers,
        redirect: `/${menuTree[0]['path']}`
    });
    //是否有首页
    let homePageFlag: boolean = false;
    for (let i = 0; i < menuTree.length; i++) {
        if ('home' === menuTree[i]['name']) {
            homePageFlag = true;
            break;
        }
    }
    let lastToPath = $function.getSessionStorage('lastToPath');
    if (!$function.isBlank(lastToPath)) {
        //跳转到最后一次的访问路径
        router.push(lastToPath);
        return;
    }
    router.push(homePageFlag ? '/home' : '/');
};

export default {
    initRouter
}