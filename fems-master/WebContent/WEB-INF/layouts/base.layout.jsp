<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="org.joda.time.DateTime"%>
<%@page import="org.slf4j.LoggerFactory"%>
<%@page import="org.slf4j.Logger"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="s"%>
<%@taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<%@taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<!DOCTYPE html>
<html lang="ko">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge" />
	<meta name="viewport" content="width=device-width, initial-scale=0.7"/>
	<tiles:insertAttribute name="globalheader"/>
</head>
<body>

	<tiles:insertAttribute name="topmenu"/>

	<!-- Page container -->
	<div class="page-container">
		<!-- Page content -->
		<div class="page-content">
			<tiles:insertAttribute name="leftmenu" />
			<!-- Main content -->
			<div class="content-wrapper">

				<!-- Page Title & Breadcrumb -->
				<tiles:insertAttribute name="pagetitle"/>
				<!-- /Page Title & Breadcrumb -->

				<!-- Content area -->
				<div class="content" id="contentArea">
					<tiles:insertAttribute name="body"/>
				</div>
				<!-- /Content area -->
			</div>
		</div>
	</div>

	<tiles:insertAttribute name="footer"/>
</body>
</html>
