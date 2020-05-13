<!-- 后台管理模板页 -->
<template>
	<div>
		<el-row class="container">
			<el-col :span="24" class="header">
				<el-col :span="4" class="logo">
					Keller Notes
				</el-col>
				<el-col :span="10" class="alignCenter">
					{{this.sysSignStr}}
				</el-col>
				<el-col :span="4" class="userinfo">
					<el-dropdown trigger="hover">
						
						<span class="el-dropdown-link userinfo-inner"><img :src="this.sysUserAvatar" /> {{sysUserName}}</span>
						<el-dropdown-menu slot="dropdown">
							<el-dropdown-item @click.native="mymessage">我的消息</el-dropdown-item>
							<el-dropdown-item @click.native="mysetting">设置</el-dropdown-item>
							<el-dropdown-item divided @click.native="mylogout">退出登录</el-dropdown-item>
						</el-dropdown-menu>
					</el-dropdown>
				</el-col>
			</el-col>
		</el-row>
		
		<el-row class="container">
			<el-col :span="4">
					<!--导航菜单-->
					<el-menu :default-active="$route.path" class="el-menu-vertical-demo" @open="handleopen($route.path)" @close="handleclose($route)"
						@select="handleselect" unique-opened router >
						<template v-for="(item,index) in $router.options.routes">
							<div :key="item.path" v-if="item.show">
								<el-submenu :index="index+''" v-if="!item.leaf" :key="index">
									<template slot="title">
										<i :class="item.iconCls"></i>
										{{item.name}}
									</template>
									<el-menu-item v-for="child in item.children" :index="child.path" :key="child.path">{{child.name}}</el-menu-item>
								</el-submenu>
								<el-menu-item v-if="item.leaf&&item.children.length>0" :index="item.children[0].path" :key="index">
									<i :class="item.iconCls"></i>
									{{item.children[0].name}}
								</el-menu-item>
							</div>
						</template>
					</el-menu>
				
				</el-col>
				<el-col :span="20" class="main">
				<section class="content-container">
					<div class="grid-content bg-purple-light">
						<el-col :span="24" class="breadcrumb-container">
							<strong class="title">{{$route.name}}</strong>
							<el-breadcrumb separator="/" class="breadcrumb-inner">
								<el-breadcrumb-item v-for="item in $route.matched" :key="item.path">
									{{ item.name }}
								</el-breadcrumb-item>
							</el-breadcrumb>
						</el-col>
						<el-col :span="24" class="content-wrapper">
							<transition name="fade" mode="out-in">
								<router-view></router-view>
							</transition>
						</el-col>
					</div>
				</section>
			</el-col>
		</el-row>
	</div>
	
		
		
		
</template>

<script>
	
	import defaultAvatar from '../../assets/default.png';
	
	export default {
		data() {
			return {
				sysName: 'Keller Notes',
				sysUserName: '',
				sysUserAvatar:defaultAvatar,
				sysSignStr:"欢迎登录 KellerNotes 后台管理系统"
			}
		},
		methods: {
			
			handleopen(path) {
				window.console.log('handleopen', path);
			},
			handleclose(route) {
				window.console.log('handleclose', route);
			},
			handleselect(a, b) {
				window.console.log('handleselect', a,b);
			},
			//个人消息
			mymessage() {
				window.console.log("个人消息");
			},
			//设置
			mysetting() {
				window.console.log("设置");
			},
			//退出登录
			mylogout() {
				var _this = this;
				this.$confirm('确认退出吗?', '提示', {
					//type: 'warning'
				}).then(() => {
					sessionStorage.removeItem('user');
					_this.$router.push('/login');
				}).catch(() => {

				});
			}
		},
		mounted() {
			
		}
	}
</script>
<style scoped lang="scss">
	.container {
		width: 100%;
		.header {
			height: 60px;
			line-height: 60px;
			background: #409EFF;
			color: #fff;

			.userinfo {
				text-align: right;
				padding-right: 35px;
				float: right;

				.userinfo-inner {
					cursor: pointer;
					color: #fff;

					img {
						width: 40px;
						height: 40px;
						border-radius: 20px;
						margin: 10px 0px 10px 10px;
						float: right;
					}
				}
			}

			.logo {
				//width:230px;
				height: 60px;
				font-size: 22px;
				padding-left: 20px;
				padding-right: 20px;
				border-color: rgba(238, 241, 146, 0.3);
				border-right-width: 1px;
				border-right-style: solid;

				img {
					width: 40px;
					float: left;
					margin: 10px 10px 10px 18px;
				}

				.txt {
					color: #fff;
				}
			}
		}

		.main {

			overflow: hidden;

			aside {
				
				width: 230px;
				.el-menu {
					height: 100%;
				}
			}
			.content-container {
				overflow-y: auto;
				padding: 20px;
				.breadcrumb-container {
					.title {
						width: 200px;
						float: left;
						color: #475669;
					}
					.breadcrumb-inner {
						float: right;
					}
				}
				.content-wrapper {
					background-color: #fff;
					box-sizing: border-box;
					margin-top: 20px;
				}
			}
		}
	}
</style>
