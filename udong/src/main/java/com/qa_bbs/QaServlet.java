package com.qa_bbs;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.Connection;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.member.SessionInfo;
import com.util.DBConn;
import com.util.MyUtil;

@WebServlet("/qa/*")
public class QaServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	Connection conn = DBConn.getConnection();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		process(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		process(req, resp);
	}
	
	protected void forward(HttpServletRequest req, HttpServletResponse resp, String path) throws ServletException, IOException {
		RequestDispatcher rd = req.getRequestDispatcher(path);
		rd.forward(req, resp);
	}
	
	protected void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("utf-8");
		
		String uri = req.getRequestURI();
		
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		if (info==null) {
			String cp = req.getContextPath();
			resp.sendRedirect(cp+"/member/login.do");
			return;
		}
		
		if (uri.indexOf("list.do") != -1) {
			list(req, resp);
		} else if (uri.indexOf("created.do") != -1) {
			created(req, resp);
		} else if (uri.indexOf("created_ok.do") != -1) {
			createdSubmit(req, resp);
		} else if (uri.indexOf("update.do") != -1) {
			updateForm(req, resp);
		} else if (uri.indexOf("update_ok.do") != -1) {
			updateSubmit(req, resp);
		} else if (uri.indexOf("delete.do") != -1) {
			delete(req, resp);
		} else if (uri.indexOf("article.do") != -1) {
			article(req, resp);
		} else if(uri.indexOf("reply.do") != -1) {
			replyForm(req, resp);
		} else if(uri.indexOf("reply_ok.do") != -1) {
			replySubmit(req, resp);
		}
	}
	
	protected void list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		QaDAO dao = new QaDAO();
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
			dataCount = dao.dataCount();
		} else {
			dataCount = dao.dataCount(condition, keyword);
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
			list = dao.listBoard(offset, rows);
		else
			list = dao.listBoard(offset, rows, condition, keyword);
		
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
		
		String listUrl = cp + "/qa/list.do";
		String articleUrl = cp + "/qa/article.do?page=" + current_page;
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
		
		forward(req, resp, "/WEB-INF/views/qa/list.jsp");
	}

	protected void created(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setAttribute("mode", "created");
		forward(req, resp, "/WEB-INF/views/qa/created.jsp");
	}

	protected void createdSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String cp=req.getContextPath();
		if(req.getMethod().equalsIgnoreCase("GET")) {
			resp.sendRedirect(cp+"/board/list.do");
			return;
		}
		
		HttpSession session=req.getSession();
		SessionInfo info=(SessionInfo)session.getAttribute("member");
		
		QaDAO dao=new QaDAO();
		
		try {
			QaDTO dto=new QaDTO();
			dto.setSubject(req.getParameter("subject"));
			dto.setContent(req.getParameter("content"));
			dto.setUserId(info.getUserId());
			
			dao.insertBoard(dto, "created");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		resp.sendRedirect(cp+"/qa/list.do");
	}

	protected void updateForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 수정 폼
		HttpSession session=req.getSession();
		SessionInfo info=(SessionInfo)session.getAttribute("member");
		
		String cp=req.getContextPath();
		String page=req.getParameter("page");
		String query="page="+page;
		
		QaDAO dao=new QaDAO();
		
		try {
			String condition = req.getParameter("condition");
			String keyword = req.getParameter("keyword");
			if(condition==null) {
				condition="subject";
				keyword="";
			}
			keyword = URLDecoder.decode(keyword, "utf-8");
			if(keyword.length()!=0) {
				query+="&condition="+condition+
					     "&keyword="+URLEncoder.encode(keyword, "utf-8");
			}
			
			int boardNum=Integer.parseInt(req.getParameter("boardNum"));
			QaDTO dto=dao.readBoard(boardNum);
			
			if(dto==null) {
				resp.sendRedirect(cp+"/qa/list.do?"+query);
				return;
			}
			
			// 게시물을 올린 사용자가 아니면 리스트로 리다이렉트
			if(! dto.getUserId().equals(info.getUserId())) {
				resp.sendRedirect(cp+"/qa/list.do?"+query);
				return;
			}
			
			req.setAttribute("dto", dto);
			req.setAttribute("page", page);
			req.setAttribute("condition", condition);
			req.setAttribute("keyword", keyword);
			req.setAttribute("mode", "update");
			
			forward(req, resp, "/WEB-INF/views/qa/created.jsp");
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		resp.sendRedirect(cp+"/qa/list.do?"+query);
	}

	protected void updateSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 수정 완료
		HttpSession session=req.getSession();
		SessionInfo info=(SessionInfo)session.getAttribute("member");
		
		String cp=req.getContextPath();
		if(req.getMethod().equalsIgnoreCase("GET")) {
			resp.sendRedirect(cp+"/board/list.do");
			return;
		}
		
		QaDAO dao=new QaDAO();
		
		String page=req.getParameter("page");
		String query="page="+page;
		
		try {
			String condition = req.getParameter("condition");
			String keyword = req.getParameter("keyword");
			if(keyword.length()!=0) {
				query+="&condition="+condition+
					     "&keyword="+URLEncoder.encode(keyword, "utf-8");
			}
			
			QaDTO dto=new QaDTO();
			dto.setBoardNum(Integer.parseInt(req.getParameter("boardNum")));
			dto.setSubject(req.getParameter("subject"));
			dto.setContent(req.getParameter("content"));
			
			dao.updateBoard(dto, info.getUserId());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		resp.sendRedirect(cp+"/qa/list.do?"+query);
	}

	protected void delete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 삭제
		HttpSession session=req.getSession();
		SessionInfo info=(SessionInfo)session.getAttribute("member");

		String cp=req.getContextPath();
		QaDAO dao=new QaDAO();
		
		String page=req.getParameter("page");
		String query="page="+page;
		
		try {
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
			
			int boardNum=Integer.parseInt(req.getParameter("boardNum"));
			QaDTO dto=dao.readBoard(boardNum);
			
			if(dto==null) {
				resp.sendRedirect(cp+"/qa/list.do?"+query);
				return;
			}
			
			/* 역할처리를 어떻게 할지 ?
			// 게시물을 올린 사용자나 admin이 아니면 리스트로 리다이렉트
			if(! dto.getUserId().equals(info.getUserId()) && ! info.getUserId().equals("admin")) {
				resp.sendRedirect(cp+"/qa/list.do?"+query);
				return;
			}
			*/
			
			// 일단 게시글을 올린 사용자인지만 확인
			if(! dto.getUserId().equals(info.getUserId())) {
				resp.sendRedirect(cp+"/qa/list.do?"+query);
				return;
			}
			
			dao.deleteBoard(boardNum);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		resp.sendRedirect(cp+"/qa/list.do?"+query);
	}

	protected void article(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 글보기
		QaDAO dao=new QaDAO();
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
			
			dao.updateHitCount(boardNum);
			QaDTO dto=dao.readBoard(boardNum);
			if(dto==null) {
				resp.sendRedirect(cp+"/qa/list.do?"+query);
				return;
			}
			
			// dto.setContent(dto.getContent().replaceAll("\n", "<br>"));
			MyUtil util=new MyUtil();
			dto.setContent(util.htmlSymbols(dto.getContent()));
			
			/*
			QadDTO preReadDto=dao.preReadBoard(dto.getGroupNum(),
					dto.getOrderNo(), condition, keyword);
			QaDTO nextReadDto=dao.nextReadBoard(dto.getGroupNum(),
					dto.getOrderNo(), condition, keyword);
			*/
		
			req.setAttribute("dto", dto);
			// req.setAttribute("preReadDto", preReadDto);
			// req.setAttribute("nextReadDto", nextReadDto);
			req.setAttribute("query", query);
			req.setAttribute("page", page);
			
			forward(req, resp, "/WEB-INF/views/qa/article.jsp");
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		resp.sendRedirect(cp+"/qa/list.do?"+query);
	}
	
	protected void replyForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 답변 폼
		QaDAO dao=new QaDAO();
		String cp=req.getContextPath();
		String page = req.getParameter("page");
		
		try {
			int boardNum = Integer.parseInt(req.getParameter("boardNum"));
			
			QaDTO dto=dao.readBoard(boardNum);
			if(dto==null) {
				resp.sendRedirect(cp+"/qa/list.do?page="+page);
				return;
			}
			
			String s="["+dto.getSubject()+"] 에 대한 답변입니다.\n";
			dto.setContent(s);
			
			req.setAttribute("mode", "reply");
			req.setAttribute("dto", dto);
			req.setAttribute("page", page);
		
			forward(req, resp, "/WEB-INF/views/qa/created.jsp");
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		resp.sendRedirect(cp+"/qa/list.do?page="+page);
	}
	
	protected void replySubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 답변 완료
		String cp=req.getContextPath();
		if(req.getMethod().equalsIgnoreCase("GET")) {
			resp.sendRedirect(cp+"/qa/list.do");
			return;
		}
		
		String page=req.getParameter("page");
		HttpSession session=req.getSession();
		SessionInfo info=(SessionInfo)session.getAttribute("member");
		
		QaDAO dao=new QaDAO();
		
		try {
			QaDTO dto=new QaDTO();
			
			dto.setSubject(req.getParameter("subject"));
			dto.setContent(req.getParameter("content"));
			
			dto.setGroupNum(Integer.parseInt(req.getParameter("groupNum")));
			dto.setOrderNo(Integer.parseInt(req.getParameter("orderNo")));
			dto.setDepth(Integer.parseInt(req.getParameter("depth")));
			dto.setParent(Integer.parseInt(req.getParameter("parent")));
			
			dto.setUserId(info.getUserId());
			
			dao.insertBoard(dto, "reply");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		resp.sendRedirect(cp+"/qa/list.do?page="+page);		
	}
	
}
