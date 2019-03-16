<template>
	<div id="recordListModal" class="modal fade" role="dialog">
		<div class="modal-dialog modal-lg">
			<!-- Modal content-->
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">&times;</button>
					<h4 class="modal-title">
						{{condition.from}} ~ {{condition.to}} {{getKindAttr(kindType).title}} 내역 (총: {{sum |
						numberFormat}})
					</h4>
				</div>
				<div class="modal-body" style="max-height: 450px; overflow: auto;">
					<table class="table table-striped jambo_table bulk_action table-bordered" id="grid-pop">
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
							</tr>
						</thead>
						<tbody>
							<tr v-for="(item, idx) in transactionList" :key="idx">
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
							</tr>
						</tbody>
					</table>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-success" @click="exportExcel();">내보내기(엑셀)</button>
					<button type="button" class="btn btn-default" data-dismiss="modal">닫기</button>
				</div>
			</div>
		</div>
	</div>
</template>

<script type="text/javascript">
import moment from "moment";
import _ from 'lodash';
import "datatables";
import "datatables.net-buttons";
import "datatables.net-buttons/js/buttons.html5.js";


import VueUtil from "../../js/vue-util.js";
import { TransactionMixin, AppUtil, TYPE_VALUE } from "../../js/bokslmoney.js";
import "../../js/vue-common.js";

// vue 객체 생성
export default {
	mixins: [TransactionMixin],
	data: function() {
		return {
			// 검색 조건
			condition: {
				kindTypeSet: [],
				from: moment(),
				to: moment(),
				categorySeq: 0,
				note: ""
			},
			kindType: "SPENDING",
			gridTable: null
		};
	},
	computed: {
		sum() {
			let sum = _.sumBy(this.transactionList, t => t.money);
			return sum;
		}
	},
	methods: {
		initGrid() {
			this.gridTable = $("#grid-pop").DataTable({
				paging: false,
				bInfo: false,
				searching: false,
				dom: "Bfrtip",
				buttons: [
					{
						extend: "excelHtml5",
						exportOptions: {
							columns: [0, 1, 2, 3, 4, 5, 6, 7, 8, 9]
						},
						title:
							"복슬머니(" + this.condition.from + "_" + this.condition.to + ")",
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
		//  거래 내역 다시 조회
		reload() {
			this.search();
		},
		// 거래내역 조회
		loadTransaction() {
			VueUtil.get(
				"/transaction/listByRange.json",
				this.condition,
				result => {
					this.destroyGrid();
					this.transactionList = result.data;
					this.$nextTick(() => {
						this.initGrid();
					});
				},
				{ paramParsing: true }
			);
		},
		openForm(from, to, kind, categorySeq) {
			this.condition.kindTypeSet = [];

			this.condition.from = from.format("YYYY-MM-DD");
			this.condition.to = to.format("YYYY-MM-DD");
			this.condition.kindTypeSet.push(kind);
			this.condition.categorySeq = categorySeq;

			this.kindType = kind;
			this.loadTransaction();
			$("#recordListModal").modal();
		},
		// 엑셀 다운로드
		exportExcel() {
			// datatables에 있는 버튼 클릭
			$(".buttons-excel").trigger("click");
		}
	},
	mounted() {}
};
// <style>
// .modal-lg {
// 	width: 80%;
// 	height: 100%;
// }
// </style>
</script>
