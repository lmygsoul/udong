<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>공지사항</title>
<jsp:include page="/WEB-INF/views/layout/staticHeader.jsp"/>

<script type="text/javascript">
	function searchList() {
		var f=document.searchForm;
		f.submit();
	}
	function listNotice() {
	    var f=document.noticeListForm;
	    f.page.value="1";
	    f.action="${pageContext.request.contextPath}/notice/list.do";
	    f.submit();
	}
	<c:if test="${sessionScope.member.userId=='admin'}">
	$(function(){
		$("#chkAll").click(function(){
			if($(this).is(":checked")) {
				$("input[name=nums]").prop("checked", true);
			} else {
				$("input[name=nums]").prop("checked", false);
			}
		});
		
		$("#btnDeleteList").click(function(){
			var cnt=$("input[name=nums]:checked").length;
			if(cnt==0) {
				alert("삭제할 게시물을 먼저 선택하세요.");
				return false;
			}
			
			if(confirm("선택한 게시물을 삭제 하시겠습니까 ?")) {
				var f=document.noticeListForm;
				f.action="${pageContext.request.contextPath}/notice/deleteList.do";
				f.submit();
			}
			
		});
	});
	</c:if>
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
        				<h3>&nbsp;공지사항</h3>
        			</td>
	        		<td align="right">
				          <form name="searchForm" action="${pageContext.request.contextPath}/notice/list.do" method="post">
				              <select name="condition" class="selectField">
				                  <option value="all"         ${condition=="all"?"selected='selected'":"" }>제목+내용</option>
				                  <option value="subject"     ${condition=="subject"?"selected='selected'":"" }>제목</option>
				                  <option value="userName"    ${condition=="userName"?"selected='selected'":"" }>작성자</option>
				                  <option value="content"     ${condition=="content"?"selected='selected'":"" }>내용</option>
				                  <option value="created"     ${condition=="created"?"selected='selected'":"" }>등록일</option>
				            </select>
				            <input type="text" name="keyword" class="boxTF" value="${keyword}">
				            <button type="button" class="btn btnSearch" onclick="searchList()">검색</button>
				        </form>
				      </td>
			      </tr>
        	</table>
        </div>
        
        <div class="body-board">
        	<form name="noticeListForm" method="post">
			<table style="width: 100%; margin: 20px auto 0px; border-spacing: 0px;">
			   <tr height="35">
			      <td align="left" width="50%">
			         <c:if test="${sessionScope.member.userId=='admin'}">
			          	<button type="button" class="btn" id="btnDeleteList" style="margin: 0 5px 10px 0;">삭제</button>
			          	<input type="hidden" name="page" value="${page}">  <!-- 삭제할때도 page 번호를 넘겨야함  -->
			          </c:if>
			          	${dataCount}개(${page}/${total_page} 페이지)
			      </td>
			      <td align="right">
			          &nbsp;
			      </td>
			   </tr>
			   
			</table>
			
			<table style="width: 100%; margin: 0px auto; border-spacing: 0px; border-collapse: collapse;">
			  <tr class="table-row1"> 
			      <c:if test="${sessionScope.member.userId=='admin'}">
				  	  <th width="40" style="color: #787878;">
				  	  	<input type="checkbox" name="chkAll" id="chkAll" style="margin-top: 3px;">
				  	  </th>
			  	  </c:if> 
			      <th width="60">번호</th>
			      <th>제목</th>
			      <th width="100">작성자</th>
			      <th width="80">작성일</th>
			      <th width="60">조회수</th>
			      <th width="50">첨부</th>
			  </tr>
			 
			 
			 <!-- 공지글 -->
			 <c:forEach var="dto" items="${listNotice}">
			  <tr class="notice-row"> 
			  	  <c:if test="${sessionScope.member.userId=='admin'}">
			  	     <td>
			  	  		<input type="checkbox" name="nums" value="${dto.num}" style="margin-top: 3px;">
			  	  	 </td>
			  	  </c:if>
			      <td align="left" style="padding-left: 14px;">
			           <span class="notice-badge">공지</span>
			      </td>
			      <td align="left" style="padding-left: 10px;">
			           <a href="${articleUrl}&num=${dto.num}">${dto.subject}</a>
			      </td>
			      <td>${dto.userName}</td>
			      <td>${dto.created}</td>
			      <td>${dto.hitCount}</td>
			      <td>
						<c:if test="${not empty dto.saveFilename}">
						      <a href="${pageContext.request.contextPath}/notice/download.do?num=${dto.num}"><i class="far fa-file"></i></a>
						</c:if>
			      </td>			      
			  </tr>
			</c:forEach> 
			 
			  <!-- 일반글 -->
			 <c:forEach var="dto" items="${list}">
			  <tr class="table-row2"> 
			      <c:if test="${sessionScope.member.userId=='admin'}">
			   	     <td>
			   	  		<input type="checkbox" name="nums" value="${dto.num}" style="margin-top: 3px;">
			   	  	 </td>
			  	  </c:if>
			      <td>${dto.listNum}</td>
			      <td align="left" style="padding-left: 10px;">
			           <a href="${articleUrl}&num=${dto.num}">${dto.subject}</a>
			           <c:if test="${dto.gap<1}"><img src="${pageContext.request.contextPath}/resource/images/new.gif"></c:if>
			      </td>
			      <td>${dto.userName}</td>
			      <td>${dto.created}</td>
			      <td>${dto.hitCount}</td>
			      <td>
						<c:if test="${not empty dto.saveFilename}">
						      <a href="${pageContext.request.contextPath}/notice/download.do?num=${dto.num}"><i class="far fa-file"></i></a>
						</c:if>
			      </td>
			  </tr>
			</c:forEach> 
			</table>
			 </form>
			 
			<!-- 없을 때 -->
			<c:if test="${dataCount==0}">
			<table style="width: 100%; margin: 0px auto; border-spacing: 0px;">
			   <tr height="35">
				<td align="center">
			      등록된 게시물이 없습니다.
				</td>
			   </tr>
			</table>
			</c:if>
			
			<!-- 하단 -->
			<table style="width: 100%; margin: 10px auto; margin-top: 30px; border-spacing: 0px;">
			   <tr height="40">
			      <td align="left" width="100">
			          <button type="button" class="btn" onclick="javascript:location.href='${pageContext.request.contextPath}/notice/list.do';">새로고침</button>
			      </td>
			      <c:if test="${dataCount!=0}">
			      <td align="center">
			      	${paging}
			      </td>
			      </c:if>
			      <td align="right" width="100">
			      	<c:if test="${sessionScope.member.userId=='admin'}">
			          <button type="button" class="btn btnCreate" onclick="javascript:location.href='${pageContext.request.contextPath}/notice/created.do';"><i class="fab fa-telegram-plane"></i>&nbsp;&nbsp;글쓰기</button>
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