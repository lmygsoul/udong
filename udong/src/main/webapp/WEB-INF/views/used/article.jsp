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
// 관리자이거나 글 작성자가 아니면, 스크립트가 보이지 않게.
<c:if test="${dto.userId == sessionScope.member.userId || sessionScope.member.userId == 'admin'}">
function deleteBoard(num) {
	if(confirm("게시물을 삭제 하시겠습니까 ?")) {
		var url="${pageContext.request.contextPath}/used/delete.do?num="+num+"&${query}";
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
            <h3><span style="font-family: Webdings">4</span> 중고거래 </h3>
        </div>
        
        <div>
			<table style="width: 100%; margin: 0 auto; border-spacing: 0px; border-collapse: collapse;">
			<tr height="35" class="row-3">
			    <td width="50%" align="left" class="col-2">
			        ${dto.subject}
			    </td>     
			</tr>
			
			<tr height="35" class="row-2">
			    <td width="50%" align="left" class="col-2">
			       작성자 : ${dto.nickName} 
			    </td>
			    <td width="50%" align="right" class="col-2">
			        ${dto.created}
			    </td>
			</tr>
			
			<tr class="row-2">
			  <td  rowspan="6" height="50">
			    <img src="${pageContext.request.contextPath}/uploads/photo/${dto.imageFilename}" style="max-width:85%; height:auto; resize:both;">
			   </td>
			    <td  height="25">
			    > ${dto.category}
			   </td>
			</tr>
			<tr class="row-2">
			    <td  height="40">
			    ${dto.subject}
			   </td>
			</tr>
			<tr class="row-2">
			    <td  height="40">
			    <b>${dto.price} 원</b>  
			   </td>
			</tr>
			<tr class="row-2">
			    <td  height="40" style="#BDBDBD;"> ${dto.area} </td>
			</tr>
			<tr class="row-2">			   
			   <td  height="40">  
			       <c:if test ="${sessionScope.member.userId != null && sessionScope.member.userId != dto.userId}">
			       <a onclick="return confirm('관심글에 추가할까요?')" href="${pageContext.request.contextPath}/used/like.do?num=${dto.num}&page=${page}">관심 글에 추가 ♡</a>
			       </c:if>
			    </td>
			</tr>
			<tr class="row-2">				
			    <td  height="50" style="margin: 0 auto; text-align: center;">			    
			  	<button type="button" class="btn btnList" style="width: 45%; height: 85%;" onclick="">구매 문의 쪽지</button>
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
					<c:if test="${not empty preReadUsed}">
						<a href="${pageContext.request.contextPath}/used/article.do?num=${preReadUsed.num}&${query}">${preReadUsed.subject}</a>
					</c:if>
			    </td>
			</tr>
			
			<tr height="35" class="row-2">
			    <td colspan="2" align="left" class="col-3">
			       다음글 :
					<c:if test="${not empty nextReadUsed}">
						<a href="${pageContext.request.contextPath}/used/article.do?num=${nextReadUsed.num}&${query}">${nextReadUsed.subject}</a>
					</c:if>
			    </td>
			</tr>
			<tr height="45">
			    <td class="col-4">
			    	<c:choose>
			    		<c:when test="${dto.userId == sessionScope.member.userId}">
			          		<button type="button" class="btn" style="margin-right: 3px;" onclick="javascript:location.href='${pageContext.request.contextPath}/used/update.do?num=${dto.num}&page=${page}';">수정</button>
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
			    <td align="right" class="col-4">
			        <button type="button" class="btn btnList" onclick="javascript:location.href='${pageContext.request.contextPath}/used/list.do?${query}';">리스트</button>
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