var rootVM = null;
var mngAreaSeq = "";

function MarketMngVM() {

	var self = this;
	self.clazz = "MarketMngVM";

	//Extend VM
	vmExtender.extendGridVM(self);

	//Base URL
	self.baseURL = contextPath + "/areasystem/marketmng";
	//List API URL
	self.listURL = self.baseURL +"/api/page.json";
	//Details REST API URL
	self.detailsURL = self.baseURL +"/api/details";

	// Select Options
	self.areaSelectOptions = _.filter(pageParam.MNG_AREA_CODE_MAP, function(row){return row.value == pageParam.MNG_AREA_SEQ});

	//Search VM
	self.searchData = ko.observable({
		searchMngAreaSeq:"",
		searchMarketName:""
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

	//Create
	self.onCreateClick = function() {
		self.detailsVM.startCreate({
			marketSeq:"",// 고유번호
			mngAreaSeq:mngAreaSeq,//관제지역명
			marketName:"",//관리지역명
			managerName:"",//담당자
			phoneNo:"",//휴대폰번호
			telephoneNo:"",//일반전화번호
			zipCode:"",//우편번호
			roadAddress:"",//도로명주소
			parcelAddress:"",//지번주소
			latitude:"",//위도
			longitude:"",//경도
			scale:"",//축적
			firestationName:"",
			firestationManagerName:"",
			firestationTelephoneNo:"",
			ctrdCode:"",
			signguCode:"",
			dongCode:"",
			liCode:"",
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
					url: $.format("{0}/{1}.json", self.detailsURL, data.marketSeq),
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
		{ field: "mngAreaSeq", title: "관제지역명", width: 150, ezAlign: 'left', template: self.gridColumnTemplate.getTextFromCodeMap('mngAreaSeq',pageParam.MNG_AREA_CODE_MAP)},
		{ field: "marketName", title: "관리지역명", width: 150, ezAlign: 'left'},
		{ field: "managerName", title: "담당자", width: 150, ezAlign: 'left'},
		{ field: "phoneNo", title: "휴대폰번호", width: 150, ezAlign: 'left'},
		{ field: "telephoneNo", title: "일반전화번호", width: 150, ezAlign: 'left'},
		{ field: "regDate", title: "등록일", width: 90, ezAlign: 'center', template: self.gridColumnTemplate.momentISOFormatter('regDate') }
	];

	// Default grid height:670
	self.customGridOption = {height: 340};

	self.grid = new AbleKendoGridVM("#grid", self.listURL, self.gridColumns, self.onGridEvent, self.applySearchCondition, self.customGridOption);

	//Excel Download
	self.isMergeExcelCell = ko.observable(false);
	self.excelDownload = function(){
		var url = self.baseURL +"/excel?";
		var data  = _.extend(self.searchCondition, {mergeExcelCell: self.isMergeExcelCell()});
		var params = $.param(data);
		document.location.href = url + params;
	}

	self.detailsVM = new DataDetailsVM( self.detailsURL, self.grid.load);


	self.init = function() {
		self.searchData().searchMngAreaSeq = mngAreaSeq;
		self.search();
		console.log(self.clazz + " Initialized.");
	};

	self.commonPopupEvent = new CommonPopupEvent();

	self.clickSearchAddress = function() {
		new daum.Postcode(self.commonPopupEvent.searchAddrEvent.openFn).open();
	}

	//좌표 선택
	var originalLocationModal = $('#selectLocationModal').clone();
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
			mngAreaSeq:{required:true},
			marketName:{required:true},
			managerName:{required:true},
			phoneNo:{required:true,maxlength:20},
			zipCode:{number:true,maxlength:5,required:true},
			latitude:{required:true},
			longitude:{required:true},
			parcelAddress:{required:true},
			roadAddress:{required:true},
			scale:{number:true,maxlength:2,required:true}
		}
	};

	self.validationRules.insert = _.extend(_.clone(self.validationRules.common), {
	});

	self.validationRules.update = _.extend(_.clone(self.validationRules.common), {
	});

	self.customValidation = function(){
		var errorMsg = "";
		if( !ezUtil.isMobilePhoneCheck(self.data().phoneNo()) ){
			errorMsg = '휴대폰번호가 유효하지 않습니다.\n';
		}
		if ( _.isBlank(self.data().zipCode())){
			errorMsg+='우편번호는 필수 입력 사항입니다.\n';
		}
		if(_.isBlank(self.data().roadAddress())){
			errorMsg+='도로명주소 필수 입력 사항입니다.\n';
		}
		if(_.isBlank(self.data().parcelAddress())){
			errorMsg+='지번주소 필수 입력 사항입니다.\n';
		}
		if(_.isBlank(self.data().latitude())){
			errorMsg+='위도는  필수 입력 사항입니다.\n';
		}
		if(_.isBlank(self.data().longitude())){
			errorMsg+='경도는  필수 입력 사항입니다.\n';
		}
		if(_.isBlank(self.data().ctrdCode())){
			errorMsg+='지번주소코드의 시도코드는  필수 입력 사항입니다.\n';
		}
		if(_.isBlank(self.data().signguCode())){
			errorMsg+='지번주소코드의 시군구코드는  필수 입력 사항입니다.\n';
		}
		if(_.isBlank(self.data().dongCode())){
			errorMsg+='지번주소코드의 읍면동코드는  필수 입력 사항입니다.\n';
		}
		if(_.isBlank(self.data().liCode())){
			errorMsg+='지번주소코드의 리코드는  필수 입력 사항입니다.\n';
		}
		if(!errorMsg){
			return true;
		}else{
			alert(errorMsg);
			return false;
		}
	};

	//Details original data
	self.originalData = ko.observable(null);
	//Details copy data
	self.data = ko.observable(null);

	var setData = function(data) {
		//  ezUtil.dataTypeToString - Object value 값을  스트링 값으로 변환
		var dataCloned = ezUtil.dataTypeToString(data);
		if ( dataCloned != null ){
			dataCloned.mngAreaSeq = ko.observable(dataCloned.mngAreaSeq);
			dataCloned.telephoneNo = ko.observable(dataCloned.telephoneNo);
			dataCloned.phoneNo = ko.observable(dataCloned.phoneNo);
			dataCloned.firestationTelephoneNo = ko.observable(dataCloned.firestationTelephoneNo);

			dataCloned.zipCode = ko.observable(dataCloned.zipCode);
			dataCloned.roadAddress = ko.observable(dataCloned.roadAddress);
			dataCloned.parcelAddress = ko.observable(dataCloned.parcelAddress);
			dataCloned.latitude = ko.observable(dataCloned.latitude);
			dataCloned.longitude = ko.observable(dataCloned.longitude);

			dataCloned.ctrdCode = ko.observable(dataCloned.ctrdCode);
			dataCloned.signguCode = ko.observable(dataCloned.signguCode);
			dataCloned.dongCode = ko.observable(dataCloned.dongCode);
			dataCloned.liCode = ko.observable(dataCloned.liCode);
		}
		self.originalData(data);
		self.data(dataCloned);
	}

	var pkColumn = "marketSeq";
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
		if( self.validate(rule, self.formId)  && self.customValidation() && confirm('추가하시겠습니까?')) {
			var dataCloned = ko.toJS(self.data());
			// 등록
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

	//연기이벤트 전송제외/복구 저장.
	self.smokeAlarm = function(alarmYN) {
		var confirmMsg = "";
		var resultMsg = "";

		if (alarmYN == "On") {
			confirmMsg = "전통시장 내 모든 점포의 연기화재 알림을 전송 하시겠습니까?";
			resultMsg = "전통시장 내 모든 점포의 연기화재 알림을 전송으로 설정 하였습니다.";
		} else {
			confirmMsg = "전통시장 내 모든 점포의 연기화재 알림을 제외 하시겠습니까?";
			resultMsg = "전통시장 내 모든 점포의 연기화재 알림을 제외로 설정 하였습니다.";
		}

		if(confirm(confirmMsg)) {
			$.ajax({
				url: $.format(self.detailsURL+"/{0}/{1}/{2}", "smokealarm", self.originalData()[pkColumn], alarmYN),
				type: "POST",
				contentType: "application/json",
				data: '',
				success: function(data) {
					if(ezUtil.checkRESTResponseMsg(data)) {
						_.defer(function() {
							alert(resultMsg);
						})
					}
				}
			});
		}
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
			rootVM.detailsVM.data().latitude(data.Ga);
			rootVM.detailsVM.data().longitude(data.Ha);
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
                rootVM.commonPopupEvent.searchAddrEvent.successCallBack({zip: zip, roadAddr: roadAddr, jibunAddr: jibunAddr, Ga:'', Ha:''});
                //좌표선택모달 오픈
                rootVM.showSelectLocationModal();
            }
        }
	};
	self.selectLocationEvent = {
		successCallback: function(obj){
			if ( !_.isNull(rootVM.detailsVM) && _.isObject(rootVM.detailsVM.data()) ){
				var detailsData = rootVM.detailsVM.data();
				detailsData['latitude'](obj.la);
				detailsData['longitude'](obj.lo);
			}
			rootVM.selectLocationPopupVM.close();
		}
	};
}

$(document).ready(function() {
	//공통 초기화
	ezUtil.ezInitialize(false/*blockUsingPageAjaxIndicator*/);
	rootVM = new MarketMngVM();
	rootVM.init();
	ko.applyBindings(rootVM);
});