<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>${title }</title>
<jsp:include page="/WEB-INF/views/layout/staticHeader.jsp"/>
<script type="text/javascript">
    function sendOk() {
        var f = document.tableNameForm;

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

   		f.action="${pageContext.request.contextPath}/member/sm_created_ok.do";

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
            <h3>${title}</h3>
        </div>
        
        <div>
			<form name="tableNameForm" method="post" class="formBox">
			   <table class="create-table">
			  	<tr class="create-row"> 
			      <td class="create-col1">받는&nbsp;&nbsp;&nbsp;사람</td>
			      <td style="padding-left:10px;"> 
			        <input type="text" name="receiveUserId" maxlength="100" class="boxTF" style="width: 97%;" value="${mdto.userId}">
			      </td>
			  </tr>
			
			  <tr align="left" height="43" style="border-bottom: 1px solid #ccc;"> 
			      <td class="create-col1">보내는&nbsp;&nbsp;&nbsp;사람</td>
			      <td style="padding-left:10px;"> 
			          ${sessionScope.member.userName}
			      </td>
			  </tr>
			  <tr align="left" height="43" style="border-bottom: 1px solid #ccc; border-top: 1px solid #ccc;"> 
			      <td class="create-col1">제&nbsp;&nbsp;&nbsp;목</td>
			      <td style="padding-left:10px;"> 
			        <input type="text" name="subject" maxlength="100" class="boxTF" style="width: 97%;" >
			      </td>
			  </tr>
			
			  <tr align="left" style="border-bottom: 1px solid #ccc;"> 
			      <td class="create-col2">내&nbsp;&nbsp;&nbsp;&nbsp;용</td>
			      <td valign="top" style="padding:10px 0px 5px 10px;"> 
			        <textarea name="content" rows="12" class="boxTA" style="width: 97%;"></textarea>
			      </td>
			  </tr>
			  </table>
			
			  <table class="create-table2">
			     <tr height="45"> 
			      <td align="center" >
			        <button type="button" class="btn btnCreate" onclick="sendOk();"><i class="fab fa-telegram-plane"></i>&nbsp;&nbsp;보내기</button>
			        <button type="reset" class="btn">다시입력</button>
			        <button type="button" class="btn" onclick="javascript:location.href='${pageContext.request.contextPath}/member/sm_list.do?${query }';">작성취소</button>
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