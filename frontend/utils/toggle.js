import Config from '@/utils/config.js'
import Rest from '@/utils/rest.js'

module.exports = {	
	loadToggle() {
		return new Promise((resolve, reject) => {
			var url = `${Config.JQ_DOMAIN}/api/wetalk/toggle-config`
			Rest.get(url).then(data => {
				if (data == null) {
					resolve(null)
				}
				var result = JSON.parse(data);
				resolve(result)
			}, err => {
				console.log("error to fetch toggles")
				reject(error)
			});
		})
	},
	
}
