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
function sendOk(){
	var f = document.body;
	
	str = f.content.value;
	if(!str) {
		alert("빈내용으로 출석체크를 할순 없어요. ");
		f.content.focus();
		return;
	}
	f.action="${pageContext.request.contextPath}/member/check_ok.do"
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
        				<h3>&nbsp;출석체크</h3>
        			</td>
			    </tr>
        	</table>
        </div>
        
  
        <div class="body-board">
			<table style="width: 100%; margin: 20px auto 0px; border-spacing: 0;">
			   <tr>
			   	<td style="padding: 0 10px 20px;">
			   		<h3 style="font-size: 18px; font-weight: 500;">오늘도 우동사리에 와줘서 고마워요!&nbsp;<i class="fas fa-smile-wink"></i></h3>
			   	</td>
			   </tr>
			   
			   <tr height="35">
			      <td align="left" width="50%" style="padding: 0 10px">
			           출석 인원 : ${dataCount}명
			      </td>
			      <td align="right">
			          &nbsp;
			      </td>
			   </tr>
			</table>
			
			<c:if test="${sessionScope.member.created==ato.created }">
			<form name="body" method="post"  >
			<table style="width: 100%; border-spacing: 0; border-collapse: collapse; background-color: #F5EBDF;" >
			  <tr height="35">
			    <td width="15%" align="left" style="padding-left: 5px; text-align: center;">
			      이름 : ${sessionScope.member.userName}
			    </td>
			    <td width="55%" align="right" style="padding: 15px 0 10px;">
				   <textarea name="content" rows="2" class="boxTA" style="width: 480px; height:17px; padding: 10px; border: 0;" placeholder="오늘의 한마디를 적어주세요"></textarea>
			    </td>
			    <td width="13%" style="padding: 0 10px;">
			    	<input type="hidden" name="num" value="${ato.num}">
			    	<input type="hidden" name="num" value="${ato.userId}">
			    	<input type="hidden" name="num" value="${ato.userName}">
			    	<input type="hidden" name="num" value="${ato.content}">
			    	<input type="hidden" name="num" value="${ato.created}">
			    	<input type="hidden" name="num" value="${ato.checkType}">
				    <button type="button" class="btn" 
				    style="background-color: #FF8A3D; color: white; font-weight: 700; padding: 10px 20px; border: 0;"
				    onclick="sendOk(); this.onclick='';">출석</button>
				 </td>
			</tr>
			</table>
			</form>
			</c:if>
			 <div class="body-board">
			<table style="width: 100%; margin: 0px auto; border-spacing: 0px; border-collapse: collapse;">
			<tr class="table-row2">
				  <th width="10%">등수</th>
			      <th width="60%">이야기</th>
			      <th width="10%">이름</th>
			      <th width="20%">출첵일</th>
			</tr>
			 <c:forEach var="ato" items="${list}">
			    <tr class="table-row2">
			    	<td>${ato.listNum }</td>
			      <td style="text-align: left; padding-left: 20px">${ato.content}</td>
			      <td>${ato.userName}</td>
			      <td>${ato.created}</td>
			      <td style="display: none;" >${ato.checkType }</td>
			    </tr>
			</c:forEach> 
			</table>
		</div>
			
			<table style="width: 100%; margin: 10px auto;  margin-top: 30px; border-spacing: 0px;">
			   <tr height="40">
			   	<td align="left" width="100">
			          <button type="button" class="btn" onclick="javascript:location.href='${pageContext.request.contextPath}/member/check_list.do';">새로고침</button>
			      </td>
			   <c:if  test="${sessionScope.member.userId=='admin'||sessionScope.member.userId=='admin1'||sessionScope.member.userId=='admin2'||sessionScope.member.userId=='admin3'||sessionScope.member.userId=='admin4'||sessionScope.member.userId=='admin5'||sessionScope.member.userId=='admin6'}">
			   <td align="right" width="100">
			          <button type="button" class="btn" onclick="javascript:location.href='${pageContext.request.contextPath}/member/check_delete.do';">삭제</button>
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