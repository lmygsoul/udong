package com.used;

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


import com.member.SessionInfo;
import com.util.FileManager;
import com.util.MyUploadServlet;
import com.util.MyUtil;

@MultipartConfig
@WebServlet("/used/*")
public class UsedServlet extends MyUploadServlet {
	private static final long serialVersionUID = 1L;

	private String pathname;
	
	@Override
	protected void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("utf-8");
		String uri = req.getRequestURI();		
		String cp=req.getContextPath();
			
		HttpSession session=req.getSession();
		SessionInfo info=(SessionInfo)session.getAttribute("member");
		if(info == null) {
			resp.sendRedirect(cp+"/member/login.do");
			return;			
		}
		
		//이미지를 저장할 경로(pathname)
		String root=session.getServletContext().getRealPath("/");
		pathname=root+"uploads"+File.separator+"photo";
				//root아래 uploads를 만들고 그 아래 photo를 만듦
		//uri에 따른 작업 구분
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
	
	//게시물 리스트
	private void list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String cp = req.getContextPath();
		UsedDAO dao = new UsedDAO();
		MyUtil util = new MyUtil();
		HttpSession session=req.getSession();
		SessionInfo info=(SessionInfo)session.getAttribute("member");
		
		String page = req.getParameter("page");
		int current_page = 1;
		if(page != null) {
			current_page=Integer.parseInt(page);
		}
		
		// 전체데이터 개수
		int dataCount = dao.dataCount(info.getUserId());

		// 전체페이지수
		int rows = 9;
		int total_page = util.pageCount(rows, dataCount);
		if(current_page > total_page) {
			current_page = total_page;
		}
		
		// 게시물 가져올 시작과 끝위치
		int offset = (current_page-1) * rows;
		if(offset < 0) offset = 0;
			
		// 게시물 가져오기
		List<UsedDTO> list = dao.listUsed(offset, rows, info.getUserId());
		
		// 페이징 처리
		String listUrl = cp + "/used/list.do";
		String articleUrl = cp + "/used/article.do?page="+current_page;
		String paging=util.paging(current_page, total_page, listUrl);
		
		// 포워딩할 list.jsp에 넘길 값
		req.setAttribute("list", list);
		req.setAttribute("dataCount", dataCount);
		req.setAttribute("articleUrl", articleUrl);
		req.setAttribute("page", current_page);
		req.setAttribute("total_page", total_page);
		req.setAttribute("paging", paging);
		
		forward(req, resp, "/WEB-INF/views/used/list.jsp");
	}
	
	//글쓰기 폼
	private void createdForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setAttribute("mode", "created");
		forward(req, resp, "/WEB-INF/views/used/created.jsp");
	}
	
	//게시물 저장
	private void createdSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String cp=req.getContextPath();
		HttpSession session=req.getSession();
		SessionInfo info=(SessionInfo)session.getAttribute("member");
		
		UsedDAO dao=new UsedDAO();
		
		try {
			UsedDTO dto=new UsedDTO();
			
			dto.setUserId(info.getUserId());
			dto.setSubject(req.getParameter("subject"));
			dto.setContent(req.getParameter("content"));
			dto.setPrice(req.getParameter("price"));
			dto.setArea(req.getParameter("area"));
			dto.setCategory(req.getParameter("category"));
						
			Map<String, String[]> map = doFileUpload(req.getParts(), pathname);
			if(map != null) {
				String []saveFiles = map.get("saveFilenames");
				dto.setImageFiles(saveFiles);
			}
			
			dao.insertUsed(dto);

		} catch (Exception e) {
			e.printStackTrace();
		}
			
		resp.sendRedirect(cp+"/used/list.do");
	}
	
	//게시물 보기
	private void article(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String cp=req.getContextPath();
		HttpSession session=req.getSession();
		SessionInfo info=(SessionInfo)session.getAttribute("member");
		
		UsedDAO dao=new UsedDAO();
		String page=req.getParameter("page");
		
		try {
			int num=Integer.parseInt(req.getParameter("num"));
			
			UsedDTO dto=dao.readUsed(num);
			if(dto==null || ! dto.getUserId().equals(info.getUserId())) {
				resp.sendRedirect(cp+"/used/list.do?page="+page);
				return;
			}
			
			dto.setContent(dto.getContent().replaceAll("\n", "<br>"));
			
			UsedDTO preReadDto = dao.preReadUsed(num, info.getUserId());
			UsedDTO nextReadDto = dao.nextReadUsed(num, info.getUserId());
			
			List<UsedDTO> listFile = dao.listPhotoFile(num);
			
			req.setAttribute("dto", dto);
			req.setAttribute("preReadDto", preReadDto);
			req.setAttribute("nextReadDto", nextReadDto);
			req.setAttribute("listFile", listFile);
			req.setAttribute("page", page);
			
			forward(req, resp, "/WEB-INF/views/used/article.jsp");
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		resp.sendRedirect(cp+"/used/list.do?page="+page);
	}
	
	//게시물 수정폼
	private void updateForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	
		String cp=req.getContextPath();
		HttpSession session=req.getSession();
		SessionInfo info=(SessionInfo)session.getAttribute("member");
		
		UsedDAO dao=new UsedDAO();
	
		String page=req.getParameter("page");
		
		try {
			int num=Integer.parseInt(req.getParameter("num"));
			UsedDTO dto=dao.readUsed(num);
			
			if(dto==null) {
				resp.sendRedirect(cp+"/used/list.do?page="+page);
				return;
			}
			
			// 게시물을 올린 사용자가 아니면
			if(! dto.getUserId().equals(info.getUserId())) {
				resp.sendRedirect(cp+"/used/list.do?page="+page);
				return;
			}
			
			List<UsedDTO> listFile = dao.listPhotoFile(num);
			
			req.setAttribute("dto", dto);
			req.setAttribute("page", page);
			req.setAttribute("listFile", listFile);
			
			req.setAttribute("mode", "update");

			forward(req, resp, "/WEB-INF/views/used/created.jsp");
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		resp.sendRedirect(cp+"/used/list.do?page="+page);
	}
	
	//게시물 수정완료
	private void updateSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String cp=req.getContextPath();
		UsedDAO dao=new UsedDAO();
		UsedDTO dto=new UsedDTO();
		
		if(req.getMethod().equalsIgnoreCase("GET")) {
			resp.sendRedirect(cp+"/used/list.do");
			return;
		}
		
		String page=req.getParameter("page");
		
		try {
			dto.setNum(Integer.parseInt(req.getParameter("num")));
			dto.setSubject(req.getParameter("subject"));
			dto.setContent(req.getParameter("content"));
			
			Map<String, String[]> map = doFileUpload(req.getParts(), pathname);		
			if(map != null) {
				String []saveFiles = map.get("saveFilenames");
				dto.setImageFiles(saveFiles);
			}
			
			dao.updateUsed(dto);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		resp.sendRedirect(cp+"/used/list.do?page="+page);	
	}
	
	// 수정에서 파일만 삭제
	private void deleteFile(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		HttpSession session=req.getSession();
		SessionInfo info=(SessionInfo)session.getAttribute("member");
		
		UsedDAO dao=new UsedDAO();
		String cp=req.getContextPath();
	
		String page=req.getParameter("page");
		
		try {
			int num=Integer.parseInt(req.getParameter("num"));
			int fileNum=Integer.parseInt(req.getParameter("fileNum"));
			UsedDTO dto=dao.readUsed(num);
			
			if(dto==null) {
				resp.sendRedirect(cp+"/used/list.do?page="+page);
				return;
			}
			
			if(! info.getUserId().equals(dto.getUserId())) {
				resp.sendRedirect(cp+"/used/list.do?page="+page);
				return;
			}
			List<UsedDTO> listFile=dao.listPhotoFile(num);
			
			for(UsedDTO vo : listFile) {
				if(vo.getFileNum()==fileNum) {
					// 파일삭제
					FileManager.doFiledelete(pathname, vo.getImageFilename());
					dao.deletePhotoFile("one", fileNum);
					listFile.remove(vo);
					break;
				}
			}
			
			req.setAttribute("dto", dto);
			req.setAttribute("listFile", listFile);
			req.setAttribute("page", page);
			
			req.setAttribute("mode", "update");

			forward(req, resp, "/WEB-INF/views/used/created.jsp");
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		resp.sendRedirect(cp+"/used/list.do?page="+page);
	}
	
	//게시물 삭제
	private void delete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String cp=req.getContextPath();
		HttpSession session=req.getSession();
		SessionInfo info=(SessionInfo)session.getAttribute("member");
		
		String page=req.getParameter("page");
		UsedDAO dao=new UsedDAO();
		
		try {
			int num=Integer.parseInt(req.getParameter("num"));
			
			UsedDTO dto=dao.readUsed(num);
			if(dto==null) {
				resp.sendRedirect(cp+"/used/list.do?page="+page);	
				return;
			}
			
			// 게시물을 올린 사용자가 아니면
			if(! dto.getUserId().equals(info.getUserId())) {
				resp.sendRedirect(cp+"/used/list.do?page="+page);
				return;
			}
			
			// 이미지 파일 지우기
			List<UsedDTO> listFile = dao.listPhotoFile(num);
			for(UsedDTO vo : listFile) {
				FileManager.doFiledelete(pathname, vo.getImageFilename());
			}
			dao.deletePhotoFile("all", num);
			
			// 테이블 데이터 삭제
			dao.deleteUsed(num);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		resp.sendRedirect(cp+"/used/list.do?page="+page);			
	}
}
