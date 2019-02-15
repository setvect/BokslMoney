<%@ page contentType="text/html; charset=UTF-8"%>

<div class="" id="app" v-cloak>
	<div class="page-title">
		<div class="title_left">
			<h3>분류 관리</h3>
		</div>
	</div>
	<div class="clearfix"></div>
	<div class="row">
		<div class="col-md-12 col-sm-12 col-xs-12">
			<div class="x_panel">
				<div class="" role="tabpanel" data-example-id="togglable-tabs">
					<ul id="myTab" class="nav nav-tabs bar_tabs" role="tablist">
						<li role="presentation" class="active"><a href="#tab_content1" data-toggle="tab" aria-expanded="true">지출 항목</a></li>
						<li role="presentation" class=""><a href="#tab_content2" data-toggle="tab" aria-expanded="false">수입 항목</a></li>
						<li role="presentation" class=""><a href="#tab_content3" data-toggle="tab" aria-expanded="false">이체 항목</a></li>
					</ul>
					<div id="myTabContent" class="tab-content">
						<div role="tabpanel" class="tab-pane fade active in" id="tab_content1" aria-labelledby="home-tab">
							<panel kind='SPENDING'/>
						</div>

						<div role="tabpane2" class="tab-pane fade in" id="tab_content2" aria-labelledby="home-tab">
							<panel kind='INCOME'/>
						</div>

						<div role="tabpane3" class="tab-pane fade in" id="tab_content3" aria-labelledby="home-tab">
							<panel kind='TRANSFER'/>
						</div>
					</div>
				</div>
				<add/>
			</div>
		</div>
	</div>
</div>

<script type="text/javascript" src="${pageContext.request.contextPath}/assets/bundle/js/app/category.js"></script>