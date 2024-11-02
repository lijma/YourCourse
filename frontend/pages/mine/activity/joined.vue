<template>
	<view class="content">

		<uni-card v-for="(item,index) in joinedActivityList" :key="index">
			<uni-row class="demo-uni-row" style="padding-bottom: 10px;">
				<text>[{{item.siteName}}] - {{item.startAt}}</text>
			</uni-row>
			<uni-row class="demo-uni-row" style="padding-bottom: 10px;">
				<text>{{item.title}}</text>
			</uni-row>
			<uni-row class="demo-uni-row">
				<button class="mini-btn" size="mini" @click="clickLink('/pages/activity/activity?id=' + item.id)">
					查看
				</button>
			</uni-row>
		</uni-card>

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
				siteMap: new Map(),
				siteList: [],
				ownActivityList: [],
				joinedActivityList: [],
				loaded: true,
				activityToDelete: {},
				deleteMsg: ''
			};
		},

		components: {
			JiangqieNoData,
		},

		onLoad(options) {
			this.loadJoinedActivities()
		},

		methods: {

			clickLink(link) {
				Util.openLink(link);
			},

			loadJoinedActivities() {
				var url = `${Config.JQ_DOMAIN}/api/wetalk/activities/joined`
				Rest.get(url).then(data => {
					console.log("joined activities size: " + data.length)
					this.joinedActivityList = data;
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
