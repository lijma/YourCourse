<template>
	<view class="content">

		<uni-group v-for="(favorList,key) in favorMap" :key="key" :title="getTitle(key)" mode="card">
			<view v-for="(favor,index) in favorList" :key="index">
				<uni-card :is-shadow="false" is-full @click="visit(favor)">
					<text class="uni-h6">{{favor.refTitle}}</text>
				</uni-card>
			</view>
		</uni-group>

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
				favorMap: [],
				loaded: true,
			};
		},

		components: {
			JiangqieNoData,
		},

		onLoad(options) {
			this.loadFavors()
		},

		methods: {
			clickLink(link) {
				Util.openLink(link);
			},
			visit(favor){
			  console.log(JSON.stringify(favor));	
			  if (favor.favorType == 'SITE'){
				  this.clickLink('/pages/site/site?id='+favor.refId)
			  }
			  else if (favor.favorType == 'TEACHER'){
			  	  this.clickLink('/pages/teacher/teacher?id='+favor.refId)
			  }
			  
			},
			getTitle(key) {
				if (key == 'SITE') {
					return '俱乐部'
				} else if (key == 'TEACHER') {
					return '讲师'
				} else {
					return '其他'
				}
			},
			loadFavors() {
				var url = `${Config.JQ_DOMAIN}/api/wetalk/favors/myfavor`
				Rest.get(url).then(data => {
					console.log("joined activities size: " + data.length)
					this.favorMap = data.favors;
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
