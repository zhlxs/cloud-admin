// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'
import http from 'axios'

import store from './store'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import Router from 'vue-router'
import 'font-awesome/css/font-awesome.min.css'
import '@/assets/scss/reset.scss' // 全局样式

import VCharts from 'v-charts'
// import Directives from 'vue-directives'

Vue.use(ElementUI)
Vue.use(Router)
Vue.use(VCharts)
// Vue.use(Directives)

Vue.prototype.$http = http

Vue.config.productionTip = false
/**
  * 判断当前是否登录，未登录不能跳转路由
  * 防止未登录状态下直接输入路由跳转
  */
router.beforeEach((to, from, next) => {
  if (to.path === '/login') {
    sessionStorage.removeItem('user')
  }
  let user = JSON.parse(sessionStorage.getItem('user'))
  if (!user && to.path !== '/login') {
    next({
      path: '/login'
    })
  } else {
    next()
  }
})

/* eslint-disable no-new */
// new Vue({
//   el: '#app',
//   router,
//   components: { App },
//   template: '<App/>'
// })

// 引入路由
new Vue({
  el: '#app',
  store,
  router, // 注入到根实例中
  render: h => h(App)
})
