<template>
	<form method="post" class="_login">
		<input type="hidden" :name="name" :value="value">
		<h1>복슬머니 로그인</h1>
		<div>
			<input
				name="password"
				type="password"
				class="form-control"
				placeholder="Password"
				@keypress.13="login"
			>
		</div>
		<div>
			<label>
				<input type="checkbox" name="remember-me">
				<i></i>
				<em>로그인 정보 유지</em>
			</label>
		</div>
		<div>
			<button class="btn btn-default submit" @click="login">
				<em>로그인</em>
			</button>
		</div>
	</form>
</template>

<script>
import $ from "jquery";
import CommonUtil from "../js/common-util.js";
export default {
	name: "layout",
	data: function() {
		return {};
	},
	props: ["name", "value"],
	methods: {
		login(event) {
			event.preventDefault();
			$("._login")[0].action = CommonUtil.appendContextRoot("/login.do");
			let passinput = $("._login input[name='password']");
			if (!passinput.val()) {
				alert("비밀번호를 입력하세요.");
				passinput.focus();
				return;
			}
			$("._login")[0].submit();
		}
	},
	mounted() {
		// timeout을 안주면 포커스가 안됨.
		setTimeout(() => {
			$("._login input[name='password']").focus();
		}, 500);
	}
};
</script>