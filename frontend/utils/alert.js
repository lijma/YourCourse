function toast(msg) {
	uni.showToast({
		icon: 'none',
	    title: msg
	});
}

function error(msg) {
	uni.showToast({
		icon: 'none',
	    title: msg
	});
}

function success(msg) {
	uni.showToast({
		icon: 'none',
	    title: msg
	});
}

module.exports = {
	toast,
	error,
	success
};