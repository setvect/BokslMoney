import Vue from 'vue'
import loginForm from '../../component/login/loginForm.vue'

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
