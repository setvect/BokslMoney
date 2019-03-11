import Vue from 'vue'
import loginForm from '../../component/login/loginForm.vue'
import VeeValidate from "vee-validate";

Vue.use(VeeValidate, {
	locale: "ko",
	events: "blur"
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
