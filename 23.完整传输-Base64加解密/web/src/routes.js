import Login from './views/Login.vue'
import Register from './views/Register.vue'
import CodeLogin from "./views/CodeLogin.vue"
import ForgetPassword from "./views/ForgetPassword.vue"
import ResetPassword from "./views/ResetPassword.vue"
import Home from "./views/Home.vue"
import NotFound from "./views/404.vue"

let routes = [
	{	path: '/Login',component: Login,name:"Login"},
    { 	path: '/Register',component: Register, name: 'Register'},
	{	path: '/ForgetPassword',component: ForgetPassword,name:"ForgetPassword"},
	{	path: '/ResetPassword/:token',component: ResetPassword,name:"ResetPassword"},
	{ 	path: '/CodeLogin',component: CodeLogin, name: 'CodeLogin'},
	{ 	path: '/Home',component: Home, name: 'Home'},
	{ 	path: '/404',component: NotFound, name: 'NotFound'},
	{	path: '*', redirect: { path: '/Login' }}
];

export default routes;