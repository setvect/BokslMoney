<%@ page contentType="text/html; charset=UTF-8"%>
<template id='item-list'>
	<div class="col-md-4 col-sm-4 col-xs-12">
		<p>{{area === 'main' ? '대분류': '소분류'}}</p>
		<table class="table table-striped">
			<tbody>
				<tr v-for="(item, index) in itemList">
					<td><a href="javascript:" @click="selectItem(item)">{{item.name}}</a></td>
					<td style="text-align: left;">
						<a href="javascript:" @click="changeOrder(itemList[index - 1].transactionKindSeq, item.transactionKindSeq)" :style="{visibility: isUpable(index) ? '' : 'hidden'}"><i class="fa fa-arrow-up"></i></a>
						<a href="javascript:" @click="changeOrder(item.transactionKindSeq, itemList[index + 1].transactionKindSeq)" :style="{visibility: isDownable(index) ? '' : 'hidden'}"><i class="fa fa-arrow-down"></i></a>
					</td>
					<td style="text-align: center;">
						<a href="javascript:" @click="editForm(item)"><i class="fa fa-edit" @click="editForm(item)"></i></a>
						<a href="javascript:" @click="deleteAction(item.transactionKindSeq)"><i class="fa fa-remove"></i></a>
					</td>
				</tr>
			</tbody>
		</table>
		<div>
			<button type="button" class="btn btn-success" @click="addForm()" v-show="parentSeq !== -1">추가</button>
		</div>
	</div>
</template>

<script type="text/javascript">
	const itemListComponent = Vue.component("itemList", { template: '#item-list',
		data(){
			return {
				itemList: [],
			};
		},
		props: {
			kind: String,
			area: String,
			parentSeq: Number,
		},
		watch: {
			parentSeq: {
				handler(newVal, oldVal){
					this.list();
				},
			}
		},
		methods: {
			// 리스트
			list(){
				let param = {kind: this.kind, parent: this.parentSeq};
				VueUtil.get("/hab/transaction_kind/list.json", param, (result) => {
					this.itemList = result.data;
				});
			},
			// 등록 폼
			addForm(){
				// 목록에서 최대 orderNo + 1 구하기
				let maxOrder = this.itemList.reduce((acc, item) => {return Math.max(acc, item.orderNo)}, 0) + 1;
				EventBus.$emit('addFormEvent', {kind: this.kind, parentSeq: this.parentSeq, orderNo: maxOrder }, this.list);
			},
			// 수정폼
			editForm(item){
				EventBus.$emit('editFormEvent', item, this.list);
			},
			// 정렬 순서 변경
			changeOrder(downtransactionKindSeq, uptransactionKindSeq){
				let param = {downtransactionKindSeq: downtransactionKindSeq, uptransactionKindSeq: uptransactionKindSeq};
				VueUtil.post("/hab/transaction_kind/changeOrder.do", param, (result) => {
					this.list();
				});
			},
			// 삭제
			deleteAction(transactionKindSeq, callBack){
				if(!confirm("삭제?")){
					return;
				}
				let param = {transactionKindSeq: transactionKindSeq};
				VueUtil.post("/hab/transaction_kind/delete.do", param, (result) => {
					this.list();
					this.$emit('@select-item', {transactionKindSeq: -1});
				});
			},
			isUpable(index){
				if(this.itemList.length <= 1){
					return false;
				}
				return index !== 0;
			},
			isDownable(index){
				if(this.itemList.length <= 1){
					return false;
				}
				return index + 1 !== this.itemList.length;
			},
			selectItem(item){
				this.$emit('@select-item', item);
			}
		},
		mounted() {
			this.list();
		},
	});
</script>