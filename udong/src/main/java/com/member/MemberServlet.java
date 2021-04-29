package com.member;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.class_bbs.ClassDAO;
import com.class_bbs.ClassDTO;
import com.greeting.GreetingDAO;
import com.greeting.GreetingDTO;
import com.neighbor.NeighborDAO;
import com.neighbor.NeighborDTO;
import com.qa_bbs.QaDAO;
import com.qa_bbs.QaDTO;
import com.store.StoreDAO;
import com.store.StoreDTO;
import com.udong.UdongDAO;
import com.udong.UdongDTO;
import com.used.UsedDAO;
import com.used.UsedDTO;
import com.util.MyServlet;
import com.util.MyUtil;

@WebServlet("/member/*")
public class MemberServlet extends MyServlet{
	private static final long serialVersionUID = 1L;
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		process(req, resp);
	}
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		process(req, resp);
	}
	@Override
	protected void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("utf-8");
		
		
		String uri = req.getRequestURI();
		
		if(uri.indexOf("login.do")!=-1) {
			loginForm(req,resp);
		} else if(uri.indexOf("login_ok.do")!=-1) {
			loginSubmit(req,resp);
		} else if(uri.indexOf("logout.do")!=-1) {
			logout(req,resp);
		} else if(uri.indexOf("member.do")!=-1) {
			memberForm(req,resp);
		} else if(uri.indexOf("member_ok.do")!=-1) {
			memberSubmit(req,resp);
		} else if(uri.indexOf("myProfile.do")!=-1) {
			myProfile(req,resp);
		} else if(uri.indexOf("another_Profile.do")!=-1) {
			another_Profile(req,resp);
		} else if(uri.indexOf("update.do")!=-1) {
			update(req,resp);
		} else if(uri.indexOf("update_ok.do")!=-1) {
			updateForm(req,resp);
		} else if(uri.indexOf("sm_created.do")!=-1) {
			sendMessage(req,resp);
		} else if(uri.indexOf("sm_list.do")!=-1) {
			sendMessageList(req,resp);
		} else if(uri.indexOf("sm_created_ok.do")!=-1) {
			sendMessage_ok(req,resp);
		} else if(uri.indexOf("sm_article.do")!=-1) {
			sendMessage_article(req,resp);
		} else if(uri.indexOf("sm_delete.do")!=-1) {
			sm_Message_delete(req,resp);
		} else if(uri.indexOf("rm_list.do")!=-1) {
			receiveMessageList(req,resp);
		} else if(uri.indexOf("rm_created.do")!=-1) {
			recieveMessage(req, resp);
		} else if(uri.indexOf("rm_article.do")!=-1) {
			receiveMessage_article(req,resp);
		} else if(uri.indexOf("rm_delete.do")!=-1) {
			rm_Message_delete(req,resp);
		} else if(uri.indexOf("cb_list.do")!=-1) {
			cb_list(req,resp);
		}  else if (uri.indexOf("cb_article.do") != -1) {
			cb_article(req, resp);
		} else if(uri.indexOf("gt_list.do")!=-1) {
			gt_list(req,resp);
		}  else if (uri.indexOf("gt_article.do") != -1) {
			gt_article(req, resp);
		} else if (uri.indexOf("nb_list.do") != -1) {
			nb_list(req, resp);
		} else if (uri.indexOf("qa_list.do") != -1) {
			qa_list(req, resp);
		}  else if (uri.indexOf("qa_article.do") != -1) {
			qa_article(req, resp);
		} else if (uri.indexOf("st_list.do") != -1) {
			st_list(req, resp);
		}  else if (uri.indexOf("st_article.do") != -1) {
			st_article(req, resp);
		} else if (uri.indexOf("ud_list.do") != -1) {
			ud_list(req, resp);
		}  else if (uri.indexOf("ud_article.do") != -1) {
			ud_article(req, resp);
		} else if (uri.indexOf("used_list.do") != -1) {
			used_list(req, resp);
		}  else if (uri.indexOf("used_article.do") != -1) {
			used_article(req, resp);
		} 
		
	}
	private void loginForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("utf-8");
		
		String path="/WEB-INF/views/member/login.jsp";
		forward(req, resp, path);
	}
	private void loginSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		HttpSession session = req.getSession();
		MemberDAO dao = new MemberDAO();
		String cp = req.getContextPath();
		String userId = req.getParameter("userId");
		String userPwd = req.getParameter("userPwd");
		MemberDTO dto = dao.readMember(userId);
		if(dto!=null) {
			if(userPwd.equals(dto.getUserPwd())&&(!dto.getType().equals("3"))) {
			//if(userPwd.equals(dto.getUserPwd())&&dto.getType()!=3)
				//type 0: 관리자, 1:일반유저, 2:상공인, 3:탈퇴유저
				session.setMaxInactiveInterval(0);
				
				//세션에 저장할 내용
				SessionInfo info = new SessionInfo();
				info.setUserId(dto.getUserId());
				info.setUserName(dto.getUserName());
				info.setType(dto.getType());
				
				// 세션에 member로 저장
				session.setAttribute("member", info);
				
				//메인화면으로 리다이렉트
				resp.sendRedirect(cp);
				return;
				
			}
		}
		//로그인 실패
		String msg = "아이디 또는 패스워드가 일치하지 않습니다.";
		req.setAttribute("message", msg);
		
		forward(req, resp, "/WEB-INF/views/member/login.jsp");
	}
	private void logout(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession();
		String cp = req.getContextPath();
		
		//세션에 저장된 정보 지우기
		session.removeAttribute("member");
		
		//세션 초기화
		session.invalidate();
		
		//페이지로 리다이렉트
		resp.sendRedirect(cp);
	}
	private void memberForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 회원가입폼
		req.setAttribute("title", "회원 가입");
		req.setAttribute("mode", "member");
		
		forward(req, resp, "/WEB-INF/views/member/member.jsp");
	}
	private void memberSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		MemberDAO dao = new MemberDAO();
		MemberDTO dto = new MemberDTO();
		String cp = req.getContextPath();
		String message="";
		try {
			dto.setUserId(req.getParameter("userId"));
			dto.setUserPwd(req.getParameter("userPwd"));
			dto.setUserName(req.getParameter("userName"));
			dto.setNickName(req.getParameter("nickName"));
			dto.setType(req.getParameter("type"));
			
			String birth = req.getParameter("birth").replaceAll("(\\.|\\-|\\/)", "");
			dto.setBirth(birth);
			String email1 = req.getParameter("email1");
			String email2 = req.getParameter("email2");
			dto.setEmail(email1+"@"+email2);
			String tel1 = req.getParameter("selectTel");
			String tel2 = req.getParameter("tel2");
			String tel3 = req.getParameter("tel3");
			dto.setTel(tel1+"-"+tel2+"-"+tel3);
			dto.setZipCode(req.getParameter("zipCode"));
			dto.setAddr1(req.getParameter("addr1"));
			dto.setAddr2(req.getParameter("addr2"));
			dto.setMyComment(req.getParameter("myComment"));
			
			dao.insertMember(dto);
			resp.sendRedirect(cp);
			return;
		} catch (SQLException e) {
			if(e.getErrorCode()==1)
				message = "아이디 중복으로 회원 가입이 실패 했습니다.";
			else if(e.getErrorCode()==1400)
				message = "필수 사항을 입력하지 않았습니다.";
			else if(e.getErrorCode()==1861)
				message = "날짜 형식이 일치하지 않습니다.";
			else
				message = "회원 가입이 실패 했습니다.";
		} catch (Exception e) {
			e.printStackTrace();
		}
		req.setAttribute("title", "회원 가입");
		req.setAttribute("mode", "member");
		req.setAttribute("message", message);
		forward(req, resp, "/WEB-INF/views/member/member.jsp");
	}
	private void myProfile(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		MemberDAO dao = new MemberDAO();
		HttpSession session=req.getSession();
		try {
			SessionInfo info=(SessionInfo)session.getAttribute("member");
			MemberDTO dto = dao.readMember(info.getUserId());
			String[] telNum = dto.getTel().split("-");
			String tel1 = telNum[0];
			String tel2 = telNum[1];
			String tel3 = telNum[2];
			
			dto.setTel1(tel1);
			dto.setTel2(tel2);
			dto.setTel3(tel3);
			req.setAttribute("title", "마이프로필");
			req.setAttribute("mode", "myProfile");
			req.setAttribute("dto", dto);
			
			forward(req, resp, "/WEB-INF/views/member/myProfile.jsp");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void another_Profile(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		MemberDAO dao = new MemberDAO();
		try {
			
			String id = req.getParameter("userId");
			MemberDTO dto = dao.readMember(id);
			if(dto==null) {
				return;
			}
			String[] telNum = dto.getTel().split("-");
			String tel1 = telNum[0];
			String tel2 = telNum[1];
			String tel3 = telNum[2];
			
			dto.setTel1(tel1);
			dto.setTel2(tel2);
			dto.setTel3(tel3);
			req.setAttribute("title", "프로필");
			req.setAttribute("mode", "another_Profile");
			req.setAttribute("dto", dto);
			
			forward(req, resp, "/WEB-INF/views/member/myProfile.jsp");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void update(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		MemberDAO dao = new MemberDAO();
		HttpSession session=req.getSession();
		try {
			SessionInfo info=(SessionInfo)session.getAttribute("member");
			MemberDTO dto = dao.readMember(info.getUserId());
			
			req.setAttribute("title", "회원 수정");
			req.setAttribute("mode", "update");
			req.setAttribute("dto", dto);
			
			forward(req, resp, "/WEB-INF/views/member/member.jsp");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	private void updateForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		String cp=req.getContextPath();
		MemberDAO dao=new MemberDAO();
		MemberDTO dto = new MemberDTO();
		try {
			
			dto.setUserId(req.getParameter("userId"));
			dto.setUserPwd(req.getParameter("userPwd"));
			dto.setUserName(req.getParameter("userName"));
			dto.setNickName(req.getParameter("nickName"));
			dto.setType(req.getParameter("type"));
			
			String birth = req.getParameter("birth").replaceAll("(\\.|\\-|\\/)", "");
			dto.setBirth(birth);
			String email1 = req.getParameter("email1");
			String email2 = req.getParameter("email2");
			dto.setEmail(email1+"@"+email2);
			String tel1 = req.getParameter("selectTel");
			String tel2 = req.getParameter("tel2");
			String tel3 = req.getParameter("tel3");
			dto.setTel(tel1+"-"+tel2+"-"+tel3);
			dto.setZipCode(req.getParameter("zipCode"));
			dto.setAddr1(req.getParameter("addr1"));
			dto.setAddr2(req.getParameter("addr2"));
			dto.setMyComment(req.getParameter("myComment"));
			
			dao.updateMember(dto);
		} catch (Exception e) {
			e.printStackTrace();
		}
		resp.sendRedirect(cp);
	}
	private void sendMessageList(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		SendMessageDAO dao = new SendMessageDAO();
		MyUtil util= new MyUtil();
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		String page = req.getParameter("page");
		String cp = req.getContextPath();
		int current_page = 1;
		
		if (info==null) {
			resp.sendRedirect(cp+"/member/login.do");
			return;
		}
		if(page!=null) {
			current_page = Integer.parseInt(page);
		}
		String condition = req.getParameter("condition");
		String keyword = req.getParameter("keyword");
		if(condition==null) {
			condition = "all";
			keyword="";
		}
		if(req.getMethod().equalsIgnoreCase("GET")) {
			keyword=URLDecoder.decode(keyword,"UTF-8");
		}
		int dataCount;
		if(keyword.length()==0) {
			dataCount = dao.dataCount_sm(info.getUserId());
		} else {
			dataCount = dao.dataCount_sm(info.getUserId(),condition, keyword);
		}
		int rows = 10;
		int total_page = util.pageCount(rows, dataCount);
		
		if(current_page > total_page) {
			current_page = total_page ;
		}
		int offset = (current_page -1 ) * rows;
		if(offset <0) offset = 0;
		List<MessageDTO> list = null;
		if(keyword.length()==0) {
			list=dao.listsm(offset, rows,info.getUserId());
		} else {
			list=dao.listsm(offset, rows, condition, keyword,info.getUserId());
		}
		int listNum=1, n=0;
		for(MessageDTO mdto : list) {
				listNum = dataCount - (offset+n);
				mdto.setListNum(listNum);
				n++;
		}
		String query="";
		if(keyword.length()!=0) {
			query="condition="+condition+"&keyword="+URLEncoder.encode(keyword, "UTF-8");
		}
		String listUrl = cp+"/member/sm_list.do";
		String articleUrl = cp+"/member/sm_article.do?page="+current_page;
		if(query.length()!=0) {
			listUrl += "?"+query;
			articleUrl = "&"+articleUrl;
		}
		String paging = util.paging(current_page, total_page,listUrl);
		
		req.setAttribute("list", list);
		req.setAttribute("paging", paging);
		req.setAttribute("page", current_page);
		req.setAttribute("dataCount", dataCount);
		req.setAttribute("total_page", total_page);
		req.setAttribute("articleUrl", articleUrl);
		req.setAttribute("condition", condition);
		req.setAttribute("keyword", keyword);
		req.setAttribute("mode", "sendMessage");
		req.setAttribute("title", "보낸쪽지함");
		
		forward(req, resp, "/WEB-INF/views/member/sm_list.jsp");
		return;
	}
	private void sendMessage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		
		
		req.setAttribute("title", "보낸쪽지함");
		req.setAttribute("mode", "sm_created");
		
		forward(req, resp, "/WEB-INF/views/member/sm_created.jsp");
		return;
	}

	private void sendMessage_ok(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		SendMessageDAO mdao = new SendMessageDAO();
		MessageDTO mdto = new MessageDTO();
		HttpSession session=req.getSession();
		String cp = req.getContextPath();
		try {
			SessionInfo info=(SessionInfo)session.getAttribute("member");
			mdto.setSendUser(info.getUserId());
			mdto.setReceiveUser(req.getParameter("receiveUserId"));
			mdto.setSubject(req.getParameter("subject"));
			mdto.setContent(req.getParameter("content"));
			
			mdao.insertMessage(mdto);
			
			req.setAttribute("title", "보낸쪽지함");
			req.setAttribute("mdto", mdto);
			
			resp.sendRedirect(cp+"/member/sm_list.do");
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	private void sendMessage_article(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		SendMessageDAO mdao= new SendMessageDAO();
		String cp = req.getContextPath();
		HttpSession session=req.getSession();
		String page = req.getParameter("page");
		String query = "page="+page;
		try {
			int num = Integer.parseInt(req.getParameter("num"));
			SessionInfo info=(SessionInfo)session.getAttribute("member");
			String condition = req.getParameter("condition");
			String keyword = req.getParameter("keyword");
			
			if(condition==null) {
				condition = "all";
				keyword="";
			}
			keyword = URLDecoder.decode(keyword,"UTF-8");
			
			if(keyword.length()!=0) {
				query +="&condition="+condition+"&keyword="+URLEncoder.encode(keyword,"UTF-8");
			}
			MessageDTO mdto = mdao.readMember(num);
			if(mdto==null) {
				resp.sendRedirect(cp+"/member/sm_list.do?"+query);
				return;
			}
			mdto.setContent(mdto.getContent().replaceAll("\n", "<br>"));
			MessageDTO preReadSM = mdao.preReadSM(num, condition, keyword,info.getUserId());
			MessageDTO nextReadSM = mdao.nextReadSM(num, condition, keyword,info.getUserId());
			
			req.setAttribute("mdto", mdto);
			req.setAttribute("preReadSM", preReadSM);
			req.setAttribute("nextReadSM", nextReadSM);
			req.setAttribute("page", page);
			req.setAttribute("query", query);
			
			forward(req, resp, "/WEB-INF/views/member/sm_article.jsp");
			return;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	private void sm_Message_delete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		SendMessageDAO mdao = new SendMessageDAO();
		HttpSession session=req.getSession();
		String cp = req.getContextPath();
		String page = req.getParameter("page");
		try {
			int num= Integer.parseInt(req.getParameter("num"));
			SessionInfo info=(SessionInfo)session.getAttribute("member");
			
			
			mdao.deletesendMessage(info.getUserId(),num);
			
			resp.sendRedirect(cp+"/member/sm_list.do?page="+page);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void receiveMessageList(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		SendMessageDAO dao = new SendMessageDAO();
		MyUtil util= new MyUtil();
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		String page = req.getParameter("page");
		String cp = req.getContextPath();
		int current_page = 1;
		
		if (info==null) {
			resp.sendRedirect(cp+"/member/login.do");
			return;
		}
		if(page!=null) {
			current_page = Integer.parseInt(page);
		}
		String condition = req.getParameter("condition");
		String keyword = req.getParameter("keyword");
		if(condition==null) {
			condition = "all";
			keyword="";
		}
		if(req.getMethod().equalsIgnoreCase("GET")) {
			keyword=URLDecoder.decode(keyword,"UTF-8");
		}
		int dataCount;
		if(keyword.length()==0) {
			dataCount = dao.dataCount_rm(info.getUserId());
		} else {
			dataCount = dao.dataCount_rm(info.getUserId(),condition, keyword);
		}
		int rows = 10;
		int total_page = util.pageCount(rows, dataCount);
		
		if(current_page > total_page) {
			current_page = total_page ;
		}
		int offset = (current_page -1 ) * rows;
		if(offset <0) offset = 0;
		List<MessageDTO> list = null;
		if(keyword.length()==0) {
			list=dao.listrm(offset, rows,info.getUserId());
		} else {
			list=dao.listrm(offset, rows, condition, keyword,info.getUserId());
		}
		int listNum, n=0;
		for(MessageDTO mdto : list) {
				listNum = dataCount - (offset+n);
				mdto.setListNum(listNum);
				n++;
		}
		String query="";
		if(keyword.length()!=0) {
			query="condition="+condition+"&keyword="+URLEncoder.encode(keyword, "UTF-8");
		}
		String listUrl = cp+"/member/rm_list.do";
		String articleUrl = cp+"/member/rm_article.do?page="+current_page;
		if(query.length()!=0) {
			listUrl += "?"+query;
			articleUrl = "&"+articleUrl;
		}
		String paging = util.paging(current_page, total_page,listUrl);
		
		req.setAttribute("list", list);
		req.setAttribute("paging", paging);
		req.setAttribute("page", current_page);
		req.setAttribute("dataCount", dataCount);
		req.setAttribute("total_page", total_page);
		req.setAttribute("articleUrl", articleUrl);
		req.setAttribute("condition", condition);
		req.setAttribute("keyword", keyword);
		req.setAttribute("mode", "receiveMessage");
		req.setAttribute("title", "받은쪽지함");
		
		forward(req, resp, "/WEB-INF/views/member/rm_list.jsp");
		return;
		
	}
	
	private void recieveMessage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		req.setAttribute("title", "받은쪽지함");
		req.setAttribute("mode", "rm_created");
		
		forward(req, resp, "/WEB-INF/views/member/rm_created.jsp");
		return;
	}
	private void receiveMessage_article(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		SendMessageDAO mdao= new SendMessageDAO();
		String cp = req.getContextPath();
		HttpSession session=req.getSession();
		String page = req.getParameter("page");
		String query = "page="+page;
		try {
			int num = Integer.parseInt(req.getParameter("num"));
			SessionInfo info=(SessionInfo)session.getAttribute("member");
			String condition = req.getParameter("condition");
			String keyword = req.getParameter("keyword");
			
			if(condition==null) {
				condition = "all";
				keyword="";
			}
			keyword = URLDecoder.decode(keyword,"UTF-8");
			
			if(keyword.length()!=0) {
				query +="&condition="+condition+"&keyword="+URLEncoder.encode(keyword,"UTF-8");
			}
			MessageDTO mdto = mdao.readMember_rm(num);
			if(mdto==null) {
				resp.sendRedirect(cp+"/member/rm_list.do?"+query);
				return;
			}
			mdao.updateMessage(info.getUserId(), num);
			mdto.setContent(mdto.getContent().replaceAll("\n", "<br>"));
			MessageDTO preReadSM = mdao.preReadSM(num, condition, keyword,info.getUserId());
			MessageDTO nextReadSM = mdao.nextReadSM(num, condition, keyword,info.getUserId());
			
			req.setAttribute("mdto", mdto);
			req.setAttribute("preReadSM", preReadSM);
			req.setAttribute("nextReadSM", nextReadSM);
			req.setAttribute("page", page);
			req.setAttribute("query", query);
			
			forward(req, resp, "/WEB-INF/views/member/rm_article.jsp");
			return;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	private void rm_Message_delete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		SendMessageDAO mdao = new SendMessageDAO();
		String cp = req.getContextPath();
		String page = req.getParameter("page");
		HttpSession session=req.getSession();
		try {
			int num= Integer.parseInt(req.getParameter("num"));
			SessionInfo info=(SessionInfo)session.getAttribute("member");
			MessageDTO mdto = mdao.readsendUser(info.getUserId(), num);
			mdao.deletereceiveMessage(mdto.getSendUser(),num);
			
			resp.sendRedirect(cp+"/member/rm_list.do?page="+page);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void cb_list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ClassDAO dao = new ClassDAO();
		HttpSession session=req.getSession();
		SessionInfo info=(SessionInfo)session.getAttribute("member");
		MyUtil util = new MyUtil();
		String cp = req.getContextPath();

		String page = req.getParameter("page");
		int current_page = 1;
		if(page != null) {
			current_page = Integer.parseInt(page);
		}

		String condition = req.getParameter("condition");
		String keyword = req.getParameter("keyword");
		if(condition == null) {
			condition = "all";
			keyword="";
		}

		if(req.getMethod().equalsIgnoreCase("GET")) {
			keyword = URLDecoder.decode(keyword, "utf-8");
		}


		int dataCount;
		if(keyword.length()==0) {
			dataCount = dao.dataCount_cb(info.getUserId());
		} else {
			dataCount = dao.dataCount_cb(condition, keyword,info.getUserId());
		}

		int rows = 10;
		int total_page = util.pageCount(rows, dataCount);
		if (current_page > total_page) {
			current_page = total_page;
		}

		int offset = (current_page - 1) * rows;
		if(offset < 0) offset = 0;

		List<ClassDTO> list;
		if (keyword.length() == 0)
			list = dao.listBoard_cb(offset, rows,info.getUserId());
		else
			list = dao.listBoard_cb(offset, rows, condition, keyword,info.getUserId());

		int listNum, n = 0;
		for(ClassDTO dto : list) {
			listNum = dataCount - (offset + n);
			dto.setListNum(listNum);
			n++;
		}

		String query = "";
		if (keyword.length() != 0) {
			query = "condition=" + condition + "&keyword=" + URLEncoder.encode(keyword, "utf-8");
		}

		String listUrl = cp + "/member/cb_list.do";
		String articleUrl = cp + "/member/cb_article.do?page=" + current_page;
		if (query.length() != 0) {
			listUrl += "?" + query;
			articleUrl += "&" + query;
		}

		String paging = util.paging(current_page, total_page, listUrl);

		req.setAttribute("list", list);
		req.setAttribute("dataCount", dataCount);
		req.setAttribute("page", current_page);
		req.setAttribute("total_page", total_page);
		req.setAttribute("articleUrl", articleUrl);
		req.setAttribute("paging", paging);
		req.setAttribute("condition", condition);
		req.setAttribute("keyword", keyword);
		req.setAttribute("mode", "myContent");

		forward(req, resp, "/WEB-INF/views/dayclass/list.jsp");
	}
	protected void cb_article(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 글보기
		ClassDAO dao=new ClassDAO();
		HttpSession session=req.getSession();
		SessionInfo info=(SessionInfo)session.getAttribute("member");
		String cp=req.getContextPath();
		String page = req.getParameter("page");
		String query="page="+page;

		try {
			int boardNum = Integer.parseInt(req.getParameter("boardNum"));

			String condition = req.getParameter("condition");
			String keyword = req.getParameter("keyword");
			if(condition==null) {	
				condition="all";
				keyword="";
			}
			keyword = URLDecoder.decode(keyword, "utf-8");

			if(keyword.length()!=0) {
				query+="&condition="+condition+
						"&keyword="+URLEncoder.encode(keyword, "utf-8");
			}

			ClassDTO dto=dao.readBoard_cb(boardNum,info.getUserId());
			int curClass = dao.readClass_cb(boardNum,info.getUserId());
			
			if(dto==null) {
				resp.sendRedirect(cp+"/member/cb_list.do?"+query);
				return;
			}

			MyUtil util=new MyUtil();
			dto.setContent(util.htmlSymbols(dto.getContent()));


			ClassDTO preReadDto=dao.preReadBoard_cb(dto.getBoardNum(), condition, keyword,info.getUserId());
			ClassDTO nextReadDto=dao.nextReadBoard_cb(dto.getBoardNum(), condition, keyword,info.getUserId());


			req.setAttribute("dto", dto);
			req.setAttribute("preReadDto", preReadDto);
			req.setAttribute("nextReadDto", nextReadDto);
			req.setAttribute("query", query);
			req.setAttribute("page", page);
			req.setAttribute("curClass", curClass);

			forward(req, resp, "/WEB-INF/views/dayclass/article.jsp");
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}

		resp.sendRedirect(cp+"/member/cb_list.do?"+query);
	}
	protected void gt_list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {		
		GreetingDAO dao = new GreetingDAO();
		HttpSession session=req.getSession();
		SessionInfo info=(SessionInfo)session.getAttribute("member");
		MyUtil util = new MyUtil();
		String cp = req.getContextPath();		
		
		//파라미터로 넘어온 페이지 번호
		String page = req.getParameter("page");
		int current_page = 1;
		if(page !=null) {
			current_page = Integer.parseInt(page);
		}
		//검색
		String condition = req.getParameter("condition");
		String keyword = req.getParameter("keyword");
		if(condition == null) {
			condition = "all";
			keyword="";
		}
		
		//GET 방식일 경우 디코딩
		if(req.getMethod().equalsIgnoreCase("GET")) {
			keyword = URLDecoder.decode(keyword, "utf-8");
		}
		
		//전체 데이터 개수
		int dataCount;
		if(keyword.length()==0) {
			dataCount = dao.dataCount_gt(info.getUserId());
		} else {
			dataCount = dao.dataCount_gt(condition, keyword,info.getUserId());
		}
		//전체 페이지 수 
		int rows = 10; 
		int total_page = util.pageCount(rows, dataCount);
		if(current_page> total_page) {
			current_page = total_page;
		}
		int offset = (current_page -1) * rows;
		if(offset < 0) offset = 0;
		
		//게시글 가져오기
		List<GreetingDTO> list = null;
		if(keyword.length()==0) {
			list = dao.listBoard_gt(offset, rows,info.getUserId());
		}else {
			list = dao.listBoard_gt(offset, rows, condition, keyword,info.getUserId());
		}		
		
		//리스트 글번호 만들기
		int listNum, n=0;
		for(GreetingDTO dto : list) {
			listNum = dataCount - (offset+n);
			dto.setListNum(listNum);
			n++;
		}
		
		String query="";
		if(keyword.length()!=0) {
			query="condition="+condition+"&keyword="+URLEncoder.encode(keyword, "utf-8");
		}
		
		//페이징 처리
		String listUrl = cp+"/member/gt_list.do";
		String articleUrl = cp+"/member/gt_article.do?page="+current_page;
		if(query.length()!=0) {
			listUrl += "?" +query;
			articleUrl += "&" +query;
		}
		String paging= util.paging(current_page, total_page, listUrl);
		
		//포워딩할 JSP로 넘길 속성
		req.setAttribute("list", list);
		req.setAttribute("paging",paging);
		req.setAttribute("page", current_page );
		req.setAttribute("dataCount", dataCount );
		req.setAttribute("total_page", dataCount );
		req.setAttribute("articleUrl", articleUrl );
		req.setAttribute("condition", condition);
		req.setAttribute("keyword", keyword );
		req.setAttribute("mode", "myContent");
		
		//JSP로 포워딩
		forward(req, resp, "/WEB-INF/views/greeting/list.jsp");
	}
	protected void gt_article(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//게시글 보기(페이지번호는 무조건 넘어오고 condition,keyword는 검색일 때만 넘어옴)
		GreetingDAO dao = new GreetingDAO();
		String cp = req.getContextPath();
		String page = req.getParameter("page");
		String query = "page="+page;
		HttpSession session=req.getSession();
		SessionInfo info=(SessionInfo)session.getAttribute("member");
								
		try {
			int num = Integer.parseInt(req.getParameter("num"));
									
			String condition = req.getParameter("condition");
			String keyword = req.getParameter("keyword");
			if(condition == null) {//검색이 x 때 
				condition = "all";
				keyword = "";
			}
			keyword = URLDecoder.decode(keyword, "utf-8");//넘어온 방식이GET방식이기 때문에 인코딩되어 넘어와서 다시 디코딩 해줌
									
			if(keyword.length() !=0) {//검색일 때 
					query += "&condition="+condition+"&keyword="+URLEncoder.encode(keyword,"utf-8");
			}
									
			//조회수
			dao.updateHitCount_gt(num,info.getUserId());
									
			//게시글 가져오기 
			GreetingDTO dto = dao.readGreeting_gt(num,info.getUserId());
			if(dto == null) {
				resp.sendRedirect(cp+"/member/gt_list.do?"+query);
				return;
			}
			dto.setContent(dto.getContent().replace("\n", "<br>"));	
			GreetingDTO preReadDto = dao.preReadGreeting_gt(num, condition, keyword,info.getUserId());
			GreetingDTO nextReadDto =dao.nextReadGreeting_gt(num, condition, keyword,info.getUserId());
									
			req.setAttribute("dto", dto);
			req.setAttribute("preReadDto", preReadDto);
			req.setAttribute("nextReadDto", nextReadDto);
			req.setAttribute("page", page);
			req.setAttribute("query", query);
									
			forward(req, resp, "/WEB-INF/views/greeting/article.jsp");
			return;			
			} catch (Exception e) {
				e.printStackTrace();
			}
								
			resp.sendRedirect(cp+"/member/gt_list.do?"+query);	
		
	}
	protected void nb_list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		NeighborDAO dao = new NeighborDAO();
		MyUtil util = new MyUtil();
		String cp = req.getContextPath();
		HttpSession session=req.getSession();
		SessionInfo info=(SessionInfo)session.getAttribute("member");
		String page = req.getParameter("page");
		int current_page = 1;
		if (page != null)
			current_page = Integer.parseInt(page);

		String condition = req.getParameter("condition");
		String keyword = req.getParameter("keyword");
		if (condition == null) {
			condition = "all";
			keyword = "";
		}

		if (req.getMethod().equalsIgnoreCase("GET")) {
			keyword = URLDecoder.decode(keyword, "UTF-8");
		}

		int dataCount;
		if (keyword.length() == 0)
			dataCount = dao.dataCount_nb(info.getUserId());
		else
			dataCount = dao.dataCount_nb(condition, keyword,info.getUserId());
		int rows = 10;
		int total_page = util.pageCount(rows, dataCount);
		if (current_page > total_page)
			current_page = total_page;

		int offset = (current_page - 1) * rows;
		if (offset < 0)
			offset = 0;

		List<NeighborDTO> list = null;
		if (keyword.length() == 0) {
			list = dao.listBoard_nb(offset, rows,info.getUserId());
		} else {
			list = dao.listBoard_nb(offset, rows, condition, keyword,info.getUserId());
		}

		int listNum, n = 0;
		for (NeighborDTO dto : list) {
			listNum = dataCount - (offset + n);
			dto.setListNum(listNum);
			n++;
		}

		String query = "";
		if (keyword.length() != 0) {
			query = "condition=" + condition + "&keyword=" + URLEncoder.encode(keyword, "utf-8");
		}

		String listUrl = cp + "/member/nb_list.do";
		String articleUrl = cp + "/member/nb_list.do?page=" + current_page;
		if (query.length() != 0) {
			listUrl += "?" + query;
			articleUrl += "&" + query;
		}

		String paging = util.paging(current_page, total_page, listUrl);

		req.setAttribute("list", list);
		req.setAttribute("page", current_page);
		req.setAttribute("total_page", total_page);
		req.setAttribute("dataCount", dataCount);
		req.setAttribute("articleUrl", articleUrl);
		req.setAttribute("paging", paging);
		req.setAttribute("condition", condition);
		req.setAttribute("keyword", keyword);
		req.setAttribute("mode", "myContent");

		forward(req, resp, "/WEB-INF/views/neighbor/list.jsp");

	}
	protected void qa_list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		QaDAO dao = new QaDAO();
		MyUtil util = new MyUtil();
		String cp = req.getContextPath();
		HttpSession session=req.getSession();
		SessionInfo info=(SessionInfo)session.getAttribute("member");
		String page = req.getParameter("page");
		int current_page = 1;
		if(page != null) {
			current_page = Integer.parseInt(page);
		}
		
		String condition = req.getParameter("condition");
		String keyword = req.getParameter("keyword");
		if(condition == null) {
			condition = "all";
			keyword="";
		}
		
		if(req.getMethod().equalsIgnoreCase("GET")) {
			keyword = URLDecoder.decode(keyword, "utf-8");
		}
		
		
		int dataCount;
		if(keyword.length()==0) {
			dataCount = dao.dataCount_qa(info.getUserId());
		} else {
			dataCount = dao.dataCount_qa(condition, keyword,info.getUserId());
		}
		
		int rows = 10;
		int total_page = util.pageCount(rows, dataCount);
		if (current_page > total_page) {
			current_page = total_page;
		}
		
		int offset = (current_page - 1) * rows;
		if(offset < 0) offset = 0;

		List<QaDTO> list;
		if (keyword.length() == 0)
			list = dao.listBoard_qa(offset, rows,info.getUserId());
		else
			list = dao.listBoard_qa(offset, rows, condition, keyword,info.getUserId());
		
		int listNum, n = 0;
		for(QaDTO dto : list) {
			listNum = dataCount - (offset + n);
			dto.setListNum(listNum);
			n++;
		}
		
		String query = "";
		if (keyword.length() != 0) {
			query = "condition=" + condition + "&keyword=" + URLEncoder.encode(keyword, "utf-8");
		}
		
		String listUrl = cp + "/member/qa_list.do";
		String articleUrl = cp + "/member/qa_article.do?page=" + current_page;
		if (query.length() != 0) {
			listUrl += "?" + query;
			articleUrl += "&" + query;
		}

		String paging = util.paging(current_page, total_page, listUrl);

		req.setAttribute("list", list);
		req.setAttribute("dataCount", dataCount);
		req.setAttribute("page", current_page);
		req.setAttribute("total_page", total_page);
		req.setAttribute("articleUrl", articleUrl);
		req.setAttribute("paging", paging);
		req.setAttribute("condition", condition);
		req.setAttribute("keyword", keyword);
		req.setAttribute("mode", "myContent");
		
		forward(req, resp, "/WEB-INF/views/qa/list.jsp");
	}
	protected void qa_article(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 글보기
		QaDAO dao=new QaDAO();
		String cp=req.getContextPath();
		HttpSession session=req.getSession();
		SessionInfo info=(SessionInfo)session.getAttribute("member");
		String page = req.getParameter("page");
		String query="page="+page;
		
		try {
			int boardNum = Integer.parseInt(req.getParameter("boardNum"));
			
			String condition = req.getParameter("condition");
			String keyword = req.getParameter("keyword");
			if(condition==null) {
				condition="all";
				keyword="";
			}
			keyword = URLDecoder.decode(keyword, "utf-8");
			
			if(keyword.length()!=0) {
				query+="&condition="+condition+
						"&keyword="+URLEncoder.encode(keyword, "utf-8");
			}
			
			dao.updateHitCount_qa(boardNum,info.getUserId());
			QaDTO dto=dao.readBoard_qa(boardNum,info.getUserId());
			if(dto==null) {
				resp.sendRedirect(cp+"/member/qa_list.do?"+query);
				return;
			}
			
			// dto.setContent(dto.getContent().replaceAll("\n", "<br>"));
			MyUtil util=new MyUtil();
			dto.setContent(util.htmlSymbols(dto.getContent()));
			
			QaDTO preReadDto=dao.preReadBoard_qa(dto.getGroupNum(),
					dto.getOrderNo(), condition, keyword,info.getUserId());
			QaDTO nextReadDto=dao.nextReadBoard_qa(dto.getGroupNum(),
					dto.getOrderNo(), condition, keyword,info.getUserId());
		
			req.setAttribute("dto", dto);
			req.setAttribute("preReadDto", preReadDto);
			req.setAttribute("nextReadDto", nextReadDto);
			req.setAttribute("query", query);
			req.setAttribute("page", page);
			
			forward(req, resp, "/WEB-INF/views/qa/article.jsp");
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		resp.sendRedirect(cp+"/member/qa_list.do?"+query);
	}
	protected void st_list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		StoreDAO dao = new StoreDAO();
		MyUtil util = new MyUtil();
		String cp = req.getContextPath();
		HttpSession session=req.getSession();
		SessionInfo info=(SessionInfo)session.getAttribute("member");
		String page = req.getParameter("page");
		String keyword=req.getParameter("keyword");
		int current_page = 1;
		if (page != null) {
			current_page = Integer.parseInt(page);
		}
		
		int dataCount;
		if(keyword==null || keyword.equals("ALL"))
			dataCount=dao.dataCount_st(info.getUserId());
		else
			dataCount=dao.dataCount_st(keyword,info.getUserId());
		
		int rows = 6;
		int total_page = util.pageCount(rows, dataCount);
		if (current_page>total_page) {
			current_page = total_page;
		}
		
		int offset = (current_page - 1) * rows;
		if (offset < 0)
			offset = 0;
		List<StoreDTO> list = null;
		
		if(keyword==null || keyword.equals("ALL")) {
			list = dao.listStore_st(offset, rows,info.getUserId());
		} else {
			list = dao.listStore_st(offset, rows, keyword,info.getUserId());
		}
		
		String query = "";
		if(keyword!=null && !keyword.equals("ALL")) {
			query="keyword="+URLEncoder.encode(keyword, "utf-8");
		}
		String listUrl = cp + "/member/st_list.do";
		String articleUrl = cp + "/member/st_article.do?page=" + current_page;
		if(query.length() !=0) {
			listUrl += "?" + query;
			articleUrl += "&" + articleUrl;
		}
		String paging = util.paging(current_page, total_page, listUrl);
		
		req.setAttribute("list", list);
		req.setAttribute("paging", paging);
		req.setAttribute("page", page);
		req.setAttribute("dataCount", dataCount);
		req.setAttribute("total_page", total_page);
		req.setAttribute("articleUrl", articleUrl);
		req.setAttribute("keyword", keyword);
		req.setAttribute("mode", "myContent");
		forward(req,resp, "/WEB-INF/views/store/list.jsp");
		
	}
	protected void st_article(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		StoreDAO dao = new StoreDAO();
		String cp = req.getContextPath();
		String page = req.getParameter("page");
		String query = "page="+page;
		boolean isGrade = true;
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		try {
			int num = Integer.parseInt(req.getParameter("num"));
			StoreDTO dto = dao.readStore(num);
			
			if(dto==null) {
				resp.sendRedirect(cp+"/store/list.do?"+query);
			}
			
			dto.setContent(dto.getContent().replace("\n", "<br>"));
			
			StoreDTO preReadDto = dao.preReadStore_st(num,info.getUserId());
			StoreDTO nextReadDto = dao.nextReadStore_st(num,info.getUserId());
			
			req.setAttribute("dto", dto);
			req.setAttribute("page", page);
			req.setAttribute("query", query);
			req.setAttribute("preReadDto", preReadDto);
			req.setAttribute("nextReadDto", nextReadDto);
			
			if (info != null) {
				StoreDTO grade = new StoreDTO();
				grade.setNum(num);
				grade.setUserId(info.getUserId());
				isGrade = dao.RecCheck(grade);
				req.setAttribute("isGrade", isGrade);
			}
			
			forward(req, resp, "/WEB-INF/views/store/article.jsp");
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}
		resp.sendRedirect(cp+"/member/st_list.do?"+query);
	}
	protected void ud_list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 게시글 리스트
		UdongDAO dao = new UdongDAO();
		MyUtil util = new MyUtil();
		String cp = req.getContextPath();
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		// 파라미터로 넘어온 페이지 번호
		String page = req.getParameter("page");
		int current_page = 1;
		if(page != null) {
			current_page = Integer.parseInt(page);
		}
		
		// 검색
		String condition = req.getParameter("condition");
		String keyword = req.getParameter("keyword");
		if(condition==null) {
			condition = "all";
			keyword = "";
		}
		if(req.getMethod().equalsIgnoreCase("GET")) {
			keyword = URLDecoder.decode(keyword, "utf-8");
		}
		
		// 데이터 개수
		int dataCount;
		if(keyword.length()==0) {
			dataCount = dao.dataCount_ud(info.getUserId());
		} else {
			dataCount = dao.dataCount_ud(condition, keyword,info.getUserId());
		}
		
		int rows = 10;
		int total_page = util.pageCount(rows, dataCount);
		if(current_page > total_page) {
			current_page = total_page;
		}
		
		int offset = (current_page - 1) * rows;
		if(offset < 0) offset = 0;
		
		// 게시글 가져오기
		List<UdongDTO> list = null;
		if(keyword.length() == 0) {
			list = dao.listBoard_ud(offset, rows,info.getUserId());
		} else {
			list = dao.listBoard_ud(offset, rows, condition, keyword,info.getUserId());
		}
		
		// 리스트 글번호 만들기
		int listNum, n=0;
		for(UdongDTO dto : list) {
			listNum = dataCount - (offset+n);
			dto.setListNum(listNum);
			n++;
		}
		
		String query="";
		if(keyword.length()!=0) {
			query="condition="+condition+"&keyword="+URLEncoder.encode(keyword, "utf-8");
		}
		
		String listUrl = cp+"/member/ud_list.do";
		String articleUrl = cp+"/member/ud_article.do?page="+current_page;
		if(query.length()!=0) {
			listUrl += "?" + query;
			articleUrl += "&" + query;
		}
		String paging = util.paging(current_page, total_page, listUrl);
		
		// list.jsp에 넘겨줄 데이터
		req.setAttribute("list", list);
		req.setAttribute("paging", paging);
		req.setAttribute("page", current_page);
		req.setAttribute("dataCount", dataCount);
		req.setAttribute("total_page", total_page);
		req.setAttribute("articleUrl", articleUrl);
		req.setAttribute("condition", condition);
		req.setAttribute("keyword", keyword);
		req.setAttribute("mode", "myContent");
		forward(req, resp, "/WEB-INF/views/udong/list.jsp");
	}
	protected void ud_article(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 게시글 보기
		UdongDAO dao = new UdongDAO();
		String cp = req.getContextPath();
		String page = req.getParameter("page");
		String query = "page="+page;
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		
		try {
			int num = Integer.parseInt(req.getParameter("num"));
			
			String condition = req.getParameter("condition");
			String keyword = req.getParameter("keyword");
			if(condition == null) {
				condition = "all";
				keyword = ""; 
			}
			keyword = URLDecoder.decode(keyword, "utf-8");
			
			if(keyword.length() != 0) {
				query += "&condition="+condition+"&keyword="+URLEncoder.encode(keyword,"utf-8");
			}
			
			// 조회수
			dao.updateHitCount_ud(num,info.getUserId());
			
			// 게시글 가져오기
			UdongDTO dto = dao.readBoard_ud(num,info.getUserId());
			if(dto == null) {
				resp.sendRedirect(cp+"/member/ud_list.do?"+query);
				return;
			}
			
			dto.setContent(dto.getContent().replaceAll("\n", "<br>"));
			
			UdongDTO preReadDto = dao.preReadBoard_ud(num, condition, keyword,info.getUserId());
			UdongDTO nextReadDto = dao.nextReadBoard_ud(num, condition, keyword,info.getUserId());
			
			req.setAttribute("dto", dto);
			req.setAttribute("preReadDto", preReadDto);
			req.setAttribute("nextReadDto", nextReadDto);
			req.setAttribute("page", page);
			req.setAttribute("query", query);
			
			forward(req, resp, "/WEB-INF/views/udong/article.jsp");
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		resp.sendRedirect(cp+"/member/ud_list.do?"+query);

	}
	private void used_list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		UsedDAO dao = new UsedDAO();
		MyUtil util = new MyUtil();
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		// 게시물 리스트
		String cp = req.getContextPath();
		
		//파라미터로 넘어온 페이지 번호
		String page = req.getParameter("page");
		int current_page = 1;
		if(page !=null) {
			current_page = Integer.parseInt(page);
		}
		
		// 검색
		String condition=req.getParameter("condition");
		String keyword=req.getParameter("keyword");
		if(condition==null) {
			condition="all";
			keyword="";
		}
		
		// GET 방식인 경우 디코딩
		if(req.getMethod().equalsIgnoreCase("GET")) {
			keyword=URLDecoder.decode(keyword, "utf-8");
		}
		
		//전체 데이터 개수
		int dataCount;
		if(keyword.length()==0) {
		dataCount = dao.dataCount_used(info.getUserId());
		} else {
		dataCount = dao.dataCount_used(condition, keyword,info.getUserId());
		}
		
		// 전체 페이지 수
		int rows=10;
		int total_page=util.pageCount(rows, dataCount);
		if(current_page>total_page)
			current_page=total_page;
		
		int offset = (current_page-1) * rows;
		if(offset < 0) offset = 0;
		
		// 게시물 가져오기
		List<UsedDTO> list=null;
		if(keyword.length()==0) {
			list=dao.listUsed_used(offset, rows,info.getUserId());
		} else {
			list=dao.listUsed_used(offset, rows, condition, keyword,info.getUserId());
		}
		
		// 리스트 글번호 만들기
		int listNum, n=0;
		for(UsedDTO dto:list) {
			listNum=dataCount-(offset+n);
			dto.setListNum(listNum);
			n++;
		}
		
		String query="";
		if(keyword.length()!=0) {
			query="condition="+condition+ "&keyword="+URLEncoder.encode(keyword, "utf-8");
		}
		
		// 페이징 처리
		String listUrl=cp+"/member/used_list.do";
		String articleUrl=cp+"/member/uesd_article.do?page="+current_page;
		if(query.length()!=0) {
			listUrl+="?"+query;
			articleUrl+="&"+query;
		}
		
		String paging=util.paging(current_page, total_page, listUrl);
		
		// 포워딩할 JSP로 넘길 속성
		req.setAttribute("list", list);
		req.setAttribute("page", current_page);
		req.setAttribute("total_page", total_page);
		req.setAttribute("dataCount", dataCount);
		req.setAttribute("articleUrl", articleUrl);
		req.setAttribute("paging", paging);
		req.setAttribute("condition", condition);
		req.setAttribute("keyword", keyword);
		req.setAttribute("mode", "myContent");
		// JSP로 포워딩
		forward(req, resp, "/WEB-INF/views/used/list.jsp");
	}
	private void used_article(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		UsedDAO dao=new UsedDAO();
		String cp = req.getContextPath();
		String page = req.getParameter("page");
		String query = "page="+page;
								
		try {
			int num = Integer.parseInt(req.getParameter("num"));
									
			String condition = req.getParameter("condition");
			String keyword = req.getParameter("keyword");
			if(condition == null) {//검색이 x 때 
				condition = "all";
				keyword = "";
			}
			keyword = URLDecoder.decode(keyword, "utf-8");//넘어온 방식이GET방식이기 때문에 인코딩되어 넘어와서 다시 디코딩 해줌
									
			if(keyword.length() !=0) {//검색일 때 
					query += "&condition="+condition+"&keyword="+URLEncoder.encode(keyword,"utf-8");
			}
			
						
			//게시글 가져오기 
			UsedDTO dto = dao.readUsed_used(num,info.getUserId());
			if(dto == null) { //게시글이 없으면
				resp.sendRedirect(cp+"/member/used_list.do?"+query); //리스트로
				return;
			}
			dto.setContent(dto.getContent().replace("\n", "<br>"));	
			
			UsedDTO preReadUsed = dao.preReadUsed_used(num, condition, keyword,info.getUserId());
			UsedDTO nextReadUsed =dao.nextReadUsed_used(num, condition, keyword,info.getUserId());
									
			req.setAttribute("dto", dto);
			req.setAttribute("preReadUsed", preReadUsed);
			req.setAttribute("nextReadUsed", nextReadUsed);
			req.setAttribute("page", page);
			req.setAttribute("query", query);
									
			forward(req, resp, "/WEB-INF/views/used/article.jsp");
			return;			
			} catch (Exception e) {
				e.printStackTrace();
			}
								
			resp.sendRedirect(cp+"/member/used_list.do?"+query);	
		
	}	
}
