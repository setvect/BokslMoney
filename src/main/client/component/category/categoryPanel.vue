<template id='item-panel'>
	<div role="tabpanel" class="tab-pane fade active in" id="tab_content1" aria-labelledby="home-tab">
		<div>
			<list :kind="kind" area="main" :parent-seq="0" v-on:@select-item="selectItem"/>
		</div>
		<div>
			<list :kind="kind" area="sub" :parent-seq="mainItem.categorySeq"/>
		</div>
	</div>
</template>

<script type="text/javascript">
import Vue from "vue";
import VeeValidate from "vee-validate";

import listComponent from "./categoryList.vue";

Vue.use(VeeValidate, {
	locale: "ko",
	events: "blur"
});

export default {
	data() {
		return {
			mainItem: { categorySeq: -1 }
		};
	},
	props: {
		kind: String
	},
	components: {
		list: listComponent
	},
	methods: {
		selectItem(item) {
			this.mainItem = item;
		}
	}
};
</script>