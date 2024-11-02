import Constant from '@/utils/constants';
import Rest from '@/utils/rest.js'
import Config from '@/utils/config';

// #ifdef MP-WEIXIN
import COS from '@/static/cos/cos-wx-sdk-v5.min.js';
// #endif

// #ifdef H5
import COS from '@/static/cos/cos-js-sdk-v5.min.js';
// #endif


module.exports = {
		
	loadConfig: function() {
		var url = `${Config.JQ_DOMAIN}/api/wetalk/cos-config`
		Rest.get(url).then(data => {
			uni.setStorageSync(Constant.OSS_KEY, data);
		}, err => {
			console.log(err)
		});
	},
	
	upload: function(image, callaback){
		let ossConfig = uni.getStorageSync(Constant.OSS_KEY);
		let fileName = ossConfig.path+image.name
		let bucket = ossConfig.bucket_name
		let region = ossConfig.region
		const cos = new COS({
			SecretId: ossConfig.secret_id,
			SecretKey: ossConfig.secret_key,
		});
		
		cos.putObject({
		    Bucket: bucket,
		    Region: 'ap-chengdu',
			Key: fileName,
			Body: image.file,
			FilePath: image.blobPath
		}, function (err, data) {
			if(err != undefined){
				console.log("上传失败"+err)
			}
			let filePath = 'https://'+bucket+'.cos.'+region+'.myqcloud.com/'+fileName
			callaback(filePath)
		});
	}
		
};


