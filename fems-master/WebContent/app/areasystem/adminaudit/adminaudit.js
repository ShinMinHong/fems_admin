var rootVM = null;

function AdminAuditVM() {

	var self = this;
	self.clazz = "AdminAuditVM";

	//Extend VM
	vmExtender.extendGridVM(self);

	//List API URL
	self.listURL = contextPath + "/areasystem/adminaudit/api/page";
	//Details REST API URL
	self.detailsURL = contextPath + "/areasystem/adminaudit/api/details";

	//Search VM
	self.searchData = ko.observable({
		searchManagerId: "",
		searchMenuName: "",
		searchActionName: "",
		searchStartDate : ko.observable(moment(serverTime).add(-1, 'week').format("YYYY-MM-DD")),
		searchEndDate : ko.observable(moment(serverTime).format("YYYY-MM-DD"))
	});

	//Search Condition Applied
	self.searchCondition = {};
	//Apply Search Condition
	self.applySearchCondition = function(param) {
		return _.extend(param, self.searchCondition);
	}

	//Search
	self.search = function() {
		self.searchCondition = _.chain(self.searchData())
			.omit(_.isEmpty)
			.mapObject(function(val, key) { return _.isString(val)? encodeURIComponent(val) : val; })
			.value();
		self.grid.load();
	};

	//Grid Event Listeners
	self.onGridEvent = function(type, data) {
		switch(type) {
			case "rowSelected" :
				self.detailsVM.startDetails(_.clone(data));
				break;
			case "dataSourceChanged":
				self.detailsVM.cancel();
				break;
		}
	};

	self.gridColumns = [
		{ ezAlign: 'left', field: "auditSeq", title: "SEQ", width: 50 },
        { ezAlign: 'left', field: "actionName", title: "작업명", width: 150 },
        { ezAlign: 'left', field: "actionDetail", title: "작업상세", minWidth: 300 },
		{ ezAlign: 'center', field: "managerId", title: "관리자ID", width: 120 },
		{ ezAlign: 'center', field: "managerName", title: "관리자명", width: 120 },
		{ ezAlign: 'center', field: 'regDate', title: "등록일", width: 150, template: self.gridColumnTemplate.momentFormatter('regDate') }
	];

	//Grid VM
	self.grid = new AbleKendoGridVM("#grid", self.listURL, self.gridColumns, self.onGridEvent, self.applySearchCondition);

	//Details VM
	self.detailsVM = new DataDetailsVM(self.detailsURL);

	//Excel Download
	self.isMergeExcelCell = ko.observable(false);
	self.excelDownload = function(){
		var url = contextPath + "/areasystem/adminaudit/excel?";
		var data  = _.extend(self.searchCondition, {mergeExcelCell: self.isMergeExcelCell()});
		var params = $.param(data);
		document.location.href = url + params;
	}

	self.init = function() {
		self.search();
		self.detailsVM.init();
		console.log(self.clazz + " Initialized.");
	};
}

/**
 * DetailsVM
 */
function DataDetailsVM(detailsURL) {
	var self = this;
	self.clazz = "DetailsVM";

	//상세정보 REST API 기본URL - CRUD
	self.detailsURL = detailsURL;

	//Extend VM
	vmExtender.extendBaseVM(self);

	//Form
	self.formId = "#detailsForm";

	//Details Data
	self.data = ko.observable(null);

	var setData = function(data) {
		var dataCloned = _.clone(data);
		self.data(dataCloned);
	};

	//편집모드 상태값
	var pkColumn = "auditSeq";
	self.isVisible = ko.computed(function() { return !_.isNull(self.data()) && !_.isBlank(self.data()[pkColumn]); });

	self.startDetails = function(details) {
		console.log(self.clazz + ".startDetails => ", details);
		setData(details);
		$(window).scrollTo(self.formId, 300);
	};

	//취소
	self.cancel = function() {
		setData(null);
	};

	//init
	self.init = function() {
		console.log(self.clazz + " Initialized.");
	};
};

$(document).ready(function() {
	ezUtil.ezInitialize(false/*blockUsingPageAjaxIndicator*/);

	rootVM = new AdminAuditVM();
	rootVM.init();

	ko.applyBindings(rootVM);
});
