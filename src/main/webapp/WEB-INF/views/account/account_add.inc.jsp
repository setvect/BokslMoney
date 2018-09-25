<%@ page contentType="text/html; charset=UTF-8"%>
<template id='item-add'>
	<div id="addItem" class="modal fade" role="dialog">
		<div class="modal-dialog">
			<!-- Modal content-->
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">&times;</button>
					<h4 class="modal-title">등록</h4>
				</div>
				<div class="modal-body">
					<form class="form-horizontal">
						<div class="form-group">
							<label class="control-label col-md-3 col-sm-3 col-xs-3">이름: </label>
							<div class="col-md-9 col-sm-9 col-xs-9">
								<input type="text" class="form-control" name="name" v-model="item.name" v-validate="'required|max:20'" data-vv-as="이름 ">
								<span class="error" v-if="errors.has('name')">{{errors.first('name')}}</span>
							</div>
							</div>
						<div class="form-group">
							<label class="control-label col-md-3 col-sm-3 col-xs-3">계좌번호: </label>
							<div class="col-md-9 col-sm-9 col-xs-9">
								<input type="text" class="form-control" name="accountNumber" v-model="item.accountNumber" v-validate="'max:30'" data-vv-as="계좌번호 ">
								<span class="error" v-if="errors.has('accountNumber')">{{errors.first('accountNumber')}}</span>
							</div>
						</div>
						<div class="form-group">
							<label class="control-label col-md-3 col-sm-3 col-xs-3">자산종류: </label>
							<div class="col-md-9 col-sm-9 col-xs-9">
								<select class="form-control" v-model="item.kindCode">
									<option v-for="kindCode in kindCodeList" v-bind:value="kindCode.codeItemKey.codeItemSeq">
										{{ kindCode.name }}
									</option>
								</select>
							</div>
						</div>
						<div class="form-group">
							<label class="control-label col-md-3 col-sm-3 col-xs-3">잔고: </label>
							<div class="col-md-9 col-sm-9 col-xs-9">
								<input type="number" class="form-control" name="balance" v-model="item.balance" v-validate="'required|integer|max:11'" data-vv-as="잔고 ">
								<span class="error" v-if="errors.has('balance')">{{errors.first('balance')}}</span>
							</div>
						</div>
						<div class="form-group">
							<label class="control-label col-md-3 col-sm-3 col-xs-3">이율: </label>
							<div class="col-md-9 col-sm-9 col-xs-9">
								<input type="text" class="form-control" name="interestRate" v-model="item.interestRate" v-validate="'max:20'" data-vv-as="이율 ">
								<span class="error" v-if="errors.has('interestRate')">{{errors.first('interestRate')}}</span>
							</div>
						</div>
						<div class="form-group">
							<label class="control-label col-md-3 col-sm-3 col-xs-3">계약기간: </label>
							<div class="col-md-9 col-sm-9 col-xs-9">
								<input type="text" class="form-control" name="term" v-model="item.term" v-validate="'max:20'" data-vv-as="계약기간 ">
								<span class="error" v-if="errors.has('term')">{{errors.first('term')}}</span>
							</div>
						</div>
						<div class="form-group">
							<label class="control-label col-md-3 col-sm-3 col-xs-3">만기일: </label>
							<div class="col-md-9 col-sm-9 col-xs-9">
								<input type="text" class="form-control" name="expDate" v-model="item.expDate" v-validate="'max:20'" data-vv-as="만기일 ">
								<span class="error" v-if="errors.has('expDate')">{{errors.first('expDate')}}</span>
							</div>
						</div>
						<div class="form-group">
							<label class="control-label col-md-3 col-sm-3 col-xs-3">월 납입액: </label>
							<div class="col-md-9 col-sm-9 col-xs-9">
								<input type="text" class="form-control" name="monthlyPay" v-model="item.monthlyPay" v-validate="'max:20'" data-vv-as="월 납입액 ">
								<span class="error" v-if="errors.has('monthlyPay')">{{errors.first('monthlyPay')}}</span>
							</div>
						</div>
						<div class="form-group">
							<label class="control-label col-md-3 col-sm-3 col-xs-3">이체일: </label>
							<div class="col-md-9 col-sm-9 col-xs-9">
								<input type="text" class="form-control" name="transferDate" v-model="item.transferDate" v-validate="'max:20'" data-vv-as="이체일 ">
								<span class="error" v-if="errors.has('transferDate')">{{errors.first('transferDate')}}</span>
							</div>
						</div>
						<div class="form-group">
							<label class="control-label col-md-3 col-sm-3 col-xs-3">메모 내용: </label>
							<div class="col-md-9 col-sm-9 col-xs-9">
								<textarea class="form-control" name="note" v-model="item.note" v-validate="'max:200'" data-vv-as="메모 내용 "></textarea>
								<span class="error" v-if="errors.has('note')">{{errors.first('note')}}</span>
							</div>
						</div>

					</form>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-info" @click="addAction()">저장</button>
					<button type="button" class="btn btn-default" data-dismiss="modal">닫기</button>
				</div>
			</div>
		</div>
	</div>
</template>

<script type="text/javascript">
	const itemAddComponent = Vue.component("itemAdd", { template: '#item-add',
		data(){
			return {
				item: {},
				afterEventCallback: null,
				actionType: 'add',
				kindCodeList: []
			};
		},
		methods: {
			// 등록 폼 
			addForm(item, afterEventCallback){
				this.actionType = 'add';
				this.openForm(item, afterEventCallback);
			},
			//수정 폼 
			editForm(item, afterEventCallback){
				this.actionType = 'edit';
				this.openForm(item, afterEventCallback);
			},
			openForm(item, afterEventCallback){
				this.item = $.extend(true, {}, item);
				this.afterEventCallback = afterEventCallback;
				$("#addItem").modal();
			},
			// 등록 또는 수정
			addAction(){
				let self = this;
				
				this.$validator.validateAll().then((result) => {
					if(!result){
						return;
					}
					let url = this.actionType == 'add' ? '/account/add.do' : '/account/edit.do'
					waitDialog.show('처리 중입니다.', {dialogSize: 'sm'});
					axios.post(CommonUtil.getContextPath() + url, $.param(self.item, true)).then((result) => {
						$("#addItem").modal('hide');
						self.afterEventCallback();
						self.item.name = "";
					}).catch((err) =>	CommonUtil.popupError(err)).finally (() => waitDialog.hide());
				});
			},
			// 자산 코드 읽어 오기
			listKindCode(){
				waitDialog.show('조회 중입니다.', {dialogSize: 'sm'});
				axios.get(CommonUtil.getContextPath() + "/code/list.json", {params: {codeMainId: 'KIND_CODE'}}).then((result) => {
					this.kindCodeList = result.data;
				}).catch((err) => CommonUtil.popupError(err)).finally (() =>	waitDialog.hide());
			}
		},
		mounted(){
			this.listKindCode();
		},
		created(){
			EventBus.$on('addFormEvent', this.addForm);
			EventBus.$on('editFormEvent', this.editForm);
		},
	});
</script>
