<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8" trimDirectiveWhitespaces="true" isELIgnored="false"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"></meta>
<title>Lucene-AppEngine Search</title>
<style>
#container {
	width: 960px;
	margin: 150px auto;
	text-align: center;
}

#searchbar {
	margin-top: 20px;
}

input[name=q] {
	width: 400px;
}
</style>
</head>
<body>

	<div id="blackbar">
		<a href="add.jsp">Add</a>
	</div>
	<div id="container">
		<div>
			<img src="logo.png" />
		</div>
		<div id="searchbar">
			<form action="index" method="post">
				<textarea rows="5" cols="50" name="text">Lucene AppEngine Search</textarea>
				</br>
				Index:
				<select name="index">
					<c:forTokens items="0,1,2,3,4,5,6,7,8,9" delims="," var="i">
						<option value="${i}">Indice${i}</option>
					</c:forTokens>
				</select>
				<input type="submit" value="Adicionar" />
			</form>
		</div>
</body>
</html>