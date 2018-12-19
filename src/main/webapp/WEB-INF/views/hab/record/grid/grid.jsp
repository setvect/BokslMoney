<%@ page contentType="text/html; charset=UTF-8"%>
<style>
	#grid tr td{vertical-align: middle;}
</style>
<div id="app">
	<div class="page-title">
		<div class="title_left">
			<h3>표 입력</h3>
		</div>
	</div>
	<div class="clearfix"></div>
	<div class="row">

		<div class="col-md-12 col-sm-12 col-xs-12">
			<div class="x_panel">
				<div v-cloak>
					<div class="col-md-9 col-sm-9 col-xs-12">

						<div class="form-inline">
							<div class="form-group">
								<label for="memo_field">메모:</label>
								<input type="text" class="form-control" id="memo_field" v-model="condition.note" @keyup.13="search()">
							</div>
							<div class="checkbox">
								<label>
									<input type="checkbox" value="SPENDING" class="flat" v-model="condition.kindTypeSet"> 지출
								</label>
								<label>
									<input type="checkbox" value="INCOME" class="flat" v-model="condition.kindTypeSet"> 수입
								</label>
								<label>
									<input type="checkbox" value="TRANSFER" class="flat" v-model="condition.kindTypeSet"> 이체
								</label>
							</div>
							<button type="button" class="btn btn-info" style="margin: 0" @click="search();">검색</button>
							<button type="button" class="btn btn-success" style="margin: 0;float: right" @click="exportExcel();">내보내기(엑셀)</button>
						</div>


						<table class="table table-striped jambo_table bulk_action table-bordered" id="grid">
							<thead>
								<tr class="headings">
									<th>No</th>
									<th>유형</th>
									<th>메모</th>
									<th>대분류</th>
									<th>소분류</th>
									<th>금액</th>
									<th>수수료</th>
									<th>출금계좌</th>
									<th>일금계좌</th>
									<th>날짜</th>
									<th>기능</th>
								</tr>
							</thead>
							<tbody>
								<tr v-for="(item, idx) in transactionList" class="">
									<td>{{idx + 1}}</td>
									<td :style="{color:getKindAttr(item.kind).color}" style="font-weight: bold">{{getKindAttr(item.kind).title}}</td>
									<td>{{item.note}}</td>
									<td>{{item.parentCategory.name}}</td>
									<td>{{item.category.name}}</td>
									<td class="text-right">{{item.money | numberFormat}}</td>
									<td class="text-right">{{item.fee | numberFormat}}</td>
									<td>{{item.payAccount | accountName}}&nbsp;</td>
									<td>{{item.receiveAccount | accountName}}&nbsp;</td>
									<td>{{item.transactionDate | dateFormat('YYYY.MM.DD')}}</td>
									<td class="text-center">
										<div class="btn-group  btn-group-xs">
											<button type="button" class="btn btn-success btn-xs" @click="editForm(item)">수정</button>
											<button type="button" class="btn btn-dark btn-xs" @click="deleteAction(item)">삭제</button>
										</div>
									</td>
								</tr>
							</tbody>
						</table>
					</div>
					<div class="col-md-3 col-sm-3 col-xs-12">
						<div class="page-header">
							<button type="button" data-type="SPENDING" class="btn btn-info _input">지출</button>
							<button type="button" data-type="INCOME" class="btn btn-info _input">수입</button>
							<button type="button" data-type="TRANSFER" class="btn btn-info _input">이체</button>
						</div>
						<div>
							<div class="form-horizontal">
								<div class="form-group">
									<label class="control-label col-sm-3" for="start_date">시작일:</label>
									<div class="col-sm-9">
										<input type="input" class="form-control _datepicker_from" readonly="readonly" placeholder="Enter start_date"
										 name="start_date">
									</div>
								</div>
								<div class="form-group">
									<label class="control-label col-sm-3" for="end_date">종료일:</label>
									<div class="col-sm-9">
										<input type="input" class="form-control _datepicker_to" readonly="readonly" placeholder="Enter end_date" name="end_date">
									</div>
								</div>
								<div class="col-md-9 col-sm-9 col-xs-12 col-md-offset-3">
									<button type="button" class="btn btn-info btn-sm" @click="search();">조회</button>
									<button type="button" class="btn btn-primary btn-sm" @click="moveMonth(-1)">이전달</button>
									<button type="button" class="btn btn-primary btn-sm" @click="moveMonth(0)">이번달</button>
									<button type="button" class="btn btn-primary btn-sm" @click="moveMonth(1)">다음달</button>
								</div>
								<div class="form-group"></div>
								<div class="ln_solid" style="margin:10px 0;"></div>
							</div>
							<div>
								<h4>{{condition.from}} ~ {{condition.to}} 결산</h4>
								<table class="table table-bordered">
									<tbody>
										<tr>
											<td><span :style="{color:getKindAttr('INCOME').color}" style="line-height: normal ">수입</span></td>
											<td class="text-right">{{sumIncome | numberFormat}}</td>
										</tr>
										<tr>
											<td><span :style="{color:getKindAttr('SPENDING').color}" style="line-height: normal ">지출</span></td>
											<td class="text-right">{{sumSpending | numberFormat}}</td>
										</tr>
										<tr>
											<td><span :style="{color:getKindAttr('INCOME').color}" style="line-height: normal ">수입</span> - <span :style="{color:getKindAttr('SPENDING').color}"
												 style="line-height: normal ">지출</span></td>
											<td class="text-right">{{sumIncome - sumSpending | numberFormat}}</td>
										</tr>
										<tr>
											<td><span :style="{color:getKindAttr('TRANSFER').color}" style="line-height: normal ">이체</span></td>
											<td class="text-right">{{sumTransfer | numberFormat}}</td>
										</tr>
									</tbody>
								</table>
							</div>


						</div>
					</div>
				</div>
			</div>
		</div>

	</div>
	<div>
		<add />
	</div>
</div>

<jsp:include page="../record_add.inc.jsp"></jsp:include>

<script type="text/javascript">
	Vue.use(VeeValidate, {
		locale: 'ko',
		events: 'blur',
	});
	let EventBus = new Vue();

	const NOW_DATE = new Date();
	// vue 객체 생성
	const app = new Vue({
		mixins: [TransactionMixin],

		data: function () {
			return {
				// 검색 조건
				condition: {
					kindTypeSet: ["INCOME", "SPENDING", "TRANSFER"],
					from: moment([NOW_DATE.getFullYear(), NOW_DATE.getMonth()]).format("YYYY-MM-DD"),
					to: moment().format("YYYY-MM-DD"),
					note: "",
				},
				gridTable: null,
			};
		},
		components: {
			'add': itemAddComponent,
		},
		computed: {
		},
		methods: {
			// UI 객체 초기화
			initUi() {
				let self = this;

				$('input.flat').iCheck({
					checkboxClass: 'icheckbox_flat-green',
				});

				let kind = self.condition.kindTypeSet;
				$('input.flat').on('ifChecked', function (e) {
					kind.push($(this).val());
				});
				$('input.flat').on('ifUnchecked', function (e) {
					kind.splice(kind.indexOf($(this).val()), 1)
				});

				this.initDatepicker();
			},
			// datepicker 선택
			initDatepicker() {
				$('._datepicker_from').daterangepicker({
					singleDatePicker: true,
					singleClasses: "",
					showDropdowns: true,
					startDate: this.condition.from
				}, (from) => {
					this.condition.from = from.format("YYYY-MM-DD");
				});

				$('._datepicker_to').daterangepicker({
					singleDatePicker: true,
					singleClasses: "",
					showDropdowns: true,
					startDate: this.condition.to
				}, (to) => {
					this.condition.to = to.format("YYYY-MM-DD");
				});
			},
			initGrid() {
				this.gridTable = $('#grid').DataTable({
					paging: false, bInfo: false, searching: false,
					dom: 'Bfrtip',
					buttons: [{
						extend: 'excelHtml5',
						exportOptions: {
							columns: [0, 1, 2, 3, 4, 5, 6, 7, 8, 9]
						},
						title: '복슬머니(' + this.condition.from + '_' + this.condition.to + ')',
						customize: function (xlsx) {
							var sheet = xlsx.xl.worksheets['sheet1.xml'];
							$('row c', sheet).attr('s', '25');
						}
					}]
				});
				// 엑셀 다운로드 button 감추기
				$(".buttons-excel").hide();
			},
			destroyGrid() {
				if (this.gridTable != null) {
					this.gridTable.destroy();
				}
			},
			//  거래 내역 다시 조회
			reload() {
				this.search();
			},
			// 거래내역 조회
			loadTransaction() {
				VueUtil.get("/hab/transaction/listByRange.json", this.condition, (result) => {
					this.destroyGrid();
					this.transactionList = result.data;
					this.$nextTick(() => {
						this.initGrid();
					});
				}, { paramParsing: true })
			},
			// 검색
			search() {
				this.loadTransaction();
			},
			// 달 이동
			moveMonth(diff) {
				let fromDate = moment(this.condition.from, 'YYYY-MM-DD');
				fromDate.add(diff, "months");
				if (diff == 0) {
					fromDate = moment();
				}

				fromDate.date(1);
				let toDate = fromDate.clone();
				toDate.add(1, "months").add(-1, 'Days');

				this.condition.from = fromDate.format("YYYY-MM-DD");
				this.condition.to = toDate.format("YYYY-MM-DD");
				this.initDatepicker();
				this.search();
			},
			// 엑셀 다운로드
			exportExcel() {
				// datatables에 있는 버튼 클릭
				$(".buttons-excel").trigger("click");
			}
		},
		mounted() {
			this.initUi();
			this.loadTransaction();
			// 지출, 이체, 수입 버튼 클릭
			$("._input").click((event) => {
				let type = $(event.target).attr("data-type");
				this.addItemForm(type);
			});
			EventBus.$on('reloadEvent', this.search);
		}
	}).$mount('#app');
</script>