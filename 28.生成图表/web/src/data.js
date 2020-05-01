const config = {
	nginxUrl: "http://127.0.0.1:8081/nginx",
	type: {
		register: 0,
		login: 1,
		resetPassword: 2
	}
}

/**
 * 对比笔记内容，判断 MarkDown 编辑器中是否删除了图片
 * @param {Object} oldContent
 * @param {Object} newContent
 */
const editorImgCheck = {
	/**
	 * 获取到MarkDown编辑器中删除的图片
	 * @param {Object} oldContent	修改前的内容
	 * @param {Object} newContent	修改后的内容
	 */
	getMarkDownDelImgs(oldContent, newContent) {
		let oldImgs = this.getMarkDownImg(oldContent);
		//如果修改前没有图片,不用判断
		if (oldImgs.length < 1) {
			return [];
		}
		let newImgs = this.getMarkDownImg(newContent);
		//如果修改后没有图片,说明图片全被删除
		if (newImgs.length < 1) {
			return oldImgs;
		}
		let del;
		let delImgs = [];
		for (let i in oldImgs) {
			del = false;
			for (let j in newImgs) {
				//如果修改前的图片,修改后仍存在,说明未删除
				if (oldImgs[i] === newImgs[j]) {
					del = false;
					break;
				} else {
					del = true;
				}
			}
			if (del) {
				delImgs.push(oldImgs[i]);
			}
		}
		return delImgs;
	},
	/**
	 * 从 MarkDown 格式的内容中读取出图片名称(不带路径)
	 * @param {Object} content
	 */
	getMarkDownImg(content) {
		/* 匹配 MarkDown 中的图片 匹配格式：![图片描述](图片地址) */
		// eslint-disable-next-line
		var pattern = /!\[?([^\)]*)\)?/g;
		var arr = content.match(pattern);
		let imgs = []
		for (let index in arr) {
			let img = arr[index];
			img = img.substring(img.lastIndexOf("/") + 1, img.length - 1);
			imgs.push(img);
		}
		return imgs;
	}
}


const format = {
	emailFormat: new RegExp(
		"^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$"
	),
	isEmail(email) {
		return this.emailFormat.test(email);
	},
	codeFormat: new RegExp("^[a-zA-Z0-9]{6}$"),
	isCode(code) {
		return this.codeFormat.test(code);
	},
	passwordFormat: new RegExp("^[0-9A-Za-z]{6,16}$"),
	isPassword(password) {
		return this.passwordFormat.test(password);
	},
	pattern: "yyyy-MM-dd",
	SIGN_REGEXP: /([yMdhsm])(\1*)/g,
	padding(s, len) {
		var ilen = len - (s + '').length;
		for (var i = 0; i < ilen; i++) {
			s = '0' + s;
		}
		return s;
	},
	formatDate(date) {
		date = new Date(date);
		let that = this;
		return this.pattern.replace(this.SIGN_REGEXP, function($0) {
			switch ($0.charAt(0)) {
				case 'y':
					return that.padding(date.getFullYear(), $0.length);
				case 'M':
					return that.padding(date.getMonth() + 1, $0.length);
				case 'd':
					return that.padding(date.getDate(), $0.length);
				case 'w':
					return date.getDay() + 1;
				case 'h':
					return that.padding(date.getHours(), $0.length);
				case 'm':
					return that.padding(date.getMinutes(), $0.length);
				case 's':
					return that.padding(date.getSeconds(), $0.length);
			}
		});
	},
	checkPortrait(file) {
		const isImg = file.type === 'image/jpeg' || file.type === 'image/png';
		const isLt2M = file.size / 1024 / 1024 < 2;


		return isImg && isLt2M;
	}
}

const utils = {
	/**
	 * 获取url 中的带的查询参数
	 * @param {Object} name
	 */
	getParameter(name) {
		var query = window.location.search;
		var iLen = name.length;
		var iStart = query.indexOf(name);
		if (iStart == -1)
			return "";
		var iEnd = query.indexOf("&", iStart);
		if (iEnd != -1) {
			return query.substring(iStart + iLen + 1, iEnd);
		} else {
			return query.substring(iStart + iLen + 1);
		}
	}
}
export {
	utils,
	config,
	format,
	editorImgCheck
	}
