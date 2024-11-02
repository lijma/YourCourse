<template>
	<view class="content">
		<view style="padding: 10px;">
			<uni-forms ref="form" :modelValue="formData" :rules="rules">
				<uni-forms-item label="昵称" name="siteName">
					<uni-easyinput type="text" v-model="formData.userName" placeholder="..." />
				</uni-forms-item>
				<uni-forms-item label="头像" name="icon">
					<file-uploader v-model="formData.icon"></file-uploader>
				</uni-forms-item>
			</uni-forms>
			<button @click="submit">更改设置</button>
		</view>

	</view>
</template>

<script>
	import Rest from '@/utils/rest';
	import Config from '@/utils/config';
	import Alert from '@/utils/alert';
	import Oss from '@/utils/oss.js'
	import Util from '@/utils/util';
	import FileUploader from '@/components/file-uploader/file-uploader.vue';

	export default {
		data() {
			return {
				level: 1,
				formData: {
					userName: undefined,
					icon: undefined
				},
				rules: {
					userName: {
						rules: [{
								required: false,
								errorMessage: '请输入名称',
							},
							{
								minLength: 1,
								maxLength: 100,
								errorMessage: '标题长度在 {minLength} 到 {maxLength} 个字符',
							}
						]
					},
					icon: {
						rules: [{
							required: false,
							errorMessage: '请选择照片',
						}, ]
					}
				}
			};
		},

		onLoad(options) {
			console.log(options.level)
			this.level = options.level;
			this.loadUser()
		},

		components: {
			FileUploader
		},


		methods: {
			loadUser() {
				var url = `${Config.JQ_DOMAIN}/api/wetalk/profile`
				Rest.get(url).then(data => {
					console.log("user: " + data)
					this.formData = data;
				}, err => {
					console.log(err)
				});
			},
			
			submit() {
				this.$refs.form.validate().then(res => {
					var url = `${Config.JQ_DOMAIN}/api/wetalk/profile/update`
					Rest.post(url, this.formData).then(res => {
						Alert.success('更新成功！');
						setTimeout(function() {
							Util.navigateBack(this.level);
						}, 2000); //Delay 2 seconds
					}, err => {
						console.log(err)
					});
				}).catch(err => {
					console.log(this.formData)
				})
			}
		}

	}
</script>

<style lang="scss">
	@import "@/style/style.scss";

	.slot-box {
		/* #ifndef APP-NVUE */
		display: flex;
		/* #endif */
		flex-direction: row;
		align-items: center;
	}

	.slot-image {
		/* #ifndef APP-NVUE */
		display: block;
		/* #endif */
		margin-right: 10px;
		width: 30px;
		height: 30px;
	}

	.slot-text {
		flex: 1;
		font-size: 14px;
		color: #4cd964;
		margin-right: 10px;
	}
</style>
