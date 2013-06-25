<!-- pestanasCenSJCSNoColegiado.jsp -->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="struts-html.tld" 	prefix="html"%>
<%@ taglib uri="libreria_SIGA.tld" 	prefix="siga"%>
 
<!-- IMPORTS -->
<%@ page import="java.util.Hashtable"%>

<!-- JSP -->
<% 
	String sIdInstitucion = (String) request.getParameter("idInstitucion");
	String sIdPersona = (String) request.getParameter("idPersona");

	//Preparo los parametros a pasar al siguiente nivel de pestanhas:
	Hashtable datosPestana = new Hashtable();	
	datosPestana.put("idInstitucionPestana", sIdInstitucion);
	datosPestana.put("idPersonaPestana", sIdPersona);
	request.setAttribute("datos", datosPestana);
%>

<html>

<head> 	
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
</head>

<body onload="ajusteAlto('mainPestanasCenSJCSNoColegiado'); return activarPestana();">
	
	<siga:PestanasExt 
		pestanaId="TO_NOCOLE" 
		target="mainPestanasCenSJCSNoColegiado" 
		elementoactivo="1"
		parametros="datos"
	/>
		
	<iframe src="<html:rewrite page='/html/jsp/general/blank.jsp'/>" id="mainPestanasCenSJCSNoColegiado" name="mainPestanasCenSJCSNoColegiado" scrolling="no" frameborder="0" class="framePestanas"></iframe>
	
	<iframe name="submitAreaPestanasCenSJCSNoColegiado" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>" style="display:none"></iframe>
</body>
</html>