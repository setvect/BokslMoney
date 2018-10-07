<%@ page contentType="text/html; charset=UTF-8"%>
<template id='item-add'>
	<div id="addItem" class="modal fade" role="dialog">
		<div class="modal-dialog modal-lg">
			<!-- Modal content-->
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">&times;</button>
					<h4 class="modal-title">{{itemLabel}} 내역 {{actionType == 'add' ? '등록' : '수정'}}</h4>
				</div>
				<div class="modal-body">
					<form class="form-horizontal">
						<div class="col-md-7 col-sm-7 col-xs-7">

							<div class="form-group">
								<label class="control-label col-md-2 col-sm-2 col-xs-2">날짜: </label>
								<div class="col-md-10 col-sm-10 col-xs-10">
									<input type="text" class="form-control has-feedback-left _datepicker" placeholder="First Name" readonly="readonly"
									 v-model="item.transferDate" v-once>
									<span class="fa fa-calendar-o form-control-feedback left" aria-hidden="true"></span>
								</div>
							</div>

							<div class="form-group">
								<label class="control-label col-md-2 col-sm-2 col-xs-2">항목: </label>
								<div class="col-md-10 col-sm-10 col-xs-10">
									<div class="input-group no-padding">
										<input type="text" class="form-control" readonly="readonly" name="item" v-model="itemPath" v-validate="'required'"
										 data-vv-as="항목 ">
										<span class="input-group-btn">
											<button class="btn btn-default" type="button" @click="openItemList(kindType)">선택</button>
										</span>
									</div>
									<div v-if="errors.has('item')">
										<span class="error">{{errors.first('item')}}</span>
									</div>
								</div>
							</div>
							<div class="form-group">
								<label class="control-label col-md-2 col-sm-2 col-xs-2">메모: </label>
								<div class="col-md-10 col-sm-10 col-xs-10">
									<input type="text" class="form-control" name="note" v-model="item.note" v-validate="'required'" data-vv-as="메모 ">
									<span class="error" v-if="errors.has('note')">{{errors.first('note')}}</span>
								</div>
							</div>

							<div class="form-group">
								<label class="control-label col-md-2 col-sm-2 col-xs-2">금액: </label>
								<div class="col-md-10 col-sm-10 col-xs-10">
									<my-currency-input v-model="item.money" class="form-control" name="money" maxlength="10" v-validate="'required'" data-vv-as="금액 "></my-currency-input>
									<span class="error" v-if="errors.has('money')">{{errors.first('money')}}</span>
								</div>
							</div>

							<div class="form-group">
								<label class="control-label col-md-2 col-sm-2 col-xs-2">지출계좌: </label>
								<div class="col-md-10 col-sm-10 col-xs-10">
									<select class="form-control" v-model="item.payAccount" name="payAccount" v-validate="validatePay" data-vv-as="지출계좌 "
									 :disabled="disablePay">
										<option v-for="account in accountList" v-bind:value="account.accountSeq">
											{{account.name}}
										</option>
									</select>
									<span class="error" v-if="errors.has('payAccount')">{{errors.first('payAccount')}}</span>
								</div>
							</div>

							<div class="form-group">
								<label class="control-label col-md-2 col-sm-2 col-xs-2">수입계좌: </label>
								<div class="col-md-10 col-sm-10 col-xs-10">
									<select class="form-control" v-model="item.receiveAccount" name="receiveAccount" v-validate="validateReceive"
									 data-vv-as="수입계좌 " :disabled="disableReceive">
										<option v-for="account in accountList" v-bind:value="account.accountSeq">
											{{account.name}}
										</option>
									</select>
									<span class="error" v-if="errors.has('receiveAccount')">{{errors.first('receiveAccount')}}</span>
								</div>
							</div>

							<div class="form-group">
								<label class="control-label col-md-2 col-sm-2 col-xs-2">속성: </label>
								<div class="col-md-10 col-sm-10 col-xs-10">
									<select class="form-control" v-model="item.attribute" name="attribute" v-validate="'required'" data-vv-as="속성 ">
										<option v-for="attribute in attributeList" v-bind:value="attribute.codeItemKey.codeItemSeq">
											{{attribute.name}}
										</option>
									</select>
									<span class="error" v-if="errors.has('attribute')">{{errors.first('attribute')}}</span>
								</div>
							</div>

							<div class="form-group">
								<label class="control-label col-md-2 col-sm-2 col-xs-2">수수료: </label>
								<div class="col-md-10 col-sm-10 col-xs-10">
									<my-currency-input v-model="item.fee" class="form-control" name="money" maxlength="5"></my-currency-input>
								</div>
							</div>
						</div>
						<div class="col-md-5 col-sm-5 col-xs-5">
							aaaaaaaaaaaaa<br />sa
						</div>
					</form>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-info" @click="addAction()">저장</button>
					<button type="button" class="btn btn-default" data-dismiss="modal">닫기</button>
				</div>
			</div>
		</div>
		<all-list />
	</div>
</template>

<jsp:include page="record_item.inc.jsp"></jsp:include>

<script type="text/javascript">
	const itemAddComponent = Vue.component("itemAdd", {
		template: '#item-add',
		data() {
			return {
				item: {money: 0, fee: 0},
				accountList: [],
				actionType: 'add',
				attributeList: [],
				kindType: null,
				itemPath: null,
				selectDate: null,

			};
		},
		components: {
			'allList': itemAllListComponent
		},
		computed: {
			itemLabel() {
				const ITEM_TYPE_LABEL = { INCOME: '수입', SPENDING: '지출', TRANSFER: '이체' }
				return ITEM_TYPE_LABEL[this.kindType];
			},
			// 지출계좌 선택 박스 비활성
			disablePay() {
				return this.kindType == "INCOME";
			},
			// 수입계좌 선택 박스 비활성
			disableReceive() {
				return this.kindType == "SPENDING";
			},
			validatePay() {
				return this.disablePay ? "" : "required";
			},
			validateReceive() {
				if (this.kindType == "SPENDING") {
					return "";
				}
				// 이체에서는 지출계좌와 수입 계좌가 같으면 안됨.
				if (this.kindType == "TRANSFER") {
					return { required: true, notEquals: this.item.payAccount };
				}
				return "required";
			},
		},
		methods: {
			// 등록 폼
			addForm(kindType, date) {
				this.selectDate = date;
				this.actionType = 'add';
				this.item.transferDate = this.selectDate.format("YYYY-MM-DD")
				this.item.kind = kindType;
				this.openForm(kindType);
			},
			//수정 폼
			editForm(kindType) {
				this.actionType = 'edit';
				this.openForm(kindType);
			},
			// datepicker
			updateDate: function (d) {
				this.item.transferDate = d;
			},
			// 계좌 입력 팝업창.
			openForm(kindType) {
				this.kindType = kindType;
				const ITEM_TYPE_ATTR = { INCOME: 'ATTR_INCOME', SPENDING: 'ATTR_SPENDING', TRANSFER: 'ATTR_TRANSFER' }
				this.loadAttribute(ITEM_TYPE_ATTR[this.kindType]);

				$('._datepicker').daterangepicker({
					singleDatePicker: true,
					singleClasses: "",
					startDate: this.selectDate.format("YYYY-MM-DD")
				}, (start) => {
					this.item.transferDate = start.format("YYYY-MM-DD");
				});

				$("#addItem").modal();
			},
			// 등록 또는 수정
			addAction() {
				this.$validator.validateAll().then((result) => {
					if (!result) {
						return;
					}
					let url = this.actionType == 'add' ? '/hab/transfer/add.do' : '/hab/transfer/edit.do'
					VueUtil.post(url, this.item, (result) => {
						$("#addItem").modal('hide');
						EventBus.$emit('listEvent');
					});
				});
			},
			// 계좌 목록
			loadAccount() {
				VueUtil.get("/hab/account/list.json", {}, (result) => {
					this.accountList = result.data;
				});
			},
			// 속성
			loadAttribute(codeMainId) {
				VueUtil.get("/code/list.json", { codeMainId: codeMainId }, (result) => {
					this.attributeList = result.data;
					if(this.actionType == "add"){
						this.item.attribute = this.attributeList[0].codeItemKey.codeItemSeq;
					}
				});
			},
			// 항목 선택 팝업.
			openItemList(kindType) {
				EventBus.$emit('openItemListEvent', kindType);
			},
			// 항목 팝업에서 선택한 값 입력
			insertItem(mainItem, subItem) {
				this.item.itemSeq = subItem.itemSeq;
				this.itemPath = mainItem.name + " > " + subItem.name;
			}
		},
		mounted() {
			this.loadAccount();
		},
		created() {
			EventBus.$on('addFormEvent', this.addForm);
			EventBus.$on('editFormEvent', this.editForm);
			EventBus.$on('insertItemEvent', this.insertItem);

			// 커스텀 validation
			this.$validator.extend('notEquals', {
				getMessage: function (field, args) {
					return '같은 계좌를 지정할 수 없습니다.';
				},
				validate: function (value, args) {
					return value != args[0];
				}
			});
		},
	});
</script>