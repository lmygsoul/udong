package com.used;
  
import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
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
		} else if(uri.indexOf("like.do")!=-1) {
			updateLike(req, resp); 
		}
	}
	
	//게시물 리스트
	private void list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		UsedDAO dao = new UsedDAO();
		MyUtil util = new MyUtil();
		// 게시물 리스트
		String cp = req.getContextPath();
		
		//파라미터로 넘어온 페이지 번호
		String page = req.getParameter("page");
		int current_page = 1;
		if(page !=null) {
			current_page = Integer.parseInt(page);
		}
		
		// 검색
		String condition=req.getParameter("condition");
		String keyword=req.getParameter("keyword");
		if(condition==null) {
			condition="all";
			keyword="";
		}
		
		// GET 방식인 경우 디코딩
		if(req.getMethod().equalsIgnoreCase("GET")) {
			keyword=URLDecoder.decode(keyword, "utf-8");
		}
		
		//전체 데이터 개수
		int dataCount;
		if(keyword.length()==0) {
		dataCount = dao.dataCount();
		} else {
		dataCount = dao.dataCount(condition, keyword);
		}
		
		// 전체 페이지 수
		int rows=10;
		int total_page=util.pageCount(rows, dataCount);
		if(current_page>total_page)
			current_page=total_page;
		
		int offset = (current_page-1) * rows;
		if(offset < 0) offset = 0;
		
		// 게시물 가져오기
		List<UsedDTO> list=null;
		if(keyword.length()==0) {
			list=dao.listUsed(offset, rows);
		} else {
			list=dao.listUsed(offset, rows, condition, keyword);
		}
		
		// 리스트 글번호 만들기
		int listNum, n=0;
		for(UsedDTO dto:list) {
			listNum=dataCount-(offset+n);
			dto.setListNum(listNum);
			n++;
		}
		
		String query="";
		if(keyword.length()!=0) {
			query="condition="+condition+ "&keyword="+URLEncoder.encode(keyword, "utf-8");
		}
		
		// 페이징 처리
		String listUrl=cp+"/used/list.do";
		String articleUrl=cp+"/used/article.do?page="+current_page;
		if(query.length()!=0) {
			listUrl+="?"+query;
			articleUrl+="&"+query;
		}
		
		String paging=util.paging(current_page, total_page, listUrl);
		
		// 포워딩할 JSP로 넘길 속성
		req.setAttribute("list", list);
		req.setAttribute("page", current_page);
		req.setAttribute("total_page", total_page);
		req.setAttribute("dataCount", dataCount);
		req.setAttribute("articleUrl", articleUrl);
		req.setAttribute("paging", paging);
		req.setAttribute("condition", condition);
		req.setAttribute("keyword", keyword);
		
		// JSP로 포워딩
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
			dto.setNickName(req.getParameter("nickName"));
			dto.setSubject(req.getParameter("subject"));
			dto.setContent(req.getParameter("content"));
			dto.setCategory(req.getParameter("category"));
			dto.setPrice(req.getParameter("price"));
			dto.setArea(req.getParameter("area"));
			
			
			String filename = null;
			Part p = req.getPart("selectFile"); //selectFile(이미지 넣은 input의 name)
			Map<String, String> map = doFileUpload(p, pathname);
			if(map !=null) {
				filename = map.get("saveFilename");
			}
			
			if(filename !=null) {
				dto.setImageFilename(filename);
				dao.insertUsed(dto);
			}
									

		} catch (Exception e) {
			e.printStackTrace();
		}
			
		resp.sendRedirect(cp+"/used/list.do");
	}
	
	//게시물 보기
	private void article(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
					
		UsedDAO dao=new UsedDAO();
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
			
						
			//게시글 가져오기 
			UsedDTO dto = dao.readUsed(num);
			if(dto == null) { //게시글이 없으면
				resp.sendRedirect(cp+"/used/list.do?"+query); //리스트로
				return;
			}
			dto.setContent(dto.getContent().replace("\n", "<br>"));	
			
			UsedDTO preReadUsed = dao.preReadUsed(num, condition, keyword);
			UsedDTO nextReadUsed =dao.nextReadUsed(num, condition, keyword);
									
			req.setAttribute("dto", dto);
			req.setAttribute("preReadUsed", preReadUsed);
			req.setAttribute("nextReadUsed", nextReadUsed);
			req.setAttribute("page", page);
			req.setAttribute("query", query);
									
			forward(req, resp, "/WEB-INF/views/used/article.jsp");
			return;			
			} catch (Exception e) {
				e.printStackTrace();
			}
								
			resp.sendRedirect(cp+"/used/list.do?"+query);	
		
	}						
	//게시물 수정폼
	private void updateForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	
		String cp=req.getContextPath();
		UsedDAO dao=new UsedDAO();
		
		HttpSession session=req.getSession();
		SessionInfo info=(SessionInfo)session.getAttribute("member");
	
		String page=req.getParameter("page");
		
		try {
			int num=Integer.parseInt(req.getParameter("num"));
			UsedDTO dto=dao.readUsed(num);
			
			if(dto == null) {
				resp.sendRedirect(cp+"/used/list.do?page="+page);
				return;
			}
			
			// 게시물을 올린 사용자가 아니면
			if(! dto.getUserId().equals(info.getUserId())) {
				resp.sendRedirect(cp+"/used/list.do?page="+page);
				return;
			}
			
			req.setAttribute("dto", dto);
			req.setAttribute("page", page);
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
		
		
		HttpSession session=req.getSession();
		SessionInfo info=(SessionInfo)session.getAttribute("member");
		String page = req.getParameter("page");
				
		try {
			if(req.getMethod().equalsIgnoreCase("GET")) {
				resp.sendRedirect(cp+"/used/list.do?page="+page);
				return;
			}
			
			UsedDTO dto=new UsedDTO();
			
			dto.setNum(Integer.parseInt(req.getParameter("num")));
			dto.setSubject(req.getParameter("subject"));
			dto.setContent(req.getParameter("content"));
			dto.setPrice(req.getParameter("price"));
			dto.setArea(req.getParameter("area"));
			dto.setCategory(req.getParameter("category"));
			dto.setLikeCount(Integer.parseInt(req.getParameter("likeCount")));

			String imageFilename=req.getParameter("imageFilename");
			
			Part p = req.getPart("selectFile");
			Map<String, String> map = doFileUpload(p, pathname);
			if(map != null) { // 이미지 파일을 업로드 한경우
				
				// 기존 이미지 파일 지우기
				FileManager.doFiledelete(pathname, imageFilename);
				
				//새로운 이미지 파일
				String filename = map.get("saveFilename");
				
				dto.setImageFilename(filename);
			}else {
				//새로 업로드한 이미지가 없으면 기존 이미지파일로
				dto.setImageFilename(imageFilename);
			}
			
			dto.setUserId(info.getUserId());
			
			dao.updateUsed(dto);
			
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
			
			// 게시물을 올린 사용자나 admin이 아니면
			if(! dto.getUserId().equals(info.getUserId())&& ! info.getUserId().equals("admin")) {
				resp.sendRedirect(cp+"/used/list.do?page="+page);
				return;
			}
			
			// 이미지 파일 지우기
			FileManager.doFiledelete(pathname, dto.getImageFilename());
			
			// 테이블 데이터 삭제
			dao.deleteUsed(num, info.getUserId());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		resp.sendRedirect(cp+"/used/list.do?page="+page);			
	}
	
	
	//관심 추가하기
	private void updateLike(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		UsedDAO dao=new UsedDAO();
		
		String cp = req.getContextPath();
		String page = req.getParameter("page");
		String query = "page="+page;
	
		
		HttpSession session=req.getSession();
		SessionInfo info=(SessionInfo)session.getAttribute("member");
		
		try {
			int num = Integer.parseInt(req.getParameter("num"));
			
			dao.insertLike(info.getUserId(), num);
			
			dao.updateLikeCount(num);
			
			
			
			resp.sendRedirect(cp+"/used/article.do?num="+num+"&"+query);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//resp.sendRedirect(cp+"/used/article.do?"+query);
	
	}
	
}
