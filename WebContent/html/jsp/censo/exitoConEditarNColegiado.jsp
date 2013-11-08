<!DOCTYPE html>
<html>
<head>
<!-- exitoConEditarNColegiado.jsp -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<%@ page pageEncoding="ISO-8859-1"%>
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>

<%@ page import="com.siga.administracion.SIGAConstants"%>

<%
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	
	// ATRIBUTOS
	// MENSAJE = mensaje a mostrar (si no hay mensaje no muestra alert)  
	String mensaje       = (String)request.getAttribute("mensaje");
	String idPersona     = (String)request.getAttribute("idPersona");
	String idInstitucion = (String)request.getAttribute("idInstitucion");
%>
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	<script type="text/javascript">
		function reloadPage() {
			<%
				if (mensaje!=null){
					%>
						var type = '<siga:Idioma key="<%=mensaje%>"/>';
						alert(type);
					<%
				}
			%>
			parent.document.forms[0].modo.value="abrirEditarNColegiado";
			parent.document.forms[0].idPersona.value="<%=idPersona%>";
			parent.document.forms[0].idInstitucion.value="<%=idInstitucion%>";
			<%
				if (request.getAttribute("pestanaSituacion")!=null && request.getAttribute("pestanaSituacion").equals("1")){
					%>
		 				/* si estamos mostrado los datos de colegiacion desde la pestaña de situacion*/
		  				parent.document.forms[0].target="";
					<%
				}else{
					%>
		  				parent.document.forms[0].target="mainPestanas";
					<%
				}
			%>
			parent.document.forms[0].submit();
		}
	</script>
</head>

<body onload="reloadPage();">
<form >
 <input type="hidden" name="pestanaSituacion" value="<%=(String)request.getAttribute("pestanaSituacion")%>">		
</form >
</body>
</html>