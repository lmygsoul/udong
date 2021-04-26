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
        var f = document.noticeForm;

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

   		f.action="${pageContext.request.contextPath}/notice/${mode}_ok.do";

        f.submit();
    }

    <c:if test="${mode=='update'}">
    function deleteFile(fileNum) {
    	if(! confirm("파일을 삭제 하시겠습니까 ?")) {
    		return;
    	}
    	
		var url="${pageContext.request.contextPath}/notice/deleteFile.do?num=${dto.num}&fileNum="+fileNum+"&page=${page}&rows=${rows}";
		location.href=url;
    }
</c:if>
    
</script>
</head>
<body>

<div class="header">
    <jsp:include page="/WEB-INF/views/layout/header.jsp"></jsp:include>
</div>
	
<div class="container">
    <div class="body-container">
        <div class="body-title" style="margin-bottom: 0; border-bottom: 0;">
            <h3><span style="font-family: Webdings">4</span> 공지사항 </h3>
        </div>
        
        <div>
			<form name="noticeForm" method="post" class="formBox" enctype="multipart/form-data">
			  <table style="width: 100%; margin: 0 auto; border-spacing: 0px; border-collapse: collapse;">
			  <tr align="left" height="43" style="border-bottom: 1px solid #ccc; border-top: 1px solid #ccc;"> 
			      <td width="100" style="text-align: center;">제&nbsp;&nbsp;&nbsp;&nbsp;목</td>
			      <td style="padding-left:10px;"> 
			        <input type="text" name="subject" maxlength="100" class="boxTF" style="width: 97%;" value="${dto.subject}">
			      </td>
			  </tr>
			  
			  <tr align="left" height="40" style="border-bottom: 1px solid #cccccc;"> 
			      <td width="100" bgcolor="#eeeeee" style="text-align: center;">공지여부</td>
			      <td style="padding-left:10px;"> 
			        <input type="checkbox" name="notice" value="1" ${dto.notice==1 ? "checked='checked' ":"" } > 공지
			      </td>
			  </tr>
			
			  <tr align="left" height="43" style="border-bottom: 1px solid #ccc;"> 
			      <td width="100" style="text-align: center;">작성자</td>
			      <td style="padding-left:10px;"> 
			          ${sessionScope.member.userName}
			      </td>
			  </tr>
			
			  <tr align="left" style="border-bottom: 1px solid #ccc;"> 
			      <td width="100" style="text-align: center; padding-top:10px;" valign="top">내&nbsp;&nbsp;&nbsp;&nbsp;용</td>
			      <td valign="top" style="padding:10px 0px 5px 10px;"> 
			        <textarea name="content" rows="12" class="boxTA" style="width: 97%;">${dto.content}</textarea>
			      </td>
			  </tr>
			  
			  <tr align="left" height="40" style="border-bottom: 1px solid #cccccc;">
			      <td width="100" bgcolor="#eeeeee" style="text-align: center;">첨&nbsp;&nbsp;&nbsp;&nbsp;부</td>
			      <td style="padding-left:10px;"> 
			           <input type="file" name="selectFile" class="boxTF" size="53" style="height: 25px;" multiple="multiple">
			       </td>
			  </tr> 
			  
			  <c:if test="${mode=='update'}">
			  	<c:forEach var="vo" items="${listFile}">
				  <tr align="left" height="40" style="border-bottom: 1px solid #cccccc;">
				      <td width="100" bgcolor="#eeeeee" style="text-align: center;">첨부된파일</td>
				      <td style="padding-left:10px;"> 
				            <a href="javascript:deleteFile('${vo.fileNum}');"><i class="far fa-trash-alt"></i></a>
				           ${vo.originalFilename}
				       </td>
				   </tr>
				 </c:forEach> 
			  </c:if>
			  </table>
			
			  <table style="width: 100%; border-spacing: 0px; margin-top: 5px;">
			     <tr height="45"> 
			      <td align="center" >
			        <button type="button" class="btn btnCreate" onclick="sendNotice();">${mode=='update'?'수정완료':'등록하기'}</button>
			        <button type="reset" class="btn">다시입력</button>
			        <button type="button" class="btn" onclick="javascript:location.href='${pageContext.request.contextPath}/notice/list.do?rows=${rows}';">${mode=='update'?'수정취소':'등록취소'}</button>
			      <!-- 수정 모드일 때 -> 글번호, 페이지 번호를 넘긴다 (*page는 dto 안에 없음) -->
			      	<c:if test="${mode=='update'}">
			      		<input type="hidden" name="num" value="${dto.num}">
			      		<input type="hidden" name="page" value="${page}">
			      	</c:if>
			      	 <input type="hidden" name="rows" value="${rows}">
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