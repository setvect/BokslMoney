import Vue from "vue";
import codeApp from "../../component/code/codeApp.vue";

Vue.use(VeeValidate, {
	locale: "ko",
	events: "blur"
});
new Vue({
	el: "#app",
	render: h => h(codeApp)
});