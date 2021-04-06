var rootVM = null;

function MngAreaMngVM() {

	var self = this;
	self.clazz = "MngAreaMngVM";

	//Extend VM
	vmExtender.extendGridVM(self);

	//List API URL
	self.listURL = contextPath + "/system/mngareamng/api/page.json";
	//Details REST API URL
	self.detailsURL = contextPath + "/system/mngareamng/api/details";
	//HQ어드민에 관할구역 설정
	self.changeAreaURL = contextPath + "/system/mngareamng/api/setmngarea";

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

	//Create
	self.onCreateClick = function() {
		self.detailsVM.startCreate({
			mngAreaSeq: null
//			, mngAreaName: '동대문'
//			, managerName: '동대문책임자'
//			, telephoneNo: '010-1122-1122'
//			, phoneNo: ''
//			, noAlarmTime: 60
//			, zipCode: ''
//			, roadAddress: ''
//			, parcelAddress: ''
//			, latitude: ''
//			, longitude: ''
//			, scale: 7
//			, alarmStore: true
//			, alarmMarket: false
//			, alarmArea: false
//			, regDate: '' //등록일
//			, regAdminId: '' //등록자
//			, updDate: '' //최종수정일
//			, updAdminId: '' //최종수정자
			, mngAreaName: ''
			, managerName: ''
			, phoneNo: ''
			, telephoneNo: ''
			, noAlarmTime: 60
			, zipCode: ''
			, roadAddress: ''
			, parcelAddress: ''
			, latitude: ''
			, longitude: ''
			, scale: 6
			, alarmStore: true
			, alarmMarket: false
			, alarmArea: false
			, regDate: '' //등록일
			, regAdminId: '' //등록자
			, updDate: '' //최종수정일
			, updAdminId: '' //최종수정자
		});
	};

	//Grid Event Listeners
	self.onGridEvent = function(type, data) {
		switch(type) {
			case "rowSelected" :
				$.ajax({
					url: $.format("{0}/{1}.json", self.detailsURL, data.mngAreaSeq),
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

	self.gridSelectColumn = function(dataItem){
		var mngAreaSeq = dataItem['mngAreaSeq'];
		return '<button type="button" onclick="rootVM[\'selectMngArea\'](\''+mngAreaSeq+'\');" class="btn btn-primary">관제시작</button>';
		//return '<a href="javascript:;" onclick="rootVM[\'selectMngArea\'](\''+mngAreaSeq+'\');"><i class="icon-plus22"></i>[선택]</a>';
	};

	self.gridColumns = [
		{ field: "mngAreaSeq", title: "관제지역", width: 100, ezAlign: 'center', template: self.gridSelectColumn },
		{ field: "mngAreaName", title: "관제지역명", minWidth: 200, ezAlign: 'center' },
		{ field: "managerName", title: "책임자", width:200, ezAlign: 'center'},
		{ field: "phoneNo", title: "휴대폰번호", width:200, ezAlign: 'center'},
		{ field: "telephoneNo", title: "일반전화번호", width:200, ezAlign: 'center'},
	];

	//Grid VM
	self.grid = new AbleKendoGridVM("#grid", self.listURL, self.gridColumns, self.onGridEvent, self.applySearchCondition);

	//Details VM
	self.detailsVM = new DataDetailsVM(self.detailsURL, self.grid.load);

	self.init = function() {
		self.grid.load();
		self.detailsVM.init();
		console.log(self.clazz + " Initialized.");
	};

	self.selectMngArea = function(mngAreaSeq){
		$.ajax({
			url: $.format(self.changeAreaURL+"/{0}", mngAreaSeq),
			type: "GET",
			contentType: "application/json",
			data: null,
			success: function(data) {
				document.location = "/";
			}
		});
	}


	//기타 설정===========================================================
	//주소 검색
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
				mngAreaName: { required: true, notBlank: true, maxlength:50 },
				managerName: { required: true, notBlank: true, maxlength:50 },
				phoneNo: { required: true, notBlank: true, maxlength:20 },
				telephoneNo: { maxlength:20 },
				noAlarmTime: { required: true, notBlank: true, maxlength:20 },
				scale: { required: true, notBlank: true, maxlength:20 }
			}
		};

	self.validationRules.insert = _.extend(_.clone(self.validationRules.common), {
	});
	self.validationRules.update = _.extend(_.clone(self.validationRules.common), {
	});

	//Details 원본 Data
	self.originalData = ko.observable(null);
	//Details Data
	self.data = ko.observable(null);

	var setData = function(data) {
		var dataCloned = _.clone(data);

		if ( dataCloned != null ){
//			dataCloned.mngAreaSeq = ko.observable(dataCloned.mngAreaSeq);
//			dataCloned.mngAreaName = ko.observable(dataCloned.mngAreaName);
//			dataCloned.managerName = ko.observable(dataCloned.managerName);
			dataCloned.phoneNo = ko.observable(dataCloned.phoneNo);
			dataCloned.telephoneNo = ko.observable(dataCloned.telephoneNo);
//			dataCloned.noAlarmTime = ko.observable(dataCloned.noAlarmTime);
			dataCloned.zipCode = ko.observable(dataCloned.zipCode);
			dataCloned.roadAddress = ko.observable(dataCloned.roadAddress);
			dataCloned.parcelAddress = ko.observable(dataCloned.parcelAddress);
			dataCloned.latitude = ko.observable(dataCloned.latitude);
			dataCloned.longitude = ko.observable(dataCloned.longitude);
//			dataCloned.scale = ko.observable(dataCloned.scale);
//			dataCloned.alarmStore = ko.observable(dataCloned.alarmStore);
//			dataCloned.alarmMarket = ko.observable(dataCloned.alarmMarket);
//			dataCloned.alarmArea = ko.observable(dataCloned.alarmArea);
//			dataCloned.regDate = ko.observable(dataCloned.regDate);
//			dataCloned.regAdminId = ko.observable(dataCloned.regAdminId);
//			dataCloned.updDate = ko.observable(dataCloned.updDate);
//			dataCloned.updAdminId = ko.observable(dataCloned.updAdminId);

			if ( dataCloned != null ){
				_.defer(function(){
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
				});
			}
		}
		self.originalData(dataCloned);
		self.data(dataCloned);
	};

	//편집모드 상태값
	var pkColumn = "mngAreaSeq";
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

	self.customValidation = function(){
		if ( _.isBlank(self.data().zipCode()) ){
			alert('주소검색을 이용해서 주소를 입력해 주세요.');
			return false;
		}
		if ( _.isBlank(self.data().latitude()) ){
			alert('위도를 입력해 주세요.');
			return false;
		}
		if ( _.isBlank(self.data().longitude()) ){
			alert('경도를 입력해 주세요.');
			return false;
		}

		if ( !_.isEmpty(self.data().phoneNo()) && !(ezUtil.isPhoneCheck(self.data().phoneNo()) || ezUtil.isMobilePhoneCheck(self.data().phoneNo()) )){
			alert('알맞은 휴대폰번호를 입력해 주세요');
			return false;
		}

		if ( !_.isEmpty(self.data().telephoneNo()) && !(ezUtil.isPhoneCheck(self.data().telephoneNo()) || ezUtil.isMobilePhoneCheck(self.data().telephoneNo()) )){
			alert('알맞은 일반전화번호를 입력해 주세요');
			return false;
		}
		
		return true;
	};

	//취소
	self.cancel = function() {
		setData(null);
	};

	//insert
	self.insertData = function() {
		console.log(self.clazz + ".insertData => ", self.data());
		var rule = self.validationRules.insert;
		if( self.validate(rule, self.formId) && self.customValidation() ) {
			if( self.isCreateMode() && confirm('추가하시겠습니까?')) {
				var dataCloned = _.omit(ko.toJS(rootVM.detailsVM.data()), 'regDate', 'regAdminId', 'updDate', 'updAdminId');
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
		}
	};

	//update
	self.updateData = function() {
		console.log(self.clazz + ".updateData => ", self.data());
		var rule = self.validationRules.update;
		if( self.validate(rule, self.formId) && self.customValidation() ) {
			if( self.isEditMode() && confirm('수정하시겠습니까?')) {
				var originalData = self.originalData();
				var dataCloned = _.omit(ko.toJS(rootVM.detailsVM.data()), 'regDate', 'regAdminId', 'updDate', 'updAdminId');
				$.ajax({
					url: $.format(self.detailsURL+"/{0}/{1}", "update", originalData.roleCode),
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

	//삭제
	self.deleteData = function() {
		console.log(self.clazz + ".deleteData => ", self.originalData());
		if(confirm('삭제하시겠습니까?')) {
			var originalData = self.originalData();
			$.ajax({
				url: $.format(self.detailsURL+"/{0}/{1}", "delete", originalData.mngAreaSeq),
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
                rootVM.commonPopupEvent.searchAddrEvent.successCallBack({zip: zip, roadAddr: roadAddr, jibunAddr: jibunAddr, Ga:'', Ha:''});
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
	}
}

$(document).ready(function() {

	ezUtil.ezInitialize(false/*blockUsingPageAjaxIndicator*/);

	rootVM = new MngAreaMngVM();
	rootVM.init();

	ko.applyBindings(rootVM);
});