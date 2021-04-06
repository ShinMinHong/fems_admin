var rootVM = null;

// 화재감지기 이벤트 조회(검색 , 리스트)
function FireDetectorEventLogVM() {

	var self = this;
	self.clazz = "FireDetectorEventLogVM";

	//Extend VM
	vmExtender.extendGridVM(self);

	//Base URL
	self.baseURL = contextPath + "/firedetector/firedetectoreventlog";
	//List API URL
	self.listURL = self.baseURL +"/api/page.json";
	//Details REST API URL
	self.detailsURL = self.baseURL +"/api/details";

	self.fireDetectorApiURL = contextPath + "/firedetector/firedetectormng/api/details";

	// 시장목록 코드맵
	self.marketSelectOptions =pageParam.MARKET_CODE_MAP;

	// 감지기 상태 코드맵
	self.signalType = ezUtil.convertObjectToSelectOptions(pageParam.DETECTOR_SIGNAL_TYPE);

	//Search VM
	self.searchData = ko.observable({
		searchMarketSeq :pageParam.ME_MARKET_SEQ,
		searchStoreName:"",
		searchCtnNo:"",
		searchSignalType:"",
		searchModelNo:"",
		searchStartDate : ko.observable(moment(serverTime).add(-1, 'week').format("YYYY-MM-DD")),
		searchEndDate : ko.observable(moment(serverTime).format("YYYY-MM-DD"))
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
			.mapObject(function(val, key) { return _.isString(val)? encodeURIComponent(val) : val; })
			.value();
		self.grid.load();
	};

	//Custom GridColumn Template- 수치값과  boolean 값을 한번에 표시
	self.getStatusAndValueByMsg = function(booleanStatusField , valueFiled, tMsg, fMsg) {
		var closure = {
				booleanStatusField: booleanStatusField,
				valueFiled: valueFiled,
				tMsg : tMsg,
				fMsg : fMsg
		};
		var template = function(dataItem) {
			var value = dataItem[closure.valueFiled];
			return dataItem[closure.booleanStatusField] ? tMsg + "("+value+")" : fMsg + "("+value+")";
		};
		return template;
	};

	// 각행에 CTN 번호를 가지고 있는 a href 태그 생성
	self.createHyperlink = function(dataItem){
		var text = '[전송금지]';
		return '<a href="#" onclick = rootVM.preventsubmission("'+dataItem['ctnNo']+'")>'+text+'</a>';
	};

	// 전송금지 하이퍼링크 클릭시
	self.preventsubmission = function(ctnNo){
		$.ajax({
			url: $.format(self.fireDetectorApiURL+"/{0}/{1}", "preventsubmission", ctnNo),
			type: "POST",
			contentType: "application/json",
			data: null,
			success: function(data) {
				if(ezUtil.checkRESTResponseMsg(data)) {
					console.log("전송금지등록이 완료 되었습니다.");
				}
			}
		});
	}


	var trueMessage = "<font color='red'><b>ON</b></font>";
	var falseMessage = "<font color='blue'><b>OFF</b></font>";
	//Data Table Columns Settings
	self.gridColumns = [
		{ field: "rn", title: "No", width: 40, ezAlign: 'center'},
		{ field: "mngAreaSeq", title: "관제지역명", width: 150, ezAlign: 'center',template: self.gridColumnTemplate.getTextFromCodeMap('mngAreaSeq',pageParam.MNG_AREA_CODE_MAP)},
		{ field: "marketSeq", title: "관리지역명", width: 170, ezAlign: 'center', template: self.gridColumnTemplate.getTextFromCodeMap('marketSeq',pageParam.MARKET_CODE_MAP)},
		{ field: "storeName", title: "점포명", width: 200, ezAlign: 'center'},
		{ field: "modelNo", title: "모델번호", width: 120, ezAlign: 'center'},
		{ field: "ctnNo", title: "CTN 번호", width: 120, ezAlign: 'center'},
		{ field: "signalType", title: "신호타입", width: 120, ezAlign: 'center', template: self.gridColumnTemplate.getTextFromCodeMap('signalType',self.signalType)},
		{ field: "smokeValue", title: "화재(연기)", width: 100, ezAlign: 'center', template: self.getStatusAndValueByMsg('smokeEvent','smokeValue',trueMessage,falseMessage)},
		{ field: "temperatureValue", title: "화재(온도)", width: 100, ezAlign: 'center', template: self.getStatusAndValueByMsg('temperatureEvent','temperatureValue',trueMessage,falseMessage)},
		{ field: "flame1Value", title: "화재(불꽃1)", width: 100, ezAlign: 'center', template: self.getStatusAndValueByMsg('flameEvent','flame1Value',trueMessage,falseMessage)},
		{ field: "flame2Value", title: "화재(불꽃2)", width: 100, ezAlign: 'center', template: self.getStatusAndValueByMsg('flameEvent','flame2Value',trueMessage,falseMessage)},
		{ field: "coValue", title: "화재(CO)", width: 100, ezAlign: 'center', template: self.getStatusAndValueByMsg('coEvent','coValue',trueMessage,falseMessage)},
		{ field: "batteryValue", title: "3V 배터리(%)", width: 100, ezAlign: 'center'},
		{ field: "battery2Value", title: "3.6V 배터리(%)", width: 110, ezAlign: 'center'},
		{ field: "notFireYn", title: "비화재보", width: 80, ezAlign: 'center', template: self.gridColumnTemplate.TFCodeMap('notFireYn',"<font color='blue'><b>비화재</b></font>","<font color='red'><b>화재</b></font>") },
		{ field: "demonRegDate", title: "발생일시", width: 170, ezAlign: 'center', template:self.gridColumnTemplate.momentFormatter('demonRegDate', 'YYYY-MM-DD HH:mm:ss.SSS')},
		{ title: "전송금지등록", width: 100, ezAlign: 'center', template:self.createHyperlink}
	];

	self.customGridOption = {
			/*height: 400*/
			/*scrollable: true*/
			/*filterable: true*/
	};

	self.grid = new AbleKendoGridVM("#grid", self.listURL, self.gridColumns, null, self.applySearchCondition, self.customGridOption);

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
	};

	//Excel Download
	self.isMergeExcelCell = ko.observable(false);
	self.excelDownload = function(){
		var url = self.baseURL +"/excel?";
		var data  = _.extend(self.searchCondition, {mergeExcelCell: self.isMergeExcelCell()});
		var params = $.param(data);
		document.location.href = url + params;
	};

	self.init = function() {
		self.search();
		console.log(self.clazz + " Initialized.");
	};
}

$(document).ready(function() {
	//공통 초기화
	ezUtil.ezInitialize(false/*blockUsingPageAjaxIndicator*/);
	rootVM = new FireDetectorEventLogVM();
	rootVM.init();
	ko.applyBindings(rootVM);
});
