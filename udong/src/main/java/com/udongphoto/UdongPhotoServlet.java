package com.udongphoto;

import java.io.File;
import java.io.IOException;
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
@WebServlet("/udongphoto/*")
public class UdongPhotoServlet extends MyUploadServlet {
	private static final long serialVersionUID = 1L;
	
	private String pathname;

	@Override
	protected void process(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		req.setCharacterEncoding("utf-8");
		String uri=req.getRequestURI();
		
		String cp=req.getContextPath();
		
		HttpSession session=req.getSession();
		SessionInfo info=(SessionInfo)session.getAttribute("member");
		if(info == null) { // 로그인되지 않은 경우
			resp.sendRedirect(cp+"/member/login.do");
			return;
		}
		
		// 이미지를 저장할 경로(pathname)
		String root=session.getServletContext().getRealPath("/");
		pathname=root+"uploads"+File.separator+"photo";
		
		// uri에 따른 작업 구분
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

	private void list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 게시물 리스트
		String cp = req.getContextPath();
		UdongPhotoDAO dao = new UdongPhotoDAO();
		MyUtil util = new MyUtil();
		
		String page = req.getParameter("page");
		int current_page = 1;
		if(page != null) {
			current_page=Integer.parseInt(page);
		}
		
		// 전체데이터 개수
		int dataCount = dao.dataCount();

		// 전체페이지수
		int rows = 6;
		int total_page = util.pageCount(rows, dataCount);
		if(current_page > total_page) {
			current_page = total_page;
		}
		
		// 게시물 가져올 시작과 끝위치
		int offset = (current_page-1) * rows;
		if(offset < 0) offset = 0;
			
		// 게시물 가져오기
		List<UdongPhotoDTO> list = dao.listPhoto(offset, rows);
		
		// 페이징 처리
		String listUrl = cp + "/udongphoto/list.do";
		String articleUrl = cp + "/udongphoto/article.do?page="+current_page;
		String paging=util.paging(current_page, total_page, listUrl);
		
		// 포워딩할 list.jsp에 넘길 값
		req.setAttribute("list", list);
		req.setAttribute("dataCount", dataCount);
		req.setAttribute("articleUrl", articleUrl);
		req.setAttribute("page", current_page);
		req.setAttribute("total_page", total_page);
		req.setAttribute("paging", paging);
		
		forward(req, resp, "/WEB-INF/views/udongphoto/list.jsp");
	}
	
	private void createdForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 글쓰기 폼
		req.setAttribute("mode", "created");
		forward(req, resp, "/WEB-INF/views/udongphoto/created.jsp");
	}
	
	private void createdSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 게시물 저장
		String cp=req.getContextPath();
		HttpSession session=req.getSession();
		SessionInfo info=(SessionInfo)session.getAttribute("member");
		
		UdongPhotoDAO dao=new UdongPhotoDAO();
		
		try {
			UdongPhotoDTO dto=new UdongPhotoDTO();
			
			dto.setUserId(info.getUserId());
			dto.setSubject(req.getParameter("subject"));
			dto.setContent(req.getParameter("content"));
			
			String filename=null;
			Part p = req.getPart("selectFile");
			Map<String, String> map = doFileUpload(p, pathname);
			if(map != null) {
				filename = map.get("saveFilename");
			}
			
			if(filename!=null) {
				dto.setImageFilename(filename);
				dao.insertPhoto(dto);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
			
		resp.sendRedirect(cp+"/udongphoto/list.do");
	}

	private void article(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 게시물 보기
		String cp=req.getContextPath();
		
		UdongPhotoDAO dao=new UdongPhotoDAO();
		String page=req.getParameter("page");
		
		try {
			int num=Integer.parseInt(req.getParameter("num"));
			
			UdongPhotoDTO dto=dao.readPhoto(num);
			if(dto==null) {
				resp.sendRedirect(cp+"/udongphoto/list.do?page="+page);
				return;
			}
			
			dto.setContent(dto.getContent().replaceAll("\n", "<br>"));
			
			req.setAttribute("dto", dto);
			req.setAttribute("page", page);
			
			forward(req, resp, "/WEB-INF/views/udongphoto/article.jsp");
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		resp.sendRedirect(cp+"/udongphoto/list.do?page="+page);
	}
	
	private void updateForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 수정 폼
		String cp=req.getContextPath();
		HttpSession session=req.getSession();
		SessionInfo info=(SessionInfo)session.getAttribute("member");
		UdongPhotoDAO dao=new UdongPhotoDAO();
	
		String page=req.getParameter("page");
		
		try {
			int num=Integer.parseInt(req.getParameter("num"));
			UdongPhotoDTO dto=dao.readPhoto(num);
			
			if(dto==null) {
				resp.sendRedirect(cp+"/udongphoto/list.do?page="+page);
				return;
			}
			
			// 게시물을 올린 사용자가 아니면
			if(! dto.getUserId().equals(info.getUserId())) {
				resp.sendRedirect(cp+"/udongphoto/list.do?page="+page);
				return;
			}
			
			req.setAttribute("dto", dto);
			req.setAttribute("page", page);
			
			req.setAttribute("mode", "update");

			forward(req, resp, "/WEB-INF/views/udongphoto/created.jsp");
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		resp.sendRedirect(cp+"/udongphoto/list.do?page="+page);
	}

	private void updateSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 수정 완료
		String cp=req.getContextPath();
		UdongPhotoDAO dao=new UdongPhotoDAO();
		UdongPhotoDTO dto=new UdongPhotoDTO();
		
		if(req.getMethod().equalsIgnoreCase("GET")) {
			resp.sendRedirect(cp+"/udongphoto/list.do");
			return;
		}
		
		String page=req.getParameter("page");
		
		try {
			dto.setNum(Integer.parseInt(req.getParameter("num")));
			dto.setSubject(req.getParameter("subject"));
			dto.setContent(req.getParameter("content"));
			
			String imageFilename=req.getParameter("imageFilename");
			dto.setImageFilename(imageFilename);
			
			Part p = req.getPart("selectFile");
			Map<String, String> map = doFileUpload(p, pathname);
			if(map != null) { // 이미지 파일을 업로드 한경우
				String filename = map.get("saveFilename");
				// 기존 이미지 파일 지우기
				FileManager.doFiledelete(pathname, imageFilename);
				dto.setImageFilename(filename);
			}
			
			dao.updatePhoto(dto);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		resp.sendRedirect(cp+"/udongphoto/list.do?page="+page);	
	}

	private void delete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 삭제 완료
		String cp=req.getContextPath();
		HttpSession session=req.getSession();
		SessionInfo info=(SessionInfo)session.getAttribute("member");
		
		String page=req.getParameter("page");
		UdongPhotoDAO dao=new UdongPhotoDAO();
		
		try {
			int num=Integer.parseInt(req.getParameter("num"));
			
			UdongPhotoDTO dto=dao.readPhoto(num);
			if(dto==null) {
				resp.sendRedirect(cp+"/udongphoto/list.do?page="+page);	
				return;
			}
			
			// 게시물을 올린 사용자나 admin이 아니면
			if(! dto.getUserId().equals(info.getUserId()) && ! info.getUserId().equals("admin")) {
				resp.sendRedirect(cp+"/udongphoto/list.do?page="+page);
				return;
			}
			
			// 이미지 파일 지우기
			FileManager.doFiledelete(pathname, dto.getImageFilename());
			
			// 테이블 데이터 삭제
			dao.deletePhoto(num);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		resp.sendRedirect(cp+"/udongphoto/list.do?page="+page);			
	}
}
