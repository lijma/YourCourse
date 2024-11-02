<template>
	<view class="content">

		<view :style="activity == null ? 'display: none' : 'display: block'">
			<view class="zhuige-points-goods-img">
				<view class="zhuige-point-goods">
					<view class="zhuige-point-goods-title">
						<view>{{activity.title}}</view>
						<view>
							<text>{{activity.siteName}}</text>
							<text>{{activity.startAt}}</text>
						</view>
					</view>
					<view class="zhuige-point-goods-info">

						<view class="zhuige-point-goods-header">角色管理</view>
						<view class="zhuige-point-goods-cont">
							<view class="example">
								<!-- Dynamic form validation -->
								<uni-forms ref="dynamicForm" :modelValue="formData">
									<uni-forms-item label="角色模版" required name="name">
										<picker @change="bindPickerChange" :value="index" :range="array">
											<view class="uni-input">{{array[index]}}</view>
										</picker>
									</uni-forms-item>
									<uni-forms-item required v-for="(item,index) in formData.roles" :key="index"
										:label="'角色'+(index+1)">
										<view class="form-item">
											<uni-easyinput v-model="item.roleText" placeholder="请输入角色" />
											<button class="button" size="mini" type="default"
												@click="del(index)">删除</button>
										</view>
									</uni-forms-item>
								</uni-forms>
								<view class="button-group">
									<button type="primary" size="mini" @click="add">新增角色</button>
									<button size="mini" @click="reset">重置</button>
									<button type="primary" size="mini" @click="save()">提交保存</button>
								</view>
							</view>

						</view>
					</view>

				</view>
				<!-- <image :src="activity.img" style="width: 100%;"></image> -->
			</view>


		</view>


		<uni-popup ref="confirm" type="dialog">
			<uni-popup-dialog type="success" cancelText="关闭" confirmText="同意" title="保存确认"
				:content="'确定保存吗？该操作会导致丢失报名数据'" @confirm="submit()">
			</uni-popup-dialog>
		</uni-popup>

	</view>
</template>

<script>
	import JiangqieNoData from "@/components/nodata/nodata";
	import Rest from '@/utils/rest';
	import Config from '@/utils/config';
	import Alert from '@/utils/alert';
	import Util from '@/utils/util';
	import Auth from '@/utils/auth';

	export default {
		data() {
			return {
				activityId: null,
				activity: {
					title: ''
				},
				array: [],
				templates: [],
				index: 0,
				formData: {
					template: '',
					roles: [],
					activityId: ''
				}
			};
		},

		components: {
			JiangqieNoData
		},

		onLoad(options) {
			this.activityId = options.id;
			this.formData.activityId = this.activityId
			this.loadActitiy();
			this.loadTemplates();
		},

		methods: {
			bindPickerChange: function(e) {
				this.index = e.detail.value
				this.formData.roles = this.templates[this.index].roles
				this.formData.template = this.templates[this.index].code
			},

			reset() {
				this.loadActitiy();
			},

			clickLink(link) {
				Util.openLink(link);
			},

			checkLogin() {
				if (Auth.getToken()) {
					return;
				}
				Util.openLink('/pages/login/login');
			},

			save() {
				this.$refs.confirm.open()
			},

			loadTemplates() {
				let that = this
				var url = `${Config.JQ_DOMAIN}/api/wetalk/template-config`
				Rest.get(url).then(data => {
					if (data == null) {
						that.templates = [{
							code: 'none',
							name: '自定义',
							roles: []
						}, ]
						that.array=['默认模板'];
						return
					}
					var result = JSON.parse(data);
					that.templates = result.templates;
					that.array = result.labels
				}, err => {
					that.templates = [{
						code: 'none',
						name: '自定义',
						roles: []
					}, ]
					that.array = ['默认模板'];
				});

			},

			loadActitiy() {
				var url = `${Config.JQ_DOMAIN}/api/wetalk/activities/${this.activityId}`
				Rest.get(url).then(data => {
					this.activity = data.activity
					this.formData.audiences = data.audiences
					this.formData.roles = data.roles
					this.formData.template = data.template
				}, err => {
					this.activity = null
					Alert.error("加载活动失败，请稍后重试！")
				});
			},

			submit() {
				//this.checkLogin()
				this.$refs.dynamicForm.validate().then(res => {
					var url = `${Config.JQ_DOMAIN}/api/wetalk/activities/roles`
					Rest.post(url, this.formData).then(res => {
						Alert.success("保存成功")
					}, err => {
						Alert.error("保存失败！")
					});
				}).catch(err => {
					Alert.error("参数不完整")
				})
			},

			add() {
				this.formData.roles.push({
					roleText: '角色名称'
				})
			},
			del(id) {
				let index = this.formData.roles.findIndex(v => v.id === id)
				this.formData.roles.splice(index, 1)
			},
		}
	}
</script>

<style lang="scss">
	@import "@/style/style.scss";

	page,

	.segmented-control {
		margin-bottom: 15px;
	}

	.button-group {
		display: flex;
		justify-content: space-around;
	}

	.form-item {
		display: flex;
		align-items: center;
	}

	.button {
		display: flex;
		align-items: center;
		height: 35px;
		margin-left: 10px;
	}

	.content {
		background: #FFFFFF;
	}

	.zhuige-points-goods-img {
		position: relative;
		background-size: 100%;
		padding-top: 50px;
	}

	.zhuige-points-goods-img,
	.zhuige-points-goods-img swiper,
	.zhuige-points-goods-img swiper-item,
	.zhuige-points-goods-img image {}

	.zhuige-point-goods {
		position: relative;
		margin-top: -50rpx;
		padding: 0 0 200rpx;
		z-index: 2;
		background-color: transparent;
	}

	.zhuige-point-goods-title {
		background: #FFFFFF;
		background-color: transparent;
	}

	.zhuige-point-goods-title view:nth-child(1) {
		padding: 50rpx 30rpx 30rpx;
		font-weight: 600;
		font-size: 30rpx;
		background-color: transparent;
	}

	.zhuige-point-goods-title view:nth-child(2) text:nth-child(2) {
		font-size: 20rpx;
		font-weight: 200;
		color: #666666;
	}

	.zhuige-point-goods-title view:nth-child(2) {
		padding: 20rpx 30rpx 30rpx;
		display: flex;
		align-items: center;
	}

	.zhuige-point-goods-title view:nth-child(2) text:nth-child(1) {
		font-size: 20rpx;
		font-weight: 600;
		color: #dd524d;
		padding-right: 20rpx;
	}

	.zhuige-point-goods-info {
		padding: 30rpx;
		margin-bottom: 30rpx;
		background: #FFFFFF;
		background-color: transparent;
	}

	.zhuige-point-goods-header {
		font-size: 36rpx;
		font-weight: 600;
		padding-bottom: 30rpx;
	}

	.zhuige-point-goods-item {
		background-color: transparent;
	}

	.zhuige-point-goods-btn {
		display: flex;
		width: 70%;
		margin: 0 15%;
		position: fixed;
		bottom: 80rpx;
	}

	.zhuige-point-goods-btn .view {
		height: 3rem;
		line-height: 3rem;
		text-align: center;
		border-radius: 12rpx;
		color: #FFFFFF;
		font-weight: 400;
	}

	.zhuige-point-goods-btn .view image {
		height: 60rpx;
		width: 60rpx;
		margin-top: 16rpx;
	}


	.zhuige-point-goods-btn .view {
		width: 100%;
		background: #dd524d;
	}

	/* --- 自定义轮播图指示点 --- */
	.zhuige-points-goods-img swiper .wx-swiper-dot,
	.zhuige-points-goods-img uni-swiper .uni-swiper-dot {
		margin-bottom: 50rpx;
	}
</style>
