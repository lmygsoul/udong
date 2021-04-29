﻿<%@ page contentType="text/html; charset=UTF-8" %>
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
    <div class="body-container" style="width: 900px;">
        <div class="body-title">
            <h3><i class="fas fa-chalkboard"></i> 우동이야기 </h3>
        </div>
        
        <div>
			<table style="width: 100%; margin: 20px auto 0px; border-spacing: 0px;">
			   <tr height="35">
			      <td align="left" width="50%">
			          ${dataCount}개(${page}/${total_page} 페이지)
			      </td>
			      <td align="right">
			          &nbsp;
			      </td>
			   </tr>
			</table>
			
			<table style="width: 100%; margin: 0px auto; border-spacing: 0px; border-collapse: collapse;">
			  <tr align="center" bgcolor="#eeeeee" height="35" style="border-top: 1px solid #cccccc; border-bottom: 1px solid #cccccc;"> 
			      <th width="60" style="color: #787878;">번호</th>
			      <th style="color: #787878;">제목</th>
			      <th width="100" style="color: #787878;">작성자</th>
			      <th width="80" style="color: #787878;">작성일</th>
			      <th width="60" style="color: #787878;">조회수</th>
			  </tr>
			 
			 <c:forEach var="dto" items="${list}">
			  <tr align="center" bgcolor="#ffffff" height="35" style="border-bottom: 1px solid #cccccc;"> 
			      <td>${dto.listNum}</td>
			      <td align="left" style="padding-left: 10px;">
			           <a href="${articleUrl}&num=${dto.num}">${dto.subject}</a>
			      </td>
			      <td>${dto.userName}</td>
			      <td>${dto.created}</td>
			      <td>${dto.hitCount}</td>
			  </tr>
			</c:forEach> 
			</table>
			 
			<table style="width: 100%; margin: 0px auto; border-spacing: 0px;">
			   <tr height="35">
				<td align="center">
			        ${dataCount==0?"등록된 게시물이 없습니다.":paging}
				</td>
			   </tr>
			</table>
			<c:if test="${mode!='myContent' }">
			<table style="width: 100%; margin: 10px auto; border-spacing: 0px;">
			   <tr height="40">
			      <td align="left" width="100">
			          <button type="button" class="btn" onclick="javascript:location.href='${pageContext.request.contextPath}/udong/list.do';">새로고침</button>
			      </td>
			      <td align="center">
			          <form name="searchForm" action="${pageContext.request.contextPath}/udong/list.do" method="post">
			              <select name="condition" class="selectField">
			                  <option value="all"         ${condition=="all"?"selected='selected'":"" }>제목+내용</option>
			                  <option value="subject"     ${condition=="subject"?"selected='selected'":"" }>제목</option>
			                  <option value="userName"    ${condition=="userName"?"selected='selected'":"" }>작성자</option>
			                  <option value="content"     ${condition=="content"?"selected='selected'":"" }>내용</option>
			                  <option value="created"     ${condition=="created"?"selected='selected'":"" }>등록일</option>
			            </select>
			            <input type="text" name="keyword" class="boxTF" value="${keyword}">
			            <button type="button" class="btn" onclick="searchList()">검색</button>
			        </form>
			      </td>
			      <td align="right" width="100">
			          <button type="button" class="btn" onclick="javascript:location.href='${pageContext.request.contextPath}/udong/created.do';">글올리기</button>
			      </td>
			   </tr>
			</table>
			</c:if>
			<c:if test="${mode=='myContent' }">
			<table style="width: 100%; margin: 10px auto; border-spacing: 0px;">
			   <tr height="40">
			     <td align="left" width="100">
			          <button type="button" class="btn" onclick="javascript:location.href='${pageContext.request.contextPath}/member/ud_list.do';">새로고침</button>
			      </td>
			   </tr>
			   <tr height="40">
			   		 <td align="left" width="100">
			          <button type="button" class="btn" onclick="javascript:location.href='${pageContext.request.contextPath}/member/cb_list.do';">새로고침</button>
			      </td>
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