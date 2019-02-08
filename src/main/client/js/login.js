import Vue from 'vue'
import loginForm from '../component/loginForm.vue'

$(() => {
	new Vue({
		el: "#app",
		data: {
		},
		components: {
			login: loginForm
		},
	});
});
