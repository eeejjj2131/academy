package com.ej.summary;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/*
 	MVC에서 Controller
 	View에서 보고자 하는 DATA를 요청하는 곳이 Controller
 	데이터와 비즈니스 로직 사이의 상호동작을 관리
 	사용자의 입력처리와 흐름제어 담당(서블릿)
 	
 	서블릿이 웹 브라우저의 요청을 처리하는 5단계:
		1) 웹 브라우저가 전송한 HTTP 요청을 받는다. 서블릿의 doGet()/doPost()메소드 호출
		2) 클라이언트가 요구하는 기능 분석(어떤 기능 요청했는지 ex.목록/글쓰기)
		3) 모델을 사용하여 요청한 기능 수행(요청한 비즈니스로직을 처리하는 모델 사용)
		4) 모델로부터 전달받은 결과물을 알맞게 가공한 후
		   request나 session의 setAttribute("가짜", 진짜)메서드 사용 ->결과값을 속성에 저장
		   이렇게 저장한 결과값은 뷰인 JSP에서 사용(getAttribute("가짜")나 EL, JSTL사용) 
			(그냥 임포트하거나 가져다가 쓸수가 없음.) request 이용해야함.
		5) 웹브라우저에 결과를 전송할 JSP를 선택후 해당 JSP로 
		   포워딩(dispatcher사용)/리다이렉트
 */


@WebServlet("/list.do")
public class Controller extends HttpServlet {
	public void doUser(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html; charset=utf-8");

		PrintWriter out = response.getWriter();
		System.out.println("<h1>ListController.java자바 서블릿문서</h1>");

		DAO dao = new DAO();
		Bean bean = new Bean();
		
		//지역변수: 사용하기 전에 초기값 넣어줘야함
		
		int startpage = 1, endpage = 10, pagecount = 1; // 페이지 표시 시작~끝, 총 페이지 수 [1]~[10], 총 32페이지
		int start = 1, end = 10; // list 시작~끝
		int pageNUM = 1; // 현재페이지 
		int temp = 0; //startpage 계산할때 쓸 값
		String pnum = "1"; // 넘어온 페이지. getParameter()로 받을것

		String skey = "", sval = "", returnpage = "";	//검색조건, 검색어, 주소에 넣어줄 인자 값

		String a = request.getParameter("keyfield");
		String b = request.getParameter("keyword"); 
		 if(!(a == null || b == null)) {
  		 skey = a;
  		 sval = b;
  	 }
		returnpage = "&keyfield=" + skey + "&keyword=" + sval;

		int Stotal = dao.dbCount(skey, sval);	//검색어 사용해서 조회한 레코드 수
		int Gtotal = dao.dbCount("", "");	//검색x 전체 조회한 레코드 수
		pnum = request.getParameter("pageNum");
		if (pnum == null || pnum == "") {
			pnum = "1";
		}
		pageNUM = Integer.parseInt(pnum);	//넘어온 페이지
		start = (pageNUM - 1) * 10 + 1;	//리스트 시작: ex) 10페이지면 91~100번 레코드 조회
		end = pageNUM * 10;	//리스트 끝
		pagecount = (int) Math.ceil((double) Stotal / (double) 10);	//총 페이지 수(조회한 레코드수/페이지당보여줄리스트수)의 올림
		temp = (pageNUM - 1) % 10;
		startpage = pageNUM - temp;	//페이지 표시 시작할 숫자
		endpage = startpage + 9;	//페이지 표시 끝 숫자
		if (endpage > pagecount) {	//전체 페이지보다 많으면 전체페이지까지만 표시하게 처리
			endpage = pagecount;
		}

		// :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
	//MVC2의 Model 비즈니스 사용(DAO의 메소드 사용)
		ArrayList<Bean> LG = dao.dbSelect(start, end, skey, sval); //리스트의 시작,끝, 검색조건, 검색어 넣어서 db 조회
		
		// view에서 받아서 쓸 속성들을  ""안의 이름으로 보내줌
		request.setAttribute("returnpage", returnpage);
		request.setAttribute("sval", sval);
		request.setAttribute("skey", skey);
		request.setAttribute("pagecount", pagecount);
		request.setAttribute("pageNUM", pageNUM);
		request.setAttribute("endpage", endpage);
		request.setAttribute("startpage", startpage);
		request.setAttribute("endpage", endpage);
		request.setAttribute("Gtotal", Gtotal);
		request.setAttribute("Stotal", Stotal);
		request.setAttribute("naver", LG); // view에서 받아서 쓸 속성들을  ""안의 이름으로 보내줌

		RequestDispatcher dis = request.getRequestDispatcher("guestList.jsp");	//포워딩할 문서
		dis.forward(request, response);	//실제로 포워딩 수행
	}// doUser end

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		//post방식일때
		doUser(request, response);
	}// end

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		//get방식일때
		doUser(request, response);
	}// end

}
