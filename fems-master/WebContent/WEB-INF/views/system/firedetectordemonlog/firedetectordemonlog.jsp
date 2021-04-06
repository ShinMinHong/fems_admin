<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/layouts/include.jsp" %>

<!-- 검색창  -->
<div class="panel panel-flat" data-bind="with: searchData, enterKey: $root.search">
	<div class="panel-heading">
		<span class="text-semibold">검색조건</span>
		<div class="heading-elements">
			<ul class="icons-list">
				<li><a data-action="collapse"></a></li>
			</ul>
		</div>
		<a class="heading-elements-toggle"><i class="icon-menu"></i></a>
	</div>
	<div class="panel-body paddingbottom0">
		<form class="form-horizontal" role="form">
			<div class="form-group">
				<div class="col-md-3">
					<label class="control-label text-right">CTN번호</label>
					<input type="text" class="form-control input-xs" placeholder="CTN번호" data-bind="value: searchCtnNo">
				</div>
			 	<div class="col-md-3">
					<label class="control-label text-right">발생일시 시작일</label>
					<input type="text" class="form-control input-xs" id="searchStartDate" data-bind="value: searchStartDate, rangeDatePicker: {value: searchStartDate, endElementId: 'searchEndDate', endValue: searchEndDate}" />
				</div>
				<div class="col-md-3">
					<label class="control-label text-right">발생일시 종료일</label>
					<input type="text" class="form-control input-xs" id="searchEndDate" data-bind="value: searchEndDate"/>
				</div>
				<div class="col-md-3">
					<label class="control-label text-right"></label>
					<div class="text-right">
						<button type="button" class="btn btn-primary" data-bind="click: $root.search">검색<i class="glyphicon glyphicon-search position-right"></i></button>
					</div>
				</div>
			</div>
		</form>
	</div>
</div>

<!-- 목록  -->
<div id="grid"></div>

<script type="text/javascript">
	setPageLocation("system/firedetectordemonlog", "Demon 로그 조회");
	var pageParam = {
		
	};
 	var serverTime = "${serverTime}";
</script>
<script type="text/javascript" src="${contextPath}/app/system/firedetectordemonlog/firedetectordemonlog.js"></script>