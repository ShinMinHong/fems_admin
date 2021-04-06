var rootVM = null;

// 문자메시지 발신이력 조회
function MsgSendLogVM() {

	var self = this;
	self.clazz = "MsgSendLogVM";

	//Extend VM
	vmExtender.extendGridVM(self);

	//Base URL
	self.baseURL = contextPath + "/areasystem/msgsendlog"
	//List API URL
	self.listURL = self.baseURL +"/api/page.json";

	//Search VM
	self.searchData ={
		searchReceiveUserNm: null,
		searchReceivePhoneNo: ko.observable(""),
		searchStartDate : ko.observable(moment(serverTime).add(-1, 'week').format("YYYY-MM-DD")),
		searchEndDate : ko.observable(moment(serverTime).format("YYYY-MM-DD"))
	};

	// 수신전화 번호 throttle
    self.searchReceivePhoneNoThrottled = ko.computed(function() { return self.searchData.searchReceivePhoneNo(); }).extend({throttle: 0});
    self.ReceivePhoneNoThrottled = function(newValue) {
        var formattedResult = ezUtil.inputPhoneNumberByValue(newValue);
        self.searchData.searchReceivePhoneNo(formattedResult);
    };
    self.searchReceivePhoneNoThrottled.subscribe(self.ReceivePhoneNoThrottled);

	//Search Condition Applied
	self.searchCondition = {};

	//Apply Search Condition
	self.applySearchCondition = function(param) {
		return _.extend(param, self.searchCondition);
	}

	//Search
	self.search = function() {
		if( !_.isEmpty(self.searchData.searchReceivePhoneNo()) &&!ezUtil.isMobilePhoneCheck(self.searchData.searchReceivePhoneNo()) ){
			alert('연락처 항목의 휴대폰번호가 유효하지 않습니다.');
			return false;
		}
		var dataCloned = ko.toJS(self.searchData);
		dataCloned.searchReceivePhoneNo = self.searchData.searchReceivePhoneNo().replace(/-/gi,"");
		self.searchCondition = _.chain(dataCloned)
			.omit(_.isEmpty)
			.mapObject(function(val, key) { return _.isString(val)? encodeURIComponent(val) : val; })
			.value();
		self.grid.load();
	};

	//Data Table Columns Settings
	self.gridColumns = [
		{ field: "rn", title: "No", width: 40, ezAlign: 'center'},
		{ field: "mngAreaName", title: "관리지역명", width: 120, ezAlign: 'center'},
		{ field: "marketName", title: "시장명", width: 200, ezAlign: 'center'},
		{ field: "receiveUserNm", title: "이름", width: 100, ezAlign: 'center'},
		{ field: "receivePhoneNo", title: "수신 번호", width: 150, ezAlign: 'center'},
		{ field: "sendDate", title: "발송일", width: 150, ezAlign: 'center', template: self.gridColumnTemplate.momentFormatter('sendDate')},
		{ field: "smsTitle", title: "발송제목", width: 150, ezAlign: 'center'},
		{ field: "smsMessage", title: "발송 내용", width: 1600, ezAlign: 'left'}
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

	rootVM = new MsgSendLogVM();
	rootVM.init();

	ko.applyBindings(rootVM);
});
