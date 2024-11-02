<script>
	import Vue from 'vue'
	
	export default {
		globalData: {
			appName: 'WeTalk学习共创助手',
			appDesc: '',
		},

		onLaunch: function() {
			uni.getSystemInfo({
				success: (e) => {
					Vue.prototype.$is_ios = (e.platform == 'ios');
				}
			})
		},

	}
</script>

<style lang="scss">
	@import "style/style.scss";
	@import "style/main.scss";
</style>
