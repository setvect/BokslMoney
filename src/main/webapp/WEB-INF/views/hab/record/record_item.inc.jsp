<%@ page contentType="text/html; charset=UTF-8"%>
<template id='item-all-list'>
	<div id="itemAllList" class="modal fade" role="dialog">
		<div class="modal-dialog">
			<!-- Modal content-->
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">&times;</button>
					<h4 class="modal-title">항목 선택</h4>
				</div>
				<div class="modal-body">
					<form class="form-horizontal">
						<div class="col-md-6 col-sm-6 col-xs-6">
							<p>대분류</p>
							<select class="form-control" size="10" v-model="subList" @change="reset()">
								<option v-for="(item, index) in mainList" :value="item.children">{{item.data.name}}</option>
							</select>
						</div>
						<div class="col-md-6 col-sm-6 col-xs-6">
							<p>소분류</p>
							<select class="form-control" size="10" v-model="selectItem">
								<option v-for="(item, index) in subList" :value="item.data">{{item.data.name}}</option>
							</select>
						</div>
					</form>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-info" @click="confirm()">저장</button>
					<button type="button" class="btn btn-default" @click="close()">닫기</button>
				</div>
			</div>
		</div>
	</div>
</template>

<script type="text/javascript">
	const itemAllListComponent = Vue.component("itemAllList", {
		template: '#item-all-list',
		data() {
			return {
				itemList: {},
				mainList:[],
				subList:[],
				selectItem:null,
			};
		},
		methods: {
			// 확인
			confirm(){
				if(!this.selectItem){
					alert("소분류 항목 선택해 주세요.");
					return;
				}
				this.close();
			},
			// 항목 조회
			loadItemAllList() {
				VueUtil.get("/hab/item/listAll.json", {}, (result) => {
					this.itemList = result.data;
				});
			},
			close(){
				$("#itemAllList").modal("hide");
			},
			reset(){
				this.selectItem = null;
			},
			openItemList(itemType) {
				this.mainList = this.itemList[itemType];
				$("#itemAllList").modal();
			},
		},
		mounted() {
			this.loadItemAllList();
		},
		created() {
			EventBus.$on('openItemListEvent', this.openItemList);
		},
	});
</script>