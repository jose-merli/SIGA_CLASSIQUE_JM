<!DOCTYPE html>
<html>
<head>
<!-- pestanasRelacionPlantillas.jsp -->

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
	<!-- Escribe el t�tulo y localizaci�n en la barra de t�tulo del frame principal -->	
	<!-- FIN: TITULO Y LOCALIZACION -->
</head>

<body onload="ajusteAlto('mainPestanasRelacionPlantillas');return activarPestana();">
	
	<siga:PestanasExt 
		pestanaId="PLANTILLA" 
		target="mainPestanasRelacionPlantillas"
		modos="modosRelacionPlantillas" 
		parametros="hashRelacionPlantillas"
	/>
		
	<iframe src="<html:rewrite page='/html/jsp/general/blank.jsp'/>" id="mainPestanasRelacionPlantillas" name="mainPestanasRelacionPlantillas" scrolling="no" frameborder="0" class="framePestanas"></iframe>
	
	<iframe name="submitAreaRelacionPlantillas" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>" style="display:none"></iframe>
</body>
</html>