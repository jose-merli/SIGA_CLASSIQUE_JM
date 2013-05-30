<!-- default.jsp -->
<%@ page pageEncoding="ISO-8859-1"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>

<% String app=request.getContextPath(); %>
<html>
<head>
	<meta http-equiv="Expires" content="0">
	<meta http-equiv="Pragma" content="no-cache"> 
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	
	<title>Sistema de Gestión de la Abogacía</title>
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
</head>
<body onLoad="initStyles();">
	
</body>
</html>
