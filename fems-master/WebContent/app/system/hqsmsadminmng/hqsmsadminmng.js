var rootVM = null;

//SMS수신자 관리
function SmsAdminMngVM() {

	var self = this;
	self.clazz = "SmsAdminMngVM";

	//Extend VM
	vmExtender.extendGridVM(self);

	//Base URL
	self.baseURL = contextPath + "/system/hqsmsadminmng";
	//List API URL
	self.listURL = self.baseURL +"/api/page.json";
	//Details REST API URL
	self.detailsURL = self.baseURL +"/api/details";

	//Search VM
	self.searchData = {
		searchSmsReceiveYn:"", // SMS 수신여부
		searchAdminName:"" // 이름
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

	//Create
	self.onCreateClick = function() {
		self.detailsVM.startCreate({
			smsAdminSeq:"",
			adminName:"",
			dutyName:"",
			phoneNo:"",
			smsReceiveYn:"",
			regAdminId :"",
			regDate:"",
			updAdminId :"",
			updDate:""
		});
	};

	//Grid Event Listeners
	self.onGridEvent = function(type, data) {
		switch(type) {
			case "rowSelected" :
				$.ajax({
					url: $.format("{0}/{1}.json", self.detailsURL, data.smsAdminSeq),
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

	//Data Table Columns Settings
	self.gridColumns = [
		{ field: "rn", title: "No", width: 40, ezAlign: 'center'},
		{ field: "adminName", title: "이름", width: 150, ezAlign: 'center'},
		{ field: "dutyName", title: "직책", width: 150, ezAlign: 'center'},
		{ field: "phoneNo", title: "휴대폰번호", width: 150, ezAlign: 'center'},
		{ field: "smsReceiveYn", title: "SMS수신여부", width:70, ezAlign: 'center', template: self.gridColumnTemplate.TFCodeMap('smsReceiveYn','수신','미수신')  },
		{ field: "regDate", title: "등록일", width: 90, ezAlign: 'center', template: self.gridColumnTemplate.momentISOFormatter('regDate') }
	];

	// Default grid height:670
	self.customGridOption = {height: 340};

	self.grid = new AbleKendoGridVM("#grid", self.listURL, self.gridColumns, self.onGridEvent, self.applySearchCondition, self.customGridOption);

	self.detailsVM = new DataDetailsVM( self.detailsURL, self.grid.load);

	self.init = function() {
		self.search();
		console.log(self.clazz + " Initialized.");
	};
}

function DataDetailsVM(detailsURL, onDetailsChanged) {

	var self = this;
	self.clazz = "DetailsVM";

	//상세정보 REST API 기본URL - CRUD
	self.detailsURL = detailsURL;

	//데이터 변경 핸들러
	self.onDetailsChanged = onDetailsChanged || function() {};

	//Extend VM
	vmExtender.extendValidatableVM(self);

	//Extend VM
	vmExtender.extendGridVM(self);

	//Form
	self.formId = "#detailsForm";

	// Validation
	self.validationRules = {
		common: {
			adminName:{required: true}, // 아이디 이름
			phoneNo:{required: true}, // 휴대폰 번호
		}
	};

	self.validationRules.insert = _.extend(_.clone(self.validationRules.common), {
	});

	self.validationRules.update = _.extend(_.clone(self.validationRules.common), {

	});

	self.customValidation = function(){
		if( !ezUtil.isMobilePhoneCheck(self.data().phoneNo()) ){
			alert('연락처 항목의 휴대폰번호가 유효하지 않습니다.');
			return false;
		}
		return true;
	};

	//Details original data
	self.originalData = ko.observable(null);
	//Details copy data
	self.data = ko.observable(null);

	var setData = function(data) {
		//  ezUtil.dataTypeToString - Object value 값을  스트링 값으로 변환
		var dataCloned = ezUtil.dataTypeToString(data);

		if ( dataCloned != null ){
			dataCloned.adminName = ko.observable(dataCloned.adminName);
			dataCloned.phoneNo = ko.observable(dataCloned.phoneNo);
			dataCloned.smsReceiveYn = ko.observable(dataCloned.smsReceiveYn);

			// 핸드폰 번호 입력시 핸드폰번호 포멧변경(ex.010xxxxxxxx->010-xxxx-xxxx)
			dataCloned.phoneNo.subscribe(function(newValue){
				var formattedResult = ezUtil.inputPhoneNumberByValue(newValue);
				dataCloned.phoneNo(formattedResult);
			});
		}
		self.originalData(data);
		self.data(dataCloned);
	};

	var pkColumn = "smsAdminSeq";
	self.isEditMode = ko.computed(function() { return !_.isNull(self.originalData()) && !_.isBlank(self.originalData()[pkColumn]); });
	self.isCreateMode = ko.computed(function() { return !_.isNull(self.originalData()) && _.isBlank(self.originalData()[pkColumn]); });
	self.isVisible = ko.computed(function() { return self.isEditMode() || self.isCreateMode() });

	//수정시작
	self.startEdit = function(details) {
		console.log(self.clazz + ".startEdit => ", details);
		setData(details);
		$(window).scrollTo(self.formId, 300);
	};

	//생성시작
	self.startCreate = function(details) {
		console.log(self.clazz + ".startCreate => ", details);
		setData(details);
		$(window).scrollTo(self.formId, 300);
	};

	//취소
	self.cancel = function() {
		setData(null);
	};

	// 등록
	self.insertData = function() {
		var rule = self.validationRules.insert;
		if( self.validate(rule, self.formId) && self.customValidation() && confirm('추가하시겠습니까?')) {
			var dataCloned = ko.toJS(self.data());
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
		var rule = self.validationRules.update;
		if( self.validate(rule, self.formId) && self.customValidation() && confirm('수정하시겠습니까?')){
			var dataCloned = ko.toJS(self.data());
			$.ajax({
				url: $.format(self.detailsURL+"/{0}/{1}", "update", self.originalData()[pkColumn]),
				type: "POST",
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
	};

	//delete
	self.deleteData = function() {
		console.log(self.clazz + ".deleteData => ", self.originalData());
		if(confirm('삭제하시겠습니까?')) {
			$.ajax({
				url: $.format(self.detailsURL+"/{0}/{1}", "delete", self.originalData()[pkColumn]),
				type: "POST",
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

}

$(document).ready(function() {
	//공통 초기화
	ezUtil.ezInitialize(false/*blockUsingPageAjaxIndicator*/);
	rootVM = new SmsAdminMngVM();
	rootVM.init();
	ko.applyBindings(rootVM);
});