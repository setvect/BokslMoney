<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>

<script type="text/javascript" src="${pageContext.request.contextPath}/assets/lib/jquery/jquery-1.12.1.min.js"></script>

<script type="text/javascript" src="${pageContext.request.contextPath}/assets/lib/jquery.cookie/jquery.cookie.js"></script>

<script type="text/javascript" src="${pageContext.request.contextPath}/assets/lib/bootstrap/js/bootstrap.min.js"></script>

<script type="text/javascript" src="${pageContext.request.contextPath}/assets/lib/jszip/dist/jszip.js"></script>

<!-- webpack bundle -->
<script type="text/javascript" src="${pageContext.request.contextPath}/assets/bundle/js/vendors.js"></script>

<script type="text/javascript">
	// 좌측 메뉴 활성화
	// 현재 보고 있는 페이지에 따라 메뉴 활성화 처리
	$(function(){
		let url = new URL(location.href);
		if(url.pathname === "/"){
			return;
		}
		let aTag = $("#sidebar-menu > div > ul > li > a[href^='" + url.pathname + "']");
		aTag.parent().addClass("active");
		aTag.parent().find("ul").css("display", "block");
	});

	<sec:authorize access="isAuthenticated()">
		const LOGIN_USER_NAME = $('<textarea />').html("<sec:authentication property='principal.name'/>").text();
		const LOGIN_USER_ID = "<sec:authentication property='principal.username'/>";
	</sec:authorize>
</script>