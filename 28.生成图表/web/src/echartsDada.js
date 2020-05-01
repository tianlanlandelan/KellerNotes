/**
 * 定义一个Echarts 模板数据,方便生成不同的 Echarts 图表
 * 当前支持的简单图表有:
 * 	柱状图  折线图 饼图  雷达图  散点图 漏斗图 仪表盘
 */
const echartsDemo = {
	//获取一个基础的柱状图数据
	getBar() {
		// 使用深拷贝获得一个新的 baseData 对象
		let bar = JSON.parse(JSON.stringify(this.baseData));
		bar.series = [{
			type: "bar"
		}];
		return bar;
	},
	//获取一个基础的折线图数据
	getLine() {
		let line = JSON.parse(JSON.stringify(this.baseData));
		line.series = [{
			type: "line"
		}];
		return line;
	},
	//获取一个基础的饼图数据
	getPie() {
		let pie = JSON.parse(JSON.stringify(this.baseData));
		pie.series = [{
			type: "pie"
		}];
		pie.legend = {
			orient: 'vertical',
			left: 'right',
			top: 'middle'
		};
		return pie;
	},
	// 获取一个基础的散点图数据
	getScatter() {
		let line = JSON.parse(JSON.stringify(this.baseData));
		line.series = [{
			type: "scatter"
		}];
		return line;
	},
	// 漏斗图
	getFunnel() {
		let line = JSON.parse(JSON.stringify(this.baseData));
		line.series = [{
			type: "funnel"
		}];
		return line;
	},
	// 获取一个仪表盘	
	getGuage() {
		return JSON.parse(JSON.stringify(this.gauge));
	},
	// 获取一个雷达图
	getRadar(){
		return JSON.parse(JSON.stringify(this.radar));	
	},
	getTest() {
		return this.radar;
	},
	// 基础仪表盘数据模板
	gauge: {
		tooltip: {
			formatter: '{a} <br/>{b} : {c}%'
		},
		toolbox: {
			feature: {
				restore: {},
				saveAsImage: {}
			}
		},
		series: [{
			name: '业务指标',
			type: 'gauge',
			detail: {
				formatter: '{value}%'
			},
			data: [{
				value: 75,
				name: '完成率'
			}]
		}]
	},
	// 雷达图模板
	radar: {
		tooltip: {},
		legend: {},
		radar: {
			// shape: 'circle',
			// 雷达图维度名称的样式
			name: {
				textStyle: {
					color: '#fff',
					backgroundColor: '#999',
					borderRadius: 3,
					padding: [3, 5]
				}
			},
			// 雷达图的各个维度
			indicator: [{
					name: '销售（sales）',
					max: 6500
				},
				{
					name: '管理（Administration）',
					max: 16000
				},
				{
					name: '信息技术（Information Techology）',
					max: 30000
				},
				{
					name: '客服（Customer Support）',
					max: 38000
				},
				{
					name: '研发（Development）',
					max: 52000
				},
				{
					name: '市场（Marketing）',
					max: 25000
				}
			]
		},
		series: [{
			name: '',
			type: 'radar',
			// areaStyle: {normal: {}},
			data: [{
					value: [4300, 10000, 28000, 35000, 50000, 19000],
					name: '预算分配（Allocated Budget）'
				},
				{
					value: [5000, 14000, 28000, 31000, 42000, 21000],
					name: '实际开销（Actual Spending）'
				}
			]
		}]
	},
	// 一个基础的多用途数据模板,可以生成柱状图/折线图/饼图/散点图/漏斗图
	baseData: {
		legend: {},
		tooltip: {},
		dataset: {
			dimensions: [],
			// Provide data.
			source: []
		},
		// Declare X axis, which is a category axis, mapping
		// to the first column by default.
		xAxis: {
			type: 'category'
		},
		// Declare Y axis, which is a value axis.
		yAxis: {},
		// 一组数据有多列，要定义多个 type ，每个 type 可以是不一样的
		series: [{}]
	},
	initOptions: {
		renderer: 'canvas'
	}
}

export {
	echartsDemo
}
