<template>
	<view class="content">
		<view class="uni-padding-wrap uni-common-mt">
			<uni-segmented-control
				:current="current" 
				:values="items" 
				:active-color="'#000000'" 
				@clickItem="onClickItem" />
		</view>
		<uni-collapse>
			<uni-collapse-item v-for="(item,index) in mySites[current]" :key="index" :title="item.site.siteName"
				:open="index == 0">
				<uni-card v-for="(user,index2) in item.siteUsers" :key="index2">
					<uni-row class="demo-uni-row" style="padding-bottom: 10px;">
						<uni-col :span="12">
							<text>{{user.userName == null ? user.userId : user.userName}}</text>
						</uni-col>
					</uni-row>
					<uni-row :style="current == 1 ? 'display: block' : 'display: none'" class="demo-uni-row">
						<button class="mini-btn" size="mini" @click="setRunner(user)">
							设为管理员
						</button>
						<button style="margin-left: 20px;" class="mini-btn" size="mini"
							@click="inputDialogToggle(user)">
							从俱乐部移除
						</button>
					</uni-row>
				</uni-card>

			</uni-collapse-item>
		</uni-collapse>
		<view>
			<uni-popup ref="delete" type="dialog">
				<uni-popup-dialog type="success" cancelText="关闭" confirmText="同意" title="通知" :content="deleteMsg"
					@confirm="deleteUser" @close="dialogClose">
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

	export default {
		data() {
			return {
				formData: {
					siteName: '',
					email: '',
					city: ''
				},
				mySites: [],
				userToDelete: {},
				deleteMsg: '',
				current: 0,
				items: ['我参与的', '我管理的']
			};
		},

		components: {
			JiangqieNoData,
		},

		onLoad(options) {
			this.load()
		},

		methods: {
			onClickItem(e) {
				if (this.current !== e.currentIndex) {
					this.current = e.currentIndex
				}
			},
			
			submit() {
				var url = `${Config.JQ_DOMAIN}/api/wetalk/sites`
				Rest.post(url, this.formData).then(res => {
					this.$refs.message.open()
				}, err => {
					console.log(err)
				});
			},

			load() {
				var url = `${Config.JQ_DOMAIN}/api/wetalk/sites/mine`
				Rest.get(url).then(data => {
					console.log("sites size: " + data.length)
					this.mySites = data;
				}, err => {
					console.log(err)
				});
			},
			setRunner(user) {
				var url = `${Config.JQ_DOMAIN}/api/wetalk/sites/asRunner`
				console.log(user.siteId)
				var request = {
					siteId: user.siteId,
					userId: user.userId
				}
				console.log(user)
				Rest.post(url, request).then(data => {
					Alert.success("设置成功")
				}, err => {
					Alert.error(err)
				});
			},
			inputDialogToggle(user) {
				this.userToDelete = user
				this.deleteMsg = `确定删除 ${user.userName} 吗`
				this.$refs.delete.open()
			},
			dialogClose() {
				console.log('点击关闭')
				this.userToDelete = {}
			},
			deleteUser() {
				var url = `${Config.JQ_DOMAIN}/api/wetalk/sites/deleteUser`
				var request = {
					siteId: this.userToDelete.siteId,
					userId: this.userToDelete.userId
				}
				Rest.post(url, request).then(data => {
					Alert.success("成功删除用户")
				}, err => {
					Alert.error(err)
				});
			},
		}

	}
</script>

<style lang="scss">
	@import "@/style/style.scss";

	.slot-box {
		/* #ifndef APP-NVUE */
		display: flex;
		/* #endif */
		flex-direction: row;
		align-items: center;
	}

	.slot-image {
		/* #ifndef APP-NVUE */
		display: block;
		/* #endif */
		margin-right: 10px;
		width: 30px;
		height: 30px;
	}

	.slot-text {
		flex: 1;
		font-size: 14px;
		color: #4cd964;
		margin-right: 10px;
	}
</style>
