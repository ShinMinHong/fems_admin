<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/layouts/include.jsp" %>
<!--@css(/awesome/css/swiper.min.css)-->
<link href="/css/home.css" rel="stylesheet" type="text/css">


<script type="text/javascript">
	$(document).ready(function() {
		$(".page-container").addClass("login-container");
		$(".page-container").attr("style","min-height:950px;");
		$(".page-container .content").attr("style","width: 100% !important;");
	});
</script>
<!-- Simple login form -->

<div class="login_wrap">
	<div class="login_title"><img src="/images/home/login_01.jpg"></div>
	<div class="login_back">
		<div id="login_back_img">
			<form id="loginForm" action="${contextPath}/loginProcess" method="POST" data-bind="enterKey: doLogin">
				<div class="panel panel-body login-form">
					<div class="text-center">
						<h5 class="content-group">지능형 화재감지 시스템<small class="display-block">에 오신 것을 환영합니다.</small></h5>
					</div>

					<div class="form-group has-feedback has-feedback-left">
						<input type="text" name="username" class="form-control" placeholder="Username" data-bind="value: userName, hasFocus: userNameFocus">
						<div class="form-control-feedback">
							<i class="icon-user text-muted"></i>
						</div>
					</div>

					<div class="form-group has-feedback has-feedback-left">
						<input type="password" name="password" class="form-control" placeholder="Password" autocomplete="off" data-bind="value: userPass, hasFocus: userPassFocus">
						<div class="form-control-feedback">
							<i class="icon-lock2 text-muted"></i>
						</div>
					</div>
					<div class="form-group">
						<button type="button" value="Login" class="btn btn-block"  style="background-color:#428bca; color: #ffffff; font-weight: bolder;" data-bind="click: doLogin">로그인<i class="icon-arrow-right14 position-right"></i></button>
					</div>
				</div>

			</form>
		</div>
	</div>
	<!-- /simple login form -->

	<div class="login_footer">
		<span><img src="/images/home/login_03.jpg"></span>
		<span>www.questar.co.kr</span>
	    <span>서울시 성동구 성수이로7길 27, 서울숲코오롱디지털타워2차 209호</span>
	    <span>TEL 02・465・0971</span>
	    FAX 02・465・0973
	</div>
</div>
<!-- ko with: changePasswordPopupVM -->
<div id="changePasswordPopup" style="display: none;" class="loginPopupContainer" data-bind="visible: isVisible">
	<div class="popupContents">
		<button class="popupCloseBtn" data-bind="click: close"></button>
		<div class="tabarea">
			<div class="on fullsize"><span>비밀번호 변경</span></div>
		</div>
		<div class="contentsarea">
			<form id="changePasswordForm" method="POST">
				<div class="titlearea sidepd40 nobg">
						<p data-bind="text: errorMsg"></p>
				</div>

				<div class="inputarea">
					<div class="inputgroup">
						<label>아이디</label>
						<input type="text" id="loginId" name="loginId" data-bind="value: loginId" maxlength="50" disabled="disabled">
					</div>
					<div class="inputgroup">
						<label>기존 비밀번호</label>
						<input type="password" id="originalPassword" name="originalPassword" maxlength="20" placeholder="기존 비밀번호" data-bind="value: originalPassword">
					</div>
					<hr />
					<div class="inputgroup">
						<label>신규 비밀번호</label>
						<input type="password" id="newPassword" name="newPassword" placeholder="신규 비밀번호" maxlength="20" data-bind="value: newPassword, event:{ change: onPwdChange}">
					</div>
					<div class="inputgroup">
						<label>비밀번호 확인</label>
						<input type="password" id="newPasswordRe" name="newPasswordRe" placeholder="비밀번호 확인" maxlength="20" data-bind="value: newPasswordRe, event:{ change: onPwdChange}">
					</div>
				</div>

				<div class="btnarea pt30">
					<button type="button" id="doChangePassword" class="bluebtn" data-bind="click: doChangePassword">비밀번호 변경</button>
				</div>
			</form>
		</div>
	</div>
	<div class="blockBg"></div>
</div>
<!--  /ko -->

<script>
	var tryUserName = '${tryUserName}';
	var loginFailType = '${loginFailType}';
	var errorMsg = '${errorMsg}';
</script>
<script type="text/javascript" src="${contextPath}/app/login/loginForm.js"></script>
