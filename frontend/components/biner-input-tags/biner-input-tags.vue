<template>
	<view class="box-border">
		<view class="u-tag clearfixed">
			<block v-for="(item,index) in selectlist" :key='index'>
				<view class="tag-label" @click="del(index)">
					{{item.title}}
				</view>
			</block>
		</view>
		<view>
			<input class="input-tag" v-model="inputzy" type="text" :placeholder="placeholder" @confirm="confirm">
		</view>
	</view>
</template>
<script>
	export default {
		data() {
			return {
				inputzy: ""
			}
		},
		props:{
			selectlist:{
				type: Array,
				default:function(){
					return []
				}
			},
			placeholder:{
				type: String,
				default:''
			}
		},
		watch: {
			inputzy(news, olds) {
				var key = news.trim();
				var title = key;
				if (news[0] == ' ' || news[0] == ',' || news[0] == '，') {
					var title = news.split(news[0])[1];
					this.inputzy = title;
					return;
				}
				if (news.length != '1' && news[news.length - 1] == ' ' || news[news.length - 1] == ',' || news[news.length - 1] ==
					'，') {
					title = key.split(news[news.length - 1])[0];
					title = title.trim();
					var obj = this.selectlist.find(item=>item.title == title);
					if(obj){
						this.inputzy = '';
						return;
					}
					this.selectlist.push({
						title
					});
					this.inputzy = '';
					this.change();
				}
			
			}

		},
		methods: {
			confirm(e) {
				this.changeVal(e.mp.detail.value);
			},
			changeVal(news) {
				var key = news.trim();
				if (key == '') {
					this.inputzy = '';
					return;
				}
				var title = key;
				var obj = this.selectlist.find(item=>item.title == title);
				if(obj){
					this.inputzy = '';
					return;
				}
				this.selectlist.push({
					title
				});
				this.inputzy = '';
				this.change();
			},
			del(index) {
				this.selectlist.splice(index, 1);
				this.change();
			},
			change(){
				this.$emit('change',this.selectlist);
			}
		},
		
	}
</script>
<style scoped lang="scss">
	.clearfixed::after {
		content: '';
		display: block;
		clear: both;
	}

	.box-border {
		border: 1px solid #D8D8D8;
		padding: 20upx 32upx;
		border-radius: 8upx;
		box-sizing: border-box;
	}

	/*
	 *  u-tag
	 */
	.u-tag {
		.tag-label {
			position: relative;
			float: left;
			font-size: 28upx;
			padding-right: 50upx;
			height: 50upx;
			line-height: 50upx;
			border-radius: 4upx;
			background: #38bfff;
			color: #fff;
			padding-left: 20upx;
			margin-right: 20upx;
			margin-bottom: 30upx;

			&::after {
				position: absolute;
				content: '×';
				margin-top:-2upx;
				right: 20upx;
			}
		}
	}

	.input-tag {
		line-height: 50upx;
		font-size: 28upx;
	}
</style>
