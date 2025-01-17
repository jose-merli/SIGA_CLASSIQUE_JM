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
	
	//Leemos el token de la cabecera para poder hacer login de nuevo
	String token = (String)request.getParameter("token");
	String urlLogin="";
    if(token == null){
    	token= "";
    }else{
    	token="?token="+token;
    	urlLogin="/SIGA/login.do"+token;
    }
	
	
%>


	
		<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script>
		<script type="text/javascript">
		
			var url2 = window.location;
			
			function cargarInicio() {
				
				var msg1 = '<siga:Idioma key="messages.noSession"/>';
				<%if(token.equalsIgnoreCase("")){%>
				alertStop(msg1);
				<%}else{%>
				window.location.replace(url2);
				<%}%>


			}

		</script>

	</head>
	
	<body onLoad="cargarInicio();">
		<iframe src="<%=urlLogin%>" style="display: none"/>
	</body>
	
	
</html>