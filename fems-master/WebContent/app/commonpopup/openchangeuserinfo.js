var rootVM = null;

function RootVM() {

	var self = this;
	self.clazz = "RootVM";
	self.formId = "#detailsForm";
	
	//Extend VM
	//vmExtender.extendGridVM(self);
	
	//Extend VM
	vmExtender.extendValidatableVM(self);
	
	//Details REST API URL
	self.userDetailsURL = contextPath + "/home/api/getuserinfo";
	self.changeUserInfoURL =  contextPath + "/home/api/changeuserinfo/update"
 
	self.originalData = ko.observable(null);
	self.data = ko.observable(null);
	
	// 비밀번호 변경여부
	self.changePassword = ko.observable(false);
	self.oldWrongPwdReason = ko.observable('');
	self.oldWrongPwd = ko.computed(function() { return !_.isBlank(self.oldWrongPwdReason()) });
	self.wrongPwdReason = ko.observable('');
	self.wrongPwd = ko.computed(function() { return !_.isBlank(self.wrongPwdReason()) });

	// 사용자 정보 가져오기
	self.getInitDetailsData = function(){
		$.ajax({
			url: $.format("{0}/{1}", self.userDetailsURL, pageParam.ADMIN_SEQ),
			type: "GET",
			contentType: "application/json",
			data: null,
			success: function(data) {
				if(ezUtil.checkRESTResponseMsg(data)) {
					self.startEdit(data.body);
				}
			}
		});
	};
	
	// Validation
	self.validationRules = {
		common: {
			oldPassword: { required: true, maxbyte:45, minlength:4}, // 기존 비밀번호
			adminPassword: { maxbyte:45, minlength:4}, // 신규 비밀번호
			confirmPassword: {required: true, maxbyte:45, minlength:4, equalTo: "#adminPassword" }, // 비밀번호 확인
			adminName:{required: true}, // 아이디 이름
			phoneNo:{required: true} // 휴대폰 번호
		}
	};
	
	self.validationRules.insert = _.extend(_.clone(self.validationRules.common), {
	});
	
	self.validationRules.update = _.extend(_.clone(self.validationRules.common), {	
	});	
	
	var setData = function(data) {
		var dataCloned = ezUtil.dataTypeToString(data);
		if ( dataCloned != null ){	
			// 권한
			dataCloned.appUserGradeSelectOptions =  _.filter(pageParam.APP_USER_GRADE,function(row){return row.value == pageParam.ROLEGROUP_CODE})[0].text;
			dataCloned.phoneNo = ko.observable(dataCloned.phoneNo);
			dataCloned.useYn = ko.observable(dataCloned.useYn);
			dataCloned.adminId = ko.observable(dataCloned.adminId);
			dataCloned.oldPassword = ko.observable('');
			dataCloned.adminPassword = ko.observable('');
			dataCloned.confirmPassword = ko.observable('');
			dataCloned.rolegroupCode = ko.observable(dataCloned.rolegroupCode);
			dataCloned.mngAreaName = ezUtil.findByValue(pageParam.MNG_AREA_CODE_MAP,dataCloned.mngAreaSeq);
			dataCloned.mngAreaSeq = ko.observable(dataCloned.mngAreaSeq);
			dataCloned.marketName=ezUtil.findByValue(pageParam.MARKET_CODE_MAP,dataCloned.marketSeq);
			dataCloned.marketSeq = ko.observable(dataCloned.marketSeq);
			dataCloned.checkChangePassword = ko.observable(false);
			dataCloned.phoneNo.subscribe(function(newValue){
				var formattedResult = ezUtil.inputPhoneNumberByValue(newValue);
				dataCloned.phoneNo(formattedResult);
			})
			
			dataCloned.checkChangePassword.subscribe(function(newValue){
				if(!newValue){
					self.oldWrongPwdReason('');
					self.wrongPwdReason('');
				}
			})
		}
		self.originalData(data);
		self.data(dataCloned);
	};

	self.onOldPwdChange = function(){
		if ( !ezUtil.isPasswordCheck(self.data().oldPassword())){
			self.oldWrongPwdReason('비밀번호는 영문, 숫자, 특수문자를 조합하여 8~20자리를 사용해야합니다.');
		} else {
			self.oldWrongPwdReason('');
		}
	}

	self.onPwdChange = function(){
		if ( !ezUtil.isPasswordCheck(self.data().adminPassword())){
			self.wrongPwdReason('비밀번호는 영문, 숫자, 특수문자를 조합하여 8~20자리를 사용해야합니다.');
		} else if ( !_.isBlank(self.data().confirmPassword()) && self.data().adminPassword() != self.data().confirmPassword() ){
			self.wrongPwdReason('비밀번호가 일치하지 않습니다. 다시 시도해 주세요.');
		} else {
			self.wrongPwdReason('');
		}
	}
	
	self.updateCustomValidation = function(){
		if( !ezUtil.isMobilePhoneCheck(self.data().phoneNo()) ){
			alert('연락처 항목의 휴대폰번호가 유효하지 않습니다.');
			return false;
		}
		if ( self.oldWrongPwd() ){
			alert('기존 비밀번호가 잘못되었습니다.');
			$("#oldPassword").focus();
			return false;
		}
		if ( self.wrongPwd() ){
			alert('신규 비밀번호가 잘못되었습니다.');
			$("#adminPassword").focus();
			return false;
		}
		return true;
	};
	
	//update
	self.updateData = function() {		
		console.log(self.clazz + ".updateData => ", self.data());
		var rule = self.validationRules.update;
		if( self.validate(rule, self.formId) && self.updateCustomValidation()) {
			var dataCloned = ko.toJS(self.data());
			$.ajax({
				url: $.format(self.changeUserInfoURL+"/{0}",self.originalData()['adminSeq']),
				type: "POST",
				contentType: "application/json",
				data: dataCloned,
				success: function(data) {
					if(ezUtil.checkRESTResponseMsg(data)) {
						alert("회원변경이 완료되었습니다.");	
						parent.changeUserInfoPopupVM.close();
					}
				}
			});
		}
	};
	
	//취소
	self.cancel = function() {
		setData(null);
		parent.changeUserInfoPopupVM.close();
	};
	
	self.startEdit = function(details) {
		console.log(self.clazz + ".startEdit => ", details);
		//초기화 ( 비밀번호 flag , 에러 msg )
		self.changePassword(false);
		self.oldWrongPwdReason('');
		self.wrongPwdReason('');
		setData(details);
		//$(window).scrollTo(self.formId, 300);
	};
	
	self.init = function() {
		self.getInitDetailsData();
		console.log(self.clazz + " Initialized.");
	};
}

$(document).ready(function() {
	ezUtil.ezInitialize(false/*blockUsingPageAjaxIndicator*/);

	rootVM = new RootVM();
	rootVM.init();

	ko.applyBindings(rootVM);
});
