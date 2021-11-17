import Vue from 'vue'
import Vuex from 'vuex'
import common from './modules/common'
import tab from './modules/tab'
import user from './modules/user'

Vue.use(Vuex)

const store = new Vuex.Store({
  modules: {
    common,
    tab,
    user
  }
})

export default store
// 每一个Vuex应用的核心就是store(仓库)
// store 基本上就是一个容器，它包含着你的应用中大部分的状态（state）
// Vuex 的状态存储是响应式的。当 Vue 组件从 store 中读取状态的时候，
// 若 store 中的状态发生变化，那么相应的组件也会相应地得到高效更新。
// 你不能直接改变 store 中的状态。改变 store 中的状态的唯一途径就是显式地提交（commit）mutation。
