<template>
	<div class="col-md-12 col-sm-12 col-xs-12">
		<div class="form-row">
			<div class="form-group col-md-4">
				<label for="inputCity">재산(내가 모은 돈)</label>
				<span class="form-control text-right">
					{{property
					| numberFormat}}
				</span>
			</div>
			<div class="form-group col-md-4">
				<label for="inputCity">자산(마이너스가 아닌 계좌 합)</label>
				<span class="form-control text-right">
					{{asset
					| numberFormat}}
				</span>
			</div>
			<div class="form-group col-md-4">
				<label for="inputCity">부채(마이너스 계좌 합)</label>
				<span class="form-control text-right">
					{{debt
					| numberFormat}}
				</span>
			</div>
		</div>
		<table class="table table-striped jambo_table bulk_action table-bordered" id="grid">
			<thead>
				<tr class="headings">
					<th>자산종류</th>
					<th>이름</th>
					<th>잔고</th>
					<th>이율</th>
					<th>계좌(카드)번호</th>
					<th>월 납입액</th>
					<th>만기일</th>
					<th>메모</th>
				</tr>
			</thead>
			<tbody>
				<tr v-for="(item) in itemList" :key="item.accountSeq" @click="readForm(item)" style="cursor: pointer">
					<td>{{item.kindName}}</td>
					<td>{{item.name}}</td>
					<td class="text-right">{{item.balance | numberFormat}}</td>
					<td>{{item.interestRate}}</td>
					<td>{{item.accountNumber}}</td>
					<td>{{item.monthlyPay}}</td>
					<td>{{item.expDate}}</td>
					<td class="td-ell">{{item.note}}</td>
				</tr>
			</tbody>
		</table>
		<div>
			<button type="button" class="btn btn-success" @click="addForm()">추가</button>
			<button type="button" class="btn btn-success" style="margin: 0;float: right" @click="exportExcel();">내보내기(엑셀)</button>
		</div>
	</div>
</template>

<script type="text/javascript">
import vueUtil from "../../js/vue-util.js";
import  "../../js/vue-common.js";

export default{
	data() {
		return {
			itemList: [],
			gridTable: null,
			// 정렬 조건 유지하기 위함
			order: [0, "asc"]
		};
	},
	props: {},
	computed: {
		property() {
			return this.asset + this.debt;
		},
		asset() {
			let value = this.itemList.reduce(function(acc, item) {
				if (item.balance > 0) {
					return acc + item.balance;
				}
				return acc;
			}, 0);
			return value;
		},
		debt() {
			let value = this.itemList.reduce(function(acc, item) {
				if (item.balance < 0) {
					return acc + item.balance;
				}
				return acc;
			}, 0);
			return value;
		}
	},
	methods: {
		// 리스트
		list() {
			vueUtil.get("/account/list.json", {}, result => {
				this.itemList = result.data;
				this.$nextTick(() => {
					if (this.gridTable != null) {
						this.gridTable.destroy();
					}
					this.gridTable = $("#grid").DataTable({
						paging: false,
						bInfo: false,
						searching: false,
						dom: "Bfrtip",
						buttons: [
							{
								extend: "excelHtml5",
								title: "복슬머니 계좌목록",
								customize: function(xlsx) {
									var sheet = xlsx.xl.worksheets["sheet1.xml"];
									$("row c", sheet).attr("s", "25");
								}
							}
						]
					});

					this.gridTable.order(this.order).draw();
					$("#grid").on("order.dt", () => {
						if (this.gridTable.order().length == 0) {
							return;
						}
						this.order = this.gridTable.order()[0];
					});

					// 엑셀 다운로드 button 감추기
					$(".buttons-excel").hide();
				});
			});
		},
		readForm(item) {
			this.$EventBus.$emit("readFormEvent", item);
		},
		// 등록 폼
		addForm() {
			this.$EventBus.$emit("addFormEvent", {});
		},
		// 수정폼
		editForm(item) {},
		// 엑셀 다운로드
		exportExcel() {
			// datatables에 있는 버튼 클릭
			$(".buttons-excel").trigger("click");
		}
	},
	mounted() {
		this.list();
	},
	created() {
		this.$EventBus.$on("listEvent", this.list);
	}
};
</script>

<style>
</style>
