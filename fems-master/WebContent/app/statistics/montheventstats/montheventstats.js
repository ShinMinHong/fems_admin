var rootVM = null;

function RootVM() {

	var self = this;
	self.clazz = "RootVM";
	//Extend VM
	vmExtender.extendGridVM(self);

	//List API URL
	self.listURL = contextPath + "/statistics/montheventstats/api/montheventstatslist";

	//시장관리자면 본인 시장 셋팅
	self.getSearchMarketSeq = _.isEmpty(pageParam.ME_MARKET_SEQ) ? "" : pageParam.ME_MARKET_SEQ;

	//관리지역 Code Map
	self.marketSelectOptions =  ezUtil.orderBySelectOptionsText(pageParam.MARKET_CODE_MAP);
	self.marketCodeMap = ezUtil.convertSelectOptionsToObject(pageParam.MARKET_CODE_MAP);

	// 사용자 접속 시간 기준 발송 시작일 발송 종료일 만들기 위해 서버타임 설정
	self.serverTime = serverTime;
	// 현재년도
	var currentYear = moment(self.serverTime,"YYYY-MM-DD HH:mm:ss").format("YYYY");
	// 2020년 이후부터 현재년도까지
	self.yearMap = _.map(_.range( parseInt(currentYear),2019,-1), function(row){return {'text':row.toString(), 'value':row.toString()}});

	//Search VM
	self.searchData = ko.observable({
		searchMarketSeq: self.getSearchMarketSeq,
		searchRegDate: currentYear
	});
	//Search Condition Applied
	self.searchCondition = {};

	//Apply Search Condition
	self.applySearchCondition = function(param) {
		return _.extend(param, self.searchCondition);
	}

	self.data = ko.observable();

	//Search
	self.search = function() {
	    self.searchCondition = _.chain(self.searchData())
	      .omit(_.isEmpty)
	      .mapObject(function(val, key) { return _.isString(val)? encodeURIComponent(val) : val; })
	      .value();

	    self.pieChartData = [];
		var param =$.param(rootVM.searchCondition);
		$.ajax({
			url: $.format("{0}?{1}", self.listURL, param ),
			type: "GET",
			contentType: "json",
			success: function(data) {
				var res=data.ableResponseEntity;
				if(ezUtil.checkRESTResponseMsg(res)) {
					var originalData = res.body;
					// 총합을 구할 칼럼명
					var columnToSum = ['smokeCount','temperatureCount','flameCount','coCount','rowSum'];
					// 테이블 데이터에 각 필드들의 총합을 구해줌
					var dataCloned =self.addTotalSum(originalData,columnToSum);
					// 테이블 데이터 삽입
					self.grid.setData(dataCloned? dataCloned : []);
					//테이블 렌더링
					self.postRendering('bg-dark', 'bg-dark');

					self.data(dataCloned);
					self.createChartData(originalData);
				}
			}
		});
	};

	//Data Table Columns Settings
	self.gridColumns = [
		{ field: "mm", title: "월", width: 100, ezAlign: 'center'},
		{ field: "smokeCount", title: "연기화재", width: 100, ezAlign: 'center'},
		{ field: "temperatureCount", title: "온도화재", width: 100, ezAlign: 'center'},
		{ field: "flameCount", title: "불꽃화재", width: 100, ezAlign: 'center'},
		{ field: "coCount", title: "CO화재", width: 100, ezAlign: 'center'},
		{ field: "rowSum", title: "합계", width: 100, ezAlign: 'center'}
	];

	self.grid = new AbleKendoTableVM("#grid", self.gridColumns, {height: 420, pageable:false, selectable: false, enable:false}, null);

	//Excel Download
	self.isMergeExcelCell = ko.observable(false);
	self.excelDownload = function(){
		if(rootVM.grid.dataSource._pristineData.length==0){
			alert("검색된 데이터가 없습니다.");
			return;
		}
		var url = contextPath + "/statistics/montheventstats/excel?";
		var data  = _.extend(self.searchCondition, {mergeExcelCell: self.isMergeExcelCell()});
		var params = $.param(data);
		document.location.href = url + params;
	};

	// 차트 데이터 생성
	self.createChartData = function(data){
		// data - y축에 들어갈 데이터 , names - 이벤트 종류 , categories = x 축데이터
		//var data =[{"mm":"1","fire":0,"mainSiren":20,"localSiren":45,"brokenDevice":50,"lowwaterLevel":0,"mainPower":0,"subPower":90,"facilityAlarm":0,"rowSum":0},{"mm":"2","fire":0,"mainSiren":0,"localSiren":100,"brokenDevice":0,"lowwaterLevel":0,"mainPower":0,"subPower":0,"facilityAlarm":0,"rowSum":0},{"mm":"3","fire":0,"mainSiren":0,"localSiren":0,"brokenDevice":0,"lowwaterLevel":0,"mainPower":0,"subPower":0,"facilityAlarm":0,"rowSum":0},{"mm":"4","fire":0,"mainSiren":0,"localSiren":0,"brokenDevice":0,"lowwaterLevel":0,"mainPower":0,"subPower":0,"facilityAlarm":0,"rowSum":0},{"mm":"5","fire":0,"mainSiren":0,"localSiren":0,"brokenDevice":0,"lowwaterLevel":0,"mainPower":0,"subPower":0,"facilityAlarm":0,"rowSum":0},{"mm":"6","fire":0,"mainSiren":0,"localSiren":0,"brokenDevice":0,"lowwaterLevel":0,"mainPower":0,"subPower":0,"facilityAlarm":0,"rowSum":0},{"mm":"7","fire":0,"mainSiren":0,"localSiren":0,"brokenDevice":0,"lowwaterLevel":0,"mainPower":0,"subPower":0,"facilityAlarm":0,"rowSum":0},{"mm":"8","fire":0,"mainSiren":0,"localSiren":0,"brokenDevice":0,"lowwaterLevel":0,"mainPower":0,"subPower":0,"facilityAlarm":0,"rowSum":0},{"mm":"9","fire":0,"mainSiren":0,"localSiren":0,"brokenDevice":0,"lowwaterLevel":0,"mainPower":0,"subPower":0,"facilityAlarm":0,"rowSum":0},{"mm":"10","fire":1,"mainSiren":0,"localSiren":0,"brokenDevice":0,"lowwaterLevel":0,"mainPower":1,"subPower":0,"facilityAlarm":0,"rowSum":2},{"mm":"11","fire":0,"mainSiren":0,"localSiren":0,"brokenDevice":0,"lowwaterLevel":0,"mainPower":0,"subPower":0,"facilityAlarm":0,"rowSum":0},{"mm":"12","fire":0,"mainSiren":0,"localSiren":0,"brokenDevice":0,"lowwaterLevel":0,"mainPower":0,"subPower":0,"facilityAlarm":0,"rowSum":0},{"mm":"합계","fire":1,"mainSiren":0,"localSiren":0,"brokenDevice":0,"lowwaterLevel":0,"mainPower":1,"subPower":0,"facilityAlarm":8,"rowSum":2}];
		var names =["연기화재","온도화재","불꽃화재","CO화재"];
		var categories = ["1월","2월","3월","4월","5월","6월","7월","8월","9월","10월","11월","12월"];
		var title = "월별 화재 신호 통계"
		if(data){
			// 총합계 데이터 제거
			var arr =data.slice(0,-1);
		}
		var desirableData  =_.chain(arr)
									.map(function(row){
										var arr= _.values(_.omit(row,'mm','rowSum')); // 주의: rawData 에서 뺴줘야 할 항목을 넣어줘야함
										return arr;})
									.unzip()
									.value();

		// seriesData 만들기 위한 처리
		var zip =_.zip(names,desirableData);
		var colorList = ['#f53718', '#6f7a18', '#c6a933', '#fda11e'];
		var i=0;
		var seriesData =_.map(zip, function(row){
			return {name:_.first(row),data:_.last(row), color:colorList[i++]}
		})

		// 차트 데이터 삽입 차트 그리기
		self.drawChart(categories,seriesData,title);
	}

	// 차트 그리기
	self.drawChart = function(categories,seriesData,title){
		$("#chart").kendoChart({
            title: {
                text: title
            },
            legend: {
                position: "bottom"
            },
            chartArea: {
                background: ""
            },
            seriesDefaults: {
                type: "line",
                style: "smooth"
            },
            series: seriesData,
            valueAxis: {
                labels: {
                    format: "{0}"
                },
                line: {
                    visible: false
                },
                axisCrossingValue: -10
            },
            categoryAxis: {
                categories: categories ,
                majorGridLines: {
                    visible: false
                },
                labels: {
                    rotation: "auto"
                }
            },
            tooltip: {
                visible: true,
                format: "{0}",
                template: "#= series.name #: #= value #"
            }
        });
	}

	// 테이블 렌더링
	self.postRendering = function(rowColSumColor,sumColor){
		//rowColSumColor- row, Column 총합의 색깔 , sumColor - 우측 및 하단 합의 색깔
		var rowColSumColor = rowColSumColor;
		var sumColor = sumColor;
		// 하단 색깔
		_.each($('#grid tbody tr'), function(row) { $(row).find("td:last").addClass(sumColor)})
		_.each($('#grid tbody tr'), function(row) { $(row).find("td:last").removeClass('k-state-selected')})
		// 우측 색깔
		$("tbody:last tr:last ").each(function(index,item){$(item).addClass(sumColor)});
		$('tr:last td:last').addClass(rowColSumColor);

	}

	//  기존 데이터 총합 추가
	self.addTotalSum = function(dataCloned,columnToSum){
		var columnNameList = _.pluck(self.gridColumns,"field");
		var columnToSum = columnToSum;
		var colSum={};
		for( mainIndex in dataCloned ){
			var row = dataCloned[mainIndex]
			for(property in row ){
				if(_.contains(columnToSum, property)) {
					colSum[property] = colSum[property] ? colSum[property] + row[property] : row[property]
				}else{
					colSum[property]="합계";
				}
			}
		}
		dataCloned.push(colSum);
		// 기존의 데이터에 총합을 구해 리스트 마지막에 추가 , 정제된 차트 data
		return dataCloned;
	}


	$(window).on("resize", function () {
		self.createChartData(self.data());
	});

	self.init = function() {
		self.search();
		console.log(self.clazz + " Initialized.");
	};
}


$(document).ready(function() {
	//공통 초기화
	ezUtil.ezInitialize(false/*blockUsingPageAjaxIndicator*/);

	rootVM = new RootVM();
	rootVM.init();

	ko.applyBindings(rootVM);
});