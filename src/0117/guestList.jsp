<!-- 
	사용자가 근본적으로 접근하고자 곳이 View(JSP)
	 텍스트, 체크박스 항목 등과 같은 사용자 인터페이스 요소
		  비즈니스영역에 대한 프레젠테이션 뷰(사용자가 보게 될 결과 화면)(JSP)
	사용자가 요청 -> 컨트롤러 -> 비즈니스로직처리는 모델에서 -> 뷰 선택 하고 뷰가 응답
	비즈니스로직을 처리하는 모델과 결과화면을 보여주는 뷰를 분리
	       어플리케이션의 흐름제어나 사용자의 처리 요청은 컨트롤러에 집중
 -->

<%@ page language="java" contentType="text/html; charset=UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>	<!-- JSTL 사용 -->



<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>guestList.jsp</title>
<style type="text/css">
* {
	font-size: 14pt;
	font-weight: bold;
	font-family: "나눔스퀘어라운드"
}

a {
	text-decoration: none;
	color: green;
	font-weight: bold;
	font-weight: bold;
}

a:hover {
	color: red;
	font-weight: bolder;
	font-size: larger;
}
</style>

</head>
<body>

	<table width="800" border="1" cellspacing="0">
		<tr align="right" height="40">
			<td colspan="6">
				<a href="index.jsp">[index]</a>&nbsp; 
				<a href="guest.jsp">[입력]</a>&nbsp;
				<a href="list.do">[출력]</a>&nbsp;
				조회결과/전체결과: ${Stotal}/${Gtotal} &nbsp;&nbsp;
			</td>
		</tr>

		<tr bgcolor="pink">
			<td>행번호</td>
			<td>사 번</td>
			<td>이 름</td>
			<td>제 목</td>
			<td>날 짜</td>
			<td>급 여</td>
		</tr>

		<c:forEach var="bean" items="${naver}">		<!-- ${ } EL태그 사용 -->
			<tr>
				<td>${bean.rn}</td>
				<td>${bean.sabun}</td>
				<td>${bean.name}</td>
				<td><a href="detail.do?index=${bean.sabun}"> ${bean.title}</a></td>
				<td>${bean.nalja}</td>
				<td>${bean.pay}</td>
			</tr>
		</c:forEach>

		<tr align="center">	<!-- 페이징 처리 -->
			<td colspan="6">
				<c:forEach var="i" begin="${startpage}" end="${endpage}">		<!-- JSTL과 EL태그 같이 사용 -->
					<c:if test="${i == startpage && startpage != 1}">
						<a href="list.do?pageNum=1${returnpage}">[처음]</a>
						<a href="list.do?pageNum=${startpage - 10 += returnpage}">[이전]</a>
					</c:if>
					<a href="list.do?pageNum=${i += returnpage}">[${i}]</a>
					<c:if test="${i == endpage && endpage != pagecount}">
						<a href="list.do?pageNum=${startpage + 10 += returnpage}">[다음]</a>
						<a href="list.do?pageNum=${pagecount += returnpage}">[끝]</a>
					</c:if>
				</c:forEach></td>
		</tr>
		<tr align="center">	<!-- 검색 처리 -->
			<td colspan="6">
				<form action="list.do">
				<input type="hidden" name="pageNum" value="${pageNUM}">
				<select name="keyfield">
					<option label="전체" value=""/>
					<option label="이름" value="name"/>
					<option label="제목" value="title"/>
				</select>
				<input type="text" name="keyword" placeholder="검색어를 입력하세요">&nbsp;&nbsp;
				<input type="submit" value="검색">
				</form>
			</td>
		</tr>
	</table>
</html>



