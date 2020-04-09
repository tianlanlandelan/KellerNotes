<!-- 笔记列表 -->
<template>
	<div>
		<draggable group="note" v-model="list" @change="noteListChange()">
			<!-- 笔记列表 -->
			<div class="font16 ColorCommon border-three padding5" v-for="item in list" :key="item.id" @click="currentNote(item)">
				<div :class="note.id == item.id ? 'note-bg':'cursorPointer '">
					<el-row>
						<el-col :span="2" :offset="1">
							<i v-if="item.type == 0" class="el-icon-document"></i>
							<i v-else class="el-icon-collection-tag"></i>
						</el-col>

						<el-col :span="14">
							{{item.title.length > 10 ?item.title.substring(0,10) + '...' : item.title}}
						</el-col>
						<el-col :span="6">
							{{format.formatDate(item.createTime)}}
						</el-col>
						<el-col :span="1" class="alignRight cursorPointer" v-show="note.id == item.id">
							<i class="el-icon-delete" @click="handleDelete"></i>
						</el-col>
					</el-row>
				</div>

			</div>
		</draggable>
	</div>
</template>

<script>
	import draggable from 'vuedraggable';
	import {
		format
	} from "../data.js";
	import {
		
		req_getNoteList,
		req_noteReSort
	} from "../api.js";
	export default {
		components: {
			draggable
		},
		data() {
			return {
				format: format,
				list: [],
				note: {}
			}
		},
		methods: {
			currentNote(note) {
				this.note = note;
				this.func();
			},
			getNoteList(notesId) {
				req_getNoteList(notesId).then(response => {
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
						if (data.length > 0) {
							this.list = data;
							this.note = this.list[0];
							this.func();
						}

					}
				});
			},
			/**
			 * 笔记列表发生变化时执行的方法
			 */
			noteListChange() {
				for (var i = 0; i < this.list.length; i++) {
					//如果笔记的顺序没有发生变化，不做处理
					if(this.list[i].sort == i ){
						continue;
					}
					//当笔记顺序发生变化时，重新排序
					req_noteReSort(this.list[i].id, i).then(response => {
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
						}
					});
				}
			},
			//删除笔记
			handleDelete(){
				
			},
			load(notesId) {
				this.getNoteList(notesId);
			},
			func() {
				this.$emit('func', this.note);
			}
		},
		mounted() {
			/**
			 * draggable在火狐浏览器中使用时，拖拽事件会打开新的标签页，在此要阻止窗口事件
			 * @param {Object} event
			 */
			document.body.ondrop = function (event) {
				event.preventDefault();
				event.stopPropagation();
			}
		}
	}
</script>

<style scoped>
	.note-bg {
		background-color: #f0f1f1;
	}
</style>
