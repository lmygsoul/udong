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
function deletePhoto(num) {
    var query = "num="+num+"&page=${page}";
    var url = "${pageContext.request.contextPath}/udongphoto/delete.do?" + query;

    if(confirm("위 자료를 삭제 하시 겠습니까 ? ")) {
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
    <div class="body-container">
        
        <div>
			<table style="width: 100%; margin: 0 auto; border-bottom: 1px solid #E9ECEF; border-spacing: 0px; border-collapse: collapse;">
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
	
			<tr>
			  <td colspan="2" align="left" style="padding: 15px 5px;">
			      <img src="${pageContext.request.contextPath}/uploads/photo/${dto.imageFilename}" style="max-width:100%; height:auto; resize:both;">
			   </td>
			</tr>
						
			<tr>
			  <td colspan="2" align="left" style="padding: 0 5px;" valign="top" height="200">
			      ${dto.content}
			   </td>
			</tr>
			</table>
			
			<table style="width: 100%; margin: 10px auto; border-spacing: 0px;">
			<tr height="45">
			    <td width="300" align="left">
			    	<c:choose>
			    		<c:when test="${sessionScope.member.userId==dto.userId}">
			    			<button type="button" class="btn" onclick="javascript:location.href='${pageContext.request.contextPath}/udongphoto/update.do?num=${dto.num}&page=${page}';">수정</button>
			    		</c:when>
			    		<c:otherwise>
			    			<button type="button" class="btn" disabled="disabled">수정</button>
			    		</c:otherwise>
			    	</c:choose>
			    	<c:choose>
			    		<c:when test="${sessionScope.member.userId==dto.userId || sessionScope.member.userId=='admin'}">
			    			<button type="button" class="btn" onclick="deletePhoto('${dto.num}');">삭제</button>
			    		</c:when>
			    		<c:otherwise>
			    			<button type="button" class="btn" disabled="disabled">삭제</button>
			    		</c:otherwise>
			    	</c:choose>
			    </td>
			
			    <td align="right">
			        <button type="button" class="btn btnList" onclick="javascript:location.href='${pageContext.request.contextPath}/udongphoto/list.do?page=${page}';">리스트</button>
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