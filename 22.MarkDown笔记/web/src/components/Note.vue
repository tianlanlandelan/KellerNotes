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
		<WangEditor v-show="editMode && note.type == 0" ref="wangEditor"></WangEditor>

		<MarkDown v-show = "editMode && note.type == 1" ref = "markDown"></MarkDown>
		<!-- 阅读模式 -->
		<div v-show="!editMode" class="ColorMain" v-html="note.html"></div>
	</div>
</template>

<script>
	import WangEditor from "./WangEditor.vue"
	import MarkDown from "./MarkDown.vue"
	
	import {
		req_getNoteInfoById,
		req_setNoteContent
	} from "../api.js";
	export default {
		components: {
			WangEditor,MarkDown
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
					this.$refs.wangEditor.load(this.note.html);
				}
				// MarkDown 笔记加载 mavon-editor
				else if(this.note.type == 1){
					this.$refs.markDown.load(this.note.text,this.note.html);
				}
			},
			/**
			 * 切换到阅读模式
			 */
			handleView() {
				this.editMode = false;
				this.handleSave();
			},
			handleSave() {
				let newText;
				let newHtml;
				if(this.note.type == 0){
					newText = this.$refs.wangEditor.getText();
					newHtml = this.$refs.wangEditor.getHtml();
				}else if(this.note.type == 1){
					newText = this.$refs.markDown.getText();
					newHtml = this.$refs.markDown.getHtml();
				}else{
					return;
				}
				//如果笔记内容有变化，保存
				if(this.note.html !== newHtml){
					req_setNoteContent(this.note.id, newText,newHtml).then(response => {
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
				}else{
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
						if(data.html){
							this.note.html = data.html;
						}else{
							this.note.html = "";
						}
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
</style>
