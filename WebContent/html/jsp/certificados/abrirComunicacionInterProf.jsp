<!-- abrirComunicacionInterProf.jsp -->
<meta http-equiv="Expires" content="0"> 
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "libreria_SIGA.tld" prefix="siga"%>

<%@ page import="com.atos.utils.*"%>

<%
	String app=request.getContextPath();
	String url = (String)request.getAttribute("URL"); 
%>

<html>
	<head>
		<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
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