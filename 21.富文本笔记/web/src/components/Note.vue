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
		<WangEditor v-show="editMode && note.type == 0" @func="setContent" ref="wangEditor"></WangEditor>

		<!-- 阅读模式 -->
		<div v-show="!editMode" class="ColorMain" v-html="content"></div>
	</div>
</template>

<script>
	import WangEditor from "../components/WangEditor.vue"
	import {
		req_getNoteContent,
		req_setNoteContent
	} from "../api.js";
	export default {
		components: {
			WangEditor
		},
		data() {
			return {
				//编辑模式
				editMode: false,
				note: {},
				content: "",
				contentMd: ""
			}
		},
		methods: {
			//初始化数据，防止在笔记切换过程中造成的数据混乱
			init() {
				this.editMode = false;
				this.note = {};
				this.content = "";
				this.contentMd = "";
			},
			/**
			 * 切换到编辑模式，加载markdown编辑器
			 */
			handleEdit() {
				this.editMode = true;

				if (this.note.type == 0) {
					this.$refs.wangEditor.load(this.content);
				}
			},
			/**
			 * 删除笔记
			 */
			handleDelete() {
				this.editMode = false;
				this.show = false;
			},
			/**
			 * 切换到阅读模式
			 */
			handleView() {
				this.editMode = false;

			},
			// WangEditor 中内容发生变化时重新设置内容
			setContent(content) {
				this.content = content;
			},
			handleSave() {
				this.content = this.$refs.wangEditor.get();
				req_setNoteContent(this.note.id, this.content).then(response => {
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
					}
				});
			},
			// 获取笔记内容
			getContent() {
				req_getNoteContent(this.note.id).then(response => {
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
						if (data) {
							this.content = data;
						} else {
							this.content = "";
						}

					}
				});
			},
			load(note) {
				this.init();
				this.note = note;
				this.getContent();
			}
		},
		mounted() {

		}
	}
</script>

<style scoped>
</style>
