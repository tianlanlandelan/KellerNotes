const config = {
	nginxUrl: "http://127.0.0.1:8081/nginx",
	type: {
		register: 0,
		login: 1,
		resetPassword: 2
	}
}

const format = {
	emailFormat : new RegExp("^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$"),
	isEmail(email){
		console.log(email);
		return this.emailFormat.test(email);
	},
	codeFormat:new RegExp("^[a-zA-Z0-9]{6}$"),
	isCode(code){
		return this.codeFormat.test(code);
	},
	passwordFormat:new RegExp("^[0-9A-Za-z]{6,16}$"),
	isPassword(password){
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
	format
}
