import Vue from "vue";
import gridApp from "../../component/transaction/grid.vue";
import VeeValidate from "vee-validate";
import ko from "vee-validate/dist/locale/ko";

Vue.use(VeeValidate, {
	locale: "ko",
	events: "blur",
	dictionary: {
		ko
	}
});

let EventBus = new Vue();
Vue.prototype.$EventBus = EventBus;
new Vue({
	el: "#app",
	render: h => h(gridApp),
	mounted() {
	}
});
