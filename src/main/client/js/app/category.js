import Vue from "vue";
import addComponent from "../../component/category/categoryAdd.vue";
import panelComponent from "../../component/category/categoryPanel.vue";

let EventBus = new Vue();
Vue.prototype.$EventBus = EventBus;
new Vue({
	el: "#app",
	data: function() {
		return {
			addType: "main",
			item: { name: "" },
			kind: "",
			addAfterCallback: null
		};
	},
	components: {
		panel: panelComponent,
		add: addComponent
	}
});
