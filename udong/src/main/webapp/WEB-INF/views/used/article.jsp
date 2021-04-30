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
$(".btn btnList").click(() {
	  $(this).attr('disabled',true);
	});
</script>
</head>
<body>

<div class="header">
    <jsp:include page="/WEB-INF/views/layout/header.jsp"></jsp:include>
</div>
	
<div class="container">
    <div class="body-container">
        
        <div>
			<table style="width: 100%; margin: 0 auto; border-spacing: 0px; border-collapse: collapse;">
			<tr height="35" class="row-3">
			    <td width="50%" align="left" class="col-1">
			        ${dto.subject}
			    </td>     
			</tr>
			
			<tr height="35" class="row-2">
			    <td width="50%" align="left" class="col-2" >
			    <form method="post" action="${pageContext.request.contextPath}/member/Profile.do">
			        <input style="border:none; background: white;" name="nickName" type="submit" value="작성자 : ${dto.nickName}"> 
			      </form>
			    </td>
			    <td width="50%" align="right" class="col-2">
			        ${dto.created}
			    </td>
			</tr>
			</table>
			
			<table style="width: 100%; margin: 0 auto; border-spacing: 0px; border-collapse: collapse;">
			<tr>
			  <td style="padding-left: 20px; padding-top: 20px;">
			    <img src="${pageContext.request.contextPath}/uploads/photo/${dto.imageFilename}" style="max-width:85%; height:auto; resize:both;">
			   </td>
			    <td width="60%" >
			    
			    <table style="width: 100%; margin-top: 20px;">
				    <tr>
					    <td height="30" style="color: gray;" >
					    > ${dto.category}
					    </td>
					</tr>
					<tr>
					    <td height="30" style="font-weight: 700; font-size: 16px;">
					    <span style="color: #FF8A3D; margin-right: 5px;">판매</span>${dto.subject}
					   </td>
					</tr>
					<tr>
					    <td height="30" style="font-weight: 700; font-size: 16px;">
					    <b>${dto.price} 원</b>  
					   </td>
					</tr>
					<tr>
					    <td height="30" style="#BDBDBD;"> ${dto.area} </td>
					</tr>
					<tr height="50">			   
						<td width="50%" align="left">  
					       <c:if test ="${sessionScope.member.userId != null && sessionScope.member.userId != dto.userId}">
						       <button type="button" class="btn sendbtn" 
						       style="width: 90%; color: #495057; background-color: #eee; border: 0; font-weight: 700;"
						       onclick="javascript:location.href='${pageContext.request.contextPath}/used/like.do?num=${dto.num}&page=${page}';">
						       <i class="fas fa-heart"></i>&nbsp;&nbsp;관심글에 추가</button>
					       </c:if>
					       <c:if test ="${sessionScope.member.userId == dto.userId}">
						       <button type="button" class="btn sendbtn" 
						       style="width: 90%; color: #495057; background-color: #eee; border: 0; font-weight: 700;"
						       disabled="disabled"
						       onclick="javascript:location.href='${pageContext.request.contextPath}/used/like.do?num=${dto.num}&page=${page}';">
						       <i class="fas fa-heart"></i>&nbsp;&nbsp;관심글에 추가</button>
					    	</c:if>
					    </td>
					    <td width="50%" align="left">
					    <form method="post" action="${pageContext.request.contextPath}/member/sm_created.do">
					    <c:if test ="${sessionScope.member.userId != null && sessionScope.member.userId != dto.userId}">
					    	<input 	type="hidden" name="userId" value="${dto.userId}">
					  		<button type="submit" class="btn" 
					  		style="width: 90%; color: #495057; background-color: #eee; border: 0; font-weight: 700;">
					  		<i class="fas fa-comment-dots"></i>&nbsp;&nbsp;구매문의 쪽지</button>
					    </c:if>
					    <c:if test ="${sessionScope.member.userId == dto.userId}">	
					  		<button type="button" class="btn" 
					  		style="width: 90%; color: #495057; background-color: #eee; border: 0; font-weight: 700;"
					  		disabled="disabled"
					  		onclick="javascript:location.href='${pageContext.request.contextPath}/used/message_ready.do?num=${dto.num}&page=${page}';">
					  		<i class="fas fa-comment"></i>&nbsp;&nbsp;구매문의 쪽지</button>
					  	</c:if>
					  	</form>
					    </td>
				    </tr>
				    </table>
				    
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