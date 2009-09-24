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
<%@ page import="com.atos.utils.ReadProperties"%>
<%@ page import="com.atos.utils.BotonesMenu"%>


<%
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);
	//ReadProperties rproperties=new ReadProperties("SIGA.properties");
	//String pathInicio = rproperties.returnProperty("general.paginaInicio");
	
	
	//Cogemos el path de la base de datos no del SIGA.prperties
	
	String pathInicio = BotonesMenu.getPathCerrarSesion("0");//Pasamos la institucion 0 por defecto porque cuando se abre esta pagina se pierde el 
	                                                         // USERBEAN de sesion.
	
%>

<html>
	<head>
		<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
		
		<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>
		
		<script language="JavaScript">
			function cargarInicio() {
				var msg1 = '<siga:Idioma key="messages.noSession"/>';
				var msg2 = '<siga:Idioma key="messages.noSession.modal"/>';
				if (top.dialogArguments) {
					// es ventana modal
					alert(msg2);
					top.cierraConParametros("MODIFICADO");
				} else {
					alert(msg1);
					window.top.location="<%=pathInicio%>";
				}
			}
		</script>

	</head>
	
	<body onLoad="cargarInicio();">
	</body>
</html>