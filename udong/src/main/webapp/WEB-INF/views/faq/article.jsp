﻿<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>타이틀</title>
<jsp:include page="/WEB-INF/views/layout/staticHeader.jsp"/>

<script type="text/javascript">
// 관리자이거나 글 작성자가 아니면, 스크립트가 보이지 않게.
<c:if test="${dto.userId == sessionScope.member.userId || sessionScope.member.userId == 'admin'}">
function deleteBoard(num) {
	if(confirm("게시물을 삭제 하시겠습니까 ?")) {
		var url="${pageContext.request.contextPath}/faq/delete.do?num="+num+"&${query}";
		location.href=url;
	}
}
</c:if>
</script>
</head>
<body>

<div class="header">
    <jsp:include page="/WEB-INF/views/layout/header.jsp"></jsp:include>
</div>
	
<div class="container">
    <div class="body-container" style="width: 700px;">
        <div class="body-title">
            <h3><span style="font-family: Webdings">2</span> FAQ </h3>
        </div>
        
        <div>
			<table style="width: 100%; margin: 20px auto 0px; border-spacing: 0px; border-collapse: collapse;">
			<tr height="35" class="col-1">
			    <td colspan="2" align="center">
				   ${dto.subject}
			    </td>
			</tr>
			
			<tr height="35" class="col-2">
			    <td width="50%" align="left" style="padding-left: 5px;">
			       이름 : ${dto.userName}
			    </td>
			    <td width="50%" align="right" style="padding-right: 5px;">
			        ${dto.created}
			    </td>
			</tr>
			
			<tr class="col-2">
			  <td colspan="2" align="left" style="padding: 10px 5px;" valign="top" height="200">
			      ${dto.content}
			   </td>
			</tr>
			
			<tr height="35" class="col-2">
			    <td colspan="2" align="left" style="padding-left: 5px;">
			       이전글 : 
					<c:if test="${not empty preReadDto}">
						<a href="${pageContext.request.contextPath}/faq/article.do?num=${preReadDto.num}&${query}">${preReadDto.subject}</a>
					</c:if>
			    </td>
			</tr>
			
			<tr height="35" class="col-2">
			    <td colspan="2" align="left" style="padding-left: 5px;">
			       다음글 :
					<c:if test="${not empty nextReadDto}">
						<a href="${pageContext.request.contextPath}/faq/article.do?num=${nextReadDto.num}&${query}">${nextReadDto.subject}</a>
					</c:if>
			    </td>
			</tr>
			<tr height="45">
			    <td>
			    	<c:choose>
			    		<c:when test="${dto.userId == sessionScope.member.userId}">
			          		<button type="button" class="btn" onclick="javascript:location.href='${pageContext.request.contextPath}/faq/update.do?num=${dto.num}&page=${page}';">수정</button>
			    		</c:when>
			    		<c:otherwise>
			    			<button type="button" class="btn" disabled="disabled">수정</button>
			    		</c:otherwise>
			    	</c:choose>
			    	
			    	<c:choose>
			    		<c:when test="${dto.userId == sessionScope.member.userId || sessionScope.member.userId=='admin'}">
				        	<button type="button" class="btn" onclick="deleteBoard('${dto.num}');">삭제</button>
			    		</c:when>
			    		<c:otherwise>
			    			<button type="button" class="btn" disabled="disabled">삭제</button>
			    		</c:otherwise>
			    	</c:choose>
			    	
			    </td>
			
			    <td align="right">
			        <button type="button" class="btn" onclick="javascript:location.href='${pageContext.request.contextPath}/faq/list.do?${query}';">리스트</button>
			    </td>
			</tr>
			</table>
        </div>

    </div>
</div>

<div class="footer">
    <jsp:include page="/WEB-INF/views/layout/footer.jsp"></jsp:include>
</div>

<jsp:include page="/WEB-INF/views/layout/staticFooter.jsp"/>
</body>
</html>