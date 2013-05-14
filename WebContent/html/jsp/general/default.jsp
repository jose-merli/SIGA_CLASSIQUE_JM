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
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/js/jquery.ui/css/jquery-ui.1.9.2.custom.min.css'/>"/>
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-1.8.3.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-ui-1.9.2.custom.min.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script>
</head>
<body onLoad="initStyles();">
	
</body>
</html>
