<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>우동사리</title>
<link rel="icon" href="data:;base64,iVBORw0KGgo=">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resource/css/style.css" type="text/css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resource/css/layout.css" type="text/css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resource/jquery/css/smoothness/jquery-ui.min.css" type="text/css">

<script type="text/javascript" src="${pageContext.request.contextPath}/resource/js/util.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/resource/jquery/js/jquery.min.js"></script>
<script type="text/javascript">
function deleteStore(num) {
	if(confirm("게시물을 삭제 하시겠습니까 ?")) {
		var url="${pageContext.request.contextPath}/store/delete.do?num=${dto.num}&page=${page}";
		location.href=url;
	}
}
function score(){
	if(!${isGrade}) {
		alert("평점 등록은 한번만 가능합니다.");
		return;
	}
	var f= document.scoreForm;
	f.submit();
	
}
</script>
<jsp:include page="/WEB-INF/views/layout/staticHeader.jsp"/>
</head>
<body>
	<div class="header">
	    <jsp:include page="/WEB-INF/views/layout/header.jsp"></jsp:include>
	</div>
<div class="container">
    <div class="body-container" style="width: 1000px;">
        <div class="body-title">
            <h3><span style="font-family: Webdings">2</span> 우리동네 홍보 </h3>
        </div>
        
        <div>
			<table style="width: 100%; margin: 20px auto 0px; border-spacing: 0px; border-collapse: collapse;">
			<tr height="35" style="border-top: 1px solid #cccccc; border-bottom: 1px solid #cccccc;">
			    <td colspan="2" align="center">
				  	${dto.subject}
			    </td>
			</tr>
			
			<tr height="35" style="border-bottom: 1px solid #cccccc;">
			    <td width="50%" align="left" style="padding-left: 5px;">
			       이름 : ${dto.nickname}
			    </td>
			    <td width="50%" align="right" style="padding-right: 5px;">
			        ${dto.created} | 평점 : ${dto.score}/5.0
			    </td>
			</tr>
			
			<tr>
				<td colspan="2" align="left" style="padding: 10px 5px;">
					<img src="${pageContext.request.contextPath}/uploads/photo/${dto.imageFileName}" style="max-width:100%; height: auto; resize:both;">
				</td>
			</tr>
			<tr >
			  <td colspan="2" align="left" style="padding: 10px 5px;" valign="top" height="200">
			      ${dto.content}
			   </td>
			</tr>
			  	<c:choose>
			    		<c:when test="${sessionScope.member.userId!=null}">
			<tr height="35" style="border-top: 1px solid #cccccc;">
			    <td colspan="2" align="right">
			    <form name="scoreForm" action="${pageContext.request.contextPath}/store/score.do" method="post">
				       평점등록 : <select id="selectScore" name="selectScore" class="selectField">
				       	<option value="5">5</option>
				       	<option value="4.5">4.5</option>
				       	<option value="4">4</option>
				       	<option value="3.5">3.5</option>
				       	<option value="3">3</option>
				       	<option value="2.5">2.5</option>
				       	<option value="2">2</option>
				       	<option value="1.5">1.5</option>
				       	<option value="1">1</option>
				       	<option value="0.5">0.5</option>
				       	<option value="0">0</option>
				       </select>
				       <input type="hidden" name="num" value="${dto.num}">
				       <input type="hidden" name="page" value="${page}">
			       	   <button type="button" class="btn" onclick="javascript:score();" >확인</button>
			       </form>
			    </td>
			</tr>
			</c:when>
			</c:choose>

			<tr height="35" style="border-bottom: 1px solid #cccccc; border-top: 1px solid #cccccc">
			    <td colspan="2" align="left" style="padding-left: 5px;">
			       이전글 : <c:if test="${not empty preReadDto}">
						<a href="${pageContext.request.contextPath}/store/article.do?num=${preReadDto.num}&${query}">${preReadDto.subject}</a>
						</c:if>	      
			    </td>
			</tr>
			
			<tr height="35" style="border-bottom: 1px solid #cccccc;">
			    <td colspan="2" align="left" style="padding-left: 5px;">
			       다음글 : <c:if	test="${not empty nextReadDto}">
						<a href="${pageContext.request.contextPath}/store/article.do?num=${nextReadDto.num}&${query}">${nextReadDto.subject}</a>
					</c:if>
			       
			    </td>
			</tr>
			<tr height="45">
			    <td>
					<c:choose>
			    			<c:when test="${sessionScope.member.userId==dto.userId || sessionScope.member.type==0}">
			         			<button type="button" class="btn" onclick="javascript:location.href='${pageContext.request.contextPath}/store/update.do?num=${dto.num}&page=${page}';">수정</button>
			          			<button type="button" class="btn" onclick="deleteStore('1');">삭제</button>
			    			</c:when>
			    	</c:choose>
			    </td>
			
			    <td align="right">
			        <button type="button" class="btn" onclick="javascript:location.href='${pageContext.request.contextPath}';">리스트</button>
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