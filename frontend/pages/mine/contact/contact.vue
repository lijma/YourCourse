<template>
	<view class="content">

		<view style="padding: 10px;">
			<uni-forms ref="form" :modelValue="formData">
				<uni-forms-item label="姓名" name="name">
					<uni-easyinput type="text" v-model="formData.name" placeholder="请输入姓名" />
				</uni-forms-item>
				<uni-forms-item label="邮件" name="email">
					<uni-easyinput type="text" v-model="formData.email" placeholder="请输入邮件" />
				</uni-forms-item>
				<uni-forms-item label="意见" name="comment">
					<textarea type="text" v-model="formData.comment" placeholder="请输入反馈意见" />
				</uni-forms-item>
			</uni-forms>
			<button @click="submit">提交反馈</button>
		</view>
		
	</view>
</template>

<script>

	export default {
		data() {
			return {
				formData: {
					name: '',
					email: '',
					comment:''
				},
				loaded: true
			};
		}

	}
</script>

<style lang="scss">
	@import "@/style/style.scss";

</style>
