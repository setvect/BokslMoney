import Vue from "vue";
import loginUserForm from "../../component/user/loginUserEdit.vue";
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
		el: "#edit-my",
		render: h => h(loginUserForm)
	});
});
