<!DOCTYPE html>
<html>
<head>
<!-- inicioSesion.jsp -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<%@ taglib uri = "libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>

<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="org.redabogacia.sigaservices.app.util.ReadProperties"%>
<%@ page import="com.atos.utils.BotonesMenu"%>
<%@ page import="java.util.Properties"%>

<%
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	
	
	//ReadProperties rproperties=new ReadProperties("SIGA.properties");
	//String pathInicio = rproperties.returnProperty("general.paginaInicio");
	
	
	//Cogemos el path de la base de datos no del SIGA.prperties
	
	String pathInicio = BotonesMenu.getPathCerrarSesion("2000");
	
%>


	
		<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script>
		<script type="text/javascript">
			function cargarInicio() {
				var msg1 = '<siga:Idioma key="messages.noSession"/>';
				var msg2 = '<siga:Idioma key="messages.noSession.modal"/>';
				if (top.dialogArguments) {
					// es ventana modal
					alertStop(msg2);
					top.cierraConParametros("MODIFICADO");
				} else {
					alertStop(msg1);
					window.top.location="<%=pathInicio%>";
				}
			}
		</script>

	</head>
	
	<body onLoad="cargarInicio();">
	</body>
</html>