<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/layouts/include.jsp" %>
<span style="color:red;font-size:20px;text-align:center;">지도에 마우스를 클릭하여 정확한 위치를 지정해 주세요.</span>
<div id="map" style="width:500px;height:450px;margin-top:10px;padding-top: 5px;display:none;"></div>
<div class="text-right" style="padding-top: 5px;">
	<button type="button" data-bind="click: onSelectClick" class="btn btn-primary btn-xs"><b><i class="icon-plus3"></i></b> 해당 위치 좌표로 추가</button>
</div>

<script type="text/javascript" src="${contextPath}/app/commonpopup/openselectlocation.js"></script>
<!-- 카카오맵 지도 SDK -->
<script type="text/javascript" src="//dapi.kakao.com/v2/maps/sdk.js?appkey=7b2ae4fd6565f66569df474406353e0f&libraries=services"></script>
<!-- //카카오맵 지도 SDK -->

