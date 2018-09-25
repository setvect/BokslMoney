<%@ page contentType="text/html; charset=UTF-8"%>
<template id='item-list'>
	<div class="col-md-4 col-sm-4 col-xs-12">
		<p>{{area === 'main' ? '대분류': '소분류'}}</p>
		<table class="table table-striped">
			<tbody>
				<tr v-for="(item, index) in itemList">
					<td><a href="javascript:" @click="selectItem(item)">{{item.name}}</a></td>
					<td style="text-align: left;">
						<a href="javascript:" @click="changeOrder(itemList[index - 1].itemSeq, item.itemSeq)" :style="{visibility: isUpable(index) ? '' : 'hidden'}"><i class="fa fa-arrow-up"></i></a>
						<a href="javascript:" @click="changeOrder(item.itemSeq, itemList[index + 1].itemSeq)" :style="{visibility: isDownable(index) ? '' : 'hidden'}"><i class="fa fa-arrow-down"></i></a>
					</td>
					<td style="text-align: center;">
						<a href="javascript:" @click="editForm(item)"><i class="fa fa-edit" @click="editForm(item)"></i></a>
						<a href="javascript:" @click="deleteAction(item.itemSeq)"><i class="fa fa-remove"></i></a>
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
				waitDialog.show('조회 중입니다.', {dialogSize: 'sm'});
			
				axios.get(CommonUtil.getContextPath() + "/item/list.json", {params:param}).then((result) => {
					this.itemList = result.data;
				}).catch((err) => CommonUtil.popupError(err)).finally (() =>	waitDialog.hide());
			},
			// 등록 폼 
			addForm(){
				// 목록에서 최대 orderNo + 1 구하기
				let maxOrder = this.itemList.reduce((acc, item) => {return Math.max(acc, item.orderNo)}, 0) + 1;
				EventBus.$emit('addFormEvent', {kind: this.kind, parentItemSeq: this.parentSeq, orderNo: maxOrder }, this.list);
			},
			// 수정폼
			editForm(item){
				EventBus.$emit('editFormEvent', item, this.list);
			},
			// 정렬 순서 변경
			changeOrder(downItemSeq, upItemSeq){
				waitDialog.show('처리 중입니다.', {dialogSize: 'sm'});
				axios.post(CommonUtil.getContextPath() + '/item/changeOrder.do', $.param({downItemSeq: downItemSeq, upItemSeq: upItemSeq})).then((result) => {
					this.list();
				}).catch((err) =>	CommonUtil.popupError(err)).finally (() => waitDialog.hide());
			},
			// 삭제
			deleteAction(itemSeq, callBack){
				if(!confirm("삭제?")){
					return;
				}
				waitDialog.show('처리 중입니다.', {dialogSize: 'sm'});
				axios.post(CommonUtil.getContextPath() + '/item/delete.do', $.param({itemSeq: itemSeq})).then((result) => {
					this.list();
					this.$emit('@select-item', {itemSeq: -1});
				}).catch((err) =>	CommonUtil.popupError(err)).finally (() => waitDialog.hide());
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