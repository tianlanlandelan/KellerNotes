<!-- 登录页面 -->
<template>
	<el-row class="little-container">
		<el-form label-position="left" label-width="0px">
			<div class="alignCenter font24 font-bold marginBottom20">KellerNotes 登录</div>

			<el-form-item>
				<!-- 用户名输入框 -->
				<span class="tip">邮箱 </span>
				<span class="ColorDanger" v-show="!user.emailChecked"> ( 请输入正确的邮箱地址 )</span>
				<el-input type="text" autofocus="autofocus" v-model="user.email" auto-complete="off" placeholder="kellerNotes@foxmail.com"></el-input>

				<!-- 密码输入框 -->
				<span class="tip">密码 </span>
				<span class="ColorDanger" v-show="!user.passwordChecked"> ( 请输入密码 )</span>
				<el-input type="password" v-model="user.password" auto-complete="off" placeholder="6到12位,英文字符和数字的组合"></el-input>
			</el-form-item>
			
			<el-form-item style="width:100%;">
				<!-- 登录按钮 -->
				<el-button type="primary" style="width:100%;" @click="handleLogon()">登录</el-button>
				
				<!-- 忘记密码和注册账号的按钮 -->
				<el-row>
					<el-col :span="8">
						<el-button type="text" @click="handleGoPassword()">忘记密码 ？</el-button>
					</el-col>
					<el-col :span="8" class="alignCenter">
						<el-button type="text" @click="handleGoRegister()">注册账号</el-button>
					</el-col>
					<el-col :span="8" class="alignRight">
						<el-button type="text" @click="handleGoCodeLogin()">验证码登录</el-button>
					</el-col>
				</el-row>
			</el-form-item>
		</el-form>
	</el-row>
</template>

<script>
	import {
		req_logon
	} from '../api';
	import {
		format
	} from "../data.js";
	export default {
		data() {
			return {
				user: {
					email:null,
					password:null,
					emailChecked:true,
					passwordChecked:true
				}
			}
		},
		methods: {
			//登录操作
			handleLogon(){
				this.user.emailChecked = format.isEmail(this.user.email);
				this.user.passwordChecked = format.isPassword(this.user.password);
				
				if (this.user.emailChecked && this.user.passwordChecked) {
					req_logon(this.user).then(response =>{
						let{
							data,
							message,
							success
						} = response;
						if(success != 0){
							this.$message.error(message);
						}else{
							window.localStorage.setItem("token",data);
							this.$router.push({ path: '/Home' });
						}
					});
				}
			},
			//跳转到重置密码页面	
			handleGoPassword(){
				this.$router.push({ path: '/Password' });
			},
			//跳转到注册页面
			handleGoRegister(){
				this.$router.push({ path: '/Register' });
			},
			//跳转到账号密码登录页面
			handleGoCodeLogin(){
				this.$router.push({ path: '/CodeLogin' });
			}
		},
		mounted() {

		}
	}
</script>

<style scoped>
</style>
