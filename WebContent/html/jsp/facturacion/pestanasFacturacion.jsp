<!-- pestanasFacturacion.jsp -->
<!-- VENTANA CON PESTAÑAS -->
<!-- Contiene los diferentes conjuntos de pestanhas ordenadas segun
     el criterio elegido. Los diferentes conjuntos están numerados de tal
     modo que se puedan identificar y manejar dentro de la logica de navegación
     de la aplicacion 
     VERSIONES:
	yolanda.garcia 22-12-2004 Creación
-->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
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

<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);	
	request.getSession().setAttribute("pestanas","1");

	String idSerieFacturacion=(String)request.getAttribute("idSerieFacturacion");
	String editable = (String)request.getAttribute("editable");
	String accion = (String)request.getAttribute("accion");
	ses.setAttribute("idSerieFacturacion",idSerieFacturacion);
	ses.setAttribute("editable",editable);
	ses.setAttribute("accion",accion);
%>	
	
<html>

	<!-- HEAD -->
	<head>
		<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
		<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>

		<!-- INICIO: TITULO Y LOCALIZACION -->
		<!-- Escribe el título y localización en la barra de título del frame principal -->
		<siga:Titulo 
			titulo="facturacion.asignacionDeConceptosFacturables.titulo" 
			localizacion="facturacion.asignacionDeConceptosFacturables.titulo"/>
		<!-- FIN: TITULO Y LOCALIZACION -->

		<script language="JavaScript">
			<!-- Funcion asociada a boton buscar -->
			function buscar() 
			{
				document.frames["mainPestanas"].location.href=document.frames["mainPestanas"].location.href;
			}
		</script>
	</head>
 
	<body onload="ajusteAlto('mainPestanas');return activarPestana();">

		<div class="posicionPestanas">

		<!-- Formulario de la lista de detalle multiregistro -->
		<html:form action="/FAC_DatosGenerales.do" method="POST">

			<!-- Campo obligatorio -->
			<html:hidden property = "modo" value = "editar"/>
	
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
				pestanaId="SERIESFAC" 
				target="mainPestanas"
				elementoactivo="1"
			/>

			<!-- FIN: CONJUNTO DE PESTAÑAS (LINEA) -->
			<!-- Del mismo modo se pueden configurar más lineas de pestanhas iterando conjuntos de pestanhas -->

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
			marginwidth="0";					 
			style="position:absolute; width:964; height:373; z-index:2; top: 50px; left: 0px">
		</iframe>-->
	
		<!-- INICIO: SUBMIT AREA -->
		<!-- Obligatoria en todas las páginas-->
		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
		<!-- FIN: SUBMIT AREA -->
	</body>
</html>
