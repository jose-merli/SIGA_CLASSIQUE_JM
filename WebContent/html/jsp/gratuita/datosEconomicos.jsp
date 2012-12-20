<!-- datosEconomicos.jsp -->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="struts-html.tld" 	prefix="html"%>
<%@ taglib uri="libreria_SIGA.tld" 	prefix="siga"%>

<%@ page import = "com.atos.utils.UsrBean"%>
<%@ page import = "java.util.Hashtable"%>

<% 	
	String idtipoejg = (String) request.getParameter("IDTIPOEJG");
	String anio = (String) request.getParameter("ANIO");
	String numero = (String) request.getParameter("NUMERO");	
	
	Hashtable htParametros = new Hashtable();
	htParametros.put("idtipoejg", idtipoejg);
	htParametros.put("anio", anio);
	htParametros.put("numero", numero);
	request.setAttribute("EJG", htParametros);
%>	

<html>

<head> 	
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>">
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.custom.js'/>"></script>		
</head>

<body onload="ajusteAlto('mainPestanasDatosEconomicos');return activarPestana();">
	
	<siga:PestanasExt 
		pestanaId="DATOSECONO" 
		target="mainPestanasDatosEconomicos"
		modos="modosDatosEconomicosEJG" 
		parametros="EJG"
	/>
		
	<iframe src="<html:rewrite page='/html/jsp/general/blank.jsp'/>" id="mainPestanasDatosEconomicos" name="mainPestanasDatosEconomicos" scrolling="no" frameborder="0" class="framePestanas"></iframe>
	
	<iframe name="submitAreaDatosEconomicos" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>" style="display:none"></iframe>
</body>
</html>