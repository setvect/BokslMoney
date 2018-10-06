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
	// 버튼에 X가 표시 되도록 강제 설정. 왜 안되는지 모르겠다.
	$("button.ui-dialog-titlebar-close").addClass("ui-icon").addClass("ui-icon-closethick")
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

// 콤마
CommonUtil.toComma = function(value) {
	return value.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
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

// context root path
CommonUtil.getContextPath = function(){
	return $("meta[name='contextRoot']").attr("content")
}

CommonUtil.appendContextRoot = function(url){
	return CommonUtil.getContextPath() + url;
}

// 정규표현식에서 사용하는 특수문자를 escape 처리함
CommonUtil.escapeRegExp = function(str) {
	return str.replace(/[\-\[\]\/\{\}\(\)\*\+\?\.\\\^\$\|]/g, "\\$&");
}
// input text에 숫자 입력 시 콤마 표시
CommonUtil.inputComma = function (event) {
	// When user select text in the document, also abort.
	var selection = window.getSelection().toString();
	if (selection !== '') {
		return;
	}
	// When the arrow keys are pressed, abort.
	if ($.inArray(event.keyCode, [38, 40, 37, 39]) !== -1) {
		return;
	}
	var $this = $(this);
	// Get the value.
	var input = $this.val();
	var input = input.replace(/[\D\s\._\-]+/g, "");
	input = input ? parseInt(input, 10) : 0;
	$this.val(function () {
		return (input === 0) ? "" : input.toLocaleString("en-US");
	});
}
