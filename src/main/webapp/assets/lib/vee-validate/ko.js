! function(n, e) {
	"object" == typeof exports && "undefined" != typeof module ? module.exports = e() : "function" == typeof define && define.amd ? define(e) : (n.__vee_validate_locale__ko = n.__vee_validate_locale__ko || {}, n.__vee_validate_locale__ko.js = e())
}(this, function() {
	"use strict";
	var n = {
		name: "ko",
		messages: {
			after: function(n, e) {
				return n + "항목은 " + e[0] + "항목 뒤에 와야 합니다."
			},
			alpha_dash: function(n) {
				return n + "항목은 문자와 숫자 그리고 대시, 언더스코어를 사용할 수 있습니다."
			},
			alpha_num: function(n) {
				return n + "항목은 영문자와 숫자만 사용할 수 있습니다."
			},
			alpha_spaces: function(n) {
				return n + "항목은 영문자와 공백만 사용할 수 있습니다."
			},
			alpha: function(n) {
				return n + "항목은 영문자만 사용할 수 있습니다."
			},
			before: function(n, e) {
				return n + "항목은 " + e[0] + "항목의 앞에 와야 합니다."
			},
			between: function(n, e) {
				return n + "항목은 " + e[0] + " 와 " + e[1] + " 사이 값이어야 합니다."
			},
			confirmed: function(n, e) {
				return n + "의 항목이 " + e[0] + "항목과 일치하지 않습니다."
			},
			date_between: function(n, e) {
				return n + "항목은 " + e[0] + "와 " + e[1] + " 사이의 날짜이어야 합니다."
			},
			date_format: function(n, e) {
				return n + "항목은 " + e[0] + " 형식이어야 합니다."
			},
			decimal: function(n, e) {
				void 0 === e && (e = []);
				var t = e[0];
				return void 0 === t && (t = "*"), n + "항목은 숫자이어야 하고 " + ("*" === t ? "" : t) + " 소숫점을 가질 수 있습니다."
			},
			digits: function(n, e) {
				return n + "항목은 숫자이며 " + e[0] + "글자를 필요합니다."
			},
			dimensions: function(n, e) {
				return n + "의 사진 크기는 " + e[0] + "px과 " + e[1] + "px 이어야 합니다."
			},
			email: function(n) {
				return n + "항목은 올바른 이메일 형식이어야 합니다."
			},
			ext: function(n) {
				return n + "항목은 올바른 파일 형식이어야 합니다."
			},
			image: function(n) {
				return n + "항목은 이미지 파일이어야 합니다."
			},
			in: function(n) {
				return n + "항목은 올바른 값이어야 합니다."
			},
			ip: function(n) {
				return n + "항목은 올바른 IP 주소이어야 합니다."
			},
			max: function(n, e) {
				return n + "항목은 " + e[0] + "글자보다 작아야 합니다."
			},
			max_value: function(n, e) {
				return n + "는 최대한 " + e[0] + " 보다 작어야 합니다."
			},
			mimes: function(n) {
				return n + "는 올바른 파일이어야 합니다."
			},
			min: function(n, e) {
				return n + "는 최소한 " + e[0] + "글자보다 커야 합니다."
			},
			min_value: function(n, e) {
				return n + " " + e[0] + " 보다 커야합니다."
			},
			not_in: function(n) {
				return n + "항목은 올바른 값이어야 합니다."
			},
			numeric: function(n) {
				return n + "항목은 숫자이어야 합니다."
			},
			regex: function(n) {
				return n + "항목은 형식에 맞지 않습니다."
			},
			required: function(n) {
				return n + "항목이 필요합니다."
			},
			size: function(n, e) {
				return n + "항목의 크기는 " + function(n) {
					var e = 0 == (n = 1024 * Number(n)) ? 0 : Math.floor(Math.log(n) / Math.log(1024));
					return 1 * (n / Math.pow(1024, e)).toFixed(2) + " " + ["Byte", "KB", "MB", "GB", "TB", "PB", "EB", "ZB", "YB"][e]
				}(e[0]) + " 보다 작아야 합니다."
			},
			url: function(n) {
				return n + "항목은 올바른 주소(URL)가 아닙니다."
			}
		},
		attributes: {}
	};
	if ("undefined" != typeof VeeValidate) {
		VeeValidate.Validator.localize((e = {}, e[n.name] = n, e));
		var e
	}
	return n
});