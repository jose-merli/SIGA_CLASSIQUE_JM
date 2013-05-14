<!-- inicioFichaMutualidad.jsp -->
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

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>"/>
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/js/jquery.ui/css/jquery-ui.1.9.2.custom.min.css'/>"/>
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-1.8.3.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-ui-1.9.2.custom.min.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script>	
</head>
 
<body onload="ajusteAlto('mainPestanas');return activarPestana();">

	<!-- Formulario de la lista de detalle multiregistro -->
	<html:form action="/CEN_BusquedaClientes.do" method="POST" style="display:none">

	<!-- Campo obligatorio -->
	<html:hidden property = "modo" value = ""/>
	</html:form> 
	
	<!-- INICIO: CONJUNTO DE PESTAÑAS (LINEA) -->

	<!-- TAG DE CONJUNTO DE PESTANAS -->
	
	<siga:PestanasExt 
			pestanaId="MUTUA" 
			target="mainPestanas"
			parametros="datosMutualidad"
			

		
	/>
	<iframe src="<html:rewrite page='/html/jsp/general/blank.jsp'/>"
			id="mainPestanas"
			name="mainPestanas"
			scrolling="no"
			frameborder="0"
			marginheight="0"
			marginwidth="0";
			class="framePestanas"
	>
	</iframe>
	<!-- FIN: IFRAME GESTION PRINCIPAL -->

<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

</body>
</html>