<!DOCTYPE html>
<html>
<head>
<!-- pestanaAsisLetradoSJCS.jsp -->
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
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="java.util.Properties"%>
<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	
%>	
	


<!-- HEAD -->


	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>


	<script language="JavaScript">
			//Funcion asociada a boton buscar
			function buscar() {
				window.frames["mainPestanas"].location.href = window.frames["mainPestanas"].location.href;
			}

			function refrescarLocal() {
				buscar();
			}
	
	</script>
	
</head>
 
<body onload="ajusteAlto('mainPestanas');return activarPestana();">

<div class="posicionPestanas">

	<!-- Formulario de la lista de detalle multiregistro -->
	<html:form action="/JGR_AsistenciasLetrado.do" method="POST">

	<!-- Campo obligatorio -->
	<html:hidden property = "modo"/>
	
	<!-- INICIO: CONJUNTO DE PESTA�AS (LINEA) -->
<!-- Esto pinta una linea de pestanhas, pero primero es necesario
	 crear los diferentes arrays que contienen los elementos de la linea.
	 Estos arrays deben medir igual para representar las etiquetas a mostrar,
	 los action a ejecutar y el target destino.
	 Contiene un par�metro que indica la numeracion del conjunto para su manipulaci�n
	 y una atributo visible, para configuirar qu� conjunto estara inicialmente visible
	 dentro de la logica de funcionamiento de las pestanhas 
-->

	<!-- TAG DE CONJUNTO DE PESTANAS -->
	
	
	<% 
		String grupoPestanas = "LETASIST";
	%>
	
	<siga:PestanasExt 
			pestanaId="<%=grupoPestanas%>" 
			target="mainPestanas"
			parametros="asistencia"
			elementoactivo="1"
	/>
	

<!-- FIN: CONJUNTO DE PESTA�AS (LINEA) -->
<!-- Del mismo modo se pueden configurar m�s lineas de pestanhas iterando conjuntos de pestanhas -->

	</html:form> 

</div>
	

		<!-- INICIO: IFRAME GESTION PRINCIPAL -->
	<iframe src="<%=app%>/html/jsp/general/blank.jsp"
			id="mainPestanas"
			name="mainPestanas"
			scrolling="no"
			frameborder="0"
			class="framePestanas"
			>
	</iframe>
	<!-- FIN: IFRAME GESTION PRINCIPAL -->
	
	<!--<iframe align="center" src="<%=app%>/html/jsp/general/blank.jsp"
				id="mainPestanas"
				name="mainPestanas" 
				scrolling="no"
				frameborder="0"
				marginheight="0"
				marginwidth="0"				 
				style="position:absolute; width:945; height:373; z-index:2; top: 50px; left: 0px">
	</iframe>-->
	

<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las p�ginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

</body>
</html>
