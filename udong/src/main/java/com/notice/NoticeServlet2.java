package com.notice;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
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
@WebServlet("/notice/*")
public class NoticeServlet2 extends MyUploadServlet {
	private static final long serialVersionUID = 1L;

	private String pathname;
	
	@Override
	protected void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("utf-8");

		String cp = req.getContextPath();
		String uri = req.getRequestURI();
		
		HttpSession session = req.getSession();
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		
		if(uri.indexOf("list.do") == -1 && info == null ) {
			resp.sendRedirect(cp+"/member/login.do");
			return;
		}
		
		// 파일을 저장할 경로(pathname)
		String root=session.getServletContext().getRealPath("/");
		pathname = root+"uploads"+File.separator+"notice";
		
		// uri에 따른 작업 구분
		if(uri.indexOf("list.do")!=-1) {
			list(req, resp);
		} else if(uri.indexOf("created.do")!=-1) {
			createdForm(req, resp);
		}  else if(uri.indexOf("created_ok.do")!=-1) {
			createdSubmit(req, resp);
		}  else if(uri.indexOf("article.do")!=-1) {
			article(req, resp);
		} else if(uri.indexOf("update.do")!=-1) {
			updateForm(req, resp);
		} else if(uri.indexOf("update_ok.do")!=-1) {
			updateSubmit(req, resp);
		} else if(uri.indexOf("deleteFile.do")!=-1) {
			deleteFile(req, resp);
		} else if(uri.indexOf("delete.do")!=-1) {
			delete(req, resp);
		} else if(uri.indexOf("download.do")!=-1) {
			download(req, resp);
		} else if(uri.indexOf("deleteList.do")!=-1) {
			deleteList(req, resp);
		}
	}

	private void list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 게시물 리스트
		NoticeDAO2 dao = new NoticeDAO2();
		MyUtil util = new MyUtil();		
		String cp = req.getContextPath();
		
		String page = req.getParameter("page");
		int current_page = 1;
		if(page != null) {
			current_page=Integer.parseInt(page);
		}
		
		String condition=req.getParameter("condition");
		String keyword=req.getParameter("keyword");
		if(condition==null) {
			condition="all";
			keyword="";
		}
		if(req.getMethod().equalsIgnoreCase("GET")) {
			keyword=URLDecoder.decode(keyword,"utf-8");
		}
		
		// 한페이지 표시할 데이터 개수
        String numPerPage=req.getParameter("rows");
        int rows = numPerPage == null ? 10 : Integer.parseInt(numPerPage);
        
		int dataCount, total_page;
		
		if(keyword.length()!=0) {
			dataCount= dao.dataCount(condition, keyword);
		} else {
			dataCount= dao.dataCount();
		}
		total_page=util.pageCount(rows, dataCount);
		
		if(current_page>total_page) {
			current_page=total_page; 
		}
		
		int offset=(current_page-1)*rows;
		if(offset < 0) offset = 0;
		
		List<NoticeDTO2> list;
		if(keyword.length()!=0) {
			list= dao.listNotice(offset, rows, condition, keyword);
		} else {
			list= dao.listNotice(offset, rows);
		}
		
		// 공지글
		List<NoticeDTO2> listNotice=null;
		listNotice = dao.listNotice();
		for(NoticeDTO2 dto : listNotice){
			dto.setCreated(dto.getCreated().substring(0, 10));
		}

		long gap;
		Date curDate = new Date();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		// 리스트 글번호 만들기
		int listNum, n=0;
		for(NoticeDTO2 dto : list){
			listNum=dataCount-(offset+n);
			dto.setListNum(listNum);
			
			try {
				Date date=sdf.parse(dto.getCreated());
				
				// gap = (curDate.getTime() - date.getTime()) /(1000*60*60*24); // 일자
				gap = (curDate.getTime() - date.getTime()) /(1000*60*60); // 시간 
				dto.setGap(gap);
			} catch (Exception e) {
			}
			
			dto.setCreated(dto.getCreated().substring(0, 10));
			n++;
		}
		
		String query="";
		String listUrl;
		String articleUrl;
		
		listUrl=cp+"/notice/list.do?rows="+rows;
		articleUrl=cp+"/notice/article.do?page=" +current_page+"&rows="+rows;
		if(keyword.length()!=0) {
			query="condition="+condition+"&keyword="+URLEncoder.encode(keyword,"utf-8");
			
			listUrl += "&"+query;
			articleUrl += "&"+query;
		}
		
		String paging=util.paging(current_page, total_page, listUrl);
		
		// 포워딩 jsp에 넘길 데이터
		req.setAttribute("list", list);
		req.setAttribute("listNotice", listNotice);
		req.setAttribute("articleUrl", articleUrl);
		req.setAttribute("dataCount", dataCount);
		req.setAttribute("page", current_page);
		req.setAttribute("total_page", total_page);
		req.setAttribute("paging", paging);
		req.setAttribute("condition", condition);
		req.setAttribute("keyword", keyword);
		req.setAttribute("rows", rows);
		
		// JSP로 포워딩
		forward(req, resp, "/WEB-INF/views/notice/list.jsp");
	}
	
	private void createdForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 글쓰기 폼
		HttpSession session=req.getSession();
		SessionInfo info=(SessionInfo)session.getAttribute("member");
		String cp=req.getContextPath();
		String rows=req.getParameter("rows");
		
		// admin만 글을 등록
		if(! info.getUserId().equals("admin")) {
			resp.sendRedirect(cp+"/notice/list.do?rows="+rows);
			return;
		}
		
		req.setAttribute("mode", "created");
		req.setAttribute("rows", rows);
		forward(req, resp, "/WEB-INF/views/notice/created.jsp");
	}
	
	private void createdSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 글 저장
		HttpSession session=req.getSession();
		SessionInfo info=(SessionInfo)session.getAttribute("member");
		
		NoticeDAO2 dao=new NoticeDAO2();
		String cp=req.getContextPath();
		
		String rows=req.getParameter("rows");
		// admin만 글을 등록
		if(! info.getUserId().equals("admin")) {
			resp.sendRedirect(cp+"/notice/list.do?rows="+rows);
			return;
		}
		
		try {
		    NoticeDTO2 dto=new NoticeDTO2();
		    dto.setUserId(info.getUserId());
		    if(req.getParameter("notice")!=null) {
		    	dto.setNotice(Integer.parseInt(req.getParameter("notice")));
		    }
		    dto.setSubject(req.getParameter("subject"));
		    dto.setContent(req.getParameter("content"));
		    
			Map<String, String[]> map = doFileUpload(req.getParts(), pathname);		
			if(map != null) {
				String []saveFiles = map.get("saveFilenames");
				String []originalFiles = map.get("originalFilenames");
				dto.setSaveFiles(saveFiles);
				dto.setOriginalFiles(originalFiles);
			}

		    dao.insertNotice(dto);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		resp.sendRedirect(cp+"/notice/list.do?rows="+rows);
	}

	private void article(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 글보기
		NoticeDAO2 dao=new NoticeDAO2();
		String cp=req.getContextPath();
		
		String page=req.getParameter("page");
		String rows=req.getParameter("rows");
		String query="page="+page+"&rows="+rows;
		
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
			
			// 조회수
			dao.updateHitCount(num);
			
			// 게시물 가져오기
			NoticeDTO2 dto=dao.readNotice(num);
			if(dto==null) {
				resp.sendRedirect(cp+"/notice/list.do?"+query);
				return;
			}
			
			dto.setContent(dto.getContent().replaceAll("\n", "<br>"));
			
			// 이전글/다음글
			NoticeDTO2 preReadDto = dao.preReadNotice(dto.getNum(), condition, keyword);
			NoticeDTO2 nextReadDto = dao.nextReadNotice(dto.getNum(), condition, keyword);
			
			// 파일
			List<NoticeDTO2> listFile = dao.listNoticeFile(num);
			
			req.setAttribute("dto", dto);
			req.setAttribute("preReadDto", preReadDto);
			req.setAttribute("nextReadDto", nextReadDto);
			req.setAttribute("listFile", listFile);
			req.setAttribute("query", query);
			req.setAttribute("page", page);
			req.setAttribute("rows", rows);
			
			forward(req, resp, "/WEB-INF/views/notice/article.jsp");
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		resp.sendRedirect(cp+"/notice/list.do?"+query);
	}
	
	private void updateForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 수정 폼
		HttpSession session=req.getSession();
		SessionInfo info=(SessionInfo)session.getAttribute("member");
		
		NoticeDAO2 dao=new NoticeDAO2();
		String cp=req.getContextPath();
		
		String page=req.getParameter("page");
		String rows=req.getParameter("rows");
		
		try {
			int num=Integer.parseInt(req.getParameter("num"));
			
			NoticeDTO2 dto=dao.readNotice(num);
			if(dto==null) {
				resp.sendRedirect(cp+"/notice/list.do?page="+page+"&rows="+rows);
				return;
			}
			
			// 글을 등록한 사람만 수정 가능
			if(! info.getUserId().equals(dto.getUserId())) {
				resp.sendRedirect(cp+"/notice/list.do?page="+page+"&rows="+rows);
				return;
			}
			
			// 파일
			List<NoticeDTO2> listFile = dao.listNoticeFile(num);
			
			req.setAttribute("dto", dto);
			req.setAttribute("listFile", listFile);
			req.setAttribute("page", page);
			req.setAttribute("rows", rows);
			
			req.setAttribute("mode", "update");
			
			forward(req, resp, "/WEB-INF/views/notice/created.jsp");
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		resp.sendRedirect(cp+"/notice/list.do?page="+page+"&rows="+rows);
	}

	private void updateSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 수정 완료
		NoticeDAO2 dao=new NoticeDAO2();
		String cp=req.getContextPath();
		
		NoticeDTO2 dto=new NoticeDTO2();
		
		String page=req.getParameter("page");
		String rows=req.getParameter("rows");
		
		if(req.getMethod().equalsIgnoreCase("GET")) {
			resp.sendRedirect(cp+"/notice/list.do?page="+page+"&rows="+rows);
			return;
		}
		
		try {
			int num=Integer.parseInt(req.getParameter("num"));
			dto.setNum(num);
		    if(req.getParameter("notice")!=null) {
		    	dto.setNotice(Integer.parseInt(req.getParameter("notice")));
		    }
			dto.setSubject(req.getParameter("subject"));
			dto.setContent(req.getParameter("content"));
			
			Map<String, String[]> map = doFileUpload(req.getParts(), pathname);		
			if(map != null) {
				String []saveFiles = map.get("saveFilenames");
				String []originalFiles = map.get("originalFilenames");
				dto.setSaveFiles(saveFiles);
				dto.setOriginalFiles(originalFiles);
			}

			dao.updateNotice(dto);

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		resp.sendRedirect(cp+"/notice/list.do?page="+page+"&rows="+rows);
	}

	private void deleteFile(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 수정에서 파일만 삭제
		HttpSession session=req.getSession();
		SessionInfo info=(SessionInfo)session.getAttribute("member");
		
		NoticeDAO2 dao=new NoticeDAO2();
		String cp=req.getContextPath();
	
		String page=req.getParameter("page");
		String rows=req.getParameter("rows");
		
		try {
			int num=Integer.parseInt(req.getParameter("num"));
			int fileNum=Integer.parseInt(req.getParameter("fileNum"));
			NoticeDTO2 dto=dao.readNotice(num);
			if(dto==null) {
				resp.sendRedirect(cp+"/notice/list.do?page="+page+"&rows="+rows);
				return;
			}
			
			if(! info.getUserId().equals(dto.getUserId())) {
				resp.sendRedirect(cp+"/notice/list.do?page="+page+"&rows="+rows);
				return;
			}
			
			List<NoticeDTO2> listFile=dao.listNoticeFile(num);
			
			for(NoticeDTO2 vo : listFile) {
				if(vo.getFileNum()==fileNum) {
					// 파일삭제
					FileManager.doFiledelete(pathname, vo.getSaveFilename());
					dao.deleteNoticeFile("one", fileNum);
					listFile.remove(vo);
					break;
				}
			}
			
			req.setAttribute("dto", dto);
			req.setAttribute("listFile", listFile);
			req.setAttribute("page", page);
			req.setAttribute("rows", rows);
			
			req.setAttribute("mode", "update");

			forward(req, resp, "/WEB-INF/views/notice/created.jsp");
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		resp.sendRedirect(cp+"/notice/list.do?page="+page+"&rows="+rows);
	}

	private void delete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 삭제
		HttpSession session=req.getSession();
		SessionInfo info=(SessionInfo)session.getAttribute("member");
		
		NoticeDAO2 dao=new NoticeDAO2();
		String cp=req.getContextPath();
		
		String page=req.getParameter("page");
		String rows=req.getParameter("rows");
		String query="page="+page+"&rows="+rows;
		
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

			NoticeDTO2 dto=dao.readNotice(num);
			if(dto==null) {
				resp.sendRedirect(cp+"/notice/list.do?"+query);
				return;
			}
			
			// 글을 등록한 사람, admin 만 삭제 가능
			if(! info.getUserId().equals(dto.getUserId()) && ! info.getUserId().equals("admin")) {
				resp.sendRedirect(cp+"/notice/list.do?"+query);
				return;
			}
			
			// 파일삭제
			List<NoticeDTO2> listFile=dao.listNoticeFile(num);
			for(NoticeDTO2 vo : listFile) {
				FileManager.doFiledelete(pathname, vo.getSaveFilename());
			}
			dao.deleteNoticeFile("all", num);
			
			// 게시글 삭제
			dao.deleteNotice(num);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		resp.sendRedirect(cp+"/notice/list.do?"+query);
	}

	private void download(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 파일 다운로드
		NoticeDAO2 dao=new NoticeDAO2();
		boolean b=false;
		
		try {
			int fileNum=Integer.parseInt(req.getParameter("fileNum"));
			
			NoticeDTO2 dto=dao.readNoticeFile(fileNum);
			if(dto!=null) {
				b = FileManager.doFiledownload(dto.getSaveFilename(), dto.getOriginalFilename(), pathname, resp);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(! b) {
			resp.setContentType("text/html;charset=utf-8");
			PrintWriter out=resp.getWriter();
			out.print("<script>alert('파일다운로드가 실패 했습니다.');history.back();</script>");
		}
	}
	
	private void deleteList(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session=req.getSession();
		SessionInfo info=(SessionInfo)session.getAttribute("member");
		String cp=req.getContextPath();
		
		if(! info.getUserId().equals("admin")) {
			resp.sendRedirect(cp+"/notice/list.do");
			return;
		}
		
		String page=req.getParameter("page");
		String rows=req.getParameter("rows");
		String query="rows="+rows+"&page="+page;
		
		String condition=req.getParameter("condition");
		String keyword=req.getParameter("keyword");
		
		try {
			if(keyword!=null && keyword.length()!=0) {
				query+="&condition="+condition+"&keyword="+
			               URLEncoder.encode(keyword, "UTF-8");
			}
			
			String []nn=req.getParameterValues("nums");
			int nums[]=null;
			nums=new int[nn.length];
			for(int i=0; i<nn.length; i++) {
				nums[i]=Integer.parseInt(nn[i]);
			}
			
			NoticeDAO2 dao=new NoticeDAO2();
			
			// 파일 삭제
			for(int i=0; i<nums.length; i++) {
				List<NoticeDTO2> listFile = dao.listNoticeFile(nums[i]);
				for(NoticeDTO2 vo : listFile) {
					FileManager.doFiledelete(pathname, vo.getSaveFilename());
				}
				dao.deleteNoticeFile("all", nums[i]);
			}
			
			// 게시글 삭제
			dao.deleteNoticeList(nums);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		resp.sendRedirect(cp+"/notice/list.do?"+query);
	}
	
}
