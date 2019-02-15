import Vue from "vue";
import settlementApp from "../../component/settlement/settlement.vue";

Vue.use(VeeValidate, {
	locale: "ko",
	events: "blur"
});
new Vue({
	el: "#app",
	render: h => h(settlementApp)
});
