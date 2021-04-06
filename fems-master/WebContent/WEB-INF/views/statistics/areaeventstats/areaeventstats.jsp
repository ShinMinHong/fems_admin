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
					<label class="control-label text-right">발생기간 시작일</label>
					<input type="text" class="form-control input-xs" id="searchStartDate" data-bind="value: searchStartDate, rangeDatePicker: {value: searchStartDate, endElementId: 'searchEndDate', endValue: searchEndDate}" />
		        </div>
		        <div class="col-md-3">
					<label class="control-label text-right">발생기간 종료일</label>
					<input type="text" class="form-control input-xs" id="searchEndDate" data-bind="value: searchEndDate"/>
		        </div>
				<div class="col-md-3">
					<label class="control-label text-right"> </label>
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
<div id="grid"></div>

<!-- Excel Download  -->
<div class="text-left" style="padding-top: 10px; float: left;">
	<a href="#" class="btn bg-teal btn-xs" data-bind="click: excelDownload ">Excel Download<i class="icon-file-excel position-right"></i></a>
</div>
<br>

<!-- 상세페이지  -->
<form id="detailsForm" class="form-horizontal" action="#" data-bind="visible: detailsVM.isEditMode"  style="padding-top:40px;">
	<div class="panel panel-flat">
		<div class="panel-heading">
    		<h5><i class="icon-stats-bars2"></i><bold> 지역별 화재 신호 통계 </bold></h5>
      		<div class="heading-elements">
        		<ul class="icons-list">
          			<li><a data-action="collapse"></a></li>
        		</ul>
      		</div>
      		<a class="heading-elements-toggle"><i class="icon-menu"></i></a>
    	</div>
    <div class="panel-body" data-bind="with: detailsVM" >
        <div class=" form-group">
           <!-- 통계 상세 테이블  -->
           <div class=" text-nowrap col-md-6 col-sm-12 ">
                <table id="mainprposstats" class="table table-bordered table-hover">
                  <colgroup>
                <col style="width: 30%">
                <col style="width: 30%">
                <col style="width: 30%">
                </colgroup>
                  <thead class="bg-primary">
                    <tr>
                      <th class="text-center" scope="col">구분</th>
                      <th class="text-center" scope="col">건수</th>
                      <th class="text-center" scope="col">발생률<br />(%)</th>
                    </tr>
                  </thead>
                  <tbody id="tbody">
                    <!-- ko foreach: data() -->
                    <tr>
                      <td class="text-center" style="vertical-align: middle !important;"><span data-bind="text: category"></span><div style="display: inline;" class="text-right"></div></div></td>
                      <td class="text-center" style="vertical-align: middle !important;"><span data-bind="text: value"></span></td>
                      <td class="text-center" style="vertical-align: middle !important;"><span data-bind="text: percent"></span></td>
                    </tr>
                    <!-- /ko -->
                  </tbody>
                  <tfoot style="border-top: thick double silver;" data-bind="with:dataSum">
                    <tr>
                      <td class="text-center"><span>합계</span></td>
                      <td class="text-center"><span data-bind="text: total"></span></td>
                      <td class="text-center"><span data-bind="text: percent"  ></span></td>
                    </tr>
                  </tfoot>
                </table>
          </div>
          <!-- 원형 차트  -->
          <!-- 주의 : javaScript 에서 height 조절 및 에러 메세지 제어  -->
          <div class="col-md-6 mb20 col-sm-12">
            <div id="example" style="border:2px solid #bdbdbd">
              <div class="demo-section k-content ">
                <div id="chart" style="width:auto;">
                </div>
              </div>
            </div>
          </div>
          <div class="text-right " data-bind="visible: isEditMode">
          <button type="button" data-bind="click: cancel" class="mr-10 mt-20 btn btn-xs"><b><i class="icon-cross"></i></b> 닫기</button>
        </div>
      </div>
    </div>
  </div>
</form>


<script type="text/javascript">
	setPageLocation("statistics/areaeventstats", "지역별 화재 신호 통계");

    var pageParam = {
    		MARKET_CODE_MAP: ${ MARKET_CODE_MAP },
    		MNG_AREA_SEQ: '${ me.mngAreaSeq }',
    		ME_MARKET_SEQ: '${ me.marketSeq }'
	};

    var serverTime = "${serverTime}";

</script>

<script type="text/javascript" src="${contextPath}/app/statistics/areaeventstats/areaeventstats.js"></script>
