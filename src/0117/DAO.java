package com.ej.summary;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

/*
	MVC에서 Model 
	애플리케이션의 정보(데이터)/비즈니스 영역의 로직 처리(DAO, bean)
	Controller가 요청한 DATA를 처리하는 곳이 Model
 */


public class DAO {

	//전역변수=필드=field는  배열처럼 자동초기화됨. 초기값 안써줘도 됨
	//DAO에서 쓸 변수들 미리 전역으로 선언해주기
	
	Connection CN; //DB서버정보기억,명령어생성
  Statement ST, ST2; //select쿼리문 수행할 명령어
  PreparedStatement PST; //insert, update
	ResultSet RS, RS2; //RS=ST.executeQuery("select~")
	
	String x,y,m,z ; //guestList.jsp문서에서 페이징할때 사용 
	String msg, msg2;  //insert,delete,update,select
	int a, d;
	String b,c;
	int total, Gtotal, Gtotal2 ; //데이타갯수
	
	int Gsabun, Gpay ; //사번,급여
	String Gname, Gtitle; //이름,제목
	int RGtotal;
	java.util.Date  Gnalja, dt ; //날짜
	
	int Gnum, num;  //행번호 1시작이 아니라 316 
	
	public DAO() {
		try{ 
	  	Class.forName("oracle.jdbc.driver.OracleDriver"); //db서버드라이브 로드
	  	String url="jdbc:oracle:thin:@127.0.0.1:1521:XE";  //db서버정보
	  	CN=DriverManager.getConnection(url, "system", "oracle");	//db연결 수행
	  }catch(Exception ex){
	  	System.out.println("db연결실패=" + ex);
	  }
	}//생성자 end
	
	public int dbCount(String skey, String sval) {	
		//검색어 받아서 레코드 수 조회하기
		int count = 0;
		String sqry = " where " + skey + " like '%" + sval + "%'";
		if(skey==null || sval==null || skey=="" || sval=="" ) {
			sqry = "";
		}
		
		try {
			msg = "select count(*) as cnt from guest " + sqry;
			ST = CN.createStatement();
			RS = ST.executeQuery(msg);
			if (RS.next()) {
				count = RS.getInt("cnt");
			}
		} catch(Exception ex) { }
		return count;
	}//dbCount end
	
	public ArrayList<Bean> dbSelect(int start, int end, String skey, String sval) {
		//페이징&검색
		ArrayList<Bean> list = new ArrayList<Bean>();
		String sqry = " where " + skey + " like '%" + sval + "%'";
		if(skey==null || sval==null || skey=="" || sval=="" ) {
			sqry =" ";
		}
		try {
			x = "select * from ("
					+ " select rownum rn, g.* from "
					+ " (select * from guest "+ sqry + " order by sabun ) g ) "
					+ " where rn between " + start + " and " + end;
			System.out.println(x);
			ST = CN.createStatement();
			RS = ST.executeQuery(x);
			while(RS.next()) {
				Bean bean = new Bean();
				bean.setRn(RS.getInt("rn"));
				bean.setSabun(RS.getInt("sabun"));
				bean.setName(RS.getString("name"));
				bean.setTitle(RS.getString("title"));
				bean.setNalja(RS.getDate("nalja"));
				bean.setPay(RS.getInt("pay"));
				list.add(bean);
			}
		} catch(Exception ex) { System.out.println("조회오류" + ex);}
		return list;
	}//dbSelect end
}
