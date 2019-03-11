import Vue from "vue";
import settlementApp from "../../component/settlement/settlement.vue";
import VeeValidate from "vee-validate";

Vue.use(VeeValidate, {
	locale: "ko",
	events: "blur"
});

new Vue({
	el: "#app",
	render: h => h(settlementApp)
});
