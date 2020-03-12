import Login from './views/Login.vue'
import Register from './views/Register.vue'
import CodeLogin from "./views/CodeLogin.vue"
import Password from "./views/Password.vue"
import Home from "./views/Home.vue"
import NotFound from "./views/404.vue"

let routes = [
	{	path: '/Login',component: Login,name:"Login"},
    { 	path: '/Register',component: Register, name: 'Register'},
	{	path: '/Password',component: Password,name:"Password"},
	{ 	path: '/CodeLogin',component: CodeLogin, name: 'CodeLogin'},
	{ 	path: '/Home',component: Home, name: 'Home'},
	{ 	path: '/404',component: NotFound, name: 'NotFound'},
	{	path: '*', redirect: { path: '/404' }}
];

export default routes;