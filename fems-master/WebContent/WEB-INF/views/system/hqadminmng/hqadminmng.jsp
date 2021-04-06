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
					<label class="control-label text-right">아이디</label>
					<input type="text" class="form-control input-xs" placeholder="아이디" data-bind="value: searchAdminId" style="ime-mode:inactive;">
				</div>
				<div class="col-md-3">
					<label class="control-label text-right">이름</label>
					<input type="text" class="form-control input-xs" placeholder="이름" data-bind="value: searchAdminName" style="ime-mode:inactive;">
				</div>
				<div class="col-md-3">
					<label class="control-label text-right">SMS 수신여부</label>
					<select class="form-control input-xs" data-bind="options: ezUtil.smsYnSelectOptions, optionsText:'text', optionsValue:'value', value: searchSmsReceiveYn, optionsCaption: '전체'"></select>
				</div>
				<div class="col-md-3">
					<label class="control-label text-right">사용여부</label>
					<select class="form-control input-xs" data-bind="options: ezUtil.ynSelectOptions, optionsText:'text', optionsValue:'value', value: searchUseYn, optionsCaption: '전체'"></select>
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

<!-- 목록  -->
<div id="grid" style="height: 370px;"></div>

<!-- 버튼  -->
<div class="text-right" style="padding-top: 10px;">
	<sec:authorize access="hasRole('ROLE_ADMIN_MNG')">
	<button type="button" data-bind="click: onCreateClick" class="btn btn-primary btn-xs"><b><i class="icon-plus3"></i></b> 관리자 추가</button>
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
						<span class="col-md-8"><i class="icon-lock5 position-left"></i> 관리자 상세정보</span>
					</legend>
					<!-- 상세 내용 -->
					<div class="col-md-6">
						<!-- ko if: $parent.isCreateMode() -->
						<div class="form-group">
							<label class="col-md-4 control-label text-right" for="adminId">아이디<span class="text-danger">*</span></label>
							<div class="col-md-6">
				                <input type="text" class="form-control input-xs" name="adminId" data-bind="value: adminId"  maxlength="50"/ >
							</div>
							<div class="col-md-2">
				                <button type="button" id="checkDuplicateId" data-bind="click: $parent.checkDuplicateId" class="btn bg-teal-400 btn-xs"><b><i class="icon-checkmark3"></i></b> 중복체크</button>
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-4 control-label text-right" for="adminName">이름<span class="text-danger">*</span></label>
							<div class="col-md-8">
								<input type="text" class="form-control input-xs" name="adminName" data-bind="value: adminName" />
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-4 control-label text-right" for="adminPassword">비밀번호<span class="text-danger">*</span></label>
							<div class="col-md-8">
				                <input type="password" id="adminPassword" class="form-control input-xs" name="adminPassword"  maxlength="20" autocomplete="off" data-bind="value: adminPassword ,event:{ change: $parent.onPwdChange}"/>
							</div>
						</div>

						<div class="form-group">
							<label class="col-md-4 control-label text-right" for="confirmPassword">비밀번호 확인<span class="text-danger">*</span></label>
							<div class="col-md-8">
				                <input type="password" id="confirmPassword" class="form-control input-xs" name="confirmPassword" maxlength="20" autocomplete="off" data-bind="value: confirmPassword, event:{ change: $parent.onPwdChange}"/>
							</div>
							<label data-bind="text:$parent.wrongPwdReason" class="col-md-6 control-label text-left text-warning" for="confirmPassword"></label>
						</div>
						<!-- /ko -->
						<!-- ko if: $parent.isEditMode() -->
						<div class="form-group">
							<label class="col-md-4 control-label text-right" for="adminId">아이디<span class="text-danger">*</span></label>
							<div class="col-md-8">
				                <input type="text" class="form-control input-xs" name="adminId" data-bind="value: adminId" disabled="disabled"/>
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-4 control-label text-right" for="adminName">이름<span class="text-danger">*</span></label>
							<div class="col-md-8">
								<input type="text" class="form-control input-xs" name="adminName" data-bind="value: adminName" />
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-4 control-label text-right" for="checkChangePassword" >비밀번호 변경</label>
							<div class="col-md-8">
								<div class="checkbox">
									<label> <input type="checkbox" checked="checked" data-bind="checked: checkChangePassword">
									</label>
								</div>
							</div>
						</div>
						<!-- ko if:  checkChangePassword() -->
						<div class="form-group">
							<label class="col-md-4 control-label text-right" for="adminPassword">비밀번호<span class="text-danger">*</span></label>
							<div class="col-md-8">
				                <input type="password" id="adminPassword" class="form-control input-xs" name="adminPassword" maxlength="20" autocomplete="off" data-bind="value: adminPassword, event:{ change: $parent.onPwdChange}"/>
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-4 control-label text-right" for="confirmPassword">비밀번호 확인<span class="text-danger">*</span></label>
							<div class="col-md-8">
				                <input type="password" class="form-control input-xs" name="confirmPassword" maxlength="20" autocomplete="off" data-bind="value: confirmPassword, event:{ change: $parent.onPwdChange}"/>
							</div>
							<label data-bind="text:$parent.wrongPwdReason" class="col-md-6 control-label text-left text-warning" for="confirmPassword"></label>
						</div>
						<!-- /ko -->
						<!-- /ko -->
					</div>
					<div class="col-md-6">
						<div class="form-group">
							<label class="col-md-4 control-label text-right" for="dutyName">직책</label>
							<div class="col-md-8">
								<input type="text" class="form-control input-xs" name="dutyName" data-bind="value: dutyName" />
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-4 control-label text-right" for="phoneNo">휴대폰번호<span class="text-danger">*</span></label>
							<div class="col-md-8">
								<input type="text" class="form-control input-xs" name="phoneNo" data-bind="value: phoneNo" valueUpdate: ['input', 'afterkeydown']" maxlength="13" />
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-4 control-label text-right" for="smsReceiveYn">SMS 수신여부<span class="text-danger">*</span></label>
							<div class="col-md-8">
								<select class="form-control input-xs" name="smsReceiveYn" data-bind="options: ezUtil.smsYnSelectOptions, optionsText:'text', optionsValue:'value', value: smsReceiveYn"></select>
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-4 control-label text-right" for="useYn">사용여부<span class="text-danger">*</span></label>
							<div class="col-md-8">
								<select class="form-control input-xs" name="useYn" data-bind="options: ezUtil.ynSelectOptions, optionsText:'text', optionsValue:'value', value: useYn"></select>
							</div>
						</div>
					</div>
				</fieldset>
				<fieldset>
					<!-- 등록/수정  정보   -->
					<!-- ko if: $parent.isEditMode() -->
					<div class="col-md-6">
						<div class="form-group">
							<label class="col-md-4 control-label text-right" for="useYn">로그인 실패횟수<span class="text-danger">*</span></label>
							<div class="col-md-8">
								<input type="text" class="form-control input-xs" name="dutyName" data-bind="value: passwordFailCo" disabled="disabled" />
							</div>
						</div>
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
							<label class="col-md-4 control-label text-right" for="useYn">비밀번호 변경일자<span class="text-danger">*</span></label>
							<div class="col-md-8">
								<input type="text" class="form-control input-xs" name="dutyName" data-bind="momentISO: lastPwChangeDt " disabled="disabled" />
							</div>
						</div>
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
				<sec:authorize access="hasRole('ROLE_ADMIN_MNG')">
				<button type="button" data-bind="click: insertData" class="btn btn-primary btn-xs"><b><i class="icon-checkmark3"></i></b> 등록</button>
				</sec:authorize>
			</div>
			<div class="text-right" data-bind="visible: isEditMode">
				<button type="button" data-bind="click: cancel" class="btn btn-xs"><b><i class="icon-cross"></i></b> 닫기</button>
				<sec:authorize access="hasRole('ROLE_ADMIN_MNG')">
				<button type="button" data-bind="click: resetPswd" class="btn btn-success btn-xs"><b><i class="icon-undo"></i></b> 비밀번호 및 실패횟수 초기화</button>
				<button type="button" data-bind="click: updateData" class="btn btn-primary btn-xs"><b><i class="icon-checkmark3"></i></b> 수정</button>
				<button type="button" data-bind="click: deleteData" class="btn btn-warning btn-xs"><b><i class="icon-minus3"></i></b> 삭제</button>
				</sec:authorize>
			</div>
			<br/>
		</div>
	</div>
</form>

<script type="text/javascript">
    setPageLocation("system/hqadminmng", "통합관리자 관리");

    var pageParam = {
    		USER_GRADE_CODE_MAP: ${USER_GRADE_CODE_MAP}
	}
</script>

<script type="text/javascript" src="${contextPath}/app/system/hqadminmng/hqadminmng.js"></script>