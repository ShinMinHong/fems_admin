var rootVM = null;

function SmsUserMngVM() {

	var self = this;
	self.clazz = "SmsUserMngVM";

	//Extend VM
	vmExtender.extendGridVM(self);

	self.baseURL = contextPath + "/store/storesmsusermng";
	//List API URL
	self.listURL = contextPath + "/store/storesmsusermng/api/page.json";
	//Details REST API URL
	self.detailsURL = contextPath + "/store/storesmsusermng/api/details";

	//관리지역 Code Map
	self.marketSelectOptions =  ezUtil.orderBySelectOptionsText(pageParam.MARKET_CODE_MAP);
	self.marketCodeMap = ezUtil.convertSelectOptionsToObject(pageParam.MARKET_CODE_MAP);
	//관리지역에 따른 점포 Code Map
	self.storeSelectOptions = ko.observableArray([]);
	//SMS 수신여부 Code Map
	self.smsYnSelectOptions = [{text:'수신', value:true}, {text:'미수신', value:false}];

	//Search VM
	self.searchData = ko.observable({
		searchMarketSeq: "",
		searchStoreName:"",
		searchManagerName:"",
		searchPhoneNo: ko.observable(""),
		searchSmsReceiveYn:""
	});

	self.searchPhoneNoThrottled = ko.computed(function() { return self.searchData().searchPhoneNo(); }).extend({throttle: 0});
    self.PhoneNoThrottled = function(newValue) {
        var formattedResult = ezUtil.inputPhoneNumberByValue(newValue);
        self.searchData().searchPhoneNo(formattedResult);
    };
    self.searchPhoneNoThrottled.subscribe(self.PhoneNoThrottled);


	//Search Condition Applied
	self.searchCondition = {};

	//Apply Search Condition
	self.applySearchCondition = function(param) {
		return _.extend(param, self.searchCondition);
	}

	//Search
	self.search = function() {

		if( !_.isEmpty(self.searchData().searchPhoneNo()) &&!ezUtil.isMobilePhoneCheck(self.searchData().searchPhoneNo()) ){
			alert('연락처 항목의 휴대폰번호가 유효하지 않습니다.');
			return false;
		}

		self.searchCondition = _.chain(ko.toJS(self.searchData()))
			.omit(_.isEmpty)
			.mapObject(function(val, key) { return _.isString(val)? encodeURIComponent(val) : val; })
			.value();
		self.grid.load();
	};

	//Grid Event Listeners
	self.onGridEvent = function(type, data) {
		switch(type) {
			case "rowSelected" :
				$.ajax({
					url: $.format("{0}/{1}.json", self.detailsURL, data.smsUserSeq),
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
		{ field: "mngAreaName", title: "관제지역명", width: 150, ezAlign: 'center'},
		{ field: "marketName", title: "관리시장명", width: 150, ezAlign: 'center'},
		{ field: "storeName", title: "점포명", width: 200, ezAlign: 'left'},
		{ field: "managerName", title: "이름", width: 150, ezAlign: 'center'},
		{ field: "phoneNo", title: "휴대폰번호", width: 150, ezAlign: 'center'},
		{ field: "smsReceiveYn", title: "수신여부", width: 150, ezAlign: 'center', template: self.gridColumnTemplate.codeMapFormatter('smsReceiveYn', ezUtil.smsYnCodeMap) },
		{ field: "regDate", title: "등록일", width: 90, ezAlign: 'center', template: self.gridColumnTemplate.momentISOFormatter('regDate') }
	];

	self.grid = new AbleKendoGridVM("#grid", self.listURL, self.gridColumns, self.onGridEvent, self.applySearchCondition);

	//Create
	self.onCreateClick = function() {
		self.detailsVM.startCreate({
			smsUserSeq:"",		// SMS수신대상고유번호
			mngAreaName:pageParam.MNG_AREA_NAME,	// 관제지역명
			marketSeq:"",		// 관리지역고유번호
			marketName:"",		// 관리지역명
			storeSeq:"",			// 점포고유번호
			storeName:"",		// 점포명
			managerName:"",	// 이름
			phoneNo:"",			// 휴대전화번호
			smsReceiveYn:true,	// SMS수신여부
			//dutyName:"",		// 직책
			regAdminId:"",		// 등록ID
			regDate:"",			// 등록일
			updAdminId:"",		// 최종수정자ID
			updDate:""			// 최종수정일
		});
	};

	self.detailsVM = new DataDetailsVM(self.detailsURL, self.grid.load);

	//Excel Download
	self.isMergeExcelCell = ko.observable(false);
	self.excelDownload = function(){
		if(rootVM.grid.dataSource._pristineData.length==0){
			alert("검색된 데이터가 없습니다.");
			return;
		}
		var url = self.baseURL +"/excel?";
		var data  = _.extend(self.searchCondition, {mergeExcelCell: self.isMergeExcelCell()});
		var params = $.param(data);
		document.location.href = url + params;
	}

	//Excel Upload
	self.openExcelUpload = function(){
		if (_.isBlank(self.excelUploadPopupSrc)){
			self.excelUploadPopupSrc = contextPath + "/commonpopup/openstoresmsuserexcelupload";
			self.excelUploadPopupVM = new ExcelUploadPopupVM("#excelUploadModal", self.excelUploadPopupSrc);
		}
		self.excelUploadPopupVM.open();
		if ( $('iframe[id=excelUploadModal]')[0].contentWindow.rootVM != null && $('iframe[id=excelUploadModal]')[0].contentWindow.rootVM.init != null ){
			$('iframe[id=excelUploadModal]')[0].contentWindow.rootVM.init();
		}
	};

	self.init = function() {
		self.search();
		console.log(self.clazz + " Initialized.");
	};
}

/** DetailsVM */
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

	//ValidationRules
	self.validationRules = {
		common: {
			marketSeq: { required: true },							//관리지역명
			storeSeq: { required: true },								//점포명
			managerName: { required: true, maxlength: 50 },	//점주명
			phoneNo: { required: true, maxlength: 13 }			//휴대전화번호
		}
	};
	self.validationRules.insert = _.extend(_.clone(self.validationRules.common), {
	});
	self.validationRules.update = _.extend(_.clone(self.validationRules.common), {
	});
	self.customValidation = function(){
		return true;
	}

	//Details 원본 Data
	self.originalData = ko.observable(null);
	//Details Data
	self.data = ko.observable(null);

	var setData = function(data) {
		var dataCloned = _.clone(data);
		if ( dataCloned != null ){
			dataCloned.orgMarketSeq = dataCloned.marketSeq;
			dataCloned.orgStoreSeq = dataCloned.storeSeq;
			dataCloned.marketSeq = ko.observable();
			dataCloned.storeSeq = ko.observable();

			dataCloned.marketSeq.subscribe(function(newValue){
				var storeSelectListTemp = [];

				// 시장이 변경되었다면, pageParam.STORE_NAME_LIST에서 해당시장 상점 목록 추출
				if(!_.isBlank(newValue)){
					storeSelectListTemp = _.chain(_.deepClone(pageParam.STORE_NAME_LIST)).filter(function(row) {return row['marketSeq'] ==  newValue} ).map(function(row) { return { text:row.storeName, value:row.storeSeq }; }).value();
				}

				//이름순으로 정렬하여, Options에 설정
				rootVM.storeSelectOptions(ezUtil.orderBySelectOptionsText(storeSelectListTemp));

				//최초 상점 코드로 변경(등록시에는 빈값, 수정시에는 해당 점포)
				self.data().storeSeq(self.data().orgStoreSeq)
			});

			dataCloned.phoneNo = ko.observable(dataCloned.phoneNo);

			_.defer(function(){
				self.data().marketSeq(self.data().orgMarketSeq);

				// 전화번호 자동으로 '-'를 붙여주기 위한 작업
			 	self.phoneNoThrottled = ko.computed(function() { return dataCloned.phoneNo(); }).extend({throttle: 0});
			    self.fcPhoneNoThrottled = function(newValue) {
			        var formattedResult = ezUtil.inputTelNumberByValue(newValue);
			        self.data().phoneNo(formattedResult);
			    };
			    self.phoneNoThrottled.subscribe(self.fcPhoneNoThrottled);

			});
		}
		var originalData = JSON.parse(ko.toJSON(dataCloned));
		self.originalData(originalData);
		self.data(dataCloned);
	};

	//편집모드 상태값
	var pkColumn = "smsUserSeq";
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

	//등록
	self.insertData = function() {
		console.log(self.clazz + ".insertData => ", self.data());

		/*if( self.isCreateMode() && confirm('추가하시겠습니까?')) {*/
			var rule = self.validationRules.insert;
			if( self.validate(rule, self.formId) && self.customValidation() ) {
				//관제지역관리번호 올리기
				self.data()["mngAreaSeq"] = pageParam.MNG_AREA_SEQ;
				var dataCloned = _.omit(ko.toJS(_.clone(self.data())), 'smsUserSeq', 'regDate', 'regAdminId', 'updAdminId', 'updDate', 'orgMarketSeq', 'orgStoreSeq');
				//alert('등록');
				$.ajax({
					url: $.format(self.detailsURL+"/{0}", "insert"),
					type: "POST",
					contentType: "application/json",
					data: dataCloned,
					success: function(data) {
						if(ezUtil.checkRESTResponseMsg(data)) {
							setData(null);
							self.onDetailsChanged();
							//alert("등록되었습니다.");
						}
					}
				});
			}
		/*}*/
	};

	//수정
	self.updateData = function() {
		console.log(self.clazz + ".updateData => ", self.data());

		/*if( self.isEditMode() && confirm('수정하시겠습니까?')) {*/
			var rule = self.validationRules.update;
			if( self.validate(rule, self.formId) && self.customValidation() ) {
				var originalData = self.originalData();
				var dataCloned = _.omit(ko.toJS(_.clone(self.data())), 'regDate');
				$.ajax({
					url: $.format(self.detailsURL+"/{0}/{1}", "update", originalData.smsUserSeq),
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
		/*}*/
	};

	//삭제
	self.deleteData = function() {
		console.log(self.clazz + ".deleteData => ", self.originalData());
		if(confirm('삭제하시겠습니까?')) {
			var originalData = self.originalData();
			$.ajax({
				url: $.format(self.detailsURL+"/{0}/{1}", "delete", originalData.smsUserSeq),
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

	//init
	self.init = function() {
		console.log(self.clazz + " Initialized.");
	};

}

function ExcelUploadPopupVM(elementId, url) {
	var self = this;
	self.clazz = "ExcelUploadPopupVM";

	self.popupId = elementId;
	self.iframeSrc = url;

	//Close Callback
	 self.close = function() {
		self.popupElement.dialog('close');
	};

	self.popupOptions = {
	         minHeight: 900,
	         height: 900,
	         buttons: [
	            {
	               text: '닫기',
	               icons: { primary: 'icon-cross3' },
	               click: self.close
	            }
	         ],
	   }
	//Extend VM
	vmExtender.extendIframePopupVM(self);
}


$(document).ready(function() {
	//공통 초기화
	ezUtil.ezInitialize(false/*blockUsingPageAjaxIndicator*/);
	rootVM = new SmsUserMngVM();
	rootVM.init();
	ko.applyBindings(rootVM);
});