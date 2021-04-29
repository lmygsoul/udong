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
<link rel="stylesheet" href="${pageContext.request.contextPath}/resource/css/style.css" type="text/css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resource/css/layout.css" type="text/css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resource/jquery/css/smoothness/jquery-ui.min.css" type="text/css">

<script type="text/javascript" src="${pageContext.request.contextPath}/resource/js/util.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/resource/jquery/js/jquery.min.js"></script>
<script type="text/javascript">
    function sendOk() {
        var f = document.photoForm;

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

        var mode = "${mode}";
        if(mode=="created" || (mode=="update" && f.selectFile.value != "")){
        	if(!/(\.gif|\.jpg|\.png|\.jpeg)$/i.test(f.selectFile.value)){
        		alert("이미지 파일만 가능합니다.");
        		f.selectFile.focus();
        		return;
        	}
        }
    	f.action="${pageContext.request.contextPath}/store/${mode}_ok.do";

        f.submit();
    }
</script>
<jsp:include page="/WEB-INF/views/layout/staticHeader.jsp"/>
</head>
<body>
	<div class="header">
	    <jsp:include page="/WEB-INF/views/layout/header.jsp"></jsp:include>
	</div>
	
	<div class="container">
    <div class="body-container" style="width: 700px;">
        <div class="body-title" style="margin-bottom: 0; border-bottom: 0;">
            <h3><i class="fas fa-bullhorn"></i>&nbsp;&nbsp;우리동네 홍보</h3>
        </div>
        
        <div>
			<form name="photoForm" method="post" enctype="multipart/form-data" class="formBox">
			  <table class="create-table">
			  <tr class="create-row"> 
			      <td class="create-col1">제&nbsp;&nbsp;&nbsp;&nbsp;목</td>
			      <td style="padding-left:10px;"> 
			          <input type="text" name="subject" maxlength="100" class="boxTF" style="width: 95%;" value="${dto.subject }">
			      </td>
			  </tr>

			  <tr align="left" height="40" style="border-bottom: 1px solid #cccccc;"> 
			      <td class="create-col1">작성자</td>
			      <td style="padding-left:10px;"> 
			          ${sessionScope.member.userName}
			      </td>
			  </tr>
			
			  <tr align="left" style="border-bottom: 1px solid #cccccc;"> 
			      <td class="create-col2">내&nbsp;&nbsp;&nbsp;&nbsp;용</td>
			      <td valign="top" style="padding:10px 0px 5px 10px;"> 
			          <textarea name="content" rows="12" class="boxTA" style="width: 95%;">${dto.content}</textarea>
			      </td>
			  </tr>
			  <tr align="left" height="40" style="border-bottom: 1px solid #cccccc;">
			  		<td class="create-col1">주&nbsp;&nbsp;&nbsp;&nbsp;소</td>
			  	    <td style="padding: 10px; position: relative;"> 
	    				<input type="text" name="address" id="address" value="${dto.addr}" class="boxTF" readonly="readonly"  style="width: 70%; height:15px;">
			        	<button type="button" onclick="daumPostcode();" style="width: 60px; height:25px; position: absolute; left: 415px;">검색</button>      
	    			</td>
	    	  </tr>
			  <tr align="left" height="40" style="border-bottom: 1px solid #cccccc;">
			      <td class="create-col1">이미지</td>
			      <td style="padding: 10px;"> 
			           <input type="file" name="selectFile" accept="image/*"
			                      class="boxTF" size="53" style="height: 25px; border: 0;">
			       </td>
			  </tr>

			  <c:if test="${mode=='update'}">
				  <tr align="left" height="40" style="border-bottom: 1px solid #cccccc;">
				      <td class="create-col1">등록이미지</td>
				      <td style="padding-left:10px;"> 
				         <img src="${pageContext.request.contextPath}/uploads/photo/${dto.imageFileName}"
				                     width="30" height="30" border="0" style="vertical-align: middle;" >
				         <span style="vertical-align: middle; font-size: 11px; color: #333;">(새로운 이미지가 등록되면 기존 이미지는 삭제 됩니다.)</span>
				       </td>
				  </tr> 
			  </c:if>
			  </table>
			
			  <table style="width: 100%; border-spacing: 0px; margin-top: 5px;">
			     <tr height="45"> 
			      <td align="center" >
			      	<c:if test = "${mode=='update'}">
			      		<input type="hidden" name="num" value="${dto.num}">
			      		<input type="hidden" name="imageFileName" value="${dto.imageFileName}">
			      		<input type="hidden" name="page" value="${page}">
			      	</c:if>
			        <button type="button" class="btn btnCreate" onclick="sendOk();">${mode=='update'?'수정완료':'등록하기'}</button>
			        <button type="reset" class="btn">다시입력</button>
			        <button type="button" class="btn" onclick="javascript:location.href='${pageContext.request.contextPath}/store/list.do';">${mode=='update'?'수정취소':'등록취소'}</button>

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
		<script src="http://dmaps.daum.net/map_js_init/postcode.v2.js"></script>
<script>
    function daumPostcode() {
        new daum.Postcode({
            oncomplete: function(data) {
                var fullAddr = '';
                var extraAddr = '';

                if (data.userSelectedType === 'R') {
                    fullAddr = data.roadAddress;

                } else {
                    fullAddr = data.jibunAddress;
                }


                if(data.userSelectedType === 'R'){
                    if(data.bname !== ''){
                        extraAddr += data.bname;
                    }
                    if(data.buildingName !== ''){
                        extraAddr += (extraAddr !== '' ? ', ' + data.buildingName : data.buildingName);
                    }
                    fullAddr += (extraAddr !== '' ? ' ('+ extraAddr +')' : '');
                }

               document.getElementById('address').value = fullAddr;

            }
        }).open();
    }
</script>    
	
</body>
</html>