<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>${title }</title>
<jsp:include page="/WEB-INF/views/layout/staticHeader.jsp"/>
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
            <h3><span style="font-family: Webdings">4</span> ${title } </h3>
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
			  <tr align="center" bgcolor="white" height="35" style="border-top: 1px solid #cccccc; border-bottom: 1px solid #cccccc;"> 
			      <th width="60" style="color: #787878;">번호</th>
			      <th style="color: #787878;">제목</th>
			      <c:if test="${sessionScope.member.type=='0' }">
			      	<th width="100" style="color: #787878;">받는사람</th>
			      </c:if>
			      <th width="100" style="color: #787878;">보낸사람</th>
			      <th width="80" style="color: #787878;">작성일</th>
			      <th width="80" style="color: #787878;">상태</th>
			  </tr>		
			<c:forEach var="mdto" items="${list}">
				<c:if test="${sessionScope.member.userId==mdto.receiveUser || sessionScope.member.type == '0'}">
			  <tr align="center" height="35" style="border-bottom: 1px solid #cccccc;"> 
			      <td>${mdto.listNum}</td>
			      <td align="left" style="padding-left: 10px;">
			           <a href="${articleUrl}&num=${mdto.pageNum}">${mdto.subject}</a>
			      </td>
			      <c:if test="${sessionScope.member.type=='0' }">
			      	 <td>${sessionScope.member.userId}</td>
			      </c:if>
			      <td>${mdto.sendUser}</td>
			      <td>${mdto.sendTime}</td>
			      <td>${mdto.messageType=="1"? "읽지않음":"읽음" }</td>
			  </tr>
			  </c:if>
			</c:forEach>	
			</table>
			 
			<table style="width: 100%; margin: 0px auto; border-spacing: 0px;">
			   <tr height="35">
				<td align="center">
			       ${dataCount==0?"등록된 게시물이 없습니다.":paging}
				</td>
			   </tr>
			</table>
			
			<table style="width: 100%; margin: 10px auto; border-spacing: 0px;">
			   <tr>
			   	<td align="left" width="100">
			          <button type="button" class="btn" onclick="javascript:location.href='${pageContext.request.contextPath}/member/rm_list.do';">새로고침</button>
			      </td>
			      <td align="center">
			          <form name="searchForm" action="${pageContext.request.contextPath}/member/rm_list.do" method="post">
			              <select name="condition" class="selectField">
			              	  <option value="all" 		${condition=="all"?"selected='selected'":""}>제목+내용</option>
			                  <option value="subject" 	${condition=="subject"?"selected='selected'":""}>제목</option>
			                  <option value="receiveUser" 	${condition=="receiveUser"?"selected='selected'":""}>받는사람</option>
			                  <option value="content" 	${condition=="content"?"selected='selected'":""}>내용</option>
			                  <option value="sendTime" 	${condition=="sendTime"?"selected='selected'":""}>등록일</option>
			            </select>
			            <input type="text" name="keyword" class="boxTF">
			            <button type="button" class="btn" onclick="searchList()">검색</button>
			        </form>
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