import Constant from '@/utils/constants';

module.exports = {

	setToken: function(token) {
		uni.setStorageSync(Constant.USER_KEY, token);
	},

	getToken: function() {
		return uni.getStorageSync(Constant.USER_KEY);
	}
};
