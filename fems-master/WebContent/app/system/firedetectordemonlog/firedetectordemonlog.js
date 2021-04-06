var rootVM = null;

function FireDetectorDemonLogVM() {

	var self = this;
	self.clazz = "FireDetectorDemonLogVM";

	//Extend VM
	vmExtender.extendGridVM(self);

	//List API URL
	self.listURL = contextPath + "/system/firedetectordemonlog/api/page.json";

	//Search VM
	self.searchData = ko.observable({
		searchCtnNo: "",
		searchStartDate : ko.observable(moment(serverTime).add(-1, 'week').format("YYYY-MM-DD")),
		searchEndDate : ko.observable(moment(serverTime).format("YYYY-MM-DD"))
	});

	//Search Condition Applied
	self.searchCondition = {};
	//Apply Search Condition
	self.applySearchCondition = function(param) {
		return _.extend(param, self.searchCondition);
	};

	//Search
	self.search = function() {
		self.searchCondition = _.chain(self.searchData())
			.omit(_.isEmpty)
			.mapObject(function(val, key) { return _.isString(val)? encodeURIComponent(val) : val; })
			.value();
		self.grid.load();
	};

	self.gridColumns = [
		{ field: "fireDetectorDemonSignalSeq", title: "Seq", width: 80, ezAlign: 'center'},
		{ field: "msgVer", title: "Msg<br/>Ver", width:80, ezAlign: 'center'},
		{ field: "ip", title: "IP", width:130, ezAlign: 'center'},
		{ field: "port", title: "Port", width:80, ezAlign: 'center'},
		{ field: "ctnNo", title: "CTN", minWidth: 200, ezAlign: 'center' },
		
		{ field: "batteryValue", title: "배터리값", width:100, ezAlign: 'center'},
		{ field: "smokeValue", title: "연기값", width:100, ezAlign: 'center'},
		{ field: "temperatureValue", title: "온도값", width:100, ezAlign: 'center'},
		{ field: "flame1Value", title: "불꽃1값", width:100, ezAlign: 'center'},
		{ field: "flame2Value", title: "불꽃2값", width:100, ezAlign: 'center'},
		{ field: "coValue", title: "CO값", width:100, ezAlign: 'center'},
		{ field: "signalTypeValue", title: "신호타입값", width:100, ezAlign: 'center'},
		{ field: "fireEventValue", title: "화재타입", width:100, ezAlign: 'center'},
		{ field: "demonRegDate", title: "발생일시", width:150, ezAlign: 'center', template:self.gridColumnTemplate.momentFormatter('demonRegDate', 'YYYY-MM-DD HH:mm:ss.SSS')}
	];

	//Grid VM
	self.grid = new AbleKendoGridVM("#grid", self.listURL, self.gridColumns, self.onGridEvent, self.applySearchCondition);

	self.init = function() {
		self.grid.load();
		console.log(self.clazz + " Initialized.");
	};
}

$(document).ready(function() {

	ezUtil.ezInitialize(false/*blockUsingPageAjaxIndicator*/);

	rootVM = new FireDetectorDemonLogVM();
	rootVM.init();

	ko.applyBindings(rootVM);
});