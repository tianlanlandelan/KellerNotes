<!-- 用户主页 -->
<template>
    <div>
        用户主页
    </div>
</template>

<script>
    export default{
		data(){
			return{
				
			}
		},
		methods:{
			
		},mounted(){
			this.$message.success(window.localStorage.getItem("token"));
		}
	}
</script>

<style scoped>
</style>


