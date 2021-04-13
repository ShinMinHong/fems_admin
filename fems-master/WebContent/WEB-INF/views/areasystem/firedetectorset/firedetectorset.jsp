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
					<label class="control-label text-right">관리지역명</label>
					<select class="form-control input-xs" data-bind="options: $root.marketSelectOptions, optionsText:'text', optionsValue:'value', value: searchMarketSeq, optionsCaption: '전체'"></select>
				</div>
				 <div class="col-md-3">
					<label class="control-label text-right">점포명</label>
					<input type="text" class="form-control input-xs" placeholder="점포명" data-bind="value: searchStoreName"/>
				</div>
				<div class="col-md-3">
					<label class="control-label text-right">감지기설정상태</label>
 					<select class="form-control input-xs" data-bind="options: $root.fireDetectorSetType, optionsText:'text', optionsValue:'value', value: searchFireDetectorSetType, optionsCaption: '전체'"></select>
				</div>
				<div class="col-md-3">
					<label class="control-label text-right">CTN번호</label>
					<input type="text" class="form-control input-xs" placeholder="CTN" data-samask="0000-0000-0000" data-bind="value: searchCtnNo" maxlength="12" />
				</div>
			</div>
			<div class="form-group">
				<div class="col-md-3">
					<label class="control-label text-right">감지기설정 시작일시</label>
					<input type="text" class="form-control input-xs" id="searchStartDate" data-bind="value: searchStartDate, rangeDatePicker: {value: searchStartDate, endElementId: 'searchEndDate', endValue: searchEndDate}" />
				</div>
				<div class="col-md-3">
					<label class="control-label text-right">감지기설정 종료일시</label>
					<input type="text" class="form-control input-xs" id="searchEndDate" data-bind="value: searchEndDate"/>
				</div>
				<div class="col-md-3">
					<label class="control-label text-right">감지기설정 전송유무</label>
 					<select class="form-control input-xs" data-bind="options: $root.fireDetectorSortType, optionsText:'text', optionsValue:'value', value: searchSort, optionsCaption: '등록일'"></select>
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

<!-- 추가  -->
<div class="text-right" style="padding-top: 10px; height: 32px;">
	<sec:authorize access="hasRole('ROLE_FIRE_DETECTOR_MNG')">
	<button type="button" data-bind="click: onCreateClick" class="btn btn-primary btn-xs"><b><i class="icon-plus3"></i></b> 화재감지기 설정 등록</button>
	</sec:authorize>
</div>
<br/>

<!-- 상세  -->
<form id="detailsForm" class="form-horizontal" action="#" data-bind="visible: detailsVM.isVisible">
	<div class="panel panel-flat">
		<div class="panel-heading">
			<h5 class="panel-title"><i class="icon-checkmark3 position-left"></i> 상세정보</h5>
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
						<span class="col-md-8"><i class="icon-lock5 position-left"></i> 화재감지기 설정 관리 상세정보</span>
					</legend>
					<div class="col-md-6" >

						<div class="form-group">
							<label class="col-md-4 control-label text-right" for="fireDetectorSeq">고유번호</label>
							<div class="col-md-8">
								<input type="text" class="form-control input-xs" name="fireDetectorSeq" data-bind="value: fireDetectorSeq, enable:false" />
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-4 control-label text-right" for="mngAreaName">관제지역명</label>
							<div class="col-md-8">
								<input id="mngAreaName" type="text" class="form-control input-xs" name="mngAreaName" data-bind="value: mngAreaName, enable:false" />
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-4 control-label text-right" for="marketName">관리지역명</label>
							<div class="col-md-8">
								<input id="marketName" type="text" class="form-control input-xs" name="marketName" data-bind="value: marketName, enable:false" />
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-4 control-label text-right" for="storeName">점포명</label>
							<div class="col-md-8">
								<div class="col-md-7 nopadding">
				                	<input id="storeName" type="text" class="form-control input-xs" name="storeName" data-bind="value: storeName" readonly="readonly"/>
			                	</div>
			                	<div class="col-md-5 nopadding text-right">
			                		<button type="button" id="showSelectStoreModal" data-bind="click: $parent.showSelectStoreModal" class="btn bg-teal-400 btn-xs"><b><i class="icon-search4"></i></b> 점포선택</button>
			                	</div>
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-4 control-label text-right" for="storeName">CTN 번호<span class="text-danger">*</span></label>
							<div class="col-md-8">
								<div class="col-md-7 nopadding">
				                	<input id="storeName" type="text" class="form-control input-xs" name="ctnNo" data-bind="value: ctnNo" maxlength="12" />
			                	</div>
			                	<div class="col-md-5 nopadding text-right">
			                		<button type="button" id="showSelectStoreModal" data-bind="click: $parent.showSelectStoreModal" class="btn bg-teal-400 btn-xs"><b><i class="icon-search4"></i></b> CTN번호선택</button>
			                	</div>
							</div>
						</div>
					</div>
					<div class="col-md-6">
						<div class="form-group">
							<label class="col-md-4 control-label text-right" for="fireDetectorSetType">화재감지기 설정 구분<span class="text-danger">*</span></label>
							<div class="col-md-8">
			 					<select class="form-control input-xs" data-bind="options: $root.fireDetectorSetType, optionsText:'text', optionsValue:'value', value: fireDetectorSetType" disabled="disabled" ></select>
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-4 control-label text-right" for="fireDetectorSetStrDate">감지기 설정 시작일시<span class="text-danger">*</span></label>
							<div class="col-md-8">
								<input type="datetime-local" class="form-control input-xs" name="fireDetectorSetStrDate" data-bind="value: fireDetectorSetStrDate, rangeDatePicker: {value: fireDetectorSetStrDate, endElementId: 'fireDetectorSetStrDate', endValue: fireDetectorSetStrDate}" />
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-4 control-label text-right" for="fireDetectorSetEndDate">감지기 설정 종료일시</label>
							<div class="col-md-8">
								<input type="datetime-local" class="form-control input-xs" name="fireDetectorSetEndDate" data-bind="value: fireDetectorSetEndDate, rangeDatePicker: {value: fireDetectorSetEndDate, endElementId: 'fireDetectorSetEndDate', endValue: fireDetectorSetEndDate}" />
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-4 control-label text-right" for="fireDetectorSetSendYn">감지기설정 전송 여부<span class="text-danger">*</span></label>
							<div class="col-md-8">
								<select class="form-control input-xs" name="fireDetectorSetSendYn" data-bind="options: ezUtil.ynSelectOptions, optionsText:'text', optionsValue:'value', value: fireDetectorSetSendYn"></select>
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-4 control-label text-right" for="updDate">감지기설정 전송 일시</label>
							<div class="col-md-8">
								<input type="text" class="form-control input-xs" name="fireDetectorSetSendDate" data-bind="momentISO: fireDetectorSetSendDate" disabled="disabled"/>
							</div>
						</div>
					</div>
					<!-- ko if: $parent.isEditMode()  -->
					<div class="col-md-6">
						<div class="form-group">
							<label class="col-md-4 control-label text-right" for="regAdminId">등록자</label>
							<div class="col-md-8">
								<input type="text" class="form-control input-xs" name="regAdminId" data-bind="value: regAdminId" disabled="disabled"/>
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-4 control-label text-right" for="regDate">등록일</label>
							<div class="col-md-8">
								<input type="text" class="form-control input-xs" name="regDate" data-bind="momentISO: regDate " disabled="disabled"/>
							</div>
						</div>
					</div>
					<div class="col-md-6">
						<div class="form-group">
							<label class="col-md-4 control-label text-right" for="updAdminId">수정자</label>
							<div class="col-md-8">
								<input type="text" class="form-control input-xs" name="updAdminId" data-bind="value: updAdminId" disabled="disabled"/>
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-4 control-label text-right" for="updDate">최종수정일</label>
							<div class="col-md-8">
								<input type="text" class="form-control input-xs" name="updDate" data-bind="momentISO: updDate" disabled="disabled"/>
							</div>
						</div>
					</div>
					<!-- /ko -->
				</fieldset>
			</div>
			<div class="text-right" data-bind="visible: isEditMode">
				<button type="button" data-bind="click: cancel" class="btn btn-xs"><b><i class="icon-cross"></i></b> 닫기</button>
				<sec:authorize access="hasRole('ROLE_FIRE_DETECTOR_MNG')">
				<button type="button" data-bind="click: updateData" class="btn btn-primary btn-xs"><b><i class="icon-checkmark3"></i></b> 수정</button>
				<button type="button" data-bind="click: deleteData" class="btn btn-warning btn-xs"><b><i class="icon-minus3"></i></b> 삭제</button>
				</sec:authorize>
			</div>
			<br/>
			<br/>
		</div>
	</div>
</form>

<!-- Iframe 팝업 -->
<div>
	<div id="selectStoreModal" title="점포명선택" style="overflow:hidden !important;"></div>
</div>
<!-- /Iframe 팝업 -->

<script type="text/javascript">
    setPageLocation("areasystem/firedetectorset", "화재감지기 설정 관리");

    var pageParam = {
     	MARKET_CODE_MAP : ${MARKET_CODE_MAP},
     	AREA_CODE_MAP : ${AREA_CODE_MAP},
     	ME_MARKET_SEQ: '${me.marketSeq}',
     	ME_MNG_AREA_SEQ : '${me.mngAreaSeq}',
     	ME_ROLEGROUP_NAME : '${me.rolegroupCode}',
		FIRE_DETECTOR_SET_TYPE: ${FIRE_DETECTOR_SET_TYPE},
		ALLOWED_EXTENSION : "${ALLOWED_EXTENSION}" ,
		file_maxCnt : ${FILE_MAX_COUNT}
    }

 </script>

<script type="text/javascript" src="${contextPath}/app/areasystem/firedetectorset/firedetectorset.js"></script>