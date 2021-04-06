var rootVM = null;

// 화재감지기 관리 (검색 , 리스트)
function LowBatteryDetectorVM() {

	var self = this;
	self.clazz = "LowBatteryDetectorVM";

	//Extend VM
	vmExtender.extendGridVM(self);

	//List API URL
	self.listURL = contextPath +"/firedetector/lowbatterydetector/api/page.json";

	//시장관리자면 본인 시장 셋팅
	self.getSearchMarketSeq = _.isEmpty(pageParam.ME_MARKET_SEQ) ? "" : pageParam.ME_MARKET_SEQ;

	//관리지역 Code Map
	self.marketSelectOptions =  ezUtil.orderBySelectOptionsText(pageParam.MARKET_CODE_MAP);
	self.marketCodeMap = ezUtil.convertSelectOptionsToObject(pageParam.MARKET_CODE_MAP);
	self.fireDetectorStatus = ezUtil.convertObjectToSelectOptions(pageParam.FIRE_DETECTOR_STATUS);

	//Search VM
	self.searchData = ko.observable({
		searchMarketSeq : self.getSearchMarketSeq,
		searchStoreName:"",
		searchCtnNo:"",
		searchModelNo:"",
		searchProductNo:"",
		searchFireDetectorStatus:"" // 감지기 상태
	});

	//Search Condition Applied
	self.searchCondition = {};

	//Apply Search Condition
	self.applySearchCondition = function(param) {
		return _.extend(param, self.searchCondition);
	}

	//Search
	self.search = function() {
		self.searchCondition = _.chain(ko.toJS(self.searchData()))
			.omit(_.isEmpty)
			.mapObject(function(val, key) { return _.isString(val)? encodeURIComponent(val) : val; })
			.value();
		self.grid.load();
	};


	//Data Table Columns Settings
	self.gridColumns = [
		{ field: "rn", title: "No", width: 40, ezAlign: 'center'},
		{ field: "mngAreaName", title: "관제지역명", width: 100, ezAlign: 'center'},
		{ field: "marketName", title: "관리지역명", width: 100, ezAlign: 'center'},
		{ field: "storeName", title: "점포명", width: 200, ezAlign: 'left'},
		{ field: "modelNo", title: "모델번호", width: 100, ezAlign: 'center'},
		{ field: "productNo", title: "제조번호", width: 110, ezAlign: 'center'},
		{ field: "ctnNo", title: "CTN 번호", width: 110, ezAlign: 'center'},
		{ field: "remaindBattery", title: "3V 배터리(%)", width: 100, ezAlign: 'center'},
		{ field: "remaindBattery2", title: "3.6V 배터리(%)", width: 110, ezAlign: 'center'},
		{ field: "fireDetectorStatus", title: "상태", width: 70, ezAlign: 'center', template: self.gridColumnTemplate.getTextFromCodeMap('fireDetectorStatus',self.fireDetectorStatus)},
		{ field: "lastUpdtDt", title: "최종수집일", width: 150, ezAlign: 'center',template: self.gridColumnTemplate.momentFormatter('lastUpdtDt') }
	];

	self.grid = new AbleKendoGridVM("#grid", self.listURL, self.gridColumns, null, self.applySearchCondition);

	//Excel Download
	self.isMergeExcelCell = ko.observable(false);
	self.excelDownload = function(){
		if(rootVM.grid.dataSource._pristineData.length==0){
			alert("검색된 데이터가 없습니다.");
			return;
		}
		var url = contextPath + "/firedetector/lowbatterydetector/excel?";
		var data  = _.extend(self.searchCondition, {mergeExcelCell: self.isMergeExcelCell()});
		var params = $.param(data);
		document.location.href = url + params;
	}

	self.init = function() {
		self.search();
		console.log(self.clazz + " Initialized.");

	};
}

$(document).ready(function() {
	//공통 초기화
	ezUtil.ezInitialize(false/*blockUsingPageAjaxIndicator*/);
	rootVM = new LowBatteryDetectorVM();
	rootVM.init();
	ko.applyBindings(rootVM);
});
