<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/layouts/include.jsp" %>

 <div class="panel panel-flat">
	<div class="panel-heading">
		<div style="margin-top:10px;" class="row">
			<span class="col-md-3">엑셀 파일선택</span>
			<input type="file" id="uploadExcelFile" name="uploadExcelFile" class="col-md-9" style="display:inline;width:350px;">
		</div>
		<div style="margin-top:10px;" class="row">
			<div class="col-md-12">
						<ol>
							<li> 관리지역 점포정보를 대량등록 합니다.</li>
							<li> [샘플엑셀]을 다운로드 합니다.</li>
							<li> 다운받은 엑셀에 등록하려는 정보 입력합니다.</li>
							<li> 관리지역고유번호,점포명,책임자명, 휴대폰번호,우편번호,도로명주소,지번주소는 필수 입력사항입니다.</li>
							<li> SMS알림 및 119알림은 Y,N 형식으로 입력해주세요. 미입력시 Y 값으로 자동입력 됩니다.</li>
							<li> 입력한 엑셀파일을 ‘등록‘ 버튼을 눌러 업로드 합니다.</li>
							<li> 유효성체크를 통해 실패건이 없으면 정상적으로 반영됩니다.</li>
						</ol>
			</div>
		</div>
		<div class="text-center" style="margin-top:10px;" class="row">
				<button type="button" data-bind="click: excelUpload" class="btn btn-primary btn-xs"><b><i class="icon-checkmark3"></i></b> 등록</button>
				<button type="button" data-bind="click: sampleExcelDownload" class="btn btn-success btn-xs"><b><i class=" icon-download4"></i></b> 샘플엑셀</button>
		</div>
	</div>
</div>

<div class="panel panel-flat paddingbottom0 marginbottom0">
	<div class="panel-heading">
		<span class="text-semibold">업로드 결과</span>
		<div class="heading-elements">
			<ul class="icons-list">
				<li><a data-action="collapse"></a></li>
			</ul>
		</div>
		<a class="heading-elements-toggle"><i class="icon-menu"></i></a>
	</div>
	<div class="panel-body" data-bind="with:excelResultVM">
		<fieldset>
			<div class="col-md-12" data-bind="with:summaryObj">
                   <div class="form-group">
                   	<span class="control-label text-left text-info" style="margin-left: 20px;"  data-bind="text:totalMsg"></span><br/>
                   	<span class="control-label text-left" style="margin-left: 20px;" data-bind="text:totalCnt"></span>
					<span class="control-label text-left" style="margin-left: 10px;" data-bind="text:successCnt"></span>
					<span class="control-label text-left" style="margin-left: 10px;" data-bind="text:errorCnt"></span>
				</div>
			</div>
			<div class="col-md-12 paddingbottom10" data-bind="with:searchData" id ="excelResultSearchPanel">
				<div class="form-group">
					<label class="control-label col-md-2 text-right" style="margin-top:6px;">상태</label>
					<div class="col-md-4">
						<select id="searchResultCode" class="form-control input-xs" name="searchResultCode" data-bind="options: $parent.searchResultSelectOptions, optionsText:'text', optionsValue:'value', value: searchResultCode, optionsCaption: '전체'"></select>
					</div>
					<div class="col-md-6">
						<div class="text-right">
							<button type="button" class="btn btn-primary" data-bind="click: $parent.search">검색<i class="glyphicon glyphicon-search position-right"></i></button>
						</div>
					</div>
				</div>
			</div>
		</fieldset>
		<div id="excelResultGrid"></div>
	</div>
</div>
<script type="text/javascript" src="${contextPath}/app/commonpopup/openstoreexcelupload.js"></script>