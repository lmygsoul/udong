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
/* 모달대화상자 */
/* 타이틀바 */
.ui-widget-header {
	background: none;
	border: none;
	height:35px;
	line-height:35px;
	border-bottom: 1px solid #cccccc;
	border-radius: 0;
}
/* 내용 */
.ui-widget-content {
   /* border: none; */
   border-color: #cccccc; 
}
</style>

<script type="text/javascript">
    function sendPhoto() {
        var f = document.photoForm;

    	var str = f.subject.value;
        if(!str) {
            alert("제목을 입력하세요. ");
            f.subject.focus();
            return;
        }

    	str = f.content.value;
        if(!str) {
            alert("설명을 입력하세요. ");
            f.content.focus();
            return;
        }

        var mode = "${mode}";
        if( mode=="created" || (mode=="update" && f.selectFile.value != "") ) {
        	if(! /(\.gif|\.jpg|\.png|\.jpeg)$/i.test(f.selectFile.value)) {
        		alert("이미지 파일만 가능합니다.");
        		f.selectFile.focus();
        		return;
        	}
        }
    	  
      	f.action="${pageContext.request.contextPath}/udongphoto/${mode}_ok.do";
        f.submit();
    }
    
    $(function(){
    	$("body").on("change", "form input[type=file]", function(e){
    		var file = e.target.files[0];
    		if( ! file.type.match("image.*")) {
    			return false; // 이미지 파일이 아니면
    		}
    		
    		var reader = new FileReader();
    		reader.onload = function(evt) {
    			$(".selectImage").attr("src", evt.target.result);
    			$("#dialogPhoto").dialog({
    	    		title:"이미지 미리보기",
    	    		width: 350,
    	    		height: 280,
    	    		modal: true
    	    	});
    		}
    		reader.readAsDataURL(file);
    	});
    });
</script>
</head>
<body>

<div class="header">
    <jsp:include page="/WEB-INF/views/layout/header.jsp"></jsp:include>
</div>
	
<div class="container">
    <div class="body-container">
        <div class="body-title" style="margin-bottom: 0; border-bottom: 0;">
            <h3><i class="far fa-image"></i>&nbsp;&nbsp;우동사진</h3>
        </div>
        
        <div>
 			<form name="photoForm" method="post" enctype="multipart/form-data">
			  <table class="create-table">
			  <tr class="create-row"> 
			      <td class="create-col1">제&nbsp;&nbsp;&nbsp;&nbsp;목</td>
			      <td style="padding-left:10px;"> 
			        <input type="text" name="subject" maxlength="100" class="boxTF" style="width: 95%;" value="${dto.subject}">
			      </td>
			  </tr>
			
			  <tr align="left" height="40" style="border-bottom: 1px solid #cccccc;"> 
			      <td class="create-col1">작성자</td>
			      <td style="padding-left:10px;"> 
			            ${sessionScope.member.userName}
			      </td>
			  </tr>
			
			  <tr align="left" style="border-bottom: 1px solid #cccccc;"> 
			      <td class="create-col2">설&nbsp;&nbsp;&nbsp;&nbsp;명</td>
			      <td valign="top" style="padding:10px 0px 5px 10px;"> 
			        <textarea name="content" rows="12" class="boxTA" style="width: 97%;">${dto.content}</textarea>
			      </td>
			  </tr>
			  
			  <tr align="left" height="40" style="border-bottom: 1px solid #cccccc;">
			      <td class="create-col1">이미지</td>
			      <td style="padding-left:10px;"> 
			           <input type="file" name="selectFile" accept="image/*"
			                      class="boxTF" size="53" style="height: 25px; border:0;">
			       </td>
			  </tr>

			  <c:if test="${mode=='update'}">
				  <tr align="left" height="40" style="border-bottom: 1px solid #cccccc;">
				      <td class="create-col1">등록이미지</td>
				      <td style="padding-left:10px;"> 
				         <img src="${pageContext.request.contextPath}/uploads/photo/${dto.imageFilename}"
				                     width="30" height="30" border="0" style="vertical-align: middle;" >
				         <span style="vertical-align: middle; font-size: 11px; color: #333;">(새로운 이미지가 등록되면 기존 이미지는 삭제 됩니다.)</span>
				       </td>
				  </tr> 
			  </c:if>			  
			  </table>
			
			  <table class="create-table2">
			     <tr height="45"> 
			      <td align="center" >
			        <button type="button" class="btn btnCreate" onclick="sendPhoto();">${mode=='update'?'수정완료':'등록하기'}</button>
			        <button type="reset" class="btn">다시입력</button>
			        <button type="button" class="btn" onclick="javascript:location.href='${pageContext.request.contextPath}/udongphoto/list.do';">${mode=='update'?'수정취소':'등록취소'}</button>
			         <c:if test="${mode=='update'}">
                             <input type="hidden" name="num" value="${dto.num}">
                             <input type="hidden" name="imageFilename" value="${dto.imageFilename}">
                             <input type="hidden" name="page" value="${page}">
			        </c:if>
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

<div id="dialogPhoto" style="display: none;">
      <div id="imagePhotoLayout">
      		<img class="selectImage" width="320" height="200">
      </div>
</div>

<jsp:include page="/WEB-INF/views/layout/staticFooter.jsp"/>
</body>
</html>