<!-- 重置密码 -->
<template>
	<div class="little-container">
		<el-form label-position="left" label-width="0px">
			<div class="alignCenter font24 font-bold marginBottom20">重置密码</div>
			<el-form-item>
				<span class="ColorCommon font-bold">密码</span>
				<span class="ColorDanger" v-show="!checked"> (6到12位,英文字符和数字的组合)</span>
				<el-input type="password" autofocus v-model="password" auto-complete="off"></el-input>
			</el-form-item>
			<el-form-item>
				<el-button type="primary" style="width:100%;" @click="resetPassword()" :disabled="disabled">重置密码</el-button>
			</el-form-item>
		</el-form>
	</div>
</template>

<script>
	import {
		req_resetPasswordByEmail
	} from '../api';
	import {
		format
	} from "../data.js";
	export default {
		data() {
			return {
				password: null,
				token: null,
				checked: true,
				disabled:false
			}
		},
		methods: {
			resetPassword() {
				this.checked = format.isPassword(this.password);
				if (this.checked) {
					this.disabled = true;
					req_resetPasswordByEmail(this.password, this.token).then(response => {
						let {
							message,
							success
						} = response;
						if (success != 0) {
							this.$message.error(message);
						} else {
							//应答成功
							this.$notify.success({
								title: "操作成功",
								message: "密码重置完成"
							});
							this.$router.push({
								path: '/Login'
							});
						}
					});
				}

			}
		},
		mounted() {
			this.token = this.$route.params.token;
		}
	}
</script>

<style scoped>
</style>
