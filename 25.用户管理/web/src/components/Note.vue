<!-- 笔记详情 -->
<template>
	<div>
		<el-row class="border-2-info font18 ColorCommon marginBottom10 padding5">
			<el-col :span="20">
				<div class="font-bold ">{{note.title}}</div>
			</el-col>
			<!-- 操作按钮 -->
			<el-col :span="2" class="alignRight" v-show="!editMode">
				<!-- 编辑 -->
				<el-link :underline="false" @click="handleEdit()">
					<i class="el-icon-edit font24" title="编辑"></i>
				</el-link>
			</el-col>
			<!-- 操作按钮 -->
			<el-col :span="2" class="alignRight" v-show="editMode">
				<!-- 预览 -->
				<el-link :underline="false" @click="handleView()">
					<i class="el-icon-view font24" title="预览"></i>
				</el-link>
			</el-col>
			<!-- 操作按钮 -->
			<el-col :span="2" class="alignRight" v-show="editMode">
				<!-- 保存 -->
				<el-link :underline="false" @click="handleSave()" v-show="editMode">
					<i class="el-icon-check font24" title="保存"></i>
				</el-link>
			</el-col>

		</el-row>
		<!-- 仅在富文本笔记的编辑模式中使用 WandEditor -->
		<WangEditor class="note-content" v-show="editMode && note.type == 0" ref="wangEditor"></WangEditor>

		<MarkDown class="note-content" v-show="editMode && note.type == 1" ref="markDown" @func="handleSave"></MarkDown>
		<!-- 阅读模式 -->
		<div v-show="!editMode" class="ColorMain note-content" v-html="note.html"></div>
	</div>
</template>

<script>
	import WangEditor from "./WangEditor.vue"
	import MarkDown from "./MarkDown.vue"

	import {
		editorImgCheck
	} from "../data.js";

	import {
		req_getNoteInfoById,
		req_setNoteContent,
		req_delNoteImg
	} from "../api.js";
	export default {
		components: {
			WangEditor,
			MarkDown
		},
		data() {
			return {
				//编辑模式
				editMode: false,
				note: {}
			}
		},
		methods: {
			//初始化数据，防止在笔记切换过程中造成的数据混乱
			init() {
				this.editMode = false;
				this.note = {};
			},
			/**
			 * 切换到编辑模式，加载编辑器
			 */
			handleEdit() {
				this.editMode = true;

				//富文本笔记加载 WangEditor
				if (this.note.type == 0) {
					this.$refs.wangEditor.load(this.note.html, this.note.id);
				}
				// MarkDown 笔记加载 mavon-editor
				else if (this.note.type == 1) {
					this.$refs.markDown.load(this.note.text, this.note.html, this.note.id);
				}
			},
			/**
			 * 切换到阅读模式
			 */
			handleView() {
				this.editMode = false;
				this.handleSave();
			},
			/**
			 * 保存编辑内容
			 */
			handleSave() {
				let newText;
				let newHtml;
				if (this.note.type == 0) {
					newText = this.$refs.wangEditor.getText();
					newHtml = this.$refs.wangEditor.getHtml();
				} else if (this.note.type == 1) {
					newText = this.$refs.markDown.getText();
					newHtml = this.$refs.markDown.getHtml();
					
					//如果MarkDown笔记内容有变化，检查是否删除了图片，如果删除了图片，则请求服务器删除图片
					if(this.note.text !== newText){
						let delImgs = editorImgCheck.getMarkDownDelImgs(this.note.text,newText);
						if(delImgs.length > 0){
							for(let index in delImgs){
								this.handleDelImg(delImgs[index]);
							}
						}
					}
				} else {
					return;
				}
				
				//如果笔记内容有变化，保存
				if (this.note.html !== newHtml) {
					req_setNoteContent(this.note.id, newText, newHtml).then(response => {
						let {
							success,
							message
						} = response;
						//应答不成功，提示错误信息
						if (success !== 0) {
							this.$message({
								message: message,
								type: 'error'
							});
						} else {
							this.$notify({
								title: '保存成功',
								type: 'success'
							});
							this.note.text = newText;
							this.note.html = newHtml;
						}
					});
				} else {
					//笔记内容无变化，直接提示保存成功
					this.$notify({
						title: '保存成功',
						type: 'success'
					});
				}

			},
			
			// 获取笔记内容
			getNoteInfo() {
				req_getNoteInfoById(this.note.id).then(response => {
					let {
						success,
						message,
						data
					} = response;
					//应答不成功，提示错误信息
					if (success !== 0) {
						this.$message({
							message: message,
							type: 'error'
						});
					} else {
						if (data.text) {
							this.note.text = data.text;
						} else {
							this.note.text = "";
						}
						if (data.html) {
							this.note.html = data.html;
						} else {
							this.note.html = "";
						}
					}
				});
			},
			// 删除笔记中的图片触发的回调
			handleDelImg(imgName) {
				req_delNoteImg(imgName).then(response => {
					if (response.success === 0) {
						window.console.log("handleDelImg Success", imgName);
					} else {
						window.console.error("handleDelImg Error", imgName);
					}
				});
			},
			load(note) {
				this.init();
				this.note = note;
				this.getNoteInfo();
			}
		},
		mounted() {

		}
	}
</script>

<style scoped>
	.note-content {
		min-height: 400px;
	}
</style>
