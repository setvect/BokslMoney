<%@ page contentType="text/html; charset=UTF-8"%>
<div id="app">
	<div class="page-title">
		<div class="title_left">
			<h3>표 입력</h3>
		</div>
	</div>
	<div class="clearfix"></div>
	<div class="row">

		<div class="col-md-12 col-sm-12 col-xs-12">
			<div class="x_panel">
				<div v-cloak>
					<div class="col-md-2 col-sm-2 col-xs-12">
						aaaa
					</div>
					<div class="col-md-10 col-sm-10 col-xs-12">
						<table class="table table-striped jambo_table bulk_action table-bordered" id="grid">
							<thead>
								<tr class="headings">
									<th>유형</th>
									<th>메모</th>
									<th>대분류</th>
									<th>소분류</th>
									<th>출금액</th>
									<th>입금액</th>
									<th>수수료</th>
									<th>출금계좌</th>
									<th>일금계좌</th>
								</tr>
							</thead>
							<tbody>
								<tr>
									<td>1유형</td>
									<td>1메모</td>
									<td>대분류</td>
									<td>소분류</td>
									<td>출금액</td>
									<td>입금액</td>
									<td>수수료</td>
									<td>출금계좌</td>
									<td>일금계좌</td>
								</tr>
								<tr>
									<td>2유형</td>
									<td>2메모</td>
									<td>대분류</td>
									<td>소분류</td>
									<td>출금액</td>
									<td>입금액</td>
									<td>수수료</td>
									<td>출금계좌</td>
									<td>일금계좌</td>
								</tr>
								<tr>
									<td>3유형</td>
									<td>3메모</td>
									<td>대분류</td>
									<td>소분류</td>
									<td>출금액</td>
									<td>입금액</td>
									<td>수수료</td>
									<td>출금계좌</td>
									<td>일금계좌</td>
								</tr>
							</tbody>
						</table>
					</div>
				</div>
			</div>
		</div>

	</div>
	<div>
		<add />
	</div>
	<div>
		<memo />
	</div>
</div>

<jsp:include page="../record_add.inc.jsp"></jsp:include>
<jsp:include page="../memo.inc.jsp"></jsp:include>

<script type="text/javascript">
	Vue.use(VeeValidate, {
		locale: 'ko',
		events: 'blur',
	});
	let EventBus = new Vue();

	// vue 객체 생성
	const app = new Vue({
		data: function () {
			return {
			};
		},
		components: {
			'add': itemAddComponent,
			'memo': memoComponent
		},
		computed: {
		},
		methods: {
		},
		mounted() {
			$('#grid').DataTable({ paging: false, bInfo: false });
		}
	}).$mount('#app');
</script>