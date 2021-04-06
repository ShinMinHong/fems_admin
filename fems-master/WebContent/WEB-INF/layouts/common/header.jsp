<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/layouts/include.jsp" %>

<!-- HEADER START ======================================================= -->
<!-- 여기에 공통 헤더 추가 -->
<title>지능형 화재감지 시스템</title>
<link rel="shortcut icon" href="${contextPath}/images/ico/favicon.ico">

<!-- Limitless stylesheets -->
<link href="${contextPath}/lib/limitless/css/fonts.css" rel="stylesheet" type="text/css">
<link href="${contextPath}/lib/limitless/css/icons/fontawesome/styles.min.css" rel="stylesheet" type="text/css">
<link href="${contextPath}/lib/limitless/css/icons/icomoon/styles.css" rel="stylesheet" type="text/css">
<link href="${contextPath}/lib/limitless/css/bootstrap.css" rel="stylesheet" type="text/css">
<link href="${contextPath}/lib/limitless/css/core.css" rel="stylesheet" type="text/css">
<link href="${contextPath}/lib/limitless/css/components.css" rel="stylesheet" type="text/css">
<link href="${contextPath}/lib/limitless/css/colors.css" rel="stylesheet" type="text/css">
<!-- /Limitless stylesheets -->

<!-- KendoUI stylesheets -->
<link href="${contextPath}/lib/kendoui/styles/kendo.common.min.css" rel="stylesheet" type="text/css">
<link href="${contextPath}/lib/kendoui/styles/kendo.bootstrap.min.css" rel="stylesheet" type="text/css">
<!-- //KendoUI stylesheets -->

<link href="${contextPath}/lib/select2-3.5.4/select2.css" rel="stylesheet" type="text/css">
<link href="${contextPath}/css/common.css" rel="stylesheet" type="text/css">

<script type="text/javascript" src="${contextPath}/lib/jquery-3.5.1.js"></script>
<script type="text/javascript" src="${contextPath}/lib/jquery-migrate-3.5.1.js"></script>
<%-- <script type="text/javascript" src="${contextPath}/lib/jquery-migrate-1.4.1.js"></script> --%>
<script type="text/javascript" src="${contextPath}/lib/jquery.ba-bbq.min.js"></script>
<script type="text/javascript" src="${contextPath}/lib/globalize-1.0.0/globalize.js"></script>
<script type="text/javascript" src="${contextPath}/lib/jquery-validation-1.19.3/jquery.validate.min.js"></script>
<script type="text/javascript" src="${contextPath}/lib/jquery-validation-1.19.3/additional-methods.min.js"></script>
<script type="text/javascript" src="${contextPath}/lib/jquery-validation-1.19.3/localization/messages_ko.min.js"></script>
<script type="text/javascript" src="${contextPath}/lib/jquery.blockUI.js"></script>
<script type="text/javascript" src="${contextPath}/lib/jquery.fileDownload.js"></script>
<script type="text/javascript" src="${contextPath}/lib/jquery.iframe-transport.js"></script>
<script type="text/javascript" src="${contextPath}/lib/jquery.modal.min.js"></script>
<script type="text/javascript" src="${contextPath}/lib/jquery.numeric.min.js"></script>
<script type="text/javascript" src="${contextPath}/lib/jquery.scrollTo.js"></script>
<script type="text/javascript" src="${contextPath}/lib/jquery.scrollTo.min.js"></script>
<script type="text/javascript" src="${contextPath}/lib/js.cookie-1.5.1.min.js"></script>
<script type="text/javascript" src="${contextPath}/lib/jquery.querystring.js"></script>
<script type="text/javascript" src="${contextPath}/lib/select2-3.5.4/select2.min.js"></script>
<script type="text/javascript" src="${contextPath}/lib/select2-3.5.4/select2_locale_ko.js"></script>
<script type="text/javascript" src="${contextPath}/lib/underscore-min.js"></script>
<script type="text/javascript" src="${contextPath}/lib/underscore.string.min.js"></script>
<script type="text/javascript" src="${contextPath}/lib/moment.js"></script>
<script type="text/javascript" src="${contextPath}/lib/knockout-3.3.0.js"></script>
<script type="text/javascript" src="${contextPath}/lib/knockout-postbox.min.js"></script>

<!-- KendoUI -->
<script type="text/javascript" src="${contextPath}/lib/kendoui/js/kendo.all.min.js"></script>
<script type="text/javascript" src="${contextPath}/lib/kendoui/js/cultures/kendo.culture.ko-KR.min.js"></script>
<!-- //KendoUI -->

<!-- Common JS Module -->
<script type="text/javascript" src="${contextPath}/app/able.extend.underscore.js"></script>
<script type="text/javascript" src="${contextPath}/app/able.extend.knockout.js"></script>
<script type="text/javascript" src="${contextPath}/app/able.extend.jquery.validate.js"></script>
<script type="text/javascript" src="${contextPath}/app/able.vmutil.js"></script>
<script type="text/javascript" src="${contextPath}/app/able.vmextender.js"></script>
<script type="text/javascript" src="${contextPath}/app/able.ezutil.js"></script>
<script type="text/javascript" src="${contextPath}/app/appconfig.js"></script>

<script type="text/javascript" src="${contextPath}/app/able.kendogrid.vm.js"></script>

<!-- Limitless -->
<script type="text/javascript" src="${contextPath}/lib/limitless/js/core/libraries/bootstrap.js"></script>
<script type="text/javascript" src="${contextPath}/lib/limitless/js/core/libraries/jquery_ui/widgets.min.js"></script>
<script type="text/javascript" src="${contextPath}/lib/limitless/js/core/libraries/jquery_ui/effects.min.js"></script>
<script type="text/javascript" src="${contextPath}/lib/limitless/js/core/app.js"></script>