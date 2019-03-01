import Vue from "vue";
import loginUserForm from "../../component/user/loginUserEdit.vue";

$(() => {
	new Vue({
		el: "#edit-my",
		render: h => h(loginUserForm)
	});
});
