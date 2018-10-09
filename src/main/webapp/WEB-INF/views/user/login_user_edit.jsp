<%@ page contentType="text/html; charset=UTF-8"%>
<!-- 개인정보 변경 -->
<form class="form-horizontal form-label-left" @submit.prevent="onSubmit()" id="edit-my">
	<div class="modal-header">
		<button type="button" class="close" data-dismiss="modal">&times;</button>
		<h4 class="modal-title">비밀번호 수정</h4>
	</div>
	<div class="modal-body">
		<div class="form-group">
			<label class="control-label col-md-3 col-sm-3 col-xs-12">비밀번호</label>
			<div class="col-md-9 col-sm-9 col-xs-12">
				<input type="password" class="form-control" name="password" v-model="item.password" v-validate="'required|min:4'" data-vv-as="비밀번호 ">
				<span class="error" v-if="errors.has('password')">{{errors.first('password')}}</span>
			</div>
		</div>
		<div class="form-group">
			<label class="control-label col-md-3 col-sm-3 col-xs-12">비밀번호(확인)</label>
			<div class="col-md-9 col-sm-9 col-xs-12">
				<input type="password" class="form-control" name="re-password" v-validate="'confirmed:password'" data-vv-as="비밀번호 ">
				<span class="error" v-if="errors.has('re-password')">{{errors.first('re-password')}}</span>
			</div>
		</div>
		<div class="modal-footer">
			<button type="submit" class="btn btn-primary">확인</button>
			<button type="button" class="btn btn-default" data-dismiss="modal">닫기</button>
		</div>
	</div>
</form>
<script type="text/javascript">
	Vue.use(VeeValidate, {
		locale: 'ko',
		events: 'blur',
	});

	new Vue({
		el: '#edit-my',
		data: {
			item: {}
		},
		methods: {
			// 수정
			onSubmit() {
				this.$validator.validateAll().then((result) => {
					if (!result) {
						return;
					}
					VueUtil.post('/user/edit.do', this.item, (result) => {
						alert("비밀번호 수정이 완료되었습니다.");
						$('#myinfo-edit-dialog').modal('hide');
					});
				});
			},
		},
	})
</script>