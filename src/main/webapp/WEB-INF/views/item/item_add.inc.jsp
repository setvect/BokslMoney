<%@ page contentType="text/html; charset=UTF-8"%>
<template id='item-add'>
	<div id="addItem" class="modal fade" role="dialog">
		<div class="modal-dialog">
			<!-- Modal content-->
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">&times;</button>
					<h4 class="modal-title">등록</h4>
				</div>
				<div class="modal-body">
					<form>
						<div class="form-group">
							<label>이름: </label><input type="text" class="form-control" name="name" v-model="item.name" v-validate="'required|max:20'" data-vv-as="이름 ">
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
</template>

<script type="text/javascript">
	const itemAddComponent = Vue.component("itemAdd", { template: '#item-add',
		data(){
			return {
				item: {name:""},
				afterEventCallback: null,
				actionType: 'add'
			};
		},
		methods: {
			// 등록 폼 
			addForm(item, afterEventCallback){
				this.actionType = 'add';
				this.openForm(item, afterEventCallback);
			},
			//수정 폼 
			editForm(item, afterEventCallback){
				this.actionType = 'edit';
				this.openForm(item, afterEventCallback);
			},
			openForm(item, afterEventCallback){
				this.item = $.extend(true, {}, item);
				this.afterEventCallback = afterEventCallback;
				$("#addItem").modal();
			},
			// 등록 또는 수정
			addAction(){
				let self = this;
				
				this.$validator.validateAll().then((result) => {
					if(!result){
						return;
					}
					let url = this.actionType == 'add' ? '/item/add.do' : '/item/edit.do'
					waitDialog.show('처리 중입니다.', {dialogSize: 'sm'});
					axios.post(CommonUtil.getContextPath() + url, $.param(self.item, true)).then((result) => {
						$("#addItem").modal('hide');
						self.afterEventCallback();
						self.item.name = "";
					}).catch((err) =>	CommonUtil.popupError(err)).finally (() => waitDialog.hide());
				});
			},
		},
		created(){
			EventBus.$on('addFormEvent', this.addForm);
			EventBus.$on('editFormEvent', this.editForm);
		},
	});
</script>
