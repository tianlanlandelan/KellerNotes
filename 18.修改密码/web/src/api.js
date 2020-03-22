import axios from 'axios';
/** 需要先登录再访问的路由 */
let api = 'api';
let form = "form"

/** 不需要登录就能访问的路由 */
let base = 'base';

let upload = 'upload';

/**
 * 获取注册验证码 1001
 */
export const req_getCodeForRegister = (email) => { 
    return axios.get(base + '/getCodeForRegister', {
		params:{
			email:email
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
    return axios.post(base + '/login', {
        email:user.email,
		password:user.password
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
    return axios.get(form, {
		headers:{
			'method':'userCard',
			'token'	: window.localStorage.getItem("token")
		}
	}).then(res => res.data).catch(err => err);  
};



export const req_addNote = (note) => { 
    return axios.post(api, {
		id			:note.id,
		userId		:note.userId,
		notesId		:note.notesId,
		chapterId	:note.chapterId,
        title		:note.title,
    },{
		headers:{
			'method':'note'
		}
	}).then(res => res.data).catch(err => err); 
};
export const req_saveNote = (id,name,value) => { 
    return axios.put(api, {
		id:id,
		name:name,
        value:value
    },{
		headers:{
			'method':'note/save'
		}
	}).then(res => res.data).catch(err => err); 
};

/**
 * 修改课时顺序
 */
export const req_updateNote = (id,chapterId,sort) => { 
    return axios.put(api, {
		id:id,
		chapterId:chapterId,
		sort:sort
    },{
		headers:{
			'method':'note'
		}
	}).then(res => res.data).catch(err => err); 
};

export const req_deleteTopic = (id) => { 
    return axios.delete(form, {
		params:{
			id:id
		},
		headers:{
			'method':'topicInfo'
		}
	}).then(res => res.data).catch(err => err); 
};
export const req_saveNotes = (notes) => { 
    return axios.post(api, {
		id:notes.id,
		userId:notes.userId,
        title:notes.title,
		subTitle:notes.subTitle
    },{
		headers:{
			'method':'notes'
		}
	}).then(res => res.data)
	.catch(err => err); 
};
export const req_saveChapter = (chapter) => { 
    return axios.post(api, {
		id:chapter.id,
        notesId:chapter.notesId,
		name:chapter.name,
		sort:chapter.sort
    },{
		headers:{
			'method':'chapter'
		}
	}).then(res => res.data)
	.catch(err => err); 
};
export const req_updateChapter = (id,sort,name) => { 
    return axios.put(api, {
		id:id,
		sort:sort,
		name:name
    },{
		headers:{
			'method':'chapter'
		}
	}).then(res => res.data).catch(err => err); 
};
export const req_getNotesList = (userId) => { 
    return axios.get(form, {
		params:{
			userId:userId
		},
		headers:{
			'method':'notes/getList'
		}
	}).then(res => res.data).catch(err => err);  
};

export const req_deleteChapter = (id) => { 
    return axios.delete(form, {
		params:{
			id:id
		},
		headers:{
			'method':'chapter'
		}
	}).then(res => res.data).catch(err => err); 
};
export const req_getCourse = (id) => { 
    return axios.get(form, {
		params:{
			id:id
		},
		headers:{
			'method':'course'
		}
	}).then(res => res.data).catch(err => err); 
};
export const req_getNoteList = (notesId) => { 
    return axios.get(form, {
		params:{
			notesId:notesId
		},
		headers:{
			'method':'note/getList'
		}
	}).then(res => res.data).catch(err => err); 
};


export const req_getNoteContent = (id,name) => { 
    return axios.get(form, {
		params:{
			id:id,
			name:name
		},
		headers:{
			'method':'note/getContent'
		}
	}).then(res => res.data).catch(err => err); 
};

