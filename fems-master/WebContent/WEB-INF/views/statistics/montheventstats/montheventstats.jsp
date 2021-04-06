<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/layouts/include.jsp" %>

<!-- 검색  -->
<div class="panel panel-flat" data-bind="with: searchData, enterKey: $root.search">
    <div class="panel-heading">
        <span class="text-semibold">검색조건</span>
        <div class="heading-elements">
            <ul class="icons-list">
                <li><a data-action="collapse"></a></li>
            </ul>
        </div>
        <a class="heading-elements-toggle"><i class="icon-menu"></i></a>
    </div>
    <div class="panel-body paddingbottom0">
        <form class="form-horizontal" role="form">
			<!-- 검색조건  -->
			<div class="form-group">
				<div class="col-md-3">
					<label class="control-label text-right">관제지역</label>
					<select class="form-control input-xs" data-bind="options: $root.marketSelectOptions, optionsText:'text', optionsValue:'value', value: searchMarketSeq, optionsCaption: '전체'"></select>
				</div>
				<div class="col-md-3">
					<label class="control-label text-right">검색년도</label>
					<select class="form-control input-xs" data-bind="options:rootVM.yearMap , optionsText:'text', optionsValue:'value', value: searchRegDate"></select>
				</div>
		        <div class="col-md-6">
					<label class="control-label text-right"></label>
					<div class="text-right">
						<button type="button" class="btn btn-primary" data-bind="click: $root.search">검색<i class="glyphicon glyphicon-search position-right"></i></button>
					</div>
				</div>
			</div>
			<!-- 검색 조건 끝  -->
        </form>
    </div>
</div>

<!-- 목록  -->
<style>
/* 마지막열 총합 사라지는 현상 제거하기 위한 CSS*/
.k-grid tr:hover:last-child {
  background: #888;
}
/* 총합 및 합계*/
.bg-dark {
   background-color:#343a40!important;
    color: #fff;
}
</style>
<div id="grid"></div>

<!-- Excel Download  -->
<div class="text-left" style="padding-top: 10px; float: left;">
	<a href="#" class="btn bg-teal btn-xs" data-bind="click: excelDownload ">Excel Download<i class="icon-file-excel position-right"></i></a>
</div>
<br></br>

<!-- 차트  -->
<div class="col-md-12" style="padding-top:15px;">
	<div id="example">
	    <div class="demo-section k-content" style="border:2px solid #9e9e9e;"><!-- wide" style="overflow-x:auto; border:2px solid #9e9e9e;  -->
	        <div id="chart" ></div><!-- style="width:1550px;" -->
	    </div>
	</div>
</div>

<script type="text/javascript">
	setPageLocation("statistics/montheventstats", "월별 화재 신호 통계");

    var pageParam = {
    		MARKET_CODE_MAP: ${ MARKET_CODE_MAP },
    		MNG_AREA_SEQ: '${ me.mngAreaSeq }',
    		ME_MARKET_SEQ: '${ me.marketSeq }'
	};

    var serverTime = "${serverTime}";

</script>

<script type="text/javascript" src="${contextPath}/app/statistics/montheventstats/montheventstats.js"></script>
