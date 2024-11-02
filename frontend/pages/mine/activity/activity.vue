<template>
	<view class="content">
		
		<uni-card v-for="(item,index) in ownActivityList" :key="index">
				<uni-row class="demo-uni-row" style="padding-bottom: 10px;">
					<text>[{{item.siteName}}] - {{item.startAt}}</text>
				</uni-row>
				<uni-row class="demo-uni-row" style="margin-bottom: 10px;">
					<text>{{item.title}}</text>
				</uni-row>
				<uni-row class="demo-uni-row">
					<uni-tag style="margin-right: 20rpx; margin-bottom: 5px;" type="primary" text="修改"
						@click="clickLink('/pages/mine/activity/activityCreate?id=' + item.id)" />
						
					<uni-tag style="margin-right: 20rpx; margin-bottom: 5px;" type="primary" text="角色" 
						@click="clickLink('/pages/mine/activity/activityRole?id=' + item.id)" />

					<uni-tag style="margin-right: 20rpx; margin-bottom: 5px;" type="primary" text="名单"
						@click="clickLink('/pages/mine/activity/activityDetail?id=' + item.id)" />

					<uni-tag style="margin-right: 20rpx; margin-bottom: 5px;" type="primary" text="投票"
						@click="clickLink('/pages/mine/activity/activityVoting?id=' + item.id)" />

					<uni-tag style="margin-right: 20rpx; margin-bottom: 5px;" type="primary" text="详情"
						@click="clickLink('/pages/activity/activity?id=' + item.id)" />
						
					<uni-tag style="margin-right: 20rpx; margin-bottom: 5px;" type="primary" text="删除"
						@click="deleteConfirm(item)" />
				</uni-row>
		</uni-card>
		
		
		<uni-popup ref="delete" type="dialog">
			<uni-popup-dialog type="success" cancelText="关闭" confirmText="同意" title="通知" :content="deleteMsg"
				@confirm="deleteActivity">
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

	export default {
		data() {
			return {
				img: undefined,
				siteMap: new Map(),
				siteList: [],
				ownActivityList: [],
				joinedActivityList: [],
				loaded: true,
				activityToDelete: {},
				deleteMsg: '',
			};
		},

		components: {
			JiangqieNoData,
		},

		onLoad(options) {
			this.loadActivities()
		},

		methods: {
			
			clickLink(link) {
				console.log(link)
				Util.openLink(link);
			},
			

			loadActivities() {
				var url = `${Config.JQ_DOMAIN}/api/wetalk/activities/owned`
				Rest.get(url).then(data => {
					console.log("own activities size: " + data.length)
					this.ownActivityList = data;
				}, err => {
					console.log(err)
				});
			},

			deleteConfirm(activity) {
				this.activityToDelete = activity
				this.deleteMsg = `确定删除 ${activity.title} 吗`
				this.$refs.delete.open()
			},
			deleteActivity() {
				var url = `${Config.JQ_DOMAIN}/api/wetalk/activities/${this.activityToDelete.id}`
				Rest.post(url).then(res => {
					Alert.success("删除成功")
					this.loadActivities();
					this.loadJoinedActivities()
				}, err => {
					console.log(err)
				});
			}
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
