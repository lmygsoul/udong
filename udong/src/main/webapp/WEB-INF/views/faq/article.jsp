<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>FAQ</title>
<jsp:include page="/WEB-INF/views/layout/staticHeader.jsp"/>

<script type="text/javascript">
// 관리자이거나 글 작성자가 아니면, 스크립트가 보이지 않게.
<c:if test="${dto.userId == sessionScope.member.userId || sessionScope.member.userId == 'admin'}">
function deleteFaq(num) {
    var query = "num="+num+"&${query}";
    var url = "${pageContext.request.contextPath}/faq/delete.do?" + query;
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
        
        <div>
			<table style="width: 100%; margin: 0 auto; border-spacing: 0px; border-collapse: collapse;">
			<tr height="35" class="row-2">
			    <td colspan="2" align="left" class="col-1" style="padding-bottom: 20px;">
				   ${dto.subject}
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
						<a href="${pageContext.request.contextPath}/faq/article.do?num=${preReadDto.num}&${query}">${preReadDto.subject}</a>
					</c:if>
			    </td>
			</tr>
			
			<tr height="35" class="row-2">
			    <td colspan="2" align="left" class="col-3">
			       다음글 :
					<c:if test="${not empty nextReadDto}">
						<a href="${pageContext.request.contextPath}/faq/article.do?num=${nextReadDto.num}&${query}">${nextReadDto.subject}</a>
					</c:if>
			    </td>
			</tr>
			<tr height="45">
			    <td class="col-4">
			    	<c:if test="${dto.userId == sessionScope.member.userId || sessionScope.member.userId=='admin'}">
			    		<button type="button" class="btn" style="margin-right: 3px;" onclick="javascript:location.href='${pageContext.request.contextPath}/faq/update.do?num=${dto.num}&page=${page}';">수정</button>
				        <button type="button" class="btn" onclick="deleteFaq('${dto.num}');">삭제</button>
			    	</c:if>
			    </td>
			
			    <td align="right" class="col-4">
			        <button type="button" class="btn btnList" onclick="javascript:location.href='${pageContext.request.contextPath}/faq/list.do?${query}';">리스트</button>
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