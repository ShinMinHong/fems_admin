<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/layouts/include.jsp" %>

<!-- 상세 페이지  -->
 <form id="detailsForm" class="form-horizontal" action="#">
	<div class="panel-body">
		<div class="row" data-bind="with: data">
			<fieldset>
				<legend class="text-semibold" style="height:30px;">
					<span class="col-md-8"><i class="icon-lock5 position-left"></i> 사용자 상세정보</span>
				</legend>
				<!-- 상세 내용 -->
				<div class="col-md-6">
					<div class="form-group">
						<label class="col-md-4 control-label text-right" for="rolegroupCode" >사용자 구분</label>
						<div class="col-md-8">
							<input type="text" class="form-control input-xs" name="rolegroupCode" data-bind="value: appUserGradeSelectOptions" disabled="disabled"/ >
						</div>
					</div>
					<div class="form-group">
						<label class="col-md-4 control-label text-right" for="mngAreaName" >지역</label>
						<div class="col-md-8">
							<input type="text" class="form-control input-xs" name="mngAreaName" data-bind="value: mngAreaName" disabled="disabled"/ >
						</div>
					</div>
					<div class="form-group">
						<label class="col-md-4 control-label text-right" for="marketName" >시장</label>
						<div class="col-md-8">
							<input type="text" class="form-control input-xs" name="marketName" data-bind="value: marketName" disabled="disabled"/ >
						</div>
					</div>
					<div class="form-group">
						<label class="col-md-4 control-label text-right" for="adminId" >ID</label>
						<div class="col-md-8">
							<input type="text" class="form-control input-xs" name="adminId" data-bind="value: adminId" disabled="disabled"/ >
						</div>
					</div>
					<div class="form-group">
						<label class="col-md-4 control-label text-right" for="adminName">이름</label>
						<div class="col-md-8">
			                <input type="text" class="form-control input-xs" name="adminName" maxlength="20" data-bind="value: adminName"/>
						</div>
					</div>
					<div class="form-group">
						<label class="col-md-4 control-label text-right" for="adminName">직책</label>
						<div class="col-md-8">
			                <input type="text" class="form-control input-xs" name="dutyName" data-bind="value: dutyName"/>
						</div>
					</div>
					<div class="form-group">
						<label class="col-md-4 control-label text-right" for="phoneNo">휴대폰번호</label>
						<div class="col-md-8">
							<input type="text" class="form-control input-xs" name="phoneNo" data-bind="value: phoneNo" valueUpdate: ['input', 'afterkeydown']" maxlength="13" />
						</div>
					</div>
				</div>
				<div class="col-md-6">
					<div class="form-group">
						<label class="col-md-4 control-label text-right" for="smsReceiveYn">SMS 수신여부</label>
						<div class="col-md-8">
							<select class="form-control input-xs" name="smsReceiveYn" data-bind="options: ezUtil.smsYnSelectOptions, optionsText:'text', optionsValue:'value', value: smsReceiveYn"></select>
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
					<!-- ko if: checkChangePassword() -->
					<div class="form-group">
						<label class="col-md-4 control-label text-right" for="oldPassword">기존 비밀번호<span class="text-danger">*</span></label>
						<div class="col-md-8">
			                <input type="password" id="oldPassword" class="form-control input-xs" name="oldPassword" maxlength="20" autocomplete="off" data-bind="value: oldPassword, event:{ change: $parent.onOldPwdChange }"/>
						</div>
						<label data-bind="text:$parent.oldWrongPwdReason" class="col-md-8 control-label text-left text-warning" for="oldPassword"></label>
					</div>
					<div class="form-group">
						<label class="col-md-4 control-label text-right" for="adminPassword">신규 비밀번호<span class="text-danger">*</span></label>
						<div class="col-md-8">
			                <input type="password" id="adminPassword" class="form-control input-xs" name="adminPassword" maxlength="20" autocomplete="off" data-bind="value: adminPassword, event:{ change: $parent.onPwdChange}"/>
						</div>
					</div>
					<div class="form-group">
						<label class="col-md-4 control-label text-right" for="confirmPassword">신규 비밀번호 확인<span class="text-danger">*</span></label>
						<div class="col-md-8">
			                <input type="password" class="form-control input-xs" name="confirmPassword" maxlength="20" autocomplete="off" data-bind="value: confirmPassword, event:{ change: $parent.onPwdChange}"/>
						</div>
						<label data-bind="text:$parent.wrongPwdReason" class="col-md-8 control-label text-left text-warning" for="confirmPassword"></label>
					</div>
					<!-- /ko -->
					<div class="form-group">
						<label class="col-md-4 control-label text-right" for="lastLoginDate">최종로그인</label>
						<div class="col-md-8">
							<input type="text" class="form-control input-xs" name="lastLoginDate" data-bind="momentISO: lastLoginDate" disabled="disabled">
						</div>
					</div>
				</div>
			</fieldset>
		</div>
		<div class="text-right">
			<button type="button" data-bind="click: updateData" class="btn btn-primary btn-xs"> <b><i class="icon-checkmark3"></i></b> 수정 </button>
			<button type="button" data-bind="click: cancel" class="btn btn-xs"><b><i class="icon-cross"></i></b> 닫기</button>
		</div>
	</div>
</form>

<script type="text/javascript">
var pageParam = {
			ADMIN_SEQ : '${ADMIN_SEQ}',
			ROLEGROUP_CODE : ${ROLEGROUP_CODE},
			APP_USER_GRADE: ${APP_USER_GRADE},
			MNG_AREA_CODE_MAP : ${MNG_AREA_CODE_MAP},
			MARKET_CODE_MAP : ${MARKET_CODE_MAP}
	}
</script>

<script type="text/javascript" src="${contextPath}/app/commonpopup/openchangeuserinfo.js"></script>
