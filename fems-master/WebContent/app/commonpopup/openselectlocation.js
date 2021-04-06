var rootVM = null;

function RootVM() {

	var self = this;
	self.clazz = "RootVM";

	self.mainWindowPopupEvent = ( parent.rootVM != null && parent.rootVM['commonPopupEvent'] != null ) ? parent.rootVM['commonPopupEvent'] : opener.rootVM['commonPopupEvent'];

	var Addr = parent.rootVM.detailsVM.data().roadAddress();

	var mapContainer = document.getElementById('map'), // 지도를 표시할 div
    mapOption = {
        center: new daum.maps.LatLng(37.537187, 127.005476), // 지도의 중심좌표
        level: 2 // 지도의 확대 레벨
    };

	//지도를 미리 생성
	var map = new daum.maps.Map(mapContainer, mapOption);
	//주소-좌표 변환 객체를 생성
	var geocoder = new daum.maps.services.Geocoder();
	//마커를 미리 생성
	var marker = new daum.maps.Marker({
	    position: new daum.maps.LatLng(37.537187, 127.005476),
	    map: map
	});

	self.latlng = null;

	 // 도로명 주소로 상세 정보를 검색
    geocoder.addressSearch(Addr, function(results, status) {
        // 정상적으로 검색이 완료됐으면
        if (status === daum.maps.services.Status.OK) {
            var result = results[0]; //첫번째 결과의 값을 활용
            // 해당 주소에 대한 좌표를 받아서
            var coords = new daum.maps.LatLng(result.y, result.x);
            // 지도를 보여준다.
            mapContainer.style.display = "block";
            map.relayout();
            // 지도 중심을 변경한다.
            map.setCenter(coords);
            // 마커를 결과값으로 받은 위치로 옮긴다.
            marker.setPosition(coords);
            rootVM.latlng = coords;
            // 지도에 클릭 이벤트를 등록합니다
			// 지도를 클릭하면 마지막 파라미터로 넘어온 함수를 호출합니다
			kakao.maps.event.addListener(map, 'click', function(mouseEvent) {
			    // 클릭한 위도, 경도 정보를 가져옵니다
				rootVM.latlng = mouseEvent.latLng;
			    // 마커 위치를 클릭한 위치로 옮깁니다
			    marker.setPosition(rootVM.latlng);
			});
        } else {
        	self.reAddressSearch();
        }
    });

    // 도로명 주소로 상세 정보를 검색 실패시 법정동 주소로 재 검색
    self.reAddressSearch = function() {
    	Addr = parent.rootVM.detailsVM.data().legaldongadrDc();
    	 // 주소로 상세 정보를 검색
        geocoder.addressSearch(Addr, function(results, status) {
            // 정상적으로 검색이 완료됐으면
            if (status === daum.maps.services.Status.OK) {
                var result = results[0]; //첫번째 결과의 값을 활용
                // 해당 주소에 대한 좌표를 받아서
                var coords = new daum.maps.LatLng(result.y, result.x);
                // 지도를 보여준다.
                mapContainer.style.display = "block";
                map.relayout();
                // 지도 중심을 변경한다.
                map.setCenter(coords);
                // 마커를 결과값으로 받은 위치로 옮긴다.
                marker.setPosition(coords);
                rootVM.latlng = coords;
                // 지도에 클릭 이벤트를 등록합니다
    			// 지도를 클릭하면 마지막 파라미터로 넘어온 함수를 호출합니다
    			kakao.maps.event.addListener(map, 'click', function(mouseEvent) {
    			    // 클릭한 위도, 경도 정보를 가져옵니다
    				rootVM.latlng = mouseEvent.latLng;
    			    // 마커 위치를 클릭한 위치로 옮깁니다
    			    marker.setPosition(rootVM.latlng);
    			});
            } else {
            	alert('현재 검색된 주소에 해당하는 위도,경도 좌표는 kakaoMap에서 제공하고 있지 않습니다.');
            }
        });

    }

	self.onSelectClick = function() {
		var successCallback = self.mainWindowPopupEvent.selectLocationEvent.successCallback;
		var selectData = {
				/*la: rootVM.latlng.Ha.toFixed(6),
				lo: rootVM.latlng.Ga.toFixed(6)*/
				la: rootVM.latlng.Ma.toFixed(6),
				lo: rootVM.latlng.La.toFixed(6)
		};
		successCallback(selectData);
	};

	self.init = function() {
		console.log(self.clazz + " Initialized.");
	};
}

$(document).ready(function() {
	ezUtil.ezInitialize(false/*blockUsingPageAjaxIndicator*/);

	rootVM = new RootVM();
	rootVM.init();

	ko.applyBindings(rootVM);
});
