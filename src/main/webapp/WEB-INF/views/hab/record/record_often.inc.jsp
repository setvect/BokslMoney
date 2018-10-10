<%@ page contentType="text/html; charset=UTF-8"%>
<template id='item-often-add'>
	<div id="addOftenItem" class="modal fade" role="dialog">
		<div class="modal-dialog">
			<!-- Modal content-->
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">&times;</button>
					<h4 class="modal-title">수정</h4>
				</div>
				<div class="modal-body">
					<form class="form-horizontal">
						<div class="col-md-12 col-sm-12 col-xs-12">
							<div class="form-group">
								<label class="control-label col-md-2 col-sm-2 col-xs-2">날짜: </label>
								<div class="col-md-10 col-sm-10 col-xs-10">
									<input type="text" class="form-control has-feedback-left _datepicker" placeholder="First Name" readonly="readonly"
									 v-model="item.transactionDate" v-once>
									<span class="fa fa-calendar-o form-control-feedback left" aria-hidden="true"></span>
								</div>
							</div>
						</div>
					</form>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-info" @click="addAction()">저장</button>
					<button type="button" class="btn btn-default"@click="close()">닫기</button>
				</div>
			</div>
		</div>
	</div>
</template>

<script type="text/javascript">
	const oftenComponent = Vue.component("oftenItem", {
		template: '#item-often-add',
		data() {
			return {
				item: {},
			};
		},
		methods: {
			// 자주 쓰는 계좌
			openForm(kindType) {
				$("#addOftenItem").modal();
			},
			close() {
				$("#addOftenItem").modal("hide");
			},
			// 등록 또는 수정
			addAction() {
			},
		},
		mounted() {
		},
		created() {
			EventBus.$on('openOftenEvent', this.openForm);
		},
	});
</script>