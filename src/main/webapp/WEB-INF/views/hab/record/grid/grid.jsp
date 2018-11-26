<%@ page contentType="text/html; charset=UTF-8"%>
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

						<div class="form-inline" action="/action_page.php">
							<div class="form-group">
								<label for="memo_field">메모:</label>
								<input type="text" class="form-control" id="memo_field">
							</div>
							<div class="checkbox">
								<label>
									<input type="checkbox" value="INCOME" class="flat" v-model="condition.kindFilter"> 지출
								</label>
								<label>
									<input type="checkbox" value="SPENDING" class="flat" v-model="condition.kindFilter"> 수입
								</label>
								<label>
									<input type="checkbox" value="TRANSFER" class="flat" v-model="condition.kindFilter"> 이체
								</label>
							</div>
							<button type="submit" class="btn btn-info" style="margin: 0">검색</button>
							<button type="submit" class="btn btn-success" style="margin: 0;float: right">내보내기(엑셀)</button>
						</div>


						<table class="table table-striped jambo_table bulk_action table-bordered" id="grid">
							<thead>
								<tr class="headings">
									<th>유형</th>
									<th>메모</th>
									<th>대분류</th>
									<th>소분류</th>
									<th>출금액</th>
									<th>입금액</th>
									<th>수수료</th>
									<th>출금계좌</th>
									<th>일금계좌</th>
								</tr>
							</thead>
							<tbody>
								<tr>
									<td>1유형</td>
									<td>1메모</td>
									<td>대분류</td>
									<td>소분류</td>
									<td>출금액</td>
									<td>입금액</td>
									<td>수수료</td>
									<td>출금계좌</td>
									<td>일금계좌</td>
								</tr>
								<tr>
									<td>2유형</td>
									<td>2메모</td>
									<td>대분류</td>
									<td>소분류</td>
									<td>출금액</td>
									<td>입금액</td>
									<td>수수료</td>
									<td>출금계좌</td>
									<td>일금계좌</td>
								</tr>
								<tr>
									<td>3유형</td>
									<td>3메모</td>
									<td>대분류</td>
									<td>소분류</td>
									<td>출금액</td>
									<td>입금액</td>
									<td>수수료</td>
									<td>출금계좌</td>
									<td>일금계좌</td>
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
									<button type="button" class="btn btn-primary">저번달</button>
									<button class="btn btn-primary" type="reset">이번달</button>
									<button type="submit" class="btn btn-success">다음달</button>
								</div>
								<div class="form-group"></div>
								<div class="ln_solid" style="margin:10px 0;"></div>
							</div>
							<div>
								<h4>{{from | dateFormat('YYYY.MM.DD')}} ~ {{to | dateFormat('YYYY.MM.DD')}} 결산</h4>
								<table class="table table-bordered">
									<tbody>
										<tr>
											<td>수입</td>
											<td class="text-right">1,000</td>
										</tr>
										<tr>
											<td>지출</td>
											<td class="text-right">536,165</td>
										</tr>
										<tr>
											<td>수입 - 지출</td>
											<td class="text-right">-535,165</td>
										</tr>
										<tr>
											<td>이체</td>
											<td class="text-right">20,000</td>
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
	<div>
		<memo />
	</div>
</div>

<jsp:include page="../record_add.inc.jsp"></jsp:include>
<jsp:include page="../memo.inc.jsp"></jsp:include>

<script type="text/javascript">
	Vue.use(VeeValidate, {
		locale: 'ko',
		events: 'blur',
	});
	let EventBus = new Vue();

	const NOW_DATE = new Date();
	// vue 객체 생성
	const app = new Vue({
		data: function () {
			return {
				// 검색 조건
				condition: {
					kindFilter: ["INCOME", "SPENDING", "TRANSFER"],
					fromDate: moment([NOW_DATE.getFullYear(), NOW_DATE.getMonth()]).format("YYYY-MM-DD"),
					toDate: moment().format("YYYY-MM-DD"),
				}
			};
		},
		components: {
			'add': itemAddComponent,
			'memo': memoComponent
		},
		computed: {
		},
		methods: {
			// UI 객체 초기화
			initUi() {
				let self = this;

				$('#grid').DataTable({ paging: false, bInfo: false, searching: false });
				$('input.flat').iCheck({
					checkboxClass: 'icheckbox_flat-green',
				});

				let kind = self.condition.kindFilter;
				$('input.flat').on('ifChecked', function (e) {
					kind.push($(this).val());
				});
				$('input.flat').on('ifUnchecked', function (e) {
					kind.splice(kind.indexOf($(this).val()), 1)
				});

				$('._datepicker_from').daterangepicker({
					singleDatePicker: true,
					singleClasses: "",
					startDate: this.condition.fromDate
				}, (from) => {
					this.condition.fromDate = from.format("YYYY-MM-DD");
				});

				$('._datepicker_to').daterangepicker({
					singleDatePicker: true,
					singleClasses: "",
					startDate: this.condition.toDate
				}, (to) => {
					this.condition.toDate = to.format("YYYY-MM-DD");
				});
			}
		},
		mounted() {
			this.initUi();
		}
	}).$mount('#app');
</script>