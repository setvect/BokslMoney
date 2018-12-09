<%@ page contentType="text/html; charset=UTF-8"%>
<template id='stat-stat-assets'>
	<div class="col-md-12 col-sm-12 col-xs-12">
		<div class="form-inline" style="padding: 0 10px;">
			<div class="form-group">
				<label><input type="radio" class="form-control" value="year" v-model="type" />년단위</label>
				<label><input type="radio" class="form-control" value="month" v-model="type" />월단위</label>
				시작일:
				<select class="form-control" v-model="fromYear">
					<option :value="year" v-for="year in yearList">{{year}}년</option>
				</select>
				<select class="form-control" v-model="fromMonth" :disabled="type =='year'">
					<option :value="month" v-for="month in monthList">{{month}}월</option>
				</select>
			</div>
			<button type="submit" class="btn btn-default" style="margin: 0" @click="runStat()">조회</button>
		</div>

		<canvas id="canvas"></canvas>
	</div>
</template>

<script type="text/javascript">

	const statAssetsComponent = Vue.component("statAssets", {
		template: '#stat-stat-assets',
		data() {
			return {
				type: "year",
				fromYear: (new Date()).getFullYear() - 1,
				fromMonth: (new Date()).getMonth(),
				sumKindOfMonth: [],
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
				if (this.type == 'year') {
					month = 1;
				}
				let fromDate = moment([this.fromYear, month - 1, 1]);
				VueUtil.get("/hab/settlement/statAssets.json", { from: fromDate.valueOf() }, (result) => {
					this.sumKindOfMonth = result.data;
					this.$nextTick(() => {
						this.drawChart();
					})
				});
			},
			drawChart() {
				console.log("call drawChart()");
			}
		},
		mounted() {
			this.runStat();
		},
	});
</script>