<%@ page contentType="text/html; charset=UTF-8"%>
<template id='item-read'>
	<div id="readItem" class="modal fade" role="dialog">
		<div class="modal-dialog modal-lg">
			<!-- Modal content-->
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">&times;</button>
					<h4 class="modal-title">내용 보기</h4>
				</div>
				<div class="modal-body">
					<table class="table table-bordered">
						<colgroup>
							<col style="width: 100px;">
							<col style="width: 35%">
							<col style="width: 100px;">
							<col style="width: 35%">
						</colgroup>
						<tbody>
							<tr>
								<th scope="row">이름</th>
								<td>{{item.name}}</td>
								<th scope="row">계좌(카드)번호</th>
								<td>{{item.accountNumber}}</td>
							</tr>
							<tr>
								<th scope="row">자산종류</th>
								<td>{{item.kindName}}</td>
								<th scope="row">잔고</th>
								<td>{{item.balance | numberFormat}}</td>
							</tr>
							<tr>
								<th scope="row">이율</th>
								<td>{{item.interestRate}}</td>
								<th scope="row">계약기간</th>
								<td>{{item.term}}</td>
							</tr>
							<tr>
								<th scope="row">만기일</th>
								<td>{{item.expDate}}</td>
								<th scope="row">월 납입액</th>
								<td>{{item.monthlyPay}}</td>
							</tr>
							<tr>
								<th scope="row">이체일</th>
								<td>{{item.transactionDate}}</td>
								<th scope="row">메모 내용</th>
								<td><span v-br="item.note"/></td>
							</tr>
						</tbody>
					</table>

				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-info" @click="editForm(item)">수정</button>
					<button type="button" class="btn btn-info" @click="deleteAction(item.accountSeq)">삭제</button>
					<button type="button" class="btn btn-default" data-dismiss="modal">닫기</button>
				</div>
			</div>
		</div>
	</div>
</template>

<script type="text/javascript">
	const itemReadComponent = Vue.component("itemRead", { template: '#item-read',
		data(){
			return {
				item: {},
			};
		},
		methods: {
			readForm(item){
				this.item = $.extend(true, {}, item);
				$("#readItem").modal();
			},
			editForm(item){
				$("#readItem").modal("hide");
				EventBus.$emit('addFormEvent', item);
			},
			// 삭제
			deleteAction(itemSeq){
				if(!confirm("삭제?")){
					return;
				}
				VueUtil.post('/hab/account/delete.do', {accountSeq: itemSeq}, (result) => {
					$("#readItem").modal('hide');
					EventBus.$emit('listEvent');
				});
			},
		},
		mounted(){
		},
		created(){
			EventBus.$on('readFormEvent', this.readForm);
		},
	});
</script>
