import CommonUtil from "../common-util.js";

$(function() {
	$("._edit-my").click(function() {
		if ($("#passwd-change-form").length) {
			$("#myinfo-edit-dialog").modal();
		} else {
			$("#myinfo-edit-dialog .modal-content").load(CommonUtil.getContextPath() + "/user/loginUserEdit.do", function() {
				$("#myinfo-edit-dialog").modal();
			});
		}
	});
});
