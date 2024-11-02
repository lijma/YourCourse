<template>
	<view class="content">
		<view :style="club_apply_enabled ? 'display: block' : 'display: none'">
			<view style="padding: 10px;">
				<uni-forms ref="form" :modelValue="formData" :rules="rules">
					<uni-forms-item required label="课程标题" name="title">
						<uni-easyinput type="text" v-model="formData.title" placeholder="请输入标题" />
					</uni-forms-item>
					<uni-forms-item label="开始时间" name="startAt">
						<uni-datetime-picker type="datetime" return-type="timestamp" v-model="formData.startAt" />
					</uni-forms-item>
					<uni-forms-item required label="结束时间" name="endAt">
						<uni-datetime-picker type="datetime" return-type="timestamp" v-model="formData.endAt" />
					</uni-forms-item>
					<uni-forms-item required label="报名截止" name="stopAt">
						<uni-datetime-picker type="datetime" return-type="timestamp" v-model="formData.stopAt" />
					</uni-forms-item>
					<uni-forms-item label="价格" name="price">
						<uni-easyinput type="number" v-model="formData.price" placeholder="(可选)默认免费" />
					</uni-forms-item>

					<uni-forms-item required label="课程照片" name="img">
						<file-uploader v-model="formData.img"></file-uploader>
					</uni-forms-item>
					<uni-forms-item required label="课程标签" name="img">
						<biner-input-tags @change="change" 
							:selectlist='selectlist' :disabled='false' placeholder='...'>
						</biner-input-tags>
					</uni-forms-item>
					<uni-forms-item label="详情海报" name="detail">
						<file-uploader v-model="formData.detail"></file-uploader>
					</uni-forms-item>


				</uni-forms>

				<uni-popup ref="message" type="message">
					<uni-popup-message type="success" message="创建成功"></uni-popup-message>
				</uni-popup>

				<button @click="submit">创建活动</button>

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
	import Oss from '@/utils/oss.js'
	import binerInputTags from '@/components/biner-input-tags/biner-input-tags'
	import FileUploader from '@/components/file-uploader/file-uploader.vue'
	import Toggle from '@/utils/toggle.js'
	
	export default {
		data() {
			return {
				club_apply_enabled: false,
				selectList: [],
				siteMap: new Map(),
				siteList: [],
				templates: ['无', '头马演讲'],
				template_index: 0,
				formData: {
					title: '',
					startAt: undefined,
					endAt: undefined,
					stopAt: undefined,
					price: undefined,
					img: undefined,
					labels: [],
					detail: undefined,
				},
				selectlist:[],
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
					endAt: {
						rules: [{
							required: true,
							errorMessage: '请输入时间',
						}]
					},
					stopAt: {
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
							errorMessage: '请输入课程照片',
						}, ]
					}
				}
			};
		},

		components: {
			binerInputTags,
			FileUploader
		},

		onLoad(options) {
			this.fetchToggle();
			this.loadTeacher()
			Oss.loadConfig()
		},

		methods: {
			fetchToggle: async function(){
				await Toggle.loadToggle().then(data => {
					this.club_apply_enabled = data.club_apply_enabled
				});
			},
			change(arr) {
				this.formData.labels = arr;
			},

			loadTeacher() {
				let that = this
				var url = `${Config.JQ_DOMAIN}/api/wetalk/teachers/valid`
				Rest.get(url).then(data => {
					console.log("---" + data)
					that.teacher = data
					if (that.teacher == '') {
						console.log("teacher" + that.teacher)
						setTimeout(function() {
							Alert.error('请联系平台完成讲师认证！');
						}, 100); //Delay 2 seconds
						Util.navigateBack()
						return
					}

				}, err => {
					that.teacher = null
					Alert.error("加载失败，请稍后重试！")
				});
			},

			bindPickerChange: function(e) {
				this.template_index = e.detail.value
			},

			clickLink(link) {
				Util.openLink(link);
			},

			loadSites() {
				var url = `${Config.JQ_DOMAIN}/api/wetalk/sites/mine`
				Rest.get(url).then(data => {
					console.log("sites size: " + data.length)
					this.siteList = data.map(mysite => {
						this.siteMap.set(mysite.site.id, mysite.site.siteName)
						return {
							value: mysite.site.id,
							text: mysite.site.siteName
						}
					})
					console.log("siteList size: " + this.siteList.length)
					console.log("siteMap size: " + this.siteMap.size)

				}, err => {
					console.log(err)
				});
			},

			changeSite() {
				this.formData.siteName = this.siteMap.get(this.formData.siteId);
				console.log("current site change to " + this.formData.siteName);
			},

			submit() {
				this.$refs.form.validate().then(res => {
					var url = `${Config.JQ_DOMAIN}/api/wetalk/programs`
					Rest.post(url, this.formData).then(res => {
						Alert.success('创建课程成功！');
						setTimeout(function() {
							Util.navigateBack()
						}, 3000);
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
