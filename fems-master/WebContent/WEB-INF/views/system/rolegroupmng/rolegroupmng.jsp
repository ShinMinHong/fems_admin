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
			<div class="form-group">
				<div class="col-md-3">
					<label class="control-label text-right">권한그룹코드</label>
					<input type="text" class="form-control input-xs" placeholder="권한그룹코드" data-bind="value: searchRolegroupCode">
				</div>
				<div class="col-md-3">
					<label class="control-label text-right">권한그룹명</label>
					<input type="text" class="form-control input-xs" placeholder="권한그룹명" data-bind="value: searchRolegroupName">
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
<div class="paddingbottom10 text-right">
</div>
<div id="grid"></div>
<div class="text-right" style="padding-top: 10px;">
	<button type="button" data-bind="click: onCreateClick" class="btn btn-primary btn-xs"><b><i class="icon-plus3"></i></b> 권한그룹추가</button>
</div>
<br />

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
						<span class="col-md-8"><i class="icon-lock5 position-left"></i> 권한그룹 정보</span>
					</legend>
					<div class="col-md-6">
						<div class="form-group">
							<label for="rolegroupCode" class="col-md-4 control-label text-right">권한그룹코드 <span class="text-danger">*</span></label>
							<div class="col-md-8">
								<input type="text" id="rolegroupCode" class="form-control input-xs" name="rolegroupCode" data-bind="value: rolegroupCode, enable:$parent.isCreateMode">
							</div>
						</div>
						<div class="form-group">
							<label for="rolegroupName" class="col-md-4 control-label text-right">권한그룹명 <span class="text-danger">*</span></label>
							<div class="col-md-8">
								<input type="text" id="rolegroupName" class="form-control input-xs" name="rolegroupName" data-bind="value: rolegroupName">
							</div>
						</div>
						<!--  ko if: $parent.isCreateMode() == false -->
						<div class="form-group">
							<label class="col-md-4 control-label text-right" for="regDate">등록일</label>
							<div class="col-md-8">
								<input type="text" class="form-control input-xs" name="regDate" data-bind="momentISO:regDate" disabled="disabled">
							</div>
						</div>
						<!-- /ko -->
					</div>
					<div class="col-md-6">
						<div class="form-group">
							<label class="col-md-4 control-label text-left" for="roleList">연결된 권한코드</label>
						</div>
						<div class="form-group">
							<div class="col-md-8" data-bind="foreach: roleDetailsList">
								<div class="checkbox">
									<label>
										<input type="checkbox" checked="checked" data-bind="checked: checked">
										<span data-bind="text:roleCode + ' - ' + roleName"></span>
									</label>
								</div>
							</div>
						</div>
					</div>
				</fieldset>
			</div>
			<div class="text-right" data-bind="visible: isCreateMode">
				<button type="button" data-bind="click: cancel" class="btn btn-xs"><b><i class="icon-cross"></i></b> 취소</button>
				<button type="button" data-bind="click: insertData" class="btn btn-primary btn-xs"><b><i class="icon-checkmark3"></i></b> 등록</button>
			</div>
			<div class="text-right" data-bind="visible: isEditMode">
				<button type="button" data-bind="click: cancel" class="btn btn-xs"><b><i class="icon-cross"></i></b> 취소</button>
				<button type="button" data-bind="click: deleteData" class="btn btn-warning btn-xs"><b><i class="icon-minus3"></i></b> 삭제</button>
				<button type="button" data-bind="click: updateData" class="btn btn-primary btn-xs"><b><i class="icon-checkmark3"></i></b> 수정</button>
			</div>
		</div>
	</div>
</form>

<script type="text/javascript">
	setPageLocation("system/rolegroupmng", "권한그룹의 정보를 관리");
	var pageParam = {
		roleList: ${ roleList }
	};
</script>

<script type="text/javascript" src="${contextPath}/app/system/rolegroupmng/rolegroupmng.js"></script>