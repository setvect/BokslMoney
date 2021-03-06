<template>
	<div>
		<div class="page-title">
			<div class="title_left">
				<h3>결산</h3>
			</div>
		</div>
		<div class="clearfix"></div>
		<div class="row">
			<div class="col-md-12 col-sm-12 col-xs-12">
				<div class="x_panel">
					<div class="form-inline" style="padding: 0 10px;">
						<div class="form-group">
							<select class="form-control" v-model="yearChoice">
								<option value>--결산 년도 선택--</option>
								<option :value="year" v-for="year in yearList" :key="year">{{year}}년</option>
							</select>
						</div>
						<button type="submit" class="btn btn-default" style="margin: 0" @click="runSettlement()">조회</button>
						<button type="button" class="btn btn-success" style="margin: 0;float: right" @click="exportExcel();">내보내기(엑셀)</button>
					</div>

					<div class="col-md-12 col-sm-12 col-xs-12">
						<table class="table jambo_table bulk_action table-bordered" id="grid">
							<thead>
								<tr class="headings">
									<th class="text-center">항목</th>
									<th class="text-center" v-for="month in monthList" :key="month.valueOf()">{{month | dateFormat("YYYY년 MM월")}}</th>
								</tr>
							</thead>
							<tbody>
								<tr v-for="spending in spendingList" :key="spending.categorySeq">
									<td>{{spending.name}}</td>
									<td class="text-right" v-for="month in monthList" :key="month.valueOf()" @click="viewList(month, spending.kind, spending.categorySeq)">
										{{getSpending(month.month(),
										spending.categorySeq) | numberFormat}}
									</td>
								</tr>
								<tr class="info" v-for="kindMap in kindList" :key="Object.keys(kindMap)[0]">
									<td>{{Object.values(kindMap)[0]}}</td>
									<td
										class="text-right"
										v-for="month in monthList"
										:key="month.valueOf()"
										@click="viewList(month, Object.keys(kindMap)[0],0)"
									>{{getKindSum(month.month(), Object.keys(kindMap)[0])| numberFormat}}</td>
								</tr>
								<tr class="success">
									<td>수입-지출</td>
									<td
										class="text-right"
										v-for="month in monthList"
										:key="month.valueOf()"
									>{{getKindSum(month.month(), 'INCOME') - getKindSum(month.month(), 'SPENDING') | numberFormat}}</td>
								</tr>
							</tbody>
						</table>
					</div>
				</div>
			</div>
		</div>
		<record-list ref="recordListPopup"></record-list>
	</div>
</template>

<script type="text/javascript">
import moment from "moment";

import VueUtil from "../../js/vue-util.js";
import { TransactionMixin, AppUtil, TYPE_VALUE } from "../../js/bokslmoney.js";
import "../../js/vue-common.js";
import recordListComponent from "./recordList.vue";

const currentYear = new Date().getFullYear();

// vue 객체 생성
export default {
	data: function() {
		return {
			spendingList: [],
			year: currentYear,
			yearChoice: currentYear,
			spendingGroupSum: {},
			kindGroupSum: {},
			gridTable: null
		};
	},
	components: {
		recordList: recordListComponent
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
				months.push(start.clone().add(m, "month"));
			}
			return months;
		},
		// 유형 리스트
		kindList() {
			return [
				{ SPENDING: "지출합계" },
				{ INCOME: "수입합계" },
				{ TRANSFER: "이체합계" }
			];
		}
	},
	methods: {
		initGrid() {
			this.gridTable = $("#grid").DataTable({
				paging: false,
				bInfo: false,
				searching: false,
				ordering: false,
				dom: "Bfrtip",
				buttons: [
					{
						extend: "excelHtml5",
						title: "복슬머니 결산(" + this.yearChoice + ")",
						customize: function(xlsx) {
							var sheet = xlsx.xl.worksheets["sheet1.xml"];
							$("row c", sheet).attr("s", "25");
						}
					}
				]
			});
			// 엑셀 다운로드 button 감추기
			$(".buttons-excel").hide();
		},
		destroyGrid() {
			if (this.gridTable != null) {
				this.gridTable.destroy();
			}
		},
		// 리스트
		listSpending() {
			let param = { kind: "SPENDING", parent: 0 };
			VueUtil.get("/category/list.json", param, result => {
				this.spendingList = result.data;
			});
		},
		// 결산
		runSettlement() {
			this.year = this.yearChoice;
			VueUtil.get(
				"/settlement/groupOfMonth.json",
				{ year: this.year, kind: "SPENDING" },
				result => {
					this.spendingGroupSum = result.data;
					VueUtil.get(
						"/settlement/groupKindOfMonth.json",
						{ year: this.year },
						result => {
							this.kindGroupSum = result.data;
							this.$nextTick(() => {
								this.destroyGrid();
								this.initGrid();
							});
						}
					);
				}
			);
		},
		// 선택항목 거래 내역 조회
		// month: mement 객체
		viewList(month, kind, categorySeq) {
			let from = month.clone();
			let to = month
				.clone()
				.add("month", 1)
				.add("second", -1);
			console.log("kind :", kind);

			this.$refs.recordListPopup.openForm(from, to, kind, categorySeq);
		},
		// month: 0부터 시작,
		// categorySeq: 대분류 아이디
		getSpending(month, categorySeq) {
			let monthGroup = this.spendingGroupSum[month];
			if (!monthGroup) {
				return 0;
			}
			return monthGroup[categorySeq] || 0;
		},
		// month: 0부터 시작,
		// kind: 유형
		getKindSum(month, kind) {
			let monthGroup = this.kindGroupSum[month];
			if (!monthGroup) {
				return 0;
			}
			return monthGroup[kind] || 0;
		},
		// 엑셀 다운로드
		exportExcel() {
			// datatables에 있는 버튼 클릭
			$(".buttons-excel").trigger("click");
		}
	},
	mounted() {
		this.listSpending();
		this.runSettlement();
	}
};
</script>