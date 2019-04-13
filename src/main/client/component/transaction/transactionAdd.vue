<template>
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
								<label class="control-label col-md-2 col-sm-2 col-xs-2">날짜:</label>
								<div class="col-md-10 col-sm-10 col-xs-10">
									<div class="col-md-8 col-sm-8 col-xs-8" style="padding:0;">
										<input type="text" class="form-control has-feedback-left _datepicker" placeholder="First Name" readonly="readonly" v-model="item.transactionDate" v-once>
										<span class="fa fa-calendar-o form-control-feedback left" aria-hidden="true" style="display: block; left: 3px;"></span>
									</div>
									<div class="col-md-4 col-sm-4 col-xs-4" style="padding:0;">
										<div class="btn-group" style="float:right;">
											<button type="button" class="btn btn-default" @click="addDate(-1)">전날</button>
											<button type="button" class="btn btn-default" @click="addDate(1)">다음날</button>
										</div>
									</div>
								</div>
							</div>

							<div class="form-group">
								<label class="control-label col-md-2 col-sm-2 col-xs-2">항목:</label>
								<div class="col-md-10 col-sm-10 col-xs-10">
									<div class="input-group no-padding">
										<input type="text" class="form-control" readonly="readonly" name="item" v-model="itemPath" v-validate="'required'" data-vv-as="항목 ">
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
								<label class="control-label col-md-2 col-sm-2 col-xs-2">메모:</label>
								<div class="col-md-10 col-sm-10 col-xs-10">
									<input type="text" class="form-control _note" name="note" v-model="item.note" v-validate="'required'" data-vv-as="메모 ">
									<span class="error" v-if="errors.has('note')">{{errors.first('note')}}</span>
								</div>
							</div>

							<div class="form-group">
								<label class="control-label col-md-2 col-sm-2 col-xs-2">금액:</label>
								<div class="col-md-10 col-sm-10 col-xs-10">
									<my-currency-input v-model="item.money" class="form-control" name="money" maxlength="10" v-validate="'required'" data-vv-as="금액 " @press-enter="addAction(true)"></my-currency-input>
									<span class="error" v-if="errors.has('money')">{{errors.first('money')}}</span>
								</div>
							</div>

							<div class="form-group">
								<label class="control-label col-md-2 col-sm-2 col-xs-2">지출계좌:</label>
								<div class="col-md-10 col-sm-10 col-xs-10">
									<select class="form-control" v-model="item.payAccount" name="payAccount" v-validate="validatePay" data-vv-as="지출계좌 " :disabled="disablePay">
										<option v-for="account in accountList" v-bind:value="account.accountSeq" :key="account.accountSeq">{{account.name}} : {{account.balance | numberFormat}}원 ({{account.accountNumber}})</option>
									</select>
									<span class="error" v-if="errors.has('payAccount')">{{errors.first('payAccount')}}</span>
								</div>
							</div>

							<div class="form-group">
								<label class="control-label col-md-2 col-sm-2 col-xs-2">수입계좌:</label>
								<div class="col-md-10 col-sm-10 col-xs-10">
									<select class="form-control" v-model="item.receiveAccount" name="receiveAccount" v-validate="validateReceive" data-vv-as="수입계좌 " :disabled="disableReceive">
										<option v-for="account in accountList" v-bind:value="account.accountSeq" :key="account.accountSeq">{{account.name}} : {{account.balance | numberFormat}}원 ({{account.accountNumber}})</option>
									</select>
									<span class="error" v-if="errors.has('receiveAccount')">{{errors.first('receiveAccount')}}</span>
								</div>
							</div>

							<div class="form-group">
								<label class="control-label col-md-2 col-sm-2 col-xs-2">속성:</label>
								<div class="col-md-10 col-sm-10 col-xs-10">
									<select class="form-control" v-model="item.attribute" name="attribute" v-validate="'required'" data-vv-as="속성 ">
										<option v-for="attribute in attributeList" v-bind:value="attribute.codeItemKey.codeItemSeq" :key="attribute.codeItemKey.codeItemSeq">{{attribute.name}}</option>
									</select>
									<span class="error" v-if="errors.has('attribute')">{{errors.first('attribute')}}</span>
								</div>
							</div>

							<div class="form-group">
								<label class="control-label col-md-2 col-sm-2 col-xs-2">수수료:</label>
								<div class="col-md-10 col-sm-10 col-xs-10">
									<my-currency-input v-model="item.fee" class="form-control" name="money" maxlength="5" :disabled="disableFee"></my-currency-input>
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
										<tr v-for="(often, index) in oftenUsedList" :key="often.oftenUsedSeq">
											<td>{{index + 1}}</td>
											<td>
												<a href="javascript:" @click="selectOftenUsed(often)">{{often.title}}</a>
											</td>
											<td>
												<a href="javascript:" @click="changeOrder(oftenUsedList[index - 1].oftenUsedSeq, often.oftenUsedSeq)" :style="{visibility: isUpable(index) ? '' : 'hidden'}">
													<i class="fa fa-arrow-up"></i>
												</a>
												<a href="javascript:" @click="changeOrder(often.oftenUsedSeq, oftenUsedList[index + 1].oftenUsedSeq)" :style="{visibility: isDownable(index) ? '' : 'hidden'}">
													<i class="fa fa-arrow-down"></i>
												</a>
											</td>
											<td>
												<a href="javascript:" @click="openOften('edit', often)">
													<i class="fa fa-edit"></i>
												</a>
												<a href="javascript:" @click="deleteOftenForm(often.oftenUsedSeq)">
													<i class="fa fa-remove"></i>
												</a>
											</td>
										</tr>
									</tbody>
								</table>
							</div>
							<div class="x_content">
								<button type="button" class="btn btn-primary btn-xs" @click="openOftenAdd()">
									자주쓰는
									거래 저장
								</button>
							</div>
						</div>
					</form>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-info" @click="addAction(true)" v-show="this.actionType == 'add'">계속입력</button>
					<button type="button" class="btn btn-info" @click="addAction(false)">저장</button>
					<button type="button" class="btn btn-default" @click="close()">닫기</button>
				</div>
			</div>
		</div>
		<div>
			<often/>
		</div>
		<div>
			<category/>
		</div>
	</div>
</template>
<script type="text/javascript">
import moment from "moment";
import "daterangepicker";
import 'daterangepicker/daterangepicker.css';
import 'jquery-ui/ui/core';
import 'jquery-ui/ui/widgets/autocomplete.js'
import 'jquery-ui/themes/base/all.css'

import categoryComponent from './transactionCategory.vue';
import oftenComponent from './transactionOften.vue';
import VueUtil from "../../js/vue-util.js";

export default {
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
			// 모달 창 닫을 시 부모 페이지를 리로딩 할 지 여부
			closeReload: false,
			// 추천 카테고리
			recommendCategories: [],
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
		// 수수료 비활성
		disableFee() {
			return this.item.kind == "INCOME" || this.item.kind == "SPENDING";
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
		updateDate(d) {
			this.item.transactionDate = d;
		},
		// 현재 날짜 조정
		addDate(diff) {
			this.selectDate.add(diff, "days");
			this.item.transactionDate = this.selectDate.format("YYYY-MM-DD");
			$('._datepicker').data('daterangepicker').setStartDate(this.selectDate.format("YYYY-MM-DD"))
		},
		// 계좌 입력 팝업창.
		openForm(kind) {
			this.item.kind = kind;
			const ITEM_TYPE_ATTR = { INCOME: 'ATTR_INCOME', SPENDING: 'ATTR_SPENDING', TRANSFER: 'ATTR_TRANSFER' }
			this.loadAttribute(ITEM_TYPE_ATTR[this.item.kind]);
			this.loadOftenUsed();
			this.closeReload = false;

			$('._datepicker').daterangepicker({
				singleDatePicker: true,
				singleClasses: "",
				showDropdowns: true,
				startDate: this.selectDate.format("YYYY-MM-DD")
			}, (start) => {
				console.log('start.format("YYYY-MM-DD") :', start.format("YYYY-MM-DD"));
				this.item.transactionDate = start.format("YYYY-MM-DD");
			});
			this.$validator.reset();

			$('#addItem').off().on('shown.bs.modal', () => {
				$("._note").focus();
			});
			$('#addItem').off().on('hidden.bs.modal', () => {
				if (this.closeReload) {
					this.$EventBus.$emit('reloadEvent');
				}
			});

			$("#addItem").modal();

			// 메모 입력시 관련 카테고리 추천
			$("._note").autocomplete({
				source: (request, response) => {
					let note = request.term;
					VueUtil.get("/category/listRecommend.json", { note: note, kind: this.item.kind }, (result) => {
						response(result.data);
					}, { waitDialog: false });
				},
				focus: () => false,
				select: (event, ui) => {
					this.insertCategory(ui.item.parentCategory, ui.item);
					return false;
				},
			}).data("ui-autocomplete")._renderItem = function (ul, item) {
				return $("<li>")
					.append("<div>" + item.parentCategory.name + " > " + item.name + "</div>")
					.appendTo(ul);
			};
		},
		// 등록 또는 수정
		// cont. true: 연속입력, false: 입력후 모달 닫기
		addAction(cont) {
			this.$validator.validateAll().then((result) => {
				if (!result) {
					return;
				}
				this.beforeTransaction[this.item.kind] = $.extend(true, {}, this.item);

				delete this.item.category;
				delete this.item.parentCategory;

				let url = this.actionType == 'add' ? '/transaction/add.do' : '/transaction/edit.do'
				VueUtil.post(url, this.item, (result) => {
					this.closeReload = true;
					if (cont && this.actionType == "add") {
						this.item.note = "";
						this.item.money = "";
						// 포커스가 제대로 안되서 timeout 적용. $nextTick 안됨.
						setTimeout(() => {
							$("._note").focus();
						}, 100);
					} else {
						$("#addItem").modal('hide');
					}
				});
			});
		},
		close() {
			$("#addItem").modal('hide');
		},
		// 계좌 목록
		loadAccount() {
			VueUtil.get("/account/list.json", {}, (result) => {
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
			VueUtil.get("/oftenUsed/list.json", { kind: this.item.kind }, (result) => {
				this.oftenUsedList = result.data;
			});
		},
		// 항목 선택 팝업.
		openCategoryList(kind) {
			this.$EventBus.$emit('openCategoryListEvent', kind, 'add');
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
			if (this.item.money == 0) {
				this.item.money = "";
			}
			this.insertCategory(often.parentCategory, often.category);
			// $('#payAccountList,#receiveAccountList').inputpicker('destroy');
		},
		// 자주쓰는 거래 팝업 열기
		// actionType: add, edit
		// often: 거래 내역항목
		openOften(actionType, often) {
			this.$EventBus.$emit('openOftenEvent', actionType, $.extend(true, {}, often));
		},
		// 자주 쓰는 거래 신규 등록
		// 현재 입력한 값을 전달
		openOftenAdd() {
			let copyItem = $.extend(true, {}, this.item)
			if (!copyItem.categorySeq) {
				this.openOften('add', copyItem);
				return;
			}
			VueUtil.get("/category/getCategory.json", { categorySeq: this.item.categorySeq }, (result) => {
				if (result.data) {
					copyItem.category = result.data;
					copyItem.parentCategory = result.data.parentCategory;
				}
				delete copyItem.oftenUsedSeq;
				this.openOften('add', copyItem);
			});
		},
		// 정렬 순서 변경
		changeOrder(downOftenUsedSeq, upOftenUsedSeq) {
			let param = { downOftenUsedSeq: downOftenUsedSeq, upOftenUsedSeq: upOftenUsedSeq };
			VueUtil.post('/oftenUsed/changeOrder.do', param, (result) => {
				this.loadOftenUsed();
			});
		},
		// 자주 쓰는 거래 삭제
		deleteOftenForm(oftenUsedSeq) {
			if (!confirm("삭제할거야?")) {
				return;
			}
			let param = { oftenUsedSeq: oftenUsedSeq };
			VueUtil.post('/oftenUsed/delete.do', param, (result) => {
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
		this.$EventBus.$on('addFormEvent', this.addForm);
		this.$EventBus.$on('editFormEvent', this.editForm);
		this.$EventBus.$on('insertCategoryEvent', this.insertCategory);
		this.$EventBus.$on('listOftenUsedEvent', this.loadOftenUsed);

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
};
</script>