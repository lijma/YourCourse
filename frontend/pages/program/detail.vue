<template>
	<view class="content uni-container">
		<view :style="program == null ? 'display: block' : 'display: none'">
			<jiangqie-no-data></jiangqie-no-data>
		</view>
		<view :style="program == null ? 'display: none' : 'display: block'">
			<view class="zhuige-points-goods-img">
				<image :src="program.img" style="width: 100%;"></image>
			</view>
			<view class="zhuige-point-goods">
				<view class="zhuige-point-goods-title">
					<view>
						{{program.title}}
					</view>
					<view style="font-size: 32rpx; color: red; padding-left: 5px;">
						课程价格： {{program.price == undefined ? '￥0.00': '￥'+program.price}}
					</view>
					<view style="color: gray;">
						报名截止时间： {{program.stopAt}}
					</view>
					<view style="color: gray;">
						开课时间： {{program.startAt}} - {{program.endAt}}
					</view>
					<view style="margin-top: 10px; ">
						<uni-tag v-for="(label,index) in program.labels" :key="label.id" style="margin-right: 5px;"
							:inverted="true" :text="label" type="warning" />
					</view>
					<view style="font-size: 32rpx; font:bold; padding-left: 5px;">
						授课老师
					</view>

					<view>
						<uni-card :is-shadow="false" is-full style="padding-top: 0rpx;" @click="goTeacher()">
							<image class="logo" :src="teacher.logo" style="width: 120rpx; height: 120rpx;" mode="aspectFill">
							<view style="padding-top: 30rpx;">
								<uni-tag :text="'认证讲师'" type="warning" style="margin-right: 30px;" />
								<br/>
								<text class="" style="margin-right: 10rpx;">
									{{teacher.teacherName}}
								</text>
							</view>
						</uni-card>
					</view>


					<view>
						
					</view>
					<view style="font-size: 32rpx; font:bold; padding-left: 5px;">
						课程详情
					</view>
					<view style="padding-left: 10px;">
						<image :src="program.detail" style="width: 100%;"></image>

					</view>
				</view>
				<view>
					<button style="width: 100%; font-size: 16px;" type="primary" class="btn"
						@click="open(null)">报名课程</button>
				</view>
			</view>

			<uni-popup ref="popup" class="" :animation="true" background-color="#fff">
				<view style="padding: 10px; ">
					<uni-forms ref="aform" :modelValue="formData" :rules="rules">
						<uni-forms-item label="姓名" name="userName" required>
							<uni-easyinput type="text" v-model="formData.userName" placeholder="请输入姓名" />
						</uni-forms-item>
						<uni-forms-item label="联系方式" name="email">
							<uni-easyinput type="text" v-model="formData.email" placeholder="请输入联系方式" />
						</uni-forms-item>
					</uni-forms>
					<button @click="submit">确定报名</button>
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
				programId: null,
				program: {
					img: undefined,
				},
				teacher: {
					logo: '',
				},
				roles: [],
				users: undefined,
				name: {},
				formData: {
					userName: null,
					email: null,
					programId: null,
				},
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
			//this.programId = '1614167289624727553';
			this.programId = options.id;
			this.loadActitiy();
			this.loadTeacher();
		},

		onShareAppMessage() {
			return {
				title: this.program.title + "",
				path: 'pages/program/program?id=' + this.programId
			};
		},

		// #ifdef MP-WEIXIN
		onShareTimeline() {
			return {
				title: this.program.title,
				imageUrl: this.program.img
			};
		},
		// #endif

		methods: {
			clickLink(link) {
				Util.openLink(link);
			},

			goTeacher() {
				var link = '/pages/teacher/teacher?id=' + this.teacher.id
				Util.openLink(link);
			},

			checkLogin(event) {
				if (Auth.getToken()) {
					return;
				}
				Util.openLink('/pages/login/login');
				event.stopPropagation();
			},

			loadTeacher() {
				var url = `${Config.JQ_DOMAIN}/api/wetalk/programs/${this.programId}/teacher`
				Rest.get(url).then(data => {
					this.teacher = data
				}, err => {
					this.teacher = null
					Alert.error("加载失败，请稍后重试！")
				});
			},

			loadActitiy() {
				var url = `${Config.JQ_DOMAIN}/api/wetalk/programs/${this.programId}`
				Rest.get(url).then(data => {
					this.program = data
				}, err => {
					this.program = null
					Alert.error("加载失败，请稍后重试！")
				});
			},

			open(item) {
				if (item != null) {
					// 通过组件定义的ref调用uni-popup方法 ,如果传入参数 ，type 属性将失效 ，仅支持 ['top','left','bottom','right','center']
					this.formData.userName = item.userName
					this.formData.email = item.email
				}
				this.formData.programId = this.programId;
				this.$refs.popup.open('centor')
			},

			submit() {
				this.checkLogin()
				this.$refs.aform.validate().then(res => {
					var url = `${Config.JQ_DOMAIN}/api/wetalk/programs/join`
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

	page,
	.content {
		background: #F5F5F5;
	}

	.zhuige-points-goods-img {
		position: relative;

	}

	.logo {
		height: 150rpx;
		width: 150rpx;
		background-color: red;
		float: left;
		margin-left: 0rpx;
		margin-right: 20rpx;
		margin-bottom: 20rpx;
		border-radius: 50%;
	}

	.zhuige-points-goods-img,
	.zhuige-points-goods-img swiper,
	.zhuige-points-goods-img swiper-item,
	.zhuige-points-goods-img image {
		height: 500rpx;
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
		font-size: 42rpx;
	}

	.zhuige-point-goods-title view {
		padding: 0rpx 30rpx 0rpx 5px;
		margin-bottom: 8px;
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
