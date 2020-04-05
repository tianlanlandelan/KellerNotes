<template>
	<div>

		<div :class="notes.id == item.id ? 'notes-item notes-bg':'notes-item cursorPointer'" v-for="(item,i) in list" :key="item.id + item.title"
			:title="item.subTitle" @click="currentNotes(item)">

			<span class="font18 ColorCommon">
				<i class="el-icon-notebook-1"></i>&nbsp;&nbsp;{{item.title}}
			</span>
			<span class="font12 ColorInfo"><i class="el-icon-notebook-2"></i>{{item.noteCount}}</span>
			<el-dropdown @command="handleCommand">
				<span class="el-dropdown-link">
					<i class="el-icon-arrow-down el-icon--right"></i>
				</span>

				<el-dropdown-menu slot="dropdown">

					<el-dropdown-item icon="el-icon-plus" :command="i + '-addRichNote'">新建富文本笔记</el-dropdown-item>
					<el-dropdown-item icon="el-icon-plus" :command="i + '-addMarkDownNote'">新建MarkDown笔记</el-dropdown-item>
					<el-dropdown-item icon="el-icon-plus" :command="i + '-addNotes'">新建笔记本</el-dropdown-item>
					<el-dropdown-item icon="el-icon-edit" :command="i + '-updateNotes'">修改笔记本</el-dropdown-item>
					<el-dropdown-item icon="el-icon-delete" :command="i + '-deleteNotes'">删除笔记本</el-dropdown-item>
				</el-dropdown-menu>
			</el-dropdown>
		</div>

		<!-- 遮罩层 -->
		<div v-show="visible" class="box"></div>
		<!-- 操作 -->
		<div v-show="visible" class="boxLittle">
			<!-- 新建笔记 -->
			<el-form label-position="left" label-width="0px" v-show="status == 'addRichNote'">
				<el-form-item>
					<el-row>
						<el-col :span="16" :offset="4" class="alignCenter font24 font-bold">
							新建富文本笔记
						</el-col>
						<el-col :span="4" class="alignRight">
							<i class="el-icon-circle-close font24" @click="visible = false"></i>
						</el-col>
					</el-row>
					<span class="tip">笔记本名称</span>
					<el-input type="text" v-model="notes.title" disabled></el-input>
				</el-form-item>
				<el-form-item style="width:100%;">
					<span class="tip">笔记名称</span>
					<el-input type="text" autofocus v-model="noteTitle"></el-input>
				</el-form-item>
				<el-form-item style="width:100%;">
					<el-button type="primary" style="width:100%;" @click="addNote(0)">新建</el-button>
				</el-form-item>
			</el-form>

			<!-- 新建笔记本 -->
			<el-form label-position="left" label-width="0px" v-show="status == 'addNotes'">
				<el-form-item>
					<el-row>
						<el-col :span="16" :offset="4" class="alignCenter font24 font-bold">
							新建笔记本
						</el-col>
						<el-col :span="4" class="alignRight">
							<i class="el-icon-circle-close font24" @click="visible = false"></i>
						</el-col>
					</el-row>
					<span class="tip">笔记本名称</span>
					<el-input type="text" autofocus v-model="newNotes.title" placeholder="我的笔记本"></el-input>
				</el-form-item>
				<el-form-item style="width:100%;">
					<span class="tip">说明</span>
					<el-input type="text" v-model="newNotes.subTitle"></el-input>
				</el-form-item>
				<el-form-item style="width:100%;">
					<el-button type="primary" style="width:100%;" @click="addNotes()">新建</el-button>
				</el-form-item>
			</el-form>

			<!-- 修改笔记本 -->
			<el-form label-position="left" label-width="0px" v-show="status == 'updateNotes'">
				<el-form-item>
					<el-row>
						<el-col :span="16" :offset="4" class="alignCenter font24 font-bold">
							修改笔记本
						</el-col>
						<el-col :span="4" class="alignRight">
							<i class="el-icon-circle-close font24" @click="visible = false"></i>
						</el-col>
					</el-row>
					<span class="tip">笔记本名称</span>
					<el-input type="text" autofocus v-model="notes.title" placeholder="我的笔记本"></el-input>
				</el-form-item>
				<el-form-item style="width:100%;">
					<span class="tip">说明</span>
					<el-input type="text" v-model="notes.subTitle"></el-input>
				</el-form-item>
				<el-form-item style="width:100%;">
					<el-button type="primary" style="width:100%;" @click="updateNotes()">修改</el-button>
				</el-form-item>
			</el-form>

			<!-- 删除笔记本 -->
			<el-form label-position="left" label-width="0px" v-show="status == 'deleteNotes'">
				<el-form-item>
					<el-row>
						<el-col :span="16" :offset="4" class="alignCenter font24 font-bold">
							删除笔记本
						</el-col>
						<el-col :span="4" class="alignRight">
							<i class="el-icon-circle-close font24" @click="visible = false"></i>
						</el-col>
					</el-row>
					<P class="font20 ColorCommon">确定要<span class="font22 ColorDanger">删除</span>笔记本 <span class="font22 ColorMain">{{notes.title}}</span>
						吗？
						该操作将删除笔记本中所有笔记，且无法恢复！
					</P>
				</el-form-item>

				<el-form-item style="width:100%;">
					<el-row>
						<el-col :span="12" class="alignCenter">
							<el-button type="info" @click="visible = false">取消</el-button>
						</el-col>
						<el-col :span="12" class="alignCenter">
							<el-button type="danger" @click="deleteNotes()">删除</el-button>
						</el-col>
					</el-row>

				</el-form-item>
			</el-form>
		</div>
	</div>
</template>

<script>
	import {
		// format
	} from "../data.js";
	import {
		req_addNote,
		req_addNotes,
		req_saveNotes,
		req_getNotesList,
		req_deleteNotes
	} from "../api.js";
	export default {
		data() {
			return {
				list: [],
				notes: {},
				visible: false,
				status: "",
				newNotes: {},
				noteTitle: ""
			}
		},
		methods: {
			currentNotes(notes) {
				this.notes = notes;
				window.console.log(this.notes.title);
				this.func();
			},
			handleCommand(command) {
				window.console.log(command);
				let array = command.split("-");
				this.notes = this.list[array[0]];
				this.status = array[1];
				this.visible = true;
			},
			/**
			 * 获取笔记本列表
			 */
			getNotesList() {
				req_getNotesList().then(response => {
					//解析接口应答的json串
					let {
						data,
						message,
						success
					} = response;
					//应答不成功，提示错误信息
					if (success !== 0) {
						this.$message({
							message: message,
							type: 'error'
						});
					} else {
						this.list = data;
						this.notes = this.list[0];
						this.func();
					}
				});
			},
			/**
			 * 添加笔记
			 * @param {Object} type 笔记类型 0：富文本笔记  1：MarkDown 笔记
			 */
			addNote(type) {
				//关闭弹出框
				this.visible = false;
				let note ={
					title:this.noteTitle,
					type:type,
					notesId:this.notes.id
				}
				//添加笔记
				req_addNote(note).then(response => {
					//解析接口应答的json串
					let {
						message,
						success
					} = response;
					//应答不成功，提示错误信息
					if (success !== 0) {
						this.$message({
							message: message,
							type: 'error'
						});
					} else {
						//设置笔记本中笔记总数
						this.notes.noteCount = this.notes.noteCount + 1;	
						//回调到父组件，使其刷新该笔记本下的笔记列表
						this.func();
					}
				});
			},
			addNotes() {
				this.visible = false;
				req_addNotes(this.newNotes).then(response => {
					//解析接口应答的json串
					let {
						data,
						message,
						success
					} = response;
					//应答不成功，提示错误信息
					if (success !== 0) {
						this.$message({
							message: message,
							type: 'error'
						});
					} else {
						this.$message.success("笔记本创建完成");
						this.notes = data;
						this.func();
						this.list.push(this.notes);
						// this.getNotesList();	
					}
				});
				this.newNotes = {};
			},
			updateNotes() {
				this.visible = false;
				req_saveNotes(this.notes).then(response => {
					//解析接口应答的json串
					let {
						message,
						success
					} = response;
					//应答不成功，提示错误信息
					if (success !== 0) {
						this.$message({
							message: message,
							type: 'error'
						});
					} else {
						this.$message.success("笔记本修改完成");
						// this.getNotesList();	
					}
				});
			},
			deleteNotes() {
				this.visible = false;
				req_deleteNotes(this.notes.id).then(response => {
					//解析接口应答的json串
					let {
						message,
						success
					} = response;
					//应答不成功，提示错误信息
					if (success !== 0) {
						this.$message({
							message: message,
							type: 'error'
						});
					} else {
						this.$message.success("笔记本已删除");
						this.getNotesList();
					}
				});
			},
			load() {
				this.getNotesList();
			},
			func() {
				this.$emit('func', this.notes);
			}
		},
		mounted() {

		}
	}
</script>

<style scoped>
	.notes-bg {
		background-color: #f0f1f1;
	}

	.notes-item {
		padding-left: 10px;
	}
</style>
