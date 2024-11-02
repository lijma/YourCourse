<template>
	<view class="content">
		
		<uni-card v-for="(item,index) in ownProgramList" :key="index">
				<uni-row class="demo-uni-row" style="padding-bottom: 10px;">
					<text>{{item.title}}</text>
				</uni-row>
				<uni-row class="demo-uni-row" style="padding-bottom: 10px;">
					<text>{{item.startAt}} 至 {{item.endAt}}</text>
				</uni-row>
				<uni-row class="demo-uni-row">
					<button style="margin-left: 10px;" type="primary" class="mini-btn" size="mini"
						@click="clickLink('/pages/mine/program/ids?id=' + item.id)">
						报名名单
					</button>
					<button style="margin-left: 10px;" type="primary" class="mini-btn" size="mini"
						@click="clickLink('/pages/program/detail?id=' + item.id)">
						课程详情
					</button>
					<button style="margin-left: 10px;" type="primary" class="mini-btn" size="mini"
						@click="deleteConfirm(item)"> 
						删除
					</button>
					
					
				</uni-row>
		</uni-card>
		
		
		<uni-popup ref="delete" type="dialog">
			<uni-popup-dialog type="success" cancelText="关闭" confirmText="同意" title="删除确认" :content="deleteMsg"
				@confirm="deleteProgram">
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
				ownProgramList: [],
				loaded: true,
				programToDelete: {},
				deleteMsg: ''
			};
		},

		components: {
			JiangqieNoData,
		},

		onLoad(options) {
			this.loadprograms()
		},

		methods: {
			
			clickLink(link) {
				console.log(link)
				Util.openLink(link);
			},
			

			loadprograms() {
				var url = `${Config.JQ_DOMAIN}/api/wetalk/programs/owned`
				Rest.get(url).then(data => {
					console.log("own programs size: " + data.length)
					this.ownProgramList = data;
				}, err => {
					console.log(err)
				});
			},

			deleteConfirm(program) {
				this.programToDelete = program
				this.deleteMsg = `确定删除 ${program.title} 吗`
				this.$refs.delete.open()
			},
			deleteProgram() {
				var url = `${Config.JQ_DOMAIN}/api/wetalk/programs/${this.programToDelete.id}`
				Rest.post(url).then(res => {
					Alert.success("删除成功")
					this.loadprograms();
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
