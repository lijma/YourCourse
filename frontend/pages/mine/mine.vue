<template>
	<view class="content">
		<!-- 用户头像及信息 -->
		
		<view class="status" ></view>
		<view class="header" >
			<view class="addr"></view>
			<view class="input-box">
			</view>
			<view class="icon-btn">
				<view class="icon tongzhi" @tap="toMsg">
				</view>
				<view class="icon setting" @tap="toSetting">
					
				</view>
			</view>
		</view>
		<!-- 占位 -->
		<view class="place"></view>
		<!-- 用户信息 -->
		<view class="user">
			<!-- 头像 -->
			<view class="">
				<view :style="logged == true ? 'display: none' : 'display: block'" @click="clickLogin">
					<uni-icons type="contact" size="80"></uni-icons>
				</view>
				<view :style="logged != true ? 'display: none' : 'display: block'">
					<image :src="user.icon" style="width: 80px; height: 80px; margin-right: 10rpx;"></image>
				</view>
			</view>
			<!-- 昵称,个性签名 -->
			<view class="right">
				<view :style="logged == true ? 'display: none' : 'display: block'" @click="clickLogin">
					<view class="username">注册/登录</view>
				</view>
				<view :style="logged == true ? 'display: block' : 'display: none'">
					<view class="username" @tap="toLogin">{{user.userName}}</view>
					<view class="signature" @tap="toSetting">{{user.signature}}</view>
				</view>
			</view>
		</view>
		<!-- VIP banner -->
		<view class="VIP" style="margin-top: 10px;">
			<view class="title">认证俱乐部即可创建活动！</view>
			<view class="tis">费用全免!!!</view>
		</view>

		<view>
			<uni-section title="我的" type="line" padding>
			</uni-section>
			<uni-grid :column="4" :show-border="false" :square="false" @change="go">
				<uni-grid-item v-for="(item, index) in mine" :index="index" :key="index">
					<view class="grid-item-box">
						<uni-icons :type="item.icon" size="30"></uni-icons>
						<text class="text">{{item.title}}</text>
					</view>
				</uni-grid-item>
			</uni-grid>

			<uni-section title="更多" type="line" padding>
			</uni-section>
			<uni-grid :column="4" :show-border="false" :square="false" @change="go2">
				<uni-grid-item :style="club_apply_enabled ? 'display: block' : 'display: none'">
					<view class="grid-item-box">
						<uni-icons type="pyq" size="30"></uni-icons>
						<text class="text">俱乐部认证</text>
					</view>
				</uni-grid-item>
				<uni-grid-item :style="teacher_apply_enabled ? 'display: block' : 'display: none'">
					<view class="grid-item-box">
						<uni-icons type="vip-filled" size="30"></uni-icons>
						<text class="text">讲师认证</text>
					</view>
				</uni-grid-item>				
				<uni-grid-item v-for="(item, index) in more" :index="index" :key="index"
					:style="item.show ? 'display: block' : 'display: none'">
					<view class="grid-item-box">
						<uni-icons :type="item.icon" size="30"></uni-icons>
						<text class="text">{{item.title}}</text>
					</view>
				</uni-grid-item>
				
			</uni-grid>
		</view>

		<!-- 用户菜单 -->
		<view class="zhuige-user-menu">

			<!-- 列表块 -->
			<button open-type="contact" class="zhuige-list-block" @click="clickLink('/pages/mine/activity/myactivity')">
				<view class="zhuige-base-list">
					<view class="zhuige-list-img">
						<uni-icons type="chatboxes" size="25"></uni-icons>
					</view>
					<view class="zhuige-list-info">
						<view class="zhuige-list-title">联系我们</view>
					</view>
				</view>
				<view class="zhugie-list-link">
					<uni-icons type="arrowright" size="14"></uni-icons>
				</view>
			</button>

			<view class="zhuige-list-block" @click="clickClear">
				<view class="zhuige-base-list">
					<view class="zhuige-list-img">
						<uni-icons type="mail-open" size="25"></uni-icons>
					</view>
					<view class="zhuige-list-info">
						<view class="zhuige-list-title">退出账户</view>
					</view>
				</view>
				<view class="zhugie-list-link">
					<uni-icons type="arrowright" size="14"></uni-icons>
				</view>
			</view>
			
		</view>
		<view style="padding-top: 50rpx; height: 50rpx;">
			
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
	import Oss from '@/utils/oss.js';
	import Toggle from '@/utils/toggle.js'
	export default {
		data() {
			return {
				logged: false,
				user: {},
				background: undefined,
				page_about: undefined,
				teacher_apply_enabled: true,
				club_apply_enabled: true,
				mine: [{
						icon: 'staff',
						url: '/pages/mine/site/mysite',
						title: '俱乐部'
					},
					{
						icon: 'navigate',
						url: '/pages/mine/activity/joined',
						title: '已报名活动'
					},
					{
						icon: 'map',
						url: '/pages/mine/program/joined',
						title: '已报名课程'
					},
					{
						icon: 'star-filled',
						url: '/pages/mine/favor/myfavor',
						title: '我的收藏'
					},
					{
						icon: 'compose',
						url: '/pages/mine/activity/activityCreate',
						title: '创建活动'
					},
					{
						icon: 'navigate-filled',
						url: '/pages/mine/activity/activity',
						title: '活动管理'
					},
					{
						icon: 'compose',
						url: '/pages/mine/program/create',
						title: '创建课程'
					},
					{
						icon: 'map-filled',
						url: '/pages/mine/program/myprogram',
						title: '课程管理'
					},
				],
				more: [
					{
						icon: 'pyq',
						url: '/pages/mine/site/siteCreate',
						title: '俱乐部认证',
						type: 'club_apply_enabled',
						show: false
					},
					{
						icon: 'vip-filled',
						url: '/pages/mine/teacher/apply',
						title: '讲师认证',
						type: 'teacher_apply_enabled'
					},
					{
						icon: 'settings',
						url: '/pages/mine/settings',
						title: '账户设置',
						show: true
					},
				]
			};
		},

		onLoad() {
			this.fetchToggle();
			this.checkLogin()
			this.loadUser()
			Oss.loadConfig()
		},

		onShow(options) {
			this.fetchToggle();
			this.checkLogin()
			this.loadUser()
		},

		methods: {
			fetchToggle: async function(){
				await Toggle.loadToggle().then(data => {
					this.teacher_apply_enabled = data.teacher_apply_enabled
					this.club_apply_enabled = data.club_apply_enabled
				});
			},
			clickLogin() {
				if (Auth.getToken()) {
					this.loadUser()
					return;
				}
				Util.openLink('/pages/login/login');
			},
			
			loadUser() {
				var url = `${Config.JQ_DOMAIN}/api/wetalk/profile`
				Rest.get(url).then(data => {
					console.log("user: " + data)
					this.user = data;
				}, err => {
					console.log(err)
				});
			},

			checkLogin() {
				if (Auth.getToken()) {
					this.logged = true;
					this.loadUser()
				} else {
					this.logged = false
				}
				return
			},

			clickLink(link) {
				console.log(link)
				Util.openLink(link);
			},
			
			notice(msg) {
				util.toast(msg)
			},

			go(event) {
				let menu = this.mine[event.detail.index];
				this.clickLink(menu.url);
			},

			go1(event) {
				let menu = this.manage[event.detail.index];
				this.clickLink(menu.url);
			},

			go2(event) {
				let menu = this.more[event.detail.index];
				this.clickLink(menu.url);
			},

			clickClear() {
				uni.showModal({
					title: '提示',
					content: '清除缓存 需要重新登录',
					success(res) {
						if (res.confirm) {
							uni.clearStorageSync();
							uni.showToast({
								title: '清除完毕'
							});

							uni.reLaunch({
								url: '/pages/index/index'
							});
						}
					}
				});
			}
		}
	}
</script>

<style lang="scss">
	@import "@/style/style.scss";

	body,
	uni-page-body {
		height: 100%;
	}

	.grid-item-box {
		flex: 1;
		// position: relative;
		/* #ifndef APP-NVUE */
		display: flex;
		/* #endif */
		flex-direction: column;
		align-items: center;
		justify-content: center;
		padding: 15px 0;
	}


	.zhuige-list-block {
		padding: 30rpx 0;
		border-bottom: 1px solid #EEEEEE;
		display: flex;
		align-items: center;
		justify-content: space-between;
	}

	.zhuige-list-block:last-of-type {
		border: none;
	}

	.zhuige-base-list {
		display: flex;
		align-items: center;
	}

	

	.zhuige-user-info-content {
		float: right;
		background-color: white;
		width: 150px;
		padding-left: 70px;
		color: black;
		border-radius: 5px;
		font-size: 30rpx;
		text-align: left;
	}

	.zhuige-user-info image {
		height: 160rpx;
		width: 160rpx;
		border-radius: 50%;
		margin-bottom: 20rpx;
	}

	.zhuige-copyright {
		text-align: center;
		padding: 40rpx;
	}

	.zhuige-user-menu {
		width: 100%;
		background: #FFFFFF;
	}

	.zhuige-user-menu .zhuige-list-block {
		padding: 0 30rpx;
		height: 4rem;
		line-height: 4rem;
	}

	.zhuige-list-block:last-of-type {
		border-bottom: 1px solid #EEEEEE;
	}

	.zhuige-user-menu .zhuige-list-img image {
		height: 40rpx;
		width: 40rpx;
		vertical-align: text-bottom;
		margin-right: 20rpx;
	}

	.zhuige-user-menu .zhuige-list-title {
		font-weight: 400;
		font-size: 28rpx;
	}

	.zhuige-user-menu button {
		border: none;
		background: #FFFFFF;
		border-bottom: 1px solid #EEEEEE;
		height: 4rem;
		line-height: 4rem;
	}

	.zhuige-user-menu button:after {
		border: none;
	}

	.zhuige-copyright image {
		height: 120rpx;
		width: 120rpx;
		border-radius: 50%;
		margin-bottom: 20rpx;
	}

	.zhuige-copyright view {
		font-size: 24rpx;
		color: #999999;
		font-weight: 200;
	}

	.zhuige-user-menu button {
		height: 4rem;
		line-height: 4rem;
	}
	
	.status {
		width: 100%;
		height: 0;
		position: fixed;
		z-index: 10;
		background-color: #f06c7a;
		top: 0;
		/*  #ifdef  APP-PLUS  */
		height: var(--status-bar-height);//覆盖样式
		/*  #endif  */
		
	}
	
	.header{
		width: 92%;
		padding: 0 4%;
		height: 30upx;
		display: flex;
		justify-content: flex-end;
		align-items: center;
		position: fixed;
		top: 0;
		z-index: 10;
		/*  #ifdef  APP-PLUS  */
		top: var(--status-bar-height);
		/*  #endif  */
		.icon-btn{
			width: 120upx;
			height: 60upx;
			flex-shrink: 0;
			display: flex;
			.icon{
				width: 60upx;
				height: 60upx;
				display: flex;
				justify-content: flex-end;
				align-items: center;
				font-size: 42upx;
			}
		}
	}
	.place-bottom{
		height: 200upx;
	}
	.user{
		width: 92%;
		padding: 0 4%;
		display: flex;
		align-items: center;
		// position: relative;
		.left{
			width: 20vw;
			height: 35vw;
			flex-shrink: 0;
			margin-right: 20upx;
			border: solid 1upx #fff;
			border-radius: 100%;
			image{
				width: 20vw;
				height: 20vw;
				border-radius: 100%;
			}
			
		}
		.right{
			width: 100%;
			.username{
				font-size: 36upx;
			}
			.signature{
				font-size: 28upx;
			}
		}
		.erweima{
			flex-shrink: 0;
			width: 10vw;
			height: 10vw;
			margin-left: 5vw;
			border-radius: 100%;
		
			display: flex;
			justify-content: center;
			align-items: center;
			background: linear-gradient(to left, #fbbb37 0%,#fcf0d0 105%);
			.icon{
				color: #7b6335;
				font-size: 42upx;
			}
		}
	}
	.order{
		width: 84%;
		margin: 30upx 4% 30upx 4%;
		padding: 30upx 4% 20upx 4%;
		box-shadow: 0upx 0upx 25upx rgba(0,0,0,0.1);
		border-radius: 15upx;
		.list{
			display: flex;
			border-bottom: solid 1upx #17e6a1;
			padding-bottom: 10upx;
			.box{
				width: 20%;
				.img{
					width: 100%;
					display: flex;
					justify-content: center;
					.icon{
						font-size: 50upx;
						color: #464646;
					}
				}
				.text{
					width: 100%;
					display: flex;
					justify-content: center;
					font-size: 28upx;
				}
			}
		}
		.balance-info{
			display: flex;
			padding: 10upx 0;
			.left{
				width: 75%;
				display: flex;
				.box{
					width: 50%;
					font-size: 28upx;
					
					.num{
						width: 100%;
						height: 50upx;
						display: flex;
						justify-content: center;
						align-items: flex-end;
						color: #f9a453;
					}
					.text{
						width: 100%;
						display: flex;
						justify-content: center;
						color: #3d3d3d;
						font-size: 28upx;
					}
				}
			}
			.right{
				border-left: solid 1upx #17e6a1;
				width: 25%;
				.box{
					
					.img{
						width: 100%;
						height: 50upx;
						display: flex;
						justify-content: center;
						align-items: flex-end;
						.icon{
							font-size: 45upx;
							color: #e78901;
						}
					}
					.text{
						width: 100%;
						display: flex;
						justify-content: center;
						font-size: 28upx;
						color: #3d3d3d;
					}
				}
			}
		}
	}
	.VIP{
		width: 84%;
		margin: -65upx auto 20upx auto;
		padding: 30upx 3%;
		background: linear-gradient(to left, #dea96d 0%,#f6d59b 100%);
		box-shadow: 0upx 0upx 25upx rgba(0,0,0,0.2);
		border-radius: 15upx;
		display: flex;
		align-items: center;
		.img{
			flex-shrink: 0;
			width: 10upx;
			height: 10upx;
			image{
				width: 40upx;
				height: 60upx;
			}
		}
		.title{
			width: 100%;
			color: #796335;
			font-size: 28upx;
		}
		.tis{
			width: 80%;
			display: flex;
			justify-content: flex-end;
			color: #fcf0d0;
			font-size: 26upx;
		}
	}
	.toolbar{
		width: 92%;
		margin: 0 4% 0 4%;
		padding: 0 0 20upx 0;
		background-color: #fff;
		box-shadow: 0upx 0upx 25upx rgba(0,0,0,0.1);
		border-radius: 15upx;
		.title{
			padding-top: 10upx;
			margin: 0 0 10upx 3%;
			font-size: 30upx;
			height: 80upx;
			display: flex;
			align-items: center;
		}
		.list{
			display: flex;
			flex-wrap: wrap;
			.box{
				width: 25%;
				margin-bottom: 30upx;
				.img{
					width: 23vw;
					height: 10.5vw;
					display: flex;
					justify-content: center;
					
					image{
						width: 9vw;
						height: 9vw;
					}
				}
				.text{
					width: 100%;
					display: flex;
					justify-content: center;
					font-size: 26upx;
					color: #3d3d3d;
				}
			}
		}
	}
	
</style>
