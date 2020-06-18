import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'


//ElementUI
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
Vue.use(ElementUI);

//动画
import animated from 'animate.css'
Vue.use(animated)

//懒加载
import VueLazyload from 'vue-lazyload'
Vue.use(VueLazyload, {
  loading: require('assets/img/loading.png'), //加载中图片，一定要有，不然会一直重复加载占位图
  error: require('assets/img/error.png') //加载失败图片
});

//树形表格
import ZkTable from 'vue-table-with-tree-grid'
Vue.component('tree-table', ZkTable)

//代码高亮
import hljs from 'highlight.js';
import 'highlight.js/styles/atom-one-dark.css'
Vue.directive('highlight', function (el) {
  let blocks = el.querySelectorAll('pre code');
  blocks.forEach((block) => {
    hljs.highlightBlock(block)
  })
})

Vue.config.productionTip = false

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')