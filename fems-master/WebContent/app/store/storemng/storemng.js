var rootVM = null;

function StoreMngVM() {

	var self = this;
	self.clazz = "StoreMngVM";

	//Extend VM
	vmExtender.extendGridVM(self);

	self.baseURL = contextPath + "/store/storemng";
	//List API URL
	self.listURL = contextPath + "/store/storemng/api/page.json";
	//Details REST API URL
	self.detailsURL = contextPath + "/store/storemng/api/details";

	//관리지역 Code Map
	self.marketSelectOptions =  ezUtil.orderBySelectOptionsText(pageParam.MARKET_CODE_MAP);
	self.marketCodeMap = ezUtil.convertSelectOptionsToObject(pageParam.MARKET_CODE_MAP);
	self.alarmYnSelectOptions = [{text:'전송', value:true}, {text:'미전송', value:false}];
	//SMS 수신여부 Code Map
	self.smsYnSelectOptions = [{text:'수신', value:true}, {text:'미수신', value:false}];
	// 감지기 정렬순서
	self.storeSortSelectOption = [{text:'점포명', value:"STORE_NAME"}];

	//Search VM
	self.searchData = ko.observable({
		searchMarketSeq: self.getSearchMarketSeq,
		searchStoreName: "",
		searchSort:""
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

	//Grid Event Listeners
	self.onGridEvent = function(type, data) {
		switch(type) {
			case "rowSelected" :
				$.ajax({
					url: $.format("{0}/{1}.json", self.detailsURL, data.storeSeq),
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
		{ field: "managerName", title: "담당자", width: 150, ezAlign: 'center'},
		{ field: "phoneNo", title: "휴대폰번호", width: 150, ezAlign: 'center'},
		{ field: "telephoneNo", title: "일반전화번호", width: 150, ezAlign: 'center'},
		{ field: "regDate", title: "등록일", width: 90, ezAlign: 'center', template: self.gridColumnTemplate.momentISOFormatter('regDate') }
	];

	self.grid = new AbleKendoGridVM("#grid", self.listURL, self.gridColumns, self.onGridEvent, self.applySearchCondition);

	//Create
	self.onCreateClick = function() {
		self.detailsVM.startCreate({
			mngAreaName:pageParam.MNG_AREA_NAME,	// 관제지역명
			marketSeq:"",		// 관리지역고유번호
			storeSeq:"",		// 점포고유번호
			storeName:"",		// 점포명
			managerName:"",	// 점주명
			phoneNo:"",			// 휴대전화번호
			telephoneNo:"",		// 일반전화번호
			zipCode:"",			// 우편번호
			roadAddress:"",		// 주소(도로명)
			parcelAddress:"",	// 주소(지번)
			detailsAddress:"",	// 주소(상세)
			businessDesc:"",		// 업종설명
			smsAlarmYn:true,		// SMS 알림
			firestationAlarmYn:true,	// 119 알림
			firestationName:"",	// 관할소방서명
			firestationManagerName:"",	// 소방서담당자명
			firestationTelephoneNo:"",	// 소방서연락처
			regAdminId:"",		// 등록ID
			regDate:"",			// 등록일
			updAdminId:"",		// 최종수정자ID
			updDate:"",			// 최종수정일
			noAlarm00:false,	// 알림제한00
			noAlarm01:false,	// 알림제한01
			noAlarm02:false,	// 알림제한02
			noAlarm03:false,	// 알림제한03
			noAlarm04:false,	// 알림제한04
			noAlarm05:false,	// 알림제한05
			noAlarm06:false,	// 알림제한06
			noAlarm07:false,	// 알림제한07
			noAlarm08:false,	// 알림제한08
			noAlarm09:false,	// 알림제한09
			noAlarm10:false,	// 알림제한10
			noAlarm11:false,	// 알림제한11
			noAlarm12:false,	// 알림제한12
			noAlarm13:false,	// 알림제한13
			noAlarm14:false,	// 알림제한14
			noAlarm15:false,	// 알림제한15
			noAlarm16:false,	// 알림제한16
			noAlarm17:false,	// 알림제한17
			noAlarm18:false,	// 알림제한18
			noAlarm19:false,	// 알림제한19
			noAlarm20:false,	// 알림제한20
			noAlarm21:false,	// 알림제한21
			noAlarm22:false,	// 알림제한22
			noAlarm23:false,	// 알림제한23
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

	//점포SMS 수신대상 액셀등록
	self.openstoresmsuserexcelupload = function(){
		if (_.isBlank(self.openstoresmsuserexceluploadPopupSrc)){
			self.openstoresmsuserexceluploadPopupSrc = contextPath + "/commonpopup/openstoresmsuserexcelupload";
			self.openstoresmsuserexceluploadPopupVM = new ExcelUploadPopupVM("#openstoresmsuserexceluploadModal", self.openstoresmsuserexceluploadPopupSrc);
		}
		self.openstoresmsuserexceluploadPopupVM.open();
		if ( $('iframe[id=openstoresmsuserexceluploadModal]')[0].contentWindow.rootVM != null && $('iframe[id=openstoresmsuserexceluploadModal]')[0].contentWindow.rootVM.init != null ){
			$('iframe[id=openstoresmsuserexceluploadModal]')[0].contentWindow.rootVM.init();
		}
	};

	//점포 액셀 등록
	self.openstoreexcelupload = function(){
		if (_.isBlank(self.openstoreexceluploadPopupSrc)){
			self.openstoreexceluploadPopupSrc = contextPath + "/commonpopup/openstoreexcelupload";
			self.openstoreexceluploadPopupVM = new ExcelUploadPopupVM("#openstoreexceluploadModal", self.openstoreexceluploadPopupSrc);
		}
		self.openstoreexceluploadPopupVM.open();
		if ( $('iframe[id=openstoreexceluploadModal]')[0].contentWindow.rootVM != null && $('iframe[id=openstoreexceluploadModal]')[0].contentWindow.rootVM.init != null ){
			$('iframe[id=openstoreexceluploadModal]')[0].contentWindow.rootVM.init();
		}
	};


	self.init = function() {
		self.search();
		console.log(self.clazz + " Initialized.");
	};

	//기타 설정===========================================================
	//주소 검색
	self.commonPopupEvent = new CommonPopupEvent();

	self.clickSearchAddress = function() {
		new daum.Postcode(self.commonPopupEvent.searchAddrEvent.openFn).open();
	}

	//좌표 선택
	/*var originalLocationModal = $('#selectLocationModal').clone();
	self.showSelectLocationModal = function(){
		if ( self.selectLocationPopupSrc == null ){
			self.selectLocationPopupSrc = contextPath + "/commonpopup/openselectlocation";
			self.selectLocationPopupVM = new SelectLocationPopupVM("#selectLocationModal", self.selectLocationPopupSrc);
		} else {
			$('#selectLocationModal').remove();
			var myClone = originalLocationModal.clone();
			$('body').append(myClone);
			self.selectLocationPopupVM = new SelectLocationPopupVM(myClone, self.selectLocationPopupSrc);
		}
		self.selectLocationPopupVM.open();
	};*/
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
	vmExtender.extendGridVM(self);

	//Extend VM
	vmExtender.extendValidatableVM(self);

	//Form
	self.formId = "#detailsForm";

	//ValidationRules
	self.validationRules = {
		common: {
			marketSeq: { required: true },							//관리지역명
			storeName: { required: true, maxlength: 50 },			//점포명
			//managerName: { required: true, maxlength: 50 },	//점주명 2020-07-01 이승용과장 요청에의해 필수입력사항에서 제외됨.
			phoneNo: { required: true, maxlength: 13 }			//휴대전화번호
		}
	};
	self.validationRules.insert = _.extend(_.clone(self.validationRules.common), {
	});
	self.validationRules.update = _.extend(_.clone(self.validationRules.common), {
	});
	self.customValidation = function(){
		if (_.isBlank(self.data()["roadAddress"]())) {
			alert("점포 주소를 입력해 주세요.");
			return false;
		}

		return true;
	}

	self.storeSmsUserListGridColumns = [
		{ field: "rn", title: "No", width: 60, ezAlign: 'center'},
		{ field: "managerName", title: "이름", width: 150, ezAlign: 'center'},
		{ field: "phoneNo", title: "휴대폰번호", width: 150, ezAlign: 'center'},
		{ field: "smsReceiveYn", title: "수신여부", width: 150, ezAlign: 'center', template: self.gridColumnTemplate.codeMapFormatter('smsReceiveYn', ezUtil.smsYnCodeMap) },
		{ field: "regDate", title: "등록일", width: 90, ezAlign: 'center', template: self.gridColumnTemplate.momentISOFormatter('regDate') }
	];

	self.storeSmsUserListGrid = null;

	//Details 원본 Data
	self.originalData = ko.observable(null);
	//Details Data
	self.data = ko.observable(null);

	// 현재 DB에 저장된 SMS 수신 대상자 수
	var storeSmsUserCount = 0;
	var setData = function(data) {
		var dataCloned = _.clone(data);
		if ( dataCloned != null ){
			dataCloned.zipCode = ko.observable(dataCloned.zipCode);
			dataCloned.roadAddress = ko.observable(dataCloned.roadAddress);
			dataCloned.parcelAddress = ko.observable(dataCloned.parcelAddress);
			//dataCloned.latitude = ko.observable(dataCloned.latitude);
			//dataCloned.longitude = ko.observable(dataCloned.longitude);
			dataCloned.telephoneNo = ko.observable(dataCloned.telephoneNo);
			dataCloned.phoneNo = ko.observable(dataCloned.phoneNo);

			// SMS대상자가 없는 경우 UI 강제 주입
			if(_.isEmpty(data.storeSmsUserList)){
				data.storeSmsUserList = [new self.createSmsUserModel()];
			}else{
				// 현재 SMS 대상자 수 기록
				storeSmsUserCount = data.storeSmsUserList.length;
			}
			dataCloned.storeSmsUserList = ko.observableArray(data.storeSmsUserList);

			_.defer(function(){
				// 전화번호 자동으로 '-'를 붙여주기 위한 작업
			 	self.phoneNoThrottled = ko.computed(function() { return dataCloned.phoneNo(); }).extend({throttle: 0});
			    self.fcPhoneNoThrottled = function(newValue) {
			        var formattedResult = ezUtil.inputTelNumberByValue(newValue);
			        self.data().phoneNo(formattedResult);
			    };
			    self.phoneNoThrottled.subscribe(self.fcPhoneNoThrottled);

			    self.telephoneNoThrottled = ko.computed(function() { return dataCloned.telephoneNo(); }).extend({throttle: 0});
			    self.fcTelephoneNoThrottled = function(newValue) {
			        var formattedResult = ezUtil.inputTelNumberByValue(newValue);
			        self.data().telephoneNo(formattedResult);
			    };
			    self.telephoneNoThrottled.subscribe(self.fcTelephoneNoThrottled);

			    /*if ( self.isEditMode() ) {
			    	self.storeSmsUserListGrid = new AbleKendoTableVM("#storeSmsUserListGrid", self.storeSmsUserListGridColumns, {height: 350});
					self.storeSmsUserListGrid.changeGrid(dataCloned? dataCloned.storeSmsUserList : [],self.storeSmsUserListGridColumns);
			    }*/
			});
		}
		self.originalData(dataCloned);
		self.data(dataCloned);
	};

	// SMS 대상자 리스트 모델
	self.createSmsUserModel = function(){
		var model = {managerName:"" ,phoneNo: "", smsReceiveYn:true};
		return model;
	};

	// SMS 대상자 리스트 추가
	self.addSmsUser = function(){

		self.data()['storeSmsUserList'].push(new self.createSmsUserModel());
	}

	// SMS 대상자 리스트 삭제
	self.deleteSmsUser = function(obj){
		  self.data()['storeSmsUserList']
		  self.data()['storeSmsUserList'].remove(obj)
		  if(rootVM.detailsVM.data().storeSmsUserList().length == 0){
			  self.addSmsUser();
		  }
	}

	//편집모드 상태값
	var pkColumn = "storeSeq";
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
			var rule = self.validationRules.insert;
			if( self.validate(rule, self.formId) && self.customValidation() && confirm('추가하시겠습니까?') ) {
				//관제지역관리번호 올리기
				self.data()["mngAreaSeq"] = pageParam.MNG_AREA_SEQ;
				var dataCloned = _.omit(ko.toJS(_.clone(self.data())), 'regDate');
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
						}
					}
				});
			}
	};

	//수정
	self.updateData = function() {
		console.log(self.clazz + ".updateData => ", self.data());

			var rule = self.validationRules.update;
			if( self.validate(rule, self.formId) && self.customValidation() && confirm('수정하시겠습니까?')) {
				var originalData = self.originalData();
				var dataCloned = _.omit(ko.toJS(_.clone(self.data())), 'regDate');
				$.ajax({
					url: $.format(self.detailsURL+"/{0}/{1}", "update", originalData.storeSeq),
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

	//삭제
	self.deleteData = function() {
		console.log(self.clazz + ".deleteData => ", self.originalData());
		if(confirm('삭제하시겠습니까?')) {
			var originalData = self.originalData();
			$.ajax({
				url: $.format(self.detailsURL+"/{0}/{1}", "delete", originalData.storeSeq),
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

	//SMS 수신 대상자 저장
	self.smsUserUpdateData = function() {
		console.log(self.clazz + ".smsUserUpdateData => ", self.originalData());

		var originalData = self.originalData();
		var dataCloned = _.omit(ko.toJS(_.clone(self.data())), 'regDate');

		if (dataCloned.storeSmsUserList.length > 1) {
			// 현재 SMS 대상자수가 2건 이상이라면
			// 		빈항목이 없다면 서버로 전송
			if ( _.chain(dataCloned.storeSmsUserList).pluck('managerName').some(_.isBlank).value() ||
					_.chain(dataCloned.storeSmsUserList).pluck('phoneNo').some(_.isBlank).value() ){
				alert('수신 대상자 이름 또는 전화번호을 입력해 주세요.');
				return;
			}
		} else {
			// 현재 SMS 대상자수가 1건(default) 이라면
			if (storeSmsUserCount == 0) {
				// 기존 DB에 데이터가 없었다면
				// 		유효성 검사 후 서버 전송
				if ( _.chain(dataCloned.storeSmsUserList).pluck('managerName').some(_.isBlank).value() ||
						_.chain(dataCloned.storeSmsUserList).pluck('phoneNo').some(_.isBlank).value() ){
					alert('수신 대상자 이름 또는 전화번호을 입력해 주세요.');
					return;
				}

			} else {
				// 기존에 DB에 데이터가 있었고 현재 1건이라면
				if ( _.chain(dataCloned.storeSmsUserList).pluck('managerName').some(_.isBlank).value() &&
						_.chain(dataCloned.storeSmsUserList).pluck('phoneNo').some(_.isBlank).value() ){
					// 모든 데이터가 비어 있다면
					//		서버로 전송 모두 삭제
					alert('기존 데이터를 모두 삭제 합니다.');
					dataCloned.storeSmsUserList = null;
				} else {
					if ( _.chain(dataCloned.storeSmsUserList).pluck('managerName').some(_.isBlank).value() ||
							_.chain(dataCloned.storeSmsUserList).pluck('phoneNo').some(_.isBlank).value() ){
						alert('수신 대상자 이름 또는 전화번호을 입력해 주세요.');
						return;
					}
				}
			}
		}

		if(confirm('SMS 수신 대상자를 저장 하시겠습니까?')) {
			$.ajax({
				url: $.format(self.detailsURL+"/{0}/{1}", "updatesmsuser", dataCloned.storeSeq),
				type: "POST",
				contentType: "application/json",
				data: dataCloned,
				success: function(data) {
					if(ezUtil.checkRESTResponseMsg(data)) {
						_.defer(function() {
							alert('SMS 수신 대상자를 저장 하였습니다.');
							/*setData(null);
							self.onDetailsChanged();*/
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

function SelectLocationPopupVM(elementId, url) {
	var self = this;
	self.clazz = "SelectLocationPopupVM";

	self.popupId = elementId;
	self.iframeSrc = url;

	//Close Callback
	 self.close = function() {
		self.popupElement.dialog('close');
	};

	//Extend VM
	vmExtender.extendMapIframePopupVM(self);
}

function CommonPopupEvent(){
	var self = this;

	self.searchAddrEvent = {
		successCallBack: function(data){
			rootVM.detailsVM.data().zipCode(data.zip);
			rootVM.detailsVM.data().roadAddress(data.roadAddr);
			rootVM.detailsVM.data().parcelAddress(data.jibunAddr);
			//rootVM.detailsVM.data().latitude(data.Ga);
			//rootVM.detailsVM.data().longitude(data.Ha);
		},
		openFn: {
            oncomplete: function(data) {
        		var zip = data.zonecode;
        		var roadAddr = data.address;
        		var jibunAddr = '';
        		if(data.buildingName !== ''){
        			roadAddr += ' ('+data.buildingName +')';
        		}
        		if(data.autoJibunAddress != ''){
        			jibunAddr = data.autoJibunAddress;
        		} else {
        			jibunAddr = data.jibunAddress;
        		}
                rootVM.commonPopupEvent.searchAddrEvent.successCallBack({zip: zip, roadAddr: roadAddr, jibunAddr: jibunAddr});
                //rootVM.commonPopupEvent.searchAddrEvent.successCallBack({zip: zip, roadAddr: roadAddr, jibunAddr: jibunAddr, Ga:'', Ha:''});
                //좌표선택모달 오픈
                //rootVM.showSelectLocationModal();
            }
        }
	};

	/*self.selectLocationEvent = {
		successCallback: function(obj){
			if ( !_.isNull(rootVM.detailsVM) && _.isObject(rootVM.detailsVM.data()) ){
				var detailsData = rootVM.detailsVM.data();
				detailsData['latitude'](obj.la);
				detailsData['longitude'](obj.lo);
			}
			rootVM.selectLocationPopupVM.close();

		}
	}*/
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
	rootVM = new StoreMngVM();
	rootVM.init();
	ko.applyBindings(rootVM);

});