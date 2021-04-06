var rootVM = null;

function RolegroupMngVM() {

	var self = this;
	self.clazz = "RolegroupMngVM";

	//Extend VM
	vmExtender.extendGridVM(self);

	//Base URL
	self.baseURL = contextPath + "/system/rolegroupmng";
	//List API URL
	self.listURL = self.baseURL +"/api/page.json";
	//Details REST API URL
	self.detailsURL = self.baseURL +"/api/details";

	//Search VM
	self.searchData = ko.observable({
		searchRolegroupCode: "",
		searchRolegroupName: ""
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

	//Create
	self.onCreateClick = function() {
		self.detailsVM.startCreate({
			rolegroupCode: null,
			rolegroupName: null,
			regDate: "",
			roleDetailsList: _.chain(_.deepClone(pageParam.roleList)).map(function(row){ row.checked = false; return row; }).value()
		});
	};

	//Grid Event Listeners
	self.onGridEvent = function(type, data) {
		switch(type) {
			case "rowSelected" :
				$.ajax({
					url: $.format("{0}/{1}.json", self.detailsURL, data.rolegroupCode),
					type: "GET",
					contentType: "application/json",
					success: function(data) {
						if(ezUtil.checkRESTResponseMsg(data)) {
							self.detailsVM.startEdit(data.body);
						}
					}
				});
				break;
			case "dataSourceChanged":
				self.detailsVM.cancel();
				break;
		}
	};

	self.gridColumns = [
		{ field: "rn", title: "순번", width: 80, ezAlign: 'center'},
        { field: "rolegroupCode", title: "권한그룹코드", width: 300, ezAlign: 'center', template: self.authTypeTemplate },
		{ field: "rolegroupName", title: "권한그룹명", minWidth:300, ezAlign: 'center'},
		{ field: "regDate", title: "등록일", width: 200, ezAlign: 'center', template: self.gridColumnTemplate.momentFormatter('regDate','YYYY-MM-DD') }
	];

	// Default grid height:670
	self.customGridOption = {height: 340};

	//Grid VM
	self.grid = new AbleKendoGridVM("#grid", self.listURL, self.gridColumns, self.onGridEvent, self.applySearchCondition, self.customGridOption);

	//Details VM
	self.detailsVM = new DataDetailsVM(self.detailsURL, self.grid.load);

	//Excel Download
	self.isMergeExcelCell = ko.observable(false);
	self.excelDownload = function(){
		var url = contextPath + "/system/rolegroupmng/excel?";
		var data  = _.extend(self.searchCondition, {mergeExcelCell: self.isMergeExcelCell()});
		var params = $.param(data);
		document.location.href = url + params;
	}

	self.init = function() {
		self.grid.load();
		self.detailsVM.init();
		console.log(self.clazz + " Initialized.");
	};
}

/**
 * DetailsVM
 */
function DataDetailsVM(detailsURL, onDetailsChanged) {
	var self = this;
	self.clazz = "DetailsVM";

	//상세정보 REST API 기본URL - CRUD
	self.detailsURL = detailsURL;

	//데이터 변경 핸들러
	self.onDetailsChanged = onDetailsChanged || function() {};

	//Extend VM
	vmExtender.extendValidatableVM(self);

	//Form
	self.formId = "#detailsForm";

	self.validationRules = {
			common: {
				rolegroupName: { required: true, maxlength: 20 }
			}
		};

	self.validationRules.insert = _.extend(_.clone(self.validationRules.common), {
		rolegroupCode: { required: true, maxlength: 20 }
	});
	self.validationRules.update = _.extend(_.clone(self.validationRules.common), {
	});

	//Details 원본 Data
	self.originalData = ko.observable(null);
	//Details Data
	self.data = ko.observable(null);

	var setData = function(data) {
		var dataCloned = _.clone(data);
		self.originalData(dataCloned);
		self.data(dataCloned);
	};

	//편집모드 상태값
	var pkColumn = "rolegroupCode";
	self.isEditMode = ko.computed(function() { return !_.isNull(self.originalData()) && !_.isBlank(self.originalData()[pkColumn]); });
	self.isCreateMode = ko.computed(function() { return !_.isNull(self.originalData()) && _.isBlank(self.originalData()[pkColumn]); });
	self.isVisible = ko.computed(function() { return self.isEditMode() || self.isCreateMode() });

	//수정시작
	self.startEdit = function(details) {
		console.log(self.clazz + ".startEdit => ", details);
		setData(details);
		$(window).scrollTo("#detailsForm", 300);
	};
	//생성시작
	self.startCreate = function(details) {
		console.log(self.clazz + ".startCreate => ", details);
		setData(details);
		$(window).scrollTo("#detailsForm", 300);
	};
	//취소
	self.cancel = function() {
		setData(null);
	};

	//삭제
	self.deleteData = function() {
		console.log(self.clazz + ".deleteData => ", self.originalData());
		if(confirm('권한그룹 삭제는 시스템운영에 문제를 야기시킬 수 있습니다. 삭제하시겠습니까?')) {
			var originalData = self.originalData();
			$.ajax({
				url: $.format(self.detailsURL+"/{0}/{1}", "delete", self.originalData()[pkColumn]),
				type: "DELETE",
				contentType: "application/json",
				data: null,
				success: function(data) {
					if(ezUtil.checkRESTResponseMsg(data)) {
						_.defer(function() {
							setData(null);
							self.onDetailsChanged();
						})
					}
				}
			});
		}
	};

	//insert
	self.insertData = function() {
		console.log(self.clazz + ".insertData => ", self.data());
		var rule = self.validationRules.insert;
		if( self.isCreateMode() && self.validate(rule, self.formId) ) {
			var dataCloned = _.omit(_.clone(self.data()), 'regDate');
			//var roleCodeList = _.chain(self.data().roleDetailsList).where({checked:true}).map(function(item){return item.roleCode;}).value();
			//dataCloned.roleCodeList = roleCodeList;
			$.ajax({
				url: $.format(self.detailsURL+"/{0}", "insert"),
				type: "POST",
				contentType: "application/json",
				data: dataCloned,
				success: function(data) {
					if(ezUtil.checkRESTResponseMsg(data)) {
						setData(null);
						self.onDetailsChanged();
						alert("등록되었습니다.");
					}
				}
			});
		}
	};

	//update
	self.updateData = function() {
		console.log(self.clazz + ".updateData => ", self.data());
		if( self.isEditMode() && confirm('수정하시겠습니까?')) {
			var rule = self.validationRules.update;
			if( self.validate(rule, self.formId) ) {
				var originalData = self.originalData();
				var dataCloned = _.omit(_.clone(self.data()), 'regDate');
				var roleCodeList = _.chain(self.data().roleDetailsList).where({checked:true}).map(function(item){return item.roleCode;}).value();
				dataCloned.roleCodeList = roleCodeList;
				$.ajax({
					url: $.format(self.detailsURL+"/{0}/{1}", "update", self.originalData()[pkColumn]),
					type: "PUT",
					contentType: "application/json",
					data: dataCloned,
					success: function(data) {
						if(ezUtil.checkRESTResponseMsg(data)) {
							setData(null);
							self.onDetailsChanged();
						}
					}
				});
			}
		}
	};

	//init
	self.init = function() {
		console.log(self.clazz + " Initialized.");
	};
};

$(document).ready(function() {
	ezUtil.ezInitialize(false/*blockUsingPageAjaxIndicator*/);

	rootVM = new RolegroupMngVM();
	rootVM.init();

	ko.applyBindings(rootVM);
});