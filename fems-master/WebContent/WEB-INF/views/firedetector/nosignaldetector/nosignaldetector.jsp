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
					<label class="control-label text-right">관리지역명</label>
					<select class="form-control input-xs" data-bind="options: $root.marketSelectOptions, optionsText:'text', optionsValue:'value', value: searchMarketSeq, optionsCaption: '전체'"></select>
				</div>
				 <div class="col-md-3">
					<label class="control-label text-right">점포명</label>
					<input type="text" class="form-control input-xs" placeholder="점포명" data-bind="value: searchStoreName">
				</div>
				<div class="col-md-3">
					<label class="control-label text-right">CTN</label>
					<input type="text" class="form-control input-xs" placeholder="CTN" data-bind="value: searchCtnNo">
				</div>
				<div class="col-md-3">
					<label class="control-label text-right">모델번호</label>
					<input type="text" class="form-control input-xs" placeholder="모델번호" data-bind="value: searchModelNo">
				</div>

			</div>
			<div class="form-group">
				<div class="col-md-3">
					<label class="control-label text-right">제조번호</label>
					<input type="text" class="form-control input-xs" placeholder="제조번호" data-bind="value: searchProductNo">
				</div>
				<div class="col-md-9">
					<label class="control-label text-right"></label>
					<div class="text-right">
						<button type="button" class="btn btn-primary" data-bind="click: $root.search">검색<i class="glyphicon glyphicon-search position-right"></i></button>
					</div>
				</div>
			</div>
        </form>
    </div>
</div>

<!-- 목록  -->
<div id="grid"></div>

<!-- 엑셀 다운로드   -->
<div class="text-left" style="padding-top: 10px; float: left;">
	<a href="#" class="btn bg-teal btn-xs" data-bind="click: excelDownload ">Excel Download<i class="icon-file-excel position-right"></i></a>
</div>


<script type="text/javascript">
    setPageLocation("firedetector/nosignaldetector", "72시간(주기보고)을 초과하는 동안 신호가 없는 감지기 목록 조회");

    var pageParam = {
    		MARKET_CODE_MAP: ${ MARKET_CODE_MAP },
    		FIRE_DETECTOR_STATUS : ${FIRE_DETECTOR_STATUS},
    		MNG_AREA_SEQ: '${ me.mngAreaSeq }',
    		ME_MARKET_SEQ: '${ me.marketSeq }'
	};

 </script>
<script type="text/javascript" src="${contextPath}/app/firedetector/nosignaldetector/nosignaldetector.js"></script>