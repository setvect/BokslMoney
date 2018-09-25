<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<sec:authorize access="hasRole('ROLE_USER')">
	<%
		// 로그인 중인 경우 로그인 페이지 접근 안 됌.
			response.sendRedirect("/");
	%>
</sec:authorize>

<!DOCTYPE html>
<html lang="ko">
<head>
<jsp:include page="./include/meta.inc.jsp"></jsp:include>
<jsp:include page="./include/script.inc.jsp"></jsp:include>
<jsp:include page="./include/style.inc.jsp"></jsp:include>
<title>복슬머니</title>
</head>

<body class="login">
	<div>
		<a class="hiddenanchor" id="signup"></a> <a class="hiddenanchor"
			id="signin"></a>

		<div class="login_wrapper">
			<div class="animate form login_form">
				<div>
					<c:if test="${param.error != null}">
						<span>아이디 또는 비밀번호가 틀렸습니다.</span>
					</c:if>
					<c:if test="${param.logout != null}">
						<span>로그아웃 하셨습니다.</span>
					</c:if>
				</div>
				<section class="login_content">
					<form action="${pageContext.request.contextPath}/login.do" method="post">
						<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
						<h1>복슬머니 로그인</h1>
						<div>
							<input name="username" type="text" class="form-control" placeholder="Username" />
						</div>
						<div>
							<input name="password" type="password" class="form-control" placeholder="Password" />
						</div>
						<div>
							<label><input type="checkbox" name="remember-me"><i></i><em>로그인 정보 유지</em></label>
						</div>
						<div>
							<button class="btn btn-default submit" type="submit">
								<em>로그인</em>
							</button>
						</div>
					</form>
				</section>
			</div>
		</div>
	</div>
</body>
</html>





