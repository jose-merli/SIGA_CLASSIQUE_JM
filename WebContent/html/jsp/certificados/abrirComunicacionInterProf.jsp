<!-- abrirComunicacionInterProf.jsp -->
<meta http-equiv="Expires" content="0"> 
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<%@ taglib uri = "libreria_SIGA.tld" prefix="siga"%>

<%@ page import="com.atos.utils.*"%>

<%
	String app=request.getContextPath();
	String url = (String)request.getAttribute("URL"); 
%>

<html>
	<head>
		<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
		<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.js"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.custom.js"></script>
		<siga:Titulo titulo="certificados.comunicacionInterProfesional.titulo" localizacion="menu.certificados"/>
	</head>

	<body>
		<iframe id="centro" align="center" src="<%=app%>/html/jsp/general/blank.jsp" style="width:100%; height:100%" >
		</iframe>
	</body>
	<script>
		centro.location='<%=url%>';
	</script> 
</html>