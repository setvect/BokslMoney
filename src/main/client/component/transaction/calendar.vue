<template>
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
						<div class="col-md-6 col-sm-6 col-xs-12">
							<div id="calendar"></div>
						</div>
						<div class="col-md-6 col-sm-6 col-xs-12">
							<div class="page-header">
								<button type="button" data-type="SPENDING" class="btn btn-info _input">지출</button>
								<button type="button" data-type="INCOME" class="btn btn-info _input">수입</button>
								<button type="button" data-type="TRANSFER" class="btn btn-info _input">이체</button>
								<button type="button" data-type="MEMO" class="btn btn-warning _input">메모</button>
							</div>
							<div>
								<h4>{{selectDate | dateFormat("YYYY년 MM월 DD일")}} 내역</h4>
								<table class="table table-bordered">
									<colgroup>
										<col width="10%">
										<col width="20%">
										<col width="15%">
										<col width="10%">
										<col width="15%">
										<col width="15%">
										<col width="15%">
									</colgroup>
									<thead>
										<th>유형</th>
										<th>분류</th>
										<th>내용</th>
										<th>금액</th>
										<th>출금계좌</th>
										<th>입금계좌</th>
										<th>기능</th>
									</thead>
									<tbody>
										<tr v-for="t in listSelectDayTransfer" :key="t.transactionSeq">
											<td :style="{color:getKindAttr(t.kind).color}">{{kindMapValue(t.kind).title}}</td>
											<td>{{t.parentCategory.name}} > {{t.category.name}}</td>
											<td>{{t.note}}</td>
											<td class="text-right">{{t.money | numberFormat}}</td>
											<td>{{t.payAccount | accountName}}</td>
											<td>{{t.receiveAccount | accountName}}</td>
											<td class="text-center">
												<div class="btn-group btn-group-xs">
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
											<td>
												<span :style="{color:getKindAttr('INCOME').color}" style="line-height: normal ">수입</span>
											</td>
											<td class="text-right">{{sumIncome | numberFormat}}</td>
										</tr>
										<tr>
											<td>
												<span :style="{color:getKindAttr('SPENDING').color}" style="line-height: normal ">지출</span>
											</td>
											<td class="text-right">{{sumSpending | numberFormat}}</td>
										</tr>
										<tr>
											<td>
												<span :style="{color:getKindAttr('INCOME').color}" style="line-height: normal ">수입</span> -
												<span :style="{color:getKindAttr('SPENDING').color}" style="line-height: normal ">지출</span>
											</td>
											<td class="text-right">{{sumIncome - sumSpending | numberFormat}}</td>
										</tr>
										<tr>
											<td>
												<span :style="{color:getKindAttr('TRANSFER').color}" style="line-height: normal ">이체</span>
											</td>
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
		<div>
			<add/>
		</div>
		<div>
			<memo/>
		</div>
	</div>
</template>
<script type="text/javascript">
import _ from 'lodash';
import moment from "moment";
import 'fullcalendar';
import "fullcalendar/dist/locale/ko";
import "fullcalendar/dist/fullcalendar.min.css";
import 'jquery-contextmenu';
import 'jquery-contextmenu/dist/jquery.contextMenu.css';

import VueUtil from "../../js/vue-util.js";
import CommonUtil from "../../js/common-util.js";
import CalendarUtil from "../../js/calendar-util.js";
import { TransactionMixin, AppUtil, TYPE_VALUE } from "../../js/bokslmoney.js";
import memoComponent from "./memo.vue";
import itemAddComponent from "./transactionAdd.vue";
import "../../js/vue-common.js";

// vue 객체 생성
export default {
	mixins: [TransactionMixin],

	data: function () {
		return {
			calendar: null,
			currentMonth: null,
			memoList: []
		};
	},
	components: {
		add: itemAddComponent,
		memo: memoComponent
	},
	computed: {
		// 선택된 날짜의 지출, 수입, 이체 내역
		listSelectDayTransfer() {
			let r = this.transactionList.filter(t => {
				return (
					this.selectDate.toDate().getDate() == new Date(t.transactionDate).getDate()
				);
			});
			return r;
		}
	},
	methods: {
		initCalendar() {
			let self = this;
			if (typeof $.fn.fullCalendar === "undefined") {
				return;
			}

			let date = new Date(),
				d = date.getDate(),
				m = date.getMonth(),
				y = date.getFullYear(),
				started,
				categoryClass;

			this.calendar = $("#calendar").fullCalendar({
				editable: false,
				selectable: true,
				selectHelper: true,
				showNonCurrentDates: false,
				option: { locale: "ko" },
				header: { left: "prevYear,prev,next,nextYear today", center: "title", right: "" },
				selectConstraint: { start: "00:00", end: "24:00" },
				// 달력에서 셀(날짜) 하나를 선택했을 때
				select(start, end, allDay) {
					self.selectDate = start;
					$(".fc-day").removeClass("cal-select");
					$(".fc-day[data-date='" + self.selectDate.format("YYYY-MM-DD") + "']").addClass("cal-select");
				},
				// 이벤트를 달력 셀에 표시
				eventRender(event, element) {
					// 거래별 합산
					if (event.type) {
						let t = TYPE_VALUE[event.type];
						element.find(".fc-title").prepend("<i class='fa " + t.icon + "'></i>" + t.title + " : " + CommonUtil.toComma(event.cost));
					}
					// 메모
					if (event.text) {
						element.find(".fc-title").prepend("<i class='fa fa-sticky-note-o'></i>" + event.text);
					}
				},
				// 이벤트 항목 클릭 시 달력 셀(날짜) 클릭 효과 부여
				eventClick(calEvent, jsEvent, view) {
					self.calendar.fullCalendar("select", calEvent.start);
				},
				// 최초, 월이 변경 되었을 때 발생하는 이벤트
				viewRender(view) {
					let start = view.start._d;
					let end = view.end._d;
					let dates = { start: start, end: end };
					self.currentMonth = view.start;
					self.loadMonthData(
						self.currentMonth.toDate().getFullYear(),
						self.currentMonth.toDate().getMonth() + 1
					);
					self.holiydayDisplay();
				}
			});
			// 오른쪽 마우스 클릭
			$.contextMenu({
				selector: "#calendar",
				callback: function (type, options) {
					self.addItemForm(type);
				},
				items: {
					SPENDING: { name: "지출", icon: "fa-minus-square" },
					INCOME: { name: "수입", icon: "fa-check-square-o" },
					TRANSFER: { name: "이체", icon: "fa-plus-square" },
					MEMO: { name: "메모", icon: "fa-sticky-note-o" }
				}
			});
			$(".fc-next-button, .fc-prev-button, .fc-today-button").click(event => {
				let d = this.calendar.fullCalendar("getDate");
				if ($(event.target).hasClass("fc-today-button")) {
					this.calendar.fullCalendar("select", moment());
				} else {
					this.calendar.fullCalendar("select", d);
				}
			});
		},
		// 해당 월에 거래 내역 및 메모 조회
		loadMonthData(year, month) {
			// 해당 월에 등록된 지출,수입,이체 항목 조회
			VueUtil.get("/transaction/listByMonth.json", { year: year, month: month },
				result => {
					this.calendar.fullCalendar("removeEvents");
					this.transactionList = result.data;

					let transactionSet = this.transactionList.map(t => {
						return { date: moment(t.transactionDate).format("YYYY-MM-DD"), kind: t.kind, money: t.money };
					});
					this.multiUpdate(transactionSet);

					// 해당 월에 등록된 메모 조회
					VueUtil.get("/memo/listByMonth.json", { year: year, month: month },
						result => {
							this.memoList = result.data;
							for (let idx in this.memoList) {
								let memo = this.memoList[idx];
								this.displayMemo(memo);
							}
						}
					);
				}
			);
		},
		// 해당 날짜에 등록된 메모 항목 반환
		getMemo(date) {
			let memo = this.memoList.find(memo => {
				return (
					moment(memo.memoDate).format("YYYYMMDD") == date.format("YYYYMMDD")
				);
			});
			return memo;
		},
		// 지출, 이체, 수입 항목 달력 셀에 표시 값 지정
		// 한꺼번에 등록(속도 향상을 위함 것임)
		multiUpdate(transactionSet) {
			// debugger;
			// transactionSet
			let transactionGroupDate = _.chain(transactionSet)
				.groupBy("date")
				.map((listGroupByDate, date) => {
					var listGroupByDate = _.map(listGroupByDate, function (c) {
						return _.omit(c, ["date"]);
					});
					listGroupByDate = _.chain(listGroupByDate)
						.groupBy("kind")
						.map((listGroupByKind, kind) => {
							return { money: _.sumBy(listGroupByKind, "money"), kind };
						}).value();
					return { listGroupByDate, date };
				}).value();
			let events = [];
			transactionGroupDate.forEach(tranDate => {
				let date = tranDate["date"];
				tranDate["listGroupByDate"].forEach(tranKind => {
					let kind = tranKind["kind"];
					let money = tranKind["money"];
					events.push({ type: kind, color: TYPE_VALUE[kind].color, cost: money, start: date });
				});
			});
			this.calendar.fullCalendar("addEventSource", events);
		},
		// 달력 셀에 메모 표시 값 지정
		displayMemo(memo) {
			this.calendar.fullCalendar("renderEvent",
				{ text: memo.note, color: "#aaa", start: moment(memo.memoDate).format("YYYY-MM-DD") }
			);
		},
		kindMapValue(kind) {
			return TYPE_VALUE[kind];
		},
		// 현재 보고 있는 달력 거래 내역 다시 읽기.
		reload() {
			this.loadMonthData(this.currentMonth.toDate().getFullYear(), this.currentMonth.toDate().getMonth() + 1);
		},
		// 달력에 공휴일, 주요 행사 정보 표시
		holiydayDisplay() {
			let dayList = CalendarUtil.getAnniversary(this.currentMonth.toDate().getFullYear());
			dayList.forEach(value => {
				if (value.event.holiday) {
					$("td[data-date='" + value.date + "']").addClass("fc-holiday");
				}

				if ($("td[data-date='" + value.date + "'] span._day-label").length == 0) {
					$("td[data-date='" + value.date + "']").prepend("<span class='_day-label'>" + value.event.name + "</span>");
				} else {
					$("td[data-date='" + value.date + "'] span._day-label").append(", " + value.event.name);
				}
			});
		}
	},
	mounted() {
		this.initCalendar();
		// 지출, 이체, 수입 버튼 클릭
		$("._input").click(event => {
			let type = $(event.target).attr("data-type");
			this.addItemForm(type);
		});
		this.$EventBus.$on("reloadEvent", this.reload);
	}
};
</script>