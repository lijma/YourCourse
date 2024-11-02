<template>
	<view class="content">
		<view :style="site == null ? 'display: none' : 'display: block'">
			<view class="zhuige-points-goods-img">
				<view class="zhuige-point-goods">
					<view style="margin-top: 70px;">
						<uni-row class="demo-uni-row">
							<uni-col :span="8">
								<image class="logo" :src="site.logo" mode="aspectFill">
							</uni-col>
							<uni-col :span="10">
								<view style="margin-top: 25%;">
									<b>{{site.siteName}}</b>
									<br/>
									
								</view>
							</uni-col>
							<uni-col :span="6">
								<view style="margin-top: 10%;">
									<uni-fav style="margin-bottom: 10px;" :checked="favor" :contentText="favorTag" @click="favClick()" />
									<uni-fav :checked="join" :contentText="joinTag" @click="joinClick()" />
								</view>
							</uni-col>
						</uni-row>
						<uni-row class="demo-uni-row">
							<view class="zhuige-point-goods-info">		
								{{site.email}}
							</view>
						</uni-row>
						<uni-row class="demo-uni-row">
							<view class="zhuige-point-goods-info">
								<view class="zhuige-point-goods-header">俱乐部介绍</view>
								<rich-text :nodes="site.detail"></rich-text>
							</view>
						</uni-row>

					</view>
					<view class="zhuige-point-goods-info">
						<template>
							<view class="zhuige-point-goods-header">俱乐部活动列表</view>
							<view v-for="(item,index) in activities" :key="index" class="">
								<uni-card is-full :is-shadow="true" class="panel"
									@click="clickLink('/pages/activity/activity?id=' + item.id)">
									<uni-row class="zhuige-point-goods-item">
										<uni-col :span="8">
											<image :src="item.img" style="width: 80px; height: 80px;"></image>
										</uni-col>
										<uni-col :span="16">
											<uni-row class="demo-uni-col dark">{{item.title}}</uni-row>
											<uni-row class="demo-uni-col dark" style="font-size: 10px;">
												{{item.siteName}} | {{item.startAt}}
											</uni-row>
										</uni-col>
									</uni-row>
								</uni-card>
							</view>
						</template>


					</view>

				</view>
				<!-- <image :src="activity.img" style="width: 100%;"></image> -->
			</view>

		</view>
		
		<view>
			<!-- 输入框示例 -->
			<uni-popup ref="inputDialog" type="dialog">
				<uni-popup-dialog ref="inputClose" 
					mode="input"
					title="确定是否加入俱乐部？"
					placeholder="输入名字或邮箱..." 
					@confirm="joinSite"></uni-popup-dialog>
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
				siteId: undefined,
				favor: false,
				favorTag: {
					contentDefault: '收藏',
					contentFav: '已收藏'
				},
				join: false,
				joinTag: {
					contentDefault: '入会',
					contentFav: '已提交申请'
				},
				site: {},
				activities: []
			};
		},

		components: {
			JiangqieNoData
		},

		onLoad(options) {
			this.siteId = options.id;
			this.load()
		},
		
		onShareAppMessage() {
			return {
				title: this.site.siteName,
				path: 'pages/site/site?id=' + this.siteId,
				imageUrl: this.site.logo
			};
		},
		
		
		// #ifdef MP-WEIXIN
		onShareTimeline() {
			return {
				title: this.site.siteName,
				imageUrl: this.site.logo
			};
		},
		// #endif

		methods: {
			clickLink(link) {
				Util.openLink(link);
			},
			
			checkLogin(event) {
				if (Auth.getToken()) {
					return;
				}
				Util.openLink('/pages/login/login');
				event.stopPropagation();
			},
			
			joinSite(name) {
				this.checkLogin()
				let that = this
				var url = `${Config.JQ_DOMAIN}/api/wetalk/sites/invite`
				var data = {
					userName: name,
					siteId: this.siteId
				}
				Rest.post(url, data).then(res => {
					Alert.success("申请成功！")
					that.join = !that.join
				}, err => {
					Alert.error("申请失败")
				});
			},
			
			favorUpdate() {
				this.checkLogin()
				let that = this
				var url = `${Config.JQ_DOMAIN}/api/wetalk/favor`
				var data = {
					refId: this.siteId,
					favor: that.favor,
					favorType: 'SITE',
					refTitle: this.site.siteName
				}
				Rest.post(url, data).then(res => {
					Alert.error("关注成功！")
				}, err => {
					Alert.error("关注失败")
				});
			},
			
			inputDialogToggle() {
				this.$refs.inputDialog.open()
			},
			favClick() {
				this.favor = !this.favor
				this.favorUpdate()
			},
			joinClick() {
				if (!this.join) {
					this.inputDialogToggle();
				}
			},
			load() {
				var url = `${Config.JQ_DOMAIN}/api/wetalk/sites/${this.siteId}`
				Rest.get(url).then(data => {
					this.site = data.site;
					this.activities = data.activities
				}, err => {
					console.log(err)
				});
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

	.logo {
		height: 100px;
		width: 100px;
		float: left;
		margin-left: 10px;
		border-radius: 50%;
	}

	.middle {
		text-align: left;
		vertical-align: middle;
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
