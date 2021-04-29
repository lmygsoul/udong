package com.neighbor;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.Connection;
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
import com.util.DBConn;
import com.util.FileManager;
import com.util.MyUploadServlet;
import com.util.MyUtil;

@WebServlet("/neighbor/*")
@MultipartConfig
public class NeighborServlet extends MyUploadServlet {

	private static final long serialVersionUID = 1L;
	private String pathname;
	Connection conn = DBConn.getConnection();

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
		} else if (uri.indexOf("rec.do")!= -1) {
			rec(req,resp);
		}
	}

	protected void list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		NeighborDAO dao = new NeighborDAO();
		MyUtil util = new MyUtil();
		String cp = req.getContextPath();
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
			dataCount = dao.dataCount();
		else
			dataCount = dao.dataCount(condition, keyword);
		
		int rows = 10;
		int total_page = util.pageCount(rows, dataCount);
		if (current_page > total_page)
			current_page = total_page;

		int offset = (current_page - 1) * rows;
		if (offset < 0)
			offset = 0;

		List<NeighborDTO> list = null;
		if (keyword.length() == 0) {
			list = dao.listBoard(offset, rows);
		} else {
			list = dao.listBoard(offset, rows, condition, keyword);
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

		String listUrl = cp + "/neighbor/list.do";
		String articleUrl = cp + "/neighbor/article.do?page=" + current_page;
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

		forward(req, resp, "/WEB-INF/views/neighbor/list.jsp");

	}

	protected void created(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setAttribute("mode", "created");
		forward(req, resp, "/WEB-INF/views/neighbor/created.jsp");
	}

	protected void createdSubmit(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String cp = req.getContextPath();
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		NeighborDAO dao = new NeighborDAO();
		NeighborDTO dto = new NeighborDTO();
		
		try {
			dto.setUserId(info.getUserId());
			dto.setSubject(req.getParameter("subject"));
			dto.setContent(req.getParameter("content"));
			
			String filename = null;
			Part p = req.getPart("selectFile");
			Map<String, String> map = doFileUpload(p, pathname);
			if(map!=null) {
				filename = map.get("saveFilename");
			}
			if(filename != null) {
				dto.setImageFileName(filename);
			}
			dao.insertNeighbor(dto);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		resp.sendRedirect(cp+"/neighbor/list.do");
	}

	protected void updateForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String cp = req.getContextPath();
		String page = req.getParameter("page");
		int num = Integer.parseInt(req.getParameter("num"));
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		NeighborDAO dao = new NeighborDAO();
		NeighborDTO dto = new NeighborDTO();
		try {
			dto = dao.readNeighbor(num);
			if(dto!=null && dto.getUserId().equals(info.getUserId())) {
				req.setAttribute("dto", dto);
				req.setAttribute("page", page);
				req.setAttribute("mode", "update");
				forward(req, resp, "/WEB-INF/views/neighbor/created.jsp");
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		resp.sendRedirect(cp+"/neghbor/list.do?page="+page);
	}

	protected void updateSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String cp = req.getContextPath();
		String page = req.getParameter("page");
		int num = Integer.parseInt(req.getParameter("num"));
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		NeighborDAO dao = new NeighborDAO();
		NeighborDTO dto = new NeighborDTO();
		try {
			dto.setUserId(info.getUserId());
			dto.setSubject(req.getParameter("subject"));
			dto.setContent(req.getParameter("content"));
			dto.setNum(num);
			
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
			dao.updateNeighbor(dto);
		} catch (Exception e) {
			e.printStackTrace();
		}
		resp.sendRedirect(cp+"/neighbor/list.do?page="+page);
	}

	protected void delete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String cp = req.getContextPath();
		String page = req.getParameter("page");
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		NeighborDAO dao = new NeighborDAO();
		try {
			if(info.getUserId().equals(req.getParameter("userid")) || info.getType().equals("0")){
				int num = Integer.parseInt(req.getParameter("num"));
				dao.deleteNeighbor(num);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		resp.sendRedirect(cp+"/neighbor/list.do?page="+page);
	}

	protected void article(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String cp = req.getContextPath();
		String page = req.getParameter("page");
		String query = "page="+page;
		Boolean isRec = true;
		
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");

		NeighborDAO dao = new NeighborDAO();
		NeighborDTO dto = new NeighborDTO();
		try {
			int num = Integer.parseInt(req.getParameter("num"));
			
			dto = dao.readNeighbor(num);
			
			if(dto == null) {
				resp.sendRedirect(cp+"/neighbor/list.do?"+query);
			}
			dao.updateHitCount(num);
			dto.setContent(dto.getContent().replaceAll("\n", "<br>"));
			
			req.setAttribute("dto", dto);
			req.setAttribute("page", page);
			req.setAttribute("query", query);
			req.setAttribute("num", num);
			if(info != null) {
				NeighborDTO rec = new NeighborDTO();
				rec.setNum(num);
				rec.setUserId(info.getUserId());
				isRec = dao.recCheck(rec);
				req.setAttribute("isRec", isRec);
			}
			list(req, resp);
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}
		resp.sendRedirect(cp+"/neighbor/article.do?page="+page+"&num="+dto.getNum());
		return;
	}
	protected void rec(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		NeighborDAO dao = new NeighborDAO();
		NeighborDTO dto = new NeighborDTO();
		String cp = req.getContextPath();
		String page = req.getParameter("page");
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		
		try {
			dto.setNum(Integer.parseInt(req.getParameter("num")));
			dto.setUserId(info.getUserId());
			dto.setRec(Integer.parseInt(req.getParameter("selectRec")));
			if(dao.recCheck(dto)) {
				dao.updateRec(dto);
			} 
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		resp.sendRedirect(cp+"/neighbor/list.do?page="+page+"&num="+dto.getNum());
		return;	
	}
}
