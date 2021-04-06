<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<div id="cont"  style="width:100%">
	<div class="box cl-float"  style="width:100%">
		<div class="ac mt30" style="width:100%;text-align: center;">
			<img src="${contextPath}/images/ico/error.gif" alt="에러 아이콘" style="width: 128px;" />
			<h2 class="mt10 title type0">
				죄송합니다.<br />요청하신 처리에 오류가 발생했습니다.
			</h2>

			<p class="desc mt20">
					요청하신 페이지의 처리에 오류가 있거나,<br />
					서버에 일시적인 장애로 페이지를 표시할 수 없습니다.<br />
					<br />
					입력하신 주소가 정확한지 다시 한번 확인해 주시거나,<br />
					잠시 후 이용해 주시기 바랍니다.<br />
					<br />
				정상적인 요청인데도 계속해서 문제가 발생하는 경우,<br />
				관리자에게 알려주시면 안정된 서비스 제공을 위해 노력하겠습니다.<br />
				감사합니다.
			</p>
		</div>
		<div class="ac mt30" style="text-align: center;">
			<br /><br /><br />
			<a href="/login" title="메인페이지로"
				class="btn type3 blue prev-ico"> <span class="l-icon"></span> <span
				class="msg">메인페이지로</span> <span class="r"></span>
			</a>
		</div>
	</div>
</div>
