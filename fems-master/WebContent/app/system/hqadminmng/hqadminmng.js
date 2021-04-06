var rootVM = null;

// 관리자 관리
function AdminMngVM() {

	var self = this;
	self.clazz = "AdminMngVM";

	//Extend VM
	vmExtender.extendGridVM(self);

	//Base URL
	self.baseURL = contextPath + "/system/hqadminmng";
	//List API URL
	self.listURL = self.baseURL +"/api/page.json";
	//Details REST API URL
	self.detailsURL = self.baseURL +"/api/details";

	//Search VM
	self.searchData = {
		searchSmsReceiveYn:"", // SMS 수신여부
		searchUseYn: "", // 사용여부
		searchAdminId: "", // 아이디
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
			adminSeq:"",
			rolegroupCode: "",
			adminId:"",
			adminPassword:"",
			confirmPassword:"",
			adminName:"",
			dutyName:"",
			phoneNo:"",
			smsReceiveYn:"",
			useYn:"",
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
					url: $.format("{0}/{1}.json", self.detailsURL, data.adminSeq),
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
		{ title: "관리자구분", width: 150, ezAlign: 'left', template: self.gridColumnTemplate.getTextFromCodeMap('rolegroupCode',pageParam.USER_GRADE_CODE_MAP)},
		{ field: "adminId", title: "아이디", width: 150, ezAlign: 'left'},
		{ field: "adminName", title: "이름", width: 150, ezAlign: 'left'},
		{ field: "dutyName", title: "직책", width: 150, ezAlign: 'left'},
		{ field: "useYn", title: "사용여부", width:70, ezAlign: 'center', template: self.gridColumnTemplate.TFCodeMap('useYn','사용','미사용')  },
		{ field: "lastLoginDate", title: "최종로그인", width: 100, ezAlign: 'center' , template: self.gridColumnTemplate.momentISOFormatter('lastLoginDate') },
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
			adminId:{required: true}, // 사용자 아이디
			adminPassword: { required: true, maxbyte:45, minlength:4}, // 비밀번호
			confirmPassword: { required: true, maxbyte:45, minlength:4, equalTo: "#adminPassword" }, // 비밀번호 확인
			adminName:{required: true}, // 아이디 이름
			phoneNo:{required: true}, // 휴대폰 번호
		}
	};

	self.validationRules.insert = _.extend(_.clone(self.validationRules.common), {
	});

	self.validationRules.update = _.extend(_.clone(self.validationRules.common), {

	});

	// 관제지역 선택 , 관리자 구분 값에 따라 validation 변경(지역명,시장명)
	self.validationRules.customizedValidation = {};

	// 아이디 중복 체크
	self.validationRules.checkDuplicateId = {
			adminId: {required: true, maxbyte:45, normalizeString:true, minlength:4}
	};

	//Details original data
	self.originalData = ko.observable(null);
	//Details copy data
	self.data = ko.observable(null);

	self.isRequiredForMarket = ko.observable("");
	self.isRequiredForArea = ko.observable("");

	var setData = function(data) {
		//  ezUtil.dataTypeToString - Object value 값을  스트링 값으로 변환
		var dataCloned = ezUtil.dataTypeToString(data);

		if ( dataCloned != null ){
			dataCloned.phoneNo = ko.observable(dataCloned.phoneNo);
			dataCloned.useYn = ko.observable(dataCloned.useYn);
			dataCloned.adminPassword = ko.observable(dataCloned.adminPassword);
			dataCloned.confirmPassword = ko.observable('');
			dataCloned.rolegroupCode = ko.observable('HQ_ADMIN');
			dataCloned.checkChangePassword = ko.observable(false);

		    // 비밀번호 변경여부 변경시
			dataCloned.checkChangePassword.subscribe(function(newValue){
				// 초기화(비밀번호 오류 msg,비밀번호 초기화)
					self.wrongPwdReason('');
					dataCloned.adminPassword('');
					dataCloned.confirmPassword('');
			});

			// 핸드폰 번호 입력시 핸드폰번호 포멧변경(ex.010xxxxxxxx->010-xxxx-xxxx)
			dataCloned.phoneNo.subscribe(function(newValue){
				var formattedResult = ezUtil.inputPhoneNumberByValue(newValue);
				dataCloned.phoneNo(formattedResult);
			})

		}
		self.originalData(data);
		self.data(dataCloned);
	}

	var pkColumn = "adminSeq";
	self.isEditMode = ko.computed(function() { return !_.isNull(self.originalData()) && !_.isBlank(self.originalData()[pkColumn]); });
	self.isCreateMode = ko.computed(function() { return !_.isNull(self.originalData()) && _.isBlank(self.originalData()[pkColumn]); });
	self.isVisible = ko.computed(function() { return self.isEditMode() || self.isCreateMode() });
	// 시장 enable status
	self.enableMarketLabel =  ko.observable();

	//수정시작
	self.startEdit = function(details) {
		console.log(self.clazz + ".startEdit => ", details);
		self.wrongPwdReason('');
		self.completeCheckId(true);
		setData(details);
		$(window).scrollTo(self.formId, 300);
	};

	//생성시작
	self.startCreate = function(details) {
		console.log(self.clazz + ".startCreate => ", details);
		if ( _.isFunction(self.completeCheckId) ){
			self.completeCheckId(false);
		}
		self.wrongPwdReason('');
		setData(details);
		$(window).scrollTo(self.formId, 300);
	};

	//취소
	self.cancel = function() {
		setData(null);
	};

	// 등록
	self.insertData = function() {
		var rule = _.extend(self.validationRules.insert, self.validationRules.customizedValidation);
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

	self.resetPswd = function() {
		if(confirm('해당 사용자의 비밀번호를 초기화 하시겠습니까?')) {
			$.ajax({
				url: $.format(self.detailsURL+"/{0}/{1}", "resetPswd", self.originalData()[pkColumn]),
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

	//update
	self.updateData = function() {
		var rule = _.extend(self.validationRules.update, self.validationRules.customizedValidation);
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

	// 비밀번호 에러 메세지
	self.wrongPwdReason = ko.observable('');
	// 비밀번호 에러 상태 flag
	self.wrongPwd = ko.computed(function() { return !_.isBlank(self.wrongPwdReason()) });
	//비밀번호 변경 완료시 비밀번호 검증
	self.onPwdChange = function(){
		if ( !ezUtil.isPasswordCheck(self.data().adminPassword())){
			self.wrongPwdReason('비밀번호는 영문, 숫자, 특수문자를 조합하여 8~20자리를 사용해야합니다.');
		} else if ( !_.isBlank(self.data().confirmPassword()) && self.data().adminPassword() != self.data().confirmPassword() ){
			self.wrongPwdReason('비밀번호가 일치하지 않습니다. 다시 시도해 주세요.');
		} else {
			self.wrongPwdReason('');
		}
	};

	self.customValidation = function(){
		if(!self.completeCheckId()){
			alert('아이디 중복체크를 해주세요.');
			return false;
		}

		if( !ezUtil.isMobilePhoneCheck(self.data().phoneNo()) ){
			alert('연락처 항목의 휴대폰번호가 유효하지 않습니다.');
			return false;
		}
		if ( self.wrongPwd() ){ // 비밀번호 검증이 안되었을시
			alert('비밀번호가 잘못되었습니다.');
			$("#adminPassword").focus();
			return false;
		}
		return true;
	};

	// 아이디 중복 체크  flag
	self.completeCheckId = ko.observable(false);

	// 아이디 중복 체크
	self.checkDuplicateId = function(){
		var rule = self.validationRules.checkDuplicateId;
		if( self.validate(rule, self.formId) ) {
			var dataCloned = {userId: self.data().adminId};
			$.ajax({
				url: $.format(self.detailsURL+"/{0}/{1}", "checkDuplicateId", self.data().adminId),
				type: "POST",
				contentType: "application/json",
				data: null,
				success: function(data) {
					if(ezUtil.checkRESTResponseMsg(data)) {
						alert('사용가능한 아이디입니다.');
						self.completeCheckId(true);
					} else {
						// 에러 메세지 처리는 서버에서
						self.completeCheckId(false);
					}
				}
			});
		}
	}
}

$(document).ready(function() {
	//공통 초기화
	ezUtil.ezInitialize(false/*blockUsingPageAjaxIndicator*/);
	rootVM = new AdminMngVM();
	rootVM.init();
	ko.applyBindings(rootVM);
});
