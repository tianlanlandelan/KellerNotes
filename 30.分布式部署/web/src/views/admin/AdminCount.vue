<!-- 数据统计 -->
<template>
	<div>
		<div>
			<div class="alignCenter font24 ColorInfo font-bold"><span>近30天活跃用户数曲线图</span></div>
			<figure>
				<ECharts :options="line" :init-options="initOptions" ref="line" theme="westeros" autoresize @zr:click="handleZrClick"
					@click="handleClick" />
			</figure>
		</div>
		<div>
			<div class="alignCenter font24 ColorInfo font-bold"><span>近30天活跃用户数柱状图</span></div>
			<figure>
				<ECharts :options="bar" :init-options="initOptions" ref="bar" theme="westeros" autoresize @zr:click="handleZrClick"
					@click="handleClick" />
			</figure>
		</div>
		<div>
			<div class="alignCenter font24 ColorInfo font-bold"><span>近7天活跃用户数饼图</span></div>
			<figure>
				<ECharts :options="pie" :init-options="initOptions" ref="pie" theme="westeros" autoresize @zr:click="handleZrClick"
					@click="handleClick" />
			</figure>
		</div>
		<div>
			<div class="alignCenter font24 ColorInfo font-bold"><span>近30天活跃用户数散点图</span></div>
			<figure>
				<ECharts :options="scatter" :init-options="initOptions" ref="scatter" theme="westeros" autoresize @zr:click="handleZrClick"
					@click="handleClick" />
			</figure>
		</div>
		
		<div>
			<div class="alignCenter font24 ColorInfo font-bold"><span>近7天活跃用户数漏斗图</span></div>
			<figure>
				<ECharts :options="funnel" :init-options="initOptions" ref="funnel" theme="westeros" autoresize @zr:click="handleZrClick"
					@click="handleClick" />
			</figure>
		</div>
		
		<div>
			<div class="alignCenter font24 ColorInfo font-bold"><span>仪表盘示意图</span></div>
			<figure>
				<ECharts :options="gauge" :init-options="initOptions" ref="gauge" theme="westeros" autoresize @zr:click="handleZrClick"
					@click="handleClick" />
			</figure>
		</div>
		
		<div>
			<div class="alignCenter font24 ColorInfo font-bold"><span>雷达图示意图</span></div>
			<figure>
				<ECharts :options="radar" :init-options="initOptions" ref="radar" theme="westeros" autoresize @zr:click="handleZrClick"
					@click="handleClick" />
			</figure>
		</div>
	</div>
</template>

<script>
	// 引入格式化方法
	import {
		format
	} from "../../data.js";

	// 引入Echarts模板
	import {
		echartsDemo
	} from "../../echartsDada.js"

	import {
		
		req_getLoginLogByDay
	
	} from "../../api.js";

	import ECharts from 'vue-echarts/components/ECharts'
	import 'echarts/lib/chart/bar'
	// 引入动效
	import 'echarts/map/js/world'
	// custom theme
	import theme from './theme.json'
	// registering custom theme
	ECharts.registerTheme('westeros', theme)

	export default {
		components: {
			ECharts
		},
		data() {
			return {
				today: format.formatDate(new Date()),
				yesterday: format.formatDate(new Date().getTime() - 24 * 60 * 60 * 1000),
				preMonth: format.formatDate(new Date().getTime() - 30 * 24 * 60 * 60 * 1000),
				preWeek : format.formatDate(new Date().getTime() - 7 * 24 * 60 * 60 * 1000),

				//柱状图
				bar: echartsDemo.getBar(),
				//曲线图、折线图
				line : echartsDemo.getLine(),
				//饼图
				pie: echartsDemo.getPie(),
				// 散点图
				scatter:echartsDemo.getScatter(),
				// 漏斗图
				funnel:echartsDemo.getFunnel(),
				// 仪表盘
				gauge:echartsDemo.getGuage(),
				// 雷达图
				radar:echartsDemo.getTest(),
				
				initOptions:echartsDemo.initOptions
			}
		},
		methods: {
			handleClick() {
				window.console.log('click from echarts')
			},
			handleZrClick() {
				window.console.log('click from zrender')
			},
			/**
			 * 获取最近一个月（30） 天的登录数据
			 */
			getLoginMonth() {
				this.bar.title.text = "近30天活跃用户柱状图"
				this.bar.dataset.dimensions = ["日期","活跃用户数"];
				this.line.dataset.dimensions = ["日期","新增用户数"];
				this.scatter.dataset.dimensions = ["日期","新增用户数"];
				
				req_getLoginLogByDay(this.preMonth, this.today).then(response => {
					if (response.success === 0) {
						let data = response.data;
						for (let i in data) {
							this.bar.dataset.source.push([data[i].time, data[i].count]);
							this.line.dataset.source.push([data[i].time, data[i].count]);
							this.scatter.dataset.source.push([data[i].time, data[i].count]);
						
						}
					}
				});
			},
			/**
			 * 获取最近一周（7天）的登录数据
			 */
			getLoginWeek() {
				this.pie.dataset.dimensions = ["日期","人数"];
				this.funnel.dataset.dimensions = ["日期","人数"];
				req_getLoginLogByDay(this.preWeek, this.today).then(response => {
					if (response.success === 0) {
						let data = response.data;
					
						for (let i in data) {
							this.pie.dataset.source.push([data[i].time, data[i].count]);
							this.funnel.dataset.source.push([data[i].time, data[i].count]);
							
						}
					}
				});
			}
		},
		mounted() {
			this.getLoginMonth();
			this.getLoginWeek();
		}
	}
</script>

<style scoped>
	.echarts {
		width: 900px;
		height: 600px;
		margin: 20px auto;
	}
</style>
