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
					ㅎㅎㅎㅎㅎㅎㅎㅎㅎㅎㅎㅎㅎㅎㅎㅎㅎㅎ
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">닫기</button>
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
			};
		},
		methods: {
			// 항목 조회
			loadItemAllList() {
				VueUtil.get("/hab/item/listAll.json", {}, (result) => {
					this.itemList = result.data;
				});
			},
			openItemList(item) {
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