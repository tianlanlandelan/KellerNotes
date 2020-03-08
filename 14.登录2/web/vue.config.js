module.exports = {
	outputDir: 'dist', //build输出目录
	assetsDir: 'assets', //静态资源目录（js, css, img）
	lintOnSave: false, //是否开启eslint
	devServer: {
		open: true, //是否自动弹出浏览器页面
		host: "127.0.0.1",
		port: 8088,
		https: false, //是否使用https协议
		hotOnly: false, //是否开启热更新
		proxy: {
			'/api': {
				target: 'http://127.0.0.1:8080',
				changeOrigin: true,
				secure: false
			},
			'/base': {
				target: 'http://127.0.0.1:8080',
				changeOrigin: true,
				secure: false
			},
			'/form': {
				target: 'http://127.0.0.1:8080',
				changeOrigin: true,
				secure: false
			},
			'/upload': {
				target: 'http://127.0.0.1:8080',
				changeOrigin: true,
				secure: false
			}
		},
	}
}
