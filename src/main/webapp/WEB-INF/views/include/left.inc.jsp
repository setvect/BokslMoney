<%@ page contentType="text/html; charset=UTF-8"%>

<div class="left_col scroll-view">
	<div class="navbar nav_title" style="border: 0;">
		<a href="/" class="site_title"><i class="fa fa-paw"></i> <span>복슬머니</span></a>
	</div>
	<div class="clearfix"></div>
	<!-- sidebar menu -->
	<div id="sidebar-menu" class="main_menu_side hidden-print main_menu">
		<div class="menu_section">
			<ul class="nav side-menu">
				<li>
					<a href="${pageContext.request.contextPath}/hab/transaction/calendar.do"><i class="fa fa-calendar"></i> 가계부 쓰기(달력)</a>
				</li>
				<li>
					<a href="${pageContext.request.contextPath}/hab/transaction/grid.do"><i class="fa fa-th"></i> 가계부 쓰기(표)</a>
				</li>
				<li>
					<a href="${pageContext.request.contextPath}/hab/settlement.do"><i class="fa fa-database"></i> 결산 </a>
				</li>
				<li>
					<a><i class="fa fa-bar-chart-o"></i> 통계 </a>
				</li>
				<li>
					<a href="${pageContext.request.contextPath}/hab/category/page.do"><i class="fa fa-building-o"></i> 분류 관리 </a>
				</li>
				<li>
					<a href="${pageContext.request.contextPath}/hab/account/page.do"><i class="fa fa-money"></i> 계좌 관리 </a>
				</li>
				<li>
					<a href="${pageContext.request.contextPath}/code/page.do?codeMainId=KIND_CODE"><i class="fa fa-book"></i> 코드 관리</a>
					<ul class="nav child_menu">
						<li><a href="${pageContext.request.contextPath}/code/page.do?codeMainId=KIND_CODE">자산유형</a></li>
						<li><a href="${pageContext.request.contextPath}/code/page.do?codeMainId=ATTR_SPENDING">지출항목</a></li>
						<li><a href="${pageContext.request.contextPath}/code/page.do?codeMainId=ATTR_TRANSFER">이체항목</a></li>
						<li><a href="${pageContext.request.contextPath}/code/page.do?codeMainId=ATTR_INCOME">수입항목</a></li>
					</ul>
				</li>
			</ul>
		</div>
	</div>
	<!-- /sidebar menu -->
</div>