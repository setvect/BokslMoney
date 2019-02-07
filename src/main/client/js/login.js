import $ from 'jquery'
import Vue from 'vue'

$(() => {
	var app = new Vue({
		el: '#app',
		data: {
		},
		methods:{
			login(event){
				event.preventDefault();
				let passinput = $("._login input[name='password']");
				if(!passinput.val()){
					alert("비밀번호를 입력하세요.");
					passinput.focus();
					return;
				}
				$("._login")[0].submit();
			}
		},
		mounted(){
			// timeout을 안주면 포커스가 안됨.
			setTimeout(()=>{
				$("._login input[name='password']").focus();
			},500);
		}
	})
});
