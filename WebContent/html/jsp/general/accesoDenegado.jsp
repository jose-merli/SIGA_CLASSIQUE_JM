<!-- accesoDenegado.jsp -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.siga.Utilidades.UtilidadesString"%>


<% String app=request.getContextPath(); %>
<%
String mensaje = (String)request.getAttribute("mensaje");
UsrBean b = UsrBean.UsrBeanAutomatico("2000");
if (mensaje==null) {
	mensaje=UtilidadesString.getMensajeIdioma(b,"messages.error.accesoDenegado");
} else {
	mensaje=UtilidadesString.getMensajeIdioma(b,mensaje);
}

%>
<html>
	<head>
		<title>Sistema de Gestión de la Abogacía</title>
		
		<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
		
		<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>
	</head>

	<body onLoad="initStyles();">
		<br><br><br><br><br><br>
		<p class="titulitos" style="text-align:center"><%=mensaje %></p>
	</body>
</html>
