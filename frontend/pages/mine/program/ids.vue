<template>
	<view class="content">
		<view :style="program == null ? 'display: block' : 'display: none'">
			<jiangqie-no-data></jiangqie-no-data>
		</view>
		<view :style="program == null ? 'display: none' : 'display: block'">
			<view class="zhuige-points-goods-img">

				<view class="zhuige-point-goods" v-if="program != null">
					<view class="zhuige-point-goods-title">
						<view>{{program.title}}</view>
						<view>
							<text>报名截止：{{program.stopAt}}</text>
							<text>课程开始: {{program.startAt}}</text>
						</view>
					</view>
					<view class="zhuige-point-goods-info">
						<view class="zhuige-point-goods-header">
							观众报名情况：{{audiences.length}}
						</view>
						<view class="zhuige-point-goods-cont">
							<uni-list>
								<uni-list-item 
									v-for="(item,index) in audiences" 
									:key="index" 
									:title="item.userName"
									:rightText="item.email != null ? item.email : '联系方式未更新'" >
								</uni-list-item>
							</uni-list>
						</view>
					</view>
				</view>

			</view>

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
				programId: null,
				program: null,
				audiences: []
			};
		},

		components: {
			JiangqieNoData
		},

		onLoad(options) {
			//this.programId = '1614167289624727553'
			this.programId = options.id;
			this.loadProgram();
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

			loadProgram() {
				var url = `${Config.JQ_DOMAIN}/api/wetalk/programs/${this.programId}/list`
				Rest.get(url).then(data => {
					this.program = data.program
					this.audiences = data.list
				}, err => {
					this.program = null
					Alert.error("加载活动失败，请稍后重试！")
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
