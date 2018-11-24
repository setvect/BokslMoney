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
									 v-model="item.transactionDate" v-once>
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
											<button class="btn btn-default" type="button" @click="openCategoryList(item.kind)">선택</button>
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
									<my-currency-input v-model="item.money" class="form-control" name="money" maxlength="10" v-validate="'required'"
									 data-vv-as="금액 "></my-currency-input>
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
							<p>자주 쓰는 거래</p>
							<div style="height:285px;overflow: auto;">
								<table class="table table-striped">
									<colgroup>
										<col style="width:30px;">
										<col>
										<col style="width:50px;">
										<col style="width:50px;">
									</colgroup>
									<tbody>
										<tr v-for="(often, index) in oftenUsedList">
											<td>{{index + 1}}</td>
											<td><a href="javascript:" @click="selectOftenUsed(often)">{{often.title}}</a></td>
											<td>
												<a href="javascript:" @click="changeOrder(oftenUsedList[index - 1].oftenUsedSeq, often.oftenUsedSeq)"
												 :style="{visibility: isUpable(index) ? '' : 'hidden'}"><i class="fa fa-arrow-up"></i></a>
												<a href="javascript:" @click="changeOrder(often.oftenUsedSeq, oftenUsedList[index + 1].oftenUsedSeq)"
												 :style="{visibility: isDownable(index) ? '' : 'hidden'}"><i class="fa fa-arrow-down"></i></a>
											</td>
											<td>
												<a href="javascript:" @click="openOften('edit', often)"><i class="fa fa-edit"></i></a>
												<a href="javascript:" @click="deleteOftenForm(often.oftenUsedSeq)"><i class="fa fa-remove"></i></a>
											</td>
										</tr>
									</tbody>
								</table>
							</div>
							<div class="x_content">
								<button type="button" class="btn btn-primary btn-xs" @click="openOften('add', {kind: item.kind, money: 0})">자주쓰는
									거래 저장</button>
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
		<div>
			<often />
		</div>
		<div>
			<category />
		</div>
	</div>
</template>

<jsp:include page="record_category.inc.jsp"></jsp:include>
<jsp:include page="record_often.inc.jsp"></jsp:include>

<script type="text/javascript">
	const itemAddComponent = Vue.component("itemAdd", {
		template: '#item-add',
		data() {
			return {
				item: { money: 0, fee: 0, kind: null },
				accountList: [],
				actionType: 'add',
				attributeList: [],
				itemPath: null,
				selectDate: null,
				oftenUsedList: [],
				// 유형별 거래 내용
				// Key: kind, Value: 거래 내용
				beforeTransaction: {},
			};
		},
		components: {
			'category': categoryComponent,
			'often': oftenComponent
		},
		computed: {
			itemLabel() {
				const ITEM_TYPE_LABEL = { INCOME: '수입', SPENDING: '지출', TRANSFER: '이체' }
				return ITEM_TYPE_LABEL[this.item.kind];
			},
			// 출금계좌 선택 박스 비활성
			disablePay() {
				return this.item.kind == "INCOME";
			},
			// 수입계좌 선택 박스 비활성
			disableReceive() {
				return this.item.kind == "SPENDING";
			},
			validatePay() {
				return this.disablePay ? "" : "required";
			},
			validateReceive() {
				if (this.item.kind == "SPENDING") {
					return "";
				}
				// 이체에서는 지출계좌와 수입 계좌가 같으면 안됨.
				if (this.item.kind == "TRANSFER") {
					return { required: true, notEquals: this.item.payAccount };
				}
				return "required";
			},
		},
		methods: {
			// 등록 폼
			addForm(kind, date) {
				this.actionType = 'add';
				this.selectDate = date;
				if (this.beforeTransaction[kind]) {
					this.item = this.beforeTransaction[kind];
				} else {
					this.item = { money: 0, fee: 0, kind: null };
				}
				this.insertCategory(this.item.parentCategory, this.item.category);
				this.item.transactionDate = this.selectDate.format("YYYY-MM-DD")
				this.item.kind = kind;
				this.openForm(this.item.kind);
			},
			//수정 폼
			editForm(transaction) {
				this.actionType = 'edit';
				this.selectDate = moment(transaction.transactionDate);
				this.item = transaction;
				this.item.transactionDate = this.selectDate.format("YYYY-MM-DD")
				this.insertCategory(transaction.parentCategory, transaction.category);
				this.openForm(this.item.kind);
			},
			// datepicker
			updateDate: function (d) {
				this.item.transactionDate = d;
			},
			// 계좌 입력 팝업창.
			openForm(kind) {
				this.item.kind = kind;
				const ITEM_TYPE_ATTR = { INCOME: 'ATTR_INCOME', SPENDING: 'ATTR_SPENDING', TRANSFER: 'ATTR_TRANSFER' }
				this.loadAttribute(ITEM_TYPE_ATTR[this.item.kind]);
				this.loadOftenUsed();

				$('._datepicker').daterangepicker({
					singleDatePicker: true,
					singleClasses: "",
					startDate: this.selectDate.format("YYYY-MM-DD")
				}, (start) => {
					this.item.transactionDate = start.format("YYYY-MM-DD");
				});

				$("#addItem").modal();
			},
			// 등록 또는 수정
			addAction() {
				this.$validator.validateAll().then((result) => {
					if (!result) {
						return;
					}
					this.beforeTransaction[this.item.kind] = $.extend(true, {}, this.item);

					delete this.item.category;
					delete this.item.parentCategory;

					let url = this.actionType == 'add' ? '/hab/transaction/add.do' : '/hab/transaction/edit.do'
					VueUtil.post(url, this.item, (result) => {
						$("#addItem").modal('hide');
						EventBus.$emit('reloadEvent');
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
					if (!this.item.attribute) {
						this.item.attribute = this.attributeList[0].codeItemKey.codeItemSeq;
					}
				});
			},
			// 자주쓰는 거래 정보
			loadOftenUsed() {
				VueUtil.get("/hab/oftenUsed/list.json", { kind: this.item.kind }, (result) => {
					this.oftenUsedList = result.data;
				});
			},
			// 항목 선택 팝업.
			openCategoryList(kind) {
				EventBus.$emit('openCategoryListEvent', kind, 'add');
			},
			// 항목 팝업에서 선택한 값 입력
			insertCategory(mainItem, subItem) {
				this.itemPath = "";
				if (mainItem) {
					this.item.categorySeq = subItem.categorySeq;
					this.itemPath = mainItem.name + " > " + subItem.name;
				}
			},
			// 자주쓰는 거래 선택
			selectOftenUsed(often) {
				this.item = Object.assign(this.item, $.extend(true, {}, often));
				this.insertCategory(often.parentCategory, often.category);
			},
			// 자주쓰는 거래 팝업 열기
			openOften(type, often) {
				EventBus.$emit('openOftenEvent', type, $.extend(true, {}, often));
			},
			// 정렬 순서 변경
			changeOrder(downOftenUsedSeq, upOftenUsedSeq) {
				let param = { downOftenUsedSeq: downOftenUsedSeq, upOftenUsedSeq: upOftenUsedSeq };
				VueUtil.post('/hab/oftenUsed/changeOrder.do', param, (result) => {
					this.loadOftenUsed();
				});
			},
			// 자주 쓰는 거래 삭제
			deleteOftenForm(oftenUsedSeq) {
				if (!confirm("삭제할거야?")) {
					return;
				}
				let param = { oftenUsedSeq: oftenUsedSeq };
				VueUtil.post('/hab/oftenUsed/delete.do', param, (result) => {
					this.loadOftenUsed();
				});
			},
			isUpable(index) {
				if (this.oftenUsedList <= 1) {
					return false;
				}
				return index !== 0;
			},
			isDownable(index) {
				if (this.oftenUsedList.length <= 1) {
					return false;
				}
				return index + 1 !== this.oftenUsedList.length;
			},
		},
		mounted() {
			this.loadAccount();
		},
		created() {
			EventBus.$on('addFormEvent', this.addForm);
			EventBus.$on('editFormEvent', this.editForm);
			EventBus.$on('insertCategoryEvent', this.insertCategory);
			EventBus.$on('listOftenUsedEvent', this.loadOftenUsed);

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