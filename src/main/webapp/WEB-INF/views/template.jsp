<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page import="com.setvect.bokslmoney.BokslMoneyConstant.AttributeName"%>
<!DOCTYPE html>
<html lang="ko">
	<head>
		<jsp:include page="/WEB-INF/views//include/meta.inc.jsp"></jsp:include>
		<jsp:include page="/WEB-INF/views/include/style.inc.jsp"></jsp:include>
		<jsp:include page="/WEB-INF/views/include/script.inc.jsp"></jsp:include>
		<title>복슬머니</title>
	</head>
	<body class="nav-md">
		<div class="container body">
			<div class="main_container">
				<div class="col-md-3 left_col">
					<jsp:include page="./include/left.inc.jsp"></jsp:include>
				</div>
				<jsp:include page="./include/head.inc.jsp"></jsp:include>

				<!-- page content -->
				<div class="right_col" role="main">
					<!-- Custom Theme Scripts -->
					<% pageContext.include((String)request.getAttribute(AttributeName.LOAD_PAGE));%>
				</div>

				<!-- /page content -->
				<jsp:include page="./include/footer.jsp"></jsp:include>
				<jsp:include page="/WEB-INF/views/include/common_modal.inc.jsp"></jsp:include>
			</div>
		</div>
		<script src="${pageContext.request.contextPath}/assets/lib/gentelella/js/custom.js"></script>
	</body>
</html>