<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
	<head>
		<jsp:include page="./include/meta.inc.jsp"></jsp:include>
		<jsp:include page="./include/style.inc.jsp"></jsp:include>
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
					<div class="">
						<div class="page-title">
							<div class="title_left">
								<h3>Plain Page</h3>
							</div>
						</div>
						<div class="clearfix"></div>
						<div class="row">
							<div class="col-md-12 col-sm-12 col-xs-12">
								<div class="x_panel">
									<div class="x_title">
										<h2>Plain Page</h2>
										<div class="clearfix"></div>
									</div>
									<div class="x_content">Add content to the page ...111111111</div>
								</div>
							</div>
						</div>
					</div>
				</div>
				<!-- /page content -->
				<jsp:include page="./include/footer.jsp"></jsp:include>
			</div>
		</div>
		<jsp:include page="./include/script.inc.jsp"></jsp:include>
	</body>
</html>