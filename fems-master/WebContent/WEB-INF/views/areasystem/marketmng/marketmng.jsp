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
					<input type="text" class="form-control input-xs" placeholder="관리지역명" data-bind="value: searchMarketName">
				</div>
				<div class="col-md-9">
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

<!-- 엑셀 다운로드   -->
<div class="text-left" style="padding-top: 10px; float: left;">
	<a href="#" class="btn bg-teal btn-xs" data-bind="click: excelDownload ">Excel Download<i class="icon-file-excel position-right"></i></a>
</div>

<!-- 추가  -->
<div class="text-right" style="padding-top: 10px; height: 32px;">
	<sec:authorize access="hasRole('ROLE_MARKET_MNG')">
	<button type="button" data-bind="click: onCreateClick" class="btn btn-primary btn-xs"><b><i class="icon-plus3"></i></b> 전통시장 등록</button>
	</sec:authorize>
</div>
<br/>

<!-- 상세 페이지  -->
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
						<span class="col-md-8"><i class="icon-lock5 position-left"></i> 전통시장 상세정보</span>
					</legend>
					<div class="col-md-6" >

						<div class="form-group">
							<label class="col-md-4 control-label text-right" for="marketSeq">고유번호</label>
							<div class="col-md-8">
								<input type="text" class="form-control input-xs" name="marketSeq" data-bind="value: marketSeq, enable:false" />
							</div>
						</div>

						<div class="form-group">
							<label class="col-md-4 control-label text-right" for="mngAreaSeq">관제지역명<span class="text-danger">*</span></label>
 							<div class="col-md-8">
								<select name="mngAreaSeq" class="form-control input-xs" data-bind="options: $root.areaSelectOptions, optionsText:'text', optionsValue:'value', value: mngAreaSeq" disabled="disabled"></select>
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-4 control-label text-right" for="marketName">전통시장명<span class="text-danger">*</span></label>
							<div class="col-md-8">
								<input id="marketName" type="text" class="form-control input-xs" name="marketName" data-bind="value: marketName" />
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-4 control-label text-right" for="managerName">담당자<span class="text-danger">*</span></label>
							<div class="col-md-8">
								<input type="text" class="form-control input-xs" id="maangerName" name="managerName" data-bind="value: managerName" />
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-4 control-label text-right" for="phoneNo">휴대폰번호<span class="text-danger">*</span></label>
							<div class="col-md-8">
								<input type="text" class="form-control input-xs" name="phoneNo" data-bind="value: phoneNo" onKeyup="ezUtil.inputPhoneNumber(this)" maxlength="13"/>
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-4 control-label text-right" for="telephoneNo">일반전화번호</label>
							<div class="col-md-8">
								<input type="text" class="form-control input-xs" name="telephoneNo" data-bind="value: telephoneNo" onKeyup="ezUtil.inputPhoneNumber(this)" maxlength="13"/>
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-4 control-label text-right" for="firestationName">관할소방서</label>
							<div class="col-md-8">
								<input type="text" class="form-control input-xs" name="firestationName" data-bind="value: firestationName" />
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-4 control-label text-right" for="ctrdCode">지번주소코드<span class="text-danger">*</span></label>
							<div class="col-md-2">
								<input type="text" id="ctrdCode" class="form-control input-xs" name="ctrdCode" data-bind="value: ctrdCode" maxlength="2"/>
							</div>
							<div class="col-md-2">
								<input type="text" id="signguCode" class="form-control input-xs" name="signguCode" data-bind="value: signguCode" maxlength="3"/>
							</div>
							<div class="col-md-2">
								<input type="text" id="dongCode" class="form-control input-xs" name="dongCode" data-bind="value: dongCode" maxlength="3"/>
							</div>
							<div class="col-md-2">
								<input type="text" id="liCode" class="form-control input-xs" name="liCode" data-bind="value: liCode" maxlength="2"/>
							</div>
						</div>
					</div>
					<div class="col-md-6">
						<div class="form-group">
							<label class="col-md-4 control-label text-right" for="zipCode">우편번호<span class="text-danger">*</span></label>
							<div class="col-md-4">
								<input type="text" class="form-control input-xs" name="zipCode" data-bind="value: zipCode" disabled="disabled"/>
							</div>
							<div class="col-md-4">
								<button type="button" data-bind="click: $root.clickSearchAddress" class="btn bg-teal-400 btn-xs"><b><i class="icon-home4"></i></b> 주소검색</button>
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-4 control-label text-right" for="roadAddress">주소(도로명)<span class="text-danger">*</span></label>
							<div class="col-md-8">
								<input type="text" class="form-control input-xs" name="roadAddress" data-bind="value: roadAddress" disabled="disabled"/>
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-4 control-label text-right" for="parcelAddress">주소(지번)<span class="text-danger">*</span></label>
							<div class="col-md-8">
								<input type="text" class="form-control input-xs" name="parcelAddress" data-bind="value: parcelAddress" disabled="disabled" />
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-4 control-label text-right" for="latitude">위도(중심좌표)<span class="text-danger">*</span></label>
							<div class="col-md-8">
								<input type="text" class="form-control input-xs" name="latitude" data-bind="value: latitude" disabled="disabled" />
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-4 control-label text-right" for="longitude">경도(중심좌표)<span class="text-danger">*</span></label>
							<div class="col-md-8">
								<input type="text" class="form-control input-xs" name="longitude" data-bind="value: longitude" disabled="disabled" />
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-4 control-label text-right" for="scale">지도축적<span class="text-danger">*</span></label>
							<div class="col-md-8">
								<input type="text" class="form-control input-xs" name="scale" data-bind="value: scale" />
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-4 control-label text-right" for="firestationManagerName">소방서담당자</label>
							<div class="col-md-8">
								<input type="text" class="form-control input-xs" name="firestationManagerName" data-bind="value: firestationManagerName" />
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-4 control-label text-right" for="firestationTelephoneNo">소방서연락처</label>
							<div class="col-md-8">
								<input type="text" class="form-control input-xs" name="firestationTelephoneNo" data-bind="value: firestationTelephoneNo" onKeyup="ezUtil.inputPhoneNumber(this)" maxlength="13" />
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
			<div class="text-right" data-bind="visible: isCreateMode">
				<button type="button" data-bind="click: cancel" class="btn btn-xs"><b><i class="icon-cross"></i></b> 닫기</button>
				<sec:authorize access="hasRole('ROLE_MARKET_MNG')">
				<button type="button" data-bind="click: insertData" class="btn btn-primary btn-xs"><b><i class="icon-checkmark3"></i></b> 등록</button>
				</sec:authorize>
			</div>
			<div class="text-right" data-bind="visible: isEditMode">
				<button type="button" data-bind="click: cancel" class="btn btn-xs"><b><i class="icon-cross"></i></b> 닫기</button>
				<sec:authorize access="hasRole('ROLE_MARKET_MNG')">
				<button type="button" data-bind="click: updateData" class="btn btn-primary btn-xs"><b><i class="icon-checkmark3"></i></b> 수정</button>
				<button type="button" data-bind="click: deleteData" class="btn btn-warning btn-xs"><b><i class="icon-minus3"></i></b> 삭제</button>
				</sec:authorize>
			</div>
			<br/>
		</div>
	</div>
</form>

<!-- Iframe 팝업 -->
<div id="selectLocationModal" title="위도,경도 좌표 선택" style="overflow:hidden !important;"></div>
<!-- /Iframe 팝업 -->


<script type="text/javascript">
    setPageLocation("areasystem/marketmng", "전통시장  관리");

    var pageParam = {
    		MNG_AREA_CODE_MAP: ${MNG_AREA_CODE_MAP},
    		MNG_AREA_SEQ: '${ me.mngAreaSeq }'
	};
</script>

<script type="text/javascript" src="${contextPath}/app/areasystem/marketmng/marketmng.js"></script>
<!-- 카카오맵 지도 SDK -->
<script src="https://t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
