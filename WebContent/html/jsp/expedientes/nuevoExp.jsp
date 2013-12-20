<!DOCTYPE html>
<html>
<head>
<!-- nuevoExp.jsp -->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>

<!-- IMPORTS -->
<%@ page import="com.atos.utils.ClsConstants"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.siga.Utilidades.UtilidadesString"%>
<%@ page import="java.util.Properties"%>

<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
%>	

	<!-- HEAD -->
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	<!-- Incluido jquery en siga.js -->
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>

	<script>
		function abrirVentana() {
			var resultado = ventaModalGeneral(document.forms[1].name,"P");
			if (resultado!=undefined && resultado[0]!=undefined && resultado[1]!=undefined && resultado[0]!="V") {
				document.forms[0].action="<%=app%>/EXP_AuditoriaExpedientes.do?noReset=true&idInst="+resultado[0]+"&idTipo="+resultado[1];
				document.forms[0].modo.value="nuevo";
				document.forms[0].avanzada.value="<%=ClsConstants.DB_FALSE%>";
				document.forms[0].target="mainWorkArea";	
				document.forms[0].submit();			
			}
		}
	</script>

	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el título y localización en la barra de título del frame principal -->
	<siga:Titulo titulo="expedientes.nuevoExpediente.cabecera" localizacion="expedientes.literal.localizacion"/>
	<!-- FIN: TITULO Y LOCALIZACION -->
</head>

<body onLoad="abrirVentana();" >
	<html:form action="/EXP_AuditoriaExpedientes.do?noReset=true" method="POST" target="mainWorkArea">
		<html:hidden property = "modo" value = ""/>
		<html:hidden property = "avanzada" value = ""/>
	</html:form>

	<html:form action="/EXP_NuevoExpediente.do" method="POST" target="mainWorkArea">
		<input type="hidden" name="actionModal" value="">
		<html:hidden property = "modo" value = "abrirAvanzada"/>
		<!--<html:hidden property = "idInstitucion_TipoExpediente" value = ""/>		
		<html:hidden property = "idTipoExpediente" value = ""/>		-->
	</html:form>
</body>
</html>