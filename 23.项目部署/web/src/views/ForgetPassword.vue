<!-- 重置密码 -->
<template>
	<div class="little-container">
		<el-form label-position="left" label-width="0px">
			<div class="alignCenter font24 font-bold marginBottom20">忘记密码</div>

			<el-form-item>
				<span class="ColorDanger" v-show="!checked"> (请填写注册时的邮箱地址)</span>
				<el-input type="text" autofocus v-model="email" auto-complete="off" placeholder="请填写注册的邮箱地址"></el-input>
			</el-form-item>
			<el-form-item>				
				<el-button type="primary" style="width:100%;" @click = "sendEmail()" :disabled="disabled">发送重置密码邮件</el-button>
			</el-form-item>
		</el-form>
	</div>
</template>

<script>
	import {
		req_sendResetPasswordEmail
	} from '../api';
	import {
		format
	} from "../data.js";
	export default {
		data() {
			return {
				email:"",
				disabled:false,
				checked:true
			}
		},
		methods: {
			sendEmail(){
				this.checked = format.isEmail(this.email);
				if(this.checked){
					this.disabled = true;
					req_sendResetPasswordEmail(this.email).then(response =>{
						let{
							message,
							success
						} = response;
						if(success != 0){
							this.$message.error(message);
						}else{
							//应答成功
							this.$notify.success({
								title: "操作成功",
								message: "重置密码的邮件已发送到您的邮箱，请及时查收"
							});
						}
					});
				}
			}
		},
		mounted() {

		}
	}
</script>

<style scoped>
</style>
