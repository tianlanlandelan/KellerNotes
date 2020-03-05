//引入 vue
import Vue from 'vue'
//引入 App.vue 页面
import App from './App.vue'
//引入 vue-router
import VueRouter from 'vue-router'
//引入 element-ui
import ElementUI from 'element-ui'
//引入 element-ui 样式表
import 'element-ui/lib/theme-chalk/index.css'
//引入路由表
import routes from './routes'
//引入自定义样式表
import './index.css'

Vue.use(ElementUI)
Vue.use(VueRouter)


const router = new VueRouter({
	//使用history模式来避免页面输入路由参数后自动加 # 
	mode: 'history',
	routes
})

new Vue({
	router,
  render: h => h(App)
}).$mount('#app')
