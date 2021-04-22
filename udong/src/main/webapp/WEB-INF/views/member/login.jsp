<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Login</title>
<jsp:include page="/WEB-INF/views/layout/staticHeader.jsp"/>
<style type="text/css">
.login {
	width : 400px;
	height: 40px;
	padding: 5px;
	border: 1px solid #F5EBDF;
	margin-top:5px; margin-bottom:5px;
  	font-size:14px;
  	border-radius:4px;
  	font-family:"Noto Sans KR", "Malgun Gothic", "맑은 고딕", NanumGothic, 나눔고딕, 돋움, sans-serif;
}
.btnlogin{
	width : 410px;
	height: 50px;
	border: 1px solid #FF9933;
	background-color : #FF9933;
	color: white;
	font-size:20px;
  	border-radius:4px;
  	font-family:"Noto Sans KR", "Malgun Gothic", "맑은 고딕", NanumGothic, 나눔고딕, 돋움, sans-serif;
}
</style>
<script type="text/javascript">
function sendLogin() {
    var f = document.loginForm;

	var str = f.userId.value;
    if(!str) {
        alert("아이디를 입력하세요. ");
        f.userId.focus();
        return;
    }

    str = f.userPwd.value;
    if(!str) {
        alert("패스워드를 입력하세요. ");
        f.userPwd.focus();
        return;
    }

    f.action = "${pageContext.request.contextPath}/member/login_ok.do";
    f.submit();
}
</script>
</head>
<body>
<div class="header">
	    <jsp:include page="/WEB-INF/views/layout/header.jsp"></jsp:include>
	</div>
		
	<div class="container" style="background-color: #FBF7F2;">
	    <div class="main-container">
	    	<div style="margin: 80px auto 70px; width:410px;">
	    	<div style="text-align: center; padding-top: 150px;">
	        	<span style="font-weight: 500; font-size:27px; color: #424951;">회원 로그인</span>
	        </div>
	        
			<form name="loginForm" method="post" >
			  <table style="margin: 15px auto; width: 360px; border-spacing: 0px;">
			  <tr align="center" height="60"> 
			      <td> 
			        <input type="text" name="userId" id="userId"maxlength="15"
			                   tabindex="1" placeholder="아이디" class="login">
			      </td>
			  </tr>
			  <tr align="center" height="60"> 
			      <td>
			        <input type="password" name="userPwd" id="userPwd" maxlength="20" 
			                   tabindex="2" placeholder="비밀번호" class="login">
			      </td>
			  </tr>
			  <tr align="center" height="65" > 
			      <td>
			        <button type="button" onclick="sendLogin();" class="btnlogin">로그인</button>
			      </td>
			  </tr>

			  <tr align="center" height="60">
			      <td>
			       		<a href="${pageContext.request.contextPath}/">아이디찾기</a>&nbsp;&nbsp;&nbsp;
			       		<a href="${pageContext.request.contextPath}/">패스워드찾기</a>&nbsp;&nbsp;&nbsp;
			       		<a href="${pageContext.request.contextPath}/member/member.do">회원가입</a>
			      </td>
			  </tr>
			  
			  <tr align="center" height="40" >
			    	<td><span style="color: blue;">${message}</span></td>
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
<script type="text/javascript" src="${pageContext.request.contextPath}/resource/jquery/js/jquery-ui.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/resource/jquery/js/jquery.ui.datepicker-ko.js"></script>
</body>
</html>