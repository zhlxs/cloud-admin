import Cookies from 'js-cookie'
const user = {
  state: {
    token: '',
    userName: '',
    nickName: '',
    deptId: '',
    roles: [],
    paths: [],
    isLogin: false
  }
}
const mutations = {
  changeLoginState (state, val) {
    state.isLogin = val
  },
  saveLoginInfo (state, val) {
    if (val.user) {
      state.userName = val.user.username ? val.user.username : ''
      state.nickName = val.user.nickname ? val.user.nickname : ''
      // state.roles = val.user.roles
    }
    if (val.jwtToken && val.jwtToken.token) {
      Cookies.set('User-Token', val.jwtToken.token)
    }
  }
}

const actions = {
  // 简写方式,待研究
  changeLoginAction ({ commit }) {
    // 此处value可以是对象,可以是固定值等
    commit('changeLoginState', true)
  },
  saveLoginAction ({ commit }, userInfo) {
    // 此处value可以是对象,可以是固定值等
    commit('saveLoginInfo', userInfo)
  }
}

// getters
const getters = {
  userName: state => {
    return state.userName
  },
  token: state => {
    return state.token
  },
  roles: state => {
    return state.roles
  }
}

export default {
  user,
  mutations,
  actions,
  getters
}
