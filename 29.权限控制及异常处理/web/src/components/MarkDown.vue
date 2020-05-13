<!-- 将mavon-editor组件包装下，作为markdown编辑器插件 -->
<template>
	<div>
		<mavon-editor v-model="value" ref="md" @imgAdd="imgAdd" @change="change" @save="save"  style="min-height: 600px" />
	</div>
</template>
<script>
	import {
		mavonEditor
	} from 'mavon-editor';
	import 'mavon-editor/dist/css/index.css';
	import axios from 'axios';
	import {
		editorImgUploadUrl
	} from "../api.js";
	export default {

		// 注册
		components: {
			mavonEditor
		},
		data() {
			return {
				value: "",
				render: "",
				noteId:0
			}
		},
		methods: {
			// 将图片上传到服务器，返回地址替换到md中
			imgAdd(pos, $file) {
				/**
				 * 服务端接口用 MultipartFile file 参数接收的，此处用formdata.append('file', $file)传
				 */
				let formdata = new FormData();
				formdata.append('file', $file);
				formdata.append('noteId',this.noteId);
				formdata.append('token', window.localStorage.getItem("token"));
				
				axios.post(editorImgUploadUrl, 
					formdata
				, {
					headers: {
						'Content-Type': 'multipart/form-data'
					}
				}).then(response => {
					let {
						success,
						message,
						data
					} = response.data;

					//应答不成功，提示错误信息
					if (success !== 0) {
						this.$message({
							message: message,
							type: 'error'
						});
					} else {
						this.$refs.md.$img2Url(pos, data);
					}
				}).catch(err => {
					this.$notify.error({
						title: 'Failed',
						message: err
					});
				});

			},
			// 编辑器内容发生改变时的回调
			change(value, render) {
				// render 为 markdown 解析后的结果[htmlStr]
				this.value = value;
				this.render = render;
				/**
				 * 在这里一定要用Base64.encodeURI()，不能用Base64.encode(),否则会出现中文乱码情况
				 */
				// this.$emit('func',value,render);
			},
			/**
			 * Ctrl + S 触发的操作
			 * @param {Object} value
			 * @param {Object} render
			 */
			save(value, render) {
				// render 为 markdown 解析后的结果[htmlStr]
				this.value = value;
				this.render = render;
				this.func();
			},
			/**
			 * 获取文本内容
			 */
			getText() {
				return this.value;
			},
			/**
			 * 获取解析后的 Html 内容
			 */
			getHtml() {
				return this.render;
			},
			//设置 rander 的值是为了保证当编辑器内容没有变化时能取到原来的值
			load(value, render,noteId) {
				this.value = value;
				this.render = render;
				this.noteId = noteId;
			},
			func() {
				this.$emit('func');
			}
		},
		mounted() {

		}
	}
</script>
