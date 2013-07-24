<!DOCTYPE html>
<html>
<head>
<!-- recargarModalDesignas.jsp -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>

<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.gratuita.form.*"%>

<%
	String app=request.getContextPath();
	BuscarDesignasForm formulario = (BuscarDesignasForm)request.getAttribute("datosEntrada");
	String hayDatos = (String)request.getAttribute("hayDatos");
	request.removeAttribute("hayDatos");
%>

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	<script type="text/javascript">
	function reloadPage() {
	<%  if (hayDatos!=null){%>
				parent.document.forms[1].ncolegiado.value ="<%=formulario.getNcolegiado()%>";
				parent.document.forms[1].idPersona.value ="<%=formulario.getIdPersona()%>";
				parent.document.forms[1].manual.value ="1";
	<%  	} %>
	return false;

}
</script>
</head>
<body onload="reloadPage()"></body>
</html>
