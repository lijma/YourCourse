<template>
	<view>
		<web-view :src="src"></web-view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				src: 'https://wetalk-1259114324.cos.ap-chengdu.myqcloud.com/about_wetalk.pdf'
			};
		},

		onLoad(options) {
			if (options.src) {
				this.src = options.src;
			}
		},

		onShareAppMessage() {
			return {
				title: getApp().globalData.appName,
				path: 'pages/mix/webview/webview?src=' + this.src
			};
		},

		// #ifdef MP-WEIXIN
		onShareTimeline() {
			return {
				title: getApp().globalData.appName
			};
		},

		onAddToFavorites(res) {
			return {
				title: getApp().globalData.appName
			};
		},
		// #endif
	}
</script>

<style lang="scss">

</style>
