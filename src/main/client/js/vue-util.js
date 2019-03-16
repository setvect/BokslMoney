import CommonUtil from "./common-util.js";
import axios from "axios"
import waitDialog from './waiting.js'

/*****
 * Vue 관련 공통 함수.
 *****/
const VueUtil = {};
const NOTING_OPERATION = () => { };
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
		let paramParsing = option.paramParsing || false;
		if (paramParsing) {
			let searchParams = new URLSearchParams($.param(param, true));
			sendParam = { params: searchParams };
		} else {
			sendParam = { params: param };
		}
	} else if (callType == "post") {
		axiosMethod = axios.post;
		sendParam = $.param(param, true);
	}

	let finallyCall = option.finallyCall || NOTING_OPERATION;
	let errorCall = option.errorCall || function (err) { CommonUtil.popupError(err); };

	if (option.waitDialog != false) {
		waitDialog.show(waitMsg, { dialogSize: "sm" });
	}

	axiosMethod(CommonUtil.appendContextRoot(url), sendParam)
		.then((result) => callback(result))
		.catch((err) => errorCall(err))
		.finally(() => {
			if (option.waitDialog != false) {
				waitDialog.hide();
			}
			finallyCall();
		});
};

export default VueUtil;
