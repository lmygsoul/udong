package com.store;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
		String cp = req.getContextPath();
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
		} 
	}
	
	protected void list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		StoreDAO dao = new StoreDAO();
		MyUtil util = new MyUtil();
		String cp = req.getContextPath();
		
		String page = req.getParameter("page");
		int current_page = 1;
		if (page != null) {
			current_page = Integer.parseInt(page);
		}
		int dataCount = dao.dataCount();
		
		int rows = 6;
		int total_page = util.pageCount(rows, dataCount);
		if (current_page>total_page) {
			current_page = total_page;
		}
		
		int offset = (current_page - 1) * rows;
		if (offset < 0)
			offset = 0;
		
		List<StoreDTO> list = null;
		list = dao.listStore(offset, rows);
		
		int listNum, n = 0;
		for (StoreDTO dto : list) {
			listNum = dataCount - (offset + n);
			dto.setNum(listNum);
			n++;
		}
		
		String query = "";
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
		req.setAttribute("mode", "created");
		
		forward(req,resp, "/WEB-INF/views/store/list.jsp");
		
	}

	protected void created(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	
	}

	protected void createdSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	
	}

	protected void updateForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	
	}

	protected void updateSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	
	}

	protected void delete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	
	}

	protected void article(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	
	}
}
