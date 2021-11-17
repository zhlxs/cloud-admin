import Vue from 'vue'
import Router from 'vue-router'
// import { component } from 'vue/types/umd'
// import HelloWorld from '@/components/HelloWorld'

import Main from '../views/Main.vue'
// 解决路由重复点击的问题
const originPush = Router.prototype.push
Router.prototype.push = function push (location) {
  return originPush.call(this, location).catch(err => err)
}

Vue.use(Router)

export default new Router({
  mode: 'history',
  routes: [
    {
      path: '/login',
      name: 'login',
      component: () => import('@/views/login/LoginPlus')
    },
    {
      path: '/',
      name: 'Main',
      component: Main,
      children: [
        {
          path: '/',
          name: 'home',
          component: () => import('@/views/home/Home')
        },
        {
          path: '/user',
          name: 'user',
          component: () => import('@/views/user/User')
        },
        {
          path: '/auth',
          name: 'auth',
          component: () => import('@/views/auth/Auth')
        }
      ]
    }
  ]
})
