<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/layouts/include.jsp" %>

<!-- 검색창  -->
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
            <div class="form-group">
   				<div class="col-sm-3">
					<label class="control-label text-right">관리지역</label>
					<select class="form-control input-xs" data-bind="options: $root.marketSelectOptions, optionsText:'text', optionsValue:'value', value: searchMarketSeq"></select>
				</div>
          		<div class="col-sm-3">
					<label class="control-label text-right">점포명</label>
					<input type="text" class="form-control input-xs" placeholder="점포명" data-bind="value: searchStoreName">
				</div>
				<div class="col-sm-6">
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

<div class="text-right" style="padding-top: 10px;">
	<button type="button" data-bind="click: onSelectClick" class="btn btn-primary btn-xs"><b><i class="icon-plus3"></i></b> 선택</button>
</div>

<script type="text/javascript">
    var pageParam = {
     	MARKET_CODE_MAP : ${MARKET_CODE_MAP},
     	MNG_AREA_SEQ : '${MNG_AREA_SEQ}',
     	ME_MARKET_SEQ: '${me.marketSeq}'
    }
 </script>


<script type="text/javascript" src="${contextPath}/app/commonpopup/openselectstore.js"></script>