<!-- loadErrorPage.jsp -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>
<%@ taglib uri = "libreria_SIGA.tld" prefix="siga"%>

<%
	String app=request.getContextPath();
	String target="document";
	
	if (session.getAttribute("exceptionTarget") != null)
	{
		target = (String) session.getAttribute("exceptionTarget");
	}
%>

<html>
	<head>
		<script>
			function loadErrorPage()
			{
				<%=target%>.location.href="<%=app%>/html/jsp/general/errors.jsp";
			}
		</script>
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>
		
	</head>

	<body onLoad="loadErrorPage();">
	</body>
</html>
