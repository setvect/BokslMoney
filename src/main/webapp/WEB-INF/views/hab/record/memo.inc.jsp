<%@ page contentType="text/html; charset=UTF-8"%>
<template id='memo-add'>
	<div id="addMemo" class="modal fade" role="dialog">
		<div class="modal-dialog">
			<!-- Modal content-->
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">&times;</button>
					<h4 class="modal-title">메모 {{actionType == 'add' ? '등록' : '수정'}}</h4>
				</div>
				<div class="modal-body">
					<form class="form-horizontal">
						<div class="col-md-12 col-sm-12 col-xs-12">
							<div class="form-group">
								<label class="control-label col-md-2 col-sm-2 col-xs-2">메모: </label>
								<div class="col-md-10 col-sm-10 col-xs-10">
									<textarea class="form-control" name="note" v-model="item.note" v-validate="'required|max:500'" data-vv-as="메모 "></textarea>
									<div v-if="errors.has('note')">
										<span class="error">{{errors.first('note')}}</span>
									</div>
								</div>
							</div>
						</div>
					</form>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-info" @click="addAction()">저장</button>
					<button type="button" class="btn btn-warning" @click="deleteAction(item.memoSeq)" v-show="actionType == 'edit'">삭제</button>
					<button type="button" class="btn btn-default" @click="close()">닫기</button>
				</div>
			</div>
		</div>
	</div>
</template>

<script type="text/javascript">
	const memoComponent = Vue.component("memoForm", {
		template: '#memo-add',
		data() {
			return {
				item: { note: "" },
				actionType: 'add',
				selectDate: null,
			};
		},
		computed: {
		},
		methods: {
			// 등록 폼
			addForm(date) {
				this.item = {};
				this.actionType = 'add';
				this.item.memoDate = date.format("YYYY-MM-DD");
				this.openForm();
			},
			//수정 폼
			editForm(memo) {
				this.actionType = 'edit';
				this.item = memo;
				this.openForm();
			},
			// 자주 쓰는 계좌
			openForm(actionType, item) {
				$("#addMemo").modal();
			},
			close() {
				$("#addMemo").modal("hide");
			},
			addAction() {
				this.$validator.validateAll().then((result) => {
					if (!result) {
						return;
					}
					let url = this.actionType == 'add' ? '/hab/memo/add.do' : '/hab/memo/edit.do'
					VueUtil.post(url, this.item, (result) => {
						$("#addMemo").modal('hide');
						EventBus.$emit('reloadEvent');
					});
				});
			},
			// 삭제
			deleteAction(memoSeq) {
				if (!confirm("삭제?")) {
					return;
				}
				VueUtil.post('/hab/memo/delete.do', { memoSeq: memoSeq }, (result) => {
					$("#addMemo").modal('hide');
					EventBus.$emit('reloadEvent');
				});
			},
		},
		mounted() {
		},
		created() {
			EventBus.$on('addMemoFormEvent', this.addForm);
			EventBus.$on('editMemoFormEvent', this.editForm);
		},
	});
</script>