<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>GAE Lucene Search</title>
</head>
<body>
	
	<form action="index" method="post">
		<h1>Adicionar Frases</h1>
		<select name="index">
			<c:forTokens items="0,1,2,3,4,5,6,7,8,9" delims="," var="i">
				<option value="${i}">Indice${i}</option>
			</c:forTokens>
		</select><br>
		<textarea rows="10" cols="20" name="text">teste 1
teste 2
teste 3</textarea><br>
		<input type="submit" value="Adicionar">
	</form>

</body>
</html>