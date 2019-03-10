import Vue from "vue";
import assetsComponent from "../../component/stat/statAssets.vue";
import kindGroupComponent from "../../component/stat/statKindGroup.vue";

new Vue({
	el: "#app",
	data: function() {
		return {};
	},
	components: {
		statAssets: assetsComponent,
		kindGroup: kindGroupComponent
	},
	computed: {},
	methods: {},
	mounted() {}
});
