<!-- insercionOK.jsp -->
<!-- 
	 Muestra un alert indicando que la insercion se ha realizado correctamente
	 VERSIONES:
	 miguel.villegas 04-01-2005 
-->

<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-Control" content="no-cache">

<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "libreria_SIGA.tld" prefix="siga"%>

<%@ page import="java.util.*"%>
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<% String app=request.getContextPath();%>

<html>
	<head>	
		<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
		<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">		
		<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>	
		<script>
		function mensaje()
		{			
			var mensaje='<siga:Idioma key="messages.updated.success"/>';
			alert(mensaje);
		}
		</script>
	</head>
	<body onLoad="mensaje();" class=titulitos>

	</body>
</html>
	