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
					<label class="control-label text-right">관리지역</label>
					<select class="form-control input-xs" data-bind="options: $root.marketSelectOptions, optionsText:'text', optionsValue:'value', value: searchMarketSeq, optionsCaption: '전체'"></select>
				</div>
				<div class="col-md-3">
					<label class="control-label text-right">점포명</label>
					<input type="text" class="form-control input-xs" placeholder="점포명" data-bind="value: searchStoreName">
				</div>
				<div class="col-md-3">
					<label class="control-label text-right">담당자명</label>
					<input type="text" class="form-control input-xs" placeholder="담당자명" data-bind="value: searchManagerName">
				</div>
				<div class="col-md-3">
					<label class="control-label text-right">연락처</label>
					<input type="text" class="form-control input-xs" placeholder="연락처" data-bind="value: searchPhoneNo, valueUpdate: ['input', 'afterkeydown']" maxlength="13">
				</div>
			</div>
			<div class="form-group">
				<div class="col-md-3">
					<label class="control-label text-right">수신여부</label>
					<select class="form-control input-xs" data-bind="options: ezUtil.smsYnSelectOptions, optionsText:'text', optionsValue:'value', value: searchSmsReceiveYn, optionsCaption: '전체'"></select>
				</div>
				<div class="col-md-9">
					<label class="control-label text-right"></label>
					<div class="text-right">
						<button type="button" class="btn btn-primary" data-bind="click: $root.search">검색<i class="glyphicon glyphicon-search position-right"></i></button>
					</div>
				</div>
			</div>
			<!-- 검색 조건 끝  -->
        </form>
    </div>
</div>

<!-- 목록  -->
<div id="grid"></div>

<!-- Excel Download  -->
<div class="text-left" style="padding-top: 10px; float: left;">
	<a href="#" class="btn bg-teal btn-xs" data-bind="click: excelDownload ">Excel Download<i class="icon-file-excel position-right"></i></a>
	<sec:authorize access="hasRole('ROLE_STORE_MNG')">
	<a href="#" class="btn bg-teal btn-xs" data-bind="click: openExcelUpload" >점포 SMS수신대상 엑셀 등록<i class="icon-file-excel position-right"></i></a>
	</sec:authorize>
</div>

<!-- 추가  -->
<div class="text-right" style="padding-top: 10px; height: 32px;">
	<sec:authorize access="hasRole('ROLE_STORE_MNG')">
	<button type="button" data-bind="click: onCreateClick" class="btn btn-primary btn-xs"><b><i class="icon-plus3"></i></b> SMS 대상 등록</button>
	</sec:authorize>
</div>
<br/>



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
                        <span class="col-md-8"><i class="icon-cog4 position-left"></i> 점포 SMS 수신 대상 정보</span>
                    </legend>
                    <div class="col-md-6">
                        <div class="form-group">
                            <label for="smsUserSeq" class="col-md-4 control-label text-right">고유번호</label>
                            <div class="col-md-8">
                                <input type="text" id="smsUserSeq" class="form-control input-xs" name="smsUserSeq" data-bind="value: smsUserSeq, enable:false">
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="mngAreaName" class="col-md-4 control-label text-right">관제지역명</label>
                            <div class="col-md-8">
                                <input type="text" id="mngAreaName" class="form-control input-xs" name="mngAreaName" data-bind="value: mngAreaName, enable:false">
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="marketSeq" class="col-md-4 control-label text-right">관제시장명 <span class="text-danger">*</span></label>
                            <div class="col-md-8">
                                <select name="marketSeq" class="form-control input-xs" data-bind="options: $root.marketSelectOptions, optionsText:'text', optionsValue:'value', value: marketSeq, optionsCaption: '-- 선택 --', enable:$parent.isCreateMode"></select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="storeSeq" class="col-md-4 control-label text-right">점포명 <span class="text-danger">*</span></label>
                            <div class="col-md-8">
                                <select name="storeSeq" class="form-control input-xs" data-bind="options: $root.storeSelectOptions, optionsText:'text', optionsValue:'value', value: storeSeq, optionsCaption: '-- 선택 --', enable:$parent.isCreateMode"></select>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-6">
                    	<div class="form-group">
                            <label for="managerName" class="col-md-4 control-label text-right">이름<span class="text-danger">*</span></label>
                            <div class="col-md-8">
                                <input type="text" id="managerName" class="form-control input-xs" name="managerName" data-bind="value: managerName">
                            </div>
                        </div>
                    	<div class="form-group">
                            <label for="phoneNo" class="col-md-4 control-label text-right">휴대폰번호 <span class="text-danger">*</span></label>
                            <div class="col-md-8">
                                <input type="text" id="phoneNo" class="form-control input-xs" name="phoneNo" data-bind="value: phoneNo, valueUpdate: ['input', 'afterkeydown']" maxlength="13">
                            </div>
                        </div>
                        <div class="form-group">
							<label for="smsReceiveYn" class="col-md-4 control-label text-right">SMS 수신여부</label>
							<div class="col-md-8">
				                <select class="form-control input-xs" name="smsReceiveYn" data-bind="options: $root.smsYnSelectOptions, optionsText:'text', optionsValue:'value', value: smsReceiveYn"></select>
							</div>
						</div>
						<!-- <div class="form-group">
                            <label for="dutyName" class="col-md-4 control-label text-right">직책</label>
                            <div class="col-md-8">
                                <input type="text" id="dutyName" class="form-control input-xs" name="dutyName" data-bind="value: dutyName">
                            </div>
                        </div> -->
					</div>
					<div class="col-md-12"></div>
					<!-- ko if: $parent.isEditMode()  -->
                    <div class="col-md-6">
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
                    </div>
                    <div class="col-md-6">
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
                    </div>
                    <!-- /ko -->
                </fieldset>
            </div>
            <sec:authorize access="hasRole('ROLE_STORE_MNG')">
            <div class="text-right" data-bind="visible: isCreateMode">
                <button type="button" data-bind="click: cancel" class="btn btn-xs"><b><i class="icon-cross"></i></b> 취소</button>
                <button type="button" data-bind="click: insertData" class="btn btn-primary btn-xs"><b><i class="icon-checkmark3"></i></b> 등록</button>
            </div>
            <div class="text-right" data-bind="visible: isEditMode">
                <button type="button" data-bind="click: cancel" class="btn btn-xs"><b><i class="icon-cross"></i></b> 취소</button>
                <button type="button" data-bind="click: deleteData" class="btn btn-warning btn-xs"><b><i class="icon-minus3"></i></b> 삭제</button>
                <button type="button" data-bind="click: updateData" class="btn btn-primary btn-xs"><b><i class="icon-checkmark3"></i></b> 수정</button>
            </div>
            </sec:authorize>
        </div>
    </div>
</form>

<!-- Iframe 팝업 -->
<div>
    <div id="excelUploadModal" title="점포 sms수신대상 엑셀 대량 업로드" style="overflow:hidden !important"></div>
</div>
<!-- /Iframe 팝업 -->


<script type="text/javascript">
	setPageLocation("store/storesmsusermng", "점포 화재발생 시 SMS 문자 수신 대상자 관리");

    var pageParam = {
    		MARKET_CODE_MAP: ${ MARKET_CODE_MAP },
    		STORE_NAME_LIST: ${ STORE_NAME_LIST },
    		MNG_AREA_SEQ: '${ me.mngAreaSeq }',
    		MNG_AREA_NAME: '${ me.mngAreaName }'
	};

</script>

<script type="text/javascript" src="${contextPath}/app/store/storesmsusermng/storesmsusermng.js"></script>
