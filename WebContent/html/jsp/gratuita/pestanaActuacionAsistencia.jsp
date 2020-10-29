<!DOCTYPE html>
<html>
<head>
<!-- pestanaActuacionAsistencia.jsp -->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri="struts-html.tld" prefix="html"%>	

	<!-- HEAD -->
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
		
	<!-- Incluido jquery en siga.js -->	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>

	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el titulo y localizacion en la barra de titulo del frame principal -->	
	<!-- FIN: TITULO Y LOCALIZACION -->
</head>

<body onload="ajusteAlto('mainPestanaActuacionAsistencia');return activarPestana();">
	
	<siga:PestanasExt 
		pestanaId="ACTASIST" 
		target="mainPestanaActuacionAsistencia"
		modos="modosActuacionAsistencia" 
		parametros=""
	/>
		
	<iframe src="<html:rewrite page='/html/jsp/general/blank.jsp'/>" id="mainPestanaActuacionAsistencia" name="mainPestanaActuacionAsistencia" scrolling="no" frameborder="0" class="framePestanas"></iframe>
	
	<iframe name="submitAreaActuacionAsistencia" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>" style="display:none"></iframe>
</body>
</html>