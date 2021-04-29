<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>

<html>
<head>
<meta charset="UTF-8">
<title>우동사리</title>
<script type="text/javascript">
function deleteNeighbor(num) {
	if(confirm("게시물을 삭제 하시겠습니까 ?")) {
		var url="${pageContext.request.contextPath}/neighbor/delete.do?num=${dto.num}&page=${page}";
		location.href=url;
	}
}
function recommend1(){
	if(!${isRec}) {
		alert("추천 및 비추천은 한번만 가능합니다.");
		return;
	}
	var f= document.recForm1;
	f.submit();
}
function recommend2(){
	if(!${isRec}) {
		alert("추천 및 비추천은 한번만 가능합니다.");
		return;
	}
	var f= document.recForm1;
	f.submit();
}
function sendOk(){
	var f = document.replyForm;
	
	str = f.content.value;
	if(!str) {
		alert("내용을 입력하세요. ");
		f.content.focus();
		return;
	}
	f.action="${pageContext.request.contextPath}/neighbor/reply_ok.do"
	f.submit();
}
</script>
<link rel="icon" href="data:;base64,iVBORw0KGgo=">
<jsp:include page="/WEB-INF/views/layout/staticHeader.jsp"/>
</head>
<body>


	<div class="header">
	    <jsp:include page="/WEB-INF/views/layout/header.jsp"></jsp:include>
	</div>
		
	<div class="container" style="background-color: #FBF7F2;">
   <div class="body-container" style="width: 1000px;">
        <div class="body-title">
            <h3><span style="font-family: Webdings">2</span> 우리동네 홍보 </h3>
        </div>
        <c:if test="${dto.num!=null}">
        	<table style="width: 100%; margin: 20px auto 0px; border-spacing: 0px;">
			   <tr height="35">
			      <td align="left" width="50%">
			         <h3>게시물 번호 : No.${dto.num}</h3>
			      </td>
			      <td align="right">
			          &nbsp;
			      </td>
			   </tr>
			</table>
		<div>
			<table style="width: 100%; margin: 20px auto 0px; border-spacing: 0px; border-collapse: collapse;">
			<tr height="35" style="border-top: 1px solid #cccccc; border-bottom: 1px solid #cccccc;">
			    <td colspan="2" align="center">
				  	${dto.subject}
			    </td>
			</tr>
			
			<tr height="35" style="border-bottom: 1px solid #cccccc;">
			    <td width="50%" align="left" style="padding-left: 5px;">
			       이름 : ${dto.nickName}
			    </td>
			    <td width="50%" align="right" style="padding-right: 5px;">
			        ${dto.created} | 조회수 ${dto.hitCount}
			    </td>
			</tr>
			<tr height="35" style="border-bottom: 1px solid #cccccc;">
			    <td align="left" style="padding-left: 5px;">
			       동네 : ${dto.addr}
			    </td>
			    <td width="20%" align="right" style="padding-right: 5px;">
			        추천 ${dto.rec} / 비추천 ${dto.notRec}
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
			<tr height="45" style="border-top: 1px solid #cccccc;">
			    <td align="right">
			    <form name="recForm1" action="${pageContext.request.contextPath}/neighbor/rec.do" method="post" style="width:150px">
				       <input type="hidden" name="num" value="${dto.num}">
				       <input type="hidden" name="page" value="${page}">
			       	   <input type="hidden" name="selectRec" value="1">
				       <button type="button" class="btn" onclick="javascript:recommend1();">추천</button>
			    </form>
			    </td>
			    <td align="left">
			    <form name="recForm2" action="${pageContext.request.contextPath}/neighbor/rec.do" method="post" style="width:150px">
				       <input type="hidden" name="num" value="${dto.num}">
				       <input type="hidden" name="page" value="${page}">
			       	   <input type="hidden" name="selectRec" value="2">
				       <button type="button" class="btn" onclick="javascript:recommend2();">비추천</button>
		       </form>
			   </td>
			</tr>
			</c:when>
			</c:choose>

			<tr height="45">
			    <td colspan="2" align="right">
					<c:choose>
			    			<c:when test="${sessionScope.member.userId==dto.userId || sessionScope.member.type==0}">
			         			<button type="button" class="btn" onclick="javascript:location.href='${pageContext.request.contextPath}/neighbor/update.do?num=${dto.num}&page=${page}';">수정</button>
			          			<button type="button" class="btn" onclick="deleteNeighbor('1');">삭제</button>
			    			</c:when>
			    	</c:choose>
			    </td>
			</tr>
			</table>
        </div>
		
		<form name="replyForm" method="post" style="border: 1px solid lightgray">
		<table>
			<tr height="35" style="border-bottom: 1px solid #cccccc;">
				<td width="10%">&nbsp;</td>
			    <td width="10%" align="left" style="padding-left: 5px;">
			       ${sessionScope.member.userName}
			    </td>
			    <td width="70%" align="right" style="padding-right: 5px;">
				   <textarea name="content" rows="2" class="boxTA" style="width: 700px; height:40px;" placeholder="댓글을 입력해 주세요."></textarea>
			    </td>
			    <td>
			    	<button type="button" class="btn" onclick="sendOk();">입력</button>
			    </td>
			</tr>
		</table>
		</form>
		
		</c:if>
                
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
			      <th width="100" style="color: #787878">추천/비추천</th>
			  </tr>
			 
			 <c:forEach var="dto" items="${list}">
			  <tr align="center" bgcolor="#ffffff" height="35" style="border-bottom: 1px solid #cccccc;"> 
			      <td>${dto.listNum}</td>
			      <td align="left" style="padding-left: 10px;">
			           <a href="${articleUrl}&num=${dto.num}">${dto.subject}</a>
			      </td>
			      <td>${dto.nickName}</td>
			      <td>${dto.created}</td>
			      <td>${dto.hitCount}</td>
				  <td>${dto.rec}/${dto.notRec}</td>
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
			          <button type="button" class="btn" onclick="javascript:location.href='${pageContext.request.contextPath}/neighbor/list.do';">새로고침</button>
			      </td>
			      <c:if test="${sessionScope.member!=null}">
			      	<td align="right" width="100">
			          	<button type="button" class="btn" onclick="javascript:location.href='${pageContext.request.contextPath}/neighbor/created.do';">글올리기</button>
			      	</td>
			      </c:if>
			   </tr>
			   <tr height="40">
			   <td align="center">
			          <form name="searchForm" action="${pageContext.request.contextPath}/neighbor/list.do" method="post">
			              <select name="condition" class="selectField">
			                  <option value="all"         ${condition=="all"?"selected='selected'":"" }>전체</option>
			                  <option value="subject"     ${condition=="subject"?"selected='selected'":"" }>제목</option>
			                  <option value="nickName"    ${condition=="nickName"?"selected='selected'":"" }>작성자</option>
			                  <option value="content"     ${condition=="content"?"selected='selected'":"" }>내용</option>
			                  <option value="created"     ${condition=="created"?"selected='selected'":"" }>등록일</option>
			            </select>
			            <input type="text" name="keyword" class="boxTF" value="${keyword}">
			            <button type="button" class="btn" onclick="searchList()">검색</button>
			        </form>
			      </td>
		      </tr>
			</table>
			</c:if>
			<c:if test="${mode=='myContent' }">
			<table style="width: 100%; margin: 10px auto; border-spacing: 0px;">
			   <tr height="40">
			   	<td align="left" width="100">
			          <button type="button" class="btn" onclick="javascript:location.href='${pageContext.request.contextPath}/member/nb_list.do';">새로고침</button>
			      </td>
			      <td align="left" width="100">
			          <button type="button" class="btn" onclick="javascript:location.href='${pageContext.request.contextPath}/member/cb_list.do';">우동클래스</button>
			      </td>
			   </tr>
			   <tr height="40">
			      <td align="left" width="100">
			          <button type="button" class="btn" onclick="javascript:location.href='${pageContext.request.contextPath}/member/gt_list.do';">가입인사</button>
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
			      <td align="right" width="50%">
			          <button type="button" class="btn" onclick="javascript:location.href='${pageContext.request.contextPath}/member/up_list.do';">우동사진</button>
			      </td>
			   </tr>
			</table>
			</c:if>
        </div>

    </div>
	</div>
	<script type="text/javascript">
	function searchList() {
		var f=document.searchForm;
		f.submit();
	}
	</script>
	<div class="footer">
	    <jsp:include page="/WEB-INF/views/layout/footer.jsp"></jsp:include>
	</div>
	
<jsp:include page="/WEB-INF/views/layout/staticFooter.jsp"/>

</body>
</html>