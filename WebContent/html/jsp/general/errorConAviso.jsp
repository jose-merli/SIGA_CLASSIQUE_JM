<!DOCTYPE html>
<html>
<head>
<!-- exitoConString.jsp -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>

<%@ page import="com.siga.Utilidades.*"%>

<%
	String mensaje = (String)request.getAttribute("mensaje");
	mensaje=UtilidadesString.escape(mensaje);
	
%>

	
	<script type="text/javascript">
	function reloadPage() {
		var type = unescape('<%=mensaje%>');
		alert(type,"error");
		parent.refrescarLocal();
	
	return false;

}
</script>
</head>
<body onload="reloadPage()"></body>
</html>
