<%@ page contentType="text/html; charset=UTF-8"%> <%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<!-- top navigation -->
<div class="top_nav">
	<div class="nav_menu">
		<nav>
			<div class="nav toggle">
				<a id="menu_toggle"><i class="fa fa-bars"></i></a>
			</div>
			<ul class="nav navbar-nav navbar-right">
				<li class="">
					<a
						href="${pageContext.request.contextPath}/logout.do"
						onclick="javascript:location.href='${pageContext.request.contextPath}/logout.do'"
						class="user-profile dropdown-toggle"
						data-toggle="dropdown"
						aria-expanded="false"
					>
						로그아웃 <span class="glyphicon glyphicon-log-out"></span
					></a>
				</li>
				<li role="presentation" class="dropdown">
					<a href="javascript:;" class="dropdown-toggle info-number _edit-my" data-toggle="dropdown" aria-expanded="false">
						비밀번호 변경 <i class="glyphicon glyphicon-cog"></i
					></a>
				</li>
			</ul>
		</nav>
	</div>
</div>
<!-- /top navigation -->
<script type="text/javascript">
	$("._edit-my").click(function() {
		if ($("#passwd-change-form").length) {
			$("#myinfo-edit-dialog").modal();
		} else {
			$("#myinfo-edit-dialog .modal-content").load(CommonUtil.getContextPath() + "/user/loginUserEdit.do", function() {
				$("#myinfo-edit-dialog").modal();
			});
		}
	});
</script>
