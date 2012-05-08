<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8" trimDirectiveWhitespaces="true" isELIgnored="false"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
	<jsp:include page="head.jsp" />
</head>
<body>

	<jsp:include page="header.jsp" />
	
	<div id="searchbar">
		<form action="search">
			Index:
			<select name="index">
				<c:forTokens items="0,1,2,3,4,5,6,7,8,9" delims="," var="i">
					<c:if test="${index eq i}">
						<option value="${i}" selected="selected">Indice${i}</option>
					</c:if>
					<c:if test="${index ne i}">
						<option value="${i}">Indice${i}</option>
					</c:if>
				</c:forTokens>
			</select>
			<input type="text" name="q" value="${q}" />
			<input type="submit" value="Search" />
		</form>
	</div>

	<c:if test="${not empty results}">
		<ol>
			<c:forEach items="${results}" var="result">
				<li>${result.key} - ${result.value}</li>
			</c:forEach>
		</ol>
	</c:if>
	<c:if test="${empty results and paran.q ne null}">
		<h2>Nothing Found!</h2>
	</c:if>

	<jsp:include page="footer.jsp" />

</body>
</html>