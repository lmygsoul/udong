<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>우동사리-회원가입</title>
<style type="text/css">
.memberForm {
	width : 400px;
	height: 40px;
	padding: 5px;
	border: 1px solid #F5EBDF;
	margin-top:5px; margin-bottom:5px;
  	font-size:14px;
  	border-radius:4px;
  	font-family:"Noto Sans KR", "Malgun Gothic", "맑은 고딕", NanumGothic, 나눔고딕, 돋움, sans-serif;
}
</style>
<script type="text/javascript">
function memberOk(){
	var f = document.memberForm;
	var str;
	
	str = f.userId.value;
	str = str.trim();
	if(!str){
		alert("아이디를 입력하세요.");
		f.userId.focus();
		return;
	} else if(!/^[a-z0-9][a-z0-9]{6,14}$/i.test(str)){
		alert("아이디는 7~15자입니다.");
		f.userId.focus();
		return;
	}
	str = f.userPwd.value;
	str = str.trim();
	if(!str){
		alert("비밀번호를 입력하세요.");
		f.userId.focus();
		return;
	} else if(!/^[a-z0-9][a-z0-9]{4,9}$/i.test(str)){
		alert("비밀번호는 5~10자리 입니다.영어와 숫자를 조합해서 입력하세요");
		f.userId.focus();
		return;
	}
	if(str!=f.userPwd_check.value){
		alert("비밀번호가 동일하게 입력되지 않았습니다.");
		f.userPwd_check.focus();
		return;
	}
	str = f.nickName.value;
	str = str.trim();
	if(!str){
		alert("닉네임을 입력하세요");
		f.nickName.focus();
		return
	} else if(!/^[a-zA-Z가-힣]{0,4}$/i.test(str)){
		alert("닉네임은 5자리이하로 작성하세요.");
		return;
	}
	str= f.birth.value;
	str= str.trim();
	if(!str || !isValidDateFormat(str)){
		alert("생년월일을 입력하세요");
		return;
	}
	str = f.selectTel.value;
	str = str.trim();
	if(!str){
		alert("앞자리 전화번호를 선택하세요.");
		return;
	}
	str = f.tel2.value;
	str = str.trim();
	if(!str){
		alert("가운데 전화번호를 입력하세요");
		return;
	}
	str = f.tel3.value;
	str = str.trim();
	if(!str){
		alert("마지막 전화번호를 입력하세요");
		return;
	}
	str = f.email1.value;
	str = str.trim();
	if(!str){
		alert("이메일 앞부분을 입력하세요");
		return;
	}
	str = f.email2.value;
	str = str.trim();
	if(!str){
		alert("도메인을 선택하세요");
		return;
	}
	
	f.action = "${pageContext.request.contextPath}/member/${mode}_ok.do";
    f.submit();
}
function changeEmail() {
    var f = document.memberForm;
	    
    var str = f.selectEmail.value;
    if(str!="direct") {
        f.email2.value=str; 
        f.email2.readOnly = true;
        f.email1.focus(); 
    }
    else {
        f.email2.value="";
        f.email2.readOnly = false;
        f.email1.focus();
    }
}
</script>
<jsp:include page="/WEB-INF/views/layout/staticHeader.jsp"/>
</head>
<body>

	<div class="header">
	    <jsp:include page="/WEB-INF/views/layout/header.jsp"></jsp:include>
	</div>
		
	<div class="container" style="background-color: #FBF7F2;" style="overflow: scroll;">
	    <div class="main-container">
	    <div>
            <h3 style="text-align: center ; height: 40px; padding-top: 20px; margin: 0 0 20px 0; font-size: 20px; border-bottom: 2px solid #FF9933"> ${title} </h3>
        </div>
	    	<form name="memberForm" method="post">
	    		<table style="width: 100%;">
	    			<tr>
	    				<td width="100" valign="middle" style="text-align: right;">
	    					<label style="font-weight: bold;">아이디</label>
	    				</td>
	    				<td>
	    					<input type="text" name="userId" id="userId" maxlength="10" value="${dto.userId }"  ${mode=="update" ? "readonly='readonly' ":""}
			                   placeholder="아이디" class="memberForm" style="width: 400px; margin-left: 80px; margin-bottom: 5px;">
			                 <p style="margin-left: 80px;"> ${mode=="update" ? "*아이디는 수정되지 않습니다*":"아이디는 7~15자 이내이며, 영어와 숫자를 조합하여 작성해야합니다."}
	    				</td>
	    			</tr>
	    			<tr>
	    				<td width="100" valign="middle" style="text-align: right;">
	    					<label style="font-weight: bold;">비밀번호</label>
	    				</td>
	    				<td>
	    					<input type="password" name="userPwd" maxlength="10"  value="${dto.userPwd }"
			                   placeholder="비밀번호" class="memberForm" style="width: 400px; margin-left: 80px; margin-bottom: 5px;">
			                	<p style="margin-left: 80px;">비밀번호는 5~10자 이내이며, 영어와 숫자를 조합 입력해야합니다.
	    				</td>
	    			</tr>
	    			<tr>
	    				<td width="100" valign="middle" style="text-align: right;">
	    					<label style="font-weight: bold;">비밀번호 확인</label>
	    				</td>
	    				<td>
	    					<input type="password" name="userPwd_check" maxlength="10" 
			                   placeholder="비밀번호 확인" class="memberForm" style="width: 400px; margin-left: 80px; margin-bottom: 5px;">
			                 <p style="margin-left: 80px;">비밀번호를 동일하게 입력해야합니다. 비밀번호는 5~10자 이내이며, 영어와 숫자를 조합 입력해야합니다.
	    				</td>
	    			</tr>
	    			<tr>
	    				<td width="100" valign="middle" style="text-align: right;">
	    					<label style="font-weight: bold;">이름</label>
	    				</td>
	    				<td>
	    					<input type="text" name="userName"maxlength="10" value="${dto.userName }"  ${mode=="update" ? "readonly='readonly' ":""}
			                   class="memberForm" style="width: 200px; margin-left: 80px; margin-bottom: 5px;">
			                 <p style="margin-left: 80px;"> ${mode=="update" ? "*이름은 수정되지 않습니다*":""}
	    				</td>
	    			</tr>
	    			<tr>
	    				<td width="100" valign="middle" style="text-align: right;">
	    					<label style="font-weight: bold;">닉네임</label>
	    				</td>
	    				<td>
	    					<input type="text" name="nickName" maxlength="10" value="${dto.nickName }" 
			                   class="memberForm" style="width: 200px; margin-left: 80px; margin-bottom: 5px;">
	    				</td>
	    			</tr>
	    			<tr>
	    				<td width="100" valign="middle" style="text-align: right;">
	    					<label style="font-weight: bold;">개인/사업자</label>
	    				</td>
	    				<td>
	    					<label><input type="radio" name="type1" class="memberForm" value="1"  
	    					style="width: 50px; height:30px; margin-left: 70px;" >개인</label>
	    					<label><input type="radio" name="type2" class="memberForm" value="2" 
	    					style="width: 50px; height:30px; margin-left: 70px;" >사업자</label>
	    					<p style="margin-left: 80px;">*선택하지 않을시 개인으로 선택됩니다.
	    				</td>
	    			</tr>
	    			<tr>
	    				<td width="100" valign="middle" style="text-align: right;">
	    					<label style="font-weight: bold;">생년월일</label>
	    				</td>
	    				<td>
	    					<input type="text" name="birth"  value="${dto.birth }"  ${mode=="update" ? "readonly='readonly' ":""}
 			                   class="memberForm" style="width: 400px; margin-left: 80px; margin-bottom: 5px;" value="${dto.birth }">
			                 <p style="margin-left: 80px;">생년월일의 작성 예시) 1111-22-33 형식으로 입력합니다.
	    				</td>
	    			</tr>
	    			<tr>
	    				<td width="100" valign="middle" style="text-align: right;">
	    					<label style="font-weight: bold;">이메일</label>
	    				</td>
	    				<td>
	    					<select name="selectEmail" onchange="changeEmail();" style="margin-left: 80px; margin-top: 10px;">
			               	 	 <option value="">선 택</option>
			                	 <option value="naver.com" ${dto.email2=="naver.com" ? "selected='selected'" : ""}>네이버 메일</option>
			              		 <option value="hanmail.net" ${dto.email2=="hanmail.net" ? "selected='selected'" : ""}>한 메일</option>
			                	 <option value="hotmail.com" ${dto.email2=="hotmail.com" ? "selected='selected'" : ""}>핫 메일</option>
			                	 <option value="gmail.com" ${dto.email2=="gmail.com" ? "selected='selected'" : ""}>지 메일</option>
			                	 <option value="direct">직접입력</option>
			            	</select>
			            <input type="text" name="email1" value="${dto.email1}" size="13" maxlength="30"  class="boxTF">
			            @
			            <input type="text" name="email2" value="${dto.email2}" size="13"maxlength="30"  class="boxTF" readonly="readonly">
	    				</td>
	    			</tr>
	    			<tr>
	    				<td width="100" valign="middle" style="text-align: right;">
	    					<label style="font-weight: bold;">전화번호</label>
	    				</td>
	    				<td>
	    					<select name="selectTel" style="margin-left: 80px; margin-top: 10px;">
			                <option value="">선 택</option>
			                <option value="010" ${dto.tel1=="010" ? "selected='selected'" : ""}>010</option>
			                <option value="011" ${dto.tel1=="010" ? "selected='selected'" : ""}>011</option>
			                <option value="016" ${dto.tel1=="016" ? "selected='selected'" : ""}>016</option>
			                <option value="017" ${dto.tel1=="017" ? "selected='selected'" : ""}>017</option>
			                <option value="018" ${dto.tel1=="018" ? "selected='selected'" : ""}>018</option>
			                <option value="019" ${dto.tel1=="019" ? "selected='selected'" : ""}>019</option>
			            </select>
			            -
			            <input type="text" name="tel2" value="${dto.tel2}" size="13" maxlength="4"  class="boxTF">
			            -
			            <input type="text" name="tel3" value="${dto.tel3}" size="13" maxlength="4"  class="boxTF" >
	    				</td>
	    			</tr>
	    			<tr>
	    				<td width="100" height="50" valign="middle" style="text-align: right;">
	    					<label style="font-weight: bold;">우편번호</label>
	    				</td>
	    				<td>
	    					<input type="text" name="zipcode" id="zipCode" value="${dto.zipCode}"
			                       class="boxTF" readonly="readonly"  style="width: 30%; height:30px; margin-left: 80px; margin-bottom: 5px;">
			            	<button type="button" onclick="daumPostcode();" style="width: 10%; height:30px; margin-bottom: 5px;">우편번호</button>      
	    				</td>
	    			</tr>
	    			<tr>
			     		<td width="100" height="50" valign="middle" style="text-align: right;">
	    					<label style="font-weight: bold;">주소</label>
	    				</td>
			      		 <td>
			            	<input type="text" name="addr1" id="addr1" value="${dto.addr1}" maxlength="50" class="boxTF" 
			                        style="width: 70%; height:50px; margin-left: 80px; margin-bottom: 5px; " placeholder="기본 주소" readonly="readonly">
			       
			           		 <input type="text" name="addr2" id="addr2" value="${dto.addr2}" maxlength="50" 
			                       style="width: 70%; height:50px; margin-left: 80px; margin-bottom: 5px; " placeholder="나머지 주소">
			     		 </td>
			 		</tr>
	    			<tr >
	    				<td width="100" height="50" valign="middle" style="text-align: right;">
	    					<label style="font-weight: bold;">하고싶은 한 마디</label>
	    				</td>
	    				<td>
	    					<input type="text" name="myComment" 
			                   placeholder="하고싶은 말 한마디 적으세요" class="memberForm" style="width: 700px; height:50px; margin-left: 80px; margin-bottom: 5px;"
			                   value="${dto.myComment }" >
	    				</td>
	    			</tr>
	    		</table>
	    		
	    		<table style="width:100%; margin: 0px auto; border-spacing: 0px;">
			     	<tr height="45"> 
			      		<td align="center" >
			        		<button type="button" name="sendButton" class="btn" onclick="memberOk()">${mode=="member"?"회원가입":"정보수정"}</button>
			        		<button type="reset" class="btn" ${mode!="myProfile" ? "visible='hidden' ":""}>다시입력</button>
			        		<button type="button" class="btn" onclick="javascript:location.href='${pageContext.request.contextPath}/';">${mode=="member"?"가입취소":"수정취소"}</button>
			      		</td>
			   		</tr>
			    </table>
	    	</form>
	    </div>
	</div>
	
	<div class="footer">
	    <jsp:include page="/WEB-INF/views/layout/footer.jsp"></jsp:include>
	</div>
	<script src="http://dmaps.daum.net/map_js_init/postcode.v2.js"></script>
<script>
    function daumPostcode() {
        new daum.Postcode({
            oncomplete: function(data) {
                // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.

                // 각 주소의 노출 규칙에 따라 주소를 조합한다.
                // 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
                var fullAddr = ''; // 최종 주소 변수
                var extraAddr = ''; // 조합형 주소 변수

                // 사용자가 선택한 주소 타입에 따라 해당 주소 값을 가져온다.
                if (data.userSelectedType === 'R') { // 사용자가 도로명 주소를 선택했을 경우
                    fullAddr = data.roadAddress;

                } else { // 사용자가 지번 주소를 선택했을 경우(J)
                    fullAddr = data.jibunAddress;
                }

                // 사용자가 선택한 주소가 도로명 타입일때 조합한다.
                if(data.userSelectedType === 'R'){
                    //법정동명이 있을 경우 추가한다.
                    if(data.bname !== ''){
                        extraAddr += data.bname;
                    }
                    // 건물명이 있을 경우 추가한다.
                    if(data.buildingName !== ''){
                        extraAddr += (extraAddr !== '' ? ', ' + data.buildingName : data.buildingName);
                    }
                    // 조합형주소의 유무에 따라 양쪽에 괄호를 추가하여 최종 주소를 만든다.
                    fullAddr += (extraAddr !== '' ? ' ('+ extraAddr +')' : '');
                }

                // 우편번호와 주소 정보를 해당 필드에 넣는다.
                document.getElementById('zipCode').value = data.zonecode; //5자리 새우편번호 사용
                document.getElementById('addr1').value = fullAddr;

                // 커서를 상세주소 필드로 이동한다.
                document.getElementById('addr2').focus();
            }
        }).open();
    }
</script>    
<jsp:include page="/WEB-INF/views/layout/staticFooter.jsp"/>

</body>
</html>