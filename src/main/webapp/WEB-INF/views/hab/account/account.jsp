<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
	<head>
		<jsp:include page="../../include/meta.inc.jsp"></jsp:include>
		<jsp:include page="../../include/style.inc.jsp"></jsp:include>
		<title>복슬머니</title>
	</head>
	<body class="nav-md">
		<div class="container body">
			<div class="main_container">
				<div class="col-md-3 left_col">
					<jsp:include page="../../include/left.inc.jsp"></jsp:include>
				</div>
				<jsp:include page="../../include/head.inc.jsp"></jsp:include>
				
				<!-- page content -->
				<div class="right_col" role="main">
					<div>
						<div class="page-title">
							<div class="title_left">
								<h3>계좌 관리</h3>
							</div>
						</div>
						<div class="clearfix"></div>
						<div class="row">
							<div class="col-md-12 col-sm-12 col-xs-12">
								<div class="x_panel">
									<div class="x_content" id="app">
										<div>
											<list/>
										</div>
										<div>
											<add/>
										</div>
										<div>
											<read/>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
				<!-- /page content -->
				<jsp:include page="../../include/footer.jsp"></jsp:include>
			</div>
		</div>
		<jsp:include page="../../include/script.inc.jsp"></jsp:include>
		<jsp:include page="../../include/common_modal.inc.jsp"></jsp:include>
		<jsp:include page="account_list.inc.jsp"></jsp:include>
		<jsp:include page="account_add.inc.jsp"></jsp:include>
		<jsp:include page="account_read.inc.jsp"></jsp:include>

		<script type="text/javascript">


			Vue.use(VeeValidate,{
				locale: 'ko',
				events: 'blur',
			});
			let EventBus = new Vue();

			// vue 객체 생성
			const app = new Vue({
				data: function(){
					return {
					};
				},
				components: {
					'list': itemListComponent,
					'add': itemAddComponent,
					'read': itemReadComponent,
				},
			}).$mount('#app');
		</script>
		
		
	</body>
</html>
