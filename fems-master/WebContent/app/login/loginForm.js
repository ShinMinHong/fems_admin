var rootVM = null;

function LoginViewModel() {
	var self = this;
	self.clazz = "LoginViewModel";

	//Extend VM
	vmExtender.extendGridVM(self);
	
	self.form = $("#loginForm");

	self.userName = ko.observable();
	self.userNameFocus = ko.observable(true);

	self.userPass= ko.observable();
	self.userPassFocus = ko.observable(false);

	self.doLogin = function() {
		if ( _.isEmpty(self.userName()) ){
			alert("아이디를 입력해주세요.");
			self.userNameFocus(true);
			return false;
		}
		if ( _.isEmpty(self.userPass()) ){
			alert("패스워드를 입력해주세요.");
			self.userPassFocus(true);
			return false;
		}
		self.form.submit();
	};

	//비밀번호 변경
	self.changePasswordPopupVM = new ChangePasswordPopupVM("#changePasswordPopup");
	self.changePassword = function(tryUserName) {
		self.changePasswordPopupVM.open(tryUserName);
	};

	self.init = function() {
		console.log("knockout binding");
		setTimeout("rootVM.showAdditionalMessage()", 500);
	};
	
	self.showAdditionalMessage = function() {
		var isModalMessage = false;
		if(!_.isBlank(loginFailType)){
			if(loginFailType === 'InitPasswordException' || loginFailType === 'CredentialsExpiredException'){
				isModalMessage = true;
				self.changePassword(tryUserName);
			}
		}
		if ( !_.isEmpty(errorMsg) && !isModalMessage ){
			alert(errorMsg);
		}
	}
}

function ChangePasswordPopupVM(elementId) {

	var self = this;
	self.clazz = "ChangePasswordPopupVM";

	self.popupId = elementId;

	self.formId = $("#changePasswordForm");

	// Validatable VM 확장
	vmExtender.extendValidatableVM(self);

	self.isVisible = ko.observable(false);
	self.loginId = ko.observable("");

	self.originalPassword = ko.observable("");
	self.newPassword = ko.observable("");
	self.newPasswordRe = ko.observable("");

	self.close = function() {
		self.isVisible(false);
	};

	self.open = function(tryUserName) {
		if(!_.isBlank(tryUserName)){
			self.loginId(tryUserName);
		}
		self.originalPassword("");
		self.newPassword("");
		self.newPasswordRe("");
		self.isVisible(true);
	};

	self.onPwdChange = function(){
		if ( !ezUtil.isPasswordCheck(self.newPassword())){
			alert('비밀번호는 영문, 숫자, 특수문자를 조합하여 8~20자리를 사용해야합니다.');
			self.newPassword("");
			$('#newPassword').focus();
		} else if ( !_.isBlank(self.newPasswordRe()) && self.newPassword() != self.newPasswordRe() ){
			alert('비밀번호가 일치하지 않습니다. 다시 시도해 주세요.');
			self.newPasswordRe("");
			$('#newPasswordRe').focus();
		}
	}

	self.doChangePassword = function() {
		if ( s.isBlank(self.originalPassword()) ){
			alert("기존 비밀번호를 입력해주세요.");
			$('#originalPassword').focus();
			return false;
		}
		if ( s.isBlank(self.newPassword()) ){
			alert("신규 비밀번호를 입력해주세요.");
			$('#newPassword').focus();
			return false;
		}
		if ( s.isBlank(self.newPasswordRe()) ){
			alert("비밀번호 확인을 입력해주세요.");
			$('#newPasswordRe').focus();
			return false;
		}
		if ( self.newPassword() !== self.newPasswordRe() ){
			alert("비밀번호와 비밀번호 확인 값이 일치하지 않습니다.");
			return false;
		}
		var dataCloned = ko.toJS(_.pick(rootVM.changePasswordPopupVM, ['loginId', 'originalPassword', 'newPassword']));
		$.ajax({
			url: $.format(contextPath+"/{0}", "home/api/changepassword"),
			type: "POST",
			contentType: "application/json",
			data: dataCloned,
			success: function(data) {
				if(ezUtil.checkRESTResponseMsg(data)) {
					alert('비밀번호가 변경되었습니다.\n 다시 로그인 해주세요.');
					rootVM.changePasswordPopupVM.close()
				}
			}
		})
	};
}

$(document).ready(function() {
	ezUtil.ezInitialize(true/*blockUsingPageAjaxIndicator*/);

	rootVM = new LoginViewModel();
	rootVM.init();

	ko.applyBindings(rootVM);
});


