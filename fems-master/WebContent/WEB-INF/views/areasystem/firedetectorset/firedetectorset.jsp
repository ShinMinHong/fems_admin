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
					<label class="control-label text-right">감지기상태</label>
 					<select class="form-control input-xs" data-bind="options: $root.fireDetectorStatus, optionsText:'text', optionsValue:'value', value: searchFireDetectorStatus, optionsCaption: '전체'"></select>
				</div>
				<div class="col-md-3">
					<label class="control-label text-right">CTN</label>
					<input type="text" class="form-control input-xs" placeholder="CTN" data-bind="value: searchCtnNo">
				</div>
			</div>
			<div class="form-group">
			 	<div class="col-md-3">
					<label class="control-label text-right">모델번호</label>
					<input type="text" class="form-control input-xs" placeholder="모델번호" data-bind="value: searchModelNo">
				</div>
				<div class="col-md-3">
					<label class="control-label text-right">제조번호</label>
					<input type="text" class="form-control input-xs" placeholder="제조번호" data-bind="value: searchProductNo">
				</div>
				<div class="col-md-3">
					<label class="control-label text-right">정렬순서</label>
 					<select class="form-control input-xs" data-bind="options: $root.fireDetectorSortType, optionsText:'text', optionsValue:'value', value: searchSort, optionsCaption: '등록일'"></select>
				</div>
				<div class="col-md-3">
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
	<a href="#" class="btn bg-teal btn-xs" data-bind="click: excelDownload" >Excel Download<i class="icon-file-excel position-right"></i></a>
	<sec:authorize access="hasRole('ROLE_FIRE_DETECTOR_MNG')">
	<a href="#" class="btn bg-teal btn-xs" data-bind="click: openExcelUpload" >화재감지기 엑셀 등록<i class="icon-file-excel position-right"></i></a>
	</sec:authorize>
</div>

<!-- 추가  -->
<div class="text-right" style="padding-top: 10px; height: 32px;">
	<sec:authorize access="hasRole('ROLE_FIRE_DETECTOR_MNG')">
	<button type="button" data-bind="click: onCreateClick" class="btn btn-primary btn-xs"><b><i class="icon-plus3"></i></b> 화재감지기 등록</button>
	</sec:authorize>
</div>
<br/>

<!-- 상세  -->
<form id="detailsForm" class="form-horizontal" action="#" data-bind="visible: detailsVM.isVisible">
	<div class="panel panel-flat">
		<div class="panel-heading">
			<h5 class="panel-title"><i class="icon-checkmark3 position-left"></i> 상세정보</h5>
			<div class="heading-elements">
				<ul class="icons-list">
					<li><a data-action="collapse"></a></li>
				</ul>
			</div>
			<a class="heading-elements-toggle"><i class="icon-menu"></i></a>
		</div>
		<div class="panel-body" data-bind="with: detailsVM">
			<div class="row" data-bind="with: data">
				<fieldset>
					<legend class="text-semibold" style="height:30px;">
						<span class="col-md-8"><i class="icon-lock5 position-left"></i> 화재감지기 관리 상세정보</span>
					</legend>
					<div class="col-md-6" >

						<div class="form-group">
							<label class="col-md-4 control-label text-right" for="fireDetectorSeq">고유번호</label>
							<div class="col-md-8">
								<input type="text" class="form-control input-xs" name="fireDetectorSeq" data-bind="value: fireDetectorSeq, enable:false" />
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-4 control-label text-right" for="mngAreaName">관제지역명</label>
							<div class="col-md-8">
								<input id="mngAreaName" type="text" class="form-control input-xs" name="mngAreaName" data-bind="value: mngAreaName, enable:false" />
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-4 control-label text-right" for="marketName">관리지역명</label>
							<div class="col-md-8">
								<input id="marketName" type="text" class="form-control input-xs" name="marketName" data-bind="value: marketName, enable:false" />
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-4 control-label text-right" for="storeName">점포명</label>
							<div class="col-md-8">
								<div class="col-md-7 nopadding">
				                	<input id="storeName" type="text" class="form-control input-xs" name="storeName" data-bind="value: storeName" readonly="readonly"/>
			                	</div>
			                	<div class="col-md-5 nopadding text-right">
			                		<button type="button" id="showSelectStoreModal" data-bind="click: $parent.showSelectStoreModal" class="btn bg-teal-400 btn-xs"><b><i class="icon-search4"></i></b> 점포선택</button>
			                	</div>
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-4 control-label text-right" for="modelNo">모델번호</label>
							<div class="col-md-8">
								<input id="modelNo" type="text" class="form-control input-xs" name="modelNo" data-bind="value: modelNo" />
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-4 control-label text-right" for="productNo">제조번호</label>
							<div class="col-md-8">
								<input id="productNo" type="text" class="form-control input-xs" name="productNo" data-bind="value: productNo" />
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-4 control-label text-right" for="ctnNo">CTN<span class="text-danger">*</span></label>
							<div class="col-md-8">
								<input id="ctnNo" type="text" class="form-control input-xs" name="ctnNo" data-bind="value: ctnNo, enable:$parent.isCreateMode()" />
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-4 control-label text-right" for="serialNo">단말기 일련번호<span class="text-danger">*</span></label>
							<div class="col-md-8">
								<input id="serialNo" type="text" class="form-control input-xs" name="serialNo" data-bind="value: serialNo" />
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-4 control-label text-right" for="usimNo">유심번호<span class="text-danger">*</span></label>
							<div class="col-md-8">
								<input id="usimNo" type="text" class="form-control input-xs" name="usimNo" data-bind="value: usimNo" />
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-4 control-label text-right" for="fireDetectorName">화재감지기명</label>
							<div class="col-md-8">
								<input id="fireDetectorName" type="text" class="form-control input-xs" name="fireDetectorName" data-bind="value: fireDetectorName" />
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-4 control-label text-right" for="smsAddMessage">SMS 추가메시지</label>
							<div class="col-md-8">
								<input id="smsAddMessage" type="text" class="form-control input-xs" name="smsAddMessage" data-bind="value: smsAddMessage" />
							</div>
						</div>
					</div>
					<div class="col-md-6">
						<div class="form-group">
							<label class="col-md-4 control-label text-right" for="zipCode">우편번호
								<!-- ko if:storeName()  -->
								<span class="text-danger">*</span>
								<!-- /ko -->
							</label>
							<div class="col-md-4">
				                <input id="zipCode" type="text" class="form-control input-xs" name="zipCode" data-bind="value: zipCode" disabled="disabled"/>
							</div>
							<div class="col-md-4">
								<button type="button" data-bind="click: $parent.clickSearchAddress" class="btn bg-teal-400 btn-xs"><b><i class="icon-home4"></i></b> 주소검색</button>
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-4 control-label text-right" for="roadAddress">주소(도로명)
								<!-- ko if:storeName()  -->
								<span class="text-danger">*</span>
								<!-- /ko -->
							</label>
							<div class="col-md-8">
								<input id="roadAddress" type="text" class="form-control input-xs" name="roadAddress" data-bind="value: roadAddress" disabled="disabled" />
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-4 control-label text-right" for="parcelAddress">주소(지번)
								<!-- ko if:storeName()  -->
								<span class="text-danger">*</span>
								<!-- /ko -->
							</label>
							<div class="col-md-8">
								<input id="parcelAddress" type="text" class="form-control input-xs" name="parcelAddress" data-bind="value: parcelAddress" disabled="disabled"/>
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-4 control-label text-right" for="latitude">위도(중심좌표)
								<!-- ko if:storeName()  -->
								<span class="text-danger">*</span>
								<!-- /ko -->
							</label>
							<div class="col-md-8">
								<input id="latitude" type="text" class="form-control input-xs" name="latitude" data-bind="value: latitude" disabled="disabled"/>
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-4 control-label text-right" for="longitude">경도(중심좌표)
								<!-- ko if:storeName()  -->
								<span class="text-danger">*</span>
								<!-- /ko -->
							</label>
							<div class="col-md-8">
								<input id="longitude" type="text" class="form-control input-xs" name="longitude" data-bind="value: longitude" disabled="disabled"/>
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-4 control-label text-right" for="installPlace">설치위치
							<!-- ko if:storeName()  -->
							<span class="text-danger">*</span>
							<!-- /ko -->
							</label>
							<div class="col-md-8">
								<input type="text" class="form-control input-xs" name="installPlace" data-bind="value: installPlace, enable:storeName()"  />
							</div>
						</div>
						<div class="form-group">
							<label for="roleName" class="col-md-4 control-label text-right">첨부파일</label>
							<div class="col-md-8">
								<table class="q-table">
									<tbody>
										<tr>
											<td>
												<span class="input-group">
													<div><label class="plupload_button bg-teal-400" id="btnLocalFileInput" for="attached-file">파일찾기</label></div>
													<div id="areaFileInput"></div>
												</span>
												<span class="desc-txt">첨부파일은 개당 ${FILE_DISPLAY_UPLOAD_MAX_SIZE} 까지 업로드 가능합니다.(첨부파일 최대 ${FILE_MAX_COUNT}개 까지 가능)</span>
												<div id="areaFilePreview" class="gray-box" style="min-height: 60px;"></div>
											</td>
										</tr>
									</tbody>
								</table>
							</div>
						</div>
						<!-- ko if: $parent.isEditMode()  -->
						<div class="form-group">
							<label class="col-md-4 control-label text-right" for="fireDetectorStatus">감지기상태<span class="text-danger"></span></label>
							<div class="col-md-8">
			 					<select class="form-control input-xs" data-bind="options: $root.fireDetectorStatus, optionsText:'text', optionsValue:'value', value: fireDetectorStatus" disabled="disabled" ></select>
							</div>
						</div>
						<!-- /ko -->
						<div class="form-group">
							<label class="col-md-4 control-label text-right" for="fireDetectorAckValue">감지기ACK<span class="text-danger">*</span></label>
							<div class="col-md-8">
								<select class="form-control input-xs" data-bind="options: $root.fireDetectorAckValue, optionsText:'text', optionsValue:'value', value: fireDetectorAckValue"></select>
							</div>
						</div>
					</div>
					<!-- ko if: $parent.isEditMode()  -->
					<div class="col-md-6">
						<div class="form-group">
							<label class="col-md-4 control-label text-right" for="regAdminId">등록자</label>
							<div class="col-md-8">
								<input type="text" class="form-control input-xs" name="regAdminId" data-bind="value: regAdminId" disabled="disabled"/>
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-4 control-label text-right" for="regDate">등록일</label>
							<div class="col-md-8">
								<input type="text" class="form-control input-xs" name="regDate" data-bind="momentISO: regDate " disabled="disabled"/>
							</div>
						</div>
					</div>
					<div class="col-md-6">
						<div class="form-group">
							<label class="col-md-4 control-label text-right" for="updAdminId">수정자</label>
							<div class="col-md-8">
								<input type="text" class="form-control input-xs" name="updAdminId" data-bind="value: updAdminId" disabled="disabled"/>
							</div>
						</div>
						<div class="form-group">
							<label class="col-md-4 control-label text-right" for="updDate">최종수정일</label>
							<div class="col-md-8">
								<input type="text" class="form-control input-xs" name="updDate" data-bind="momentISO: updDate" disabled="disabled"/>
							</div>
						</div>
					</div>
					<!-- /ko -->
				</fieldset>
			</div>
			<div class="text-right" data-bind="visible: isCreateMode">
				<button type="button" data-bind="click: cancel" class="btn btn-xs"><b><i class="icon-cross"></i></b> 닫기</button>
				<sec:authorize access="hasRole('ROLE_FIRE_DETECTOR_MNG')">
				<button type="button" data-bind="click: insertData" class="btn btn-primary btn-xs"><b><i class="icon-checkmark3"></i></b> 등록</button>
				</sec:authorize>
			</div>
			<div class="text-right" data-bind="visible: isEditMode">
				<button type="button" data-bind="click: cancel" class="btn btn-xs"><b><i class="icon-cross"></i></b> 닫기</button>
				<sec:authorize access="hasRole('ROLE_FIRE_DETECTOR_MNG')">
				<button type="button" data-bind="click: disconnectionWithStore" class="btn btn-primary btn-xs"><b><i class="icon-checkmark3"></i></b> 점포선택해제</button>
				<button type="button" data-bind="click: updateData" class="btn btn-primary btn-xs"><b><i class="icon-checkmark3"></i></b> 수정</button>
				<button type="button" data-bind="click: deleteData" class="btn btn-warning btn-xs"><b><i class="icon-minus3"></i></b> 삭제</button>
				</sec:authorize>
			</div>
			<br/>
			<br/>
			<!-- 수신기 변경이력  -->
			<div class="col-md-12" data-bind="visible: isEditMode">
				<div class="panel-group panel-group-control panel-group-control-right" id="accordion-control-fireDetectorNowStatusListGrid">
					<div class="panel panel-white">
						<div class="panel-heading">
							<span class="col-md-8 text-semibold">화재감지기 현재상태</span>
						</div>
						<div class="panel-body">
							<div id=fireDetectorNowStatusHist></div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</form>

<!-- Iframe 팝업 -->
<div>
	<div id="selectStoreModal" title="점포명선택" style="overflow:hidden !important;"></div>
	<div id="selectLocationModal" title="주소검색" style="overflow:hidden !important"></div>
	<div id="excelUploadModal" title="화재감지기 엑셀 대량 업로드" style="overflow:hidden !important"></div>
</div>
<!-- /Iframe 팝업 -->

<script type="text/javascript">
    setPageLocation("areasystem/firedetectorset", "화재감지기 관리");

    var pageParam = {
     	MARKET_CODE_MAP : ${MARKET_CODE_MAP},
     	AREA_CODE_MAP : ${AREA_CODE_MAP},
     	ME_MARKET_SEQ: '${me.marketSeq}',
     	ME_MNG_AREA_SEQ : '${me.mngAreaSeq}',
     	ME_ROLEGROUP_NAME : '${me.rolegroupCode}',
		FIRE_DETECTOR_STATUS: ${FIRE_DETECTOR_STATUS},
		FIRE_DETECTOR_ACK_VALUE: ${FIRE_DETECTOR_ACK_VALUE},
		FIRE_DETECTOR_SORT_TYPE: ${FIRE_DETECTOR_SORT_TYPE},
     	fileUploadSize : ${FILE_UPLOAD_MAX_SIZE},
		fileDisplayUploadSize : "${FILE_DISPLAY_UPLOAD_MAX_SIZE}",
		ALLOWED_EXTENSION : "${ALLOWED_EXTENSION}" ,
		file_maxCnt : ${FILE_MAX_COUNT}
    }

 </script>

<!-- 카카오맵 지도 SDK -->
<script src="https://t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
<!-- //카카오맵 지도 SDK -->

<script type="text/javascript" src="${contextPath}/app/areasystem/firedetectorset/firedetectorset.js"></script>