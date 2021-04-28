<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<script type="text/javascript">
//엔터 처리
$(function(){
	   $("input").not($(":button")).keypress(function (evt) {
	        if (evt.keyCode == 13) {
	            var fields = $(this).parents('form,body').find('button,input,textarea,select');
	            var index = fields.index(this);
	            if ( index > -1 && ( index + 1 ) < fields.length ) {
	                fields.eq( index + 1 ).focus();
	            }
	            return false;
	        }
	     });
});
</script>

<div class="header-top">
    <div class="header-left">
        <p style="margin: 2px;">
            <a href="${pageContext.request.contextPath}/" style="text-decoration: none;">
            	<img src="${pageContext.request.contextPath}/resource/images/logo2.png" style="width: 35%; position: relative; top:18px; ">
            </a>
        </p>
    </div>
    <div class="header-right">
        <div style="padding-top: 30px;  float: right;">
            <c:if test="${empty sessionScope.member}">
                <a href="${pageContext.request.contextPath}/member/login.do">로그인</a>
                    &nbsp;|&nbsp;
                <a href="${pageContext.request.contextPath}/member/member.do">회원가입</a>
            </c:if>
            <c:if test="${not empty sessionScope.member}">
                <span style="color:red;">${sessionScope.member.userName}</span>님
                    &nbsp;|&nbsp;
                    <a href="${pageContext.request.contextPath}/member/logout.do">로그아웃</a>
                    &nbsp;|&nbsp;
                    <a href="${pageContext.request.contextPath}/member/myProfile.do?mode=myProfile">정보수정</a>
            </c:if>
        </div>
    </div>
</div>

<div class="menu">
    <ul class="nav">
        <li>
            <a href="${pageContext.request.contextPath}/used/list.do"><span>중고거래</span></a>
        </li>
			
        <li>
            <a href="#"><span>동네홍보</span></a>
            <ul>
                <li><a href="${pageContext.request.contextPath}/neighbor/list.do" style="margin-left:124px; " onmouseover="this.style.marginLeft='124px';">우동자랑</a></li>
                <li><a href="${pageContext.request.contextPath}/store/list.do">우동홍보</a></li>
                <li><a href="${pageContext.request.contextPath}/dayclass/list.do">우동클래스</a></li>
            </ul>
        </li>

        <li>
            <a href="#"><span>커뮤니티</span></a>
            <ul>
                <li><a href="#" style="margin-left:235px; " onmouseover="this.style.marginLeft='235px';">출석체크</a></li>
                <li><a href="${pageContext.request.contextPath}/greeting/list.do">가입인사</a></li>
                <li><a href="${pageContext.request.contextPath}/udong/list.do">우동이야기</a></li>
                <li><a href="${pageContext.request.contextPath}/udongphoto/list.do">우동사진</a></li>
                <li><a href="${pageContext.request.contextPath}/qa/list.do">우동지식</a></li>
            </ul>
        </li>
        
        <li>
            <a href="#"><span>고객센터</span></a>
            <ul>
                <li>
                <a href="${pageContext.request.contextPath}/notice/list.do" style="margin-left:347px; " onmouseover="this.style.marginLeft='347px';">공지사항</a></li>
	            <li>
	            <a href="${pageContext.request.contextPath}/faq/list.do"><span>FAQ</span></a>
				</li>
            </ul>
        </li>
        
        <li>
        </li>

        <!--
        <c:if test="${not empty sessionScope.member}">
        </c:if>
        -->
        <li>
            <a href="#"><span>마이페이지</span></a>
            <ul>
                <li><a href="${pageContext.request.contextPath}/member/sm_list.do" style="margin-left:456px; " onmouseover="this.style.marginLeft='456px';">보낸 쪽지함</a></li>
                <li><a href="${pageContext.request.contextPath}/member/rm_list.do">받은 쪽지함</a></li>
                <li><a href="#">내가쓴글</a></li>
                <li><a href="#">찜한목록</a></li>
            </ul>
        </li>
    </ul>      
</div>
