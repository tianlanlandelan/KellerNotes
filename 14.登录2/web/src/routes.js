import Login from './views/Login.vue'
import Register from './views/Register.vue'
import NotFound from "./views/404.vue"

let routes = [
	{	path: '/Login',component: Login,name:"Login"},
    { 	path: '/Register',component: Register, name: 'Register'},
	{ 	path: '/404',component: NotFound, name: 'NotFound'},
	{	path: '*', redirect: { path: '/404' }}
];

export default routes;