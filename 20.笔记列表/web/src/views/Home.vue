<!-- 用户主页 -->
<template>
	<div>

		<el-container>
			<!-- 页头 -->
			<el-header class="header">
				<el-row>
					<!-- 应用名 -->
					<el-col :span="4" class="font20 ColorInfo font-bold">
						Keller Notes
					</el-col>
					<!-- 个性签名 -->
					<el-col :span="10" class="ColorInfo">
						{{user.label}}
					</el-col>
					<el-col :span="10" class="alignRight">
						<div>
							<!-- 头像，添加一个超连接，点击可以查看原图 -->
							<a :href="user.protraitUrl" target="_blank">
								<img class="span smallAvatar" :src="user.protraitThumUrl" />
							</a>
							<!-- 昵称 -->
							<span class="span ColorInfo"> {{user.nickName}}</span>
							<!-- 下拉菜单，用于进行修改名片、修改密码、退出登录等操作 -->
							<el-dropdown @command="handleCommand">
								<span class="el-dropdown-link">
									<i class="el-icon-arrow-down el-icon--right"></i>
								</span>
								<el-dropdown-menu slot="dropdown">
									<el-dropdown-item command="userCard">修改名片</el-dropdown-item>
									<el-dropdown-item command="userPassword">修改密码</el-dropdown-item>
									<el-dropdown-item command="logout">退出登录</el-dropdown-item>
								</el-dropdown-menu>
							</el-dropdown>
						</div>
					</el-col>
					
					
				</el-row>
			</el-header>
			<!-- 页面内容 -->
			<el-container>
				<!-- 左侧笔记本列表 -->
				<el-aside width="14%" class="notes-list">

					<NotesList @func="getNotes" ref="notesList"></NotesList>
				</el-aside>
				<!-- 中间笔记列表 -->
				<el-aside width="21%" class="note-list">
					
				</el-aside>
				<!-- 右侧笔记内容 -->
				<el-main class="note-info">
					
				</el-main>
			</el-container>
		</el-container>
		<!-- 遮罩层 -->
		<div v-if="mask == 'userCard' || mask == 'userPassword'" class="box"></div>
		<!-- 修改用户名片 -->
		<div v-if="mask == 'userCard'" class="boxCenter">
			<el-form label-position="left" label-width="0px">
				<el-form-item>
					<el-row>
						<el-col :span="16" :offset="4" class="alignCenter font24 font-bold">
							修改名片
						</el-col>
						<el-col :span="4" class="alignRight">
							<i class="el-icon-circle-close font24" @click="handleCommand()"></i>
						</el-col>
					</el-row>
					<!-- 昵称 -->
					<span class="tip">昵称 </span>
					<el-input type="text" autofocus="autofocus" v-model="user.nickName" placeholder="往后余生"></el-input>

					<!-- 邮箱 -->
					<span class="tip">邮箱 </span>
					<span class="ColorDanger" v-show="!checkEmail()"> ( 请输入正确的邮箱地址 )</span>
					<el-input type="text" v-model="user.email" placeholder="在名片上展示的邮箱地址"></el-input>

					<!-- 个性签名 -->
					<span class="tip">个性签名 </span>
					<el-input type="text" v-model="user.label" placeholder="Stay Hungry Stay Foolish"></el-input>

					<!--头像-->
					<span class="ColorCommon font-bold">选择头像</span>
					<el-upload class="avatar-uploader" :action=uploadUrl :show-file-list="false" :on-success="handleAvatarSuccess"
						:before-upload="beforeAvatarUpload">
						<img v-if="imageUrl" :src="imageUrl" class="avatar">
						<i v-else class="el-icon-plus avatar-uploader-icon"></i>
					</el-upload>
				</el-form-item>
				<el-form-item style="width:100%;">
					<!-- 修改名片 -->
					<el-button type="primary" style="width:100%;" @click="handleSetUserCard()" :disabled="!checkEmail()">保存修改</el-button>
				</el-form-item>
			</el-form>
		</div>
		<!-- 修改头像 -->
		<div v-if="mask == 'userPassword'" class="box" @click="handleCommand()">
			<div class="little-container">
				修改密码
			</div>
		</div>

	</div>
</template>

<script>
	//引入笔记本列表组件
	import NotesList from "../components/NotesList.vue";

	import {
		format
	} from "../data.js";
	import {
		req_getUserCard,
		req_setUserCard
	} from '../api';
	export default {
		components: {
			NotesList
		},
		data() {
			return {
				user: {},
				mask: null,
				uploadUrl: "upload",
				imageUrl: '',
				currentNotes: {}
			}
		},
		methods: {
			/**
			 * 笔记本列表组件 NotesList 中当前选中的笔记本有变化时，通知父组件刷新当前笔记本
			 * @param {Object} notes
			 */
			getNotes(notes) {
				this.currentNotes = notes;
				this.$notify.success({
					title: notes.title,
					message: notes.id
				});
			},
			/**
			 * 校验邮箱格式
			 */
			checkEmail() {
				return this.user.email.trim() == '' || format.isEmail(this.user.email);
			},
			/**
			 * Header 中下拉菜单选择不同的值后的回调方法
			 * @param {Object} command
			 */
			handleCommand(command) {
				this.mask = command;
				if (this.mask == "logout") {
					window.localStorage.removeItem("token");
					this.$router.push({
						path: '/Login'
					});
				}
			},
			/**
			 * 修改用户名片
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
						this.$message.success("名片设置成功");
					}
					this.handleCommand();
					this.getUserCard();
				});
			},
			/**
			 * 获取用户名片
			 */
			getUserCard() {
				req_getUserCard().then(response => {
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
						this.user = data;
					}
				});
			},
			/**
			 * 头像上传前的处理函数，在此限制图片格式和大小
			 * @param {Object} file
			 */
			beforeAvatarUpload(file) {
				const isJPG = file.type === 'image/jpeg' || file.type === 'image/png';
				const isLt2M = file.size / 1024 / 1024 < 2;

				if (!isJPG) {
					this.$message.error('上传头像图片只能是 JPG、PNG 格式!');
				}
				if (!isLt2M) {
					this.$message.error('上传头像图片大小不能超过 2MB!');
				}
				return isJPG && isLt2M;
			},
			/**
			 * 头像上传完成后的回调函数，在此指定img标签中的src
			 * @param {Object} res
			 * @param {Object} file
			 */
			handleAvatarSuccess(res, file) {
				this.imageUrl = URL.createObjectURL(file.raw);
			}
		},
		mounted() {
			//进入用户主页，首先获取用户名片
			this.getUserCard();
			this.uploadUrl = this.uploadUrl + "?token=" + window.localStorage.getItem("token");

			//加载用户笔记本列表
			this.$refs.notesList.load();
		}
	}
</script>

<style scoped>
	.header {
		height: 60px;
		padding: 10px;
		line-height: 40px;
		background-color: #dadbdf;
		
		
	}

	/* vertical-align 标签只能用在行内元素中,且该元素没有浮动 */
	.span {
		display: inline;
		vertical-align: middle
	}

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
	
	.notes-list{
		background-color: #f7f7f7;
		min-width: 230px;
		padding-top: 10px;
	}
	.note-list{
		background-color: #f8f8f8;
		min-width: 340px;
	}
	.note-info{
		background-color: #f9f9f9;
	}
</style>
