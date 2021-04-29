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

<style type="text/css">
.imgLayout{
	width: 190px;
	height: 205px;
	padding: 10px 5px 10px;
	margin: 5px;
	border: 1px solid #DAD9FF;
     cursor: pointer;
}
.subject {
     display: inline-block;
     width:180px;
     height:25px;
     line-height:25px;
     margin:5px auto;
     border-top: 1px solid #DAD9FF;
     white-space:nowrap;
     overflow:hidden;
     text-overflow:ellipsis;
}
</style>

</head>
<body>

<div class="header">
    <jsp:include page="/WEB-INF/views/layout/header.jsp"></jsp:include>
</div>
	
<div class="container">
    <div class="body-container" style="width: 1000px;">
        <div class="body-title">
            <h3><i class="far fa-image"></i> 우동사진 </h3>
        </div>
        
        <div>
        	<table style="width: 630px; margin: 0 auto; border-spacing: 0">
				<c:forEach var="dto" items="${list}" varStatus="status">
					<c:if test="${status.index==0}">
						<tr>
					</c:if>
					<c:if test="${status.index !=0 && status.index%3 == 0}">
						<c:out value="</tr><tr>" escapeXml="false"/>
					</c:if>
					<td width="210" align="center">
						<div class="imgLayout" onclick="javascript:location.href='${articleUrl}&num=${dto.num}';">
							<img src="${pageContext.request.contextPath}/uploads/photo/${dto.imageFilename}" width="180" height="180">
							<span class="subject">${dto.subject}</span>
						</div>
					</td>
				</c:forEach>
				
				<c:set var="n" value="${list.size()}"/>
				<c:if test="${n > 0 && n%3 != 0}">
					<c:forEach var="i" begin="${n%3+1}" end="3">
						<td width="210">
							<div class="imgLayout">&nbsp;</div>
						</td>
					</c:forEach>
				</c:if>
				<c:if test="${n!=0}">
					<c:out value="</tr>" escapeXml="false"/>
				</c:if>
			</table>
           
           <table style="width:100%; border-spacing:0px;">
              <tr height="50">
                 <td align="center">
                   ${dataCount==0?"등록된 게시물이 없습니다.":paging}
                 </td>
              </tr>
           </table>

			<table style="width: 100%; margin: 10px auto; border-spacing: 0px;">
			   <tr height="40">
			      <td align="left" width="50%">
			          &nbsp;
			      </td>
			      <td align="right" width="50%">
			          <button type="button" class="btn" onclick="javascript:location.href='${pageContext.request.contextPath}/udongphoto/created.do';">사진올리기</button>
			      </td>
			   </tr>
			</table>
			<c:if test="${mode=='myContent' }">
			<table style="width: 100%; margin: 10px auto; border-spacing: 0px;">
			   <tr height="40">
			     <td align="left" width="100">
			          <button type="button" class="btn" onclick="javascript:location.href='${pageContext.request.contextPath}/member/up_list.do';">새로고침</button>
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
			      <td align="right" width="50%">
			          <button type="button" class="btn" onclick="javascript:location.href='${pageContext.request.contextPath}/member/ud_list.do';">우동이야기</button>
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