<%@ page contentType="text/html; charset=UTF-8"%>
<template id='item-panel'>
	<div role="tabpanel" class="tab-pane fade active in" id="tab_content1" aria-labelledby="home-tab">
		<div>
			<list :kind='kind' area="main" :parent-seq.number="0" v-on:@select-item="selectItem"/>
		</div>
		<div>
			<list :kind='kind' area="sub" :parent-seq.number="mainItem.itemSeq"/>
		</div>
	</div>
</template>

<script type="text/javascript">
	const itemPanelComponent = Vue.component("itemPanel", { template: '#item-panel',
		data(){
			return {
				mainItem: {itemSeq: -1}
			}
		},
		props: {
			kind: String,
		},
		components: {
			'list': itemListComponent
		},
		methods: {
			selectItem(item){
				this.mainItem = item;
			}
		}
	});
</script>