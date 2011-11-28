<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>GAE Lucene Search</title>
</head>
<body>

	<a href="add.jsp">Adicionar</a>

	<form action="search">
		<h1>Pesquisar</h1>
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
		<input type="text" name="text" value="${text}"> 
		<input type="submit" value="Pesquisar">
		
	</form>

	<c:if test="${not empty results}">

		<h1>Results:</h1>

		<ol>

			<c:forEach items="${results}" var="result">
				<li>${result.key} - ${result.value}</li>
			</c:forEach>

		</ol>

	</c:if>

</body>
</html>