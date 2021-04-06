<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/layouts/include.jsp" %>

<script type="text/javascript">
	$(document).ready(function() {
		$(".page-container").addClass("login-container");
		$(".page-container").attr("style","min-height:950px;");
		$(".page-container .content").attr("style","width: 100% !important;");
	});
</script>
<!-- Simple login form -->
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
			<input type="password" name="password" class="form-control" placeholder="Password" data-bind="value: userPass, hasFocus: userPassFocus">
			<div class="form-control-feedback">
				<i class="icon-lock2 text-muted"></i>
			</div>
		</div>
		<div class="form-group">
			<button type="button" value="Login" class="btn btn-block"  style="background-color:#428bca; color: #ffffff; font-weight: bolder;" data-bind="click: doLogin">로그인<i class="icon-arrow-right14 position-right"></i></button>
		</div>
	</div>

</form>
<!-- /simple login form -->

<script>
	var errorMsg = '${errorMsg}';
</script>
<script type="text/javascript" src="${contextPath}/app/login/loginForm.js"></script>
