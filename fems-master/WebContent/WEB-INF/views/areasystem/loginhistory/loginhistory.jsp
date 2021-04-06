<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/layouts/include.jsp" %>

<!-- 검색  -->
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
			<!-- 검색조건  -->
			<div class="form-group">
				<div class="col-md-3">
					<label class="control-label text-right">사용자구분</label>
					<select class="form-control input-xs" data-bind="options: $root.appUserGradeSelectOptions, optionsText:'text', optionsValue:'value', value: searchRolegroupCode, optionsCaption: '전체'"></select>
				</div>
				<div class="col-md-3">
					<label class="control-label text-right">아이디</label>
					<input type="text" class="form-control input-xs" placeholder="아이디" data-bind="value: searchAdminId" style="ime-mode:inactive;">
				</div>
				<div class="col-md-3">
					<label class="control-label text-right">성명</label>
					<input type="text" class="form-control input-xs" placeholder="성명" data-bind="value: searchAdminName">
				</div>
			</div>
			<div class="form-group">
				<div class="col-md-3">
					<label class="control-label text-right">최근접속일 시작일</label>
					<input type="text" class="form-control input-xs" id="searchStartDate" data-bind="value: searchStartDate, rangeDatePicker: {value: searchStartDate, endElementId: 'searchEndDate', endValue: searchEndDate}" />
				</div>
				<div class="col-md-3">
					<label class="control-label text-right">최근접속일 종료일</label>
					<input type="text" class="form-control input-xs" id="searchEndDate" data-bind="value: searchEndDate"/>
				</div>
				<div class="col-md-6">
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
	setPageLocation("areasystem/loginhistory", "로그인한 사용자의 접속정보 이력을 조회");
 	var pageParam = {
 			APP_USER_GRADE_CODE_MAP: ${APP_USER_GRADE_CODE_MAP},
    		MARKET_CODE_MAP: ${MARKET_CODE_MAP},
    		MNG_AREA_CODE_MAP: ${MNG_AREA_CODE_MAP}
 	}
 	var serverTime = "${serverTime}";
</script>

<script type="text/javascript" src="${contextPath}/app/areasystem/loginhistory/loginhistory.js"></script>