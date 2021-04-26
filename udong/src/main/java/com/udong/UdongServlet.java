package com.udong;

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

@WebServlet("/udong/*")
public class UdongServlet extends HttpServlet {
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
		// 포워딩
		RequestDispatcher rd = req.getRequestDispatcher(path);
		rd.forward(req, resp);
	}
	
	protected void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("utf-8");
		String uri = req.getRequestURI();
		
		// 로그인이 되지 않은 상태이면 로그인 페이지로
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		if(info == null) {
			String cp = req.getContextPath();
			resp.sendRedirect(cp+"/member/login.do");
			return;
		}
		
		if(uri.indexOf("list.do") != -1) {
			list(req, resp);
		} else if(uri.indexOf("created.do") != -1) {
			createdForm(req, resp);
		} else if(uri.indexOf("created_ok.do") != -1) {
			createdSubmit(req, resp);
		} else if(uri.indexOf("article.do") != -1) {
			article(req, resp);
		} else if(uri.indexOf("update.do") != -1) {
			updateForm(req, resp);
		} else if(uri.indexOf("update_ok.do") != -1) {
			updateSubmit(req, resp);
		} else if(uri.indexOf("delete.do") != -1) {
			delete(req, resp);
		}
		
	}
	
	protected void list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 게시글 리스트
		UdongDAO dao = new UdongDAO();
		MyUtil util = new MyUtil();
		String cp = req.getContextPath();
		
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
			dataCount = dao.dataCount();
		} else {
			dataCount = dao.dataCount(condition, keyword);
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
			list = dao.listBoard(offset, rows);
		} else {
			list = dao.listBoard(offset, rows, condition, keyword);
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
		
		String listUrl = cp+"/udong/list.do";
		String articleUrl = cp+"/udong/article.do?page="+current_page;
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
		
		forward(req, resp, "/WEB-INF/views/udong/list.jsp");
	}
	
	protected void createdForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setAttribute("mode", "created");
		forward(req, resp, "/WEB-INF/views/udong/created.jsp");
	}
	
	protected void createdSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		UdongDAO dao = new UdongDAO();
		UdongDTO dto = new UdongDTO();
		
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		
		try {
			dto.setSubject(req.getParameter("subject"));
			dto.setContent(req.getParameter("content"));
			dto.setUserId(info.getUserId()); // 세션의 아이디
			
			dao.insertBoard(dto);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		String cp = req.getContextPath();
		resp.sendRedirect(cp+"/udong/list.do");
	}
	
	protected void article(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 게시글 보기
		UdongDAO dao = new UdongDAO();
		String cp = req.getContextPath();
		String page = req.getParameter("page");
		String query = "page="+page;
		
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
			dao.updateHitCount(num);
			
			// 게시글 가져오기
			UdongDTO dto = dao.readBoard(num);
			if(dto == null) {
				resp.sendRedirect(cp+"/udong/list.do?"+query);
				return;
			}
			
			dto.setContent(dto.getContent().replaceAll("\n", "<br>"));
			
			UdongDTO preReadDto = dao.preReadBoard(num, condition, keyword);
			UdongDTO nextReadDto = dao.nextReadBoard(num, condition, keyword);
			
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
		
		resp.sendRedirect(cp+"/udong/list.do?"+query);

	}

	protected void updateForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

	}

	protected void updateSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

	}

	protected void delete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

	}
	
}
