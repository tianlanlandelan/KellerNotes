import Login from './views/Login.vue'
import Register from './views/Register.vue'
import CodeLogin from "./views/CodeLogin.vue"
import ForgetPassword from "./views/ForgetPassword.vue"
import ResetPassword from "./views/ResetPassword.vue"
import Home from "./views/Home.vue"
import NotFound from "./views/404.vue"

import Admin from "./views/admin/Admin.vue"


let routes = [{
		path: '/Login',
		component: Login,
		name: "Login"
	},
	{
		path: '/Register',
		component: Register,
		name: 'Register'
	},
	{
		path: '/ForgetPassword',
		component: ForgetPassword,
		name: "ForgetPassword"
	},
	{
		path: '/ResetPassword/:token',
		component: ResetPassword,
		name: "ResetPassword"
	},
	{
		path: '/CodeLogin',
		component: CodeLogin,
		name: 'CodeLogin'
	},
	{
		path: '/Home',
		component: Home,
		name: 'Home'
	},
	{
		path: '/404',
		component: NotFound,
		name: 'NotFound'
	},
	//后台管理页面
	{
		path: '/AdminLogin',
		component: ()=> import("./views/admin/AdminLogin.vue"),
		name: '管理员登录'
	},
	{
		path: '/Admin0',
		component: Admin,
		name: '概览',
		iconCls: 'el-icon-menu',
		children: [{
			path: '/AdminIndex',
			component: () => import("./views/admin/AdminIndex.vue"),
			name: '系统概览'
		}],
		show: true
	},
	{
		path: '/Admin1',
		component: Admin,
		name: '用户管理',
		iconCls: 'el-icon-user-solid',
		children: [{
			path: '/AdminUser',
			component: () => import("./views/admin/AdminUser.vue"),
			name: '用户列表'
		}],
		show: true
	},
	{
		path: '/Admin2',
		component: Admin,
		name: '数据统计',
		iconCls: 'el-icon-s-data',
		children: [{
			path: '/AdminCount',
			component:() => import("./views/admin/AdminCount.vue"),
			name: '统计'
		}],
		show: true
	},
	{
		path: '*',
		redirect: {
			path: '/Login'
		}
	},
	
];

export default routes;
