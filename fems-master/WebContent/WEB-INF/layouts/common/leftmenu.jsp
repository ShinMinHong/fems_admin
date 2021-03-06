<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/layouts/include.jsp" %>

<style type="text/css">
	img.leftmenuTitleIcon {
		display: none;
		margin: 0 auto;
	}

	@media (min-width: 769px) {
		.sidebar-xs .sidebar-main .navigation .media .media-body span {
			display: none;
		}

		.sidebar-xs .sidebar-main .navigation .media .media-body img.leftmenuTitleIcon {
			display: block;
		}
	}
</style>

<!-- Main sidebar -->
<div class="sidebar sidebar-main">
	<div class="sidebar-content">
		<!-- Main navigation -->
		<div class="sidebar-category sidebar-category-visible">
			<div class="category-content no-padding">
				<ul id="navigation" class="navigation navigation-main navigation-accordion">
					<div class="media">
						<div class="media-body align-center">
							<span class="align-center media-heading contract-title" style="font-size: 15px;">${me.mngAreaName} 화재감지 시스템</span>
							<sec:authorize access="hasRole('ROLE_SYSTEM_ADMIN') AND @fireAlarmCustomSecurityService.hasMngArea()">
								<button type="button" class="btn bg-teal-400 btn-labeled btn-labeled-left" onclick="document.location='/system/mngareamng/clearmngarea'"><b><i class="icon-select2"></i></b>관제지역변경</button>
							</sec:authorize>
						</div>
					</div>
					<sec:authorize access="hasRole('ROLE_SYSTEM_ADMIN') AND !@fireAlarmCustomSecurityService.hasMngArea()">
						<li><a href="#"><i class="icon-cog3"></i><span>시스템관리</span></a>
							<ul>
								<li><a href="${contextPath}/system/mngareamng" data-nav="system/mngareamng"><i class="icon-key"></i><span>관제지역관리</span><span class="label"><i class="icon-new-tab2" onclick="clickMenuOnNewTab(this, event)"></i></span></a></li>
								<li><a href="${contextPath}/system/rolemng" data-nav="system/rolemng"><i class="icon-key"></i><span>권한관리</span><span class="label"><i class="icon-new-tab2" onclick="clickMenuOnNewTab(this, event)"></i></span></a></li>
								<li><a href="${contextPath}/system/rolegroupmng" data-nav="system/rolegroupmng"><i class="icon-key"></i><span>권한그룹관리</span><span class="label"><i class="icon-new-tab2" onclick="clickMenuOnNewTab(this, event)"></i></span></a></li>
								<li><a href="${contextPath}/system/hqadminmng" data-nav="system/hqadminmng"><i class="icon-key"></i><span>통합관리자 관리</span><span class="label"><i class="icon-new-tab2" onclick="clickMenuOnNewTab(this, event)"></i></span></a></li>
								<li><a href="${contextPath}/system/hqsmsadminmng" data-nav="system/hqsmsadminmng"><i class="icon-key"></i><span>통합SMS수신자 관리</span><span class="label"><i class="icon-new-tab2" onclick="clickMenuOnNewTab(this, event)"></i></span></a></li>
								<li><a href="${contextPath}/system/firedetectordemonlog" data-nav="system/firedetectordemonlog"><i class="icon-key"></i><span>Demon로그조회</span><span class="label"><i class="icon-new-tab2" onclick="clickMenuOnNewTab(this, event)"></i></span></a></li>
							</ul>
						</li>
					</sec:authorize>
					<sec:authorize access="hasRole('ROLE_SYSTEM_ADMIN') AND !@fireAlarmCustomSecurityService.hasMngArea()">
						<li><a href="#"><i class="icon-cog3"></i><span>통합관리</span></a>
							<ul>
								<li><a href="${contextPath}/integration/totcontrolmap" data-nav="integration/totcontrolmap"><i class="icon-key"></i><span>통합관제지도</span><span class="label"><i class="icon-new-tab2" onclick="clickMenuOnNewTab(this, event)"></i></span></a></li>
							</ul>
						</li>
					</sec:authorize>
					<sec:authorize access="hasAnyRole('ROLE_CONTROLMAP') AND @fireAlarmCustomSecurityService.hasMngArea()">
						<li><a href="#"><i class="icon-cog3"></i><span>관제지도</span></a>
							<ul>
								<li><a href="${contextPath}/" data-nav=""><i class="icon-key"></i><span>관제지역 관리</span><span class="label"><i class="icon-new-tab2" onclick="clickMenuOnNewTab(this, event)"></i></span></a></li>
							</ul>
						</li>
					</sec:authorize>

					<sec:authorize access="hasAnyRole('ROLE_STORE_MNG', 'ROLE_STORE_READ') AND @fireAlarmCustomSecurityService.hasMngArea()">
						<li><a href="#"><i class="icon-satellite-dish2"></i><span>관제지역 점포 관리</span></a>
							<ul>
								<li><a href="${contextPath}/store/storemng" data-nav="store/storemng"><i class="icon-satellite-dish2"></i><span>관제지역 점포 관리</span><span class="label"><i class="icon-new-tab2" onclick="clickMenuOnNewTab(this, event)"></i></span></a></li>
								<li><a href="${contextPath}/store/storesmsusermng" data-nav="store/storesmsusermng"><i class="icon-satellite-dish2"></i><span>점포 SMS수신대상 관리</span><span class="label"><i class="icon-new-tab2" onclick="clickMenuOnNewTab(this, event)"></i></span></a></li>
							</ul>
						</li>
					</sec:authorize>

					<sec:authorize access="hasAnyRole('ROLE_FIRE_DETECTOR_MNG', 'ROLE_FIRE_DETECTOR_READ') AND @fireAlarmCustomSecurityService.hasMngArea()">
						<li><a href="#"><i class="icon-satellite-dish2"></i><span>화재감지기 관리</span></a>
							<ul>
								<li><a href="${contextPath}/firedetector/firedetectormng" data-nav="firedetector/firedetectormng"><i class="icon-satellite-dish2"></i><span>화재감지기 관리</span><span class="label"><i class="icon-new-tab2" onclick="clickMenuOnNewTab(this, event)"></i></span></a></li>
								<li><a href="${contextPath}/firedetector/firedetectoreventlog" data-nav="firedetector/firedetectoreventlog"><i class="icon-clipboard2"></i><span>화재감지기 이벤트 조회</span><span class="label"><i class="icon-new-tab2" onclick="clickMenuOnNewTab(this, event)"></i></span></a></li>
								<li><a href="${contextPath}/firedetector/lowbatterydetector" data-nav="firedetector/lowbatterydetector"><i class="icon-database-check"></i><span>배터리부족 감지기 조회</span><span class="label"><i class="icon-new-tab2" onclick="clickMenuOnNewTab(this, event)"></i></span></a></li>
								<li><a href="${contextPath}/firedetector/nosignaldetector" data-nav="firedetector/nosignaldetector"><i class="icon-database-check"></i><span>통신장애 감지기 조회</span><span class="label"><i class="icon-new-tab2" onclick="clickMenuOnNewTab(this, event)"></i></span></a></li>
							</ul>
						</li>
					</sec:authorize>
					<sec:authorize access="hasAnyRole('ROLE_STATISTICS_READ') AND @fireAlarmCustomSecurityService.hasMngArea()">
						<li><a href="#"><i class="icon-satellite-dish2"></i><span>통계</span></a>
							<ul>
								<li><a href="${contextPath}/statistics/storeeventstats" data-nav="statistics/storeeventstats"><i class="icon-satellite-dish2"></i><span>점포별 화재 신호 통계</span><span class="label"><i class="icon-new-tab2" onclick="clickMenuOnNewTab(this, event)"></i></span></a></li>
								<li><a href="${contextPath}/statistics/areaeventstats" data-nav="statistics/areaeventstats"><i class="icon-satellite-dish2"></i><span>지역별 화재 신호 통계</span><span class="label"><i class="icon-new-tab2" onclick="clickMenuOnNewTab(this, event)"></i></span></a></li>
								<li><a href="${contextPath}/statistics/montheventstats" data-nav="statistics/montheventstats"><i class="icon-satellite-dish2"></i><span>월별 화재 신호 통계</span><span class="label"><i class="icon-new-tab2" onclick="clickMenuOnNewTab(this, event)"></i></span></a></li>
							</ul>
						</li>
					</sec:authorize>
					<sec:authorize access="hasAnyRole('ROLE_MARKET_MNG', 'ROLE_MARKET_READ', 'ROLE_ADMIN_MNG', 'ROLE_ADMIN_READ') AND @fireAlarmCustomSecurityService.hasMngArea()">
						<li><a href="#"><i class="icon-cog3"></i><span>시스템관리(지역)</span></a>
							<ul>
								<sec:authorize access="hasAnyRole('ROLE_MARKET_MNG', 'ROLE_MARKET_READ')">
									<li><a href="${contextPath}/areasystem/marketmng" data-nav="areasystem/marketmng"><i class="icon-key"></i><span>관제지역 관리</span><span class="label"><i class="icon-new-tab2" onclick="clickMenuOnNewTab(this, event)"></i></span></a></li>
								</sec:authorize>
								<sec:authorize access="hasAnyRole('ROLE_ADMIN_MNG', 'ROLE_ADMIN_READ')">
									<li><a href="${contextPath}/areasystem/adminmng" data-nav="areasystem/adminmng"><i class="icon-key"></i><span>관리자 관리</span><span class="label"><i class="icon-new-tab2" onclick="clickMenuOnNewTab(this, event)"></i></span></a></li>
									<li><a href="${contextPath}/areasystem/firedetectorset" data-nav="areasystem/firedetectorset"><i class="icon-key"></i><span>화재감지기 설정 관리</span><span class="label"><i class="icon-new-tab2" onclick="clickMenuOnNewTab(this, event)"></i></span></a></li>
									<li><a href="${contextPath}/areasystem/loginhistory" data-nav="areasystem/loginhistory"><i class="icon-key"></i><span>접속정보 이력 관리</span><span class="label"><i class="icon-new-tab2" onclick="clickMenuOnNewTab(this, event)"></i></span></a></li>
									<li><a href="${contextPath}/areasystem/msgsendlog" data-nav="areasystem/msgsendlog"><i class="icon-key"></i><span>문자메시지 발신 관리</span><span class="label"><i class="icon-new-tab2" onclick="clickMenuOnNewTab(this, event)"></i></span></a></li>
									<li><a href="${contextPath}/areasystem/fire119sendlog" data-nav="areasystem/fire119sendlog"><i class="icon-key"></i><span>119 다매체 발신 관리</span><span class="label"><i class="icon-new-tab2" onclick="clickMenuOnNewTab(this, event)"></i></span></a></li>
									<li><a href="${contextPath}/areasystem/adminaudit" data-nav="areasystem/adminaudit"><i class="icon-key"></i><span>관리자 감사로그</span><span class="label"><i class="icon-new-tab2" onclick="clickMenuOnNewTab(this, event)"></i></span></a></li>
								</sec:authorize>
								<li><a href="${contextPath}/areasystem/securitysetting" data-nav="areasystem/securitysetting"><i class="icon-key"></i><span>보안설정 정보</span><span class="label"><i class="icon-new-tab2" onclick="clickMenuOnNewTab(this, event)"></i></span></a></li>
							</ul>
						</li>
					</sec:authorize>
					<li><a href="${contextPath}/firealarm-nas/public/downloads/manual.pdf" target="_blank"><i class="icon-file-download2"></i><span>사용자취급설명서 다운로드</span></a></li>
				</ul>
			</div>
		</div>
	</div>
</div>
<!-- /main sidebar -->
<script type="text/javascript">
	function clickMenuOnNewTab(target, event) {
		var newTab = window.open($(target).parents('a').attr('href'), '_blank');
		event.preventDefault();
		return false;
	}
</script>