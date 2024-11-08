import Alert from '@/utils/alert';
import Auth from '@/utils/auth';

function navigateBack(level) {
	uni.navigateBack({
		delta: level ? level : 1,
		fail: function (res) {
			uni.redirectTo({
				url: '/pages/index/index'
			});
		}
	});
}

/**
 *  把html转义成HTML实体字符
 * @param str
 * @returns {string}
 * @constructor
 */
function htmlEncode(str) {
	var s = "";
	if (str.length === 0) {
		return "";
	}
	s = str.replace(/&/g, "&amp;");
	s = s.replace(/</g, "&lt;");
	s = s.replace(/>/g, "&gt;");
	s = s.replace(/ /g, "&nbsp;");
	s = s.replace(/\'/g, "&#39;"); //IE下不支持实体名称
	s = s.replace(/\"/g, "&quot;");
	return s;
}

/**
 *  转义字符还原成html字符
 * @param str
 * @returns {string}
 * @constructor
 */
function htmlRestore(str) {
	var s = "";
	if (str.length === 0) {
		return "";
	}
	s = str.replace(/&amp;/g, "&");
	s = s.replace(/&lt;/g, "<");
	s = s.replace(/&gt;/g, ">");
	s = s.replace(/&nbsp;/g, " ");
	s = s.replace(/&#39;/g, "\'");
	s = s.replace(/&quot;/g, "\"");
	return s;
}

function openLink(link) {
	if (!link) {
		return;
	}
	
	link = htmlRestore(link);

	if (link.startsWith('/pages/')) {
		if (!Auth.getToken()) {
			let links = [
				'/pages/mine/site/mysite',
				'/pages/mine/site/siteCreate',
				'/pages/mine/activity/activity',
				'/pages/mine/activity/joined',
				'/pages/mine/activity/activityCreate',
				'/pages/mine/activity/activityRole',
				'/pages/mine/activity/activityDetail',
				'/pages/mine/activity/activityVoting',
				'/pages/mine/program/create',
				'/pages/mine/program/joined',
				'/pages/mine/program/ids',
				'/pages/mine/teacher/apply',
				'/pages/mine/favor/myfavor',
				'/pages/mine/program/myprogram'
			];
			for (let i = 0; i < links.length; i++) {
				if (link.indexOf(links[i]) > -1) {
					uni.navigateTo({
						url: '/pages/login/login',
						fail(res) {
							uni.redirectTo({
								url: '/pages/login/login'
							});
						}
					});
					return;
				}
			}
		}
		
		uni.navigateTo({
			url: link,
			fail: () => {
				uni.redirectTo({
					url: link
				})
			}
		});
	} else if (link.startsWith('https://') || link.startsWith('http://')) {
		uni.navigateTo({
			url: '/pages/webview/webview?src=' + link,
			fail: () => {
				uni.redirectTo({
					url: '/pages/webview/webview?src=' + link
				})
			}
		});
	} else {
		// #ifdef MP-WEIXIN
		if (link.startsWith('plugin-private://wx2b03c6e691cd7370')) {
			uni.navigateTo({
				url: link,
				fail: () => {
					uni.redirectTo({
						url: link
					})
				}
			})
			return;
		} else if (link.startsWith('appid:')) {
			let appid = '';
			let page = '';
			let index = link.indexOf(';page:');
			if (index < 0) {
				appid = link.substring('appid:'.length);
			} else {
				appid = link.substring('appid:'.length, index);
				page = link.substring(index + ';page:'.length);
			}
			let params = {
				appId: appid,
				fail: res => {
					uni.setClipboardData({
						data: link
					});
				}
			};
			if (page != '') {
				params.path = page;
			}

			uni.navigateToMiniProgram(params);
			return;
		}
		// #endif

		// #ifdef H5
		Alert.toast(link);
		// #endif

		// #ifndef H5
		uni.setClipboardData({
			data: link
		});
		// #endif
	}
}

module.exports = {
	navigateBack,
	openLink,
};
