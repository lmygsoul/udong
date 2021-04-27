<%@ page contentType="text/html; charset=UTF-8" %>
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
<c:if test="${dto.userId == sessionScope.member.userId || sessionScope.member.type == '0'}">
function deleteBoard(boardNum) {
	if(confirm("게시물을 삭제 하시겠습니까 ?")) {
		var url="${pageContext.request.contextPath}/qa/delete.do?boardNum="+boardNum+"&${query}";
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
        <div class="body-title" style="margin: 0;">
            <h3><span style="font-family: Webdings">4</span> 우동지식 </h3>
        </div>
        
        <div>
			<table style="width: 100%; margin: 0 auto; border-spacing: 0px; border-collapse: collapse;">
			<tr height="35" class="row-3">
			    <td colspan="2" align="left" class="col-1">
				   <c:if test="${dto.depth!=0 }">[Re] </c:if>
				   ${dto.subject}
			    </td>
			</tr>
			
			<tr height="35" class="row-2">
			    <td width="50%" align="left" class="col-2">
			       이름 : ${dto.nickName}
			    </td>
			    <td width="50%" align="right" class="col-2">
			        ${dto.created} | 조회 ${dto.hitCount}
			    </td>
			</tr>
			
			<tr class="row-2">
			  <td colspan="2" align="left" class="artiBox" valign="top" height="200">
			      ${dto.content}
			   </td>
			</tr>
			
			<tr height="35" class="row-2">
			    <td colspan="2" align="left" class="col-3">
			       이전글 : 
					<c:if test="${not empty preReadDto}">
						<a href="${pageContext.request.contextPath}/qa/article.do?boardNum=${preReadDto.boardNum}&${query}">${preReadDto.subject}</a>
					</c:if>
			    </td>
			</tr>
			
			<tr height="35" class="row-2">
			    <td colspan="2" align="left" class="col-3">
			       다음글 :
					<c:if test="${not empty nextReadDto}">
						<a href="${pageContext.request.contextPath}/qa/article.do?boardNnum=${nextReadDto.boardNum}&${query}">${nextReadDto.subject}</a>
					</c:if>
			    </td>
			</tr>
			<tr height="45">
			    <td class="col-4">
			    	<button type="button" class="btn" onclick="javascript:location.href='${pageContext.request.contextPath}/qa/reply.do?boardNum=${dto.boardNum}&page=${page}';">답변</button>
			    	<c:choose>
			    		<c:when test="${dto.userId == sessionScope.member.userId}">
			          		<button type="button" class="btn" style="margin-right: 3px;" onclick="javascript:location.href='${pageContext.request.contextPath}/qa/update.do?boardNum=${dto.boardNum}&${query}';">수정</button>
			    		</c:when>
			    		<c:otherwise>
			    			<button type="button" class="btn" disabled="disabled">수정</button>
			    		</c:otherwise>
			    	</c:choose>
			    	
			    	<c:choose>
			    		<c:when test="${dto.userId == sessionScope.member.userId || sessionScope.member.userId=='admin'}">
				        	<button type="button" class="btn" onclick="deleteBoard('${dto.boardNum}');">삭제</button>
			    		</c:when>
			    		<c:otherwise>
			    			<button type="button" class="btn" disabled="disabled">삭제</button>
			    		</c:otherwise>
			    	</c:choose>
			    	
			    </td>
			
			    <td align="right" class="col-4">
			        <button type="button" class="btn btnList" onclick="javascript:location.href='${pageContext.request.contextPath}/qa/list.do?${query}';">리스트</button>
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