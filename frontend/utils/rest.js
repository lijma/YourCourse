const Auth = require("./auth.js");

/**
 * request封装
 */
function request(url, data = {}, method = "GET") {
	return new Promise(function(resolve, reject) {
		uni.showLoading({
			title: '加载中'
		});

		uni.request({
			url: url,
			data: data,
			method: method,
			header: {
				'Content-Type': "application/json",
				'Authorization': 'Bearer '+Auth.getToken()
			},
			success(res) {
				if (res.statusCode != 200) {
					reject(res.errMsg);
					return;
				}

				if (res.data.code == -1) {
					uni.navigateTo({
						url: '/pages/login/login',
					});
					return;
				}

				resolve(res.data);
			},
			fail(err) {
				reject(err);
			},
			complete() {
				uni.hideLoading();
			}
		});
	});
}

/**
 * get请求
 */
function get(url, data = {}) {
	return request(url, data, 'GET');
}

/**
 * post请求
 */
function post(url, data = {}) {
	return request(url, data, 'POST');
}

module.exports = {
	get,
	post,
};
