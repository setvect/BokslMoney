<template>
	<div class id="app" v-cloak>
		<div class="page-title">
			<div class="title_left">
				<h3>코드관리: {{codeMain.name}}</h3>
			</div>
		</div>
		<div class="clearfix"></div>
		<div class="row">
			<div class="col-md-12 col-sm-12 col-xs-12">
				<div class="x_panel">
					<div class="col-md-6 col-sm-8 col-xs-12">
						<table class="table table-bordered">
							<thead>
								<tr>
									<th>코드 이름</th>
									<th>순서</th>
									<th>편집</th>
								</tr>
							</thead>
							<tbody>
								<tr v-for="(item, index) in itemList" :key="item.codeItemKey.codeItemSeq">
									<td>{{item.name}}</td>
									<td>
										<a
											href="javascript:"
											@click="changeOrder(itemList[index - 1].codeItemKey.codeItemSeq, item.codeItemKey.codeItemSeq)"
											:style="{visibility: isUpable(index) ? '' : 'hidden'}"
										>
											<i class="fa fa-arrow-up"></i>
										</a>
										<a
											href="javascript:"
											@click="changeOrder(item.codeItemKey.codeItemSeq, itemList[index + 1].codeItemKey.codeItemSeq)"
											:style="{visibility: isDownable(index) ? '' : 'hidden'}"
										>
											<i class="fa fa-arrow-down"></i>
										</a>
									</td>
									<td>
										<a href="javascript:void(0);" @click="editForm(item)">
											<i class="fa fa-edit"></i>
										</a>
										<a href="javascript:" @click="deleteAction(item.codeItemKey.codeItemSeq)">
											<i class="fa fa-remove"></i>
										</a>
									</td>
								</tr>
							</tbody>
						</table>
						<div>
							<button type="button" class="btn btn-success" @click="addForm()">추가</button>
						</div>
					</div>

					<div id="addItem" class="modal fade" role="dialog">
						<div class="modal-dialog">
							<!-- Modal content-->
							<div class="modal-content">
								<div class="modal-header">
									<button type="button" class="close" data-dismiss="modal">&times;</button>
									<h4 class="modal-title">등록</h4>
								</div>
								<div class="modal-body">
									<form onsubmit="return false;">
										<div class="form-group">
											<label>이름:</label>
											<input
												type="text"
												class="form-control"
												name="name"
												v-model="formItem.name"
												v-validate="'required|max:20'"
												data-vv-as="이름 "
												v-on:keyup.13="addAction()"
											>
											<span class="error" v-if="errors.has('name')">{{errors.first('name')}}</span>
										</div>
									</form>
								</div>
								<div class="modal-footer">
									<button type="button" class="btn btn-info" @click="addAction()">저장</button>
									<button type="button" class="btn btn-default" data-dismiss="modal">닫기</button>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</template>
<script type="text/javascript">
import Vue from "vue";
import VeeValidate from "vee-validate";
import VueUtil from "../../js/vue-util.js";

Vue.use(VeeValidate, {
	locale: "ko",
	events: "blur"
});

// vue 객체 생성
export default {
	data: function() {
		return {
			item: {},
			codeMainId: "",
			itemList: [],
			actionType: "",
			codeMain: {},
			formItem: {}
		};
	},
	methods: {
		// 리스트
		list() {
			let param = { codeMainId: this.codeMainId };
			VueUtil.get("/code/list.json", param, result => {
				this.itemList = result.data;
			});
		},
		// 등록 폼
		addForm() {
			this.actionType = "add";
			// 목록에서 최대 orderNo + 1 구하기
			let orderNo =
				this.itemList.reduce((acc, item) => {
					return Math.max(acc, item.orderNo);
				}, 0) + 1;
			this.openForm({ orderNo: orderNo });
		},
		//수정 폼
		editForm(item) {
			this.actionType = "edit";
			this.openForm(item);
		},
		openForm(item) {
			this.formItem = $.extend(true, {}, item);
			$("#addItem").modal();
		},
		// 등록 또는 수정
		addAction() {
			this.$validator.validateAll().then(result => {
				if (!result) {
					return;
				}
				this.formItem.codeMainId = this.codeMainId;
				if (this.formItem.codeItemKey != null) {
					this.formItem.codeItemSeq = this.formItem.codeItemKey.codeItemSeq;
				}
				delete this.formItem.codeItemKey;
				let url = this.actionType == "add" ? "/code/add.do" : "/code/edit.do";
				VueUtil.post(url, this.formItem, result => {
					$("#addItem").modal("hide");
					this.list();
				});
			});
		},
		// 정렬 순서 변경
		changeOrder(downCodeItemSeq, upCodeItemSeq) {
			let param = {
				codeMainId: this.codeMainId,
				downCodeItemSeq: downCodeItemSeq,
				upCodeItemSeq: upCodeItemSeq
			};
			VueUtil.post("/code/changeOrder.do", param, result => {
				this.list();
			});
		},
		// 삭제
		deleteAction(codeItemSeq) {
			if (!confirm("삭제?")) {
				return;
			}
			let param = { codeMainId: this.codeMainId, codeItemSeq: codeItemSeq };
			VueUtil.post("/code/delete.do", param, result => {
				this.list();
			});
		},
		isUpable(index) {
			if (this.itemList.length <= 1) {
				return false;
			}
			return index !== 0;
		},
		isDownable(index) {
			if (this.itemList.length <= 1) {
				return false;
			}
			return index + 1 !== this.itemList.length;
		},
		// 메인코드
		loadCodeMain() {
			VueUtil.get(
				"/code/getCodeMain.json",
				{ codeMainId: this.codeMainId },
				result => {
					this.codeMain = result.data;
				}
			);
		}
	},
	created() {
		let url = new URL(location.href);
		this.codeMainId = url.searchParams.get("codeMainId");
	},
	mounted() {
		this.list();
		this.loadCodeMain();
	}
};
</script>