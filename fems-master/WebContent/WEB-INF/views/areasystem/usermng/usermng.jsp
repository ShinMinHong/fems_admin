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
					<label class="control-label text-right">아이디</label>
					<input type="text" class="form-control input-xs" placeholder="아이디" data-bind="value: searchUserId" style="ime-mode:inactive;">
				</div>
				<div class="col-md-3">
					<label class="control-label text-right">성명</label>
					<input type="text" class="form-control input-xs" placeholder="성명" data-bind="value: searchUserNm">
				</div>
				<!-- ko if: $root.isGradeHqadmin() -->
				<div class="col-md-3">
					<label class="control-label text-right">사용자구분</label>
					<select class="form-control input-xs" data-bind="options: $root.appUserGradeSelectOptions, optionsText:'text', optionsValue:'value', value: searchAppUserRole, optionsCaption: '전체'"></select>
				</div>
				<!-- /ko -->
				<!-- ko if: !$root.isGradeHqadmin() -->
				<div class="col-md-3">
					<label class="control-label text-right">사용자구분</label>
					<select class="form-control input-xs" data-bind="options: $root.searchBranchRoleSelectOptions, optionsText:'text', optionsValue:'value', value: searchAppUserRole, optionsCaption: '전체'"></select>
				</div>
				<!-- /ko -->
				<div class="col-md-3">
					<label class="control-label text-right">삭제여부</label>
					<select class="form-control input-xs" data-bind="options: ezUtil.searchDeleteYnSelectOptions, optionsText:'text', optionsValue:'value', value: searchDeleteAt"></select>
				</div>
			</div>
			<div class="form-group">
				<div class="col-md-3">
					<div class="checkbox">
						<label>
							<input type="checkbox" name="lastPasswordChange" data-bind="YNChecked: searchPswdExpired">비밀번호 변경주기 초과 조회
						</label>
					</div>
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
<div id="grid"></div>
<div class="text-left" style="padding-top: 10px; float: left;">
	<!-- ko if: me.authorgroupCode == "HQ_ADMIN" -->
	<a href="#" class="btn bg-teal" data-bind="click: openExcelUpload">대상처관리자 엑셀 등록<i class="icon-file-excel position-right"></i></a>
	<!-- /ko -->
	<!-- <a href="#" class="btn bg-teal" data-bind="click: excelDownload">Excel Download<i class="icon-file-excel position-right"></i></a> -->
</div>
<div class="text-right" style="padding-top: 10px;">
	<button type="button" data-bind="click: onCreateClick" class="btn btn-primary btn-xs"><b><i class="icon-plus3"></i></b> 사용자추가</button>
</div>
<br/>

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
						<span class="col-md-8"><i class="icon-lock5 position-left"></i> 사용자 상세정보</span>
					</legend>
					<div class="col-md-6">
						<!-- ko if: $parent.isCreateMode() -->
						<div class="form-group">
							<label class="col-md-4 control-label text-right" for="authorgroupCode">사용자 구분<span class="text-danger">*</span></label>
							<div class="col-md-8">
								<select class="form-control input-xs" name="authorgroupCode" data-bind="options: $root.appUserGradeSelectOptions, optionsText:'text', optionsValue:'value', value: authorgroupCode, optionsCaption: '선택', enable:$parent.isCreateMode"></select>
							</div>
						</div>
						<!-- /ko -->
						<!-- ko if: $parent.isEditMode() -->
						<div class="form-group">
							<label class="col-md-4 control-label text-right" for="authorgroupCode">사용자 구분<span class="text-danger">*</span></label>
							<div class="col-md-8">
								<input class="form-control input-xs" name="authorgroupCode" data-bind="value: $root.appUserGradeCodeMap[authorgroupCode()]" disabled="disabled"/>
							</div>
						</div>
						<!-- /ko -->
						<!-- ko if: $root.isGradeHqadmin() -->
						<!-- ko if: $parent.isCreateMode() -->
						<div class="form-group">
							<label class="col-md-4 control-label text-right" for=frsttCode>관할서<span class="text-danger">*</span></label>
							<div class="col-md-8">
								<select class="form-control input-xs" id=frsttCode name=frsttCode data-bind="options: $root.fireStationCodeMapSelectOptions, optionsText:'text', optionsValue:'value', value: frsttCode, optionsCaption: '선택', enable:$parent.isCreateMode"></select>
							</div>
						</div>
						<!-- /ko -->
						<!-- ko if: $parent.isEditMode() -->
						<div class="form-group">
							<label class="col-md-4 control-label text-right" for=frsttCode>관할서<span class="text-danger">*</span></label>
							<div class="col-md-8">
								<input type="text" class="form-control input-xs" name="frsttCode" data-bind="value: $root.fireStationCodeMap[frsttCode()]" disabled="disabled"/>
							</div>
						</div>
						<!-- /ko -->
						<!-- /ko -->
						<!-- ko if: !$root.isGradeHqadmin() -->
						<div class="form-group">
							<label class="col-md-4 control-label text-right" for=frsttCode>관할서<span class="text-danger">*</span></label>
							<div class="col-md-8">
								<input type="text" class="form-control input-xs" name="frsttCode" data-bind="value: $root.fireStationCodeMap[frsttCode()]" disabled="disabled"/>
							</div>
						</div>
						<!-- /ko -->
						<!-- ko if: $parent.isCreateMode() -->
						<div class="form-group">
							<label class="col-md-4 control-label text-right" for="userId">아이디<span class="text-danger">*</span></label>
							<div class="col-md-6">
				                <input type="text" class="form-control input-xs" name="userId" data-bind="value: userId" maxlength="50"/>
							</div>
							<div class="col-md-2">
				                <button type="button" id="checkDuplicateId" data-bind="click: $parent.checkDuplicateId" class="btn bg-teal-400 btn-xs"><b><i class="icon-checkmark3"></i></b> 중복체크</button>
							</div>
						</div>
						<!-- /ko -->
						<!-- ko if: $parent.isEditMode() -->
						<div class="form-group">
							<label class="col-md-4 control-label text-right" for="userId">아이디<span class="text-danger">*</span></label>
							<div class="col-md-8">
				                <input type="text" class="form-control input-xs" name="userId" data-bind="value: userId" disabled="disabled"/>
							</div>
						</div>
						<!-- /ko -->
						<!-- ko if: $parent.showAdminSelecter() -->
						<div class="form-group">
							<label class="col-md-4 control-label text-right" for="userNm">이름<span class="text-danger">*</span></label>
							<div class="col-md-8">
				                <input type="text" class="form-control input-xs" name="userNm" data-bind="value: userNm" maxlength="50"/>
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-4 control-label text-right" for="n119cnterNm">안전센터명<br/><font size=1px>(안전센터 직원인 경우만)</font></label>
							<div class="col-md-8">
				                <input type="text" class="form-control input-xs" name="n119cnterNm" data-bind="value: n119cnterNm"/>
							</div>
						</div>
						<!-- /ko -->
						<!-- ko if: $parent.showKeeperSelecter() -->
						<!-- ko if: $parent.isCreateMode() -->
						<div class="form-group">
							<label class="col-md-4 control-label text-right" for="ojtftNm">소방대상물<span class="text-danger">*</span></label>
							<div class="col-md-6">
				                <input type="text" class="form-control input-xs" name="ojtftNm" data-bind="value: ojtftNm" disabled="disabled"/>
							</div>
							<div class="col-md-2">
				                <button type="button" id="showSelectOjtftModal" data-bind="click: $parent.showSelectOjtftModal" class="btn bg-teal-400 btn-xs"><b><i class="icon-search4"></i></b> 소방대상물 검색</button>
							</div>
						</div>
						<!-- /ko -->
						<!-- ko if: $parent.isEditMode() -->
						<div class="form-group">
							<label class="col-md-4 control-label text-right" for="ojtftNm">소방대상물<span class="text-danger">*</span></label>
							<div class="col-md-8">
				                <input type="text" class="form-control input-xs" name="ojtftNm" data-bind="value: ojtftNm" disabled="disabled"/>
							</div>
						</div>
						<!-- /ko -->
						<!-- /ko -->
						<div class="form-group">
							<label class="col-md-4 control-label text-right" for="mbtlnum">연락처<span class="text-danger">*</span></label>
							<div class="col-md-8">
								<input type="text" class="form-control input-xs" name="mbtlnum" data-bind="value: mbtlnum, valueUpdate: ['input', 'afterkeydown']" maxlength="13"/>
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-4 control-label text-right" for="rspofcNm">직책</label>
							<div class="col-md-8">
				                <input type="text" class="form-control input-xs" name="rspofcNm" data-bind="value: rspofcNm"/>
							</div>
						</div>
					</div>
					<div class="col-md-6">
						<div class="form-group">
							<label class="col-md-4 control-label text-right" for="smsRcivrAt">알람SMS 수신여부<span class="text-danger">*</span></label>
							<div class="col-md-8">
								<select class="form-control input-xs" name="smsRcivrAt" data-bind="options: ezUtil.smsYnSelectOptions, optionsText:'text', optionsValue:'value', value: smsRcivrAt"></select>
							</div>
						</div>
						<!-- ko if: $parent.isEditMode() -->
						<div class="form-group">
							<label class="col-md-4 control-label text-right" for="deleteAt">삭제여부<span class="text-danger">*</span></label>
							<div class="col-md-4">
								<select class="form-control input-xs" name="deleteAt" data-bind="options: ezUtil.deleteYnSelectOptions, optionsText:'text', optionsValue:'value', value: deleteAt"></select>
							</div>
							<div class="col-md-4">
								<div style="margin-top: 5px; color: red;" data-bind="visible:deleteDt">
									<span class="text-semibold">(삭제일: </span><span class="text-semibold" data-bind="momentISO:deleteDt"></span><span class="text-semibold">)</span>
								</div>
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-4 control-label text-right" for="loginPosblAt">접속가능여부<span class="text-danger">*</span></label>
							<div class="col-md-8">
								<select class="form-control input-xs" name="loginPosblAt" data-bind="options: ezUtil.loginYnSelectOptions, optionsText:'text', optionsValue:'value', value: loginPosblAt"></select>
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-4 control-label text-right" for="checkChangePassword">비밀번호 변경</label>
							<div class="col-md-8">
								<div class="checkbox">
									<label> <input type="checkbox" checked="checked" data-bind="YNChecked: checkChangePassword">
									</label>
								</div>
							</div>
						</div>
						<!-- /ko -->
						<div class="form-group">
							<!-- ko if: $parent.enablePasswordPanel  -->
							<label class="col-md-4 control-label text-right" for="userPassword">비밀번호</label>
							<div class="col-md-8">
				                <input type="password" id="inputPassword" class="form-control input-xs" name="userPassword" maxlength="20" data-bind="value: userPassword, enable:$parent.enablePasswordPanel, event:{ change: $parent.onPwdChange}"/>
							</div>
							<!-- /ko -->
						</div>
						<div class="form-group">
							<!-- ko if: $parent.enablePasswordPanel -->
							<label class="col-md-4 control-label text-right" for="confirmPassword">비밀번호 확인</label>
							<div class="col-md-8">
				                <input type="password" class="form-control input-xs" name="confirmPassword" maxlength="20" data-bind="value: confirmPassword, enable:$parent.enablePasswordPanel, event:{ change: $parent.onPwdChange}"/>
							</div>
							<!-- /ko -->
							<div class="col-md-2"></div>
							<div class="col-md-4"></div>
							<label data-bind="visible:$parent.wrongPwd(), text:$parent.wrongPwdReason" class="col-md-8 control-label text-left text-warning" for="confirmPassword"></label>
						</div>
						<!-- ko if: $parent.isEditMode -->
						<!-- ko if: $parent.isUserBlockMode() -->
						<div class="form-group">
							<label class="col-md-4 control-label text-right" for="lastLoginDt">최종로그인</label>
							<div class="col-md-6">
								<input type="text" class="form-control input-xs" name="lastLoginDt" data-bind="momentISO: lastLoginDt" disabled="disabled"/>
							</div>
							<div class="col-md-2">
								<button type="button" data-bind="click: $parent.userAccessBlock" class="btn btn-warning btn-xs"><b><i class="icon-user-block"></i></b> 접속차단</button>
							</div>
						</div>
						<!-- /ko -->
						<!-- ko if: !$parent.isUserBlockMode() -->
						<div class="form-group">
							<label class="col-md-4 control-label text-right" for="lastLoginDt">최종로그인</label>
							<div class="col-md-8">
								<input type="text" class="form-control input-xs" name="lastLoginDt" data-bind="momentISO: lastLoginDt" disabled="disabled"/>
							</div>
						</div>
						<!-- /ko -->
						<div class="form-group">
							<label class="col-md-4 control-label text-right" for="lastPasswordChangeDt">최종 비밀번호 변경일</label>
							<div class="col-md-8">
								<input type="text" class="form-control input-xs" name="lastPasswordChangeDt" data-bind="momentISO: lastPasswordChangeDt" disabled="disabled"/>
							</div>
						</div>
						<!-- /ko -->
					</div>
					<!-- ko if: $parent.isEditMode -->
					<div class="col-md-12"></div>
					<div class="col-md-6">
						<div class="form-group">
							<label class="col-md-4 control-label text-right" for="registerId">등록자</label>
							<div class="col-md-8">
								<input type="text" class="form-control input-xs" name="registerId" data-bind="value: registerId" disabled="disabled"/>
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-4 control-label text-right" for="registDt">등록일</label>
							<div class="col-md-8">
								<input type="text" class="form-control input-xs" name="registDt" data-bind="momentISO: registDt" disabled="disabled"/>
							</div>
						</div>
					</div>
					<div class="col-md-6">
						<div class="form-group">
							<label class="col-md-4 control-label text-right" for="updusrId">수정자</label>
							<div class="col-md-8">
								<input type="text" class="form-control input-xs" name="updusrId" data-bind="value: updusrId" disabled="disabled"/>
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-4 control-label text-right" for="updtDt">최종수정일</label>
							<div class="col-md-8">
								<input type="text" class="form-control input-xs" name="updtDt" data-bind="momentISO: updtDt" disabled="disabled"/>
							</div>
						</div>
					</div>
					<!-- /ko -->
				</fieldset>
			</div>
			<div class="text-right" data-bind="visible: isCreateMode">
				<button type="button" data-bind="click: cancel" class="btn btn-xs"><b><i class="icon-cross"></i></b> 닫기</button>
				<button type="button" data-bind="click: insertData" class="btn btn-primary btn-xs"><b><i class="icon-checkmark3"></i></b> 등록</button>
			</div>
			<div class="text-right" data-bind="visible: isEditMode">
                <button type="button" data-bind="visible:isUserAccessMode, click: userAccessPossible" class="btn bg-teal-400 btn-xs"><b><i class="icon-user-block"></i></b> 접속차단해제</button>
				<button type="button" data-bind="click: cancel" class="btn btn-xs"><b><i class="icon-cross"></i></b> 닫기</button>
				<button type="button" data-bind="visible:isVisibleDeleteButton, click: deleteData" class="btn btn-warning btn-xs"><b><i class="icon-minus3"></i></b> 삭제</button>
				<button type="button" data-bind="click: updateData" class="btn btn-primary btn-xs"><b><i class="icon-checkmark3"></i></b> 수정</button>
			</div>
			<br/>
			<div class="col-md-6" data-bind="visible: isEditMode">
				<div class="panel-group panel-group-control panel-group-control-right" id="accordion-control-loginLogListGrid">
					<div class="panel panel-white">
						<div class="panel-heading">
							<h6 class="panel-title"> 최근 접속 이력 (10건)
								<a data-toggle="collapse" data-parent="#accordion-control-loginLogListGrid" href="#collapsible-loginLogListGrid" aria-expanded="true">
								</a>
							</h6>
						</div>
						<div id="collapsible-loginLogListGrid" class="panel-collapse collapse in" aria-expanded="true">
							<div class="panel-body">
								<div id=loginLogListGrid></div>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class="col-md-6" data-bind="visible: isEditMode">
				<div class="panel-group panel-group-control panel-group-control-right" id="accordion-control-pwdChangeLogListGrid">
					<div class="panel panel-white">
						<div class="panel-heading">
							<h6 class="panel-title"> 최근 비밀번호 변경 이력 (10건)
								<a data-toggle="collapse" data-parent="#accordion-control-pwdChangeLogListGrid" href="#collapsible-pwdChangeLogListGrid" aria-expanded="true">
								</a>
							</h6>
						</div>
						<div id="collapsible-pwdChangeLogListGrid" class="panel-collapse collapse in" aria-expanded="true">
							<div class="panel-body">
								<div id=pwdChangeLogListGrid></div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</form>

<!-- Iframe 팝업 -->
<div>
    <div id="selectOjtftModal" title="소방대상물 선택" style="overflow:hidden !important"></div>
</div>
<div>
	<div id="keeperExcelUploadModal" title="대상처 관리자 대량 업로드" style="overflow:hidden !important"></div>
</div>
<!-- /Iframe 팝업 -->

<script type="text/javascript" src="${contextPath}/app/areasystem/usermng/usermng.js"></script>
<script type="text/javascript">
	setPageLocation("areasystem/usermng", "사용자의 정보를 관리");
 	var pageParam = {
 			APP_USER_GRADE: ${APP_USER_GRADE},
 			FIRE_STATION_ALL_CODE_MAP: ${FIRE_STATION_ALL_CODE_MAP}
 	}
 	var meJSON = ${meJSON};
</script>