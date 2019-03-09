import Vue from "vue";
import gridApp from "../../component/transaction/grid.vue";
let EventBus = new Vue();
Vue.prototype.$EventBus = EventBus;
new Vue({
	el: "#app",
	render: h => h(gridApp),
	mounted() {
	}
});
