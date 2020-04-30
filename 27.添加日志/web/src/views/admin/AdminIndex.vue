<!-- 后台管理首页，概览 -->
<template>
    <div>
		<el-row :gutter="20">
			<!-- 用户数量 -->
			<el-col :span="8" v-for="(data,index) in list" :key="index">
				<div class="margin10">
					<span class="font24 ColorCommon font-bold">{{data.title}}</span>
				</div>
				<!-- 总用户数 -->
				<div class="margin10" v-for="(item,i) in data.data" :key="i">
					<el-card shadow="hover" class="cardBg">
						<el-row>
							<el-col :span="8" class="font60 ColorCommon">
								<i :class="item.icon"></i>
							</el-col>
							<el-col :span="16">
								<div class="marginBottom10">
									<span class="ColorInfo">{{item.title}}</span>
								</div>
								<div>
									<span class="font24 ColorCommon font-bold">{{item.content}}</span>
								</div>
							</el-col>
						</el-row>
					</el-card>
				</div>
			</el-col>
		</el-row>
		
    </div>
</template>

<script>
	import {
		req_getUserCounter,
		req_getLoginLogByDay,
		req_getLoginLogByMonth,
		req_getRegisterLogByDay} from "../../api.js";
	
	import{format} from "../../data.js";
	
    export default{
		data(){
			return{
				today : format.formatDate(new Date()),
				yesterday : format.formatDate(new Date().getTime() - 24 * 60 * 60 * 1000),
				preMonth : format.formatDate(new Date().getTime() - 30 * 24 * 60 * 60 * 1000),
				list:[
					{
						title:"用户数",
						data:[
							{
								icon:"el-icon-user",
								title:"总用户数",
								content:0
							},
							{
								icon:"el-icon-mobile-phone",
								title:"今日新增",
								content:0
							},
							{
								icon:"el-icon-data-line",
								title:"较昨日",
								content:"-"
							}
						]
					},
					{
						title:"活跃用户",
						data:[
							{
								icon:"el-icon-hot-water",
								title:"今日活跃",
								content:0
							},
							{
								icon:"el-icon-coffee-cup",
								title:"昨日活跃",
								content:0
							},
							{
								icon:"el-icon-date",
								title:"近30天活跃",
								content:0
							}
						]
					},
					{
						title:"异常日志",
						data:[
							{
								icon:"el-icon-document-copy",
								title:"总记录",
								content:0
							},
							{
								icon:"el-icon-document-add",
								title:"今日新增",
								content:0
							},
							{
								icon:"el-icon-data-analysis",
								title:"较昨日",
								content:"-"
							}
						]
					}
				]
			}
		},
		methods:{
			//查询用户总数
			getUserCounter(){
				req_getUserCounter().then(response =>{
					if (response.success == 0){
						this.list[0].data[0].content = response.data;
					}
				});
			},
			// 查询昨日和今日的新增用户数量
			getRegisterByDay(){
				req_getRegisterLogByDay(this.yesterday,this.today).then(response =>{
					if (response.success == 0){
						let data = response.data;
						if(data.length > 1){
							let yesterDayCount = data[0].count;
							let todayCount = data[1].count;
							let change = todayCount - yesterDayCount;
							
							this.list[0].data[1].content = todayCount;
							this.list[0].data[2].content = change < 0 ? "- " + change : "+ " + change;
						}else{
							this.list[0].data[1].content = data[0].count;
						}
					}
				});
				
			},
			
			// 查询昨日和今日的活跃用户数
			getLoginByDay(){
				req_getLoginLogByDay(this.yesterday,this.today).then(response =>{
					if (response.success == 0){
						let data = response.data;
						if(data.length > 1){
							this.list[1].data[0].content = data[1].count;
							this.list[1].data[1].content = data[0].count;
						}else{
							this.list[1].data[0].content = data[0].count;
						}
					}
				});
			},
			// 查询最近30天的活跃用户总数
			getLoginByMonth(){
				req_getLoginLogByMonth(this.yesterday,this.today).then(response =>{
					if (response.success == 0){
						let count = 0;
						for(let i in response.data){
							count += response.data[i].count;
						}
						this.list[1].data[2].content = count;
					}
				});
			}
		},mounted(){
			this.getUserCounter();
			this.getRegisterByDay();
			this.getLoginByDay();
			this.getLoginByMonth();
		}
	}
</script>

<style scoped>
	.cardBg{
		background-color: #fafafa;
	}
</style>