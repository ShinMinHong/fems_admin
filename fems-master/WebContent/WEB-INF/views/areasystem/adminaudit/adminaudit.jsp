<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/layouts/include.jsp" %>

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
					<label class="control-label text-right">관리자ID</label>
					<input type="text" class="form-control input-xs" placeholder="관리자ID" data-bind="value: searchManagerId">
				</div>
				<div class="col-md-3">
					<label class="control-label text-right">작업명</label>
					<input type="text" class="form-control input-xs" placeholder="작업명" data-bind="value: searchActionName">
				</div>
				<div class="col-md-3">
					<label class="control-label text-right">검색 시작일</label>
					<input type="text" class="form-control input-xs" id="searchStartDate" data-bind="value: searchStartDate, rangeDatePicker: {value: searchStartDate, endElementId: 'searchEndDate', endValue: searchEndDate}" />
				</div>
				<div class="col-md-3">
					<label class="control-label text-right">검색 종료일</label>
					<input type="text" class="form-control input-xs" id="searchEndDate" data-bind="value: searchEndDate"/>
				</div>
			</div>
			<div class="form-group">
				<div class="col-md-12">
					<div class="text-right">
						<button type="button" class="btn btn-primary" data-bind="click: $root.search">검색<i class="glyphicon glyphicon-search position-right"></i></button>
					</div>
				</div>
			</div>
		</form>
	</div>
</div>

<div id="grid"></div>
<br/>

<form id="detailsForm" class="form-horizontal" action="#" data-bind="visible: detailsVM.isVisible">
	<div class="panel panel-flat">
		<div class="panel-heading">
			<h5 class="panel-title"><i class="icon-checkmark3 position-left"></i> 세부정보</h5>
			<div class="heading-elements">
				<ul class="icons-list">
					<li><a data-action="collapse"></a></li>
				</ul>
			</div>
			<a class="heading-elements-toggle"><i class="icon-menu"></i></a>
		</div>
		<div class="panel-body" data-bind="with: detailsVM">
			<div class="row" data-bind="with: data">
				<fieldset>
					<legend class="text-semibold" style="height:30px;">
						<span class="col-md-8"><i class="icon-lock5 position-left"></i> 관리자 감사정보</span>
					</legend>
					<div class="col-md-6">
						<div class="form-group">
							<label class="col-md-4 control-label text-right" for="menuName">작업메뉴명</label>
							<div class="col-md-8">
								<input type="text" class="form-control input-xs" name="menuName" data-bind="value: menuName" disabled="disabled"/>
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-4 control-label text-right" for="actionName">작업명</label>
							<div class="col-md-8">
								<input type="text" class="form-control input-xs" name="actionName" data-bind="value: actionName" disabled="disabled"/>
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-4 control-label text-right" for="actionDetail">작업상세설명</label>
							<div class="col-md-8">
								<textarea rows="8" class="form-control" name="actionDetail" data-bind="value: actionDetail" disabled="disabled" maxlength="1000"></textarea>
							</div>
						</div>
					</div>
					<div class="col-md-6">
						<div class="form-group">
							<label class="col-md-4 control-label text-right" for="managerId">관리자ID</label>
							<div class="col-md-8">
								<input type="text" class="form-control input-xs" name="managerId" data-bind="value: managerId" disabled="disabled"/>
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-4 control-label text-right" for="managerName">관리자명</label>
							<div class="col-md-8">
								<input type="text" class="form-control input-xs" name="managerName" data-bind="value: managerName" disabled="disabled"/>
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-4 control-label text-right" for="regDate">등록일</label>
							<div class="col-md-8">
								<input type="text" class="form-control input-xs" name="regDate" data-bind="momentISO: regDate" disabled="disabled"/>
							</div>
						</div>
					</div>
				</fieldset>
			</div>
			<div class="text-right">
				<button type="button" data-bind="click: cancel" class="btn btn-xs"><b><i class="icon-cross"></i></b> 닫기</button>
			</div>
		</div>
	</div>
</form>

<script type="text/javascript">
	setPageLocation("areasystem/adminaudit", "관리자감사로그");
	var serverTime = "${serverTime}";
</script>

<script type="text/javascript" src="${contextPath}/app/areasystem/adminaudit/adminaudit.js"></script>