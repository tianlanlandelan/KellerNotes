create table user_info
(
	id int auto_increment primary key,
	type int not null comment '用户类型',
	email varchar(200) not null comment '邮箱',
	password varchar(200) null comment '密码',
	createTime datetime null,
	status int null comment '用户账号状态',
	isDelete int null,
	updateTime datetime null,
	updateUserId int null
)comment '用户信息表';
create index user_info_index_email on user_info (email);
create index user_info_index_status on user_info (status);
create index user_info_index_type on user_info (type);


INSERT INTO kyle_notes.user_info (id, type, email, password, createTime, status, isDelete, updateTime, updateUserId)
VALUES (1, 0, 'guyexing@foxmail.com', '123456', '2019-11-25 19:34:25', null, null, null, null);