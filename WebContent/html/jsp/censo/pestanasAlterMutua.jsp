<!-- pestanasMutualidad.jsp -->
<!-- EJEMPLO DE VENTANA DE PESTAÑAS -->
<!-- Contiene la zona de pestanhas y la zona de gestion principal 
	 VERSIONES:
	 raul.ggonzalez 16-12-2004 Modificacion de formularios para validacion por struts de campos
-->


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

<!-- JSP -->
	
<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href='<html:rewrite page="/html/jsp/general/stylesheet.jsp"/>'>
	<script src="<html:rewrite page='/html/js/SIGA.js'/>" type="text/javascript"></script>
</head>
 
<body onload="ajusteAlto('mainPestanas');return activarPestana();">
	<html:form action="/CEN_AlterMutua.do" method="POST">
	<html:hidden property = "modo" value = ""/>
	</html:form> 
	<siga:PestanasExt 
			pestanaId="ALTERMUT" 
			target="mainPestanas"
			parametros="SOLINC"
	/>
	<iframe src="<html:rewrite page='/html/jsp/general/blank.jsp'/>"
			id="mainPestanas"
			name="mainPestanas"
			scrolling="no"
			frameborder="0"
			class="framePestanas"
	>
	</iframe>
	<iframe name="submitArea" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>" style="display:none"></iframe>
</body>
</html>