<!-- 用户管理 -->
<template>
	<div>
		<!--工具条-->
		<el-row>
			<el-col :span="12" class="toolbar" style="padding-bottom: 0px;">
				<el-form :inline="true" :model="filters">
					<el-form-item>
						<el-input v-model="filters.name" placeholder="姓名"></el-input>
					</el-form-item>
					<el-form-item>
						<el-button type="primary" v-on:click="handleGetUserList">查询</el-button>
					</el-form-item>
				</el-form>
			</el-col>
			<el-col :span="12" class="toolbar">
				<el-pagination layout="prev, pager, next" @current-change="handleCurrentChange" :page-size="pagination.size" :total="pagination.total"  style="float:right;">
				</el-pagination>
			</el-col>
		</el-row>

		<!--列表-->
		<el-table :data="list" highlight-current-row stripe  style="width: 100%;">
			<el-table-column prop="id" label="ID" width="40">
			</el-table-column>
			<el-table-column prop="email" label="用户名" sortable>
			</el-table-column>
			<el-table-column prop="type"  label="用户类型" :formatter="formatType" sortable>
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
	import {
		req_getUserList,
		req_getUserCounter
	} from '../../api';
	
	import {format} from "../../data.js";
	export default {
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
					size: 5
				},
				format : format
			}
		},
		methods: {
			initPagination(){
				this.pagination.total = 0;
				this.pagination.page = 1;
				this.pagination.size = 5;
			},
			formatType(row,column){
				return row.type == 0 ? '普通用户' : row.type == 100 ? '管理员' : '未知';
			},
			/**
			 * 修改查询条件时，一定要先初始化页面参数，否则，如果上一次查询页数比较大，会造成本次查询无结果
			 */
			handleGetUserList(){
				this.initPagination();
				this.getUserCounter();
				this.getUserList();
			},
			handleCurrentChange(val) {
				this.pagination.page = val;
				this.getUserList();
			},
			getUserList() {
				req_getUserList(this.pagination.page, this.pagination.size,this.filters.name).then(response => {
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
			}
		},
		mounted() {
			this.getUserCounter();
			this.getUserList();
		}
	}
</script>

<style scoped>
</style>
