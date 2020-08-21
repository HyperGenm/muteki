import Vue from 'vue'
import Vuex from 'vuex'

/**
 * 生产环境不打包vueRouter
 */
if ('production' !== process.env.NODE_ENV) {
    Vue.use(Vuex);
}

export default new Vuex.Store({
    state: {},
    mutations: {},
    actions: {},
    modules: {}
})