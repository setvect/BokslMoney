import $ from "jquery";

export default {
	getContextRoot() {
		return $("meta[name='contextRoot']").attr("content");
	},
	appendContextRoot(url) {
		return this.getContextRoot() + url;
	}
};
