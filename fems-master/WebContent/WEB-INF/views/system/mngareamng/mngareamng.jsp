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
				<div class="col-md-6">
					<label class="control-label text-right">관제지역명</label>
					<input type="text" class="form-control input-xs" placeholder="관제지역명" data-bind="value: searchMngAreaName">
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

<!-- 추가  -->
<div class="text-right" style="padding-top: 10px;">
	<button type="button" data-bind="click: onCreateClick" class="btn btn-primary btn-xs"><b><i class="icon-plus3"></i></b> 관제지역 등록</button>
</div>
<br />

<!-- 상세페이지  -->
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
						<span class="col-md-8"><i class="icon-cog4 position-left"></i> 관제지역 정보</span>
					</legend>
					<div class="col-md-6">
						<div class="form-group">
							<label for="mngAreaSeq" class="col-md-4 control-label text-right">고유번호</label>
							<div class="col-md-8">
								<input type="text" id="mngAreaSeq" class="form-control input-xs" name="mngAreaSeq" data-bind="value: mngAreaSeq, enable:false">
							</div>
						</div>
						<div class="form-group">
							<label for="mngAreaName" class="col-md-4 control-label text-right">관제지역명 <span class="text-danger">*</span></label>
							<div class="col-md-8">
								<input type="text" id="mngAreaName" class="form-control input-xs" name="mngAreaName" data-bind="value: mngAreaName">
							</div>
						</div>
						<div class="form-group">
							<label for="managerName" class="col-md-4 control-label text-right">책임자 <span class="text-danger">*</span></label>
							<div class="col-md-8">
								<input type="text" id="managerName" class="form-control input-xs" name="managerName" data-bind="value: managerName">
							</div>
						</div>
						<div class="form-group">
							<label for="phoneNo" class="col-md-4 control-label text-right">휴대폰번호 <span class="text-danger">*</span></label>
							<div class="col-md-8">
								<input type="text" id="phoneNo" class="form-control input-xs" name="phoneNo" data-bind="value: phoneNo, valueUpdate: ['input', 'afterkeydown']" maxlength="13" >
							</div>
						</div>
						<div class="form-group">
							<label for="telephoneNo" class="col-md-4 control-label text-right">일반전화번호</label>
							<div class="col-md-8">
								<input type="text" id="telephoneNo" class="form-control input-xs" name="telephoneNo" data-bind="value: telephoneNo, valueUpdate: ['input', 'afterkeydown']" maxlength="13">
							</div>
						</div>
						<div class="form-group">
							<label for="noAlarmTime" class="col-md-4 control-label text-right">알림제한시간(분) <span class="text-danger">*</span></label>
							<div class="col-md-4">
								<input type="number" min="1" max="180" id="noAlarmTime" class="form-control input-xs" name="noAlarmTime" data-bind="value: noAlarmTime">
							</div>
							<div class="col-md-4">분</div>
						</div>
						<!--  ko if: $parent.isCreateMode() == false -->
						<div class="form-group">
							<label class="col-md-4 control-label text-right" for="regAdminId">등록자</label>
							<div class="col-md-8">
								<input type="text" class="form-control input-xs" name="regAdminId" data-bind="value: regAdminId" disabled="disabled">
							</div>
						</div>
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
							<label for="zipCode" class="col-md-4 control-label text-right">우편번호 <span class="text-danger">*</span></label>
							<div class="col-md-4">
								<input type="text" id="zipCode" class="form-control input-xs" name="zipCode" data-bind="value: zipCode, enable:false">
							</div>
							<div class="col-md-4">
								<button type="button" data-bind="click: $root.clickSearchAddress" class="btn bg-teal-400 btn-xs"><b><i class="icon-home4"></i></b> 주소검색</button>
							</div>
						</div>
						<div class="form-group">
							<label for="roadAddress" class="col-md-4 control-label text-right">주소(도로명)<span class="text-danger">*</span></label>
							<div class="col-md-8">
								<input type="text" id="roadAddress" class="form-control input-xs" name="roadAddress" data-bind="value: roadAddress, enable:false">
							</div>
						</div>
						<div class="form-group">
							<label for="parcelAddress" class="col-md-4 control-label text-right">주소(지번)<span class="text-danger">*</span></label>
							<div class="col-md-8">
								<input type="text" id="parcelAddress" class="form-control input-xs" name="parcelAddress" data-bind="value: parcelAddress, enable:false">
							</div>
						</div>
						<div class="form-group">
							<label for="latitude" class="col-md-4 control-label text-right">위도(중심좌표)<span class="text-danger">*</span></label>
							<div class="col-md-8">
								<input type="text" id="latitude" class="form-control input-xs" name="latitude" data-bind="value: latitude">
							</div>
						</div>
						<div class="form-group">
							<label for="longitude" class="col-md-4 control-label text-right">경도(중심좌표)<span class="text-danger">*</span></label>
							<div class="col-md-8">
								<input type="text" id="longitude" class="form-control input-xs" name="longitude" data-bind="value: longitude">
							</div>
						</div>
						<div class="form-group">
							<label for="scale" class="col-md-4 control-label text-right">지도 축척<span class="text-danger">*</span></label>
							<div class="col-md-8">
								<input type="number" min="1" max="10" id="scale" class="form-control input-xs" name="scale" data-bind="value: scale">
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-4 control-label text-right">알림대상</label>
							<div class="col-md-8">
								<input type="checkbox" name="isPush" data-bind="checked: alarmStore" /><b>점포담당자</b>&nbsp;&nbsp;
								<input type="checkbox" name="isPush" data-bind="checked: alarmMarket" /><b>시장관리자</b>&nbsp;&nbsp;
								<input type="checkbox" name="isPush" data-bind="checked: alarmArea" /><b>지역관리자</b>
							</div>
						</div>
						<!--  ko if: $parent.isCreateMode() == false -->
						<div class="form-group">
							<label class="col-md-4 control-label text-right" for="updAdminId">최종수정자</label>
							<div class="col-md-8">
								<input type="text" class="form-control input-xs" name="updAdminId" data-bind="value: updAdminId" disabled="disabled">
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-4 control-label text-right" for="updDate">최종수정일</label>
							<div class="col-md-8">
								<input type="text" class="form-control input-xs" name="updDate" data-bind="momentISO:updDate" disabled="disabled">
							</div>
						</div>
						<!-- /ko -->
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

<!-- Iframe 팝업 -->
<div id="selectLocationModal" title="위도,경도 좌표 선택" style="overflow:hidden !important;"></div>
<!-- /Iframe 팝업 -->

<script type="text/javascript">
	setPageLocation("system/mngareamng", "관할구역의 정보를 관리");
	var pageParam = {
		
	};
</script>
<script type="text/javascript" src="${contextPath}/app/system/mngareamng/mngareamng.js"></script>
<!-- 카카오맵 지도 SDK -->
<script src="https://t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
<!-- //카카오맵 지도 SDK -->