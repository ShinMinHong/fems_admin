var rootVM = null;

// 화재감지기 관리 (검색 , 리스트)
function FireDetectorSetVM() {

	var self = this;
	self.clazz = "FireDetectorSetVM";

	//Extend VM
	vmExtender.extendGridVM(self);

	//Base URL
	self.baseURL = contextPath + "/areasystem/firedetectorset";
	//List API URL
	self.listURL = self.baseURL +"/api/page.json";
	//Details REST API URL
	self.detailsURL = self.baseURL +"/api/details";

	// 사용자 권한에 따른 관제지역 코드맵 (지역 관리자 , 마켓 관리자 일경우 해당 관리자의 지역 CODE_MAP 반환)
	self.mngAreaSelectOptions = pageParam.AREA_CODE_MAP;
	// 시장목록 코드맵
	self.marketSelectOptions = pageParam.MARKET_CODE_MAP;
	// 감지기 설정 상태 코드맵
	self.fireDetectorSetType = ezUtil.convertObjectToSelectOptions(pageParam.FIRE_DETECTOR_SET_TYPE);

	//Search VM
	self.searchData = ko.observable({
		searchMarketSeq:pageParam.ME_MARKET_SEQ,
		searchStoreName:"",
		searchCtnNo:"",
		searchStartDate:"",
		searchEndDate:"",
		searchFireDetectorSetType:"", // 감지기 설정상태
		searchSort:""
	});

	//Search Condition Applied
	self.searchCondition = {};

	//Apply Search Condition
	self.applySearchCondition = function(param) {
		return _.extend(param, self.searchCondition);
	};

	//Search
	self.search = function() {
		self.searchCondition = _.chain(ko.toJS(self.searchData()))
			.omit(_.isEmpty)
			.mapObject(function(val, key) { return _.isString(val)? encodeURIComponent(val) : val;})
			.value();
		self.grid.load();
	};

	//Create
	self.onCreateClick = function() {
		self.detailsVM.startCreate({
			fireDetectorSetSeq:"",			// 감지기설정고유번호
			fireDetectorSeq:"",				// 감지기고유번호
			mngAreaName:"", 				// 관제지역명
			marketName: "", 				// 관리지역명
			storeName:ko.observable(""), 	// 점포명 (화면에 따라 변환)
			ctnNo:"", 						// CTN NO
			fireDetectorSetType:"", 		// 감지기설정 구분
			fireDetectorSetValue:"", 		// 감지기설정 구분값
			fireDetectorSetStrDate:"", 		// 감지기설정 시작일시
			fireDetectorSetEndDate:"", 		// 감지기설정 종료일시
			fireDetectorSetsendYn:"", 		// 감지기설정 전송유무
			fireDetectorSetsendDate:"", 	// 감지기설정 전송일시
			regAdminId :"", 				// 등록자
			regDate:"", 					// 등록일
			updAdminId :"", 				// 수정자
			updDate:""	 					// 수정일
		});
	};

	// 팝업 창 Event Listener
	self.commonPopupEvent = new CommonPopupEvent();

	//Grid Event
	self.onGridEvent = function(type, data) {
		switch(type) {
			case "rowSelected" :
				$.ajax({
					url: $.format("{0}/{1}.json", self.detailsURL, data.fireDetectorSetSeq),
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
		{ field: "marketName", title: "관리지역명", width: 170, ezAlign: 'center'},
		{ field: "storeName", title: "점포명", width: 200, ezAlign: 'center'},
		{ field: "ctnNo", title: "CTN 번호", width: 120, ezAlign: 'center'},
		{ field: "fireDetectorSetType", title: "감지기 설정 구분", width: 120, ezAlign: 'center', template: self.gridColumnTemplate.getTextFromCodeMap('fireDetectorSetType',self.fireDetectorSetType)},
		{ field: "fireDetectorSetStrDate", title: "감지기 설정 시작일시", width: 150, ezAlign: 'center',template: self.gridColumnTemplate.momentFormatter('fireDetectorSetStrDate',self.fireDetectorSetStrDate)},
		{ field: "fireDetectorSetEndDate", title: "감지기 설정 종료일시", width: 150, ezAlign: 'center',template: self.gridColumnTemplate.momentFormatter('fireDetectorSetEndDate',self.fireDetectorSetEndDate)},
		{ field: "fireDetectorSetSendYn", title: "감지기 설정 전송 여부", width: 120, ezAlign: 'center', template: self.gridColumnTemplate.getTextFromCodeMap('fireDetectorSetSendYn',self.fireDetectorSetSendYn)},
		{ field: "fireDetectorSetSendDate", title: "감지기 설정 전송일시", width: 150, ezAlign: 'center',template: self.gridColumnTemplate.momentFormatter('fireDetectorSetSendDate',self.fireDetectorSetSendDate)},
		{ field: "lastUpdtDt", title: "최종수집일", width: 150, ezAlign: 'center',template: self.gridColumnTemplate.momentFormatter('lastUpdtDt') }
	];

	self.grid = new AbleKendoGridVM("#grid", self.listURL, self.gridColumns, self.onGridEvent, self.applySearchCondition);

	self.detailsVM = new DataDetailsVM( self.detailsURL, self.grid.load);

	self.init = function() {
		self.search();
		console.log(self.clazz + " Initialized.");

	};
}

var file_localFileInputIdx = 1;

function DataDetailsVM(detailsURL, onDetailsChanged) {

	var self = this;
	self.clazz = "DetailsVM";
	self.file_answerCnt = 0;

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
			ctnNo:{required:true},
			serialNo:{required:true},
			usimNo:{required:true},
		}
	};

	self.validationRules.insert = _.extend(_.clone(self.validationRules.common), {
	});

	self.validationRules.update = _.extend(_.clone(self.validationRules.common), {

	});

	//Details original data
	self.originalData = ko.observable(null);
	//Details copy data
	self.data = ko.observable(null);
	// 감지기 해제
	self.isDisconnected = ko.observable(false);

	var setData = function(data) {
		var dataCloned = ezUtil.dataTypeToString(data);
		if ( dataCloned != null ){
			if(self.isEditMode){
				self.isDisconnected((dataCloned.storeSeq==null))
			}
			dataCloned.fireDetectorSetType = ko.observable(dataCloned.fireDetectorSetType);
			// 관제지역명,관리지역명,점포명
			dataCloned.mngAreaName = ko.observable(dataCloned.mngAreaName);
			dataCloned.marketName = ko.observable(dataCloned.marketName);
			dataCloned.storeName = ko.observable(dataCloned.storeName);
			dataCloned.storeSeq = ko.observable(dataCloned.storeSeq);
			// 위치 관련(우편번호,위도,경도,(도로명/법정동) 주소
			dataCloned.zipCode = ko.observable(dataCloned.zipCode);
			dataCloned.longitude = ko.observable(dataCloned.longitude);
			dataCloned.latitude = ko.observable(dataCloned.latitude);
			dataCloned.parcelAddress = ko.observable(dataCloned.parcelAddress);
			dataCloned.roadAddress = ko.observable(dataCloned.roadAddress);
		}
		self.originalData(data);
		self.data(dataCloned);
	}

	self.getCtn = function(){
		return self.data().ctnNo;
	}

	var pkColumn = "fireDetectorSetSeq";
	self.isEditMode = ko.computed(function() { return !_.isNull(self.originalData()) && !_.isBlank(self.originalData()[pkColumn]); });
	self.isCreateMode = ko.computed(function() { return !_.isNull(self.originalData()) && _.isBlank(self.originalData()[pkColumn]); });
	self.isVisible = ko.computed(function() { return self.isEditMode() || self.isCreateMode() });

	// 점포 선택 창
	self.showSelectStoreModal = function(){
		if(_.isBlank(self.selectStorePopupSrc)){
			self.selectStorePopupSrc = contextPath + "/commonpopup/openselectstore";
			self.selectStorePopupVM = new SelectStorePopupVM("#selectStoreModal", self.selectStorePopupSrc);
		} else{
			self.selectStorePopupVM.initFromParent();
		}
		self.selectStorePopupVM.open();
	};

	//수정시작
	self.startEdit = function(details) {
		console.log(self.clazz + ".startEdit => ", details);
		setData(details);
		self.initServerPreCtls();
		self.initLocalFileInput();
		$(window).scrollTo(self.formId, 300);
	};

	//생성시작
	self.startCreate = function(details) {
		console.log(self.clazz + ".startCreate => ", details);
		setData(details);
		self.initLocalFileInput();
		$(window).scrollTo(self.formId, 300);
	};

	//취소
	self.cancel = function() {
		setData(null);
	};

	// 점포 선택에 따른 Validation
	self.customValidation = function(){
		var dataCloned = self.data()
		if(dataCloned.storeName()){ // 점포정보가 있을시 감지기 위치 정보 필수입력사항
			if( !dataCloned.zipCode()){
				alert('우편번호는 필수 입력 사항입니다.\n 주소검색을 버튼을 눌러 우편번호를 설정해주세요.');
				return false;
			}

			if( !dataCloned.roadAddress()){
				alert('도로명 주소는 필수 입력 사항입니다. \n 주소검색을 버튼을 눌러 도로명 주소를 입력해 주세요');
				return false;
			}

			if( !dataCloned.parcelAddress()){
				alert('주소(지번)는 필수 입력 사항입니다. \n 주소검색 버튼을 눌러 지번주소를 입력해 주세요');
				return false;
			}

			if( !dataCloned.latitude()){
				alert('위도(중심좌표)는 필수 입력 사항입니다. \n 주소검색 후 위도(중심좌표를) 설정해 주세요');
				return false;
			}

			if( !dataCloned.longitude()){
				alert('경도(중심좌표)는 필수 입력 사항입니다. \n 주소검색 후  위도(중심좌표)를 설정해 주세요');
				return false;
			}

			if( !dataCloned.installPlace){
				alert('설치위치는 필수 입력 사항입니다.');
				return false;
			}
		}
		return true;
	}

	self.insertData = function() {
		console.log(self.clazz + ".insertData => ", self.data());
		console.log(self.clazz + ".self.formId => ", self.formId);

		var rule = self.validationRules.insert;
		if( self.validate(rule, self.formId) ) {
			if( self.isCreateMode() && self.customValidation() && confirm('추가하시겠습니까?')) {
				var dataCloned =ko.toJS(_.omit(_.clone(self.data()), 'boardFiles','fireDetectorSeq'));
				dataCloned.mngAreaSeq = pageParam.ME_MNG_AREA_SEQ;
				$.ajax({
					url: $.format(self.detailsURL+"/{0}", "insert"),
					type: "POST",
					contentType: "application/json",
					iframe: true,
					files: $(":file", self.form),
					data: dataCloned,
					progessData: false,
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
		var rule = self.validationRules.update;
		var isSuccess = self.customValidation();
		if(!isSuccess){
			return;
		}
		if( self.validate(rule, self.formId) && self.customValidation() && confirm('수정하시겠습니까?') ){
			var dataCloned =ko.toJS( _.omit(_.clone(self.data()), 'boardFiles'));
			$.ajax({
				url: $.format(self.detailsURL+"/{0}/{1}", "update", self.originalData()[pkColumn]),
				type: "POST",
				contentType: "application/json",
				iframe: true,
				files: $(":file", self.form),
				data: dataCloned,
				progessData: false,
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

	self.disconnectionWithStore = function(){
		if (!confirm("점포선택을 해제 하시겠습니까?")) {
			return;
		}
		$.ajax({
			url: $.format(self.detailsURL+"/{0}/{1}/{2}", "disconnectionWithStore", self.originalData()['ctnNo'],self.originalData()['fireDetectorSeq']),
			type: "POST",
			contentType: "application/json",
			data: null,
			success: function(data) {
				if(ezUtil.checkRESTResponseMsg(data)) {
					_.defer(function() {
						alert("상점이 해지되었습니다.");
						setData(null);
						self.onDetailsChanged();
					})
				}
			}
		});
	}

	self.initLocalFileInput = function() {
		// 첨부 최대 갯수가 0일경우 버튼 비활성화
		if( pageParam.file_maxCnt == 0 )
		{
			self.hideLocalFileInputBtn();
			return;
		}
		var localCnt = self.getLocalFileCnt();
		var serverCnt = self.getServerFileCnt();
		if (pageParam.file_maxCnt <= localCnt + serverCnt) {
			self.addLocalFileInputCtl();
			self.hideLocalFileInputBtn();
			return;
		}
		var targetId = self.addLocalFileInputCtl();
		if(_.isEmpty(targetId)){
			self.hideLocalFileInputBtn();
		}
		else{
			self.showLocalFileInputBtn(targetId);
		}
	};

	// file input 추가
	self.addLocalFileInputCtl = function() {
		if( self.isActiveFileInputExist() ) {
			return "";
		}
		var idx = file_localFileInputIdx;
		var inputFileCtl = $('<input/>')
							.attr('type', 'file')
							.attr('name', 'fileInput')
							.attr('id', 'fileInput'+idx)
							.addClass("fileCss")
							.hide();
		inputFileCtl.bind('change', {idx: idx}, function(event) {
			var changeFile =  $('#fileInput' + event.data.idx);
			if (changeFile.length > 0) {
				var fileValue = changeFile.val().split("\\");
				var fileName = fileValue[fileValue.length-1]; // 파일명
				var isSuccess = true;
				var errorMsg = "";
				// HTML5 지원 브라우저만 파일용량 브라우저에서 체크가능
				if(this.files){
					if (this.files[0].size > pageParam.fileUploadSize) {
						isSuccess = false;
						errorMsg = "첨부파일은 " + pageParam.fileDisplayUploadSize + "까지 등록가능합니다.";
					}
				}

				if (isSuccess) {
					var fileType = self.getFileType(fileName);
					if (fileType == null) {
						isSuccess = false;
						errorMsg = "첨부파일은 이미지("+pageParam.ALLOWED_EXTENSION+") 파일만 가능 합니다.";
					}
				}

				if(isSuccess){
					if(self.hasSpecialCharacter(fileName)){
						isSuccess = false;
						errorMsg="이 파일명은 특수 문자를 포함하고 있습니다. 특수 문자를 제거 하신 이후 다시 파일 업로드를 해주세요."
					}
				}

				if (isSuccess) {
					self.addLocalPreCtl(event.data.idx, fileType);

					$('#preLocalTitle' + event.data.idx).text(fileName);

					$(this).hide();
					// 파일 설정 갯수를 초과했는지 검사 후, 새로운 Input생성
					self.initLocalFileInput();
				} else {
					alert(errorMsg);
					self.clearFileInput(event.data.idx);
					return;
				}
			}
		});

		file_localFileInputIdx++;
		$("#areaFileInput").append(inputFileCtl);
		return 'fileInput'+idx;
	};

	// 파일은 추가 했을경우 파일 목록에 추가
	self.addLocalPreCtl = function(idx, type) {
		// 이미 미리보기가 생성되었다면 반환
		if ($("#preLocalArea" + idx).length > 0){
			return;
		}
		var boxArea = $('<div/>').attr('id', 'preLocalArea' + idx).addClass("filebox");
		var preArea = $('<div/>').addClass("fileitem");
		var preSubArea = $('<div/>').addClass("fileinfo-bar");
		var titleCtl = $('<span/>').addClass("filename-txt").attr('id', 'preLocalTitle' + idx);
		preSubArea.append(titleCtl);

		var btnCtl = $('<span/>').addClass("close-btn");
		btnCtl.bind('click', {
			idx : idx
		}, function(event) {
			if (!confirm("삭제 하시겠습니까?")) {
				return;
			}

			self.deletePreview(event.data.idx);
			self.deleteFileInput(event.data.idx);
			self.initLocalFileInput();
		});
		preSubArea.append(btnCtl);
		preArea.append(preSubArea);

		var descCtl = $('<div/>').addClass("filedesc").text("저장 전");

		boxArea.append(preArea);
		boxArea.append(descCtl);
		$("#areaFilePreview").append(boxArea);

	}

	self.getFileType = function(fileName) {

		var searchResult = _.indexOf( pageParam.ALLOWED_EXTENSION.split(",") ,  fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase());
		if (_.isEqual(searchResult, -1) == true) {
			return null;
		} else {
			return "file";
		}
	};

	//로컬 선택된 파일 갯수.
	self.getLocalFileCnt = function(){
		return $("[id^='preLocalArea']:visible").length;
	};
	//서버에 저장된 파일 갯수.
	self.getServerFileCnt = function (){
		return $("[id^='preServerArea']:visible").length;
	};
	//현재 활성화된 파일 입력창이 있는지 판단.
	self.isActiveFileInputExist = function (){
		if( $("input[id^='fileInput']:visible").length > 0 )
			return true;
		return false;
	};

	// 특수문자 체크
	self.hasSpecialCharacter = function(fileName){
		var format =/[\/:*?"<>|]/;
		var specialchars = format.test(fileName);
		return specialchars;
	}

	self.clearFileInput = function(idx) {
		self.deleteFileInput(idx);
		self.initLocalFileInput();
	};

	self.deleteFileInput = function(idx) {
		var fileInput = $('#fileInput' + idx).remove();
	};

	self.deletePreview = function(idx) {
		$('#preLocalArea' + idx).remove();
	};

	self.deleteServerPreview = function(idx) {
		$('#preServerArea' + idx).remove();
	};

	self.hideLocalFileInputBtn = function(){
		$("#btnLocalFileInput").hide();
	};

	self.showLocalFileInputBtn = function(targetId) {
		$("#btnLocalFileInput").show();
		$('#btnLocalFileInput').attr('for', targetId);
	};

	// 이미 등록된 파일을 미리보기에 표시.(수정모드로 들어온경우)
	self.initServerPreCtls = function() {
		console.log(self.clazz + " filnename : ", self.originalData() );
		self.file_answerCnt = self.data().boardFiles.length;
		for (var nF = 0; nF < self.data().boardFiles.length; nF++) {
			self.addServerPreCtl(self.data().boardFiles[nF], nF);
		}
	}

	// 저장하기를 통해 서버에 저장된 이미지 Preview추가
	self.addServerPreCtl = function(file, idx) {
		var boxArea = $('<div/>').attr('id', 'preServerArea' + idx).addClass("filebox");
		var preArea = $('<div/>').addClass("fileitem");
		var preSubArea = $('<div/>').addClass("fileinfo-bar");
		var titleCtl = $('<span/>').addClass("filename-txt").attr('id', 'preServerTitle' + idx).text(file.orginalFileName);
		preSubArea.append(titleCtl);
		var descCtl = $('<div/>').addClass("filedowndesc").text("다운로드");

		// 다운로드
		descCtl.bind('click', {
			fileIdx : file.attachedFileSeq,
			idx : idx
		}, function(event) {
			self.fileDownload(event.data.fileIdx);
		});

		// 삭제
		if(pageParam.ME_ROLEGROUP_NAME == 'HQ_ADMIN'){
			var btnCtl = $('<span/>').addClass("close-btn btn");
			btnCtl.bind('click', {
				fileIdx : file.attachedFileSeq,
				idx : idx
			}, function(event) {
				if (!confirm("저장된 파일은 바로 삭제 됩니다. 삭제 하시겠습니까?")) {
					return;
				}
				$.ajax({
					url : self.deleteFileURL + "/" + event.data.fileIdx,
					type : 'GET',
					data : null,
					contentType : "application/json",
					processData : false,
					success : function(data) {
						if (ezUtil.checkRESTResponseMsg(data)) {
							self.file_answerCnt--; // 서버에 저장된 파일 수 감소.
							// 미리보기 삭제
							$("#preServerArea" + event.data.idx).remove();
							// 파일입력창 재탐색
							self.initLocalFileInput();
							alert("삭제되었습니다.");
						}
					}
				});
			});
			preSubArea.append(btnCtl);
		}

		preArea.append(preSubArea);
		boxArea.append(preArea);
		$("#areaFilePreview").append(boxArea);
		boxArea.append(descCtl);
	}

	self.fileDownload = function(attachedFileSeq) {
		ezUtil.downloadAttachedFile(self.downloadURL + attachedFileSeq );
	};

}

// 상점 팝업 설정
function SelectStorePopupVM(elementId, url) {
	var self = this;
	self.clazz = "SelectStorePopupVM";

	self.popupId = elementId;
	self.iframeSrc = url;

	//Close Callback
	 self.close = function() {
		self.popupElement.dialog('close');
	};

	//화면 갱신 로직
	self.initFromParent = function() {
		$("iframe[id=selectStoreModal]")[0].contentWindow.rootVM.init();
	}

	self.popupOptions = {
		width: 1080,
		height: 780,
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

// 좌표 팝업 설정
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

// 팝업 이벤트
function CommonPopupEvent(){
	var self = this;
	// 상점검색
	self.selectStoreEvent = {
			successCallback: function(obj){
				if ( !_.isNull(rootVM.detailsVM) && _.isObject(rootVM.detailsVM.data()) ){
					var detailsData = rootVM.detailsVM.data();
					detailsData['mngAreaName'](obj.mngAreaName);
					detailsData['marketName'](obj.marketName);
					detailsData['storeName'](obj.storeName);
					detailsData['storeSeq'](obj.storeSeq);
					detailsData['zipCode'](obj.zipCode);
					detailsData['longitude'](obj.longitude);
					detailsData['latitude'](obj.latitude);
					detailsData['parcelAddress'](obj.parcelAddress);
					detailsData['roadAddress'](obj.roadAddress);
				}
				rootVM.detailsVM.selectStorePopupVM.close();
			}
	};
	// 주소 선택
	self.searchAddrEvent = {
			successCallBack: function(data){
				rootVM.detailsVM.data().zipCode(data.zip);
				rootVM.detailsVM.data().roadAddress(data.roadAddr);
				rootVM.detailsVM.data().parcelAddress(data.jibunAddr);
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
	                rootVM.detailsVM.showSelectLocationModal();
	            }
	        }
	};
	// 좌표 선택
	self.selectLocationEvent = {
		successCallback: function(obj){
			if ( !_.isNull(rootVM.detailsVM) && _.isObject(rootVM.detailsVM.data()) ){
				var detailsData = rootVM.detailsVM.data();
				detailsData['latitude'](obj.la);
				detailsData['longitude'](obj.lo);
			}
			rootVM.detailsVM.selectLocationPopupVM.close();
		}
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
	rootVM = new FireDetectorSetVM();
	rootVM.init();
	ko.applyBindings(rootVM);
});
