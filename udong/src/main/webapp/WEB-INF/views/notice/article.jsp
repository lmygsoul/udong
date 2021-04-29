<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>spring</title>
<jsp:include page="/WEB-INF/views/layout/staticHeader.jsp"/>

<script type="text/javascript">
<c:if test="${sessionScope.member.userId==dto.userId || sessionScope.member.userId=='admin'}">
function deleteNotice(num) {
    var query = "num="+num+"&${query}";
    var url = "${pageContext.request.contextPath}/notice/delete.do?" + query;
    if(confirm("게시물을 삭제하시겠습니까 ? ")) {
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
            <button type="button" class="btnArticle" onclick="javascript:location.href='${pageContext.request.contextPath}/notice/list.do';">
            > 공지사항</button>
        </div>
        
        <div>
			<table style="width: 100%; margin: 0 auto; border-spacing: 0px; border-collapse: collapse;">
			<tr height="35" class="row-3">
			    <td colspan="2" align="left" class="col-1">
				   ${dto.subject}
			    </td>
			</tr>
			
			<tr height="35" class="row-2">
			    <td width="50%" align="left" class="col-2">
			       이름 : ${dto.userName}
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
			    <td colspan="2" align="left" class="col-3" style="position: relative;">
			       첨&nbsp;&nbsp;부 :
		           <c:if test="${not empty dto.saveFilename}">
		                   ${dto.originalFilename}
		                    (<fmt:formatNumber value="${dto.fileSize/1024}" pattern="0.00"/> kb)
		                   <a href="${pageContext.request.contextPath}/notice/download.do?num=${dto.num}">
		                   		<img src="${pageContext.request.contextPath}/resource/images/download_icon.png" style="height: 22px;position: absolute;left: 253px;top: 8px;border: 1px solid #bbb;border-radius: 4px;">
		                   </a>
		           </c:if>
			    </td>
			</tr>
			
			<tr height="35" class="row-2">
			    <td colspan="2" align="left" class="col-3">
			       이전글 : 
					<c:if test="${not empty preReadDto}">
						<a href="${pageContext.request.contextPath}/notice/article.do?num=${preReadDto.num}&${query}">${preReadDto.subject}</a>
					</c:if>
			    </td>
			</tr>
			
			<tr height="35" class="row-2">
			    <td colspan="2" align="left" class="col-3">
			       다음글 :
			       	<c:if test="${not empty nextReadDto}">
						<a href="${pageContext.request.contextPath}/notice/article.do?num=${nextReadDto.num}&${query}">${nextReadDto.subject}</a>
					</c:if>
			    </td>
			</tr>
			
			<tr height="45">
			    <td class="col-4">
			    	<c:if test="${dto.userId == sessionScope.member.userId || sessionScope.member.userId=='admin'}">
			    		<button type="button" class="btn" onclick="javascript:location.href='${pageContext.request.contextPath}/notice/update.do?num=${dto.num}&page=${page}';">수정</button>
				        <button type="button" class="btn" onclick="deleteNotice('${dto.num}');">삭제</button>
			    	</c:if>
			    </td>
			
			   <td align="right" class="col-4">
			        <button type="button" class="btn btnCreate" onclick="javascript:location.href='${pageContext.request.contextPath}/notice/list.do?${query}';">리스트</button>
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