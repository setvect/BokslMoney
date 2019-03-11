import Vue from "vue";
import settlementApp from "../../component/settlement/settlement.vue";
import VeeValidate from "vee-validate";
import ko from "vee-validate/dist/locale/ko";

Vue.use(VeeValidate, {
	locale: "ko",
	events: "blur",
	dictionary: {
		ko
	}
});

new Vue({
	el: "#app",
	render: h => h(settlementApp)
});
