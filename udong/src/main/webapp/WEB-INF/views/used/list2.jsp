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

<style type="text/css">
.imgLayout{
	width: 190px;
	height: 205px;
	padding: 10px 5px 10px;
	margin: 5px;
	border: 1px solid #DAD9FF;
     cursor: pointer;
}
.subject {
     display: inline-block;
     width:180px;
     height:25px;
     line-height:25px;
     margin:5px auto;
     border-top: 1px solid #DAD9FF;
     white-space:nowrap;
     overflow:hidden;
     text-overflow:ellipsis;
}
</style>

<script type="text/javascript">
	function searchList() {
		var f=document.searchForm;
		f.submit();
	}
</script>

</head>
<body>

<div class="header">
    <jsp:include page="/WEB-INF/views/layout/header.jsp"></jsp:include>
</div>
	
<div class="container">
	
    <div class="body-container" style="width: 700px;">
        <div class="body-title">
            <h3><span style="font-family: Webdings">4</span> 중고거래 </h3>
        </div>
        
        
        <div>
        	<table style="width: 630px; margin: 0 auto; border-spacing: 0">
				<c:forEach var="dto" items="${list}" varStatus="status">
					<c:if test="${status.index==0}">
						<tr>
					</c:if>
					<c:if test="${status.index !=0 && status.index%3 == 0}">
						<c:out value="</tr><tr>" escapeXml="false"/>
					</c:if>
					<td width="210" align="center">
						<div class="imgLayout" onclick="javascript:location.href='${articleUrl}&num=${dto.num}';">
							<img src="${pageContext.request.contextPath}/uploads/photo/${dto.imageFilename}" width="180" height="180">
							<span class="subject">${dto.subject}</span>
						</div>
					</td>
				</c:forEach>
				
				<c:set var="n" value="${list.size()}"/>
				<c:if test="${n > 0 && n%3 != 0}">
					<c:forEach var="i" begin="${n%3+1}" end="3">
						<td width="210">
							<div class="imgLayout">&nbsp;</div>
						</td>
					</c:forEach>
				</c:if>
				<c:if test="${n!=0}">
					<c:out value="</tr>" escapeXml="false"/>
				</c:if>
			</table>
			 
			<table style="width: 100%; margin: 0px auto; border-spacing: 0px;">
			   <tr height="35">
				<td align="center">
			        ${dataCount==0?"등록된 게시물이 없습니다.":paging}
				</td>
			   </tr>
			</table>
			
			<table style="width: 100%; margin: 10px auto; border-spacing: 0px;">
			   <tr height="40">
			      <td align="left" width="100">
			          <button type="button" class="btn" onclick="javascript:location.href='${pageContext.request.contextPath}/used/list.do';">새로고침</button>
			      </td>
			      <td align="center">
			          <form name="searchForm" action="${pageContext.request.contextPath}/used/list.do" method="post">
			              <select name="condition" class="selectField">
			              	  <option value="all" 		${condition=="all"?"selected='selected'":""}>제목+내용</option>
			                  <option value="subject" 	${condition=="subject"?"selected='selected'":""}>제목</option>
			                  <option value="userName" 	${condition=="userName"?"selected='selected'":""}>작성자</option>
			                  <option value="content" 	${condition=="content"?"selected='selected'":""}>내용</option>
			                  <option value="created" 	${condition=="created"?"selected='selected'":""}>등록일</option>
			            </select>
			            <input type="text" name="keyword" class="boxTF">
			            <button type="button" class="btn" onclick="searchList()">검색</button>
			        </form>
			      </td>
			      <td align="right" width="100">
			          <button type="button" class="btn btnCreate" onclick="javascript:location.href='${pageContext.request.contextPath}/used/created.do';">글올리기</button>
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