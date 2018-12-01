<%@ page contentType="text/html; charset=UTF-8"%>
<div id="app">
	<div class="page-title">
		<div class="title_left">
			<h3>결산</h3>
		</div>
	</div>
	<div class="clearfix"></div>
	<div class="row">
		<div class="col-md-12 col-sm-12 col-xs-12">

			<div class="x_panel">
				<div class="form-inline" style="margin-bottom: 10px; padding-left: 10px;">
					<div class="form-group">
						<select class="form-control" v-model="yearChoice">
							<option value="">--결산 년도 선택--</option>
							<option :value="year" v-for="year in yearList">{{year}}년</option>
						</select>
					</div>
					<button type="submit" class="btn btn-default" style="margin: 0" @click="runSettlement()">조회</button>
				</div>

				<div class="col-md-12 col-sm-12 col-xs-12">
					<table class="table jambo_table bulk_action table-bordered" id="grid">
						<thead>
							<tr class="headings">
								<th class="text-center">항목</th>
								<th class="text-center" v-for="month in monthList">{{month | dateFormat("YYYY년 MM월")}}</th>
							</tr>
						</thead>
						<tbody>
							<tr v-for="spending in spendingList">
								<td>{{spending.name}}</td>
								<td class="text-right">10,000</td>
								<td class="text-right">15,000</td>
								<td class="text-right">10,000</td>
								<td class="text-right">15,000</td>
								<td class="text-right">10,000</td>
								<td class="text-right">15,000</td>
								<td class="text-right">10,000</td>
								<td class="text-right">15,000</td>
								<td class="text-right">10,000</td>
								<td class="text-right">15,000</td>
								<td class="text-right">10,000</td>
								<td class="text-right">15,000</td>
							</tr>
							<tr class="active">
								<td>지출합계</td>
								<td class="text-right">10,000</td>
								<td class="text-right">15,000</td>
								<td class="text-right">10,000</td>
								<td class="text-right">15,000</td>
								<td class="text-right">10,000</td>
								<td class="text-right">15,000</td>
								<td class="text-right">10,000</td>
								<td class="text-right">15,000</td>
								<td class="text-right">10,000</td>
								<td class="text-right">15,000</td>
								<td class="text-right">10,000</td>
								<td class="text-right">15,000</td>
							</tr>
							<tr class="active">
								<td>수입합계</td>
								<td class="text-right">10,000</td>
								<td class="text-right">15,000</td>
								<td class="text-right">10,000</td>
								<td class="text-right">15,000</td>
								<td class="text-right">10,000</td>
								<td class="text-right">15,000</td>
								<td class="text-right">10,000</td>
								<td class="text-right">15,000</td>
								<td class="text-right">10,000</td>
								<td class="text-right">15,000</td>
								<td class="text-right">10,000</td>
								<td class="text-right">15,000</td>
							</tr>
							<tr class="active">
								<td>이체합계</td>
								<td class="text-right">10,000</td>
								<td class="text-right">15,000</td>
								<td class="text-right">10,000</td>
								<td class="text-right">15,000</td>
								<td class="text-right">10,000</td>
								<td class="text-right">15,000</td>
								<td class="text-right">10,000</td>
								<td class="text-right">15,000</td>
								<td class="text-right">10,000</td>
								<td class="text-right">15,000</td>
								<td class="text-right">10,000</td>
								<td class="text-right">15,000</td>
							</tr>
							<tr class="success">
								<td>수입-지출</td>
								<td class="text-right">10,000</td>
								<td class="text-right">15,000</td>
								<td class="text-right">10,000</td>
								<td class="text-right">15,000</td>
								<td class="text-right">10,000</td>
								<td class="text-right">15,000</td>
								<td class="text-right">10,000</td>
								<td class="text-right">15,000</td>
								<td class="text-right">10,000</td>
								<td class="text-right">15,000</td>
								<td class="text-right">10,000</td>
								<td class="text-right">15,000</td>
							</tr>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>
</div>

<script type="text/javascript">
	const currentYear = (new Date()).getFullYear();
	Vue.use(VeeValidate, {
		locale: 'ko',
		events: 'blur',
	});
	let EventBus = new Vue();
	// vue 객체 생성
	const app = new Vue({
		data: function () {
			return {
				spendingList: [],
				year: currentYear,
				yearChoice: currentYear
			};
		},
		computed: {
			yearList() {
				let years = [];
				let d = new Date();
				for (let y = 2007; y <= d.getFullYear(); y++) {
					years.push(y);
				}
				return years.reverse();
			},
			monthList() {
				let start = moment([this.year, 0, 1]);
				let months = [];
				for (let m = 0; m < 12; m++) {
					months.push(start.clone().add(m, 'month'));
				}
				return months;
			}
		},
		methods: {
			// 리스트
			listSpending() {
				let param = { kind: "SPENDING", parent: 0 };
				VueUtil.get("/hab/category/list.json", param, (result) => {
					this.spendingList = result.data;
				});
			},
			// 결산
			runSettlement() {
				this.year = this.yearChoice;
				VueUtil.get("/hab/settlement/statSpending.json", { year: this.year }, (result) => {
					this.spendingList = result.data;
				});

			}
		},
		mounted() {
			this.listSpending();
		}
	}).$mount('#app');
</script>