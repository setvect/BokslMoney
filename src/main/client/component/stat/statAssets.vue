<template>
	<div class="col-md-12 col-sm-12 col-xs-12">
		<div class="form-inline" style="padding: 0 10px;">
			<div class="form-group">
				시작년:
				<select class="form-control" v-model="fromYear">
					<option :value="year" v-for="year in yearList" :key="year">{{year}}년</option>
				</select>
			</div>
			<button type="submit" class="btn btn-default" style="margin: 0" @click="runStat()">조회</button>
		</div>

		<canvas id="assets_chart"></canvas>
	</div>
</template>

<script type="text/javascript">
import moment from "moment";

import VueUtil from "../../js/vue-util.js";

export default {
	data() {
		return {
			fromYear: new Date().getFullYear() - 1,
			accumulateOfMonth: {},
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
		},
		monthList() {
			return [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12];
		}
	},
	methods: {
		// 통계
		runStat() {
			this.year = this.yearChoice;
			let from = moment();
			let month = this.fromMonth;
			if (this.type == "year") {
				month = 1;
			}
			let fromDate = moment([this.fromYear, 0, 1]);
			VueUtil.get(
				"/settlement/statAssets.json",
				{ from: fromDate.valueOf() },
				result => {
					this.accumulateOfMonth = result.data;
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

			let data = $.map(this.accumulateOfMonth, (value, key) => value);
			let dataset = [
				{ label: "자산 변화", data: data, backgroundColor: "#ffa093" }
			];

			let assetData = {
				labels: $.map(this.accumulateOfMonth, (value, key) => {
					let label = moment(parseInt(key)).format("YYYY년MM월");
					return label;
				}),
				datasets: dataset
			};

			let ctx = document.getElementById("assets_chart").getContext("2d");
			this.chart = new Chart(ctx, {
				type: "line",
				data: assetData,
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