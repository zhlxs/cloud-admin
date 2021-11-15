export default {
  state: {
    isCollapse: false,
    currentMenu: null,
    tabList: [
      {
        path: '/',
        name: 'home',
        label: '首页',
        icon: 'home'
      }
    ]
  },
  mutations: {
    collapseMenu (state) {
      state.isCollapse = !state.isCollapse
    },
    // 选择标签 选择面包屑
    selectMenu (state, val) {
      // val.name === 'home' ? (state.currentMenu = null) : (state.currentMenu = val)
      if (val.name !== 'home') {
        state.currentMenu = val
        let result = state.tabList.findIndex(item => item.name === val.name)
        if (result === -1) {
          state.tabList.push(val)
        }
        // result === -1 ? state.tabsList.push(val) : ''
        // Cookie.set('tagList', JSON.stringify(state.tabsList))
      } else {
        state.currentMenu = null
      }
      // val.name === 'home' ? (state.currentMenu = null) : (state.currentMenu = val)
    },
    // 关闭标签
    closeTab (state, val) {
      let result = state.tabList.findIndex(item => item.name === val.name)
      state.tabList.splice(result, 1)
      // Cookie.set('tagList', JSON.stringify(state.tabsList))
    }
  }
}
