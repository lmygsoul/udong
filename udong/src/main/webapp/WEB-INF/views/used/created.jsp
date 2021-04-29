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

<style type="text/css">
.imgLayout {
	max-width: 600px;
	padding: 5px;
	box-sizing: border-box;
	display: flex; /* 자손요소를 flexbox로 변경 */
	flex-direction: row; /* 정방향 수평나열 */
	flex-wrap: nowrap;
	overflow-x: auto;
}
.imgLayout img {
	width: 35px; height: 35px;
	margin-right: 5px;
	flex: 0 0 auto;
	cursor: pointer;
}
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
    function sendUsed() {
        var f = document.photoForm;

    	var str = f.subject.value;
        if(!str) {
            alert("제목을 입력하세요. ");
            f.subject.focus();
            return;
        }
		
        str = f.category.value;
		if(!str) {
    		alert("카테고리를 입력하세요. ");
    		f.category.focus();
    		return;
		}
        
        str = f.area.value;
		if(!str) {
    		alert("지역을 입력하세요. ");
    		f.area.focus();
    		return;
		}
    	
		str = f.price.value;
		if(!str) {
    		alert("가격을 입력하세요. ");
    		f.price.focus();
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
    	  
      	f.action="${pageContext.request.contextPath}/used/${mode}_ok.do";
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


//돈에 세자리마다 콤마 넣기    
    
    function inputNumberFormat(obj) {
        obj.value = comma(uncomma(obj.value));
    }

    function comma(str) {
        str = String(str);
        return str.replace(/(\d)(?=(?:\d{3})+(?!\d))/g, '$1,');
    }

    function uncomma(str) {
        str = String(str);
        return str.replace(/[^\d]+/g, '');
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
            <h3><span style="font-family: Webdings">4</span> 중고거래 </h3>
        </div>
        
        <div>
			<form name="photoForm" method="post" class="formBox" enctype="multipart/form-data">
			  <table style="width: 100%; margin: 0 auto; border-spacing: 0px; border-collapse: collapse;">
			  
			  <tr align="left" height="43" style="border-bottom: 1px solid #ccc; border-top: 1px solid #ccc;"> 
			      <td width="100" style="text-align: center;">제&nbsp;&nbsp;&nbsp;&nbsp;목</td>
			      <td style="padding-left:10px;"> 
			        <input type="text" name="subject" maxlength="100" class="boxTF" style="width: 97%;" value="${dto.subject}">
			      </td>
			  </tr>
			
			  <tr align="left" height="43" style="border-bottom: 1px solid #ccc;"> 
			      <td width="100" style="text-align: center;">작성자</td>
			      <td style="padding-left:10px;"> 
			          ${sessionScope.member.userId} 
			      </td>
			  </tr>
			  		  
			  <tr align="left" height="43" style="border-bottom: 1px solid #ccc; border-top: 1px solid #ccc;">
				<td width="100" style="text-align: center;">카테고리</td>
				<td style="padding-left:10px;"> 
						  <select name="category" class="selectField">
								<option value="">선 택</option>
								<option value="전자/가전"<c:if test="${category == '전자/가전'}">selected</c:if>>전자/가전</option>
								<option value="가구/인테리어"<c:if test="${category == '가구/인테리어'}">selected</c:if>>가구/인테리어</option>
								<option value="스포츠/레저"<c:if test="${category == '스포츠/레저'}">selected</c:if>>스포츠/레저</option>
								<option value="생활/건강"<c:if test="${category == '생활/건강'}">selected</c:if>>생활/건강</option>
								<option value="의류/잡화"<c:if test="${category == '의류/잡화'}">selected</c:if>>의류/잡화</option>
								<option value="식품"<c:if test="${category == '식품'}">selected</c:if>>식품</option>
								<option value="기타"<c:if test="${category == '기타'}">selected</c:if>>기타</option>						
						  </select>
				</td>
			</tr>
			  
			  <tr align="left" height="43" style="border-bottom: 1px solid #ccc; border-top: 1px solid #ccc;"> 
			      <td width="100" style="text-align: center;">지&nbsp;&nbsp;&nbsp;&nbsp;역</td>
			      <td style="padding-left:10px;"> 
			        <input type="text" name="area" maxlength="100" class="boxTF" style="width: 20%;" placeholder="동까지 입력" value="${dto.area}">
			      </td>
			  </tr>
			  
			  <tr align="left" height="43" style="border-bottom: 1px solid #ccc; border-top: 1px solid #ccc;"> 
			      <td width="100" style="text-align: center;">가&nbsp;&nbsp;&nbsp;&nbsp;격</td>
			      <td style="padding-left:10px;"> 
			        <input type="text" id="price" name="price" maxlength="30" class="boxTF" style="width: 20%;" onkeyup="inputNumberFormat(this)" value="${dto.price}"><span> 원</span>
			      </td> 
			  </tr>	
			  
			
			  <tr align="left" style="border-bottom: 1px solid #ccc;"> 
			      <td width="100" style="text-align: center; padding-top:10px;" valign="top">내&nbsp;&nbsp;&nbsp;&nbsp;용</td>
			      <td valign="top" style="padding:10px 0px 5px 10px;"> 
			        <textarea name="content" rows="12" class="boxTA" style="width: 97%;">${dto.content}</textarea>
			      </td>
			  </tr>
				
			 <tr align="left" height="40" style="border-bottom: 1px solid #cccccc;">
			      <td width="100" style="text-align: center;">이미지</td>
			      <td style="padding-left:10px;"> 
			           <input type="file" name="selectFile" accept="image/*" class="boxTF" size="53" style="height: 25px;">
			       </td>
			  </tr>
			  </table>
					
			  <table style="width: 100%; border-spacing: 0px; margin-top: 5px;">
			     <tr height="45"> 
			      <td align="center" >
			      <!-- 수정 모드일 때 -> 글번호, 페이지 번호를 넘긴다 (*page는 dto 안에 없음) -->
			      	<c:if test="${mode=='update'}">
			      		<input type="hidden" name="num" value="${dto.num}">
			      		<input type="hidden" name="page" value="${page}">
			      	</c:if>
			        <button type="button" class="btn btnCreate" onclick="sendUsed();">${mode=='update'?'수정완료':'등록하기'}</button>
			        <button type="reset" class="btn">다시입력</button>
			        <button type="button" class="btn" onclick="javascript:location.href='${pageContext.request.contextPath}/used/list.do';">${mode=='update'?'수정취소':'등록취소'}</button>
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