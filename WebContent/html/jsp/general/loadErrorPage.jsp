<!DOCTYPE html>
<html>
<head>
<!-- loadErrorPage.jsp -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
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


	
		<script>
			function loadErrorPage()
			{
				<%=target%>.location.href="<%=app%>/html/jsp/general/errors.jsp";
			}
		</script>
		
		
		<script src="<%=app%>/html/js/SIGA.js?v=${sessionScope.VERSIONJS}" type="text/javascript"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.js"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.custom.js"></script>
		
	</head>

	<body onLoad="loadErrorPage();">
	</body>
</html>
