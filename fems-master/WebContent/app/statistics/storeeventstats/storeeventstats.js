var rootVM = null;

function RootVM() {

	var self = this;
	self.clazz = "RootVM";
	//Extend VM
	vmExtender.extendGridVM(self);

	//List API URL
	self.listURL = contextPath + "/statistics/storeeventstats/api/storeeventstatspage.json";
	//Details REST API URL
	self.detailsURL = contextPath + "/statistics/storeeventstats/api/details";

	//시장관리자면 본인 시장 셋팅
	self.getSearchMarketSeq = _.isEmpty(pageParam.ME_MARKET_SEQ) ? "" : pageParam.ME_MARKET_SEQ;

	//관리지역 Code Map
	self.marketSelectOptions =  ezUtil.orderBySelectOptionsText(pageParam.MARKET_CODE_MAP);
	self.marketCodeMap = ezUtil.convertSelectOptionsToObject(pageParam.MARKET_CODE_MAP);

	// 사용자 접속 시간 기준 발송 시작일 발송 종료일 만들기 위해 서버타임 설정
	self.serverTime = serverTime;

	//Search VM
	self.searchData = ko.observable({
		searchMarketSeq: self.getSearchMarketSeq,
		searchStoreName: "",
	    searchStartDate : ko.observable(moment(serverTime).startOf('month').format("YYYY-MM-DD")), //발생기간 시작일
	    searchEndDate : ko.observable(moment(serverTime).subtract(1, 'days').format("YYYY-MM-DD")) // 발생기간 종료일
	});
	//Search Condition Applied
	self.searchCondition = {};

	//Apply Search Condition
	self.applySearchCondition = function(param) {
		return _.extend(param, self.searchCondition);
	}

	//Search
	self.search = function() {
		if (self.searchData().searchEndDate() > moment(serverTime).subtract(1, 'days').format("YYYY-MM-DD")) {
	      alert("검색 종료일이 전일보다 큽니다. 전일까지만 통계가 수집됩니다.");
	      return;
	    }

	    self.searchCondition = _.chain(self.searchData())
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
					url: $.format("{0}/{1}/{2}/{3}.json", self.detailsURL, data.storeSeq,rootVM.searchData().searchStartDate(),rootVM.searchData().searchEndDate()),
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
		{ field: "marketName", title: "관리지역명", width: 150, ezAlign: 'center'},
		{ field: "storeName", title: "점포명", width: 200, ezAlign: 'left'},
		{ field: "smokeCount", title: "연기", width: 100, ezAlign: 'center'},
		{ field: "temperatureCount", title: "온도", width: 100, ezAlign: 'center'},
		{ field: "flameCount", title: "불꽃", width: 100, ezAlign: 'center'},
		{ field: "coCount", title: "CO", width: 100, ezAlign: 'center'},
		{ field: "rowSum", title: "합계", width: 100, ezAlign: 'center'}
	];

	self.grid = new AbleKendoGridVM("#grid", self.listURL, self.gridColumns, self.onGridEvent, self.applySearchCondition);

	self.detailsVM = new DetailsVM(self.detailsURL, self.grid.load);

	//Excel Download
	self.isMergeExcelCell = ko.observable(false);
	self.excelDownload = function(){
		if(rootVM.grid.dataSource._pristineData.length==0){
			alert("검색된 데이터가 없습니다.");
			return;
		}
		var url = contextPath + "/statistics/storeeventstats/excel?";
		var data  = _.extend(self.searchCondition, {mergeExcelCell: self.isMergeExcelCell()});
		var params = $.param(data);
		document.location.href = url + params;
	}

	self.init = function() {
		self.search();
		console.log(self.clazz + " Initialized.");
	};
}

//통계 상세 페이지
function DetailsVM(detailsURL, onDetailsChanged) {

	var self = this;
	self.clazz = "DetailsVM";

	//상세정보 REST API 기본URL - CRUD
	self.detailsURL = detailsURL;

	//데이터 변경 핸들러
	self.onDetailsChanged = onDetailsChanged || function() {};

	//Extend VM
	vmExtender.extendGridVM(self);
	//Form
	self.formId = "#detailsForm";

	// 원본 데이터 저장
	self.originalData = ko.observable(null);

	// 통계 파이 차트 및 통계 테이블에 쓰이는 데이터
	self.data = ko.observable(null);

	// 통계 테이블 건수에 대한 총 합계
	self.dataSum = ko.observable();

	// 상세 통계 테이블 SelectedRow의 이름(구분) ex. 화재
	self.rowSelectedEvent = null;

	// 원본데이터 -> 통계 테이블 및 통계 차트에 맞는 데이터로 변환
	self.convertData = function(rawData){
		var colorList = ['#f53718', '#6f7a18', '#c6a933', '#fda11e', '#174aab', '#893bb6', '#d93072','#6600ff'];
	    var eventCodeMap={smokeCount:'연기',temperatureCount:'온도',flameCount:'불꽃',coCount:'CO'}

	    // 전체 이벤트의 총합
	    var total =_.sum(_.values(rawData));
	    idx = 0;
	    var convertedData=_.chain(rawData) // 이벤트별 통계 합 pivot , 이벤트 열을 행으로 변환 및 데이터 추가(이벤트 코드를 코드명 변경, 퍼센트 자동계산, 차트에 들어갈 색깔 넣어줌)
	     .map(function(val,key){
	       var obj={};
	       obj['value']=val;
	       obj['percent']= total==0? 0 : (val/total *100).toFixed(2);
	       obj['category']=eventCodeMap[key];
	       obj['color']=colorList[idx++];
	      return obj;
	    })
	    .value()
	    return convertedData;
	}

	// 상세 통계 파이 차트
	self.createChart = function(convertedData){
	    // 데이터가 없을떄 메세지 처리
	    if(self.dataSum().total === 0) {
	        $("#chart").kendoChart({
		          title: {
		          text: "발생된 화재 이벤트가 존재하지 않습니다.",
		          position: 'center',
		          font: "14px bold"
		        },
		        series: [{
		          type: "pie",
		          data: convertedData
		        }]
	        }).css("height",'300px'); // 데이터가 없을떄 창 크기
	    }else{
	       $("#chart").css("height",'300px'); // 데이터가 있을떄 창크기
	       $(".table").css("height",'300px');
	    }

	    $("#chart").kendoChart({
	      title: {
	        position:"top",
	        text: "점포 화재 이벤트 통계 원형 차트",
	        font: "16px bold",
	        color: '#393939'
	      },
	      legend: {
	         position: "right",
	         font: "15px",
	         background: "#dfdfdf"
	      },
	      seriesDefaults: {
	        labels: {
	          template: "#= kendo.format('{0:P}', percentage)"!="0"? "#= category # - #= kendo.format('{0:P}', percentage)#":"" ,
	          position: "outsideEnd",
	          visible: true,
	          distance:8,
	          background: "transparent",
	          font: "10px ",
	          color: '#393939'
	        }
	      },
	      series: [{
	        type: "pie",
	        data: convertedData
	      }],
	      tooltip: {
	        visible: true,
	        template: "#= category # - #= kendo.format('{0:P}', percentage) #"
	      }
	    });
	}

	var setData = function(data) {
		self.originalData(data);
	    //원본 데이터에서 통계 차트에 쓰일 property 추출
	    var dataCloned = _.pick(data,'smokeCount','temperatureCount','flameCount','coCount');
	    var convertedData = self.convertData(dataCloned);
	    //점포 화재이벤트 테이블 데이터 저장
	    self.data(convertedData);
	    var total =_.sum(_.pluck(convertedData,'value'));
	    var percent = total > 0 ? 100 : 0;
	    var sum = {total:total, percent:percent};
	    self.dataSum(sum);
	    // 통계 테이블 커스텀 렌더링
	    /*self.customTableRendering();*/
	    // 차트 그리기
	    self.createChart(convertedData);
	}

	//편집모드 상태값
	var pkColumn = "storeSeq";

	self.isEditMode = ko.computed(function() { return !_.isNull(self.originalData()) && !_.isBlank(self.originalData()[pkColumn]); });

	//수정시작
	self.startEdit = function(details) {
	    console.log(self.clazz + ".startEdit => ", details);
	    setData(details);
	    $(window).scrollTo("#detailsForm", 300);
	};

	//취소
	self.cancel = function() {
		setData(null);
	};

	// 차트 반응형
	$(window).on("resize", function () {
	    if(self.data()){
	      self.createChart(self.data());
	    }
	});
}

$(document).ready(function() {
	//공통 초기화
	ezUtil.ezInitialize(false/*blockUsingPageAjaxIndicator*/);

	rootVM = new RootVM();
	rootVM.init();

	ko.applyBindings(rootVM);
});