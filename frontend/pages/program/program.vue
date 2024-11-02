<template>
	<view class="zhuige-points-goods-img">
		<view :style="hasData == null ? 'display: block' : 'display: none'">
			<jiangqie-no-data></jiangqie-no-data>
		</view>
		<view :style="hasData == null ? 'display: none' : 'display: block'">
			<uni-row class="counting">
				<uni-col :span="12">
					<uni-card title="累计课程" :is-shadow="false">
						<text class="static">{{summary.programCount}}</text>
					</uni-card>
				</uni-col>
				<uni-col :span="12">
					<uni-card title="累计报名" :is-shadow="false">
						<text class="static">{{summary.userCount}}</text>
					</uni-card>
				</uni-col>
			</uni-row>

			<view v-for="(item,index) in programs" :key="index" class="">
				<uni-card is-full :is-shadow="true" class="panel"
					@click="clickLink('/pages/program/detail?id=' + item.id)">
					<uni-row class="demo-uni-row">
						<uni-col :span="8">
							<image :src="item.img" style="width: 80px; height: 80px;"></image>
						</uni-col>
						<uni-col :span="16">
							<uni-row class="demo-uni-col dark">{{item.title}}</uni-row>
							<uni-row class="demo-uni-col dark" style="font-size: 14rpx;">报名截止：{{item.stopAt}}</uni-row>
							<uni-row class="demo-uni-col dark" style="font-size: 14rpx;">开课时间：{{item.startAt}} -
								{{item.endAt}}</uni-row>
						</uni-col>
					</uni-row>
				</uni-card>
			</view>
		</view>
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
				hasData: undefined,
				summary: {
					programCount: 0,
					userCount: 0
				},
				programs: []
			}
		},
		components: {
			JiangqieNoData,
		},

		onLoad(options) {
			this.load();
			this.loadCounting();
		},

		methods: {
			load() {
				let that = this
				var url = `${Config.JQ_DOMAIN}/api/wetalk/programs/valid`
				Rest.get(url).then(data => {
					that.programs = data
				}, err => {
					Alert.error("加载失败，请稍后重试！")
				});
			},
			loadCounting() {
				let that = this
				var url = `${Config.JQ_DOMAIN}/api/wetalk/programs/summary`
				Rest.get(url).then(data => {
					that.hasData = true
					that.summary = data
				}, err => {
					Alert.error("加载失败，请稍后重试！")
				});
			},
			change(e) {
				console.log(e);
			},
			clickLink(link) {
				this.level = link;
				this.$data.hasSite = false;
				console.log(link)
				Util.openLink(link);
			},
		}
	};
</script>

<style lang="scss">
	@import "@/style/style.scss";

	.zhuige-points-goods-img {
		position: relative;
		background-size: 100%;
	}

	.counting .static {
		font: 18px bold;
		color: red;
	}
</style>
