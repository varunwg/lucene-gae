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
		<form action="index" method="post">
			<textarea rows="5" cols="50" name="text">Lucene AppEngine Search</textarea>
			</br> Index:
			<select name="index">
				<c:forTokens items="0,1,2,3,4,5,6,7,8,9" delims="," var="i">
					<option value="${i}">Indice${i}</option>
				</c:forTokens>
			</select>
			<input type="submit" value="Adicionar" />
		</form>
	</div>
	
	<jsp:include page="footer.jsp" />

</body>
</html>