var rootVM = null;

function RootVM() {

	var self = this;
	self.clazz = "RootVM";

	//Extend VM
	vmExtender.extendGridVM(self);

	//Details REST API URL
	self.listURL = contextPath + "/store/storemng/api/page.json";
	//self.changeAreaURL = contextPath + "/commonpopup/api/setmngarea";

	self.mainWindowPopupEvent = ( parent.rootVM != null && parent.rootVM['commonPopupEvent'] != null ) ? parent.rootVM['commonPopupEvent'] : opener.rootVM['commonPopupEvent'];

	// 사용자에 따른 MARKET_CODE_MAP 변환
	var sortMarketCodeMap =  ezUtil.orderBySelectOptionsText(pageParam.MARKET_CODE_MAP);
	self.marketSelectOptions = _.isEmpty(pageParam.ME_MARKET_SEQ) ? ezUtil.addAllOptions(sortMarketCodeMap) : sortMarketCodeMap.filter(function(row){return row.value == pageParam.ME_MARKET_SEQ })

	//Search VM
	self.searchData = ko.observable();

	//Search Condition Applied
	self.searchCondition = {};

	//Apply Search Condition
	self.applySearchCondition = function(param) {
		return _.extend(param, self.searchCondition);
	};

	//Search
	self.search = function() {
		self.searchCondition = _.chain(ko.toJS(self.searchData()))
			.omit(_.isEmpty)
			.mapObject(function(val, key) { return _.isString(val)? encodeURIComponent(val) : val; })
			.value();
		self.grid.load();
	};

	// 선택시
	self.onSelectClick = function() {

		var successCallback = self.mainWindowPopupEvent.selectStoreEvent.successCallback;

		// 부모창 전달할 데이터
		var selectData = {
				mngAreaName :self.selectedRow['mngAreaName'], // 관제지역명
				marketName :  self.selectedRow['marketName'], // 관리지역명
				storeName : self.selectedRow['storeName'], // 상점명
				storeSeq : self.selectedRow['storeSeq'], // 상점Seq
				zipCode :	self.selectedRow['zipCode'], // 우편번호
				longitude: self.selectedRow['longitude'], // 위도
				latitude: self.selectedRow['latitude'], // 경도
				parcelAddress : self.selectedRow['parcelAddress'], //주소
				roadAddress : self.selectedRow['roadAddress']//주소
		};

		successCallback(selectData);
	};

	self.selectedRow = {};

	//Grid Event Listeners
	self.onGridEvent = function(type, data) {
		switch(type) {
			case "rowSelected" :
				self.selectedRow = data;
				break;
		}
	};

	self.gridColumns = [
		{ field: "rn", title: "No", width: 40, ezAlign: 'center'},
		{ field: "mngAreaName", title: "관제지역명", width: 150, ezAlign: 'center'},
		{ field: "marketName", title: "관리지역명", width: 150, ezAlign: 'center'},
		{ field: "storeName", title: "점포명", width: 200, ezAlign: 'left'},
		{ field: "managerName", title: "담당자", width: 150, ezAlign: 'center'},
		{ field: "phoneNo", title: "휴대폰번호", width: 150, ezAlign: 'center'},
		{ field: "telephoneNo", title: "일반전화번호", width: 150, ezAlign: 'center'},
		{ field: "regDate", title: "등록일", width: 90, ezAlign: 'center', template: self.gridColumnTemplate.momentISOFormatter('regDate') }
	];

	// Default grid height:670
	self.customGridOption = {height: 340};

	//Grid VM
	self.grid = new AbleKendoGridVM("#grid", self.listURL, self.gridColumns, self.onGridEvent, self.applySearchCondition, self.customGridOption);

	self.init = function() {
		console.log(self.clazz + " Initialized.");

		if (_.isEmpty(pageParam.MNG_AREA_SEQ)) {
			alert("관제 지역이 설정되지 않았습니다. 관제지역을 설정한 이후 다시 이용 바랍니다.");
			return;
		}

		self.searchData({
			searchMarketSeq: pageParam.ME_MARKET_SEQ, // 해당 시장관리자의 시장
			searchStoreName: ""
		});

		self.search();
	};

}

$(document).ready(function() {
	ezUtil.ezInitialize(false/*blockUsingPageAjaxIndicator*/);

	rootVM = new RootVM();
	rootVM.init();

	ko.applyBindings(rootVM);
});
