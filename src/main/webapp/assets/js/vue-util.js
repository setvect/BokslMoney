/*****
 * Vue 관련 공통 함수.
 *****/
var VueUtil = {};
const NOTING_OPERATION = ()=>{};
/*
 * url: 호출 주소
 * param: 전달 파라미터
 * option: 옵션
 */
VueUtil.get = function (url, param, callback, option) {
	VueUtil._ajaxCall("get", url, param, callback, option);
};

/*
 * url: 호출 주소
 * param: 전달 파라미터
 * option: 옵션
 */
VueUtil.post = function (url, param, callback, option) {
	VueUtil._ajaxCall("post", url, param, callback, option);
};

/*
 * get, post 처리
 */
VueUtil._ajaxCall = function (callType, url, _param, _callback, _option) {
	let param = _param || {};
	let callback = _callback || NOTING_OPERATION;
	let option = _option || {};

	let waitMsg = option.waitMsg || "처리 중입니다";
	let axiosMethod;
	let sendParam;
	if (callType == "get") {
		axiosMethod = axios.get;
		sendParam = { params: param };
	} else if (callType == "post") {
		axiosMethod = axios.post;
		sendParam = $.param(param, true);
	}

	let finallyCall = option.finallyCall || NOTING_OPERATION;
	let errorCall =	option.errorCall || function (err) {CommonUtil.popupError(err);};

	waitDialog.show(waitMsg, { dialogSize: "sm" });

	axiosMethod(CommonUtil.appendContextRoot(url), sendParam)
	.then((result) => _callback(result))
	.catch((err) => errorCall(err))
	.finally(() =>{
		waitDialog.hide();
		finallyCall();
	});
};

/**
 * Vue에서 전역적으로 사용하는 필더 정의
 */

// 숫자 (,)콤마 추가
Vue.filter('formatNumber', function (value) {
	if(value === undefined){
		return null;
	}
	return value.toLocaleString();
});

Vue.filter('dateFilter', function (value, format) {
	var date = new Date(value);
	return date.format(format);
});

// timestamp dataformat
Vue.filter('timestampFormat', function (timestamp, format) {
	var d = new Date(Number(timestamp));
	return d.format(format);
});

// 목록 번호 계산. 내림차순(높은 번호 부터)으로 표시
Vue.filter('indexSeq', function (index, page) {
	return page.totalCount - ((page.currentPage - 1) * page.returnCount)  - index;
});

/*
 * 전역적으로 사용할 디렉티브 정의
 */

// 줄바꿈 -> br 태그 적용
Vue.directive('br', {
	inserted: function (el, binding, vnode) {
		$(el).html(CommonUtil.toBr(binding.value));
	}
})

