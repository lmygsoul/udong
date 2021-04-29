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
        				<h3>&nbsp;우리동네 클래스</h3>
        			</td>
	        		<td align="right">
				          <form name="searchForm" action="${pageContext.request.contextPath}/dayclass/list.do" method="post">
			              <select name="condition" class="selectField">
			              	  <option value="all" 		${condition=="all"?"selected='selected'":""}>제목+내용</option>
			                  <option value="subject" 	${condition=="subject"?"selected='selected'":""}>제목</option>
			                  <option value="userName" 	${condition=="userName"?"selected='selected'":""}>작성자</option>
			                  <option value="content" 	${condition=="content"?"selected='selected'":""}>내용</option>
			                  <option value="created" 	${condition=="created"?"selected='selected'":""}>등록일</option>
			            </select>
			            <input type="text" name="keyword" class="boxTF">
			            <button type="button" class="btn btnSearch" onclick="searchList()">검색</button>
			        </form>
				      </td>
			      </tr>
        	</table>
        </div>
        
        
        <div class="body-board">
			<table style="width: 100%;  margin: 20px auto 0px; border-spacing: 0;">
			   <tr height="35">
			      <td align="left" width="50%">
			          ${dataCount}개(${page}/${total_page} 페이지)
			      </td>
			      <td align="right">
			          &nbsp;
			      </td>
			   </tr>
			</table>
			
			<table style="width: 100%;  margin: 0px auto; border-spacing: 0; border-collapse: collapse;">
			    <tr class="table-row1"> 
			      <th width="60">번호</th>
			      <th>제목</th>
			      <th width="100">작성자</th>
			      <th width="80">작성일</th>
			      <th width="60">마감여부</th>
			  </tr>
			 
			 <c:forEach var="dto" items="${list}">
			    <tr class="table-row2">
			      <td>${dto.listNum}</td>
			      <td align="left" style="padding-left: 10px;">
			           <a href="${articleUrl}&boardNum=${dto.boardNum}">${dto.subject}</a>
			      </td>
			      <td>${dto.nickName}</td>
			      <td>${dto.created}</td>
			      <td>
			      	${dto.curClass==dto.maxClass?'모집마감':'모집중!'}
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
			
			<c:if test="${mode!='myContent' }">
			<table style="width: 100%; margin: 10px auto; margin-top: 30px; border-spacing: 0px;">
			   <tr height="40">
			      <td align="left" width="100">
			          <button type="button" class="btn" onclick="javascript:location.href='${pageContext.request.contextPath}/dayclass/list.do';">새로고침</button>
			      </td>
			      <c:if test="${dataCount!=0}">
			      <td align="center">
			      	${paging}
			      </td>
			      </c:if>
			      <td align="right" width="200">
			          <button type="button" class="btn btnCreate" onclick="javascript:location.href='${pageContext.request.contextPath}/dayclass/created.do';"><i class="fas fa-user-edit"></i>&nbsp;&nbsp;클래스 모집</button>
			      </td>
			   </tr>
			</table>
			</c:if>
			
			<c:if test="${mode=='myContent' }">
			<table style="width: 100%; margin: 10px auto; margin-top: 30px; border-spacing: 0px;">
			   <tr height="40">
			      <td align="left" width="100">
			          <button type="button" class="btn" onclick="javascript:location.href='${pageContext.request.contextPath}/member/cb_list.do';">새로고침</button>
			      </td>
			   </tr>
			   <tr height="40">
			      <td align="left" width="100">
			          <button type="button" class="btn" onclick="javascript:location.href='${pageContext.request.contextPath}/member/gt_list.do';">가입인사</button>
			      </td>
			      <td align="left" width="100">
			          <button type="button" class="btn" onclick="javascript:location.href='${pageContext.request.contextPath}/member/nb_list.do';">우동자랑</button>
			      </td>
			      <tr height="40">
			      <td align="left" width="100">
			          <button type="button" class="btn" onclick="javascript:location.href='${pageContext.request.contextPath}/member/qa_list.do';">우동지식</button>
			      </td>
			      <td align="left" width="100">
			          <button type="button" class="btn" onclick="javascript:location.href='${pageContext.request.contextPath}/member/st_list.do';">우동홍보</button>
			      </td>
			      <td align="left" width="100">
			          <button type="button" class="btn" onclick="javascript:location.href='${pageContext.request.contextPath}/member/ud_list.do';">우동이야기</button>
			      </td>
			      <td align="left" width="100">
			          <button type="button" class="btn" onclick="javascript:location.href='${pageContext.request.contextPath}/member/used_list.do';">중고거래</button>
			      </td>
			   </tr>
			</table>
			</c:if>
			
        </div>
    </div>
</div>

<div class="footer">
    <jsp:include page="/WEB-INF/views/layout/footer.jsp"></jsp:include>
</div>

<jsp:include page="/WEB-INF/views/layout/staticFooter.jsp"/>
</body>
</html>