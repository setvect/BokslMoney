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
				<div v-cloak>
					<div class="col-md-8 col-sm-8 col-xs-12">
						<div id='calendar'></div>
					</div>
					<div class="col-md-4 col-sm-4 col-xs-12">
						<div class="page-header">
							<button type="button" data-type="SPENDING" class="btn btn-info _input">지출</button>
							<button type="button" data-type="INCOME" class="btn btn-info _input">수입</button>
							<button type="button" data-type="TRANSFER" class="btn btn-info _input">이체</button>
						</div>
						<div>
							<h4>{{selectDate | dateFormat("YYYY년 MM월 DD일")}} 내역</h4>
							<table class="table table-bordered">
								<colgroup>
									<col width="12%" />
									<col width="30%" />
									<col width="17%" />
									<col width="16%" />
									<col width="25%" />
								</colgroup>
								<thead>
									<th>유형</th>
									<th>분류</th>
									<th>내용</th>
									<th>금액</th>
									<th>기능</th>
								</thead>
								<tbody>
									<tr v-for="t in listSelectDayTransfer">
										<td>{{kindMapValue(t.kind).title}}</td>
										<td>
											{{t.parentTransactionKind.name}} > {{t.transactionKind.name}}
										</td>
										<td>{{t.note}}</td>
										<td class="text-right">{{t.money | numberFormat}}</td>
										<td class="text-center">
											<div class="btn-group  btn-group-xs">
												<button type="button" class="btn btn-success btn-xs" @click="editForm(t)">수정</button>
												<button type="button" class="btn btn-dark btn-xs" @click="deleteAction(t)">삭제</button>
											</div>
										</td>
									</tr>
								</tbody>
							</table>
						</div>

						<div>
							<h4>{{currentMonth | dateFormat("YYYY년 MM월")}} 결산</h4>
							<table class="table table-bordered">
								<tbody>
									<tr>
										<td>수입</td>
										<td class="text-right">{{sumIncome | numberFormat}}</td>
									</tr>
									<tr>
										<td>지출</td>
										<td class="text-right">{{sumSpending | numberFormat}}</td>
									</tr>
									<tr>
										<td>수입 - 지출</td>
										<td class="text-right">{{sumIncome - sumSpending | numberFormat}}</td>
									</tr>
									<tr>
										<td>이체</td>
										<td class="text-right">{{sumTransfer | numberFormat}}</td>
									</tr>
								</tbody>
							</table>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<add/>
</div>

<jsp:include page="../record_add.inc.jsp"></jsp:include>

<script type="text/javascript">
	const TYPE_VALUE = {
		'SPENDING': {
			title: '지출',
			color: '#00bb33',
			icon: 'fa-minus-square'
		},
		'INCOME': {
			title: '수입',
			color: '#ff99cc',
			icon: 'fa-plus-square'
		},
		'TRANSFER': {
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
				// 지출, 수입, 이체 내역
				transferList:[],
				selectDate: moment(),
			};
		},
		components: {
			'add': itemAddComponent
		},
		computed:{
			sumIncome(){
				return this.sumCalculation(t => t.kind == 'INCOME');
			},
			sumSpending(){
				return this.sumCalculation(t => t.kind == 'SPENDING');
			},
			sumTransfer(){
				return this.sumCalculation(t => t.kind == 'TRANSFER');
			},
			// 선택된 날짜의 지출, 수입, 이체 내역
			listSelectDayTransfer(){
				let r = this.transferList.filter((t) =>{
					return this.selectDate.toDate().getDate() == new Date(t.transactionDate).getDate();
				});
				return r;
			}
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
					// 달력에서 셀(날짜) 하나를 선택했을 때
					select(start, end, allDay) {
						self.selectDate = start;
						$(".fc-day").removeClass("cal-select");
						$(".fc-day[data-date='" + self.selectDate.format("YYYY-MM-DD")  + "']").addClass("cal-select");
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
						self.loadRecord(self.currentMonth.toDate().getFullYear(), self.currentMonth.toDate().getMonth()+1);
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
			// 해당 월에 등록된 지출,수입,이체 항목 조회
			loadRecord(year, month){
				VueUtil.get("/hab/transaction/listByMonth.json", {year: year, month: month}, (result) => {
					this.calendar.fullCalendar('removeEvents');
					this.transferList = result.data;
					for(let idx in this.transferList) {
						let t = this.transferList[idx];
						this.displayTransfer(moment(t.transactionDate), t.kind, t.money);
					}
				});
			},
			/**
			 * 지출, 이체, 수입 항목 달력 셀에 입력
			 * date: 날짜(moment 객체)
			 * type: 유형코드(지출, 이체, 수입)
			 * cost: 금액
			 */
			displayTransfer(date, type, cost) {
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
					list[0].cost += cost;
					this.calendar.fullCalendar('updateEvent', list[0]);
				}
			},
			// 수입, 지출, 이체 합산
			sumCalculation(filterCondition){
				return this.transferList.filter(filterCondition).reduce((acc, t) => {return acc + t.money;}, 0);
			},
			kindMapValue(kind){
				return TYPE_VALUE[kind];
			},
			// 현재 보고 있는 달력 거래 내역 트랜.
			reload(){
				this.loadRecord(this.currentMonth.toDate().getFullYear(), this.currentMonth.toDate().getMonth() + 1);
			},
			editForm(item){
				var d = $.extend(true, {}, item);
				EventBus.$emit('editFormEvent', d);
			},
			deleteAction(item){
				if(!confirm("삭제하시겠습니까?")){
					return;
				}
				VueUtil.post('/hab/transaction/delete.do', {itemSeq: item.transactionSeq}, (result) => {
					this.reload();
				});
			}
		},
		mounted() {
			this.initCalendar();
			// 지출, 이체, 수입 버튼 클릭
			$("._input").click((event) => {
				let type = $(event.target).attr("data-type");
				EventBus.$emit('addFormEvent', type, this.selectDate);
			});
			EventBus.$on('reloadEvent', this.reload);
		}
	}).$mount('#app');
</script>