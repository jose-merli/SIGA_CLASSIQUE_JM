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
<%@ page import="com.siga.certificados.form.SIGASolicitudCertificadoForm"%>
<%@ page import="com.atos.utils.*"%>

<html:html>
<head>
<%
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	UsrBean userBean = (UsrBean)request.getSession().getAttribute("USRBEAN");
	SIGASolicitudCertificadoForm form = (SIGASolicitudCertificadoForm)request.getAttribute("SolicitudCertificadoForm");
	
	// ATRIBUTOS
	// MENSAJE = mensaje a mostrar (si no hay mensaje no muestra alert)  


%>

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	<script type="text/javascript">
		function reloadPage() {
		    var type = '<siga:Idioma key="messages.envios.noExistePersonaOrigen"/>';
			if (confirm(type)) {
				document.forms[0].modo.value = "insertar";
				document.forms[0].submit();
			}
		}
	</script>
</head>

<body onload="reloadPage();">

		<html:form action="/CER_SolicitudCertificado" method="POST" target="_self">
			<html:hidden property = "modo" value = "insertar"/>
			<html:hidden property = "hiddenFrame" value = "1"/>
			<input type="hidden" name="apellido1" value="<%= form.getApellido1() %>">
			<input type="hidden" name="apellido2" value="<%= form.getApellido2() %>">
			<input type="hidden" name="nombre" value="<%= form.getNombre() %>">
			<input type="hidden" name="nif" value="<%= form.getNif() %>">
			<input type="hidden" name="idInstitucionOrigen" value="<%= form.getIdInstitucionOrigen() %>">
			<input type="hidden" name="idInstitucionDestino" value="<%= form.getIdInstitucionDestino() %>">
			<input type="hidden" name="descripcion" value="<%= form.getDescripcion() %>">
			<input type="hidden" name="idInstitucion" value="<%= form.getIdInstitucion() %>">
			<input type="hidden" name="idPersona" value="<%= form.getIdPersona() %>">
			<input type="hidden" name="fechaSolicitud" value="<%= form.getFechaSolicitud() %>">
			<input type="hidden" name="metodoSolicitud" value="<%= form.getMetodoSolicitud() %>">
		</html:form>
</body>
</html:html>