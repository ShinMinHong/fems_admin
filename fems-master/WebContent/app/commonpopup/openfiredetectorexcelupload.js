var rootVM = null;

function RootVM(){

	var self = this;
	self.clazz = "RootVM";

	//Details REST API URL
	self.baseURL = contextPath + "/firedetector/firedetectormng/api";

	self.sampleExcelDownload = function(){
		document.location.href =  contextPath + "/documents/firedetectormng/firedetectorexceluploadsample.xlsx";
	}

	self.excelUpload = function(){
		if(_.isEmpty($("#uploadExcelFile").val())){
			alert("업로드 파일을 선택해주세요.");
			return false;
		}

		var fileInput = document.getElementById('uploadExcelFile');
		var file = fileInput.files[0];
		var formData = new FormData();
		formData.append('uploadExcelFile', file);

		$.ajax({
			url : $.format(self.baseURL+"/{0}.json", "firedetectorexcelupload"),
			type : "POST",
			data : formData,
			processData: false,  // tell jQuery not to process the data
			contentType: false,  // tell jQuery not to set contentType
			success : function(data) {
				if(ezUtil.checkRESTResponseMsg(data)) {
					self.excelResultVM.init(data.body)
					if( data.body.failCount == 0 ){
						//부모창 refresh
						parent.rootVM.search();
					}
				}
			}
		});

	};

	self.excelResultVM = new ExcelResultVM();
	self.isVisibleExcelResult = ko.computed(function() { return (self.excelResultVM != null && !_.isEmpty(self.excelResultVM.summaryObj()) ) });

	self.init = function(){
		if ( self.excelResultVM != null && self.excelResultVM.init != null ) {
			self.excelResultVM.init(null);
		}
	};
};

function ExcelResultVM(){

	var self = this;
	self.clazz = "ExcelResultVM";

	self.failCount = ko.observable();
	self.successCount = ko.observable();
	self.totalCount = ko.observable();

	//오류컬럼 색상처리
	self.textTemplate = function(gridField){
		var closure = {
				gridField: gridField
		};
		closure.gridField; //dummy for eclipse stupid validation
		var template = function(dataItem) {
			if (dataItem['field'] == closure.gridField){
				return "<div data-errored='true'>" + dataItem[closure.gridField] + "</div>";
			} else {
				return "<div>" + dataItem[closure.gridField] + "</div>";
			}
		};
		return template;
	};

	self.gridColumns =  [     
	          	{ ezAlign: 'center', field: "ctnNo", title: "CTN번호", width: 120,template: self.textTemplate('ctnNo') },
	          	{ ezAlign: 'center', field: "usimNo", title: "유심번호", width: 100,template: self.textTemplate('usimNo')},
	          	{ ezAlign: 'center', field: "serialNo", title: "단말기일련번호", width: 100,template: self.textTemplate('serialNo')},
	          	{ ezAlign: 'center', field: "productNo", title: "제조번호", width: 100,template: self.textTemplate('productNo')},
	          	{ ezAlign: 'center', field: "modelNo", title: "모델번호", width: 130,template: self.textTemplate('modelNo')},
	          	{ ezAlign: 'center', field: "storeSeq", title: "점포고유번호", width: 80,template: self.textTemplate('storeSeq')},
 				{ ezAlign: 'center', field: "storeName", title: "점포명", width: 200},
 				{ ezAlign: 'center', field: "zipCode", title: "우편번호", width: 100,template: self.textTemplate('zipCode')},
 				{ ezAlign: 'center', field: "roadAddress", title: "도로명주소", width: 200,template: self.textTemplate('roadAddress')},
 				{ ezAlign: 'center', field: "parcelAddress", title: "지번주소", width: 200,template: self.textTemplate('parcelAddress')},
 				{ ezAlign: 'center', field: "latitude", title: "위도", width: 100,template: self.textTemplate('latitude')},
 				{ ezAlign: 'center', field: "longitude", title: "경도", width: 100,template: self.textTemplate('longitude')},
 				{ ezAlign: 'center', field: "installPlace", title: "설치위치", width: 100,template: self.textTemplate('installPlace')},
 				{ ezAlign: 'center', field: "fireDetectorName", title: "화재감지기명", width: 150},
				{
					ezAlign: 'center',
					title: '결과',
					columns: [
				          	{ ezAlign: 'center', field: "rowIndex", title: "행순번", width: 60 },
				          	{ ezAlign: 'left', field: "resultMsg", title: "상태", width: 500 }
						]
			    }
	];

	self.buildGrid = function(data){
		if (self.grid != null) {
			//이미 그리드가 존재한다면... 데이터만 변경
			self.grid.setData(data);
		} else {
			//Grid VM
			self.gridOptions = {
				height: 270,
				pageable: false,
				scrollable: true
			}
			self.grid = new AbleKendoTableVM("#excelResultGrid", self.gridColumns, _.clone(self.gridOptions));
			self.grid.setData(data);
		}

		_.defer(function() {
			//오류 열 색상 처리
			$("div[data-errored=true]").closest("td").attr("style", "background-color: #FFC7C7");
		});
	};

	self.summaryObj = ko.observable();

	//서버로 부터 전달 받은 엑셀 결과 담을 객체 (검색을 위해 사용)
	self.excelResultVO = [];

	//검색 selectOptions
	self.searchResultSelectOptions = ([{value:'S', text:'정상'}, {value:'F', text:'실패'}]);

	self.search = function(){
		var data = _.clone(self.excelResultVO);
		if ( !_.isBlank(self.searchData().searchResultCode()) ) {
			data = _.filter(data, function(row) { return row.resultCode == self.searchData().searchResultCode(); });
		}
		self.buildGrid(data);
	};

	//Search VM
	self.searchData = ko.observable({
		searchResultCode:  ko.observable(null)
	});

	self.buildSearchData = function(data) {
		self.excelResultVO = data;
	};

	self.init = function(data){
		if ( data != null ){
			//grid 생성
			self.buildGrid(data.excelResultVO);
			self.buildSearchData(data.excelResultVO);
			var totalMsg = "";
			if ( data.totalCount == 0 ){
				totalMsg = "엑셀에 업로드를 진행할 데이터가 존재하지 않습니다.";
			} else if ( data.failCount > 0 ){
				totalMsg = "업로드 데이터에 오류가 발생 하였습니다. 확인 후 다시 시도해 주세요.";
			} else {
				totalMsg = "엑셀 업로드가 완료되었습니다.";
			}
			if(data.errorCnt != 0){
				rootVM.excelResultVM.searchData().searchResultCode("F");
			}
			//summary 생성
			self.summaryObj({
				totalMsg: totalMsg,
				totalCnt: "총: " + data.totalCount + "건",
				successCnt: "정상: " + data.successCount + "건",
				errorCnt: "실패: " + data.failCount + "건"
			});
		} else {
			self.summaryObj(null);
			if ( self.grid != null && self.grid.setData != null ){
				self.grid.setData([]);
			}

			if ($.browser.msie) {
				// ie 일때 input[type=file] init.
				$("#uploadExcelFile").replaceWith( $("#uploadExcelFile").clone(true) );
			} else {
				// other browser 일때 input[type=file] init.
				$("#uploadExcelFile").val("");
			}
		}
	};
};

$(document).ready(function() {
	ezUtil.ezInitialize(false/*blockUsingPageAjaxIndicator*/);
	rootVM = new RootVM();
	rootVM.init();
	ko.applyBindings(rootVM);
});