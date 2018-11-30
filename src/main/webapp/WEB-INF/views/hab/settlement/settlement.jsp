<%@ page contentType="text/html; charset=UTF-8"%>
<div id="app">
	<div class="page-title">
		<div class="title_left">
			<h3>결산</h3>
		</div>
	</div>
	<div class="clearfix"></div>
	<div class="row">
		<div class="col-md-12 col-sm-12 col-xs-12">

			<div class="x_panel">
				<div class="form-inline" style="margin-bottom: 10px; padding-left: 10px;">
					<div class="form-group">
						<select class="form-control">
							<option value="">--결산 년도 선택--</option>
						</select>
					</div>
					<button type="submit" class="btn btn-default" style="margin: 0">조회</button>
				</div>

				<div class="col-md-12 col-sm-12 col-xs-12">
					<table class="table table-striped jambo_table bulk_action table-bordered" id="grid">
						<thead>
							<tr class="headings">
								<th class="text-center">항목</th>
								<th class="text-center">2018년 1월</th>
								<th class="text-center">2018년 2월</th>
								<th class="text-center">2018년 3월</th>
								<th class="text-center">2018년 4월</th>
								<th class="text-center">2018년 5월</th>
								<th class="text-center">2018년 6월</th>
								<th class="text-center">2018년 7월</th>
								<th class="text-center">2018년 8월</th>
								<th class="text-center">2018년 9월</th>
								<th class="text-center">2018년 10월</th>
								<th class="text-center">2018년 11월</th>
								<th class="text-center">2018년 12월</th>
							</tr>
						</thead>
						<tbody>
							<tr>
								<td>식료품비</td>
								<td class="text-right">10,000</td>
								<td class="text-right">15,000</td>
								<td class="text-right">10,000</td>
								<td class="text-right">15,000</td>
								<td class="text-right">10,000</td>
								<td class="text-right">15,000</td>
								<td class="text-right">10,000</td>
								<td class="text-right">15,000</td>
								<td class="text-right">10,000</td>
								<td class="text-right">15,000</td>
								<td class="text-right">10,000</td>
								<td class="text-right">15,000</td>
							</tr>
							<tr>
								<td>주거비</td>
								<td class="text-right">10,000</td>
								<td class="text-right">15,000</td>
								<td class="text-right">10,000</td>
								<td class="text-right">15,000</td>
								<td class="text-right">10,000</td>
								<td class="text-right">15,000</td>
								<td class="text-right">10,000</td>
								<td class="text-right">15,000</td>
								<td class="text-right">10,000</td>
								<td class="text-right">15,000</td>
								<td class="text-right">10,000</td>
								<td class="text-right">15,000</td>
							</tr>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>
</div>

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
		computed: {
		},
		methods: {
		},
		mounted() {
		}
	}).$mount('#app');
</script>