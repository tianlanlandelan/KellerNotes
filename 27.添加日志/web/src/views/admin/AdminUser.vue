<!-- 用户管理 -->
<template>
	<div>
		<!--工具条-->
		<el-row>
			<el-col :span="12" class="toolbar" style="padding-bottom: 0px;">
				<el-form :inline="true" :model="filters">
					<el-form-item>
						<el-input v-model="filters.name" placeholder="用户名"></el-input>
					</el-form-item>
					<el-form-item>
						<el-button type="primary" v-on:click="handleGetUserList">查询</el-button>
					</el-form-item>
				</el-form>
			</el-col>
			<el-col :span="12" class="toolbar">
				<el-pagination layout="prev, pager, next" @current-change="handleCurrentChange" :page-size="pagination.size" :total="pagination.total"
					style="float:right;">
				</el-pagination>
			</el-col>
		</el-row>
		<!-- 导出数据 -->
		<el-row class="margin10">
			<el-col :span="12">
				<!-- 导出本页数据时，数据已经存在，直接取 list 即可 -->
				<JsonExcel :data="list" :fields="json_fields" name="用户信息.xls">
					<el-button type="primary"> 导出本页数据</el-button>
				</JsonExcel>
			</el-col>
			<el-col :span="12">
				<!-- 导出全部数据时，需要从服务端获取数据，使用 fetch 回调方法指定获取数据的方法，并等待异步请求结束后返回获取到的数据 -->
				<JsonExcel :fetch="getUserListForDownLoad" :fields="json_fields" name="用户信息.xls" type="xls">
					<el-button type="primary"> 导出全部数据</el-button>
				</JsonExcel>
			</el-col>
		</el-row>
		<!--列表-->
		<el-table :data="list" highlight-current-row stripe style="width: 100%;">
			<el-table-column prop="id" label="ID">
			</el-table-column>
			<el-table-column prop="email" label="用户名" sortable>
			</el-table-column>
			<el-table-column prop="type" label="用户类型" :formatter="formatType" sortable>
			</el-table-column>
			<el-table-column prop="createTime" label="注册时间" sortable>
				<template slot-scope="scope">
					<i class="el-icon-time"></i>
					<span style="margin-left: 10px">{{ format.formatDate(scope.row.createTime) }}</span>
				</template>
			</el-table-column>
		</el-table>



	</div>
</template>

<script>
	import JsonExcel from 'vue-json-excel';
	import {
		req_getUserList,
		req_getUserCounter
	} from '../../api';

	import {
		format
	} from "../../data.js";
	export default {
		components: {
			JsonExcel
		},
		data() {
			return {
				//用户列表
				list: [],
				//过滤器
				filters: {
					name: ''
				},
				//分页参数
				pagination: {
					total: 0,
					page: 1,
					size: 10
				},
				format: format,
				// 定义要导出的字段与数据格式，通过回调函数的方式对数据格式进行处理
				json_fields: {
					"用户ID": "id",
					"注册邮箱": "email",
					"用户类型": {
						field: "type",
						callback: (value) => {
							return value === 0 ? "普通用户" : value === 100 ? "管理员" : "未知";
						}
					},
					"注册时间": {
						field: "createTime",
						callback: (value) => {
							return this.format.formatDate(value);
						}
					}
				}
			}
		},
		methods: {
			// 初始化页面参数，当改变查询条件时，需要初始化页面参数
			initPagination() {
				this.pagination.total = 0;
				this.pagination.page = 1;
				this.pagination.size = 10;
			},
			// 在 Table 中指定字段格式化的方法
			formatType(row) {
				return row.type == 0 ? '普通用户' : row.type == 100 ? '管理员' : '未知';
			},
			/**
			 * 修改查询条件时，一定要先初始化页面参数，否则，如果上一次查询页数比较大，会造成本次查询无结果
			 */
			handleGetUserList() {
				this.initPagination();
				this.getUserCounter();
				this.getUserList();
			},
			// 当在页面上选择不同页码时，获取该页的数据
			handleCurrentChange(val) {
				this.pagination.page = val;
				this.getUserList();
			},
			// 获取用户列表
			getUserList() {
				req_getUserList(this.pagination.page, this.pagination.size, this.filters.name).then(response => {
					let {
						success,
						data,
						message
					} = response;
					if (success !== 0) {
						this.$message.error(message);
					} else {
						this.list = data;
					}
				});
			},
			// 获取用户列表，获取到数据后返回给JsonExcel组件，用于导出成 excel,在这里将最大行数限制为 1000
			async getUserListForDownLoad() {
				let userList = await req_getUserList(1, 1000, this.filters.name).then(response => {
					let {
						success,
						data,
						message
					} = response;
					if (success !== 0) {
						this.$message.error(message);
						return [];
					} else {
						return data;
					}
				});
				return userList;
			},
			// 获取符合查询条件的用户总数，每一次更改查询条件时会调用
			getUserCounter() {
				req_getUserCounter(this.filters.name).then(response => {
					let {
						success,
						data,
						message
					} = response;
					if (success !== 0) {
						this.$message.error(message);
					} else {
						this.pagination.total = data;
					}
				});
			},
		},
		mounted() {
			this.initPagination();
			this.getUserCounter();
			this.getUserList();
		}
	}
</script>

<style scoped>
</style>
