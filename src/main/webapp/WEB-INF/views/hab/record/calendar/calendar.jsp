<%@ page contentType="text/html; charset=UTF-8"%>
<div id="app">
	<div class="page-title">
		<div class="title_left">
			<h3>달력 입력</h3>
		</div>
	</div>
	<div class="clearfix"></div>
	<div class="row">
		<div class="col-md-12 col-sm-12 col-xs-12">
			<div class="x_panel">
				<div>
					<div v-cloak>
						<div class="col-md-8 col-sm-8 col-xs-12">
							<div id='calendar'></div>
						</div>
						<div class="col-md-4 col-sm-4 col-xs-12">
							<div class="page-header">
								<button type="button" class="btn btn-info _add">지출</button>
								<button type="button" class="btn btn-info">수입</button>
								<button type="button" class="btn btn-info">이체</button>
							</div>
							<div>
								<h4>2018년 9월 9일 내역</h4>
								<table class="table table-bordered">
									<colgroup>
										<col width="10%" />
										<col width="40%" />
										<col width="25%" />
										<col width="25%" />
									</colgroup>
									<thead>
										<th>유형</th>
										<th>분류</th>
										<th>내용</th>
										<th>금액</th>
									</thead>
									<tbody>
										<tr>
											<td>수익</td>
											<td>근로소득 > 급여</td>
											<td>월급</td>
											<td class="text-right">12120</td>
										</tr>
										<tr>
											<td>수익</td>
											<td>금융소득 > 이자</td>
											<td>적급</td>
											<td class="text-right">12120</td>
										</tr>
										<tr>
											<td>지출</td>
											<td>생활용품 > 주방용품</td>
											<td>냄비</td>
											<td class="text-right">12120</td>
										</tr>
										<tr>
											<td>지출</td>
											<td>생활용품 > 주방용품</td>
											<td>냄비</td>
											<td class="text-right">12120</td>
										</tr>
										<tr>
											<td>이체</td>
											<td>대차거래 > 인출</td>
											<td>이체</td>
											<td class="text-right">12120</td>
										</tr>
									</tbody>
								</table>
							</div>

							<div>
								<h4>9월 결산</h4>
								<table class="table table-bordered">
									<tbody>
										<tr>
											<td>수입</td>
											<td class="text-right">{{12200000 | formatNumber}}</td>
										</tr>
										<tr>
											<td>지출</td>
											<td class="text-right">{{120000 | formatNumber}}</td>
										</tr>
										<tr>
											<td>수입 - 지출</td>
											<td class="text-right">{{5980000 | formatNumber}}</td>
										</tr>
										<tr>
											<td>이체</td>
											<td class="text-right">{{120000 | formatNumber}}</td>
										</tr>
									</tbody>
								</table>
							</div>
						</div>
					</div>
				</div>

			</div>
		</div>
	</div>
</div>

<script type="text/javascript">
	/* CALENDAR */
	function init_calendar() {
		if (typeof ($.fn.fullCalendar) === 'undefined') { return; }
		//  console.log('init_calendar');

		var date = new Date(),
			d = date.getDate(),
			m = date.getMonth(),
			y = date.getFullYear(),
			started,
			categoryClass;

		var calendar = $('#calendar').fullCalendar({
			editable: false,
			selectable: true,
			selectHelper: true,
			// eventLimit: true, // allow "more" link when too many events
			option: {
				locale: "ko"
			},
			header: {
				left: 'prev,next today',
				center: 'title',
				right: ''
			},
			selectConstraint: {
				start: '00:00',
				end: '24:00',
			},
			select: function (start, end, allDay) {
				console.log("###################");
				console.log("################### start:", start);
				console.log("################### end:", end);
				console.log("################### allDay:", allDay);

				calendar.fullCalendar('renderEvent', {
					title: 'aaaaaaaaaa',
					start: start,
					end: end,
					allDay: allDay
				},
					true // make the event "stick"
				);
			},
			events: [{
				title: 'All Day Event',
				start: new Date(y, m, 1)
			},
			{
				title: '복슬이',
				start: '2018-09-04',
			},
			{
				title: 'Meeting',
				start: new Date(y, m, d),
				allDay: false
			}]
		});

		$("._add").click(function(){
			calendar.fullCalendar('renderEvent', {
				title: 'aaaaaaaaaa',
				start: '2018-09-22',
				color: '#DD99AA'
			});
			console.log("EEEEEEEEEEEEEEEEE");
		});
	};

	$(() => {
		init_calendar();
	});
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
		},
	}).$mount('#app');
</script>