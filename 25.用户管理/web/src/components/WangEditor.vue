<template>
	<div>
		<div  id="editor1"></div>
		
		<div class="content" id="editor2">
		</div>
	</div>
</template>

<script>
	import Editor from 'wangeditor';
	
	import {
		editorImgUploadUrl
	} from "../api.js";
	export default {
		data() {
			return {
				editor: new Editor("#editor1","#editor2"),
				noteId:0
			}
		},
		methods: {
			getText(){
				return this.editor.txt.text();
			},
			getHtml(){
				return this.editor.txt.html();
			},
			load(html,noteId){
				this.noteId = noteId;
				this.editor.txt.html(html);
			}
		},
		mounted() {
			let editor = this.editor;
			editor.customConfig.uploadFileName = "file";
			editor.customConfig.uploadImgServer = editorImgUploadUrl;
			editor.customConfig.uploadImgParams = {
				token: window.localStorage.getItem("token"),
				noteId: this.noteId
			};

			editor.customConfig.uploadImgHooks = {
				customInsert: function(insertImg, result, editor) {
					// 图片上传并返回结果，自定义插入图片的事件（而不是编辑器自动插入图片！！！）
					// insertImg 是插入图片的函数，editor 是编辑器对象，result 是服务器端返回的结果
					// result 必须是一个 JSON 格式字符串！！！否则报错
					// 举例：假如上传图片成功后，服务器端返回的是 {url:'....'} 这种格式，即可这样插入图片：
					var url = result.data;
					insertImg(url);
				}
			};
			this.editor.create();
		}
	}
</script>

<style scoped>
	."content"{
		
		min-height: 400px;
	}
</style>
