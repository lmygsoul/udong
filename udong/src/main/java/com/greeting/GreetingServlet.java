package com.greeting;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.member.SessionInfo;
import com.util.MyUtil;

@WebServlet("/greeting/*")
public class GreetingServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		process(req, resp);
	} 

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		process(req, resp);
	}

	protected void forward(HttpServletRequest req, HttpServletResponse resp, String path) throws ServletException, IOException {
		//포워딩
		RequestDispatcher rd = req.getRequestDispatcher(path);
		rd.forward(req, resp);
	}
	
	protected void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("utf-8");
		
		String uri = req.getRequestURI();
		
		//로그인이 되지 않은 상태이면 로그인 페이지로
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		if (info==null) { //=로그인된 정보가 없다 => 로그인되어있지 x -> 로그인 페이지로
			String cp = req.getContextPath();
			resp.sendRedirect(cp+"/member/login.do");
			return;
		}
		
		if(uri.indexOf("list.do")!=-1) {//리스트
			list(req, resp);
		} else if (uri.indexOf("created.do") != -1) {//글작성
			created(req, resp);
		} else if(uri.indexOf("created_ok.do")!=-1) {//글보내기
			createdSubmit(req, resp);
		}else if(uri.indexOf("article.do")!=-1) {//글보기
			article(req, resp);
		}else if(uri.indexOf("update.do")!=-1) {//수정
			updateForm(req, resp);
		}else if(uri.indexOf("update_ok.do")!=-1) {//수정완료
			updateSubmit(req, resp);
		}else if(uri.indexOf("delete.do")!=-1) {//삭제
			delete(req, resp);
		}	
	}
	
	//리스트
	protected void list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {		
		GreetingDAO dao = new GreetingDAO();
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
			dataCount = dao.dataCount();
		} else {
			dataCount = dao.dataCount(condition, keyword);
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
			list = dao.listGreeting(offset, rows);
		}else {
			list = dao.listBoard(offset, rows, condition, keyword);
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
		String listUrl = cp+"/greeting/list.do";
		String articleUrl = cp+"/greeting/article.do?page="+current_page;
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
		
		//JSP로 포워딩
		forward(req, resp, "/WEB-INF/views/greeting/list.jsp");
	}
	
	//글작성
	protected void created(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setAttribute("mode", "created");
		forward(req, resp, "/WEB-INF/views/greeting/created.jsp");
	}
	
	//글저장
	protected void createdSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String cp = req.getContextPath();

		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");
		
		GreetingDAO dao = new GreetingDAO();

		try {
			GreetingDTO dto=new GreetingDTO();
			
			// userId는 세션에 저장된 정보
			dto.setUserId(info.getUserId()); //작성자아이디	
			// 파라미터
			dto.setSubject(req.getParameter("subject")); //제목
			dto.setContent(req.getParameter("content")); //내용
		
			
			dao.insertGreeting(dto);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		resp.sendRedirect(cp+"/greeting/list.do");
	}
	
	//글보기
	protected void article(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//게시글 보기(페이지번호는 무조건 넘어오고 condition,keyword는 검색일 때만 넘어옴)
		GreetingDAO dao = new GreetingDAO();
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
									
			//조회수
			dao.updateHitCount(num);
									
			//게시글 가져오기 
			GreetingDTO dto = dao.readGreeting(num);
			if(dto == null) {
				resp.sendRedirect(cp+"/greeting/list.do?"+query);
				return;
			}
			dto.setContent(dto.getContent().replace("\n", "<br>"));	
			GreetingDTO preReadDto = dao.preReadGreeting(num, condition, keyword);
			GreetingDTO nextReadDto =dao.nextReadGreeting(num, condition, keyword);
									
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
								
			resp.sendRedirect(cp+"/greeting/list.do?"+query);	
		
	}					
			
	//글수정
	protected void updateForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String cp = req.getContextPath();
		GreetingDAO dao = new GreetingDAO();

		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");

		String page=req.getParameter("page");
		
		try {
			int num=Integer.parseInt(req.getParameter("num"));
			GreetingDTO dto=dao.readGreeting(num);
			
			if(dto==null) {
				resp.sendRedirect(cp+"/greeting/list.do?page="+page);
				return;
			}
			
			// 게시물을 올린 사용자가 아니면
			if(! dto.getUserId().equals(info.getUserId())) {
				resp.sendRedirect(cp+"/greeting/list.do?page="+page);
				return;
			}
			
			req.setAttribute("dto", dto);
			req.setAttribute("page", page);
			req.setAttribute("mode", "update");
			
			forward(req, resp, "/WEB-INF/views/greeting/created.jsp");
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		resp.sendRedirect(cp+"/greeting/list.do?page="+page);
	}

	//글수정 완료
	protected void updateSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String cp = req.getContextPath();
		GreetingDAO dao = new GreetingDAO();
		
		HttpSession session=req.getSession();
		SessionInfo info=(SessionInfo)session.getAttribute("member");
		String page=req.getParameter("page");
		
		try {
			if(req.getMethod().equalsIgnoreCase("GET")) {
				resp.sendRedirect(cp+"/greeting/list.do?page="+page);
				return;
			}
			
			GreetingDTO dto=new GreetingDTO();
			dto.setNum(Integer.parseInt(req.getParameter("num")));
			dto.setSubject(req.getParameter("subject"));
			dto.setContent(req.getParameter("content"));
			
			dto.setUserId(info.getUserId());
			
			dao.updateGreeting(dto);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		resp.sendRedirect(cp+"/greeting/list.do?page="+page);
	}
	
	//글 삭제
	protected void delete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String cp = req.getContextPath();
		GreetingDAO dao = new GreetingDAO();

		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo) session.getAttribute("member");
		String page=req.getParameter("page");
		String query="page="+page;
		
		try {
			int num=Integer.parseInt(req.getParameter("num"));
			String condition=req.getParameter("condition");
			String keyword=req.getParameter("keyword");
			if(condition==null) {
				condition="all";
				keyword="";
			}
			keyword=URLDecoder.decode(keyword, "utf-8");

			if(keyword.length()!=0) {
				query+="&condition="+condition+"&keyword="+URLEncoder.encode(keyword, "UTF-8");
			}

			dao.deleteGreeting(num, info.getUserId());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		resp.sendRedirect(cp+"/greeting/list.do?"+query);	
	}
	
	
	
}
