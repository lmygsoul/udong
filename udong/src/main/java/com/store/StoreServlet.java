package com.store;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import com.member.SessionInfo;
import com.util.FileManager;
import com.util.MyUploadServlet;
import com.util.MyUtil;

@MultipartConfig
@WebServlet("/store/*")
public class StoreServlet extends MyUploadServlet{

	private static final long serialVersionUID = 1L;
	private String pathname;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		process(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		process(req, resp);
	}
	
	protected void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("utf-8");
		
		String uri = req.getRequestURI();
		HttpSession session = req.getSession();

		String root = session.getServletContext().getRealPath("/");
		pathname = root + "uploads" + File.separator + "photo";
		
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
		} else if (uri.indexOf("score.do")!= -1) {
			score(req,resp);
		}
	}
	


	protected void list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		StoreDAO dao = new StoreDAO();
		MyUtil util = new MyUtil();
		String cp = req.getContextPath();
		
		String page = req.getParameter("page");
		String keyword=req.getParameter("keyword");
		int current_page = 1;
		if (page != null) {
			current_page = Integer.parseInt(page);
		}
		
		int dataCount;
		if(keyword==null || keyword.equals("ALL"))
			dataCount=dao.dataCount();
		else
			dataCount=dao.dataCount(keyword);
		
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
			list = dao.listStore(offset, rows);
		} else {
			list = dao.listStore(offset, rows, keyword);
		}
		
		String query = "";
		if(keyword!=null && !keyword.equals("ALL")) {
			query="keyword="+URLEncoder.encode(keyword, "utf-8");
		}
		String listUrl = cp + "/store/list.do";
		String articleUrl = cp + "/store/article.do?page=" + current_page;
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
		
		forward(req,resp, "/WEB-INF/views/store/list.jsp");
		
	}

	protected void created(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setAttribute("mode", "created");
		forward(req, resp, "/WEB-INF/views/store/created.jsp");
	}

	protected void createdSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String cp = req.getContextPath();
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		
		StoreDAO dao = new StoreDAO();
		try {
			StoreDTO dto = new StoreDTO();
			
			dto.setUserId(info.getUserId());
			dto.setSubject(req.getParameter("subject"));
			dto.setContent(req.getParameter("content"));
			dto.setAddr(req.getParameter("address"));
			
			String filename = null;
			Part p = req.getPart("selectFile");
			Map<String, String> map = doFileUpload(p, pathname);
			if (map != null) {
				filename = map.get("saveFilename");
			}
			if(filename != null) {
				dto.setImageFileName(filename);
				dao.insertStore(dto);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		resp.sendRedirect(cp+"/store/list.do");
		
	}

	protected void updateForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		StoreDAO dao = new StoreDAO();
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		String page = req.getParameter("page");
		try {
			int num = Integer.parseInt(req.getParameter("num"));
			StoreDTO dto = dao.readStore(num);
			if(dto!=null && dto.getUserId().equals(info.getUserId())){
				req.setAttribute("dto", dto);
				req.setAttribute("page", page);
				req.setAttribute("mode", "update");
				forward(req, resp, "/WEB-INF/views/store/created.jsp");
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		String cp = req.getContextPath();
		resp.sendRedirect(cp+"/store/list.do?page="+page);
	}

	protected void updateSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		StoreDAO dao = new StoreDAO();
		StoreDTO dto = new StoreDTO();
		String cp = req.getContextPath();
		String page = req.getParameter("page");
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		try {
			dto.setUserId(info.getUserId());
			dto.setSubject(req.getParameter("subject"));
			dto.setContent(req.getParameter("content"));
			dto.setNum(Integer.parseInt(req.getParameter("num")));
			dto.setAddr(req.getParameter("address"));
			
			String imageFileName = req.getParameter("imageFileName");
			Part p = req.getPart("selectFile");
			Map<String, String> map = doFileUpload(p, pathname);
			if(map !=null) {
				String fileName= map.get("saveFilename");
				FileManager.doFiledelete(pathname, imageFileName);
				dto.setImageFileName(fileName);
			} else {
				dto.setImageFileName(imageFileName);
			}
			dao.updateStore(dto);
		} catch (Exception e) {
			e.printStackTrace();
		}
		resp.sendRedirect(cp+"/store/list.do?page="+page);
		
	}

	protected void delete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		StoreDAO dao = new StoreDAO();
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		String cp = req.getContextPath();
		String page = req.getParameter("page");
		try {
			if(info.getUserId().equals(req.getParameter("userId")) || info.getType().equals("0"));{
				int num = Integer.parseInt(req.getParameter("num"));
				dao.deleteStore(num);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		resp.sendRedirect(cp+"/store/list.do?page="+page);
				
	}

	protected void article(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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
			
			StoreDTO preReadDto = dao.preReadStore(num);
			StoreDTO nextReadDto = dao.nextReadStore(num);
			
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
		resp.sendRedirect(cp+"/store/list.do?"+query);
	}
	
	protected void score(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		StoreDAO dao = new StoreDAO();
		StoreDTO dto = new StoreDTO();
		String cp = req.getContextPath();
		String page = req.getParameter("page");
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		
		try {
			dto.setNum(Integer.parseInt(req.getParameter("num")));
			dto.setUserId(info.getUserId());
			dto.setScore(Double.parseDouble(req.getParameter("selectScore")));
			if(dao.RecCheck(dto)) {
				dao.updateScore(dto);
			} 
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		resp.sendRedirect(cp+"/store/article.do?page="+page+"&num="+dto.getNum());
		return;	
	}
}
