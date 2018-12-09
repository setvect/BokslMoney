<%@ page contentType="text/html; charset=UTF-8"%>
<div id="app">
	<div class="page-title">
		<div class="title_left">
			<h3>통계</h3>
		</div>
	</div>
	<div class="clearfix"></div>
	<div class="row">
		<div class="col-md-12 col-sm-12 col-xs-12">
			<div class="x_panel">
				<div class="" role="tabpanel" data-example-id="togglable-tabs">
					<ul id="myTab" class="nav nav-tabs bar_tabs" role="tablist">
						<li role="presentation" class="active"><a href="#tab_content1" data-toggle="tab" aria-expanded="true">수입/지출/이체</a></li>
						<li role="presentation"><a href="#tab_content2" data-toggle="tab" aria-expanded="true">자산 변동</a></li>
					</ul>
					<div id="myTabContent" class="tab-content">
						<div role="tabpanel" class="tab-pane fade active in" id="tab_content1" aria-labelledby="home-tab">
							<kind-group />
						</div>
						<div role="tabpanel" class="tab-pane fade in" id="tab_content2" aria-labelledby="home-tab">
							<stat-assets/>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>

<jsp:include page="stat_assets.jsp"></jsp:include>
<jsp:include page="stat_kind_group.jsp"></jsp:include>

<script type="text/javascript">
	const currentYear = (new Date()).getFullYear();
	// vue 객체 생성
	const app = new Vue({
		data: function () {
			return {
			};
		},
		computed: {
		},
		methods: {
		},
		mounted() {
		}
	}).$mount('#app');
</script>