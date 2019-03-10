<template>
	<div class="col-md-12 col-sm-12 col-xs-12">
		<div class="form-inline" style="padding: 0 10px;">
			<div class="form-group">
				<select class="form-control" v-model="yearChoice">
					<option value>--결산 년도 선택--</option>
					<option :value="year" v-for="year in yearList" :key="year">{{ year }}년</option>
				</select>
			</div>
			<button type="submit" class="btn btn-default" style="margin: 0" @click="runStat()">조회</button>
		</div>

		<canvas id="kind_group_chart"></canvas>
	</div>
</template>
<script type="text/javascript">
import VueUtil from "../../js/vue-util.js";
import { TransactionMixin, AppUtil, TYPE_VALUE } from "../../js/bokslmoney.js";

export default {
	data() {
		return {
			year: new Date().getFullYear(),
			yearChoice: new Date().getFullYear(),
			kindGroupSum: {},
			chart: null
		};
	},
	computed: {
		yearList() {
			let years = [];
			let d = new Date();
			for (let y = 2007; y <= d.getFullYear(); y++) {
				years.push(y);
			}
			return years.reverse();
		}
	},
	methods: {
		// 통계
		runStat() {
			this.year = this.yearChoice;
			VueUtil.get(
				"/settlement/groupKindOfMonth.json",
				{ year: this.year },
				result => {
					this.kindGroupSum = result.data;
					this.$nextTick(() => {
						this.drawChart();
					});
				}
			);
		},
		drawChart() {
			if (this.chart) {
				this.chart.destroy();
			}
			// 월 목록
			let monthList = [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11];

			let dataset = $.map(TYPE_VALUE, (value, key) => {
				let data = monthList.map(m => {
					let monthData = this.kindGroupSum[m];
					if (!monthData) {
						return 0;
					}
					return monthData[key] || 0;
				});
				let item = {
					label: value.title,
					backgroundColor: value.color,
					data: data
				};
				return item;
			});
			let barChartData = {
				labels: monthList.map(m => m + 1 + "월"),
				datasets: dataset
			};

			let ctx = document.getElementById("kind_group_chart").getContext("2d");
			this.chart = new Chart(ctx, {
				type: "bar",
				data: barChartData,
				options: {
					elements: {
						rectangle: {
							borderWidth: 2,
							borderSkipped: "bottom"
						}
					},
					responsive: true,
					legend: {
						position: "top"
					},
					// 콤마 표시
					tooltips: {
						callbacks: {
							label: function(tooltipItem, data) {
								let value =
									data.datasets[tooltipItem.datasetIndex].data[
										tooltipItem.index
									];
								return (
									value
										.toString()
										.split(/(?=(?:...)*$)/)
										.join(",") + "원"
								);
							}
						}
					},
					scales: {
						yAxes: [
							{
								ticks: {
									beginAtZero: true,
									userCallback: function(value, index, values) {
										return value
											.toString()
											.split(/(?=(?:...)*$)/)
											.join(",");
									}
								}
							}
						]
					}
				}
			});
		}
	},
	mounted() {
		this.runStat();
	}
};
</script>
