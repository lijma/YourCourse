<template>
	<view class="content">
		<view class="panel">
			<uni-search-bar placeholder="输入俱乐部或者活动名称..." @confirm="search" :focus="true" v-model="searchValue"
				@blur="blur" @focus="focus" @input="input" @cancel="cancel" @clear="clear">
			</uni-search-bar>
		</view>

		<view v-for="(site,index) in sites" :key="site.id" class="">
			<uni-card :title="site.siteName" :sub-title="site.email" :extra="site.city" :thumbnail="site.logo"
				@click="clickLink('/pages/site/site?id=' + site.id)">
				<rich-text class="uni-body" :nodes="site.detail"></rich-text>
			</uni-card>
		</view>

		<view v-for="(item,index) in activity_list" :key="index" class="">
			<uni-card is-full :is-shadow="true" class="panel"
				@click="clickLink('/pages/activity/activity?id=' + item.id)">
				<uni-row class="demo-uni-row">
					<uni-col :span="8">
						<image :src="item.img" style="width: 80px; height: 80px;"></image>
					</uni-col>
					<uni-col :span="16">
						<uni-row class="demo-uni-col dark">{{item.title}}</uni-row>
						<uni-row class="demo-uni-col dark" style="font-size: 10px;">俱乐部：{{item.siteName}} </uni-row>
						<uni-row class="demo-uni-col dark" style="font-size: 10px;">活动时间：{{item.startAt}}
						</uni-row>
					</uni-col>
				</uni-row>
			</uni-card>
		</view>
		<view @click="clickLoadMore()" :style="hasData ? 'display: block' : 'display: none'">
			<uni-load-more :status="status" :content-text="contentText"/>
		</view>
		<view :style="hasData ? 'display: none' : 'display: block'">
			<jiangqie-no-data></jiangqie-no-data>
		</view>
	</view>
</template>

<script>
	import Auth from '@/utils/auth';
	import Util from '@/utils/util';
	import Alert from '@/utils/alert';
	import Rest from '@/utils/rest';
	import Config from '@/utils/config';
	import JiangqieNoData from "@/components/nodata/nodata";

	export default {
		data() {
			return {
				page:1,
				share_thumb: undefined,
				activity_list: [],
				loadMore: 'more',
				loaded: false,
				hasData: false,
				sites: [],
				info: undefined,
				modeIndex: 3,
				styleIndex: -1,
				current: 0,
				mode: 'nav',
				swiperDotIndex: 0,
				searchValue: '',
				extraIcon: {
					color: '#4cd964',
					size: '22',
					type: 'gear-filled'
				},
				status: 'more',
				statusTypes: [{
					value: 'more',
					text: '加载前',
					checked: true
				}, {
					value: 'loading',
					text: '加载中',
					checked: false
				}, {
					value: 'noMore',
					text: '没有更多',
					checked: false
				}],
				contentText: {
					contentdown: '查看更多',
					contentrefresh: '加载中',
					contentnomore: '没有更多'
				}
			};
		},

		components: {
			JiangqieNoData,
		},

		onLoad(options) {
			this.load();
			this.loadAds();
		},

		onShareAppMessage() {
			let params = {
				title: getApp().globalData.appDesc + '_' + getApp().globalData.appName,
				path: 'pages/index/index'
			};

			if (this.share_thumb) {
				params.imageUrl = this.share_thumb;
			}

			return params;
		},

		// #ifdef MP-WEIXIN
		onShareTimeline() {
			return {
				title: getApp().globalData.appName
			};
		},
		// #endif

		methods: {
			clickLoadMore() {
				this.status = 'loading'
				this.load()
			},
			clickLink(link) {
				Util.openLink(link);
			},
		
			loadAds() {
				let that = this
				var url = `${Config.JQ_DOMAIN}/api/wetalk/ads`
				Rest.get(url).then(data => {
					if (data == null || data.length == 0){
						that.info = [{
								url: 'https://wetalk-1259114324.cos.ap-chengdu.myqcloud.com/mp/p0.png',
								content: 'we talk, we grow'
							}
						]
						return;
					}
					that.info = data
				}, err => {
					console.error("加载活动失败，请稍后重试！")
				});
			},

			load() {
				let that = this
				var url = `${Config.JQ_DOMAIN}/api/wetalk/activities/valid?page=${this.page}`
				Rest.get(url).then(data => {
					if (data.length > 0) {
						const newArray = that.activity_list.concat(data);
						that.activity_list = newArray;
						that.hasData = true
						this.page = this.page+1;
						that.status = 'more'
					}else{
						that.hasData = false
						that.status = 'noMore'
					}
					
				}, err => {
					Alert.error("加载活动失败，请稍后重试！")
				});
			},

			change(e) {
				this.current = e.detail.current
			},
			selectStyle(index) {
				this.dotsStyles = this.dotStyle[index]
				this.styleIndex = index
			},
			selectMode(mode, index) {
				this.mode = mode
				this.modeIndex = index
				this.styleIndex = -1
				this.dotsStyles = this.dotStyle[0]
			},
			clickItem(e) {
				this.swiperDotIndex = e
			},
			onBanner(index) {
				console.log(22222, index);
			},
			search(res) {
				if (this.searchValue.trim() == '' || this.searchValue.length < 2) {
					Alert.error("请输入俱乐部或者活动名称")
					return;
				}
				let that = this
				var url = `${Config.JQ_DOMAIN}/api/wetalk/seach`
				Rest.post(url, {
					'search': this.searchValue
				}).then(res => {
					that.sites = res.sites
					that.activity_list = res.activities
					if (res.sites.length == 0) {
						that.hasData = false
					} else {
						that.hasData = true
					}
				}, err => {
					console.log(err)
				});
			},
			input(res) {},
			clear(res) {
				this.page = 1;
			},
			blur(res) {},
			focus(e) {},
			cancel(res) {
				this.sites = []
				this.activity_list = [];
				this.page = 1;
				this.load()
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

	.content {
		padding: 0rpx 0;
	}

	.jiangqie-icon-block {
		padding: 20rpx 30rpx 10rpx;
		background: #FFFFFF;
		border-radius: 12rpx;
		margin-bottom: 30rpx;
	}

	.jiangqie-icon-block .jiangqie-custom-icon {
		margin-bottom: 10rpx;
	}


	.swiper-box {
		height: 200px;
	}

	.swiper-item {
		/* #ifndef APP-NVUE */
		display: flex;
		/* #endif */
		flex-direction: column;
		justify-content: center;
		align-items: center;
		height: 200px;
		color: #fff;
	}

	.swiper-item0 {
		background-color: #cee1fd;
	}

	.swiper-item1 {
		background-color: #b2cef7;
	}

	.swiper-item2 {
		background-color: #cee1fd;
	}

	.image {
		width: 750rpx;
	}

	/* #ifndef APP-NVUE */
	::v-deep .image img {
		-webkit-user-drag: none;
		-khtml-user-drag: none;
		-moz-user-drag: none;
		-o-user-drag: none;
		user-drag: none;
	}

	/* #endif */

	@media screen and (min-width: 500px) {
		.uni-swiper-dot-box {
			width: 400px;
			margin: 0 auto;
			margin-top: 8px;
		}

		.image {
			width: 100%;
		}
	}

	.uni-bg-red {
		background-color: #ff5a5f;
	}

	.uni-bg-green {
		background-color: #09bb07;
	}

	.uni-bg-blue {
		background-color: #007aff;
	}

	.example-body {
		/* #ifndef APP-NVUE */
		display: flex;
		/* #endif */
		flex-direction: row;
		padding: 20rpx;
	}

	.example-body-item {

		flex-direction: row;
		justify-content: center;
		align-items: center;
		margin: 15rpx;
		padding: 15rpx;
		height: 60rpx;
		/* #ifndef APP-NVUE */
		display: flex;
		padding: 0 15rpx;
		/* #endif */
		flex: 1;
		border-color: #e5e5e5;
		border-style: solid;
		border-width: 1px;
		border-radius: 5px;
	}

	.example-body-item-text {
		font-size: 28rpx;
		color: #333;
	}

	.example-body-dots {
		width: 16rpx;
		height: 16rpx;
		border-radius: 50px;
		background-color: #333333;
		margin-left: 10rpx;
	}

	.active {
		border-style: solid;
		border-color: #007aff;
		border-width: 1px;
	}

	.search-result {
		padding-top: 10px;
		padding-bottom: 20px;
		text-align: center;
	}

	.search-result-text {
		text-align: center;
		font-size: 14px;
		color: #666;
	}

	.example-body {
		/* #ifndef APP-NVUE */
		display: block;
		/* #endif */
		padding: 0px;
	}

	.uni-mt-10 {
		margin-top: 10px;
	}
</style>
