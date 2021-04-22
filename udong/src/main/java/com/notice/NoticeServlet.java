package com.notice;

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
// import javax.servlet.http.HttpSession;

import com.util.MyUtil;

@WebServlet("/notice/*")
public class NoticeServlet extends HttpServlet {
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
		/*HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		
		if (info==null) {
			String cp = req.getContextPath();
			resp.sendRedirect(cp+"/member/login.do");
			return;
		}
		*/
		if(uri.indexOf("list.do")!=-1) {
			list(req, resp);
		} else if(uri.indexOf("created.do")!=-1) {
			createdForm(req, resp);
		} else if(uri.indexOf("created_ok.do")!=-1) {
			createdSubmit(req, resp);
		} else if(uri.indexOf("article.do")!=-1) {
			article(req, resp);
		} else if(uri.indexOf("update.do")!=-1) {
			updateForm(req, resp);
		} else if(uri.indexOf("update_ok.do")!=-1) {
			updateSubmit(req, resp);
		} else if(uri.indexOf("delete.do")!=-1) {
			delete(req, resp);
		}
	}
	
	protected void list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		NoticeDAO dao = new NoticeDAO();
		MyUtil util = new MyUtil();
		String cp = req.getContextPath();
		
		String page= req.getParameter("page");
		int current_page=1;
		
		if(page != null) {
			current_page = Integer.parseInt(page);
		}
		
		String condition = req.getParameter("condition");
		String keyword = req.getParameter("keyword");
		if(condition==null) {
			condition = "all";
			keyword = "";
		}
		if (req.getMethod().equalsIgnoreCase("GET")) {
			keyword = URLDecoder.decode(keyword, "utf-8");
		}
		
		int dataCount;
		if(keyword.length()==0) {
			dataCount = dao.dataCount();
		} else {
			dataCount = dao.dataCount(condition, keyword);
		}
		
		int rows = 10;
		int total_page = util.pageCount(rows, dataCount);
		
		if(current_page > total_page) {
			current_page=total_page;
		}
		
		int offset = (current_page -1 ) * rows;
		if(offset <0) offset = 0;
		
		List<NoticeDTO> list = null;
		if(keyword.length()==0) {
			list = dao.listNotice(offset, rows);
		}else {
			list = dao.listNotice(offset, rows, condition, keyword);
		}
		
		int listNum, n=0;
		for(NoticeDTO dto: list) {
			listNum = dataCount - (offset+n);
			dto.setListNum(listNum);
			n++;
		}
		
		String query = "";
		if(keyword.length()!=0) {
			query="condition="+condition+"&keyword="+URLEncoder.encode(keyword, "utf-8");
		}
		
		String listUrl = cp +"/notice/list.do";
		String articleUrl = cp+"/notice/article.do?page="+current_page;
		if(query.length()!=0) {
			listUrl += "?"+query;
			articleUrl += "&" + articleUrl;
		}
		
		String paging = util.paging(current_page, total_page, listUrl);
		
		req.setAttribute("list", list);
		req.setAttribute("paging", paging);
		req.setAttribute("page", current_page);
		req.setAttribute("dataCount", dataCount);
		req.setAttribute("total_page", total_page);
		req.setAttribute("articleUrl", articleUrl);
		req.setAttribute("condition", condition);
		req.setAttribute("keyword", keyword);
		
		forward(req, resp, "/WEB-INF/views/notice/list.jsp");
	}
	
	protected void createdForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setAttribute("mode", "created");
		forward(req, resp, "/WEB-INF/views/notice/created.jsp");
	}
	
	protected void createdSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		NoticeDAO dao = new NoticeDAO();
		NoticeDTO dto = new NoticeDTO();
		
		// HttpSession session = req.getSession();
		// SessionInfo info = (SessionInfo)session.getAttribute("member");
		
		try {
			dto.setSubject(req.getParameter("subject"));
			dto.setContent(req.getParameter("content"));
			// dto.setUserId(info.getUserId()); //세션의 아이디
			
			dao.insertNotice(dto);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		String cp = req.getContextPath();
		resp.sendRedirect(cp+"/notice/list.do");
	}
	
	protected void article(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		NoticeDAO dao = new NoticeDAO();
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
			
			if(keyword.length()!=0) {
				query+="&condition="+condition+"&keyword="+URLEncoder.encode(keyword, "utf-8");
			}
			
			//조회수
			dao.updateHitCount(num);
			
			//게시글 가져오기
			NoticeDTO dto = dao.readNotice(num);
			if(dto==null) {
				resp.sendRedirect(cp+"/notice/list.do?"+query);
				return;
			}
			dto.setContent(dto.getContent().replaceAll("\n", "<br>"));
			
			NoticeDTO preReadDto = dao.preReadNotice(num, condition, keyword);
			NoticeDTO nextReadDto = dao.nextReadNotice(num, condition, keyword);
			
			req.setAttribute("dto", dto);
			req.setAttribute("preReadDto", preReadDto);
			req.setAttribute("nextReadDto", nextReadDto);
			req.setAttribute("page", page);
			req.setAttribute("query", query);
			
			forward(req, resp, "/WEB-INF/views/notice/article.jsp");
			return;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	protected void updateForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// NoticeDAO dao = new NoticeDAO();
		// HttpSession session = req.getSession();
		// SessionInfo info = (SessionInfo)session.getAttribute("member");
		
		String page = req.getParameter("page");
		
		try {
			// int num = Integer.parseInt(req.getParameter("num"));
			// NoticeDTO dto = dao.readNotice(num);
			
			// DB에 데이터가 있고(dto), 게시글을 올린 사람만(로그인한 사람 info)
			/*
			if(dto!=null && dto.getUserId().equals(info.getUserId())) {
				req.setAttribute("dto", dto);
				req.setAttribute("page", page);
				req.setAttribute("mode", "update");
				forward(req, resp, "/WEB-INF/views/notice/created.jsp"); // created.jsp - textarea 수정
				return;
			}
			*/
		} catch (Exception e) {
			e.printStackTrace();
		}
		// if 조건 미충족 -> list로 돌아감
		String cp = req.getContextPath();
		resp.sendRedirect(cp+"/notice/list.do?page="+page);
	}
	
	protected void updateSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		NoticeDAO dao = new NoticeDAO();
		// HttpSession session = req.getSession();
		//SessionInfo info = (SessionInfo)session.getAttribute("member");
		String page = req.getParameter("page");
		
		try {
			NoticeDTO dto = new NoticeDTO();
			dto.setNum(Integer.parseInt(req.getParameter("num")));
			dto.setSubject(req.getParameter("subject"));
			dto.setContent(req.getParameter("content"));
			// dto.setUserId(info.getUserId());
			
			dao.updateNotice(dto);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		String cp = req.getContextPath();
		resp.sendRedirect(cp+"/notice/list.do?page="+page);
	}
	
	protected void delete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// NoticeDAO dao = new NoticeDAO();
		// HttpSession session = req.getSession();
		// SessionInfo info = (SessionInfo)session.getAttribute("member");
		
		String page = req.getParameter("page");
		String query = "page="+page;
		
		try {
			// int num = Integer.parseInt(req.getParameter("num"));
			String condition = req.getParameter("condition");
			String keyword = req.getParameter("keyword");
			
			if(condition==null) {
				condition = "all";
				keyword = "";
			}
			keyword = URLDecoder.decode(keyword, "UTF-8");
			
			if(keyword.length()!=0) {
				query += "&condition="+condition
						+"&keyword="+URLEncoder.encode(keyword, "utf-8");
			}
			// dao.deletenotice(num, info.getUserId());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		String cp = req.getContextPath();
		resp.sendRedirect(cp+"/notice/list.do?"+query);
	}
	
}

