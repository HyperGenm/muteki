import {createRouter, createWebHashHistory, RouteRecordRaw} from 'vue-router';
import MyRouter from './MyRouter';
import $function from '@/utils/function';
import {message} from 'ant-design-vue';
/*浏览器上面进度条*/
// @ts-ignore
import NProgress from 'nprogress';
import 'nprogress/nprogress.css';

NProgress.configure({
    // 是否显示加载ico
    showSpinner: false,
});

const router = createRouter({
    history: createWebHashHistory(process.env.BASE_URL),
    routes: [
        {
            path: '/login',
            name: 'login',
            component: () => import('@/views/login/Index.vue')
        },
        {
            path: '/404',
            name: '404',
            component: () => import('@/views/common/errorPage/404.vue')
        }
    ]
});

router.beforeEach((to, from, next) => {
    try {
        //清空提示信息
        message.destroy()
    } catch (e) {
        console.log('清空加载中动画出错，此提示可忽略。', e);
    }
    //浏览器上方显示进度条
    NProgress.start();
    let token = $function.getLocationStorage('token');
    //如果没有token跳转到登录页面
    if ($function.isBlank(token) && 'login' !== to.name) {
        next('/login');
        return;
    }
    //刷新页面，重新初始化路由
    if ($function.isBlank(from.name) && $function.isBlank(to.name)) {
        MyRouter.initRouter();
        return;
    }
    //正常放行
    try {
        if ('login' !== to.name) {
            //保存最后一个访问路径
            $function.setSessionStorage('lastToPath', to.path);
        }
        next();
    } catch (e) {
        console.log(e)
    }
});

router.afterEach(transition => {
    //关闭浏览器上方的进度条
    NProgress.done();
});

/**
 * 处理路由异常，一般是组件没有创建 404
 */
router.onError(error => {
    if (('' + error).startsWith('Error: Couldn\'t resolve component "default" at')) {
        console.log('请检查对应组件是否创建成功。error详情:', error);
        router.push('/404');
    }
    console.error(error);
})

export default router;