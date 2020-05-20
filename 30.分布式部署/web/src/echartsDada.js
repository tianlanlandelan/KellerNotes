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
	// 一个基础的多用途数据模板,可以生成柱状图/折线图/饼图/散点图/漏斗图,格式参考 https://www.echartsjs.com/zh/option.html#legend
	baseData: {
		// 标题
		title:{
			// 标题文本
			text:"",
			// 标题链接
			link:"",
			// 标题链接打开方式 可以指定 self 或 blank
			target:"blank",
			//标题的自定义样式,支持 color fontStyle fontWeight fontSize lineHeight 等几乎所有文字的样式
			textStyle:{
				
			}
		},
		/*
		 * 图例组件 定义图例的颜色/名字/样式/布局方式等
		 * 主要属性有:
		  * type 样式  可选值 plain:普通图例    scroll:可滚动翻页的图例
		  * left/right/bottom/width/htight/align ... 
		  * orient  布局  可选值 horizontal: 横向  vertical:纵向
		 */
		legend: {},
		/**
		 * 提示框组件。
		 * trigger 触发类型
				'item'数据项图形触发，主要在散点图，饼图等无类目轴的图表中使用。
				'axis'坐标轴触发，主要在柱状图，折线图等会使用类目轴的图表中使用。
					在 ECharts 2.x 中只支持类目轴上使用 axis trigger，在 ECharts 3 中支持在直角坐标系和极坐标系上的所有类型的轴。并且可以通过 axisPointer.axis 指定坐标轴。
				'none'什么都不触发。
			type 指示器类型
				'line' 直线指示器
				'shadow' 阴影指示器
				'none' 无指示器
				'cross' 十字准星指示器。其实是种简写，表示启用两个正交的轴的 axisPointer。
		 */
		tooltip: {},
		/**
		 * 工具栏。内置有导出图片，数据视图，动态类型切换，数据区域缩放，重置五个工具。
		 * show
		 * orient : horizontal/vertical
		 * itemSize: 工具栏 icon 的大小
		 */
		toolbox:{
			feature: {
				//配置项还原
				//restore: {},
				//保存为图片
				// saveAsImage: {},
				//数据视图工具，可以展现当前图表所用的数据，编辑后可以动态更新
				//dataView:{}
			}
		},
		dataset: {
			dimensions: [],
			// Provide data.
			source: []
		},
		/**
		 * 直角坐标系 grid 中的 x 轴，一般情况下单个 grid 组件最多只能放上下两个 x 轴，多于两个 x 轴需要通过配置 offset 属性防止同个位置多个 x 轴的重叠。
		 * position X轴的位置 可选值 top/bottom
		 * offset X轴相对于默认位置的偏移量
		 * type 坐标轴类型  可选值 
				'value' 数值轴，适用于连续数据。
				'category' 类目轴，适用于离散的类目数据，为该类型时必须通过 data 设置类目数据。
				'time' 时间轴，适用于连续的时序数据，与数值轴相比时间轴带有时间的格式化，在刻度计算上也有所不同，例如会根据跨度的范围来决定使用月，星期，日还是小时范围的刻度。
				'log' 对数轴。适用于对数数据。
			name 坐标轴名称
			nameLocation 坐标轴名称显示位置 可选值:start/middle/center/end
			nameTextStyle 坐标轴名称样式			

		 */
		xAxis: {
			type: 'category'
		},
		/**
		 * y 轴,属性同 xAxis 
		 * 唯一不同的是 position 的值为 left 或 right
		 */
		yAxis: {},
		/**
		 * 系列列表。每个系列通过 type 决定自己的图表类型
		 * 一组数据有多列，要定义多个 type ，每个 type 可以是不一样的
		 */
		series: [{}]
	},
	initOptions: {
		renderer: 'canvas'
	}
}

export {
	echartsDemo
}
