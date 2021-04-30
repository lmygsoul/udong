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
function deleteBoard(num) {
    var query = "num="+num+"&${query}";
    var url = "${pageContext.request.contextPath}/udong/delete.do?" + query;

    if(confirm("위 자료를 삭제 하시겠습니까 ? ")) {
    	location.href=url;
    }
}
</c:if>


function sendOk() {
	var f = document.replyForm;
	str = f.content.value;
	if(!str) {
		alert("내용을 입력하세요. ");
		f.content.focus();
		return;
	}
	
	f.action="${pageContext.request.contextPath}/udong/reply_ok.do"
	f.submit();
}

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
			    <td colspan="2" align="left" class="col-3">
			       이전글 : 
					<c:if test="${not empty preReadDto}">
						<a href="${pageContext.request.contextPath}/udong/article.do?num=${preReadDto.num}&${query}">${preReadDto.subject}</a>
					</c:if>
			    </td>
			</tr>
			
			<tr height="35" class="row-2">
			    <td colspan="2" align="left" class="col-3">
			       다음글 :
			       	<c:if test="${not empty nextReadDto}">
						<a href="${pageContext.request.contextPath}/udong/article.do?num=${nextReadDto.num}&${query}">${nextReadDto.subject}</a>
					</c:if>
			    </td>
			</tr>
			
			<tr height="45">
			    <td class="col-4">
			    	<c:choose>
			    		<c:when test="${sessionScope.member.userId==dto.userId}">
			    			<button type="button" class="btn" onclick="javascript:location.href='${pageContext.request.contextPath}/udong/update.do?num=${dto.num}&page=${page}';">수정</button>
			    		</c:when>
			    		<c:otherwise>
			    			<button type="button" class="btn" disabled="disabled">수정</button>
			    		</c:otherwise>
			    	</c:choose>
			    	
			    	<c:choose>
			    		<c:when test="${sessionScope.member.userId==dto.userId || sessionScope.member.userId=='admin'}">
			    			<button type="button" class="btn" onclick="deleteBoard('${dto.num}');">삭제</button>
			    		</c:when>
			    		<c:otherwise>
			    			<button type="button" class="btn" disabled="disabled">삭제</button>
			    		</c:otherwise>
			    	</c:choose>
			    </td>
			
			    <td align="right" class="col-4">
			        <button type="button" class="btn btnCreate" onclick="javascript:location.href='${pageContext.request.contextPath}/udong/list.do?${query}';">리스트</button>
			    </td>
			<c:choose>
		<c:when test="${sessionScope.member.userId!=null}">
		<form name="replyForm" method="post" style="border: 1px solid lightgray">
		<table style="background-color: #F8F9FA;">
			<tr height="35" style="border-bottom: 1px solid #cccccc;">
			    <td width="10%" align="left" style="padding-left: 5px; text-align: center;">
			       ${sessionScope.member.userName}
			    </td>
			    <td width="70%" align="right" style="padding: 15px 10px 10px;">
				   <textarea name="content" rows="2" class="boxTA" style="width: 500px; height:30px;" placeholder="댓글을 입력해 주세요."></textarea>
			    </td>
			    <td>
			    	<input type="hidden" name="num" value="${dto.num}">
				    <input type="hidden" name="page" value="${page}">
				    <button type="button" class="btn btnCreate" onclick="sendOk();" >입력</button>
			    </td>
			</tr>
		</table>
		</form>
		</c:when>
		</c:choose>

		 <div class="body-board">
			<table style="width: 100%; margin: 0px auto; border-spacing: 0px; border-collapse: collapse;">
			<tr class="table-row2">
			      <td width="10%">작성자</td>
			      <td width="70%">댓글 내용</td>
			      <td width="20%">작성일</td>
			</tr>
				 <c:forEach var="dto" items="${reply_list}">
				    <tr class="table-row2">
				      <td>${dto.userName}</td>
				      <td style="text-align: left; padding-left: 20px">${dto.content}</td>
				      <td>${dto.created}</td>
				    </tr>
				</c:forEach> 
			</table>
			
			<table style="width: 100%; margin: 10px auto; margin-top: 30px; border-spacing: 0px;">
			   <tr height="40">
			      <c:if test="${replyCount!=0}">
			      <td align="center">
			      	${reppaging}
			      </td>
			      </c:if>
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