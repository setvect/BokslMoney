import Vue from "vue";
import calendarApp from "../../component/transaction/calendar.vue";
import VeeValidate from "vee-validate";

Vue.use(VeeValidate, {
	locale: "ko",
	events: "blur"
});


let EventBus = new Vue();
Vue.prototype.$EventBus = EventBus
new Vue({
	el: "#app",
	render: h => h(calendarApp),
	mounted() {
	}
});
