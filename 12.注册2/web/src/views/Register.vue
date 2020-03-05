<template>
	<el-row class="little-container">
		<!-- 注册界面 -->
		<el-form label-position="left" label-width="0px">
			<div class="alignCenter font24 font-bold marginBottom20">KellerNotes 注册</div>
			<!-- 步骤条 -->
			<el-steps :active="step" finish-status="success">
				<el-step title="验证邮箱"></el-step>
				<el-step title="设置密码"></el-step>
				<el-step title="完善资料"></el-step>
			</el-steps>
			<br>
			<!-- 第一步 ，输入邮箱，获取验证码-->
			<el-row v-if="step == 0">
				<el-form-item>
					<!--邮箱输入框-->
					<span class="ColorCommon font-bold">邮箱</span>
					<span class="ColorDanger" v-show="!user.emailChecked"> (请填写邮箱地址)</span>
					<el-input type="text" v-model="user.email" placeholder="kellerNotes@foxmail.com" autofocus="autofocus"></el-input>
				</el-form-item>
				<el-form-item style="width:100%;">
					<el-button type="primary" style="width:100%;" :disabled="getCodeButtonDisabled" @click="handleGetCode">获取验证码</el-button>
				</el-form-item>
			</el-row>

			<!-- 第二步，输入验证码，设置密码，完成注册-->
			<el-row v-if="step == 1">
				<el-form-item>
					<!-- 验证码，必填 -->
					<span class="ColorCommon font-bold">验证码</span>
					<span class="ColorDanger" v-show="!user.codeChecked"> (请填写验证码)</span>
					<el-input type="text" v-model="user.code" placeholder="6位长度的验证码" autofocus="autofocus" ></el-input>
				</el-form-item>
				<el-form-item style="width:100%;">
					<!--密码输入框-->
					<span class="ColorCommon font-bold">密码</span>
					<span class="ColorDanger" v-show="!user.passwordChecked"> (请设置密码)</span>
					<el-input type="password" v-model="user.password" placeholder="6到12位,英文字符和数字的组合" ></el-input>
				</el-form-item>
				<el-form-item style="width:100%;">
					<el-button type="primary" style="width:100%;" @click="handleRegister">注册</el-button>
				</el-form-item>
			</el-row>

			<!-- 第三步，设置昵称和头像，完善个人信息-->
			<el-row v-if="step == 2">
				<el-form-item>
					<span class="ColorCommon font-bold">设置昵称</span>
					<span class="ColorDanger" v-show="!user.nameChecked"> (请输入昵称)</span>
					<el-input type="text" v-model="user.nickName" placeholder="kyleBlog" @blur="user.checkUserName()"></el-input>
				</el-form-item>
				<el-form-item style="width:100%;">
					<el-row>
						<el-col :span="12">
							<el-row>
								<el-popover placement="bottom-start" width="600" trigger="click" :value="showIcon">
									<el-row class="popover">
										<el-col class="icon_col center" :span="4" v-for="i in 12" :key="i">
											<img class="img" :src="'../../static/icon/' + i + '.png'" @click="handleSelectIcon(i)" />
										</el-col>
									</el-row>
									<el-button slot="reference" type="primary" style="width:100%;" @click="handleSelect">Select an avatar</el-button>
								</el-popover>
							</el-row>
							<el-row class="margin" v-show="showSelected">
								<el-button type="primary" style="width:100%;" @click="handleSubmit" :disabled="!user.nameChecked || user.avatarId == 0">Complete
									sign up</el-button>
							</el-row>

						</el-col>
						<el-col class="center" :span="12" v-show="showSelected">
							<img class="img" :src="'../../static/icon/' + user.avatarId + '.png'" />
						</el-col>
					</el-row>

				</el-form-item>


			</el-row>
		</el-form>
	</el-row>
</template>

<script>
	import {
		req_getCodeForRegister,
		req_register
		// req_updateUserInfo
	} from '../api';
	import {
		format
	} from "../data.js";
	export default {
		data() {
			return {
				getCodeButtonDisabled: false,
				showIcon: false,
				showSelected: false,
				//注册用户数据
				user: {
					email:null,
					code:null,
					password:null,
					emailChecked:true,
					codeChecked:true,
					passwordChecked:true
				},
				//注册界面步骤条当前步骤Index
				stepsActive: 0,
				step: 0,
				dialogImageUrl: '',
				dialogVisible: false,
			};
		},
		methods: {
			/**
			 * 发送验证码
			 */
			handleGetCode() {
				if(format.isEmail(this.user.email)){
					this.user.emailChecked = true;
				}else{
					this.user.emailChecked = false;
					return;
				}
				this.getCodeButtonDisabled = true;
				let email = this.user.email;
				req_getCodeForRegister(email).then(response => {
					//解析接口应答的json串
					let {
						message,
						success
					} = response;
					//应答不成功，提示错误信息
					if (success !== 0) {
						this.$message({
							message: message,
							type: 'error'
						});
					} else {
						//应答成功，提示验证码发送成功
						this.$notify.success({
							title: email,
							message: "验证码已发送到您的邮箱，请及时查收"
						});
						this.step++;
					}
				});

			},
			//注册用户
			handleRegister() {
				this.user.codeChecked = format.isCode(this.user.code);
				this.user.passwordChecked = format.isPassword(this.user.password);
				
				if (this.user.codeChecked && this.user.passwordChecked) {
					//调用注册接口
					req_register(this.user).then(response => {
						//解析接口应答的json串
						let {
							// data,
							message,
							success
						} = response;
						//应答不成功，提示错误信息
						if (success !== 0) {
							this.$message({
								message: message,
								type: 'error'
							});
						} else {
							var userName = this.user.email;
							this.$notify.success({
								title:"注册成功",
								message: "您的账号：" + userName + " 已成功注册，可以登录了"
							});
							this.$router.push({
								path: "/Login"
							});
							
							// 
							// this.user.id = data;
							// this.step++;
						}
					}).catch(response => {
						let {
							message
						} = response;
						this.$message({
							message: message,
							type: 'error'
						});
					});
				}
			},
			handleSelect() {
				this.showIcon = true;
			},
			handleSelectIcon(i) {
				this.showIcon = false;
				this.showSelected = true;
				this.user.avatarId = i;
				window.log(i, this.showIcon);
			},
			handleSubmit() {
				// req_updateUserInfo(this.user).then(response => {
				// 	//解析接口应答的json串
				// 	let {
				// 		message,
				// 		success
				// 	} = response;
				// 	// console.log("req_updateUserInfo success", data, message, success);
				// 	//应答不成功，提示错误信息
				// 	if (success !== 0) {
				// 		this.$message({
				// 			message: message,
				// 			type: 'error'
				// 		});
				// 	} else {
				// 		this.$router.push({
				// 			path: this.path
				// 		});
				// 	}
				// });
			}
		},
		mounted() {
		}
	}
</script>

<style scoped>

</style>
