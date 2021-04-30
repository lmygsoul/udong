<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>spring</title>
<jsp:include page="/WEB-INF/views/layout/staticHeader.jsp"/>
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
        				<h3>&nbsp;우리동네 사진</h3>
        			</td>
			      </tr>
        	</table>
        </div>
        
        <div class="body-photo">
        	<table style="width: 100%; margin: 35px auto 0px; border-spacing: 0px; border-collapse: collapse;">
				<c:forEach var="dto" items="${list}" varStatus="status">
					<c:if test="${status.index==0}">
						<tr>
					</c:if>
					<c:if test="${status.index !=0 && status.index%3 == 0}">
						<c:out value="</tr><tr>" escapeXml="false"/>
					</c:if>
					<td width="210" align="center">
						<div class="imgLayout" onclick="javascript:location.href='${articleUrl}&num=${dto.num}';">
							<img src="${pageContext.request.contextPath}/uploads/photo/${dto.imageFilename}"  >
							<table style="border-collapse: collapse;">
								<tr>
									<td colspan="2" width="190">
									<span class="subject">${dto.subject}</span>
									</td>
								</tr>
								<tr>
									<td colspan="2" width="190">
									<span class="subject" style="font-weight: 300;">${dto.userName}</span>
									</td>
								</tr>
								<tr style="background-color: #F8F9FA;">
									<td width="95" align="left">
									<span class="subject2"><i class="fas fa-eye"></i>&nbsp;&nbsp;${dto.hitCount}</span>
									</td>
									<td width="95" align="right">
									<span class="subject2">${dto.created}</span>
									</td>
								</tr>
							</table>
						</div>
					</td>
				</c:forEach>
				
				<!-- 공백 -->
				<c:set var="n" value="${list.size()}"/>
				<c:if test="${n > 0 && n%3 != 0}">
					<c:forEach var="i" begin="${n%3+1}" end="3">
						<td width="210" align="center">
							<div class="imgLayout">&nbsp;</div>
						</td>
					</c:forEach>
				</c:if>
				<c:if test="${n!=0}">
					<c:out value="</tr>" escapeXml="false"/>
				</c:if>
			</table>
           
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
			<c:if test="${mode!='myContent' }">
			<table style="width: 100%; margin: 10px auto; margin-top: 30px; border-spacing: 0px;">
			   <tr height="40">
			      <td align="left" width="100">
			          <button type="button" class="btn" onclick="javascript:location.href='${pageContext.request.contextPath}/udongPhoto/list.do';">새로고침</button>
			      </td>
			      <c:if test="${dataCount!=0}">
			      <td align="center">
			      	${paging}
			      </td>
			      </c:if>
			      <td align="right" width="200">
			          <button type="button" class="btn btnCreate" onclick="javascript:location.href='${pageContext.request.contextPath}/udongphoto/created.do';"><i class="fab fa-telegram-plane"></i>&nbsp;&nbsp;사진 올리기</button>
			      </td>
			   </tr>
			</table>
			</c:if>
			
			<c:if test="${mode=='myContent' }">
			<table class="myContents">
			   <tr height="40">
			      <td align="left" width="100">
			          <button type="button" class="btn" onclick="javascript:location.href='${pageContext.request.contextPath}/member/up_list.do';">새로고침</button>
			      </td>
			   </tr>
			   <tr height="40">
			      <td align="left">
			          <button type="button" class="btnTab" onclick="javascript:location.href='${pageContext.request.contextPath}/member/gt_list.do';">가입인사</button>
			      </td>
			      <td align="left">
			          <button type="button" class="btnTab" onclick="javascript:location.href='${pageContext.request.contextPath}/member/nb_list.do';">우동자랑</button>
			      </td>
			      <td align="left">
			          <button type="button" class="btnTab" onclick="javascript:location.href='${pageContext.request.contextPath}/member/qa_list.do';">우동지식</button>
			      </td>
			      <td align="left">
			          <button type="button" class="btnTab" onclick="javascript:location.href='${pageContext.request.contextPath}/member/st_list.do';">우동홍보</button>
			      </td>
			      <td align="left">
			          <button type="button" class="btnTab" onclick="javascript:location.href='${pageContext.request.contextPath}/member/ud_list.do';">우동이야기</button>
			      </td>
			      <td align="left">
			          <button type="button" class="btnTab" onclick="javascript:location.href='${pageContext.request.contextPath}/member/used_list.do';">중고거래</button>
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