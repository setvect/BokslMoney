<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>

<script type="text/javascript" src="${pageContext.request.contextPath}/assets/lib/jquery/jquery-1.12.1.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/assets/lib/jquery-twbsPagination/jquery.twbsPagination.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/assets/lib/jquery-ui/jquery-ui.js"></script>

<!-- cookie -->
<script type="text/javascript" src="${pageContext.request.contextPath}/assets/lib/jquery.cookie/jquery.cookie.js"></script>

<script type="text/javascript" src="${pageContext.request.contextPath}/assets/lib/moment-develop/moment.js"></script>

<script type="text/javascript" src="${pageContext.request.contextPath}/assets/lib/js-xlsx-master/xlsx.full.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/assets/lib/js-xlsx-master/shim.min.js"></script>

<!-- bootstrap -->
<script type="text/javascript" src="${pageContext.request.contextPath}/assets/lib/bootstrap/js/bootstrap.min.js"></script>
<!-- bootstrap-daterangepicker -->
<script type="text/javascript" src="${pageContext.request.contextPath}/assets/lib/bootstrap-daterangepicker/daterangepicker.js"></script>

<!-- FullCalendar -->
<script type="text/javascript" src="${pageContext.request.contextPath}/assets/lib/fullcalendar/fullcalendar.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/assets/lib/fullcalendar/locale/ko.js"></script>
<!-- icheck-bootstrap -->
<script type="text/javascript" src="${pageContext.request.contextPath}/assets/lib/iCheck/icheck.js"></script>

<!-- 양.음력 -->
<script type="text/javascript" src="${pageContext.request.contextPath}/assets/lib/moment-lunar/moment-lunar.js"></script>

<script type="text/javascript" src="${pageContext.request.contextPath}/assets/js/calendar-util.js?<%=com.setvect.bokslmoney.BokslMoneyConstant.CACHE_VER%>"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/assets/lib/waiting.js?<%=com.setvect.bokslmoney.BokslMoneyConstant.CACHE_VER%>"></script>

<!-- 오른쪽 마우스 클릭, 메뉴 -->
<script src="${pageContext.request.contextPath}/assets/lib/jQuery-contextMenu-master/jquery.contextMenu.js"></script>
<script src="${pageContext.request.contextPath}/assets/lib/jQuery-contextMenu-master/jquery.ui.position.js"></script>


<!-- 테이블 정렬 -->
<script type="text/javascript" src="${pageContext.request.contextPath}/assets/lib/DataTables/datatables.js"></script>

<script type="text/javascript" src="${pageContext.request.contextPath}/assets/lib/jszip/dist/jszip.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/assets/lib/datatables.net-buttons/js/dataTables.buttons.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/assets/lib/datatables.net-buttons/js/buttons.html5.js"></script>

<!-- 차트 -->
<script type="text/javascript" src="${pageContext.request.contextPath}/assets/lib/Chart.js/dist/Chart.js"></script>

<!-- functional library -->
<script type="text/javascript" src="${pageContext.request.contextPath}/assets/lib/lodash/lodash.min.js"></script>

<!-- inputpicker -->
<script type="text/javascript" src="${pageContext.request.contextPath}/assets/lib/inputpicker-master/jquery.inputpicker.js"></script>

<!-- webpack bundle -->
<script type="text/javascript" src="${pageContext.request.contextPath}/assets/bundle/js/vendors.js"></script>

<script type="text/javascript">
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