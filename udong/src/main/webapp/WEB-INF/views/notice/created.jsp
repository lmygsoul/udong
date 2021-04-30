<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>글쓰기</title>
<jsp:include page="/WEB-INF/views/layout/staticHeader.jsp"/>

<script type="text/javascript">
    function sendNotice() {
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
    function deleteFile(num) {
  	  var url="${pageContext.request.contextPath}/notice/deleteFile.do?num="+num+"&page=${page}";
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
            <h3>공지사항</h3>
        </div>
        
        <div>
			<form name="noticeForm" method="post" enctype="multipart/form-data" class="formBox">
			  <table class="create-table">
			  <tr class="create-row"> 
			      <td class="create-col1">제&nbsp;&nbsp;&nbsp;&nbsp;목</td>
			      <td style="padding-left:10px;"> 
			        <input type="text" name="subject" maxlength="100" class="boxTF" style="width: 97%;" value="${dto.subject}">
			      </td>
			  </tr>
			  
			  <tr align="left" height="40" style="border-bottom: 1px solid #cccccc;"> 
			      <td class="create-col1">공지여부</td>
			      <td style="padding-left:10px;"> 
			        <input type="checkbox" name="notice" value="1" ${dto.notice==1 ? "checked='checked' ":"" } > 공지
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
			        <textarea name="content" rows="12" class="boxTA" style="width: 97%;">${dto.content}</textarea>
			      </td>
			  </tr>

			  <tr align="left" height="40" style="border-bottom: 1px solid #cccccc;">
			      <td class="create-col1">첨&nbsp;&nbsp;&nbsp;&nbsp;부</td>
			      <td style="padding-left:10px;"> 
			           <input type="file" name="selectFile" class="boxTF" size="53" style="height: 25px; border:0;">
			       </td>
			  </tr> 

			  <c:if test="${mode=='update'}">
				  <tr align="left" height="40" style="border-bottom: 1px solid #cccccc;">
				      <td class="create-col1">첨부된파일</td>
				      <td style="padding-left:10px;"> 
				         <c:if test="${not empty dto.saveFilename}">
				             <a href="javascript:deleteFile('${dto.num}');"><i class="far fa-trash-alt"></i></a>
				             ${dto.originalFilename}
				         </c:if>     
				       </td>
				  </tr> 
			  </c:if>
			  
			  </table>
			
			  <table class="create-table2">
			     <tr height="45"> 
			      <td align="center" >
			        <button type="button" class="btn btnCreate" onclick="sendNotice();">${mode=='update'?'수정완료':'등록하기'}</button>
			        <button type="reset" class="btn">다시입력</button>
			        <button type="button" class="btn" onclick="javascript:location.href='${pageContext.request.contextPath}/notice/list.do';">${mode=='update'?'수정취소':'등록취소'}</button>
			         <c:if test="${mode=='update'}">
			         	 <input type="hidden" name="num" value="${dto.num}">
			        	 <input type="hidden" name="page" value="${page}">
			        	 <input type="hidden" name="fileSize" value="${dto.fileSize}">
			        	 <input type="hidden" name="saveFilename" value="${dto.saveFilename}">
			        	 <input type="hidden" name="originalFilename" value="${dto.originalFilename}">
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

<jsp:include page="/WEB-INF/views/layout/staticFooter.jsp"/>
</body>
</html>