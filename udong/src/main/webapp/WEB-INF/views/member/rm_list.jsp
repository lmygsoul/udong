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
	
    <div class="body-container" style="border:0;">
        <div class="list-title">
        	<table style="width: 100%; margin: 0px auto; border-spacing: 0px;">
        		<tr>
        			<td align="left">
        				<h3>&nbsp;받은 쪽지함</h3>
        			</td>
	        		<td align="right">
	        			<form name="searchForm" action="${pageContext.request.contextPath}/member/rm_list.do" method="post">
			              <select name="condition" class="selectField">
			              	  <option value="all" 		${condition=="all"?"selected='selected'":""}>제목+내용</option>
			                  <option value="subject" 	${condition=="subject"?"selected='selected'":""}>제목</option>
			                  <option value="receiveUser" 	${condition=="sendUser"?"selected='selected'":""}>보낸사람</option>
			                  <option value="content" 	${condition=="content"?"selected='selected'":""}>내용</option>
			                  <option value="sendTime" 	${condition=="sendTime"?"selected='selected'":""}>등록일</option>
			            </select>
			            <input type="text" name="keyword" class="boxTF">
			            <button type="button" class="btn btnSearch" onclick="searchList()">검색</button>
			        </form>
				    </td>
			    </tr>
        	</table>
        </div>
        
        
        <div class="body-board">
			<table style="width: 100%; margin: 20px auto 0px; border-spacing: 0;">
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
			      <th width="60">번호</th>
			      <th>제목</th>
			      <c:if test="${sessionScope.member.type=='0' }">
			      	<th width="100">받는사람</th>
			      </c:if>
			      <th width="100">보낸사람</th>
			      <th width="80">작성일</th>
			      <th width="80">상태</th>
			  </tr>		
			<c:forEach var="mdto" items="${list}">
				<c:if test="${sessionScope.member.userId==mdto.receiveUser || sessionScope.member.type == '0'}">
			  <tr align="center" height="35" style="border-bottom: 1px solid #cccccc;"> 
			      <td>${mdto.listNum}</td>
			      <td align="left" style="padding-left: 10px;">
			           <a href="${articleUrl}&num=${mdto.pageNum}">${mdto.subject}</a>
			      </td>
			      <c:if test="${sessionScope.member.type=='0' }">
			      	 <td>${mdto.receiveUser}</td>
			      </c:if>
			      <td>${mdto.sendUser}</td>
			      <td>${mdto.sendTime}</td>
			      <td>${mdto.messageType=="1"? "읽지않음":"읽음" }</td>
			  </tr>
			  </c:if>
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
			
			<table style="width: 100%; margin: 10px auto;  margin-top: 30px; border-spacing: 0px;">
			   <tr height="40">
			   	<td align="left" width="100">
			          <button type="button" class="btn" onclick="javascript:location.href='${pageContext.request.contextPath}/member/rm_list.do';">새로고침</button>
			      </td>
			      <td align="center">
			      	${paging}
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