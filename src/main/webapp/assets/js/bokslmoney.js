const TYPE_VALUE = {
	'SPENDING': {
		title: '지출',
		color: '#00bb33',
		icon: 'fa-minus-square'
	},
	'INCOME': {
		title: '수입',
		color: '#ff99cc',
		icon: 'fa-plus-square'
	},
	'TRANSFER': {
		title: '이체',
		color: '#66ccff',
		icon: 'fa-check-square-o'
	}
}

// 거래 내역 mixin
let TransactionMixin = {
	data: function () {
		return {
			// 지출, 수입, 이체 내역
			transactionList: [],
		};
	},
	computed: {
		sumIncome() {
			return this.sumCalculation(t => t.kind == 'INCOME');
		},
		sumSpending() {
			return this.sumCalculation(t => t.kind == 'SPENDING');
		},
		sumTransfer() {
			return this.sumCalculation(t => t.kind == 'TRANSFER');
		},
	},
	methods: {
		// 유형에 따른 UI 표현 속성값
		getKindAttr(kind) {
			return TYPE_VALUE[kind];
		},
		// 수입, 지출, 이체 합산
		sumCalculation(filterCondition) {
			return this.transactionList.filter(filterCondition).reduce((acc, t) => { return acc + t.money; }, 0);
		},
	}
}

/** 응용어플리케이션에 의존된 공통 기능 */
let AppUtil = {};
/**
 * 계좌 이름
 */
AppUtil.getAccountName = function (accountSeq) {
	if (AppUtil.accountMap == null) {
		var v = $.ajax({
			type: "GET",
			url: CommonUtil.appendContextRoot("/hab/account/mapName.json"),
			async: false
		}).responseText;
		AppUtil.accountMap = JSON.parse(v);
	}
	return AppUtil.accountMap[accountSeq];
}

// 계좌 이름
Vue.filter('accountName', function (accountSeq) {
	return AppUtil.getAccountName(accountSeq);
});
