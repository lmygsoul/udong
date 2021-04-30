<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>우동사리-마이프로필</title>
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
      
   <div class="container" style="height: 1430px; padding: 0; background-color: #FBF7F2; border-top: 0;">
       <div class="main-container" style="height: 1430px; width: 700px; position: relative;">
        <div class="memberFormBody">
            <div>
          <h3 class="member-title"> ${title} </h3>
          <p>&nbsp;</p>
        </div>
          <form name="memberForm" method="post">
             <table class="member-table-1">
                <tr>
                   <td class="formCol-1">
                      <label style="font-weight: 500;">아이디</label>
                   </td>
                   <td class="formCol-2">
                      <input type="text" name="userId" id="userId" maxlength="10" value="${dto.userId }"
                         ${mode!="member" ? "readonly='readonly' ":""}
                            placeholder="아이디" class="memberForm">
                   </td>
                </tr>
                 <c:if test="${mode=='myProfile' }">
                <tr>
                   <td class="formCol-1">
                      <label style="font-weight: 500;">이름</label>
                   </td>
                   <td class="formCol-2">
                      <input type="text" name="userName"maxlength="10" value="${dto.userName }" ${mode!="member" ? "readonly='readonly' ":""}
                            class="memberForm">
                   </td>
                </tr>
                </c:if>
                <tr>
                   <td class="formCol-1">
                      <label style="font-weight: 500;">닉네임</label>
                   </td>
                   <td class="formCol-2">
                      <input type="text" name="nickName" maxlength="10" value="${dto.nickName }" ${mode=="myProfile" ? "readonly='readonly' ":""}
                            class="memberForm">
                   </td>
                </tr>
                 <c:if test="${mode=='myProfile' }">
                <tr>
                   <td class="formCol-1">
                      <label style="font-weight: 500;">개인/사업자</label>
                   </td>
                   <td class="formCol-2">
                      <select name="type">
                                <option value="" disabled="disabled">선 택</option>
                             <option value="1" ${dto.type=="1" ? "selected='selected'" : ""} disabled="disabled" >개인</option>
                              <option value="2" ${dto.type=="2" ? "selected='selected'" : ""} disabled="disabled" >사업자</option>
                        </select>
                   </td>
                </tr>
                
                <tr>
                   <td class="formCol-1">
                      <label style="font-weight: 500;">생년월일</label>
                   </td>
                   <td class="formCol-2">
                      <input type="text" name="birth" ${mode!="member" ? "readonly='readonly' ":""}
                            class="memberForm" value="${birth}">
                   </td>
                </tr>
                <tr>
                   <td class="formCol-1">
                      <label style="font-weight: 500;">이메일</label>
                   </td>
                   <td class="formCol-2">
                      <select name="selectEmail" onchange="changeEmail();" style="margin-right: 13px;">
                                <option value="">선 택</option>
                             <option value="naver.com" ${dto.email2=="naver.com" ? "selected='selected'" : ""}>네이버 메일</option>
                              <option value="hanmail.net" ${dto.email2=="hanmail.net" ? "selected='selected'" : ""}>한 메일</option>
                             <option value="hotmail.com" ${dto.email2=="hotmail.com" ? "selected='selected'" : ""}>핫 메일</option>
                             <option value="gmail.com" ${dto.email2=="gmail.com" ? "selected='selected'" : ""}>지 메일</option>
                             <option value="direct">직접입력</option>
                        </select>
                     <input type="text" name="email1" value="${dto.email1}" ${mode=="myProfile" ? "readonly='readonly' ":""} size="13" maxlength="30"  class="memberForm2">
                     @
                     <input type="text" name="email2" value="${dto.email2}" ${mode=="myProfile" ? "readonly='readonly' ":""} size="13"maxlength="30"  class="memberForm2" style="background-color: #DEDEDE;" readonly="readonly">
                   </td>
                </tr>
                <tr>
                   <td class="formCol-1">
                      <label style="font-weight: 500;">전화번호</label>
                   </td>
                   <td class="formCol-2">
                      <select name="selectTel" style="margin-right: 0;">
                         <option value="">선 택</option>
                         <option value="010" ${dto.tel1=="010" ? "selected='selected'" : ""}>010</option>
                         <option value="011" ${dto.tel1=="011" ? "selected='selected'" : ""}>011</option>
                         <option value="016" ${dto.tel1=="016" ? "selected='selected'" : ""}>016</option>
                         <option value="017" ${dto.tel1=="017" ? "selected='selected'" : ""}>017</option>
                         <option value="018" ${dto.tel1=="018" ? "selected='selected'" : ""}>018</option>
                         <option value="019" ${dto.tel1=="019" ? "selected='selected'" : ""}>019</option>
                     </select>
                     <span>&nbsp;-&nbsp;</span> 
                     <input type="text" name="tel2" value="${dto.tel2}" ${mode=="myProfile" ? "readonly='readonly' ":""} size="13" maxlength="4"  class="memberForm2">
                     <span>&nbsp;-&nbsp;</span> 
                     <input type="text" name="tel3" value="${dto.tel3}" ${mode=="myProfile" ? "readonly='readonly' ":""} size="13" maxlength="4"  class="memberForm2" >
                   </td>
                </tr>
                <tr>
                   <td class="formCol-1">
                      <label style="font-weight: 500;">우편번호</label>
                   </td>
                   <td class="formCol-2">
                      <input type="text" name="zipcode" id="zipCode" value="${dto.zipCode}" ${mode=="myProfile" ? "readonly='readonly' ":""}
                                class="boxTF" readonly="readonly"  style="width: 126px; background-color: #DEDEDE; margin-left: 5px; height: 20px;">
                        <button type="button" onclick="daumPostcode();" class="btn" style="margin-left: 10px; background-color: #8c8c8c; color: white;">우편번호</button>      
                   </td>
                </tr>
                <tr>
                    <td class="formCol-1">
                      <label style="font-weight: 500;">주소</label>
                   </td>
                      <td class="formCol-2">
                        <input type="text" name="addr1" id="addr1" value="${dto.addr1}" ${mode=="myProfile" ? "readonly='readonly' ":""} maxlength="50" 
                           class="memberForm" style="background-color: #DEDEDE;" placeholder="기본 주소" readonly="readonly">
                
                           <input type="text" name="addr2" id="addr2" value="${dto.addr2}" ${mode=="myProfile" ? "readonly='readonly' ":""} maxlength="50" 
                             class="memberForm" placeholder="나머지 주소">
                     </td>
                </tr>
                </c:if>
                <tr >
                   <td width="100" height="50" valign="middle" style="text-align: left;">
                      <label style="font-weight: 500;">하고싶은 한 마디</label>
                   </td>
                   <td>
                      <input type="text" name="myComment" 
                            placeholder="하고싶은 말 한마디 적으세요" class="memberForm"
                            value="${dto.myComment }" ${mode=="myProfile" ? "readonly='readonly' ":""} >
                   </td>
                </tr>
                <tr >
                   <td class="formCol-1">
                      <label style="font-weight: 500;">가입일</label>
                   </td>
                   <td class="formCol-2">
                      <input type="text" name="created_date" 
                            class="memberForm"
                            value="${dto.created_date}" ${mode=="myProfile" ? "readonly='readonly' ":""} >
                   </td>
                </tr>
             </table>
             <c:if test="${mode=='myProfile' }">
             <table style="width:100%; margin: 20px auto; margin-bottom: 50px; border-spacing: 0px;">
                 <tr height="45"> 
                     <td align="center">
                        <button type="button" class="btn btnCreate" onclick="javascript:location.href='${pageContext.request.contextPath}/member/update.do?';">수정하기</button>
                     </td>
                  </tr>
             </table>
             </c:if>
             </form>
             <form method="post" action="${pageContext.request.contextPath}/member/sm_created.do">
					    <c:if test ="${sessionScope.member.userId != null && sessionScope.member.userId != dto.userId}">
					    	<input 	type="hidden" name="userId" value="${dto.userId}">
					  		<button type="submit" class="btn" 
					  		style="width: 50%; color: #495057; background-color: #eee; border: 0; font-weight: 700; margin-left: 200px;">
					  		<i class="fas fa-comment-dots"></i>&nbsp;&nbsp;쪽지 보내기</button>
					    </c:if>
	 </form>
       </div>
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