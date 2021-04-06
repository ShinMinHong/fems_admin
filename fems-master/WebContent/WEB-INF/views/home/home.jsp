<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/layouts/include.jsp" %>

<script type="text/html" id="fire-detector-template">
	<li class="item" data-bind="click: $parent.clickDetectorInGrid">
		<span data-bind="attr: { class: getListRowCssClassByErrorStatus(errorStatus) }"></span>
		<div class="info">
			<h5>[<!--ko text: marketName--><!--/ko-->] <!--ko text: storeName--><!--/ko--><!-- ko if: notFireYn -->(<font color="red"><b>비화재보</b></font>)<!-- /ko --></h5>
			<span><!--ko text: ctnNo--><!--/ko--></span>
		</div>
	</li>
</script>
<script type="text/html" id="fire-detector-detail-template">
	<div class="placeinfo_wrap">
		<div class="placeinfo">
			<div data-bind="attr: { class: detectorNowStatus.detailCss }" style="margin:5px 0 0 5px; width:38px"></div>
			<div class="title" data-bind="text:detector.ctnNo"></div>
			<div class="markerbg">
				<div data-bind="attr: { class: detectorNowStatus.smokeEvent ? 'event_smoke' : 'active_smoke' }"></div>
				<div data-bind="attr: { class: detectorNowStatus.temperatureEvent ? 'event_temperature' : 'active_temperature' }"></div>
				<div data-bind="attr: { class: detectorNowStatus.flameEvent ? 'event_flame' : 'active_flame' }"></div>
				<div data-bind="attr: { class: detectorNowStatus.coEvent ? 'event_co' : 'active_co' }"></div>
			</div>
			<div class="close" onclick="rootVM.closeOverlay()" title="닫기"></div>
			<div class="row" style="padding-top:5px; padding-bottom:5px;">
				<h5 class="panel-title">■ 감지기 설치 점포</h5>
				<table class="table home-details-table">
					<col width="18%" />
					<col width="32%" />
					<col width="18%" />
					<col width="32%" />
					<tbody>
						<tr>
							<th class="al_right">점포명</th>
							<td class="white-space-word-wrap"><!--ko text: store.storeName--><!--/ko--></td>
							<th class="al_right">주소</th>
							<td class="white-space-word-wrap"><!--ko text: store.roadAddress--><!--/ko--></td>
						</tr>
						<tr>
							<th class="al_right">점주</th>
							<td class="white-space-word-wrap"><!--ko text: store.managerName--><!--/ko--></td>
							<th class="al_right">연락처(점포)</th>
							<td class="white-space-word-wrap"><!--ko text: store.telephoneNo--><!--/ko--></td>
						</tr>
						<tr>
							<th class="al_right">관할 소방서</th>
							<td class="white-space-word-wrap"><!--ko text: store.firestationName--><!--/ko--></td>
							<th class="al_right">연락처(점주)</th>
							<td class="white-space-word-wrap"><!--ko text: store.phoneNo--><!--/ko--></td>
						</tr>
						<tr>
							<th class="al_right">소방서 담당자</th>
							<td class="white-space-word-wrap"><!--ko text: store.firestationManagerName--><!--/ko--></td>
							<th class="al_right">제조번호</th>
							<td class="white-space-word-wrap"><!--ko text: detector.productNo--><!--/ko--></td>
						</tr>
						<tr>
							<th class="al_right">소방서 연락처</th>
							<td class="white-space-word-wrap"><!--ko text: store.firestationTelephoneNo--><!--/ko--></td>
							<th class="al_right">첨부사진</th>
							<td class="white-space-word-wrap" data-bind="foreach: detector.boardFiles">
								<!--[<a href="#" data-bind="click: rootVM.showAttatchFile">첨부파일</a>]&nbsp;&nbsp;-->
								<a href="#" data-bind="click: rootVM.showAttatchFile"><img src="/images/ico_disk.gif" width="19" height="19" /></a>&nbsp;&nbsp;
							</td>
						</tr>
						<tr>
							<th class="al_right">설치위치</th>
							<td class="white-space-word-wrap"><!--ko text: detector.installPlace--><!--/ko--></td>
							<th class="al_right">최종신호수신</th>
							<td class="white-space-word-wrap" data-bind='moment:detector.lastUpdtDt'></td>
						</tr>
					</tbody>
				</table>
			</div>
			<div class="row">
				<h5 class="panel-title">■ 최근 발생이벤트 정보</h5>
				<div id="eventListGrid"></div>
			</div>
		</div>
		<div class="after"></div>
	</div>
</script>
<link href="${contextPath}/css/home.css" rel="stylesheet" type="text/css">
<div class="map_wrap">
	<div id="map" class="map_area"></div>
	<div id="menu_wrap" class="map_menu_wrap" data-bind="visible: menuWrap" >
		<div class="row map_menu_title">
			화재발생감지기
		</div>
		<div class="row map_menu_stat">
			<div class="col-xs-3 map_menu_header">전체</div>
			<div class="col-xs-3 map_menu_body"><!--ko text: totalDetectorCount--><!--/ko-->개</div>
			<div class="col-xs-3 map_menu_header">화재</div>
			<div class="col-xs-3 map_menu_body"><!--ko text: fireDetectorCount--><!--/ko-->개</div>
		</div>
		<div class="row map_menu_event_type">
			<div class="col-md-3"><input type="checkbox" data-bind="checked: showNormal" /> 정상</div>
			<div class="col-md-3"><input type="checkbox" data-bind="checked: showFire" /> 화재</div>
			<div class="col-md-3"><input type="checkbox" data-bind="checked: showFakeFire" /> 비화재보</div>
			<div class="col-md-3"><input type="checkbox" data-bind="checked: showNoSignal" /> 신호없음</div>
			<div class="col-md-3"><input type="checkbox" data-bind="checked: showLowBattery" /> 배터리</div>
		</div>
		<div class="row map_menu_market">
			<span class="market-selector-title">시장선택</span>
			<select class="form-control input-xs" data-bind="enable: searchMarketEnable, options: mapMarketList, optionsText:'marketName', optionsValue:'marketSeq', value: searchMarketSeq, optionsCaption: '전체'"></select>
		</div>
		<div class="row map_store_list_wrap">
			<ul id="placesList" data-bind="template: { name: 'fire-detector-template', foreach: boardFireDetectorListWithEvent }"></ul>
		</div>
   </div>
</div>

<audio id="fireAlarmAudio"><source src="/audio/siren.mp3" type="audio/mp3"></audio>
<!-- ko if: selectedDetectorDetail -->
<div id="customOverLayInDaumMap" data-bind="template: { name: 'fire-detector-detail-template'}"></div>
<!-- /ko -->
<!-- Layer popup -->
<div id="fireAlertPopup" class="panel panel-body" data-bind="with: fireAlertPopupVM" style="display:none;">
	<form id="fireAlertPopupForm" method="POST">
		<img src="/images/map_maker/siren.gif" alt="">
		<div class="text-center" data-html="true" data-bind="html: fireAlarmLoc" style="font-size:18px;color:red;margin-top:20px;"></div>
		<!-- ko if: visibleMove() -->
		<div class="form-group">
			<button type="button" class="btn btn-warning btn-block" data-bind="click: doMove" style="font-size:20px;margin-top:10px;">이동하기<i class="icon-location4 position-right"></i></button>
		</div>
		<!-- /ko -->
	</form>
</div>

<script type="text/javascript">
    setPageLocation("", "홈");
    var pageParam = {
    	NAS_URL: '${ NAS_URL }',
   		MNG_AREA_SEQ: ${ me.mngAreaSeq },
   		MAP_INIT_CONF: ${ MAP_INIT_CONF },
   		MAP_MARKET_LIST: ${ MAP_MARKET_LIST },
   		MAP_FIRE_DETECTOR_LIST: ${ MAP_FIRE_DETECTOR_LIST },
   		MAP_INIT_MARKET_SEQ: ${me.marketSeq == null? "null" : me.marketSeq}
	};

</script>
<!-- 카카오맵 지도 SDK -->
<script type="text/javascript" src="//dapi.kakao.com/v2/maps/sdk.js?appkey=7b2ae4fd6565f66569df474406353e0f&libraries=services"></script>
<!-- //카카오맵 지도 SDK -->
<script type="text/javascript" src="${contextPath}/app/home/home.js"></script>