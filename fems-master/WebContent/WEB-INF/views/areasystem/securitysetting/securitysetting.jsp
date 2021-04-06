<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/layouts/include.jsp" %>

<form id="detailsForm" class="form-horizontal" action="#">
	<div class="panel panel-flat">
		<div class="panel-heading">
			<h5 class="panel-title"><i class="icon-checkmark3 position-left"></i> 보안설정 설명</h5>
			<div class="heading-elements">
				<!-- <ul class="icons-list">
					<li><a data-action="collapse"></a></li>
				</ul> -->
			</div>
			<a class="heading-elements-toggle"><i class="icon-menu"></i></a>
		</div>
		<br/>
		<div class="panel-body">
			<div class="row" >
				<fieldset>
					<!-- 상세 내용 -->
						<div class="form-group">
							<label class="col-md-3 control-label text-right" for="rolegroupCode"><b>세션 타임아웃 :</b></label>
							<div class="col-md-9 control-label">30분 ( 아무 동작 없이 30분 초과 시 세션 타임아웃 됨. )</div>
						</div>
						<div class="form-group">
							<label class="col-md-3 control-label text-right" for="mngAreaSeq"><b>로그인 제한 :</b></label>
							<div class="col-md-9 control-label">
								<ui>
									<li>최초 로그인 시 비밀변호 재설정 후 로그인 가능.</li>
									<li>로그인 실패횟수 5회 초과시 로그인 제한 됨.</li>
									<li>비밀번호 변경일 6개월 초과 시 로그인 제한 됨.</li>
								</ui>
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-3 control-label text-right" for="marketSeq"><b>비밀번호 변경 주기 :</b></label>
							<div class="col-md-9 control-label">6개월</div>
						</div>

				</fieldset>
			</div>
		</div>
		<br/>
	</div>
</form>


<script type="text/javascript">
	setPageLocation("areasystem/securitysetting", "");
	// breadcrumb-line 표시를 위해 추가
	ko.applyBindings({clazz:'SecuritySettingVM'});
</script>
