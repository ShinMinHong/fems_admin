var rootVM = null;

// 접속 정보 이력
function AdminLoginLogVM() {

	var self = this;
	self.clazz = "AdminLoginLogVM";

	//Extend VM
	vmExtender.extendGridVM(self);

	//Base URL
	self.baseURL = contextPath + "/areasystem/loginhistory"
	//List API URL
	self.listURL = self.baseURL +"/api/page.json";


	// 사용자 구분 SelectOptions
	self.appUserGradeSelectOptions =  ezUtil.orderByAuthorGroupSelectOptions(pageParam.APP_USER_GRADE_CODE_MAP);

	//Search VM (사용자 구분 , 아이디 , 성명 , 접속시자일, 접속 종료일)
	self.searchData ={
		searchRolegroupCode: "",
		searchAdminId: "",
		searchAdminName: "",
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
		self.searchCondition = _.chain(ko.toJS(self.searchData))
			.omit(_.isEmpty)
			.mapObject(function(val, key) { return _.isString(val)? encodeURIComponent(val) : val; })
			.value();
		self.grid.load();
	};

	//Data Table Columns Settings
	self.gridColumns = [
		{ field: "rn", title: "No", width: 40, ezAlign: 'center'},
		{ field: "rolegroupCode", title: "관리자 구분", width: 120, ezAlign: 'center',template:self.gridColumnTemplate.getTextFromCodeMap('rolegroupCode',pageParam.APP_USER_GRADE_CODE_MAP)},
		{ field: "mngAreaSeq", title: "관리지역명", width: 150, ezAlign: 'center',template:self.gridColumnTemplate.getTextFromCodeMap('mngAreaSeq',pageParam.MNG_AREA_CODE_MAP)},
		{ field: "marketSeq", title: "시장명", width: 150, ezAlign: 'center',template:self.gridColumnTemplate.getTextFromCodeMap('marketSeq',pageParam.MARKET_CODE_MAP)},
		{ field: "adminId", title: "아이디", width: 150, ezAlign: 'center'},
		{ field: "adminName", title: "성명", width: 150, ezAlign: 'center'},
		{ field: "dutyName", title: "직책", width: 100, ezAlign: 'center', template: self.getLoginTyCode},
		{ field: "loginDate", title: "접속일시", width: 100, ezAlign: 'center', template: self.gridColumnTemplate.momentFormatter('loginDate') }
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
	rootVM = new AdminLoginLogVM();
	rootVM.init();
	ko.applyBindings(rootVM);

});
