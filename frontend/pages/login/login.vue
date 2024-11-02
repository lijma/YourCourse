<template>
	<view>
		<view class='header'>
			<image src='https://wetalk-1259114324.cos.ap-chengdu.myqcloud.com/mp/p4.png'></image>
		</view>
		<view class='content'>
			<view>申请获取以下权限</view>
			<text>获得你的公开信息(昵称，头像、地区等)</text>
		</view>
		<button class='bottom' type='primary' @click='login()'>
			授权登录
		</button>
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
				background: '',
				logo: '',
				title: '123',
				code: undefined,
			};
		},

		onShow(options) {
			this.clickLogin()
		},

		methods: {
			login() {
				let that = this
				wx.login({
					success(res) {
						if (res.code) {
							that.code = res.code
							wx.getUserInfo({
							  success: function(res) {
							    var userInfo = res.userInfo
							    var nickName = userInfo.nickName
							    var avatarUrl = userInfo.avatarUrl
							    var gender = userInfo.gender //性别 0：未知、1：男、2：女
							    var province = userInfo.province
							    var city = userInfo.city
							    var country = userInfo.country
								
								//发起网络请求
								let params = {
									code: that.code,
									nickName: nickName,
									avatarUrl: avatarUrl
								};
								console.log(params)
								
								var url = `${Config.JQ_DOMAIN}/api/wetalk/login`
								Rest.post(url, params).then(data => {
									Auth.setToken(data);
									Util.openLink('/pages/mine/settings?level=2');
								}, err => {
									Alert.error("登录失败，请稍后重试！")
								});
								
							  }
							})
							
							
						} else {
							Alert.error('登录失败！' + res.errMsg)
						}
					}
				})
			},
			

			getuserinfo(res) {
				let userInfo = res.detail.userInfo;
				this.login(userInfo.nickName, userInfo.avatarUrl);
			},

			clickWalk() {
				Util.navigateBack();
			},
			
			clickLogin() {
				if (Auth.getToken()) {
					Util.navigateBack();
				}
			}

		}
	}
</script>

<style lang="scss">
	@import "@/style/style.scss";

	.header {
		margin: 0rpx 0 90rpx 50rpx;
		border-bottom: 1px solid #ccc;
		text-align: center;
		width: 650rpx;
		height: 300rpx;
		line-height: 450rpx;
	}

	.header image {
		width: 400rpx;
		height: 200rpx;
	}

	.content {
		margin-left: 50rpx;
		margin-bottom: 90rpx;
	}

	.content text {
		display: block;
		color: #9d9d9d;
		margin-top: 40rpx;
	}

	.bottom {
		border-radius: 80rpx;
		margin: 70rpx 50rpx;
		font-size: 35rpx;
	}
</style>
