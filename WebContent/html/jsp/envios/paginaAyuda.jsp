<!-- pestanaCorreoElectronico.jsp -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
<%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java"
	errorPage="/html/jsp/error/errorSIGA.jsp"%>

<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri="struts-html.tld" prefix="html"%>




<html>

<head>
<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>"/>
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/js/jquery.ui/css/jquery-ui.1.9.2.custom.min.css'/>"/>
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-1.8.3.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-ui-1.9.2.custom.min.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script>
<title><siga:Idioma key="general.ayuda.titulo"/></title>

</head>

<body>

<table ALIGN="center" WIDTH="94%" border="0">
	<tr>
		<td WIDTH="10px">&nbsp;</td>
		<td>
		<p class="labelText" style="FONT-SIZE: 14px"><siga:Idioma key="general.ayuda.normativa"/></p>
		</td>
	</tr>
</table>
<hr size="1" align="center" width="97%" noshade="noshade" COLOR="black">

<table class="tablaCampos" align="center" width="100%">


	<tr><td class="labelText" >
	
	<li><siga:Idioma key="envios.ayuda.campos"/></li>
	</td>
	</tr>
	<tr><td class="labelText">
	<li><siga:Idioma key="envios.ayuda.imagenes"/></li>
	</td></tr>

</table>



</body>
</html>