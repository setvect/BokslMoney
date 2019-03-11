import Vue from "vue";
import loginUserForm from "../../component/user/loginUserEdit.vue";
import VeeValidate from "vee-validate";

Vue.use(VeeValidate, {
	locale: "ko",
	events: "blur"
});

$(() => {
	new Vue({
		el: "#edit-my",
		render: h => h(loginUserForm)
	});
});
