<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>우동사리</title>
<jsp:include page="/WEB-INF/views/layout/staticHeader.jsp"/>
</head>
<body>

	<div class="header">
	    <jsp:include page="/WEB-INF/views/layout/header.jsp"></jsp:include>
	</div>
		
	<div class="container" style="background-color: #FBF7F2; padding: 0;">
	    <div class="main-container">
	    	<div class="main-left">
	    		<ul>
	    		<li>
		    		중고 거래의<br>
		    		모든 것
	    		</li>
	    		<li>
	    			우리 동네 사는 이야기
	    		</li>
	    		</ul>
	    	</div>
	    	<div class="main-right">
	    		<div class="main-img">
		    		<img src="${pageContext.request.contextPath}/resource/images/main_img.png">
	    		</div>
	    	</div>
	    </div>
	</div>
	
	<div class="footer">
	    <jsp:include page="/WEB-INF/views/layout/footer.jsp"></jsp:include>
	</div>
	
<jsp:include page="/WEB-INF/views/layout/staticFooter.jsp"/>

</body>
</html>