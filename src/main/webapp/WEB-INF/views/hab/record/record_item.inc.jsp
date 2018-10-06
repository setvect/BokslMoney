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
							<select class="form-control _mainItemSelect" size="10" v-model="selectMainItem" @change="reset()">
								<option v-for="(item, index) in mainList" :value="item">{{item.data.name}}</option>
							</select>
						</div>
						<div class="col-md-6 col-sm-6 col-xs-6">
							<p>소분류</p>
							<select class="form-control _subItemSelect" size="10" v-model="selectSubItem">
								<option v-for="(item, index) in selectMainItem.children" :value="item">{{item.data.name}}</option>
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
				itemListMap: {},
				mainList: [],
				selectMainItem: {},
				selectSubItem: null,
			};
		},
		methods: {
			// 확인
			confirm() {
				if (!this.selectSubItem) {
					alert("소분류 항목 선택해 주세요.");
					return;
				}

				EventBus.$emit('insertItemEvent', this.selectMainItem.data, this.selectSubItem.data);
				this.close();
			},
			// 항목 조회
			loadItemAllList() {
				VueUtil.get("/hab/item/listAll.json", {}, (result) => {
					this.itemListMap = result.data;
				});
			},
			close() {
				$("#itemAllList").modal("hide");
			},
			reset() {
				this.selectSubItem = null;
				$("._subItemSelect option:eq(0)").prop("selected", true);
				this.selectSubItem = this.selectMainItem.children[0];
			},
			openItemList(itemType) {
				this.mainList = this.itemListMap[itemType];
				$("#itemAllList").modal();
				// DOM 갱신 이후 발생한 이벤트
				this.$nextTick(() => {
					let selectIdx = $("._mainItemSelect").prop('selectedIndex');
					if (selectIdx == -1) {
						$("._mainItemSelect option:eq(0)").prop("selected", true);
						this.selectMainItem = this.mainList[0];
						this.reset();
					}
				})
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