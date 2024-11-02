<template>
	<view class="content">
		<view :style="activity == null ? 'display: block' : 'display: none'">
			<jiangqie-no-data></jiangqie-no-data>
		</view>
		<view :style="activity == null ? 'display: none' : 'display: block'">
			<view class="zhuige-points-goods-img">
				<image :src="activity.img" style="width: 100%;"></image>
			</view>
			<view class="zhuige-point-goods">
				<view class="zhuige-point-goods-info"
					:style="votes == null || votes.status != 1 ? 'display: none' : 'display: block'" >
					<view class="zhuige-point-goods-header">{{activity.title}}</view>
					<view class="zhuige-point-goods-cont">
						<uni-forms ref="voteform" :modelValue="formData">
							<uni-card :title="vote.description" 
							v-for="(vote,index) in votes.votes">
							
								<uni-data-checkbox 
									v-model="formData.votes[index]" 
									:localdata="getOptions(vote.options)" />
					
							</uni-card>
							
							<button
								:disabled="submitted"
								style="width: 100%; font-size: 16px;" type="primary" class="btn"
								@click="open(null)">提交</button>							
						</uni-forms>
						
						</uni-list>
					</view>
				</view>
				
			</view>

			<uni-popup ref="popup" class="" :animation="true" background-color="#fff">
				<uni-popup-dialog type="success" cancelText="关闭" 
					confirmText="确认" title="投票确认"
					:content="'同一用户只能投一次，其他投票无效'"
					 @confirm="submit()">
				</uni-popup-dialog>
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
				submitted: false,
				activityId: null,
				activity: {
					img: undefined,
				},
				formData: {
					activityId: null,
					votes: []
				},
				votes: {}
			};
		},

		components: {
			JiangqieNoData
		},

		onLoad(options) {
			this.activityId = options.id;
			this.loadActitiy();
			this.checkLogin()
		},

		onShareAppMessage() {
			return {
				title: "投票-"+this.activity.title,
				path: 'pages/activity/voting?id=' + this.activityId,
				imageUrl: this.activity.img
			};
		},

		// #ifdef MP-WEIXIN
		onShareTimeline() {
			return {
				title: this.activity.title,
				imageUrl: this.activity.img
			};
		},
		// #endif

		methods: {	
			getOptions(options){
				var optionMap = options.map(function(option) {
					  return {
						value: option.option,
						text: option.option
						}
					});
				return optionMap;
			},
			clickLink(link) {
				Util.openLink(link);
			},
			
			goSite(){
				var link = '/pages/site/site?id='+this.activity.siteId
				Util.openLink(link);
			},

			checkLogin(event) {
				console.log(Auth.getToken())
				if (Auth.getToken()) {
					return;
				}
				Util.openLink('/pages/login/login');
			},

			loadActitiy() {
				var url = `${Config.JQ_DOMAIN}/api/wetalk/activities/${this.activityId}`
				Rest.get(url).then(data => {
					this.activity = data.activity
					if(this.activity.votes != undefined){
						this.votes = JSON.parse(this.activity.votes)
					}
					
				}, err => {
					this.activity = null
					Alert.error("加载活动失败，请稍后重试！")
				});
			},

			open(item) {
				this.$refs.popup.open('centor')
			},

			submit() {
				//this.checkLogin()
				this.formData.activityId = this.activityId
				let voteData = this.votes
				var records = this.formData.votes.map(function(item, index){
					var vote = voteData.votes[index];
					return {
						question: vote.description,
						answer: item
					}
				});
				console.log(records)
				
				var url = `${Config.JQ_DOMAIN}/api/wetalk/activities/${this.activityId}/vote`
				Rest.post(url, {
					votes: records
				}).then(res => {
					Alert.success("投票成功")
					this.$refs.popup.close()
					this.submitted = true
				}, err => {
					Alert.error("报名失败")
				});
			},
			inputDialogToggle() {
				this.$refs.inputDialog.open()
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
	}

	.zhuige-point-goods-title {
		background: #FFFFFF;
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
	}

	.zhuige-point-goods-title view:nth-child(2) {
		padding: 20rpx 30rpx 30rpx;
		display: flex;
		align-items: center;
	}

	.zhuige-point-goods-title view:nth-child(2) text:nth-child(1) {
		font-size: 20rpx;
		font-weight: 600;
		padding-right: 20rpx;
	}

	.zhuige-point-goods-info {
		padding: 30rpx;
		margin-bottom: 30rpx;
		background: #FFFFFF;
		border-radius: 16rpx;
	}

	.zhuige-point-goods-header {
		font-size: 36rpx;
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
