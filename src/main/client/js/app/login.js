import Vue from 'vue'
import loginForm from '../../component/login/loginForm.vue'
import VeeValidate from "vee-validate";
import ko from "vee-validate/dist/locale/ko";

Vue.use(VeeValidate, {
	locale: "ko",
	events: "blur",
	dictionary: {
		ko
	}
});

$(() => {
	new Vue({
		el: "#app",
		data: {
		},
		components: {
			loginForm: loginForm
		},
	});
});
