<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>${title }</title>
<jsp:include page="/WEB-INF/views/layout/staticHeader.jsp"/>

<script type="text/javascript">
// 관리자이거나 글 작성자가 아니면, 스크립트가 보이지 않게.
<c:if test="${mdto.receiveUser == sessionScope.member.userId || sessionScope.member.type == '0'}">
function deleteBoard(num) {
	if(confirm("게시물을 삭제 하시겠습니까 ?")) {
		var url="${pageContext.request.contextPath}/member/rm_delete.do?num="+num+"&${query}";
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
        <div>
			<table style="width: 100%; margin: 0 auto; border-spacing: 0px; border-collapse: collapse;">
			<tr height="35" class="row-3">
			    <td colspan="2" align="left" class="col-1">
				   ${mdto.subject}
			    </td>
			</tr>
			
			<tr height="35" class="row-2">
			    <td width="50%" align="left" class="col-2">
			       보낸사람 : ${mdto.sendUser}
			    </td>
			    <td width="50%" align="right" class="col-2">
			        ${mdto.sendTime} 
			    </td>
			</tr>
			
			<tr class="row-2">
			  <td colspan="2" align="left" class="artiBox" valign="top" height="200">
			      ${mdto.content}
			   </td>
			</tr>
			
			<tr height="35" class="row-2">
			    <td colspan="2" align="left" class="col-3">
			       이전쪽지 : 
					<c:if test="${not empty preReadSM}">
						<a href="${pageContext.request.contextPath}/member/rm_article.do?num=${preReadSM.pageNum}&${query}">${preReadSM.subject}</a>
					</c:if>
			    </td>
			</tr>
			
			<tr height="35" class="row-2">
			    <td colspan="2" align="left" class="col-3">
			       다음쪽지 :
					<c:if test="${not empty nextReadSM}">
						<a href="${pageContext.request.contextPath}/member/rm_article.do?num=${nextReadSM.pageNum}&${query}">${nextReadSM.subject}</a>
					</c:if>
			    </td>
			</tr>
			<tr height="45">
			    <td class="col-4">			    	
			    	<c:choose>
			    		<c:when test="${mdto.receiveUser == sessionScope.member.userId || sessionScope.member.type == '0'}">
				        	<button type="button" class="btn" onclick="deleteBoard('${mdto.pageNum}');">삭제</button>
			    		</c:when>
			    	</c:choose>
			    </td>
			    <td align="right" class="col-4">
			        <button type="button" class="btn btnList" onclick="javascript:location.href='${pageContext.request.contextPath}/member/rm_list.do?${query}';">리스트</button>
			    	<button type="button" class="btn btnCreate" onclick="javascript:location.href='${pageContext.request.contextPath}/member/sm_created.do';"><i class="fab fa-telegram-plane"></i>&nbsp;&nbsp;답장하기</button>
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