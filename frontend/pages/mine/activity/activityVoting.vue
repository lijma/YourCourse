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
					<view>
						<view class="example">
							<!-- Dynamic form validation -->
							<button type="primary" style="margin-right: 5px;" size="mini"
								:disabled="status"
								@click="publish()">发布</button>
							<button :disabled="!status" type="primary" style="margin-right: 5px;" size="mini" 
								@click="clickLink('/pages/activity/voting?id=' + activityId)">投票页面</button>
							<button :disabled="!status" type="primary" style="margin-right: 5px;" size="mini" @click="fetchResult()">查看投票结果</button>
							<view :style="result != undefined ? 'display: block' : 'display: none'">
								<text class="zhuige-point-goods-header"> 投票结果</text>
								<uni-card v-for="(value,key) in result" 
										:is-shadow="false" 
										:title="key"
										is-full>
									<uni-list>
										<uni-list-item v-for="(score,name) in value" 
											:title="name"  :rightText="score"
										/>
									</uni-list>
								</uni-card>
							</view>
							<view>
								<text class="zhuige-point-goods-header"> 投票操作</text>
							</view>	
							<uni-forms v-for="(vote,index) in votes" 
									:key="index"
									:ref="vote.description" >
								
								<uni-card :title="vote.description">											
									<uni-forms-item>
										<uni-easyinput v-model="vote.description" placeholder="请输入角色" />
									</uni-forms-item>
									<uni-forms-item v-for="(option,id) in vote.options" 
										:key="id">
										<view class="form-item">
											<uni-easyinput v-model="option.option" 
											placeholder="请输入角色" />
											<button class="button" size="mini" type="default"
												@click="delOption(index,id)">删除</button>
										</view>
										
									</uni-forms-item>
									
									<view class="button-group">
										<button size="mini" @click="addOption(index)">新增选项</button>
										<button size="mini" @click="del(index)">删除问题</button>
									</view>
								
								</uni-card>
							
							</uni-forms>
							<button type="primary" style="margin-right: 5px;" size="mini" @click="add">新增</button>
							<button type="primary" style="margin-right: 5px;" size="mini" @click="reset()">重置</button>
							<button type="primary" style="margin-right: 5px;" size="mini" @click="submit()">保存</button>
							
							
						</view>
					</view>	
					

				</view>
				<!-- <image :src="activity.img" style="width: 100%;"></image> -->
			</view>


		</view>


		<uni-popup ref="reset" type="dialog">
			<uni-popup-dialog type="success" cancelText="关闭" confirmText="同意" title="保存确认"
				:content="'确定吗？该操作会导致丢失更改数据'" @confirm="resetData()">
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
				menu:['0','1'],
				activityId: null,
				activity: {
					title: ''
				},
				array: [],
				templates: [],
				vote: undefined,
				index: 0,
				status: 0,
				votes:[],
				votesFake: [
					{
						description: "best speaker",
						options: [
							{
								option: "lisa",
								score: 9
							},
							{
								option: "marvin",
								score: 2
							}
						]
					},
					{
						description: "best evaluator",
						options: [
							{
								option: "jack",
								score: 0
							},
							{
								option: "rose",
								score: 1
							}
						]
					}
				],
				result: undefined
				
			};
		},

		components: {
			JiangqieNoData
		},

		onLoad(options) {
			this.activityId = options.id;
			this.loadActitiy();
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

			reset() {
				this.$refs.reset.open()
			},
			
			resetData() {
				console.log("reload all data")
				this.loadActitiy()
			},
			
			fetchResult(){
				var url = `${Config.JQ_DOMAIN}/api/wetalk/activities/${this.activityId}/votes/results`
				Rest.get(url).then(data => {
					this.result = data
					console.log(data)
				}, err => {
					this.activity = null
					Alert.error("加载活动失败，请稍后重试！")
				});
			},
			
			loadActitiy() {
				var url = `${Config.JQ_DOMAIN}/api/wetalk/activities/${this.activityId}`
				Rest.get(url).then(data => {
					this.activity = data.activity
					if (this.activity.votes != undefined){
						let votes = JSON.parse(this.activity.votes)
						console.log(votes)
						this.status = votes.status ? votes.status: 0,
						this.votes = votes.votes
					}
				}, err => {
					this.activity = null
					Alert.error("加载活动失败，请稍后重试！")
				});
			},

			publish() {
				this.status = 1
				this.submit()
				
			},
			
			submit() {
				//this.checkLogin()
				var url = `${Config.JQ_DOMAIN}/api/wetalk/activities/${this.activityId}/votes`
				var data = {
					activityId: this.activityId,
					votes: this.votes,
					status: this.status 
				}
				Rest.post(url, data).then(res => {
					Alert.success("保存成功")
				}, err => {
					Alert.error("保存失败！")
				});
			},

			add() {
				this.votes.push(
					{
						description: "",
						options: [
							{
								option: "A",
								score: 0
							},
							{
								option: "B",
								score: 0
							},
							{
								option: "C",
								score: 0
							}
						]
					},
				)
			},
			del(id) {
				this.votes.splice(id, 1)
			},
			
			delOption(voteId, optionId) {
				//console.log("optionId"+optionId+";voteId:"+voteId)
				let vote = this.votes[voteId];
				console.log(vote)
				vote.options.splice(optionId, 1)
				
			},
			
			addOption(voteId) {
				console.log("voteId"+voteId)
				let vote = this.votes[voteId];
				vote.options.push(
					{
						option: "",
						score: 0
					}
				)
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
	
	.content {
		padding: 5px;
	}

	.button-group {
		display: flex;
		justify-content: space-around;
		margin-bottom: 5px;
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
