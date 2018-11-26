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
	methods: {
		// 유형에 따른 UI 표현 속성값
		getKindAttr(kind){
			return TYPE_VALUE[kind];
		}
	}
}