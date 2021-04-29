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
<style type="text/css">
.store-adr {
	background-color: #FF8A3D;
	color: white;
	font-size: 15px;
	padding: 0 10px;
}
.store-subject {
	font-size: 15px;
	font-weight: 500;
}

.store-rate {
	font-size: 14px;
	color: gray;
}

.store-detail {
	padding-left: 35px;
	border-top: 1px solid #ddd; 
	margin: 10px 0;
}

.store-detail li {
	margin: 10px 0;
}

.store-detail ul {
	display: inline-block;
	width: 50%;
	float: left;
}

.store-detail button {
	border: 0;
	background-color: #E9ECEF;
	color: #495057;
	font-weight: bold;
	padding: 10px 30px;
	padding-bottom: 12px;
}

</style>
</head>
<body>
	<div class="header">
	    <jsp:include page="/WEB-INF/views/layout/header.jsp"></jsp:include>
	</div>
		
	<div class="container">
    <div class="body-container" style="border:0;">
        <div class="list-title">
        	<table style="width: 100%; margin: 0px auto; border-spacing: 0px; padding-left: 20px; padding-right: 20px;">
        		<tr>
        			<td align="left">
        				<h3>우리동네 홍보</h3>
        			</td>
	        		<td align="right">
			        	<form name="searchForm" action="${pageContext.request.contextPath}/store/list.do" method="post">
						      <span style="margin-right: 10px;">지역선택</span>
						      <select name="keyword" class="selectField" onchange="searchLocation()">
						                  <option value="ALL" ${keyword=="ALL"?"selected='selected'":"" }>전체</option>
						                  <option value="서울" ${keyword=="서울"?"selected='selected'":"" }>서울</option>
						                  <option value="경기" ${keyword=="경기"?"selected='selected'":"" }>경기</option>
						                  <option value="인천" ${keyword=="인천"?"selected='selected'":"" }>인천</option>
						                  <option value="기타" ${keyword=="기타"?"selected='selected'":"" }>기타</option> 
						      </select>
						 </form>
					</td>
				</tr>
			</table>
        </div>
        
        <div>
        	<table style="width: 100%; border-spacing: 0; border-bottom: 1px solid #ddd;">
        	<c:forEach var="dto" items="${list}" varStatus="status">
        		<tr>
        		<c:if test="${status.index !=0 && status.index%3 ==0}">
        			<c:out value="</tr><tr>" escapeXml="false"/>
        		</c:if>
        		<td width="210" align="center" style="border-top: 1px solid #ddd">
        			<div class="imgLayout" style="margin: 10px 0;">
        				<img src="${pageContext.request.contextPath}/uploads/photo/${dto.imageFileName}" width="230" height="230"><br>
        			</div>
        		</td>
        		<td class="store-detail">
        			<ul>
        				<li>
        					<span class="store-adr">${dto.addrsub}</span>
        				</li>
        				<li>
        					<span class="store-subject">${dto.subject}</span>
        				</li>
        				<li>
        					<span class="store-rate">평점 ${dto.score} / 5.0 (참여 ${dto.recnum})</span>
        				</li>
        			</ul>
        			<ul>
        				<li>&nbsp;</li>
        				<li>
        					<button type="button" style="float: right;" onclick="location.href='${articleUrl}&num=${dto.num}';">
        					상세보기&nbsp;&nbsp;&nbsp;
        					<i class="fas fa-caret-right"></i></button>
        				</li>
        				<li>&nbsp;</li>
        			</ul>
        		</td>
        	</c:forEach>
        	
        	<c:set var="n" value="${list.size()}"/>
        	<c:if test="${n>0 && n%3 !=0}">
        		<c:forEach var="i" begin="${n%3+1}" end="3">
        			<tr>
        			<td width="210" align="center" style="border-top: 1px solid #ddd">
        				<div style="margin: 10px 0; border: 1px solid #ccc;">
        					<img src="${pageContext.request.contextPath}/resource/images/store_soon.png" width="229" height="229">
        				</div>
        			</td>
        			<td style="border-top: 1px solid #ddd">
        				<div style="padding-left: 20px;">
        				<span>준비 중입니다</span>
        				</div>
        		</td>
        		</c:forEach>
        	</c:if>
        	<c:if test="${n!=0}">
        		<c:out value="</tr>" escapeXml="false"/>
        	</c:if>
        	</table>
        
			<table style="width: 100%; margin-top: 10px auto;  margin-top: 30px; border-spacing: 0;">
			   <tr height="40" >
			      <td align="left" width="100">
			          <button type="button" class="btn" onclick="javascript:location.href='${pageContext.request.contextPath}/store/list.do';">새로고침</button>
			      </td>
				<td align="center">
			        ${dataCount==0?"등록된 게시물이 없습니다.":paging}
				</td>
			      <c:if test="${sessionScope.member.type==0 || sessionScope.member.type==2}">
			      	<td align="right" width="100">
			          	<button type="button" class="btn btnCreate" onclick="javascript:location.href='${pageContext.request.contextPath}/store/created.do';"><i class="fab fa-telegram-plane"></i>&nbsp;&nbsp;글쓰기</button>
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