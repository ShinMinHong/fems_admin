var rootVM = null;

// 119 다매체 발신이력 조회
function Fire119SendLogVM() {

	var self = this;
	self.clazz = "Fire119SendLogVM";

	//Extend VM
	vmExtender.extendGridVM(self);

	//Base URL
	self.baseURL = contextPath + "/areasystem/fire119sendlog"
	//List API URL
	self.listURL = self.baseURL +"/api/page.json";

	//Search VM
	self.searchData ={
		searchReceiveCallNm: null,
		searchReceiveAplcntNm: null,
		searchStartDate : ko.observable(moment(serverTime).add(-1, 'week').format("YYYY-MM-DD")),
		searchEndDate : ko.observable(moment(serverTime).format("YYYY-MM-DD"))
	};

	//Search Condition Applied
	self.searchCondition = {};

	//Apply Search Condition
	self.applySearchCondition = function(param) {
		return _.extend(param, self.searchCondition);
	}

	//Search
	self.search = function() {
	var dataCloned = ko.toJS(self.searchData);
		self.searchCondition = _.chain(dataCloned)
			.omit(_.isEmpty)
			.mapObject(function(val, key) { return _.isString(val)? encodeURIComponent(val) : val; })
			.value();
		self.grid.load();
	};

	self.gridColumns = [
		{ field: "rn", title: "No", width: 40, ezAlign: 'center'},
		{ field: "mngAreaName", title: "관리지역명", width: 120, ezAlign: 'left'},
		{ field: "marketName", title: "시장명", width: 200, ezAlign: 'left'},
		{ field: "storeName", title: "점포명", width: 200, ezAlign: 'left'},
		{ field: "callName", title: "신고자명", width: 100, ezAlign: 'left'},
		{ field: "callTel", title: "신고자전화번호", width: 150, ezAlign: 'center'},
		{ field: "aplcntNm", title: "대표자명", width: 100, ezAlign: 'left'},
		{ field: "aplcntTelno", title: "대표자전화번호", width: 150, ezAlign: 'center'},
		{ field: "sttemntCn", title: "신고내용", width: 500, ezAlign: 'left'},
		{ field: "ctrdCode", title: "시도코드", width: 100, ezAlign: 'center'},
		{ field: "signguCode", title: "시군구코드", width: 150, ezAlign: 'center'},
		{ field: "dongCode", title: "동코드", width: 100, ezAlign: 'center'},
		{ field: "liCode", title: "리코드", width: 100, ezAlign: 'center'},
		{ field: "msfrtnKnCode", title: "재난종별코드", width: 150, ezAlign: 'center'},
		{ field: "sendResult", title: "전송결과", width: 100, ezAlign: 'center'},
		{ field: "sendDate", title: "전송일", width: 150, ezAlign: 'center', template: self.gridColumnTemplate.momentFormatter('sendDate')},
	];

	self.grid = new AbleKendoGridVM("#grid", self.listURL, self.gridColumns, null, self.applySearchCondition);

	self.init = function() {
		self.search();
		console.log(self.clazz + " Initialized.");
	};
}

$(document).ready(function() {
	//공통 초기화
	ezUtil.ezInitialize(false/*blockUsingPageAjaxIndicator*/);

	rootVM = new Fire119SendLogVM();
	rootVM.init();

	ko.applyBindings(rootVM);
});
