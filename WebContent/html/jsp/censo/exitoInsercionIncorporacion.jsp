<!-- exitoInsercionIncorporacion.jsp -->
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

<html:html>
<head>
<%
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	
	// ATRIBUTOS
	// MENSAJE = mensaje a mostrar (si no hay mensaje no muestra alert)  
	String mensaje = (String)request.getAttribute("mensaje");
	String idPersona = (String)request.getAttribute("idPersona");
	String idInstitucion = (String)request.getAttribute("idInstitucion");
%>

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	<script type="text/javascript">
	 function reloadPage() {
		<%  if (mensaje!=null){%>
			var type = '<siga:Idioma key="<%=mensaje%>"/>';
			alert(type);
		<%  } %>
			document.busquedaClientesForm.action=document.busquedaClientesForm.action+"?editarNColegiado=1";
			document.busquedaClientesForm.idPersona.value="<%=idPersona %>";
			document.busquedaClientesForm.idInstitucion.value="<%=idInstitucion %>";
			document.busquedaClientesForm.modo.value="recargarEditar";
			document.busquedaClientesForm.submit();
	 }
	</script>
</head>

<body onload="reloadPage();">

	<html:form  action="/CEN_BusquedaClientes.do" method="POST" target="mainWorkArea" style="display:none">
	<html:hidden  name="busquedaClientesForm" property="modo"/>
	<input type="hidden"  name="idPersona" value="">
	<input type="hidden"  name="idInstitucion" value="">
	</html:form>
	
</body>
</html:html>