<%@ page contentType="text/html; charset=UTF-8"%>
<template id='item-list'>
	<div class="col-md-12 col-sm-12 col-xs-12">
		<table class="table table-striped jambo_table bulk_action table-bordered">
			<thead>
				<tr class="headings">
					<th>자산종류</th>
					<th>이름</th>
					<th>잔고</th>
					<th>이율</th>
					<th>계좌(카드)번호</th>
					<th>월 납입액</th>
					<th>메모</th>
				</tr>
			</thead>
			<tbody>
				<tr v-for="(item, index) in itemList" @click="readForm(item)" style="cursor: pointer">
					<td>{{item.kindName}}</td>
					<td>{{item.name}}</td>
					<td>{{item.balance | formatNumber}}</td>
					<td>{{item.interestRate}}</td>
					<td>{{item.accountNumber}}</td>
					<td>{{item.monthlyPay}}</td>
					<td class="td-ell">{{item.note}}</td>
				</tr>
			</tbody>
		</table>
		<div>
			<button type="button" class="btn btn-success" @click="addForm()">추가</button>
		</div>
	</div>
</template>

<script type="text/javascript">
	const itemListComponent = Vue.component("itemList", { template: '#item-list',
		data(){
			return {
				itemList: []
			};
		},
		props: {
		},
		methods: {
			// 리스트
			list(){
				waitDialog.show('조회 중입니다.', {dialogSize: 'sm'});
				axios.get(CommonUtil.getContextPath() + "/account/list.json").then((result) => {
					this.itemList = result.data;
				}).catch((err) => CommonUtil.popupError(err)).finally (() =>	waitDialog.hide());
			},
			readForm(item){
				EventBus.$emit('readFormEvent', item);
			},
			// 등록 폼 
			addForm(){
				EventBus.$emit('addFormEvent', {});
			},
			// 수정폼
			editForm(item){
			},
		},
		mounted() {
			this.list();
		},
		created(){
			EventBus.$on('listEvent', this.list);
		}
	});
</script>