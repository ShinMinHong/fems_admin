var rootVM = null;

var NORMAL_MARKER_IMAGE = new kakao.maps.MarkerImage("/images/map_maker/normal_marker.png", new kakao.maps.Size(20, 28));
var FIRE_MARKER_IMAGE = new kakao.maps.MarkerImage("/images/map_maker/fire_marker.png", new kakao.maps.Size(32, 46));
var NO_SIGNAL_MARKER_IMAGE = new kakao.maps.MarkerImage("/images/map_maker/no_signal_marker.png", new kakao.maps.Size(26, 37));
var LOW_BATTERY_MARKER_IMAGE = new kakao.maps.MarkerImage("/images/map_maker/low_battery_marker.png", new kakao.maps.Size(26, 37));

var EVENT_STATUS_NORMAL = 0;
var EVENT_STATUS_FIRE = 1;
var EVENT_STATUS_FAKE_FIRE = 2;
var EVENT_STATUS_NO_SIGNAL = 3;
var EVENT_STATUS_LOW_BATTERY = 4;

var REFRESH_INTERVAL_SEC = 5;

//상태에 따라 Int의 Status를 가져오는 매핑 함수. 속도를 고려하여 int형 사용
function getEventStatusByErrorStatus(errorStatus){
	if("fire" === errorStatus){
		return EVENT_STATUS_FIRE;
	} else if("fakeFire" === errorStatus){
		return EVENT_STATUS_FAKE_FIRE;
	} else if("noSignal" === errorStatus){
		return EVENT_STATUS_NO_SIGNAL;
	} else if("lowBattery" === errorStatus){
		return EVENT_STATUS_LOW_BATTERY;
	} else {
		return EVENT_STATUS_NORMAL;
	}
}

//Int인 EVENT_STATUS에 따라 마커 이미지를 가져오는 매핑 함수
function getMarkerImageByEventStatus(eventStatus){
	if(EVENT_STATUS_FIRE === eventStatus || EVENT_STATUS_FAKE_FIRE === eventStatus){
		return FIRE_MARKER_IMAGE;
	} else if(EVENT_STATUS_NO_SIGNAL === eventStatus){
		return NO_SIGNAL_MARKER_IMAGE;
	} else if(EVENT_STATUS_LOW_BATTERY === eventStatus){
		return LOW_BATTERY_MARKER_IMAGE;
	} else {
		return NORMAL_MARKER_IMAGE;
	}
}

//상태에 따라 마커 이미지를 가져오는 매핑 함수
function getMarkerImageByErrorStatus(errorStatus){
	var eventStatus = getEventStatusByErrorStatus(errorStatus);
	return getMarkerImageByEventStatus(eventStatus);
}

//Int인 EVENT_STATUS에 따라 상세 팝업 Css Class명을 생성하는 함수
function getDetailCssClassByErrorStatus(errorStatus){
	var eventStatus = getEventStatusByErrorStatus(errorStatus);
	if(EVENT_STATUS_FIRE === eventStatus || EVENT_STATUS_FAKE_FIRE === eventStatus){
		return "marker_fire";
	} else if(EVENT_STATUS_NO_SIGNAL === eventStatus){
		return "marker_no_signal";
	} else if(EVENT_STATUS_LOW_BATTERY === eventStatus){
		return "marker_low_battery";
	} else {
		return "marker_normal";
	}
}

//Int인 EVENT_STATUS에 따라 상세 팝업 Css Class명을 생성하는 함수
function getListRowCssClassByErrorStatus(errorStatus){
	var eventStatus = getEventStatusByErrorStatus(errorStatus);
	if(EVENT_STATUS_FIRE === eventStatus || EVENT_STATUS_FAKE_FIRE === eventStatus){
		return "markerbg marker_fire";
	} else if(EVENT_STATUS_NO_SIGNAL === eventStatus){
		return "markerbg marker_no_signal";
	} else if(EVENT_STATUS_LOW_BATTERY === eventStatus){
		return "markerbg marker_low_battery";
	} else {
		return "markerbg";
	}
}

//관제지도 > 현황판 > 이벤트 Grid Template 함수
function gridOnOffFormat(field){
	var closure = {
		field: field
	};
	closure.field; //dummy for eclipse stupid validation
	var template = function(dataItem) {
		var value = dataItem[closure.field];
		if(value == null) {
			return "";
		}
		return dataItem[closure.field] ? "<font color='red'>On</font>" : "Off";
	};
	return template;
}

function gridPercentFormat(field){
	var closure = {
		field: field
	};
	closure.field; //dummy for eclipse stupid validation
	var template = function(dataItem) {
		var value = dataItem[closure.field];
		if(value == null) {
			return "";
		}
		return dataItem[closure.field]+"%";
	};
	return template;
}

function gridNotFireFormat(dataItem){
	return !s.isBlank(dataItem.notFireYn) && dataItem.notFireYn ? "[비화재보]" : "";
}

function gridDemonRegDateFormat(dataItem){
	return !s.isBlank(dataItem.demonRegDate) ? moment(dataItem.demonRegDate).format('YYYY-MM-DD HH:mm:ss') : "";
}

function TotControlMapModel() {
	var self = this;
	self.clazz = "TotControlMapModel";

	//Extend VM
	vmExtender.extendGridVM(self);

	self.homeMenu = ko.observable(true);
	self.menuWrap = ko.observable(ezUtil.detectMobile() ? false:true);

	self.showNormal = ko.observable(false);
	self.showFire = ko.observable(true);
	self.showFakeFire = ko.observable(true);
	self.showLowBattery = ko.observable(true);
	self.showNoSignal = ko.observable(false);

	self.mapInitConf = pageParam.MAP_INIT_CONF;
	var mapContainer = document.getElementById('map')
	var mapOptions = {
		center: new kakao.maps.LatLng(self.mapInitConf.latitude, self.mapInitConf.longitude),
		level: self.mapInitConf.scale,
		minLevel: 2,
		maxLevel: 14,
		disableDoubleClickZoom: true
	};
	self.kakaoMap = new kakao.maps.Map(mapContainer, mapOptions);

	self.placeOverlay = new kakao.maps.CustomOverlay({zIndex:5});
	self.storeDetailOverlay = document.getElementById("customOverLayInDaumMap")
	self.placeOverlay.setContent(self.storeDetailOverlay);

	// 엘리먼트에 이벤트 핸들러를 등록하는 함수
	self.addEventHandle = function(target, type, callback) {
	    if (target.addEventListener) {
	        target.addEventListener(type, callback);
	    } else {
	        target.attachEvent('on' + type, callback);
	    }
	};

	// 지도 객체에 이벤트가 전달되지 않도록 이벤트 핸들러로 kakao.maps.event.preventMap 메소드를 등록
	self.addEventHandle(self.storeDetailOverlay, 'mousedown', kakao.maps.event.preventMap);
	self.addEventHandle(self.storeDetailOverlay, 'touchstart', kakao.maps.event.preventMap);


	self.mapMarketList = pageParam.MAP_MARKET_LIST;
	self.searchMarketEnable = ko.observable(true);
	self.searchMarketSeq = ko.observable(null);
	self.centerMarketSeq = null;

	self.mapFireDetectorList = pageParam.MAP_FIRE_DETECTOR_LIST;

	//보드 화면 감지기 목록에 뿌려지는 전체 데이터. 시장선택 combobox와 반응
	self.fireDetectorListWithEvent = ko.observableArray(_.filter(self.mapFireDetectorList, function(detector){ return detector.errorStatus != "normal"; }));

	//화면에 연결되는 값
	self.totalDetectorCount = ko.observable(self.mapFireDetectorList.length);
	self.fireDetectorCount = ko.observable(_.filter(self.mapFireDetectorList, function(detector){ return detector.alarmFire == true;}).length); //화재(비화재보 포함)

    self.boardFireDetectorListWithEvent = ko.computed(function() {
    	//현황판>시장선택 변경 시, searchMarketSeq의 subscribe 함수를 별도로 짜도 되나 용도가 동일한 computed 함수에 통합
    	if(self.centerMarketSeq != self.searchMarketSeq()){
    		//상세 Overlay가 띄어져 있다면 Close
    		if(typeof self.selectedDetectorDetail == 'function' && !_.isEmpty(self.selectedDetectorDetail())){
    			self.closeOverlay(false);
    		}

    		self.centerMarketSeq = self.searchMarketSeq()
    		var market = _.find(self.mapMarketList, function(row) {return row.marketSeq == self.centerMarketSeq});
    		var latlng, scale;
    		if(market != null){
    			latlng =  new kakao.maps.LatLng(market.latitude, market.longitude)
    			scale = market.scale
    		} else {
    			latlng =  new kakao.maps.LatLng(self.mapInitConf.latitude, self.mapInitConf.longitude)
    			scale = self.mapInitConf.scale
    		}
			self.kakaoMap.setCenter(latlng);
			self.kakaoMap.setLevel(scale);
    	}

    	//이벤트 목록중에서, 이벤트 체크박스 설정 및 시장 콤보박스 설정에 반응하여 노출여부 결정.
    	return _.chain(self.fireDetectorListWithEvent())
    		.filter(function(detector){
	    		var isInclude = false;
	    		if((self.showFire() && "fire" === detector.errorStatus)
					|| (self.showFakeFire() && "fakeFire" === detector.errorStatus)
					|| (self.showNoSignal() && "noSignal" === detector.errorStatus)
					|| (self.showLowBattery() && "lowBattery" === detector.errorStatus) ) {
	    			isInclude = true;
	    		}

	    		if(isInclude){
	    			isInclude = _.string.isBlank(self.searchMarketSeq()) ? true : detector.marketSeq == self.searchMarketSeq();
	    		}

				return isInclude;
	    	}).sortBy(function(detector){ return detector.errorStatus; }).value();
    });

    //현황판 > 감지기 상태 체크 박스 변경 시 이벤트 함수
    self.showNormal.subscribe(function(){
    	self.redrawAllMarkers();
	});
    self.showFire.subscribe(function(){
    	self.redrawAllMarkers();
	});
    self.showFakeFire.subscribe(function(){
    	self.redrawAllMarkers();
	});
    self.showNoSignal.subscribe(function(){
    	self.redrawAllMarkers();
	});
    self.showLowBattery.subscribe(function(){
    	self.redrawAllMarkers();
	});

	self.startInterval;

	self.init = function() {
		self.resizeMapWrapper();
		if(self.kakaoMap) {
			self.kakaoMap.relayout();
			var latlng =  new kakao.maps.LatLng(self.mapInitConf.latitude, self.mapInitConf.longitude)
			self.kakaoMap.setCenter(latlng);
			self.kakaoMap.setLevel(self.mapInitConf.scale);
		}

		self.initMarkers();
		self.startInterval = setInterval(function() {
			self.getCurrentStatusList();
		}, REFRESH_INTERVAL_SEC * 1000);

		//Ajax오류 시, timer를 멈추기 위해 이벤트 연결
		$(document).ajaxError(self.onAjaxErrorPageHandler);
	};

	self.stopInterval = function(){
		clearInterval(self.startInterval);
	}

	self.ajaxErrorCnt = 0;
	self.onAjaxErrorPageHandler = function(event, jqXHR, ajaxSettings, thrownError){
		self.checkAjaxError();
	}

	self.checkAjaxError = function(){
		//Ajax 오류가 3회 이상 발생하면, 자동 갱신 기능 종료
		if(self.ajaxErrorCnt++ >= 3){
			self.stopInterval();
			alert("서버 데이터를 갱신 하는 중 오류가 발생하여, 자동갱신 기능을 종료합니다.\r\n화면을 새로고침 후 다시 이용해 주세요.");
		}
	}

	self.getCurrentStatusList = function(){
		$.ajax({
			url: $.format("{0}.json", contextPath + "/integration/totcontrolmap/api/getstatuslistinevent"),
			type: "GET",
			contentType: "application/json",
			success: function(data) {
				if(ezUtil.checkRESTResponseMsg(data)) {
					var currentFireStatusList = data.body;
					var newFireCount = 0;
					var firstNewFireDetector = null;
					var isFireDetectorListChanged = false;
					for(var idx=0; idx<self.mapFireDetectorList.length; idx++) {
						var detector = self.mapFireDetectorList[idx];
						var currentInfo = _.find(currentFireStatusList, function(tmpDetector){return detector.fireDetectorSeq==tmpDetector.fireDetectorSeq;});

						var currentErrorStatus = currentInfo == null ? "normal" : currentInfo.errorStatus;

						//이전 상태와 동일하면 다음 loop 진행
						if(detector.errorStatus === currentErrorStatus)
							continue;

						//이벤트변화 Flag설정
						isFireDetectorListChanged = true;

						//이전에는 정상이었다가, 화재로 변경된 경우는 isNewFire에 마킹처리
						if("fire" === currentErrorStatus){
							newFireCount++;
							if(firstNewFireDetector == null){
								firstNewFireDetector = detector;
							}
						}

						//변경된 상태 반영
						detector.alarmFire = currentInfo == null ? false : currentInfo.alarmFire;
						detector.notFireYn = currentInfo == null ? false : currentInfo.notFireYn;
						detector.noSignal = currentInfo == null ? false : currentInfo.noSignal;
						detector.lowBattery = currentInfo == null ? false : currentInfo.lowBattery;
						detector.errorStatus = currentInfo == null ? false : currentErrorStatus;

						detector.eventStatus = getEventStatusByErrorStatus(currentErrorStatus);

						//지도 반영
						self.redrawMarker(detector);
					}
					//전체 단말기에서 변경된 부분이 없다면 종료
					if( !isFireDetectorListChanged ) return;

					//보드 화면 감지기목록 및 화재발생 숫자 갱신
					self.fireDetectorCount(_.filter(currentFireStatusList, function(detector){ return detector.alarmFire == true;}).length);
					self.fireDetectorListWithEvent(null);
					self.fireDetectorListWithEvent(_.filter(self.mapFireDetectorList, function(detector){ return detector.errorStatus != "normal"; }));

					//화재 경보 알림
					if(newFireCount > 0){
						self.showFireAlarm(newFireCount, firstNewFireDetector);
					}

				} else if (!_.isEmpty(data.cbCmd) && data.cbCmd == "LOGINREQUIRED"){
					//Ajax시 인증 오류가 발생하면, 자동 갱신 기능 종료
					self.stopInterval();
					alert("인증 오류가 발생하여, 자동갱신 기능을 종료합니다.\r\n화면을 새로고침 후 다시 이용해 주세요.");
				} else if("ERROR" === data.status){
					self.checkAjaxError();
				}
			}
		});
	};

	//마커 Click 이벤트 핸들러
	self.markerClickEventHandler = function() {
		self.selectDetector(this.fireDetectorSeq);
	}

	//마커를 생성하고, 지도에 표시 및 배열에 추가함.
	//마커 생성은 최초 Page Init시에만 수행됨.
	self.initMarkers = function() {
		var newFireCount = 0;
		var firstNewFireDetector = null;

		for (var idx = 0; idx < self.mapFireDetectorList.length; idx++) {
			var fireDetector = self.mapFireDetectorList[idx];
			var tmpMarker = new kakao.maps.Marker({
				position: new kakao.maps.LatLng(fireDetector.latitude, fireDetector.longitude),
				image: getMarkerImageByErrorStatus(fireDetector.errorStatus),
				title: fireDetector.ctnNo+'-'+fireDetector.storeName
			});

			//필요한 추가 정보 설정
			fireDetector.eventStatus = getEventStatusByErrorStatus(fireDetector.errorStatus);

			if(fireDetector.alarmFire && !fireDetector.notFireYn){
				newFireCount++;
				if(firstNewFireDetector == null){
					firstNewFireDetector = fireDetector;
				}
			}

			//최초 설정에 따라 지도에 표시
			if(self.showNormal() && (EVENT_STATUS_NORMAL === fireDetector.eventStatus)){
				tmpMarker.setMap(self.kakaoMap);
			} else if(self.showFire() && (EVENT_STATUS_FIRE === fireDetector.eventStatus)){
				tmpMarker.setMap(self.kakaoMap);
			} else if(self.showFakeFire() && (EVENT_STATUS_FAKE_FIRE === fireDetector.eventStatus)){
				tmpMarker.setMap(self.kakaoMap);
			} else if(self.showNoSignal() && (EVENT_STATUS_NO_SIGNAL === fireDetector.eventStatus)){
				tmpMarker.setMap(self.kakaoMap);
			} else if(self.showLowBattery() && (EVENT_STATUS_LOW_BATTERY === fireDetector.eventStatus)){
				tmpMarker.setMap(self.kakaoMap);
			} else {
				tmpMarker.setMap(null);
			}

			tmpMarker.fireDetectorSeq = fireDetector.fireDetectorSeq; //향후 marker에서 정보 추출시 필요한 Key값 저장

			kakao.maps.event.addListener(tmpMarker, 'click', self.markerClickEventHandler);

	        //접근을 위해, 데이터 개체에 마커를 하위로 추가
			fireDetector.marker = tmpMarker;
		}

		//화재 경보 알림
		if(newFireCount > 0){
			self.showFireAlarm(newFireCount, firstNewFireDetector);
		}
	};

	//마커 전체를 다시 그리는 로직
	self.redrawAllMarkers = function() {
		for (var idx = 0; idx < self.mapFireDetectorList.length; idx++) {
			self.redrawMarker(self.mapFireDetectorList[idx]);
		}
	}

	//특정 Index의 마커를 다시 그리는 로직
	self.redrawMarker = function(detector) {
		var marker = detector.marker;

		var showInMapBefore = (marker.getMap() != null);
		var showInMapAfter = false;
		var markerImageBefore = marker.getImage();
		var markerImageAfter = getMarkerImageByEventStatus(detector.eventStatus);

		//상태가 변경되었다면, 마커 이미지 변경
		if(markerImageBefore !== markerImageAfter) {
			marker.setImage(markerImageAfter);
		}

		//이벤트 상태 및 노출여부 체크박스에 따라  After 노출여부 결정.
		if( (detector.eventStatus === EVENT_STATUS_NORMAL && self.showNormal())
			|| (detector.eventStatus === EVENT_STATUS_FIRE && self.showFire())
			|| (detector.eventStatus === EVENT_STATUS_FAKE_FIRE && self.showFakeFire())
			|| (detector.eventStatus === EVENT_STATUS_NO_SIGNAL && self.showNoSignal())
			|| (detector.eventStatus === EVENT_STATUS_LOW_BATTERY && self.showLowBattery()) ){
			showInMapAfter = true;
		}

		//노출여부가 기존과 변경되었다면, 신규 상태 반영
		if(!showInMapBefore && showInMapAfter){
			marker.setMap(self.kakaoMap);
		} else if(showInMapBefore && !showInMapAfter){
			marker.setMap(null);
		}
	}

	// 상세정보 선택. 데이터 요청 후, 화면에 그리는 로직 수행
	self.selectDetector = function(fireDetectorSeq){
		$.ajax({
			url: $.format("{0}/{1}.json", contextPath + "/home/api/details", fireDetectorSeq),
			type: "GET",
			contentType: "application/json",
			success: function(data) {
				if(ezUtil.checkRESTResponseMsg(data)) {
					//조회한 내용을 화면에 반영
					if( data.body != null) {
						var detectorDetail = data.body;
						if(detectorDetail.detectorNowStatus != null){
							detectorDetail.detectorNowStatus.detailCss = getDetailCssClassByErrorStatus(detectorDetail.detectorNowStatus.errorStatus);
						}
						self.displayPlaceInfo(detectorDetail);
					}
				}
			}
		});
	};

	//현황판의 Grid에서 선택한 경우
	self.clickDetectorInGrid = function(detector) {
		if(detector == null) return;

		self.selectDetector(detector.fireDetectorSeq);
	}

	self.selectedDetectorDetail = ko.observable(null);
	self.closeOverlay = function(userClick){
		// 상세오버레이를 닫을 때 마커를 지도의 중심으로 이동
		if(!_.isEmpty(self.selectedDetectorDetail())) {
			if(userClick) {
				var tempLatlng = new kakao.maps.LatLng(self.selectedDetectorDetail().detector.latitude, self.selectedDetectorDetail().detector.longitude);
				self.kakaoMap.setCenter(tempLatlng);
			}
			self.selectedDetectorDetail(null);
		}
		self.placeOverlay.setMap(null);
		self.kakaoMap.setZoomable(true);
	}

	self.displayPlaceInfo = function(detectorDetail){
		self.selectedDetectorDetail(detectorDetail);

		self.kakaoMap.setLevel(2);
		self.kakaoMap.setZoomable(false);

		//이벤트가 발생한 마커의 중심좌표 - 인포윈도우 표시에 사용
		var latlng =  new kakao.maps.LatLng(detectorDetail.detector.latitude, detectorDetail.detector.longitude)

		//지도 중심을 모바일기기여부, 가로/세로모드, 윈포 윈도우 위치 등에 따라 보정
		var adjustMapCenter = null;
        if(ezUtil.detectMobile()){
        	var orientation = Math.abs(window.orientation);
        	if(orientation == 0 || orientation == 180) {
        		// 세로모드인 경우
        		adjustMapCenter =  new kakao.maps.LatLng(latlng.getLat() + 0.0012, latlng.getLng() - 0.00024);
        	} else {
        		// 가로모드인 경우
        		adjustMapCenter =  new kakao.maps.LatLng(latlng.getLat() + 0.0006, latlng.getLng())
        	}
        } else {
        	adjustMapCenter =  new kakao.maps.LatLng(latlng.getLat() + 0.0012, latlng.getLng() + 0.0001)
        }

		//표시해야 되는 마커 기준으로 중심 이동 및 오버레이 표시
		self.kakaoMap.setCenter(adjustMapCenter);
		self.placeOverlay.setPosition(latlng)
		self.placeOverlay.setMap(self.kakaoMap);

		ko.cleanNode(self.storeDetailOverlay);
		ko.applyBindings(detectorDetail, self.storeDetailOverlay);

		//화면이 작아, Overlay가 표시되지 않은 경우는 Marker를 Center로 다시 보정.
		if(document.getElementById("customOverLayInDaumMap") == null){
			self.kakaoMap.setCenter(latlng);
		}

		if(document.getElementById("eventListGrid") != null ){
			//최근 단말기 발생 이벤트 정보
			self.eventListGridColumns = [
				{ field: "signalTypeAsString", title: "신호타입", width: 90, ezAlign: 'center'},
				{ field: "smokeEvent", title: "화재<br/>(연기)", width: 50, ezAlign: 'center', template: gridOnOffFormat('smokeEvent')},
				{ field: "temperatureEvent", title: "화재<br/>(온도)", width: 50, ezAlign: 'center', template: gridOnOffFormat('temperatureEvent')},
				{ field: "flameEvent", title: "화재<br/>(불꽃)", width: 50, ezAlign: 'center', template: gridOnOffFormat('flameEvent')},
				{ field: "coEvent", title: "화재<br/>(CO)", width: 50, ezAlign: 'center', template: gridOnOffFormat('coEvent')},
				/*{ field: "batteryValue", title: "3V 배터리(%)", width: 80, ezAlign: 'center', template: gridPercentFormat('batteryValue')},
				{ field: "battery2Value", title: "3.6V 배터리(%)", width: 80, ezAlign: 'center', template: gridPercentFormat('battery2Value')},*/
				{ field: "notFireYn", title: "비화재보", width: 80, ezAlign: 'center', template: gridNotFireFormat},
				{ field: "demonRegDate", title: "발생일시", width: 120, ezAlign: 'center', template: gridDemonRegDateFormat}
			];
			self.gridOptions = {
					height: 150,
					pageable: false
			};
			self.eventListGrid = null;
			self.eventListGrid = new AbleKendoTableVM("#eventListGrid", self.eventListGridColumns, self.gridOptions);
			self.eventListGrid.setData(detectorDetail.eventList ? detectorDetail.eventList : []);
		}
		return;
	}

	//화면에 화재 알림 표시
	self.fireAlertPopupVM = new FireAlertPopupVM("#fireAlertPopup");
	self.showFireAlarm = function(newFireCount, firstNewFireDetector){
		self.fireAlertPopupVM.open(newFireCount, firstNewFireDetector);
	}

	self.resizeMapWrapper = function() {
		var targetHeight = $(window).height() - 120;
		$(".map_wrap").height(targetHeight);
	};

	/**
	 * window.onorientationchange, $(window).on('orientationchange') 이벤트가
	 * 모바일에서 제대로 동작하지 않아 resize이벤트로 처리
	 * 모바일에서는 resize가 허용되지 않으므로, resize가 발생하면 orientationchange 이벤트가 발생한 것으로 간주
	 */
	self.resizeByOrientationChange = function() {
		//화재경보팝업이 열려있으면 닫고 새로 열기
		if(self.fireAlertPopupVM && self.fireAlertPopupVM.isVisible) {
			_.defer(function() {
				if(self.fireAlertPopupVM.isVisible) {
					self.fireAlertPopupVM.close();
					self.fireAlertPopupVM.open();
				}
			});
		}
		//상세오버레이가 열려있다면 닫고 새로 열기
		if(typeof self.selectedDetectorDetail == 'function' && !_.isEmpty(self.selectedDetectorDetail())) {
			_.defer(function() {
				var place = self.selectedDetectorDetail();
				self.closeOverlay(true);
				self.displayPlaceInfo(place);
			});
		}
	};

	self.showAttatchFile = function(fileInfo) {
		window.open(pageParam.NAS_URL+fileInfo.attachedFilePath, "_blank", "height=300, width=300");
	};

}

function FireAlertPopupVM(elementId) {

	var self = this;
	self.clazz = "FireAlertPopupVM";

	self.popupId = elementId;

	self.formId = $("#fireAlertPopupForm");

	self.popupOptions = {
			width: 490,
			height: 530,
			resizable: false,
			buttons: [],
			close: function (){
				self.close();
			}
	};
	// 팝업이 열려있는지 여부
	self.isVisible = false;
	self.fireAlarmLoc = ko.observable("");
	self.visibleMove = ko.observable(true);

	//Extend PopupVM
	vmExtender.extendPopupVM(self);

	self.openDetector = null;

	var constraints = { audio: true, video: false };
	//Close Callback
	 self.close = function() {
		 rootVM.fireLoc = null;
		 self.popupElement.dialog('close');
		 self.isVisible = false;
		 self.openDetector = null;
		 document.getElementById("fireAlarmAudio").loop = false;
		 document.getElementById("fireAlarmAudio").muted = true;
	};

	self.open = function(newFireCount, firstNewFireDetector) {
		self.popupElement.dialog('open');
		self.isVisible = true;

		if(newFireCount == 1){
			self.visibleMove(true);
			self.openDetector = firstNewFireDetector;
			self.fireAlarmLoc(firstNewFireDetector.storeName+". "+firstNewFireDetector.ctnNo);
		} else {
			self.visibleMove(false);
			self.openDetector = null;
			self.fireAlarmLoc(newFireCount + "건의 화재가 발생.<br/>우측 현황판에서 확인 하시기 바랍니다.");
		}

		try{
			if( ezUtil.detectIEAndEdg() ){
				self.playSound();
			} else {
				//chrome
                if(navigator.mediaDevices !== 'undefined' || typeof navigator.mediaDevices.getUserMedia === 'function'){
                    navigator.mediaDevices.getUserMedia(constraints)
                    .then(function(stream) {
                    	self.playSound();
                    })
                    .catch(function(err) {
                        console.warn("화재 발생 알림 재생에 필요한 권한이 필요합니다.");
                    });
                } else {
                	self.playSound();
                }
			}
		} catch(e){
		}
	};

	self.playSound = function() {
		document.getElementById("fireAlarmAudio").play();
		document.getElementById("fireAlarmAudio").muted = false;
		document.getElementById("fireAlarmAudio").loop = true;
	};

	self.doMove = function() {
		rootVM.selectDetector(self.openDetector.fireDetectorSeq);
		self.close();
	};
}

$(document).ready(function() {
	ezUtil.ezInitialize(true/*blockUsingPageAjaxIndicator*/);

	rootVM = new TotControlMapModel();
	rootVM.init();

	ko.applyBindings(rootVM);

	$( "#menu_wrap" ).draggable()
	$( "#menu_wrap" ).css('z-index', 50);
});


/** window resize event. orientationchange 시에도 발생 */
$(window).resize(function() {
	if(null != rootVM) {
		rootVM.resizeMapWrapper();
		if(ezUtil.detectMobile()) {
			rootVM.resizeByOrientationChange();
		}
	}
});

