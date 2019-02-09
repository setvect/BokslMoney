import Vue from 'vue'
import codeApp from '../component/codeApp.vue'

$(() => {
	new Vue({
		el: "#app",
		data: {
		},
		components: {
			codeApp: codeApp
		},
	});
});
