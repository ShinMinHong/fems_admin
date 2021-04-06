var rootVM = null;

function RootVM() {

	var self = this;
	self.clazz = "RootVM";

	//Extend VM
	vmExtender.extendGridVM(self);

	//Details REST API URL
	self.listURL = contextPath + "/mngarea/api/page.json";
	self.changeAreaURL = contextPath + "/commonpopup/api/setmngarea";

	//Search VM
	self.searchData = ko.observable({
		searchMngAreaName: ""
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

	//Grid Event Listeners
	self.onGridEvent = function(type, data) {
		switch(type) {
			case "rowSelected" :
				break;
			case "dataSourceChanged":
				break;
		}
	};
	
	self.selectMngArea = function(mngAreaSeq){
		alert("selectMngArea:"+mngAreaSeq);
		$.ajax({
			url: $.format(self.changeAreaURL+"/{0}", mngAreaSeq),
			type: "GET",
			contentType: "application/json",
			data: null,
			success: function(data) {
				if(ezUtil.checkRESTResponseMsg(data)) {
					parent.location.reload();
				}
			}
		});
	}

	self.gridSelectColumn = function(dataItem){
		var mngAreaSeq = dataItem['mngAreaSeq'];
		return '<a href="javascript:;" onclick="rootVM[\'selectMngArea\'](\''+mngAreaSeq+'\');"><i class="icon-plus22"></i>[선택]</a>';
	};
	
	self.gridColumns = [
		{ field: "rn", title: "순번", width: 50, ezAlign: 'center'},
        { field: "mngAreaName", title: "지역명", minWidth: 200, ezAlign: 'center' },
		{ field: "managerName", title: "담당자", width:200, ezAlign: 'center'},
		{ field: "phoneNo", title: "연락처", width:200, ezAlign: 'center'},
		{ field: "mngAreaSeq", title: "관리", width: 100, ezAlign: 'center', template: self.gridSelectColumn }
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

	rootVM = new RootVM();
	rootVM.init();

	ko.applyBindings(rootVM);
});
