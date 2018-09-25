// 프로그램 전역적으로 사용하는 공통 함수
var CommonUtil = {};

/**
 * 프로그램 오류로 인한 경고창
 */
CommonUtil.popupError = function(err) {
	var message = err.response == null ? err.message : err.response.data.message;
	if(err.message != null){
		CommonUtil.dialogInfo("프로그램 에러", message);
	}
	else{
		CommonUtil.dialogInfo("프로그램 에러", err);
	}
}
/**
 * 메시지 창
 */
CommonUtil.dialogInfo = function(title, message){
	$("#alert-dialog").attr("title", title);
	$("#alert-dialog ._message").html(message);

	$("#alert-dialog").dialog({
		resizable : false,
		height : "auto",
		width : 550,
		modal : true,
		open: function(){
			$('.ui-dialog').css('z-index',99999);
		},
		buttons : {
			Close : function() {
				$(this).dialog("close");
			}
		}
	});
}


/**
 * twbsPagination 페이징 처리 관련 옵션
 */
CommonUtil.makePageOption = function(page, callback){

	return {
		initiateStartPageClick : false,
		totalPages : page.totalPage == 0 ? 1 : page.totalPage,
		visiblePages : page.visiblePage,
		startPage : page.currentPage,
		first: null,
		last: null,
		prev: "«",
		next: "»",
		href: "javascript:void(0)",
		onPageClick : callback
	}
}

// 기존 페이징 객체 제거
CommonUtil.destroyPage = function(selector){
	if($(selector).data("twbs-pagination")){
		$(selector).twbsPagination('destroy');
	}
}

// 줄바꿈을 br 테그로 변경
CommonUtil.toBr = function(text){
	if(text === undefined || text == null){
		return null;
	}
	return text.replace(/(?:\r\n|\r|\n)/g, '<br/>');
}

// null 또는 빈 공백이면 true 반환
CommonUtil.isEmpty = function(val){
  return (val === undefined || val == null || val.length <= 0) ? true : false;
}

// 공백제거
CommonUtil.removeWhiteSpace = function(val){
	if(CommonUtil.isEmpty(val)){
		return '';
	}else{
		return val.replace(/\s/gi, "");
	}
}

//$route.query 값을 deep copy
CommonUtil.copyParam = function(param){
	var result = $.extend(true, {}, param);
	// route path 변경 설정을 위해 임의의 값을 줌.
	result["_dummy"] = Date.now();
	return result;
}

// context root path
CommonUtil.getContextPath = function(){
	return $("meta[name='contextRoot']").attr("content")
}

// 정규표현식에서 사용하는 특수문자를 escape 처리함
CommonUtil.escapeRegExp = function(str) {
	return str.replace(/[\-\[\]\/\{\}\(\)\*\+\?\.\\\^\$\|]/g, "\\$&");
}


/****************************************
 *  prototype 정의
 ****************************************/
// Date 객체 날짜 Formatter
Date.prototype.format = function(f) {
	if (!this.valueOf())
		return " ";

	var weekName = [ "일요일", "월요일", "화요일", "수요일", "목요일", "금요일", "토요일" ];
	var d = this;

	return f.replace(/(yyyy|yy|MM|dd|E|hh|mm|ss|a\/p)/gi, function($1) {
		switch ($1) {
		case "yyyy":
			return d.getFullYear();
		case "yy":
			return (d.getFullYear() % 1000).zf(2);
		case "MM":
			return (d.getMonth() + 1).zf(2);
		case "dd":
			return d.getDate().zf(2);
		case "E":
			return weekName[d.getDay()];
		case "HH":
			return d.getHours().zf(2);
		case "hh":
			return ((h = d.getHours() % 12) ? h : 12).zf(2);
		case "mm":
			return d.getMinutes().zf(2);
		case "ss":
			return d.getSeconds().zf(2);
		case "a/p":
			return d.getHours() < 12 ? "오전" : "오후";
		default:
			return $1;
		}
	});
};


/*******************************************************************************
 * Vue에서 전역적으로 사용하는 필더 정의
 ******************************************************************************/

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

// carriage return를 br태그로 변경
Vue.filter('br', function (value) {
	return CommonUtil.toBr(value);
});

//필터값 로그 출력
Vue.filter('logPrint', function (value) {
	if(value === undefined){
		return null;
	}
	console.log(value);
	return value;
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

// 상대적 날짜 계산 
Vue.filter('relativeDate', function (timestamp) {
	var diff = moment(Date.now()).diff(timestamp, 'days');
	if(diff < 10){
		return moment(timestamp).fromNow(); 
	}
	//10일이 지나면 일반적으로 날짜 표시
	var d = new Date(Number(timestamp));
	return d.format('yyyy-MM-dd');
});
