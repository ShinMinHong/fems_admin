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
					<label class="control-label text-right">정렬순서</label>
 					<select class="form-control input-xs" data-bind="options: $root.storeSortSelectOption, optionsText:'text', optionsValue:'value', value: searchSort, optionsCaption: '등록일'"></select>
				</div>
				<div class="col-md-3">
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
	<a href="#" class="btn bg-teal btn-xs" data-bind="click: openstoreexcelupload" >점포 액셀등록<i class="icon-file-excel position-right"></i></a>
	<a href="#" class="btn bg-teal btn-xs" data-bind="click: openstoresmsuserexcelupload" >점포 SMS수신대상 액셀등록<i class="icon-file-excel position-right"></i></a>
	</sec:authorize>
</div>

<!-- 추가  -->
<div class="text-right" style="padding-top: 10px; height: 32px;">
	<sec:authorize access="hasRole('ROLE_STORE_MNG')">
	<button type="button" data-bind="click: onCreateClick" class="btn btn-primary btn-xs"><b><i class="icon-plus3"></i></b> 점포 등록</button>
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
        	<table>
        		<colgroup>
        			<col>
        		</colgroup>
        	</table>
            <div class="row" data-bind="with: data">
                <fieldset>
                    <legend class="text-semibold" style="height:30px;">
                        <span class="col-md-8"><i class="icon-cog4 position-left"></i> 관리지역 점포 정보</span>
                    </legend>
                    <div class="col-md-6">
                        <div class="form-group">
                            <label for="storeSeq" class="col-md-4 control-label text-right">고유번호</label>
                            <div class="col-md-8">
                                <input type="text" id="storeSeq" class="form-control input-xs" name="storeSeq" data-bind="value: storeSeq, enable:false">
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
                            <label for="storeName" class="col-md-4 control-label text-right">점포명 <span class="text-danger">*</span></label>
                            <div class="col-md-8">
                                <input type="text" id="storeName" class="form-control input-xs" name="storeName" data-bind="value: storeName">
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="managerName" class="col-md-4 control-label text-right">점주명</label>
                            <div class="col-md-8">
                                <input type="text" id="managerName" class="form-control input-xs" name="managerName" data-bind="value: managerName">
                            </div>
                        </div>
                        <div class="form-group">
							<label for="businessDesc" class="col-md-4 control-label text-right">업종설명</label>
							<div class="col-md-8">
				                <input type="text" id="businessDesc" class="form-control input-xs" name="businessDesc" data-bind="value: businessDesc">
							</div>
						</div>
                    </div>
                    <div class="col-md-6">
                    	<div class="form-group">
                            <label for="phoneNo" class="col-md-4 control-label text-right">휴대폰번호 <span class="text-danger">*</span></label>
                            <div class="col-md-8">
                                <input type="text" id="phoneNo" class="form-control input-xs" name="phoneNo" data-bind="value: phoneNo, valueUpdate: ['input', 'afterkeydown']" maxlength="13">
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="telephoneNo" class="col-md-4 control-label text-right">일반전화번호</label>
                            <div class="col-md-8">
                                <input type="text" id="telephoneNo" class="form-control input-xs" name="telephoneNo" data-bind="value: telephoneNo, valueUpdate: ['input', 'afterkeydown']" maxlength="13">
                                <!-- <input type="text" id="telephoneNo" class="form-control input-xs" name="telephoneNo" data-bind="value: telephoneNo" onKeyup="ezUtil.inputPhoneNumber(this)" maxlength="13"> -->
                            </div>
                        </div>
                        <div class="form-group">
							<label for="zipCode" class="col-md-4 control-label text-right">우편번호<span class="text-danger">*</span></label>
							<div class="col-md-4">
				                <input type="text" class="form-control input-xs" name="zipCode" data-bind="value: zipCode" disabled="disabled"/>
							</div>
							<div class="col-md-4">
								<button type="button" data-bind="click: $root.clickSearchAddress" class="btn bg-teal-400 btn-xs"><b><i class="icon-home4"></i></b> 주소검색</button>
							</div>
						</div>
						<div class="form-group">
							<label for="roadAddress" class="col-md-4 control-label text-right">주소(도로명)</label>
							<div class="col-md-8">
				                <input type="text" class="form-control input-xs" name="roadAddress" data-bind="value: roadAddress" disabled="disabled"/>
							</div>
						</div>
						<div class="form-group">
							<label for="parcelAddress" class="col-md-4 control-label text-right">주소(지번)</label>
							<div class="col-md-8">
				                <input type="text" class="form-control input-xs" name="parcelAddress" data-bind="value: parcelAddress" disabled="disabled"/>
							</div>
						</div>
						<div class="form-group">
							<label for="detailsAddress" class="col-md-4 control-label text-right">주소(상세)</label>
							<div class="col-md-8">
				                <input type="text" id="detailsAddress" class="form-control input-xs" name="detailsAddress" data-bind="value: detailsAddress">
							</div>
						</div>
                    </div>
                    <div class="col-md-6">
                    	<div class="form-group">
							<label for="smsAlarmYn" class="col-md-4 control-label text-right">SMS 알림</label>
							<div class="col-md-8">
				                <select class="form-control input-xs" name="smsAlarmYn" data-bind="options: $root.alarmYnSelectOptions, optionsText:'text', optionsValue:'value', value: smsAlarmYn"></select>
							</div>
						</div>
                    </div>
                    <div class="col-md-6">
                    	<div class="form-group">
							<label for="firestationAlarmYn" class="col-md-4 control-label text-right">119 알림</label>
							<div class="col-md-8">
				                <select class="form-control input-xs" name="firestationAlarmYn" data-bind="options: $root.alarmYnSelectOptions, optionsText:'text', optionsValue:'value', value: firestationAlarmYn"></select>
							</div>
						</div>
                    </div>
                    <div class="col-md-12"></div>
                    <!--  ko if: $parent.isCreateMode() == false -->
                    <div class="col-md-6">
                    	<div class="form-group">
							<label for="firestationName" class="col-md-4 control-label text-right">관할소방서</label>
							<div class="col-md-8">
				                <input type="text" id="firestationName" class="form-control input-xs" name="firestationName" data-bind="value: firestationName, enable:false">
							</div>
						</div>
						<div class="form-group">
							<label for="firestationManagerName" class="col-md-4 control-label text-right">소방서담당자</label>
							<div class="col-md-8">
				                <input type="text" id="firestationManagerName" class="form-control input-xs" name="firestationManagerName" data-bind="value: firestationManagerName, enable:false">
							</div>
						</div>
                    </div>
                    <div class="col-md-6">
                    	<div class="form-group">
							<label for="firestationTelephoneNo" class="col-md-4 control-label text-right">연기화재알림여부</label>
							<div class="col-md-8">
				                <select class="form-control input-xs" name="firestationAlarmYn" data-bind="options: $root.alarmYnSelectOptions, optionsText:'text', optionsValue:'value', value: smokeAlarmYn"></select>
							</div>
						</div>
						<div class="form-group">
							<label for="firestationTelephoneNo" class="col-md-4 control-label text-right">소방서연락처</label>
							<div class="col-md-8">
				                <input type="text" id="firestationTelephoneNo" class="form-control input-xs" name="firestationTelephoneNo" data-bind="value: firestationTelephoneNo, enable:false">
							</div>
						</div>
                    </div>
					<div class="col-md-12">
                    	<div class="form-group">
							<label for="businessDesc" class="col-md-2 control-label text-right">알림제한시간</label>
							<div class="col-md-10">
				                <div class="form-group">
		                            <div class="col-md-12">
	                                	<input type="checkbox" checked="checked" data-bind="checked: noAlarm00">
										<span style="margin-right: 15px;">00시</span>
										<input type="checkbox" checked="checked" data-bind="checked: noAlarm01">
										<span style="margin-right: 15px;">01시</span>
										<input type="checkbox" checked="checked" data-bind="checked: noAlarm02">
										<span style="margin-right: 15px;">02시</span>
										<input type="checkbox" checked="checked" data-bind="checked: noAlarm03">
										<span style="margin-right: 15px;">03시</span>
										<input type="checkbox" checked="checked" data-bind="checked: noAlarm04">
										<span style="margin-right: 15px;">04시</span>
										<input type="checkbox" checked="checked" data-bind="checked: noAlarm05">
										<span style="margin-right: 15px;">05시</span>
										<input type="checkbox" checked="checked" data-bind="checked: noAlarm06">
										<span style="margin-right: 15px;">06시</span>
										<input type="checkbox" checked="checked" data-bind="checked: noAlarm07">
										<span style="margin-right: 15px;">07시</span>
										<input type="checkbox" checked="checked" data-bind="checked: noAlarm08">
										<span style="margin-right: 15px;">08시</span>
										<input type="checkbox" checked="checked" data-bind="checked: noAlarm09">
										<span style="margin-right: 15px;">09시</span>
										<input type="checkbox" checked="checked" data-bind="checked: noAlarm10">
										<span style="margin-right: 15px;">10시</span>
										<input type="checkbox" checked="checked" data-bind="checked: noAlarm11">
										<span style="margin-right: 15px;">11시</span>
		                            </div>
		                            <div class="col-md-12">
	                                	<input type="checkbox" checked="checked" data-bind="checked: noAlarm12">
										<span style="margin-right: 15px;">12시</span>
										<input type="checkbox" checked="checked" data-bind="checked: noAlarm13">
										<span style="margin-right: 15px;">13시</span>
										<input type="checkbox" checked="checked" data-bind="checked: noAlarm14">
										<span style="margin-right: 15px;">14시</span>
										<input type="checkbox" checked="checked" data-bind="checked: noAlarm15">
										<span style="margin-right: 15px;">15시</span>
										<input type="checkbox" checked="checked" data-bind="checked: noAlarm16">
										<span style="margin-right: 15px;">16시</span>
										<input type="checkbox" checked="checked" data-bind="checked: noAlarm17">
										<span style="margin-right: 15px;">17시</span>
										<input type="checkbox" checked="checked" data-bind="checked: noAlarm18">
										<span style="margin-right: 15px;">18시</span>
										<input type="checkbox" checked="checked" data-bind="checked: noAlarm19">
										<span style="margin-right: 15px;">19시</span>
										<input type="checkbox" checked="checked" data-bind="checked: noAlarm20">
										<span style="margin-right: 15px;">20시</span>
										<input type="checkbox" checked="checked" data-bind="checked: noAlarm21">
										<span style="margin-right: 15px;">21시</span>
										<input type="checkbox" checked="checked" data-bind="checked: noAlarm22">
										<span style="margin-right: 15px;">22시</span>
										<input type="checkbox" checked="checked" data-bind="checked: noAlarm23">
										<span style="margin-right: 15px;">23시</span>
		                            </div>
		                        </div>
							</div>
						</div>
                    </div>
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
            <!-- SMS 수신대상 정보  -->
			<div class="col-md-12" data-bind="visible:isEditMode">
				<br>
				<fieldset>
					<legend class="text-semibold" style="height:30px;">
                        <span class="col-md-8"><i class="icon-cog4 position-left"></i> SMS 수신 대상자 관리</span>
                    </legend>
                    <div class="form-group" data-bind="with: data">
						<label for="managerName" class="col-md-1"></label>
						<div class= "col-md-11">
							<!-- ko foreach: storeSmsUserList  -->
							<div class="form-group">
								<label for="managerName" class="col-md-1 control-label text-right">이름</label>
								<div class="col-md-2">
									<input type="text" id="managerName" class="form-control input-xs" name="managerName" data-bind="value: managerName">
								</div>
								<label for="phoneNo" class="col-md-1 control-label text-right">연락처</label>
								<div class="col-md-2">
									<input type="text" id="phoneNo" class="form-control input-xs" name="phoneNo" data-bind="value: phoneNo" onKeyup="ezUtil.inputPhoneNumber(this)" maxlength="13">
								</div>
								<label for="smsReceiveYn" class="col-md-1 control-label text-right">수신여부</label>
								<div class="col-md-2">
									<select class="form-control input-xs" name="smsReceiveYn" data-bind="options: $root.smsYnSelectOptions, optionsText:'text', optionsValue:'value', value: smsReceiveYn"></select>
								</div>
								<!-- 삭제버튼  -->
								<div class="text-right col-md-1">
									<button class="btn btn-warning btn-xs" type="button" data-bind="click: $root.detailsVM.deleteSmsUser.bind(this)"><b><i class="icon-minus3"></i></b> 삭제</button>
								</div>
								<!--추가 버튼  -->
								<div class="text-right col-md-1">
									<button class="btn bg-teal-400 btn-xs" type="button" data-bind="click: $root.detailsVM.addSmsUser.bind(this)"><b><i class="icon-plus3"></i></b> 대상자 추가</button>
								</div>
							</div>
							<!-- /ko -->
						</div>
					</div>
				</fieldset>
			</div>
			<sec:authorize access="hasRole('ROLE_STORE_MNG')">
            <div class="text-right" data-bind="visible: isEditMode">
                <button type="button" data-bind="click: smsUserUpdateData" class="btn btn-primary btn-xs"><b><i class="icon-checkmark3"></i></b> SMS 수신 대상자 저장</button>
            </div>
            </sec:authorize>
        </div>
    </div>
</form>

<!-- Iframe 팝업 -->
<div>
 	<div id="openstoreexceluploadModal" title="점포 액셀등록" style="overflow:hidden !important"></div>
 	<div id="openstoresmsuserexceluploadModal" title="점포 SMS수신대상 액셀등록" style="overflow:hidden !important"></div>
 </div>
<!-- /Iframe 팝업 -->


<script type="text/javascript">
	setPageLocation("store/storemng", "관제지역 점포 관리");

    var pageParam = {
    		MARKET_CODE_MAP: ${ MARKET_CODE_MAP },
    		MNG_AREA_SEQ: '${ me.mngAreaSeq }',
    		MNG_AREA_NAME: '${ me.mngAreaName }'
	};

</script>

<script type="text/javascript" src="${contextPath}/app/store/storemng/storemng.js"></script>
<!-- 카카오맵 지도 SDK -->
<script src="https://t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
<!-- //카카오맵 지도 SDK -->