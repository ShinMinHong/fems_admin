var rootVM = null;

function UserMgnVM() {

	var self = this;
	self.clazz = "UserMgnVM";

	//Extend VM
	vmExtender.extendGridVM(self);

	//List API URL
	self.listURL = contextPath + "/areasystem/usermng/api/page.json";
	//Details REST API URL
	self.detailsURL = contextPath + "/areasystem/usermng/api/details";

	self.isGradeHqadmin = ko.computed(function() { return _.contains(['GRADE_HQADMIN'], me.authorgroupCode); });

	self.fireStationCodeMap = ezUtil.convertSelectOptionsToObject(pageParam.FIRE_STATION_ALL_CODE_MAP);
	self.appUserGradeCodeMap = ezUtil.convertSelectOptionsToObject(pageParam.APP_USER_GRADE);

	self.appUserGradeSelectOptions = self.isGradeHqadmin() ? ezUtil.orderByAuthorGroupSelectOptions(pageParam.APP_USER_GRADE) : ezUtil.orderByAuthorGroupSelectOptions(_.filter(pageParam.APP_USER_GRADE, function(row) { return row.value != "GRADE_HQADMIN" && row.value != "GRADE_BRANCHADMIN"}));
	self.searchBranchRoleSelectOptions = ezUtil.orderByAuthorGroupSelectOptions(_.filter(pageParam.APP_USER_GRADE, function(row) { return row.value != "GRADE_HQADMIN"}));
	self.fireStationCodeMapSelectOptions = ko.observableArray([]);

	self.getSearchFrsttCode = self.isGradeHqadmin() ? "" : me.frsttCode;

	//Search VM
	self.searchData = ko.observable({
		searchUserId: "",
		searchUserNm: "",
		searchFrsttCode: self.getSearchFrsttCode,
		searchDeleteAt: "",
		searchAppUserRole: null,
		searchPswdExpired: ko.observable('N')
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

	//Create
	self.onCreateClick = function() {
		self.detailsVM.startCreate({
			authorgroupCode: "",
			frsttCode: self.getSearchFrsttCode,
			userId: "",
			userNm: "",
			n119cnterNm: "",
			ojtftNm: "",
			mbtlnum: "",
			rspofcNm: "",
			smsRcivrAt: true,
			deleteAt: false,
			loginPosblAt: true,
			lastLoginDt: "",
			lastPasswordChangeDt: "",
			registerId: "",
			registDt: "",
			updusrId: "",
			updtDt: ""
		});
	};

	//대상처 관리자 엑셀업로드
	self.openExcelUpload = function(){
		if (_.isBlank(self.keeperExcelUploadPopupSrc)){
			self.keeperExcelUploadPopupSrc = contextPath + "/commonpopup/openkeeperexcelupload";
			self.keeperExcelUploadPopupVM = new KeeperExcelUploadPopupVM("#keeperExcelUploadModal", self.keeperExcelUploadPopupSrc);
		}
		self.keeperExcelUploadPopupVM.open();
		if ( $('iframe[id=keeperExcelUploadModal]')[0].contentWindow.rootVM != null && $('iframe[id=keeperExcelUploadModal]')[0].contentWindow.rootVM.init != null ){
			$('iframe[id=keeperExcelUploadModal]')[0].contentWindow.rootVM.init();
		}
	}

	//Grid Event Listeners
	self.onGridEvent = function(type, data) {
		switch(type) {
			case "rowSelected" :
				$.ajax({
					url: $.format("{0}/{1}.json", self.detailsURL, data.userInnb),
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

	self.getFrsttCode = function(dataItem){
		if(!_.isEmpty(dataItem)){
			if(_.contains(["GRADE_HQADMIN","GRADE_BRANCHADMIN","GRADE_BRANCHUSER"], dataItem['authorgroupCode'])){
				return rootVM.fireStationCodeMap[dataItem['frsttCode']]
			} else {
				return dataItem.userNm;
			}
		}
	};

	self.getAppUserGrade = function(dataItem){
		return ezUtil.getSelectOptionsText(pageParam.APP_USER_GRADE, dataItem['authorgroupCode']);
	};

	//Data Table Columns Settings
	self.gridColumns = [
		{ field: "no", title: "No", width: 40, ezAlign: 'center'},
		{ field: "authorgroupCode", title: "사용자구분", width: 150, ezAlign: 'left', template: self.getAppUserGrade},
		{ field: "frsttCode", title: "소속", width: 150, ezAlign: 'left', template: self.getFrsttCode},
		{ field: "userId", title: "아이디", width: 150, ezAlign: 'left'},
		{ field: "userNm", title: "성명(대상물명)", width: 150, ezAlign: 'left'},
		{ field: "rspofcNm", title: "직책", width: 150, ezAlign: 'left'},
		{ field: "deleteAt", title: "삭제여부", width:70, ezAlign: 'center', template: self.gridColumnTemplate.codeMapFormatter('deleteAt', ezUtil.deleteCodeMap) },
		{ field: "lastLoginDt", title: "최종로그인", width: 100, ezAlign: 'center', template: self.gridColumnTemplate.momentFormatter('lastLoginDt') },
		{ field: "registDt", title: "등록일", width: 90, ezAlign: 'center', template: self.gridColumnTemplate.momentISOFormatter('registDt') }
	];

	self.grid = new AbleKendoGridVM("#grid", self.listURL, self.gridColumns, self.onGridEvent, self.applySearchCondition);

	self.detailsVM = new DataDetailsVM( self.detailsURL, self.grid.load);

	//Excel Download
	self.isMergeExcelCell = ko.observable(false);
	self.excelDownload = function(){
		var url = contextPath + "/system/usermng/excel?";
		var data  = _.extend(self.searchCondition, {mergeExcelCell: self.isMergeExcelCell()});
		var params = $.param(data);
		document.location.href = url + params;
	}

	self.commonPopupEvent = new CommonPopupEvent();

	self.init = function() {
		self.search();
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

	//Extend VM
	vmExtender.extendGridVM(self);

	//Form
	self.formId = "#detailsForm";

	self.validationRules = {
		common: {
			authorgroupCode: { required: true},
			frsttCode: { required: true},
			userId : { required: true, maxlength: 50},
			rspofcNm : { maxlength: 50},
			n119cnterNm : { maxlength: 50}
		}
	};

	self.validationRules.insert = _.extend(_.clone(self.validationRules.common), {
	});

	self.validationRules.update = _.extend(_.clone(self.validationRules.common), {
		userPassword: { required: true, maxlength: 20},
		confirmPassword: { required: true, maxlength: 20}
	});

	self.validationRules.checkNameForKeeper = {
		ojtftNm: { required: true, maxlength: 50}
	};
	self.validationRules.checkNameForAdmin = {
		userNm : { required: true, maxlength: 50}
	};
	self.validationRules.checkDuplicateIdForKeeper = {
		userId: {required:true, maxlength: 50, email:true}
	};
	self.validationRules.checkDuplicateIdForAdmin = {
		userId: {required:true, maxlength: 50}
	};

	//최근 접속이력 10건
	self.loginLogListGridColumns = [
		{ field: "no", title: "No", width: 40, ezAlign: 'center'},
		{ field: "loginDt", title: "로그인일시", width: 150, ezAlign: 'center', template: self.gridColumnTemplate.momentFormatter('loginDt')}
	];
	self.loginLogListGrid = null;

	//최근 비밀번호 변경이력 10건
	self.pwdChangeLogListGridColumns = [
		{ field: "no", title: "No", width: 40, ezAlign: 'center'},
		{ field: "passwordChangeDt", title: "비밀번호변경일", width: 150, ezAlign: 'center', template: self.gridColumnTemplate.momentFormatter('passwordChangeDt')}
	];
	self.pwdChangeLogListGrid = null;

	//Details 원본 Data
	self.originalData = ko.observable(null);
	//Details Data
	self.data = ko.observable(null);

	var setData = function(data) {
		var dataCloned = _.clone(data);
		if ( dataCloned != null ){
			dataCloned.loginPosblAt = dataCloned.loginPosblAt;
			dataCloned.deleteAt = dataCloned.deleteAt;
			dataCloned.deleteDt = ko.observable(dataCloned.deleteDt);
			dataCloned.smsRcivrAt = ko.observable(dataCloned.smsRcivrAt);
			dataCloned.authorgroupCode = ko.observable(dataCloned.authorgroupCode);
			dataCloned.frsttCode = ko.observable(dataCloned.frsttCode);
			dataCloned.mbtlnum = ko.observable(dataCloned.mbtlnum);
			dataCloned.n119cnterNm = ko.observable(dataCloned.n119cnterNm);

			dataCloned.userId = ko.observable(dataCloned.userId);
			dataCloned.userPassword = ko.observable(dataCloned['userPassword']);
			dataCloned.confirmPassword = ko.observable('');
			dataCloned.userNm = ko.observable(dataCloned.userNm);

			dataCloned.ojtftNm = ko.observable(dataCloned.userNm());
			dataCloned.ojtftSn = ko.observable(dataCloned.ojtftSn);

			dataCloned.authorgroupCode.subscribe(function(newValue){
				if(newValue == 'GRADE_KEEPERADMIN'){
					dataCloned.n119cnterNm('');
					rootVM.fireStationCodeMapSelectOptions(_.filter(pageParam.FIRE_STATION_ALL_CODE_MAP, function(row) { return row.value != 1100000}));
				} else if(newValue == 'GRADE_HQADMIN') {
					rootVM.fireStationCodeMapSelectOptions(_.find(pageParam.FIRE_STATION_ALL_CODE_MAP, function(row) { return row.value == 1100000}));
					dataCloned.userNm('');
					dataCloned.ojtftNm('');
					dataCloned.ojtftSn('');
				} else {
					rootVM.fireStationCodeMapSelectOptions(_.filter(pageParam.FIRE_STATION_ALL_CODE_MAP, function(row) { return row.value != 1100000}));
					dataCloned.userNm('');
					dataCloned.ojtftNm('');
					dataCloned.ojtftSn('');
				}
			});

			dataCloned.checkChangePassword = ko.observable('N');
			dataCloned.checkChangePassword.subscribe(function(newValue){
				var enable = ( newValue == 'Y' ) ? true : false;
				self.changePassword(enable);
				self.wrongPwdReason('');
				dataCloned.userPassword('');
				dataCloned.confirmPassword('');
			});
			dataCloned.mbtlnum.subscribe(function(newValue){
				var formattedResult = ezUtil.inputPhoneNumberByValue(newValue);
				dataCloned.mbtlnum(formattedResult);
			})

			self.userBlockYn(dataCloned.userBlockYn);
			if(!dataCloned.loginPosblAt){
				self.userBlockYn(false);
			}

			_.defer(function(){
				self.loginLogListGrid = new AbleKendoTableVM("#loginLogListGrid", self.loginLogListGridColumns, {height: 220});
				self.loginLogListGrid.setData(dataCloned? dataCloned.loginLogList : []);

				self.pwdChangeLogListGrid = new AbleKendoTableVM("#pwdChangeLogListGrid", self.pwdChangeLogListGridColumns, {height: 220});
				self.pwdChangeLogListGrid.setData(dataCloned? dataCloned.pwdChangeLogList : []);
			})

		}
		self.originalData(data);
		self.data(dataCloned);

	};

	//소방대상물 선택
	self.showSelectOjtftModal = function(){
		if (_.isBlank(self.selectOjtftPopupSrc)){
			self.selectOjtftPopupSrc = contextPath + "/commonpopup/openselectojtft";
			self.selectOjtftPopupVM = new SelectOjtftPopupVM("#selectOjtftModal", self.selectOjtftPopupSrc);
		}
		self.selectOjtftPopupVM.open();
		if ( $('iframe[id=selectOjtftModal]')[0].contentWindow.rootVM != null && $('iframe[id=selectOjtftModal]')[0].contentWindow.rootVM.init != null ){
			$('iframe[id=selectOjtftModal]')[0].contentWindow.rootVM.init();
		}
	};

	//편집모드 상태값
	var pkColumn = "userInnb";
	self.isEditMode = ko.computed(function() { return !_.isNull(self.originalData()) && !_.isBlank(self.originalData()[pkColumn]); });
	self.isCreateMode = ko.computed(function() { return !_.isNull(self.originalData()) && _.isBlank(self.originalData()[pkColumn]); });
	self.isVisible = ko.computed(function() { return self.isEditMode() || self.isCreateMode() });

	self.userBlockYn = ko.observable(false);
	self.isUserBlockMode = ko.computed(function() { return self.isEditMode() && self.userBlockYn() && !_.isNull(self.data()) && self.data().loginPosblAt });
	self.isUserAccessMode = ko.computed(function() { return self.isEditMode() && !_.isNull(self.data()) && !self.data().loginPosblAt && !self.data().deleteAt });

	self.isVisibleDeleteButton = ko.computed(function() { return self.isEditMode() && !_.isNull(self.data()) &&  !self.data().deleteAt });

	self.changePassword = ko.observable(false);
	self.enablePasswordPanel = ko.computed(function() { return self.isEditMode() && self.changePassword() });

	self.showAdminSelecter = ko.computed(function() { return !_.isNull(self.data()) && !_.isBlank(self.data()['authorgroupCode']) && _.contains(["GRADE_HQADMIN","GRADE_BRANCHADMIN","GRADE_BRANCHUSER","GRADE_IOTTRMNL"],self.data().authorgroupCode()) });
	self.showKeeperSelecter = ko.computed(function() { return !_.isNull(self.data()) && !_.isBlank(self.data()['authorgroupCode']) && _.contains(["GRADE_KEEPERADMIN"],self.data().authorgroupCode()) });

	//수정시작
	self.startEdit = function(details) {
		console.log(self.clazz + ".startEdit => ", details);
		if ( _.isFunction(self.wrongPwd) ){
			self.changePassword(false);
			self.wrongPwdReason('');
		}
		setData(details);
		$(window).scrollTo(self.formId, 300);
	};
	//생성시작
	self.startCreate = function(details) {
		console.log(self.clazz + ".startCreate => ", details);
		if ( _.isFunction(self.completeCheckId) ){
			self.completeCheckId(false);
		}
		setData(details);
		$(window).scrollTo(self.formId, 300);
	};
	//취소
	self.cancel = function() {
		setData(null);
	};

	self.completeCheckId = ko.observable(false);

	self.checkDuplicateId = function(){
		if(self.data().authorgroupCode() == "GRADE_KEEPERADMIN"){
			var rule = self.validationRules.checkDuplicateIdForKeeper;
		} else {
			var rule = self.validationRules.checkDuplicateIdForAdmin;
		}
		if( self.validate(rule, self.formId) ) {
			var dataCloned = {userId: self.data().userId()};
			$.ajax({
				url: $.format(self.detailsURL+"/{0}", "checkDuplicateId"),
				type: "POST",
				contentType: "application/json",
				data: dataCloned,
				success: function(data) {
					if(ezUtil.checkRESTResponseMsg(data)) {
						alert('사용가능한 아이디 입니다.');
						self.completeCheckId(true);
					} else {
						self.completeCheckId(false);
					}
				}
			});
		}
	}

	self.userAccessBlock = function(){
		if(confirm('요청하신 사용자가 접속차단 됩니다. 계속 하시겠습니까?')) {
			$.ajax({
				url: $.format(self.detailsURL+"/{0}/{1}", "userAccessBlock", self.data().userInnb),
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
	}

	self.userAccessPossible = function(){
		$.ajax({
			url: $.format(self.detailsURL+"/{0}/{1}", "userAccessPossible", self.data().userInnb),
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

	self.wrongPwdReason = ko.observable('');
	self.wrongPwd = ko.computed(function() { return !_.isBlank(self.wrongPwdReason()) });

	self.onPwdChange = function(){
		if ( !ezUtil.isPasswordCheck(self.data().userPassword())){
			self.wrongPwdReason('비밀번호는 영문, 숫자, 특수문자를 조합하여 8~20자리를 사용해야합니다.');
		} else if ( !_.isBlank(self.data().confirmPassword()) && self.data().userPassword() != self.data().confirmPassword() ){
			self.wrongPwdReason('비밀번호가 일치하지 않습니다. 다시 시도해 주세요.');
		} else {
			self.wrongPwdReason('');
		}
	}

	//delete
	self.deleteData = function() {
		console.log(self.clazz + ".deleteData => ", self.originalData());
		if(confirm('삭제하시겠습니까?')) {
			$.ajax({
				url: $.format(self.detailsURL+"/{0}/{1}", "delete", self.originalData()['userInnb']),
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

	self.insertCustomValidation = function(){
		if ( !self.completeCheckId() ){
			alert('아이디 중복체크를 해주세요.');
			$("#checkDuplicateId").focus();
			return false;
		}
		if( !ezUtil.isMobilePhoneCheck(self.data().mbtlnum()) ){
			alert('연락처 항목의 휴대폰번호가 유효하지 않습니다.');
			return false;
		}
		if(self.data().authorgroupCode() == "GRADE_KEEPERADMIN"){
			if(_.isEmpty(self.data().ojtftNm())){
				alert('소방대상물명을 입력해 주세요.');
				return false;
			}
		}
		return true;
	};

	//insert
	self.insertData = function() {
		console.log(self.clazz + ".insertData => ", self.data());
		if(self.data().authorgroupCode() == "GRADE_KEEPERADMIN"){
			var rule = self.validationRules.insert = _.extend(_.clone(self.validationRules.checkNameForKeeper));
		} else {
			var rule = self.validationRules.insert = _.extend(_.clone(self.validationRules.checkNameForAdmin));
		}
		if( self.validate(rule, self.formId) && self.insertCustomValidation() ) {
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
					}
				}
			});
		}
	};

	self.updateCustomValidation = function(){
		if( !ezUtil.isMobilePhoneCheck(self.data().mbtlnum()) ){
			alert('연락처 항목의 휴대폰번호가 유효하지 않습니다.');
			return false;
		}
		if ( self.wrongPwd() ){
			alert('비밀번호가 잘못되었습니다.');
			$("#inputPassword").focus();
			return false;
		}
		return true;
	};

	//update
	self.updateData = function() {
		console.log(self.clazz + ".updateData => ", self.data());
		if(self.data().authorgroupCode() == "GRADE_KEEPERADMIN"){
			var rule = self.validationRules.update = _.extend(_.clone(self.validationRules.checkNameForKeeper));
		} else {
			var rule = self.validationRules.update = _.extend(_.clone(self.validationRules.checkNameForAdmin));
		}
		if( self.validate(rule, self.formId) && self.updateCustomValidation()) {
			var dataCloned = ko.toJS(self.data());
			$.ajax({
				url: $.format(self.detailsURL+"/{0}/{1}", "update", self.originalData()['userInnb']),
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

	//init
	self.init = function() {
		console.log(self.clazz + " Initialized.");
	};
};

function SelectOjtftPopupVM(elementId, url) {
	var self = this;
	self.clazz = "SelectOjtftPopupVM";

	self.popupId = elementId;
	self.iframeSrc = url;

	//Close Callback
	 self.close = function() {
		self.popupElement.dialog('close');
	};

	self.popupOptions = {
			minHeight: 780,
			height: 780
	}

	//Extend VM
	vmExtender.extendIframePopupVM(self);
}

function KeeperExcelUploadPopupVM(elementId, url) {
	var self = this;
	self.clazz = "KeeperExcelUploadPopupVM";

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
			]
	}

	//Extend VM
	vmExtender.extendIframePopupVM(self);
}

function CommonPopupEvent(){
	var self = this;

	self.selectOjtftEvent = {
			successCallback: function(obj){
				if ( !_.isNull(rootVM.detailsVM) && _.isObject(rootVM.detailsVM.data()) ){
					var detailsData = rootVM.detailsVM.data();
					detailsData['ojtftSn'](obj['ojtftSn']);
					detailsData['ojtftNm'](obj['ojtftNm']);
					detailsData['userNm'](obj['ojtftNm']);
				}
				rootVM.detailsVM.selectOjtftPopupVM.close();
			}
		}

}

$(document).ready(function() {

	//공통 초기화
	ezUtil.ezInitialize(false/*blockUsingPageAjaxIndicator*/);

	rootVM = new UserMgnVM();
	rootVM.init();

	ko.applyBindings(rootVM);

});
