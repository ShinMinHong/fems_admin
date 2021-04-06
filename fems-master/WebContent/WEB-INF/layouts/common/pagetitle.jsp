<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/layouts/include.jsp" %>

<!-- ko if: (clazz === 'HomeViewModel' || clazz === 'TotControlMapModel')  -->
<div class="breadcrumb-line" style="display:none;" data-bind="visible:(clazz === 'HomeViewModel' || clazz === 'TotControlMapModel')">
	<div class="visibleboard">
		<label class="col-md-11">
			<input type="checkbox" checked="checked" data-bind="checked: menuWrap">
			<span>상태 목록</span>
		</label>
	</div>
</div>
<!-- /ko -->

<!-- ko ifnot: (clazz === 'HomeViewModel' || clazz === 'TotControlMapModel') -->
<!-- ko if: !ezUtil.detectMobile() -->
<div class="breadcrumb-line" style="display:none;" data-bind="visible:(clazz !== 'HomeViewModel' && clazz !== 'TotControlMapModel')">
	<ul class="breadcrumb">
		<li class="fixed-breadcrumb"><a href="${contextPath}/login"><i class="icon-home2 position-left"></i> Home</a></li>
	</ul>
</div>
<!-- /ko -->
<!-- /ko -->

<div class="page-header">
	<div class="page-header-content">
		<div class="page-title">
			<h4>
				<i class="icon-arrow-left52 position-left"></i>
			</h4>
			<!-- Iframe 팝업 -->
			<div id="adminhelpPopup" title="도움말" style="overflow: hidden !important"></div>
			<!-- /Iframe 팝업 -->
		</div>
	</div>
</div>
<script type="text/javascript">
 	$(document).ready(function() {
 		$('#openAdminHelpPopup').click(function(){
 			openAdminHelpPopup();
 		});
 		function openAdminHelpPopup(){
 			if ( window['adminhelpPopupVM'] == null ){
 				window.adminhelpPopupVM = new AdminhelpPopupVM("#adminhelpPopup");
 			}
 			adminhelpPopupVM.open();
 		};
	});

	/**
	 * AdminhelpPopupVM - 페이지 도움말
	 */
	function AdminhelpPopupVM(elementId) {
		var self = this;
		self.clazz = "AdminhelpPopupVM";

		self.popupId = elementId;
		self.iframeUrl = contextPath + "/system/adminhelp?";
		self.iframeParam = $.param({pageCode : document.location.pathname.replace(contextPath, '')});
		self.iframeSrc = self.iframeUrl + self.iframeParam;

		self.popupOptions = { 	buttons: []
								/* 팝업창을 닫을 때 jQ Dialog 초기화.
								, close: function (event, ui){
	            					self.popupElement.dialog('destroy');
	            					$(self["popupId"]).empty();
	            					window['adminhelpPopupVM'] = null;
	            				} */
	            			};
		//Extend VM
		vmExtender.extendIframePopupVM(self);

		//Close Callback
/* 		self.close = function() {
			self.popupElement.dialog('close');
		}; */
	}

	function setPageLocation(dataNav, pageDesc, pageTitle) {
		var subMenu = $("#navigation > li ul > li > a[data-nav='" + dataNav + "']");
		if(subMenu.size()==0) {
			subMenu = $("#navigation > li ul > li > a:contains('" + contextPath + "/" + dataNav + "')");
		}

		if(subMenu.size()==0) {
			console.log("지정한 메뉴를 찾을 수 었습니다. " + subMenu);
		}

		var subMenuTitle = subMenu.text();
		var parentMenu = subMenu.parent().parents("li");
		var parentMenuTitle = parentMenu.find("> a span").text();

		//expand menu
		$(".navigation .active").removeClass("active"); //reset
		parentMenu.addClass("active").children("ul").show();
		parentMenu.children("ul").show();
		subMenu.parent().addClass("active")

		//breadcrumb 설정
		var breadcrumb = $(".breadcrumb-line ul.breadcrumb");
		breadcrumb.find("li:not(.fixed-breadcrumb)").remove(); //reset
		if( parentMenuTitle ) { breadcrumb.append("<li>" + parentMenuTitle + "</li>"); }

		if( subMenuTitle ) { breadcrumb.append("<li class='active'>" + subMenuTitle + "</li>"); }


		//pagetitle 설정
		pageTitle = pageTitle || subMenuTitle;
		var pageTitlePanel = $(".page-header .page-title h4");
		pageTitlePanel.find("span").remove(); //reset
		pageTitlePanel.append("<span class='text-semibold'>" + pageTitle + "</span>");
		if(!s.isBlank(pageDesc) && !ezUtil.detectMobile()) {
			/* 2019-10-03 SHH 도움말 설명을 위한 Panel 없이 타이틀 옆에 도움말만 표시
				var icon = "<a class='icon-question4 paddingleft5' id='openAdminHelpPopup' style='font-size: 18px;' title='도움말'></a>";
				pageTitlePanel.append("<span class='text-subtitle'>" + pageDesc + icon + "</span>"); */
			pageTitlePanel.append("<span class='text-subtitle'>" + pageDesc + "</span>");
		}
	};
</script>