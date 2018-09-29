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
								<button type="button" class="btn btn-info _spending">지출</button>
								<button type="button" class="btn btn-info _income">수입</button>
								<button type="button" class="btn btn-info _transfer">이체</button>
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
	const TYPE_VALUE = {
		'spending': {
			title: '지출',
			color: '#00bb33',
			icon: 'fa-minus-square'
		},
		'income': {
			title: '수입',
			color: '#ff99cc',
			icon: 'fa-plus-square'
		},
		'transfer': {
			title: '이체',
			color: '#66ccff',
			icon: 'fa-check-square-o'
		}
	}


	Vue.use(VeeValidate, {
		locale: 'ko',
		events: 'blur',
	});
	let EventBus = new Vue();

	// vue 객체 생성
	const app = new Vue({
		data: function () {
			return {
				calendar: null,
				currentMonth: null,
				selectDate: moment(),
			};
		},
		components: {
		},
		methods: {
			initCalendar() {
				let self = this;
				if (typeof ($.fn.fullCalendar) === 'undefined') { return; }
				//  console.log('initCalendar');

				let date = new Date(),
					d = date.getDate(),
					m = date.getMonth(),
					y = date.getFullYear(),
					started,
					categoryClass;

				this.calendar = $('#calendar').fullCalendar({
					editable: false,
					selectable: true,
					selectHelper: true,
					showNonCurrentDates: false,
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
					// 날짜를 선택했을 때
					select(start, end, allDay) {
						self.selectDate = start;
						console.log("select event $$", start, start.format("YYYY-MM-DD"));
					},
					// 이벤트를 표시할 때
					eventRender(event, element) {
						if (event.type) {
							let t = TYPE_VALUE[event.type];
							element.find(".fc-title").prepend("<i class='fa " + t.icon + "'></i>" + t.title + ' : ' + CommonUtil.toComma(event.cost));
						}
					},
					// 최초, 월이 변경 되었을 때 발생하는 이벤트
					viewRender(view) {
						let start = view.start._d;
						let end = view.end._d;
						let dates = { start: start, end: end };
						self.currentMonth = view.start;
					},
				});
				$(".fc-next-button, .fc-prev-button, .fc-today-button").click((event) => {
					let d = this.calendar.fullCalendar('getDate');
					console.log("button event @@", d);
					if ($(event.target).hasClass("fc-today-button")) {
						this.calendar.fullCalendar('select', moment());
					} else {
						this.calendar.fullCalendar('select', d);
					}
				});
			},
			add(date, type, cost) {
				let list = this.calendar.fullCalendar('clientEvents',
					function (events) {
						let view_start = date.format("YYYY-MM-DD");
						return (moment(events.start).format('YYYY-MM-DD') == view_start && events.type == type);
					}
				);
				if (list.length == 0) {
					this.calendar.fullCalendar('renderEvent', {
						type: type,
						color: TYPE_VALUE[type].color,
						cost: cost,
						start: date.format("YYYY-MM-DD"),
					});
				}
				else {
					console.log(list[0]);
					this.calendar.fullCalendar('removeEvents', list[0]._id);
					// list[0].cost = cost;
					// this.calendar.fullCalendar('updateEvent', list[0]);
				}
			}
		},
		mounted() {
			this.initCalendar();
			$("._spending").click(() => {
				this.add(moment(), 'spending', Math.floor(Math.random() * 10000) + 1);
			});

			$("._income").click(() => {
				this.add(moment(), 'income', Math.floor(Math.random() * 10000) + 1);
			});

			$("._transfer").click(() => {
				this.add(moment(), 'transfer', Math.floor(Math.random() * 10000) + 1);
			});
		}
	}).$mount('#app');
</script>