import axios from 'axios';
let url = "http://127.0.0.1:8088/";
// let url = "https://47.93.84.8/";
/** 需要先登录再访问的路由 */
let api = url + 'api';


/** 不需要登录就能访问的路由 */
let base = url + 'base';

let upload = url + 'upload';

let editorImgUploadUrl = url + "upload/img";

export{
	editorImgUploadUrl,
	upload
}

/**
 * 获取注册验证码 1001
 */
export const req_getCodeForRegister = (email) => { 
    return axios.get(base + '/getCodeForRegister', {
		params:{
			email:email
		},
		headers:{
			'service':'User',
			'method':'notes'
		}
	}).then(res => res.data).catch(err => err);  
};


/**
 * 注册 1002
 */
export const req_register = (user) => { 
    return axios.post(base + '/register', {
		email:user.email,
		password:user.password,
		code:user.code
	}).then(res => res.data).catch(err => err); 
};


/**
 * 登录接口 1003
 */
export const req_logon = (user) => { 
    return axios.post(base, {
			email:user.email,
			password:user.password,
			type:user.type
		},{
			headers:{
				'method':'login',
				'service':'User'
			}
		},
		).then(res => res.data).catch(err => err); 
};

/**
 * 发送重置密码邮件 1006
 */
export const req_sendResetPasswordEmail = (email) => { 
    return axios.post(base + '/sendResetPasswordEmail', 
		{
			email:email
		},{
			headers:{
				'service':'User'
			}
		}
		).then(res => res.data).catch(err => err); 
};

/**
 * 通过邮件重置密码 1007
 */
export const req_resetPasswordByEmail = (password,token) => { 
    return axios.post(base + '/resetPasswordByEmail', {
        password:password,
		token:token
    }).then(res => res.data).catch(err => err); 
};

/**
 * 设置个人名片 2001
 */
export const req_setUserCard = (user) => { 
    return axios.post(api, {
		nickName:user.nickName,
		email:user.email,
		label:user.label
    },{
		headers:{
			'method':'userCard',
			'token'	: window.localStorage.getItem("token")
		}
	}).then(res => res.data).catch(err => err); 
};
/**
 * 获取用户名片 2002
 */
export const req_getUserCard = () => { 
    return axios.get(api, {
		headers:{
			'method':'userCard',
			'token'	: window.localStorage.getItem("token")
		}
	}).then(res => res.data).catch(err => err);  
};

/**
 * 添加笔记本 3001
 */
export const req_addNotes = (notes) => { 
    return axios.post(api, {
		title:notes.title,
		subTitle:notes.subTitle,
		sort:notes.sort
    },{
		headers:{
			'method':'notes',
			'token'	: window.localStorage.getItem("token")
		}
	}).then(res => res.data).catch(err => err); 
};

/**
 * 修改笔记本 3002
 */
export const req_saveNotes = (notes) => { 
    return axios.post(api, {
		id:notes.id,
		title:notes.title,
		subTitle:notes.subTitle,
		sort:notes.sort
    },{
		headers:{
			'method':'notes/update',
			'token'	: window.localStorage.getItem("token")
		}
	}).then(res => res.data).catch(err => err); 
};

/**
 * 修改笔记本 3003
 */
export const req_deleteNotes = (id) => { 
    return axios.post(api, {
		id:id
    },{
		headers:{
			'method':'notes/delete',
			'token'	: window.localStorage.getItem("token")
		}
	}).then(res => res.data).catch(err => err); 
};

/**
 * 获取笔记本列表 3004
 */
export const req_getNotesList = () => { 
    return axios.get(api, {
		headers:{
			'method':'notes',
			'token'	: window.localStorage.getItem("token")
		}
	}).then(res => res.data).catch(err => err);  
};


/**
 * 添加笔记 4001
 */
export const req_addNote = (note) => { 
    return axios.post(api, {
		notesId:note.notesId,
		title:note.title,
		type:note.type
    },{
		headers:{
			'method':'note',
			'token'	: window.localStorage.getItem("token")
		}
	}).then(res => res.data).catch(err => err); 
};

/**
 * 获取笔记列表 4003
 */
export const req_getNoteList = (notesId) => { 
    return axios.get(api, {
		params:{
			notesId:notesId
		},
		headers:{
			'method':'note/list',
			'token'	: window.localStorage.getItem("token")
		}
	}).then(res => res.data).catch(err => err);  
};
/**
 * 笔记重排序 4005
 */
export const req_noteReSort = (noteId,sort) => { 
    return axios.post(api, {
		noteId:noteId,
		sort:sort
    },{
		headers:{
			'method':'note/reSort',
			'token'	: window.localStorage.getItem("token")
		}
	}).then(res => res.data).catch(err => err); 
};

/**
 * 获取笔记内容 4006
 */
export const req_getNoteInfoById = (noteId) => { 
    return axios.get(api, {
		params:{
			noteId:noteId
		},
		headers:{
			'method':'note',
			'token'	: window.localStorage.getItem("token")
		}
	}).then(res => res.data).catch(err => err);  
};

/**
 * 设置笔记内容 4007
 */
export const req_setNoteContent = (noteId,text,html) => { 
    return axios.post(api, {
		noteId:noteId,
		text:text,
		html:html
    },{
		headers:{
			'method':'note/save',
			'token'	: window.localStorage.getItem("token")
		}
	}).then(res => res.data).catch(err => err); 
};

/**
 * 删除笔记 4008
 */
export const req_delNote = (noteId) => { 
    return axios.post(api, {
		noteId:noteId,
    },{
		headers:{
			'method':'note/del',
			'token'	: window.localStorage.getItem("token")
		}
	}).then(res => res.data).catch(err => err); 
};

/**
 * 删除笔记 4009
 */
export const req_delNoteImg = (imgName) => { 
    return axios.post(api, {
		imgName:imgName,
    },{
		headers:{
			'method':'note/delImg',
			'token'	: window.localStorage.getItem("token")
		}
	}).then(res => res.data).catch(err => err); 
};


/**
 * 获取用户列表 5002
 */
export const req_getUserList = (page,size,email) => { 
    return axios.get(api, {
		params:{
			page:page,
			size:size,
			email:email
		},
		headers:{
			'method':'admin/userList',
			'token'	: window.sessionStorage.getItem("adminToken")
		}
	}).then(res => res.data).catch(err => err);  
};

/**
 * 获取用户总数 5007
 */
export const req_getUserCounter = (email) => { 
    return axios.get(api, {
		params:{
			email:email
		},
		headers:{
			'method':'admin/userCounter',
			'token'	: window.sessionStorage.getItem("adminToken")
		}
	}).then(res => res.data).catch(err => err);  
};
/**
 * 获取日活用户数量 5008
 */
export const req_getLoginLogByDay = (startDate,endDate) => { 
    return axios.get(api, {
		params:{
			startDate:startDate,
			endDate:endDate
		},
		headers:{
			'method':'admin/loginLogByDay',
			'token'	: window.sessionStorage.getItem("adminToken")
		}
	}).then(res => res.data).catch(err => err);  
};
/**
 * 获取日活用户数量 5009
 */
export const req_getLoginLogByMonth = (startDate,endDate) => { 
    return axios.get(api, {
		params:{
			startDate:startDate,
			endDate:endDate
		},
		headers:{
			'method':'admin/loginLogByMonth',
			'token'	: window.sessionStorage.getItem("adminToken")
		}
	}).then(res => res.data).catch(err => err);  
};

/**
 * 获取用户日增长量 5008
 */
export const req_getRegisterLogByDay = (startDate,endDate) => { 
    return axios.get(api, {
		params:{
			startDate:startDate,
			endDate:endDate
		},
		headers:{
			'method':'admin/registerLogByDay',
			'token'	: window.sessionStorage.getItem("adminToken")
		}
	}).then(res => res.data).catch(err => err);  
};
/**
 * 获取用户月增长量 5009
 */
export const req_getRegisterLogByMonth = (startDate,endDate) => { 
    return axios.get(api, {
		params:{
			startDate:startDate,
			endDate:endDate
		},
		headers:{
			'method':'admin/registerLogByMonth',
			'token'	: window.sessionStorage.getItem("adminToken")
		}
	}).then(res => res.data).catch(err => err);  
};
