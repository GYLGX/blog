import Vue from 'vue'
import Vuex from 'vuex'
Vue.use(Vuex)


export default new Vuex.Store({
  //VueX的核心概念
  state: {
    myInfo: {},
  }, //保存状态
  //添加指定的操作方法
  mutations: {
    setMyInfo(state, count) {
      state.myInfo = count
    },
    updateMyInfo(state, count) {
      state.myInfo.id = count.id
      state.myInfo.url = count.url
      state.myInfo.userName = count.userName
      state.myInfo.sex = count.sex
      sessionStorage.setItem('myInfo', JSON.stringify(state.myInfo))
    },
    updateMyInfoUrl(state, url) {
      state.myInfo.url = url
      sessionStorage.setItem('myInfo', JSON.stringify(state.myInfo))
    },
    //退出登录
    signOutLogin(state) {
      state.myInfo = {}
    }
  },
  actions: {},
  modules: {}
})