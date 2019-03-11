import Vue from "vue";
import addComponent from "../../component/category/categoryAdd.vue";
import panelComponent from "../../component/category/categoryPanel.vue";
import VeeValidate from "vee-validate";

Vue.use(VeeValidate, {
	locale: "ko",
	events: "blur"
});

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
