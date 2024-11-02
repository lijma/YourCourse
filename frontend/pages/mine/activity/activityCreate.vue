<template>
	<view class="content">
		<view :style="club_apply_enabled ? 'display: block' : 'display: none'">
			<view style="padding: 10px;">
				<uni-forms ref="form" :modelValue="formData" :rules="rules">
					<uni-forms-item required label="俱乐部" name="siteId">
						<picker style="border: #000000;" @change="changeSite" :value="index" :range="sites">
							<view class="uni-input">{{sites[index]}}</view>
						</picker>
					</uni-forms-item>
					<uni-forms-item required label="时间" name="startAt">
						<uni-datetime-picker type="datetime" return-type="timestamp" v-model="formData.startAt" />
					</uni-forms-item>
					<uni-forms-item required label="标题" name="title">
						<uni-easyinput type="text" v-model="formData.title" placeholder="请输入标题" />
					</uni-forms-item>
					
					<uni-forms-item label="观众数量" name="audiences">
						<uni-easyinput type="text" v-model="formData.audiences" placeholder="(可选)请输入可报名观众数量..." />
					</uni-forms-item>
					
					<uni-forms-item required label="活动照片" name="img">
						<file-uploader 
							v-model="formData.img" 
							@change="fileChange"></file-uploader>
					</uni-forms-item>

					<uni-forms-item required label="活动详情" name="detail">
					</uni-forms-item>
					<editor id="editor" class="ql-container"
							@input="input" placeholder="点击输入..." 
							@ready="onEditorReady">
					</editor>

				</uni-forms>

				<uni-popup ref="message" type="message">
					<uni-popup-message type="success" message="创建成功"></uni-popup-message>
				</uni-popup>

				<button @click="submit">保存</button>

			</view>
		</view>
		<view :style="club_apply_enabled ? 'display: none' : 'display: block'">
			未开放！请联系客服
		</view>
	</view>

</template>

<script>
	import Rest from '@/utils/rest';
	import Config from '@/utils/config';
	import Alert from '@/utils/alert';
	import Util from '@/utils/util';
	import Oss from '@/utils/oss.js';
	import FileUploader from '@/components/file-uploader/file-uploader.vue'
	import Toggle from '@/utils/toggle.js'
	
	export default {
		data() {
			return {
				club_apply_enabled: false,
				activityId: undefined,
				siteIds: [],
				sites: [],
				index: 0,
				siteList: [],
				templates: ['无', '头马演讲'],
				template_index: 0,
				formData: {
					siteId: '',
					siteName: '',
					title: '',
					startAt: undefined,
					audiences: undefined,
					img: undefined,
					detail: '',
				},
				image: {
					blobPath: undefined,
					name: undefined,
					file: undefined
				},
				rules: {
					siteId: {
						rules: [{
							required: true,
							errorMessage: '请选择俱乐部',
						}]
					},
					title: {
						rules: [{
								required: true,
								errorMessage: '请输入标题',
							},
							{
								minLength: 1,
								maxLength: 100,
								errorMessage: '标题长度在 {minLength} 到 {maxLength} 个字符',
							}
						]
					},
					// 对email字段进行必填验证
					startAt: {
						rules: [{
							required: true,
							errorMessage: '请输入时间',
						}]
					},
					// 对email字段进行必填验证
					audiences: {
						rules: [{
							required: false,
							errorMessage: '请输入观众数量',
						}, ]
					},
					
					img: {
						rules: [{
							required: true,
							errorMessage: '请选择活动照片',
						}, ]
					},
					
					detail: {
						rules: [{
								required: true,
								errorMessage: '请输入详情',
							},
							{
								minLength: 1,
								errorMessage: '详情不能为空',
							}
						]
					},
				}
			};
		},
		
		onLoad(options) {
			this.fetchToggle();
			console.log(options.id)
			if (options.id != undefined){
				this.activityId = options.id;
				this.loadActitiy();
			}
			
			this.loadSites()
			Oss.loadConfig()
		},
		
		components: {
			FileUploader	
		},

		methods: {
			fetchToggle: async function(){
				await Toggle.loadToggle().then(data => {
					this.club_apply_enabled = data.club_apply_enabled
				});
			},
			fileChange: function(event){
				console.log('---'+this.formData.img)
				console.log('-----'+event)
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
			input: function(event) {
				this.formData.detail = event.detail.html;
			},
			bindPickerChange: function(e) {
				this.template_index = e.detail.value
			},

			clickLink(link) {
				console.log(link)
				Util.openLink(link);
			},
			
			renderEditor(){
				let that = this
				uni.createSelectorQuery().select('#editor').context(function(res) {
					res.context.setContents({
						html: that.formData.detail,
						success: (res) => {
							console.log(res);
						},
						fail: (err) => {
							console.log(err);
						}
					})
				}).exec();

			},
			loadActitiy() {
				var url = `${Config.JQ_DOMAIN}/api/wetalk/activities/${this.activityId}`
				Rest.get(url).then(data => {
					this.formData = data.activity
					this.renderEditor()
				}, err => {
					this.activity = null
					Alert.error("加载活动失败，请稍后重试！")
				});
			},
			
			
			loadSites() {
				let that = this
				var url = `${Config.JQ_DOMAIN}/api/wetalk/sites`
				Rest.get(url).then(data => {
					if (data.length <= 0) {
						setTimeout(function() {
							Alert.error('请联系平台完成俱乐部认证！');
						}, 100); //Delay 2 seconds
						Util.navigateBack()
						return
					}
					this.sites = data.map(mysite => {
						return mysite.siteName;
					});
					this.siteIds = data.map(mysite => {
						return mysite.id;
					})
					this.formData.siteId = this.siteIds[0];
					this.formData.siteName = this.sites[0];


				}, err => {
					console.log(err)
				});
			},

			changeSite: function(e) {
				console.log(e)
				this.index = e.detail.value
				this.formData.siteId = this.siteIds[this.index];
				this.formData.siteName = this.sites[this.index];
				console.log("current site change to " + this.formData.siteId + " " + this.formData.siteName);
			},

			submit() {
				console.log("提交活动")
				this.$refs.form.validate().then(res => {
					var url = `${Config.JQ_DOMAIN}/api/wetalk/activities`
					Rest.post(url, this.formData).then(res => {
						Alert.success('活动保存成功！');
						setTimeout(function() {
							Util.navigateBack()
						}, 2000);

					}, err => {
						console.log(err)
					});
				}).catch(err => {
					Alert.error("参数不完整")
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
