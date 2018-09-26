<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html lang="ko">

<head>
	<jsp:include page="/WEB-INF/views/include/meta.inc.jsp"></jsp:include>
	<jsp:include page="/WEB-INF/views/include/style.inc.jsp"></jsp:include>

	<title>복슬머니</title>
</head>

<body class="nav-md">
	<div class="container body">
		<div class="main_container">
			<div class="col-md-3 left_col">
				<jsp:include page="/WEB-INF/views/include/left.inc.jsp"></jsp:include>
			</div>
			<jsp:include page="/WEB-INF/views/include/head.inc.jsp"></jsp:include>

			<!-- page content -->
			<div class="right_col" role="main">
				<div class="">
					<div class="page-title">
						<div class="title_left">
							<h3>달력 입력</h3>
						</div>
					</div>
					<div class="clearfix"></div>
					<div class="row">
						<div class="col-md-12 col-sm-12 col-xs-12">
							<div class="x_panel">

								<div class="x_content" id="app">
									<div id='calendar'></div>
								</div>

							</div>
						</div>
					</div>
				</div>
			</div>
			<!-- /page content -->
			<jsp:include page="/WEB-INF/views/include/footer.jsp"></jsp:include>
		</div>
	</div>


	<!-- calendar modal -->
	<div id="CalenderModalNew" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">

				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
					<h4 class="modal-title" id="myModalLabel">New Calendar Entry</h4>
				</div>
				<div class="modal-body">
					<div id="testmodal" style="padding: 5px 20px;">
						<form id="antoform" class="form-horizontal calender" role="form">
							<div class="form-group">
								<label class="col-sm-3 control-label">Title</label>
								<div class="col-sm-9">
									<input type="text" class="form-control" id="title" name="title">
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-3 control-label">Description</label>
								<div class="col-sm-9">
									<textarea class="form-control" style="height:55px;" id="descr" name="descr"></textarea>
								</div>
							</div>
						</form>
					</div>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default antoclose" data-dismiss="modal">Close</button>
					<button type="button" class="btn btn-primary antosubmit">Save changes</button>
				</div>
			</div>
		</div>
	</div>
	<div id="CalenderModalEdit" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">

				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
					<h4 class="modal-title" id="myModalLabel2">Edit Calendar Entry</h4>
				</div>
				<div class="modal-body">

					<div id="testmodal2" style="padding: 5px 20px;">
						<form id="antoform2" class="form-horizontal calender" role="form">
							<div class="form-group">
								<label class="col-sm-3 control-label">Title</label>
								<div class="col-sm-9">
									<input type="text" class="form-control" id="title2" name="title2">
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-3 control-label">Description</label>
								<div class="col-sm-9">
									<textarea class="form-control" style="height:55px;" id="descr2" name="descr"></textarea>
								</div>
							</div>

						</form>
					</div>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default antoclose2" data-dismiss="modal">Close</button>
					<button type="button" class="btn btn-primary antosubmit2">Save changes</button>
				</div>
			</div>
		</div>
	</div>

	<div id="fc_create" data-toggle="modal" data-target="#CalenderModalNew"></div>
	<div id="fc_edit" data-toggle="modal" data-target="#CalenderModalEdit"></div>
	<!-- /calendar modal -->

	<jsp:include page="/WEB-INF/views/include/script.inc.jsp"></jsp:include>
	<jsp:include page="/WEB-INF/views/include/common_modal.inc.jsp"></jsp:include>

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
				header: {
					left: 'prev,next today',
					center: 'title',
					right: 'month,agendaWeek,agendaDay,listMonth'
				},
				selectable: true,
				selectHelper: true,
				select: function (start, end, allDay) {
					$('#fc_create').click();

					started = start;
					ended = end;

					$(".antosubmit").on("click", function () {
						var title = $("#title").val();
						if (end) {
							ended = end;
						}

						categoryClass = $("#event_type").val();

						if (title) {
							calendar.fullCalendar('renderEvent', {
								title: title,
								start: started,
								end: end,
								allDay: allDay
							},
								true // make the event "stick"
							);
						}

						$('#title').val('');

						calendar.fullCalendar('unselect');

						$('.antoclose').click();

						return false;
					});
				},
				eventClick: function (calEvent, jsEvent, view) {
					$('#fc_edit').click();
					$('#title2').val(calEvent.title);

					categoryClass = $("#event_type").val();

					$(".antosubmit2").on("click", function () {
						calEvent.title = $("#title2").val();

						calendar.fullCalendar('updateEvent', calEvent);
						$('.antoclose2').click();
					});

					calendar.fullCalendar('unselect');
				},
				editable: true,
				events: [{
					title: 'All Day Event',
					start: new Date(y, m, 1)
				}, {
					title: 'Long Event',
					start: new Date(y, m, d - 5),
					end: new Date(y, m, d - 2)
				}, {
					title: 'Meeting',
					start: new Date(y, m, d, 10, 30),
					allDay: false
				}, {
					title: 'Lunch',
					start: new Date(y, m, d + 14, 12, 0),
					end: new Date(y, m, d, 14, 0),
					allDay: false
				}, {
					title: 'Birthday Party',
					start: new Date(y, m, d + 1, 19, 0),
					end: new Date(y, m, d + 1, 22, 30),
					allDay: false
				}, {
					title: 'Click for Google',
					start: new Date(y, m, 28),
					end: new Date(y, m, 29),
					url: 'http://google.com/'
				}]
			});
		};

		$(()=>{
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


</body>

</html>