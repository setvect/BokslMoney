import $ from 'jquery'

module.exports = {
	getContextRoot: function () {
		return $("meta[name='contextRoot']").attr("content");
	},
	appendContextRoot: function (url) {
		console.log("url", url)
		return this.getContextRoot() + url;
	}
}

