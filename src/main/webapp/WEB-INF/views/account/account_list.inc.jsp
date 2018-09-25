<%@ page contentType="text/html; charset=UTF-8"%>
<template id='item-list'>
	<div class="col-md-12 col-sm-12 col-xs-12">
		<table class="table table-bordered">
			<thead>
				<tr>
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
				<tr v-for="(item, index) in itemList">
					<td>자산종류</td>
					<td>이름</td>
					<td>잔고</td>
					<td>이율</td>
					<td>계좌(카드)번호</td>
					<td>월 납입액</td>
					<td>메모</td>
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
			// 등록 폼 
			addForm(){
				EventBus.$emit('addFormEvent', this.list);
			},
			// 수정폼
			editForm(item){
			},
			// 삭제
			deleteAction(itemSeq, callBack){
			},
		},
		mounted() {
			this.list();
		},
	});
</script>