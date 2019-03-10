import Vue from "vue";
import { AppUtil } from "./bokslmoney.js";
import CommonUtil from "./common-util.js"

// 숫자 (,)콤마 추가
Vue.filter("numberFormat", value => {
	if (value === undefined) {
		return null;
	}
	return value.toLocaleString();
});

// 날짜 포맷 변환
// moment format pattern
Vue.filter("dateFormat", (value, format) => {
	if (moment.isMoment(value)) {
		return value.format(format);
	}
	if (value instanceof Date) {
		return moment(value).format(format);
	}
	if (!isNaN(value)) {
		return moment(value).format(format);
	}
	return moment().format(format);
});

// 계좌 이름
Vue.filter("accountName", function(accountSeq) {
	return AppUtil.getAccountName(accountSeq);
});

// 목록 번호 계산. 내림차순(높은 번호 부터)으로 표시
Vue.filter("indexSeq", (index, page) => page.totalCount - (page.currentPage - 1) * page.returnCount - index);

/*
 * 전역적으로 사용할 디렉티브 정의
 */

// 줄바꿈 -> br 태그 적용
Vue.directive("br", {
	inserted: (el, binding, vnode) => {
		$(el).html(CommonUtil.toBr(binding.value));
	}
});

// 컴포넌트(component)
// 선언
Vue.component("datepicker", {
	template: "<input/>",
	mounted: () => {
		var self = this;
		$(this.$el).datepicker({
			showOn: "button",
			dateFormat: "yy-mm-dd",
			onSelect: function(d) {
				self.$emit("update-date", d);
			}
		});
	},
	beforeDestroy: () => {
		$(this.$el)
			.datepicker("hide")
			.datepicker("destroy");
	}
});

Vue.component("my-currency-input", {
	props: ["value"],
	template: `
			<input type="text" v-model="displayValue" @blur="isInputActive = false" @focus="isInputActive = true" @keyup.13="emitEnter()"/>
		`,
	data() {
		return {
			isInputActive: false
		};
	},
	computed: {
		displayValue: {
			get() {
				if (this.isInputActive) {
					// Cursor is inside the input field. unformat display value for user
					return this.value.toString();
				} else {
					// User is not modifying now. Format display value for user interface
					return this.value.toString().replace(/(\d)(?=(\d{3})+(?:\.\d+)?$)/g, "$1,");
				}
			},
			set(modifiedValue) {
				// Recalculate value after ignoring "$" and "," in user input
				let newValue = parseFloat(modifiedValue.replace(/[^\d\.]/g, ""));
				// Ensure that it is not NaN
				if (isNaN(newValue)) {
					newValue = 0;
				}
				// Note: we cannot set this.value as it is a "prop". It needs to be passed to parent component
				// $emit the event so that parent component gets it
				this.$emit("input", newValue);
			}
		}
	},
	methods: {
		emitEnter() {
			this.$emit("press-enter");
		}
	}
});
