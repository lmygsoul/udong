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
    function sendOk() {
        var f = document.classForm;

    	var str = f.subject.value;
        if(!str) {
            alert("제목을 입력하세요. ");
            f.subject.focus();
            return;
        }

    	str = f.content.value;
        if(!str) {
            alert("내용을 입력하세요. ");
            f.content.focus();
            return;
        }
        
    	str = f.maxClass.value;
        if(!str) {
            alert("모집인원을 입력하세요. ");
            f.maxClass.focus();
            return;
        }
        
    	str = f.maxClass.value;
        if(str <= 1) {
            alert("클래스 최소인원은 최소 2명입니다. ");
            f.maxClass.focus();
            return;
        }
        
   		f.action="${pageContext.request.contextPath}/dayclass/${mode}_ok.do";

        f.submit();
    }
</script>
</head>
<body>

<div class="header">
    <jsp:include page="/WEB-INF/views/layout/header.jsp"></jsp:include>
</div>
	
<div class="container">
    <div class="body-container">
        <div class="body-title" style="margin-bottom: 0; border-bottom: 0;">
            <h3>우리동네 클래스</h3>
        </div>
        
        <div>
			<form name="classForm" method="post" class="formBox">
			   <table class="create-table">
			  	<tr class="create-row">
			      <td class="create-col1">제&nbsp;&nbsp;&nbsp;&nbsp;목</td>
			      <td style="padding-left:10px;"> 
			        <input type="text" name="subject" maxlength="100" class="boxTF" style="width: 97%;" value="${dto.subject}">
			      </td>
			  </tr>
			
			  <tr align="left" height="43" style="border-bottom: 1px solid #ccc;"> 
			      <td class="create-col1">작성자</td>
			      <td style="padding-left:10px;"> 
			          ${sessionScope.member.userName}
			      </td>
			  </tr>
			
			  <tr align="left" style="border-bottom: 1px solid #ccc;"> 
			      <td class="create-col2">내&nbsp;&nbsp;&nbsp;&nbsp;용</td>
			      <td valign="top" style="padding:10px 0px 5px 10px;"> 
			        <textarea name="content" rows="12" class="boxTA" style="width: 97%;">${dto.content}</textarea>
			      </td>
			  </tr>
			  
			  <tr align="left" height="43" style="border-bottom: 1px solid #ccc;"> 
			      <td class="create-col1">모집인원</td>
			      <td style="padding-left:10px;"> 
			        <input type="number" name="maxClass" maxlength="100" class="boxTF" style="width: 97%;" value="${dto.maxClass}">
			      </td>
			  </tr>
			  </table>
			
			   <table class="create-table2">
			     <tr height="45"> 
			      <td align="center" >
			      	<input type="hidden" name="boardNum" value="${dto.boardNum}">
			      	<input type="hidden" name="condition" value="${condition}">
			        <input type="hidden" name="keyword" value="${keyword}">
			      	<input type="hidden" name="page" value="${page}">
			      	
			      	<c:choose>
			      		<c:when test="${mode=='update'}">
			        		<button type="button" class="btn btnCreate" onclick="sendOk();">수정하기</button>
			        	</c:when>
			        	<c:otherwise>
			        		<button type="button" class="btn btnCreate" onclick="sendOk();">등록하기</button>
			        	</c:otherwise>
			        </c:choose>
			        <button type="reset" class="btn">다시입력</button>
			        <button type="button" class="btn" onclick="javascript:location.href='${pageContext.request.contextPath}/dayclass/list.do';">${mode=='update'?'수정취소':'등록취소'}</button>
			      </td>
			    </tr>
			  </table>
			</form>
        </div>

    </div>
</div>

<div class="footer">
    <jsp:include page="/WEB-INF/views/layout/footer.jsp"></jsp:include>
</div>
<jsp:include page="/WEB-INF/views/layout/staticFooter.jsp"/>
</body>
</html>