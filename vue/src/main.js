import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'

import ElementUI from "element-ui";
//引入覆盖element-ui的css
import './assets/sass/element-variables.scss'

if ('production' !== process.env.NODE_ENV) {
    Vue.use(ElementUI);
}

/**引入封装的axios*/
import {weiAxios, weiAxiosDown} from './utils/axios';

Vue.prototype.$axios = weiAxios;
Vue.prototype.$axiosDown = weiAxiosDown;

/**引入全局变量和方法*/
import globalVariable from './utils/global_variable'
import globalFunction from './utils/global_function'
import cryptoJS from './utils/cryptoJS'

Vue.prototype.$global = globalVariable;
Vue.prototype.$globalFun = globalFunction;
Vue.prototype.$cryptoJS = cryptoJS;

new Vue({
    router,
    store,
    render: h => h(App)
}).$mount('#app');