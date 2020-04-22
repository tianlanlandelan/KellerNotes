module.exports = {
	// 部署应用时的基本 URL
	publicPath: process.env.NODE_ENV === 'production' ? './' : '/',
	outputDir: 'dist', //build输出目录
	assetsDir: 'assets', //静态资源目录（js, css, img）
	// 默认在生成的静态资源文件名中包含hash以控制缓存
	filenameHashing: false,
	// 是否在开发环境下通过 eslint-loader 在每次保存时 lint 代码 (在生产构建时禁用 eslint-loader)
	lintOnSave: process.env.NODE_ENV !== 'production',
	// 开发环境配置
	devServer: {
		open: true, //是否自动弹出浏览器页面
		host: "127.0.0.1",
		port: 8088,
		https: false, //是否使用https协议
		hotOnly: false, //是否开启热更新
		proxy: {
			'/api': {
				target: 'https://127.0.0.1',
				changeOrigin: true,
				secure: false
			},
			'/base': {
				target: 'https://127.0.0.1',
				changeOrigin: true,
				secure: false
			},
			'/form': {
				target: 'https://127.0.0.1',
				changeOrigin: true,
				secure: false
			},
			'/upload': {
				target: 'https://127.0.0.1',
				changeOrigin: true,
				secure: false
			}
		},
	}
}
