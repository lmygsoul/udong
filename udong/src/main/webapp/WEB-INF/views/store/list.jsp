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
<jsp:include page="/WEB-INF/views/layout/staticHeader.jsp"/>
<script type="text/javascript">
	function searchLocation() {
		var f=document.searchForm;
		f.submit();
	}
</script>
</head>
<body>
	<div class="header">
	    <jsp:include page="/WEB-INF/views/layout/header.jsp"></jsp:include>
	</div>
		
	<div class="container" style="background-color: white; border-top: 1px solid #E9ECEF;">
   <div class="body-container" style="background-color: white; border: 0; position: relative;">
        <div class="body-title" style="border: 0;">
	       <h3 style="font-size: 24px;">우리동네 홍보</h3>
        </div>
        <div align="right" style="padding-right: 10px">
        	<form name="searchForm" action="${pageContext.request.contextPath}/store/list.do" method="post">
			      지역선택
			      <select name="keyword" class="selectField" onchange="searchLocation()">
			                  <option value="ALL" ${keyword=="ALL"?"selected='selected'":"" }>전체</option>
			                  <option value="서울" ${keyword=="서울"?"selected='selected'":"" }>서울</option>
			                  <option value="경기" ${keyword=="경기"?"selected='selected'":"" }>경기</option>
			                  <option value="인천" ${keyword=="인천"?"selected='selected'":"" }>인천</option>
			                  <option value="기타" ${keyword=="기타"?"selected='selected'":"" }>기타</option> 
			      </select>
			 </form>
       	</div>
        <div>
        	<table style="width: 800px; margin: 10px auto; border-spacing: 0">
        	<c:forEach var="dto" items="${list}" varStatus="status">
        		<c:if test = "${status.index==0}">
        			<tr>
        		</c:if>
        		<c:if test="${status.index !=0 && status.index%3 ==0}">
        			<c:out value="</tr><tr>" escapeXml="false"/>
        		</c:if>
        		<td width="210" align="center" onclick="location.href='${articleUrl}&num=${dto.num}';" >
        			<div class="imgLayout">
        				<img src="${pageContext.request.contextPath}/uploads/photo/${dto.imageFileName}" width="230" height="230"><br>
        				<span class="subject">[${dto.addrsub}] ${dto.subject}</span><br>
        				<span class="subject" style="color: gray; font-size: 11px">평점 ${dto.score} / 5.0 (참여 ${dto.recnum})</span>
        			</div>
        		</td>
        	</c:forEach>
        	
        	<c:set var="n" value="${list.size()}"/>
        	<c:if test="${n>0 && n%3 !=0}">
        		<c:forEach var="i" begin="${n%3+1}" end="3">
        			<td width="210">
        				<div class="imgLayout"></div>
        			</td>
        		</c:forEach>
        	</c:if>
        	</table>
        
			<table style="width: 100%; border-spacing: 0;">
			   <tr height="35">
				<td align="center">
			        ${dataCount==0?"등록된 게시물이 없습니다.":paging}
				</td>
			   </tr>
			</table>
			
			<table style="width: 100%; margin-top: 10px; margin-left: 10px; margin-right: 10px; border-spacing: 0;">
			   <tr height="40" >
			      <td align="left" width="100">
			          <button type="button" class="btn" onclick="javascript:location.href='${pageContext.request.contextPath}/store/list.do';">새로고침</button>
			      </td>
			      <c:if test="${sessionScope.member.type==0 || sessionScope.member.type==2}">
			      	<td align="right" width="100">
			          	<button type="button" class="btn" onclick="javascript:location.href='${pageContext.request.contextPath}/store/created.do';">글올리기</button>
			      	</td>
			      </c:if>
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