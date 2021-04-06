<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<%@include file="/WEB-INF/layouts/include.jsp" %>

<!-- Main navbar -->
<div class="navbar navbar-inverse">
	<div class="navbar-header">
		<a class="navbar-brand" href="${contextPath}/">
			<span style="font-size: 17px;font-weight: 400"><b>지능형 화재감지 시스템</b></span>
		</a>
		<ul class="nav navbar-nav visible-xs-block">
			<li><a data-toggle="collapse" data-target="#navbar-mobile"><i class="icon-user-tie"></i></a></li>
			<li><a class="sidebar-mobile-main-toggle"><i class="icon-paragraph-justify3"></i></a></li>
		</ul>
	</div>

	<!-- 모바일 용 메뉴 (화면 가로폭이 좁아졌을 때에만 보임) -->
	<div class="navbar-collapse collapse" id="navbar-mobile">
		<ul class="nav navbar-nav">
			<li><a class="sidebar-control sidebar-main-toggle hidden-xs"><i class="icon-paragraph-justify3"></i></a></li>
		</ul>

		<ul class="nav navbar-nav navbar-right">
			<li class="dropdown dropdown-user">
				<a class="dropdown-toggle" data-toggle="dropdown" aria-expanded="true">
					<i class="icon-user-tie"></i>
					<sec:authorize access="isAuthenticated()">
					<span>${me.username}</span>
					</sec:authorize>
					<sec:authorize access="!isAuthenticated()">
					<span>Anonymous</span>
					</sec:authorize>
					<i class="caret"></i>
				</a>
				<ul class="dropdown-menu dropdown-menu-right">
					<li><a href="javascript: openChangeUserInfoPopup()"><i class="icon-user-check"></i> 회원정보 변경</a></li>
					<li class="divider"></li>
					<sec:authorize access="isAuthenticated()">
					<li><a href="${contextPath}/logout"><i class="icon-switch2"></i> 로그아웃</a></li>
					</sec:authorize>
					<sec:authorize access="!isAuthenticated()">
					<li><a href="${contextPath}/login"><i class="icon-switch2"></i> 로그인</a></li>
					</sec:authorize>
				</ul>
			</li>
		</ul>
	</div>
	<!-- /모바일 용 메뉴 (화면 가로폭이 좁아졌을 때에만 보임) -->
</div>
<sec:authorize access="isAuthenticated()">
	<div>
	    <div id="changeUserInfoPopup" title="회원정보 변경" style="overflow:hidden !important"></div>
	</div>
</sec:authorize>
<sec:authorize access="isAuthenticated()">
<script type="text/javascript">
	/* 회원정보 변경 */
	var firstLoad = true;
	function openChangeUserInfoPopup(){
		if ( window['changeUserInfoPopupVM'] == null ){
			window.changeUserInfoPopupVM = new ChangeUserInfoPopupVM("#changeUserInfoPopup");
		}

		if ( !firstLoad && $('iframe[id=changeUserInfoPopup]')[0].contentWindow.rootVM != null
				&& (typeof $('iframe[id=changeUserInfoPopup]')[0].contentWindow.rootVM.init) === 'function' ){
			$('iframe[id=changeUserInfoPopup]')[0].contentWindow.rootVM.init();
		}
		firstLoad = false;

		window.changeUserInfoPopupVM.open();
	};

	/* 회원정보 변경 */
	function ChangeUserInfoPopupVM(elementId) {
		var self = this;
		self.clazz = "ChangeUserInfoPopupVM";

		self.popupId = elementId;
		self.iframeSrc = contextPath + "/home/openchangeuserinfo";

		//Close Callback
		 self.close = function() {
			self.popupElement.dialog('close');
		};

		self.popupOptions = {
				resizable : false,
				buttons: [],
				minWidth: 1100,
				width: 1100,
				minHeight: 600,
				height: 600
		}

		//Extend VM
		vmExtender.extendIframePopupVM(self);
	}



	$(document).ready(function() {
	});
</script>
</sec:authorize>