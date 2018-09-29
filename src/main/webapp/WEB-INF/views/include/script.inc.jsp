<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>

<script type="text/javascript" src="${pageContext.request.contextPath}/assets/lib/jquery/jquery-1.12.1.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/assets/lib/jquery-twbsPagination/jquery.twbsPagination.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/assets/lib/jquery-ui/jquery-ui.js"></script>

<script type="text/javascript" src="${pageContext.request.contextPath}/assets/lib/vue/vue.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/assets/lib/vue/vue-router.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/assets/lib/vue/axios.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/assets/lib/vee-validate/vee-validate.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/assets/lib/vee-validate/ko.js"></script>

<!-- cookie -->
<script type="text/javascript" src="${pageContext.request.contextPath}/assets/lib/jquery.cookie/jquery.cookie.js"></script>

<script type="text/javascript" src="${pageContext.request.contextPath}/assets/lib/moment-develop/moment.js"></script>

<script type="text/javascript" src="${pageContext.request.contextPath}/assets/lib/js-xlsx-master/xlsx.full.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/assets/lib/js-xlsx-master/shim.min.js"></script>

<!-- bootstrap -->
<script type="text/javascript" src="${pageContext.request.contextPath}/assets/lib/bootstrap/js/bootstrap.min.js"></script>

<script src="${pageContext.request.contextPath}/assets/lib/parsleyjs/bower_components/bootstrap/js/tooltip.js"></script>
<script src="${pageContext.request.contextPath}/assets/lib/parsleyjs/bower_components/bootstrap/js/popover.js"></script>

<!-- FastClick -->
<script src="${pageContext.request.contextPath}/assets/lib/fastclick/lib/fastclick.js"></script>
<!-- NProgress -->
<script src="${pageContext.request.contextPath}/assets/lib/nprogress/nprogress.js"></script>
<!-- FullCalendar -->
<script src="${pageContext.request.contextPath}/assets/lib/fullcalendar/fullcalendar.js"></script>
<script src="${pageContext.request.contextPath}/assets/lib/fullcalendar/locale/ko.js"></script>

<script type="text/javascript" src="${pageContext.request.contextPath}/assets/js/common-util.js?<%=com.setvect.bokslmoney.BokslMoneyConstant.CACHE_VER%>"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/assets/js/vue-util.js?<%=com.setvect.bokslmoney.BokslMoneyConstant.CACHE_VER%>"></script>

<script type="text/javascript" src="${pageContext.request.contextPath}/assets/lib/waiting.js?<%=com.setvect.bokslmoney.BokslMoneyConstant.CACHE_VER%>"></script>

<script type="text/javascript" >
	moment.locale('ko');

	// 좌측 메뉴 활성화
	// 현재 보고 있는 페이지에 따라 메뉴 활성화 처리
	$(()=>{
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