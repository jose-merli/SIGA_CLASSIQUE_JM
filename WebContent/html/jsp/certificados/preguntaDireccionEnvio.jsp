<!DOCTYPE html>
<html>
<head>
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
<%@ page import="com.atos.utils.*"%>

<%
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	UsrBean userBean = (UsrBean)request.getSession().getAttribute("USRBEAN");	
	
	// ATRIBUTOS
	// MENSAJE = mensaje a mostrar (si no hay mensaje no muestra alert)  
	String PREG_idTipoEnvio = (String)request.getAttribute("PREG_idTipoEnvio");
	String PREG_idSolicitud = (String)request.getAttribute("PREG_idSolicitud");
	String PREG_idFactura = (String)request.getAttribute("PREG_idFactura");
	String PREG_idPersona = (String)request.getAttribute("PREG_idPersona");
	String PREG_idEnvio = (String)request.getAttribute("PREG_idEnvio");
	String PREG_nombreEnvio = (String)request.getAttribute("PREG_nombreEnvio");
	String PREG_idPlantilla = (String)request.getAttribute("PREG_idPlantilla");
	String PREG_idPlantillaGeneracion = (String)request.getAttribute("PREG_idPlantillaGeneracion");
	String PREG_fechaProgramada=(String)request.getAttribute("PREG_fechaProgramada");
	String PREG_existe=(String)request.getAttribute("PREG_existe");
	String PREG_subModo=(String)request.getAttribute("PREG_subModo");

%>

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	<script type="text/javascript">
		function reloadPage() {
		    var type = '<siga:Idioma key="messages.envios.noExisteDireccionOrigen"/>';
			if (confirm(type)) {
				document.forms[0].submit();
			}
		}
	</script>
</head>

<body onload="reloadPage();">

		<html:form action="/ENV_DefinirEnvios.do" method="POST" target="_self">
		
			<html:hidden property = "modo" value = "insertarEnvioModal"/>
			<html:hidden property = "paraConsejo" value = "1"/>

			<html:hidden property = "nombre" value = "<%=PREG_nombreEnvio %>"/>
			<html:hidden property = "idPlantillaEnvios" value = "<%=PREG_idPlantilla %>"/>
			<html:hidden property = "idPlantillaGeneracion" value = "<%=PREG_idPlantillaGeneracion %>"/>
			<html:hidden property = "idTipoEnvio" value = "<%=PREG_idTipoEnvio %>"/>
			<html:hidden property = "idSolicitud" value = "<%=PREG_idSolicitud %>"/>
			<html:hidden property = "idFactura" value = "<%=PREG_idFactura %>"/>
			<html:hidden property = "idPersona" value = "<%=PREG_idPersona %>"/>
			<html:hidden property = "idEnvio" value = "<%=PREG_idEnvio %>"/>
			<html:hidden property = "fechaProgramada" value = "<%=PREG_fechaProgramada %>"/>
			<html:hidden property = "PREG_existe" value = "<%=PREG_existe %>"/>
			<html:hidden property = "subModo" value = "<%=PREG_subModo%>"/>
			
		</html:form>

</body>
</html>