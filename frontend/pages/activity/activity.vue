<template>
	<view class="content">
		<view class="scrollable-content">
			<view :style="activity == null ? 'display: block' : 'display: none'">
				<jiangqie-no-data></jiangqie-no-data>
			</view>
			<view :style="activity == null ? 'display: none' : 'display: block'">
				<view class="zhuige-points-goods-img">
					<image :src="activity.img" style="width: 100%;"></image>
				</view>
				
				<view class="zhuige-point-goods">
					<view class="zhuige-point-goods-title">
						<uni-title style="padding-left: 20rpx; padding: 0px;" type="h3" :title="activity.title" align="center"></uni-title>
						<uni-list>
							<uni-list-item title="俱乐部" showArrow :rightText="activity.siteName" 
								clickable @click="goSite()" />
							<uni-list-item title="活动时间" :rightText="activity.startAt" />
							<uni-list-item title="报名名额还剩" :rightText="''+summary" />
						</uni-list>
					</view>
					
					<view class="zhuige-point-goods-info">
						<view class="zhuige-point-goods-header">活动介绍</view>
						<view class="zhuige-point-goods-cont">
							<rich-text :nodes="activity.detail"></rich-text>
						</view>
						<view class="zhuige-point-goods-header">角色详情</view>
						<view class="zhuige-point-goods-cont">
							<uni-list>
								<uni-list-item v-for="(item,index) in roles" :key="index" :title="item.roleText"
									:note="item.userId != null ? item.userName : '待报名'" showArrow="item.userId == null"
									:rightText="item.userId == null ? '报名' : '修改'" clickable @click="open(item)">
								</uni-list-item>
							</uni-list>
						</view>
					</view>
					
				</view>
			
				<uni-popup ref="popup" class="" :animation="true" background-color="#fff">
					<view style="padding: 10px; ">
						<uni-forms ref="aform" :modelValue="formData" :rules="rules">
							<uni-forms-item label="昵称" name="userName" required>
								<uni-easyinput type="text" v-model="formData.userName" placeholder="昵称" />
							</uni-forms-item>
							<uni-forms-item label="备注" name="email">
								<uni-easyinput type="text" v-model="formData.email" placeholder="备注" />
							</uni-forms-item>
						</uni-forms>
						<button @click="submit">确定报名</button>
					</view>
				</uni-popup>
			
			</view>
		</view>

		<view class="fixed-row">
			<uni-row class="demo-uni-row">
				<uni-col :span="8" :offset="4">
					<view class="icon-with-label"  
						  :style="votes != null && votes.status == 1 ? 'display: block' : 'display: none'"
						  @click="clickLink('/pages/activity/voting?id=' + activityId)">
						<div class="label-container">
							<uni-icons type="star-filled" size="30"></uni-icons>活动投票
						</div>
					</view>		
				</uni-col>
				<uni-col :span="8" :offset="2">
					<view class="icon-with-label"
						  :style="summary != undefined && summary >= 0 ? 'display: block' : 'display: none'"
						  @click="open(null)">
						<div class="label-container">
							<uni-icons type="paperplane" size="30"></uni-icons>报名活动
						</div>
					</view>		
				</uni-col>
			</uni-row>
			
			
		  <!-- 行元素内容 -->
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
				activity: {
					img: undefined,
				},
				summary: '',
				roles: [],
				users: [],
				formData: {
					userName: null,
					email: null,
					roleId: null,
					activityId: null,
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
				votes: undefined
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
				title: this.activity.title,
				path: 'pages/activity/activity?id=' + this.activityId,
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
			shareContent() {
				  uni.share({
			        provider: 'weixin', // 分享平台，比如微信、QQ等
			        type: 0, // 分享类型：0表示图文链接，1表示纯文字
			        title:  this.activity.title,
			        summary:  this.activity.title,
			        href: 'pages/activity/activity?id=' + this.activityId, // 分享链接
			        imageUrl: this.activity.img, // 图片链接（仅在type为0时有效）
			        success() {
			          console.log('分享成功');
			        },
			        fail(err) {
			          console.error('分享失败', err);
			        }
			      });
			},
			clickLink(link) {
				Util.openLink(link);
			},
			
			goSite(){
				var link = '/pages/site/site?id='+this.activity.siteId
				Util.openLink(link);
			},

			checkLogin(event) {
				if (Auth.getToken()) {
					return;
				}
				Util.openLink('/pages/login/login');
			},

			loadActitiy() {
				var url = `${Config.JQ_DOMAIN}/api/wetalk/activities/${this.activityId}`
				Rest.get(url).then(data => {
					this.activity = data.activity
					this.roles = data.roles
					this.users = data.audiences
					if (this.activity == undefined){
						console.log("cannot find activity")
						return
					}
					
					if(this.activity.audiences != null) {
						this.summary = data.activity.audiences - data.audiences.length
					} else {
						this.summary = '不限制人数'
					}
					
					if(this.activity.votes != undefined){
						this.votes = JSON.parse(this.activity.votes)
						console.log(this.votes)
					}
					
				}, err => {
					this.activity = null
					Alert.error("加载活动失败，请稍后重试！")
				});
			},

			open(item) {
				if(item != null){
					// 通过组件定义的ref调用uni-popup方法 ,如果传入参数 ，type 属性将失效 ，仅支持 ['top','left','bottom','right','center']
					this.formData.roleId = item.id
					this.formData.userName = item.userName
					this.formData.email = item.email
				}
				this.formData.activityId = this.activityId;
				this.$refs.popup.open('centor')
			},

			submit() {
				this.checkLogin()
				this.$refs.aform.validate().then(res => {
					var url = `${Config.JQ_DOMAIN}/api/wetalk/activities/join`
					Rest.post(url, this.formData).then(res => {
						Alert.success("报名成功")
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
			}

		}
	}
</script>

<style lang="scss">
	@import "@/style/style.scss";
	
	page 
	.content {
		background: #F5F5F5;
		height: 100vh;
		position: relative;

	}
	
	.scrollable-content {
	  height: calc(100% - 120upx); /* 调整内容区域的高度，留出底部行元素的空间 */
	  overflow-y: auto; /* 添加垂直方向的滚动条 */
	  padding-bottom: 50rpx;
	}
	
	.fixed-row {
		position: sticky;
	    bottom: 0;
	    width: 100%;
	    height: 120upx;
	    background-color: #fff;
	}
	
	.icon-with-label {
	  display: flex;
	  align-items: center;
	  justify-content: center;
	}
	
	.label-container {
	  display: flex;
	  align-items: center;
	}
	
	.label {
	  display: flex;	
	  margin-left: 1px;
	}
	
	.zhuige-points-goods-img {
		position: relative;
	}

	.zhuige-points-goods-img image {
		height: 500rpx;
	}

	.zhuige-point-goods-info {
		padding: 30rpx;
		margin-bottom: 10rpx;
		background: #FFFFFF;
		border-radius: 16rpx;
	}

	.zhuige-point-goods-header {
		font-size: 30rpx;
		font-weight: 600;
		padding-bottom: 15rpx;
	}

	
</style>
