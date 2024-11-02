<template>
	<view>
		<view class="photo">
			<image v-for="(item,index) in imgArr" @click="preImage(item.url,imgArr)" :key="index" :src="item.url"
				mode="scaleToFill">
			</image>
			
			<image v-if="imgArr.length<imgLimit" @tap="chooseImg"
				src="@/static/images/add.png" />
		</view>

	</view>
</template>

<script>
	import Oss from '@/utils/oss.js';
	import Rest from '@/utils/rest.js';
	export default {
		data() {
			return {
				dataList: [],
				imgArr: [{
					url: 'https://thirdwx.qlogo.cn/mmopen/vi_32/POgEwh4mIHO4nibH0KlMECNjjGxQUq24ZEaGT4poC6icRiccVGKSyXwibcPq4BWmiaIGuG1icwxaQX6grC9VemZoJ8rg/132'
				}],
				imgLimit: 10,
				fo: {
					filePath: undefined,
					name: undefined,
					file: undefined
				}
			}
		},
		props: {
			value: String | Number,
			mediaType: {
				type: String,
				default: 'image'
			}
		},
		watch: {
		  value(newVal) {
		    // Handle the updated prop value
		    console.log('The prop value has changed:', newVal);
			this.imgArr = [{
				url: newVal
			}]
		    // Perform additional actions based on the updated value
		  }
		},
		methods: {
			chooseImg: async function() {
				//选择图片
				// #ifdef MP-WEIXIN
				console.log("wechat pic selecting")
				wx.chooseImage({
					sizeType: ['original', 'compressed'], //可选择原图或压缩后的图片
					sourceType: ['album', 'camera'], //可选择性开放访问相册、相机
					success: res => {
						this.select(res);
					}
				})

				// #endif

				// #ifdef H5
				console.log("h5 pic selecting")
				uni.chooseImage({
					sourceType: ['camera', 'album'],
					sizeType: ['compressed', 'original'],
					count: 1,
					success: e => {
						this.select(e);
					}
				});
				// #endif
			},

			select: async function(e) {
				console.log("selecting...")
				console.log(e)
				this.fo.filePath = e.tempFilePaths[0];
				
				// #ifdef MP-WEIXIN
				this.fo.name = this.fo.filePath.substring(this.fo.filePath.lastIndexOf('/') + 1)
				this.fo.file = await this.wxfile(this.fo.filePath).then(data => data);
				// #endif
				
				// #ifdef H5
				this.fo.name = e.tempFiles[0].name;
				this.fo.file = await fetch(this.fo.filePath).then(e => e.blob())
				// #endif
		
				console.log(this.fo);
				let that = this;
				Oss.upload(this.fo, (fileName) => {
					console.log("上传成功. ")
					console.log(fileName)
					this.$emit('input', fileName);
					this.$emit('change', fileName);
					this.imgArr = [{
						url: fileName
					}]
				})
			},

			preImage(url, urls) {
				let _urls = urls.map(ele => {
					return ele.url;
				})
				console.log(url, urls);
				uni.previewImage({
					current: url,
					urls: _urls,
					longPressActions: {
						itemList: ['发送给朋友', '保存图片', '收藏'],
						success: function(data) {
							console.log('选中了第' + (data.tapIndex + 1) + '个按钮,第' + (data.index + 1) + '张图片');
						},
						fail: function(err) {
							console.log(err.errMsg);
						}
					}
				});
			},

			wxfile: function(path) {
				return new Promise((resolve, reject) => {
					let fs = wx.getFileSystemManager();
					fs.readFile({
						filePath: path,
						success: function(res) {
							resolve(res.data)
						},
						fail: function(error) {
							console.log("error happend" + error)
							reject(error)
						}
					});
				})
			}
		},

	}
</script>

<style lang="scss">
	.photo {
		image {
			width: 23%;
			height: 150upx;
			margin: 0 2% 20upx 0;
			position: relative;
		}
	}
</style>
