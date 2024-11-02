// #ifdef H5
// 通过blob:url读取实际的blob数据
// 参考：https://ask.dcloud.net.cn/question/75697
const blobURLToBlob = (url) => {
	return new Promise((resolve, reject) => {
		var http = new XMLHttpRequest()
		http.open('GET', url, true)
		http.responseType = 'blob'
		http.onload = function(e) {
			if (this.status == 200 || this.status === 0) {
				resolve(this.response)
			} else {
				reject(this.status)
			}
		}
		http.send()
	})
}

//  通过blob:url读取实际的ArrayBuffer数据
const H5ReadBlobUrlArrayBuffer = (blobUrl) => {
	return new Promise(async (resolve, reject) => {
		try {
			const reader = new FileReader()
			// blob数据转file对象数据
			const fileBlob = await blobURLToBlob(blobUrl)
			const file = new window.File([fileBlob], 'file.name', {
				type: 'file.type'
			})
			// 读取file对象ArrayBuffer
			reader.readAsArrayBuffer(file)
			reader.onload = function(e) {
				resolve(e?.target?.result)
			}
		} catch (e) {
			reject(e)
		}
	})
}
// #endif

// #ifdef APP-PLUS
// 通过plus.io读取文件的ArrayBuffer数据
const H5PlusReadFileArrayBuffer = (filePath) => {
	return new Promise((resolve, reject) => {
		try {
			plus.io.resolveLocalFileSystemURL(
				filePath,
				function(entry) {
					entry?.file(function(file) {
						const fileReader = new plus.io.FileReader()
						fileReader.readAsDataURL(file, 'utf-8')
						fileReader.onloadend = function(evt) {
							const result = {
								base64: evt.target.result.split(',')[1],
								size: file.size,
							}
							resolve(uni.base64ToArrayBuffer(result.base64))
						}
					})
				},
				function(error) {
					reject(error)
				},
			)
		} catch (error) {
			reject(error)
		}
	})
}
// #endif

module.exports = {
	getFileArrayBuffer: function(filePath) {
		if (uni.canIUse('getFileSystemManager') && uni.getFileSystemManager) {
		  const fs = uni.getFileSystemManager()
		  return fs.readFileSync(filePath)
		}
		// #ifdef APP-PLUS
		else if (plus.io) {
		  return H5PlusReadFileArrayBuffer(filePath)
		}
		// #endif
		// #ifdef H5
		else if (XMLHttpRequest) {
		  return H5ReadBlobUrlArrayBuffer(filePath)
		}
		// #endif
		else {
		  throw new Error('不支持的平台')
		}
	}
};

