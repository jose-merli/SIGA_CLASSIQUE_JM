<!-- pestanaGuardiaExp.jsp -->
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
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);
	request.getSession().setAttribute("pestanasG","Exp");
%>	
	
<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>"/>
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/js/jquery.ui/css/jquery-ui.1.9.2.custom.min.css'/>"/>
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-1.8.3.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-ui-1.9.2.custom.min.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script>

	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el título y localización en la barra de título del frame principal -->
	<siga:Titulo 
		titulo="" 
		localizacion="Censo > Buscar Colegiado > SJCS > Guardias Inscrito"/>
	<!-- FIN: TITULO Y LOCALIZACION -->

<script language="JavaScript">
			<!-- Funcion asociada a boton buscar -->
			function buscar() {
				window.frames["mainPestanas"].location.href=window.frames["mainPestanas"].location.href;
			}

</script>
	
</head>
 
<body onload="ajusteAlto('mainPestanas');return activarPestana();">


	<!-- Formulario de la lista de detalle multiregistro -->
	<html:form action="/DefinirTurnosAction.do" method="POST" style="display:none">
		<!-- Campo obligatorio -->
		<html:hidden property = "modo" value = "editar"/>
	</html:form> 
	
	<!-- INICIO: CONJUNTO DE PESTAÑAS (LINEA) -->
<!-- Esto pinta una linea de pestanhas, pero primero es necesario
	 crear los diferentes arrays que contienen los elementos de la linea.
	 Estos arrays deben medir igual para representar las etiquetas a mostrar,
	 los action a ejecutar y el target destino.
	 Contiene un parámetro que indica la numeracion del conjunto para su manipulación
	 y una atributo visible, para configuirar qué conjunto estara inicialmente visible
	 dentro de la logica de funcionamiento de las pestanhas 
-->

	<!-- TAG DE CONJUNTO DE PESTANAS -->
	
	<siga:PestanasExt 
			pestanaId="GUARDIAEXP" 
			target="mainPestanas"
			parametros="HASHGUARDIA"
			elementoactivo="1"
	/>

<!-- FIN: CONJUNTO DE PESTAÑAS (LINEA) -->
<!-- Del mismo modo se pueden configurar más lineas de pestanhas iterando conjuntos de pestanhas -->


	

		<!-- INICIO: IFRAME GESTION PRINCIPAL -->
	<iframe src="<%=app%>/html/jsp/general/blank.jsp"
			id="mainPestanas"
			name="mainPestanas"
			scrolling="no"
			frameborder="0"
			class="framePestanas">
	</iframe>
	

<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

</body>
</html>
