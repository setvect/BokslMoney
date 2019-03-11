import Vue from "vue";
import assetsComponent from "../../component/stat/statAssets.vue";
import kindGroupComponent from "../../component/stat/statKindGroup.vue";
import VeeValidate from "vee-validate";

Vue.use(VeeValidate, {
	locale: "ko",
	events: "blur"
});

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
