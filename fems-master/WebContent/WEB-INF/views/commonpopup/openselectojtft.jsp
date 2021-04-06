<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/layouts/include.jsp" %>

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
		<form class="form-horizontal" role="form">  <!-- sm xs -->
			<div class="form-group">
				<!-- ko if: $root.isGradeHqadmin() -->
				<div class="col-sm-3">
					<label class="control-label text-right">관할서</label>
					<select class="form-control input-xs" data-bind="options: $root.fireStationCodeMapSelectOptions, optionsText:'text', optionsValue:'value', value: searchFrsttCode, optionsCaption: '전체'"></select>
				</div>
				<!-- /ko -->
				<!-- ko if: !$root.isGradeHqadmin() -->
				<div class="col-sm-3">
					<label class="control-label text-right">관할서</label>
					<input type="text" class="form-control input-xs" name="frsttCode" data-bind="value: $root.fireStationCodeMap[me.frsttCode]" disabled="disabled"/>
				</div>
				<!-- /ko -->
				<div class="col-sm-3">
					<label class="control-label text-right">소방대상물명</label>
					<input type="text" class="form-control input-xs" placeholder="소방대상물명" data-bind="value: searchOjtftNm">
				</div>
				<div class="col-sm-3">
					<label class="control-label text-right">주소(지번/도로명)</label>
					<input type="text" class="form-control input-xs" placeholder="주소(지번/도로명)" maxlength="50" data-bind="value: searchAddress">
				</div>
				<div class="col-sm-3">
					<label class="control-label text-right"></label>
					<div class="text-right">
						<button type="button" class="btn btn-primary" data-bind="click: $root.search">검색<i class="glyphicon glyphicon-search position-right"></i></button>
					</div>
				</div>
			</div>
		</form>
	</div>
</div>
<div id="grid"></div>
<div class="text-right" style="padding-top: 10px;">
	<button type="button" data-bind="click: onSelectClick" class="btn btn-primary btn-xs"><b><i class="icon-plus3"></i></b> 선택</button>
</div>

<script type="text/javascript" src="${contextPath}/app/commonpopup/openselectojtft.js"></script>
<script type="text/javascript">
 	var pageParam = {
			FIRE_STATION_CODE_MAP: ${FIRE_STATION_CODE_MAP},
			OJTFT_SEPARATE_CODE_MAP: ${OJTFT_SEPARATE_CODE_MAP},
			MAINPRPOS_CODE_MAP: ${MAINPRPOS_CODE_MAP}
	}
</script>

