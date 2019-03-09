import Vue from "vue";
import calendarApp from "../../component/transaction/calendar.vue";

let EventBus = new Vue();
Vue.prototype.$EventBus = EventBus
new Vue({
	el: "#app",
	render: h => h(calendarApp),
	mounted() {
	}
});
