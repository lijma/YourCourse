<template>
	<view class="content">
		<view :style="activity == null ? 'display: block' : 'display: none'">
			<jiangqie-no-data></jiangqie-no-data>
		</view>
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
						<view class="zhuige-point-goods-header">角色报名情况</view>
						<view class="zhuige-point-goods-cont">
							<uni-list>
								<uni-list-item v-for="(item,index) in roles" :key="index" :title="item.roleText"
									:note="item.userId != null ? item.userName : '待报名'" showArrow="item.userId == null"
									:rightText="item.userId == null ? '设置' : '修改'" clickable @click="open(item)">
								</uni-list-item>
							</uni-list>
						</view>
					</view>
					<view class="zhuige-point-goods-info">
						<view class="zhuige-point-goods-header">观众报名情况：{{audiences.length}}</view>
						<view class="zhuige-point-goods-cont">
							<uni-list>
								<uni-list-item v-for="(item,index) in audiences" :key="index" :title="item.roleText"
									:note="item.userId != null ? item.userName : '待报名'" showArrow="item.userId == null"
									:rightText="item.userId == null ? '设置' : '修改'" clickable @click="open(item)">
								</uni-list-item>
							</uni-list>
						</view>
					</view>
				</view>

			</view>
			<uni-popup ref="popup" class="" :animation="true" background-color="#fff">
				<view style="padding: 10px; ">
					<uni-forms ref="aform" :modelValue="formData" :rules="rules">
						<uni-forms-item label="姓名" name="userName" required>
							<uni-easyinput type="text" v-model="formData.userName" placeholder="请输入姓名或ID" />
						</uni-forms-item>
						<uni-forms-item label="邮件地址" name="email">
							<uni-easyinput type="text" v-model="formData.email" placeholder="请输入邮件" />
						</uni-forms-item>
					</uni-forms>
					<button @click="submit">确定</button>
				</view>
			</uni-popup>

		</view>

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
				activity: null,
				roles: [],
				audiences: [],
				formData: {
					userName: null,
					email: null,
					roleId: null,
				},
				name: {},
				rules: {
					userName: {
						rules: [{
								required: true,
								errorMessage: '请输入内容',
							},
							{
								minLength: 1,
								maxLength: 100,
								errorMessage: '长度在 {minLength} 到 {maxLength} 个字符',
							}
						]
					}
				},
			};
		},

		components: {
			JiangqieNoData
		},

		onLoad(options) {
			this.activityId = options.id;
			this.loadActitiy();
		},

		onShareAppMessage() {
			return {
				title: "WeTalk社区例会邀请函",
				path: 'pages/activity/activity?id=' + this.activityId
			};
		},

		// #ifdef MP-WEIXIN
		onShareTimeline() {
			return {
				title: "WeTalk社区例会邀请函"
			};
		},
		// #endif

		onPullDownRefresh() {
			this.loadGoods();
		},

		methods: {
			clickLink(link) {
				Util.openLink(link);
			},

			checkLogin() {
				if (Auth.getToken()) {
					return;
				}
				Util.openLink('/pages/login/login');
			},

			loadActitiy() {
				var url = `${Config.JQ_DOMAIN}/api/wetalk/activities/${this.activityId}`
				Rest.get(url).then(data => {
					this.activity = data.activity
					this.audiences = data.audiences
					this.roles = data.roles
				}, err => {
					this.activity = null
					Alert.error("加载活动失败，请稍后重试！")
				});
			},

			open(item) {
				// 通过组件定义的ref调用uni-popup方法 ,如果传入参数 ，type 属性将失效 ，仅支持 ['top','left','bottom','right','center']
				this.formData.roleId = item.id
				this.formData.userName = item.userName
				this.formData.email = item.email
				this.$refs.popup.open('centor')
			},

			submit() {
				this.checkLogin()
				this.$refs.aform.validate().then(res => {
					var url = `${Config.JQ_DOMAIN}/api/wetalk/activities/join`
					Rest.post(url, this.formData).then(res => {
						Alert.success("报名成功")
						this.loadActitiy()
						this.$refs.popup.close()
					}, err => {
						Alert.error("报名失败")
					});
				}).catch(err => {
					Alert.error("参数不完整")
				})
			},
			inputDialogToggle() {
				this.$refs.inputDialog.open()
			},

			joinSite(name) {
				this.checkLogin()
				var url = `${Config.JQ_DOMAIN}/api/wetalk/sites/invite`
				var data = {
					userId: this.activity.userId,
					userName: name,
					siteId: this.activity.siteId
				}
				Rest.post(url, data).then(res => {
					Alert.success("关注成功")
				}, err => {
					Alert.error("关注失败")
				});
			}
		}
	}
</script>

<style lang="scss">
	@import "@/style/style.scss";

	page,
	.content {
		background: #F5F5F5;
	}

	.zhuige-points-goods-img {
		position: relative;
		background-size: 100%;
		padding-top: 50px;
	}

	.zhuige-points-goods-img,
	.zhuige-points-goods-img swiper,
	.zhuige-points-goods-img swiper-item,
	.zhuige-points-goods-img image {
		height: 600rpx;
	}

	.zhuige-point-goods {
		position: relative;
		margin-top: -50rpx;
		padding: 0 0 200rpx;
		z-index: 2;
		background-color: transparent;
	}

	.zhuige-point-goods-title {
		border-radius: 16rpx;
		margin-bottom: 10rpx;
	}

	.zhuige-point-goods-title view:nth-child(1) {
		padding: 50rpx 30rpx 30rpx;
		font-weight: 600;
		font-size: 30rpx;
		border-bottom: 1rpx solid #DDDDDD;
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
		border-radius: 16rpx;
	}

	.zhuige-point-goods-header {
		font-size: 24rpx;
		font-weight: 600;
		padding-bottom: 30rpx;
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
