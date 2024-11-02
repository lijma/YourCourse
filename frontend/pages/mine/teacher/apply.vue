<template>
	<view class="content">
		<view :style="teacher_apply_enabled ? 'display: block' : 'display: none'">
			<view style="padding: 10px;">
				<uni-forms ref="form" :modelValue="formData" :rules="rules">
					<uni-forms-item required label="名称" name="teacherName">
						<uni-easyinput type="text" v-model="formData.teacherName" placeholder="..." />
					</uni-forms-item>
					<uni-forms-item required label="联系方式" name="email">
						<uni-easyinput type="text" v-model="formData.email" placeholder="..." />
					</uni-forms-item>
					<uni-forms-item label="城市" name="city">
						<uni-easyinput type="text" v-model="formData.city" placeholder="..." />
					</uni-forms-item>
					<uni-forms-item label="个人主页" name="url">
						<uni-easyinput type="text" v-model="formData.url" placeholder="..." />
					</uni-forms-item>
					<uni-forms-item required label="照片" name="logo">
						<file-uploader v-model="formData.logo"></file-uploader>
					</uni-forms-item>
					<uni-forms-item required label="讲师介绍" name="detail">
					</uni-forms-item>
					<editor id="editor" class="ql-container"
						@input="input"
						placeholder="点击输入..." @ready="onEditorReady"></editor>
				</uni-forms>
				<uni-popup ref="message" type="message">
					<uni-popup-message type="success" message="申请成功! 我们会在1周内完成审核并和您取得联系"></uni-popup-message>
				</uni-popup>
				<button @click="submit">提交申请</button>
			</view>
		</view>
		<view :style="teacher_apply_enabled ? 'display: none' : 'display: block'">
			未开放注册！请联系客服
		</view>	
	</view>
</template>

<script>
	import JiangqieNoData from "@/components/nodata/nodata";
	import Rest from '@/utils/rest';
	import Config from '@/utils/config';
	import Alert from '@/utils/alert';
	import Oss from '@/utils/oss.js'
	import Util from '@/utils/util';
	import FileUploader from '@/components/file-uploader/file-uploader.vue';
	import Toggle from '@/utils/toggle.js'

	export default {
		data() {
			return {
				teacher_apply_enabled: undefined,
				club_apply_enabled: undefined,
				formData: {
					teacherName: undefined,
					email: undefined,
					city: undefined,
					url: undefined,
					logo: undefined,
					detail: undefined
				},
				rules: {
					teacherName: {
						rules: [{
								required: true,
								errorMessage: '请输入名称',
							},
							{
								minLength: 1,
								maxLength: 100,
								errorMessage: '标题长度在 {minLength} 到 {maxLength} 个字符',
							}
						]
					},
					email: {
						rules: [{
							required: true,
							errorMessage: '请输入联系方式',
						}]
					},
					detail: {
						rules: [{
							required: true,
							errorMessage: '请输入内容',
						}]
					},
					logo: {
						rules: [{
							required: true,
							errorMessage: '请选择照片',
						}, ]
					}
				}
			};
		},

		onLoad(options) {
			this.fetchToggle();
		},

		methods: {
			fetchToggle: async function(){
				await Toggle.loadToggle().then(data => {
					this.teacher_apply_enabled = data.teacher_apply_enabled
					this.club_apply_enabled = data.club_apply_enabled
				});
			},
			onEditorReady() {
				// #ifdef MP-BAIDU
				this.editorCtx = requireDynamicLib('editorLib').createEditorContext('editor');
				// #endif
			
				// #ifdef APP-PLUS || H5 ||MP-WEIXIN
				uni.createSelectorQuery().select('#editor').context((res) => {
					this.editorCtx = res.context
				}).exec()
				// #endif
			},
			input: function(event){
				this.formData.detail = event.detail.html;
			},
			submit() {
				this.$refs.form.validate().then(res => {
					var url = `${Config.JQ_DOMAIN}/api/wetalk/teachers`
					Rest.post(url, this.formData).then(res => {
						Alert.success('申请成功，我们会在1周内完成审核并和你取得联系！');
						setTimeout(function() {
							Util.navigateBack();
						}, 3000); //Delay 2 seconds
						
					}, err => {
						Alert.error("申请失败，请联系管理员！")
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
