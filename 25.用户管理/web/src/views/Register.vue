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
					<span class="ColorDanger" v-show="!user.emailChecked"> (请填写正确的邮箱地址)</span>
					<el-input type="text" v-model="user.email" placeholder="kellerNotes@foxmail.com" autofocus></el-input>
				</el-form-item>
				<el-form-item style="width:100%;">
					<el-button type="primary" style="width:100%;" :disabled="getCodeButtonDisabled" @click="handleGetCode">获取验证码</el-button>
					<!-- 跳转到登录页面的按钮 -->
					<el-row>
						<el-col class="alignRight">
							<el-button type="text" @click="handleGoLogin()">已有账号，直接登录</el-button>
						</el-col>
					</el-row>
				</el-form-item>
			</el-row>

			<!-- 第二步，输入验证码，设置密码，完成注册-->
			<el-row v-if="step == 1">
				<el-form-item>
					<!-- 验证码，必填 -->
					<span class="ColorCommon font-bold">验证码</span>
					<span class="ColorDanger" v-show="!user.codeChecked"> (请填写验证码)</span>
					<el-input type="text" v-model="user.code" placeholder="6位长度的验证码" autofocus="autofocus"></el-input>
				</el-form-item>
				<el-form-item style="width:100%;">
					<!--密码输入框-->
					<span class="ColorCommon font-bold">密码</span>
					<span class="ColorDanger" v-show="!user.passwordChecked"> (6到12位,英文字符和数字的组合)</span>
					<el-input type="password" v-model="user.password" placeholder="6到12位,英文字符和数字的组合"></el-input>
				</el-form-item>
				<el-form-item style="width:100%;">
					<el-button type="primary" style="width:100%;" @click="handleRegister">注册</el-button>
				</el-form-item>
			</el-row>

			<!-- 第三步，设置昵称和头像，完善个人信息 -->
			<el-row v-if="step == 2">
				<el-form-item>
					<!-- 昵称 -->
					<span class="ColorCommon font-bold">设置昵称</span>
					<el-input type="text" v-model="user.nickName" placeholder="往后余生" autofocus="autofocus"></el-input>
				</el-form-item>
				<el-form-item style="width:100%;">
					<!--头像-->
					<span class="ColorCommon font-bold">选择头像</span>
					<el-upload class="avatar-uploader" :action=uploadUrl :show-file-list="false" :on-success="handleAvatarSuccess"
						:before-upload="beforeAvatarUpload">
						<img v-if="imageUrl" :src="imageUrl" class="avatar">
						<i v-else class="el-icon-plus avatar-uploader-icon"></i>
					</el-upload>
				</el-form-item>
				<el-form-item style="width:100%;">
					<el-button type="primary" style="width:100%;" @click="handleSetUserCard">完成</el-button>
				</el-form-item>
			</el-row>
		</el-form>
	</el-row>
</template>

<script>
	import {
		req_getCodeForRegister,
		req_register,
		req_setUserCard,
		upload
	} from '../api';
	import {
		format
	} from "../data.js";
	export default {
		data() {
			return {
				//是否禁用获取验证码按钮
				getCodeButtonDisabled: false,
				//注册用户数据
				user: {
					email: null,
					code: null,
					password: null,
					emailChecked: true,
					codeChecked: true,
					passwordChecked: true,
					nickName: null
				},
				//注册界面步骤条当前步骤Index
				stepsActive: 0,
				step: 0,
				uploadUrl: upload,
				imageUrl: ''
			};
		},
		methods: {
			/**
			 * 发送验证码
			 */
			handleGetCode() {
				if (format.isEmail(this.user.email)) {
					this.user.emailChecked = true;
				} else {
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
							data,
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
								title: "注册成功",
								message: "您的账号：" + userName + " 已成功注册"
							});
							this.step++;
							window.localStorage.setItem("token", data);
							this.uploadUrl = this.uploadUrl + "?token=" + data;
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
			/**
			 * 跳转到登录页面
			 */
			handleGoLogin() {
				this.$router.push({
					path: '/Login'
				});
			},
			/**
			 * 设置用户名片，设置完成后跳转到用户主页
			 */
			handleSetUserCard() {
				req_setUserCard(this.user).then(response => {
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
							title: this.user.nickName,
							message: "名片设置成功"
						});
						this.$router.push({
							path: "/Home"
						});
					}
				});
			},
			beforeAvatarUpload(file) {
				const isJPG = file.type === 'image/jpeg';
				const isLt2M = file.size / 1024 / 1024 < 2;

				if (!isJPG) {
					this.$message.error('上传头像图片只能是 JPG 格式!');
				}
				if (!isLt2M) {
					this.$message.error('上传头像图片大小不能超过 2MB!');
				}
				return isJPG && isLt2M;
			},
			handleAvatarSuccess(res, file) {
				this.imageUrl = URL.createObjectURL(file.raw);
			}
		},
		mounted() {

		}
	}
</script>

<style scoped>
	.avatar-uploader .el-upload {
		border: 1px dashed #d9d9d9;
		border-radius: 6px;
		cursor: pointer;
		position: relative;
		overflow: hidden;
	}

	.avatar-uploader .el-upload:hover {
		border-color: #409EFF;
	}

	.avatar-uploader-icon {
		font-size: 28px;
		color: #8c939d;
		width: 178px;
		height: 178px;
		line-height: 178px;
		text-align: center;
	}

	.avatar {
		width: 178px;
		height: 178px;
		display: block;
	}
</style>
