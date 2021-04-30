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
	function searchList() {
		var f=document.searchForm;
		f.submit();
	}
	
	function listFaq() {
	    var f=document.faqListForm;
	    f.page.value="1";
	    f.action="${pageContext.request.contextPath}/faq/list.do";
	    f.submit();
	}
</script>
</head>
<body>

<div class="header">
    <jsp:include page="/WEB-INF/views/layout/header.jsp"></jsp:include>
</div>
	
<div class="container">
	
    <div class="body-container" style="border: 0;">
        <div class="list-title">
        	<table style="width: 100%; margin: 0px auto; border-spacing: 0px;">
        		<tr>
        			<td align="left">
        				<h3>&nbsp;FAQ</h3>
        			</td>
			      </tr>
        	</table>
        </div>
        
        <div class="body-board">
			<table style="width: 100%; margin-top: 20px; border-spacing: 0;">
			   <tr height="35">
			      <td align="left" width="50%">
			          ${dataCount}개(${page}/${total_page} 페이지)
			      </td>
			      <td align="right">
			          &nbsp;
			      </td>
			   </tr>
			</table>
			
			<table style="width: 100%; border-spacing: 0; border-collapse: collapse;">
			  <tr class="table-row1"> 
			      <th width="120">번호</th>
			      <th align="left">제목</th>
			  </tr>
			 
			 <c:forEach var="dto" items="${list}">
			  <tr class="table-row2"> 
			      <td>${dto.listNum}</td>
			      <td align="left">
			           <a href="${articleUrl}&num=${dto.num}">${dto.subject}</a>
			      </td>
			  </tr>
			</c:forEach>
			
			</table>
			
			<c:if test="${dataCount==0}">
			<table style="width: 100%; margin: 0px auto; border-spacing: 0px;">
			   <tr height="35">
				<td align="center">
			        등록된 게시물이 없습니다.
				</td>
			   </tr>
			</table>
			</c:if>
			
			<table style="width: 100%; margin: 0px auto; margin-top: 30px; border-spacing: 0px;">
			   <tr height="40">
			      <td align="left" width="100">
			          <button type="button" class="btn" onclick="javascript:location.href='${pageContext.request.contextPath}/faq/list.do';">새로고침</button>
			      </td>
			      <c:if test="${dataCount!=0}">
			      <td align="center">
			      	${paging}
			      </td>
			      </c:if>
			      <td align="right" width="100">
			      	<c:if test="${sessionScope.member.userId=='admin'}">
			          <button type="button" class="btn btnCreate" onclick="javascript:location.href='${pageContext.request.contextPath}/faq/created.do';"><i class="fab fa-telegram-plane"></i>&nbsp;&nbsp;글쓰기</button>
			      	</c:if>
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