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
                <div class="col-md-3">
                    <label class="control-label text-right">관리지역명</label>
                    <input type="text" class="form-control input-xs" placeholder="관리지역명" data-bind="value: searchMngAreaName">
                </div>
            </div>
        </form>
    </div>
</div>

<!-- 목록  -->
<div id="grid"></div>

<script type="text/javascript">
var pageParam = {
			//APP_USER_GRADE: ${APP_USER_GRADE},
			//FIRE_STATION_ALL_CODE_MAP: ${FIRE_STATION_ALL_CODE_MAP}
	}
</script>

<script type="text/javascript" src="${contextPath}/app/commonpopup/openselectmngarea.js"></script>
