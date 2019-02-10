import Vue from "vue";
import addComponent from "../../component/account/accountAdd.vue";
import listComponent from "../../component/account/accountList.vue";
import readComponent from "../../component/account/accountRead.vue";

Vue.use(VeeValidate, {
	locale: "ko",
	events: "blur"
});
let EventBus = new Vue();
Vue.prototype.$EventBus = EventBus
new Vue({
	el: "#app",
	components: {
		list: listComponent,
		add: addComponent,
		read: readComponent
	}
});
