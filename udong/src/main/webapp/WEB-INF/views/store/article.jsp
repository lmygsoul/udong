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
			       작성자 : ${dto.nickname}
			    </td>
			    <td width="50%" align="right" class="col-2">
			        ${dto.created}
			    </td>
			</tr>
			<tr height="35" class="row-2" style="border-bottom: 0;">
				<td colspan="2" align="left" class="col-2" style="padding-top: 10px; padding-bottom: 5px;">
			       <strong style="color: #FF8A3D">평점&nbsp;&nbsp;</strong><strong>${dto.score} / 5.0</strong>
			    </td>
			</tr>
			<tr height="35" class="row-2">
				<td colspan="2" align="left" class="col-2">
			       주소 : ${dto.addr}
			    </td>
			</tr>
			
			<tr>
				<td colspan="2" align="left" style="padding: 10px 5px;">
					<img src="${pageContext.request.contextPath}/uploads/photo/${dto.imageFileName}" style="max-width:100%; height: auto; resize:both;">
				</td>
			</tr>
			<tr>
			  <td colspan="2" align="left" style="padding: 10px 5px;" valign="top" height="200">
			      ${dto.content}
			   </td>
			</tr>
			  	<c:choose>
			    		<c:when test="${sessionScope.member.userId!=null}">
			<tr height="35" style="border-top: 1px solid #cccccc;">
			    <td colspan="2" align="right">
			    <form name="scoreForm" action="${pageContext.request.contextPath}/store/score.do" method="post">
				       평점등록 &nbsp;:&nbsp;<select id="selectScore" name="selectScore" class="selectField" style="width: 70px; margin: 10px;">
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
			       	   <button type="button" class="btn" onclick="javascript:score();"
			       	   		style="background-color: #FF8A3D; color: white; border: #FF8A3D; font-weight: bold; padding: 4px 25px 6px; margin-right: 15px;">
			       	   		확인
			       	   </button>
			       </form>
			    </td>
			</tr>
			</c:when>
			</c:choose>

			<tr height="35" class="row-2">
			    <td colspan="2" align="left" class="col-3" style="border-top: 1px solid #E9ECEF;">
			       이전글 : <c:if test="${not empty preReadDto}">
						<a href="${pageContext.request.contextPath}/store/article.do?num=${preReadDto.num}&${query}">${preReadDto.subject}</a>
						</c:if>	      
			    </td>
			</tr>
			
			<tr height="35" class="row-2">
			    <td colspan="2" align="left" class="col-3">
			       다음글 : <c:if	test="${not empty nextReadDto}">
						<a href="${pageContext.request.contextPath}/store/article.do?num=${nextReadDto.num}&${query}">${nextReadDto.subject}</a>
					</c:if>
			       
			    </td>
			</tr>
			<tr height="45">
			    <td class="col-4">
					<c:choose>
			    			<c:when test="${sessionScope.member.userId==dto.userId || sessionScope.member.type==0}">
			         			<button type="button" class="btn" onclick="javascript:location.href='${pageContext.request.contextPath}/store/update.do?num=${dto.num}&page=${page}';">수정</button>
			          			<button type="button" class="btn" onclick="deleteStore('1');">삭제</button>
			    			</c:when>
			    	</c:choose>
			    </td>
			
			    <td align="right" class="col-4">
			        <button type="button" class="btn btnList" onclick="javascript:location.href='${pageContext.request.contextPath}/store/list.do?${query}';">리스트</button>
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