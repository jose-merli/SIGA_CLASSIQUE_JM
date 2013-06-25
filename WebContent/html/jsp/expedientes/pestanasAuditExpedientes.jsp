<!-- pestanasAuditExpedientes.jsp -->
<!-- Contiene la zona de pestanhas y la zona de gestion principal de la auditoría de expedientes
	 VERSIONES:
	 emilio.grau 17-01-2005 versión inicial
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
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="java.util.Properties"%>

<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	
	String grupo = request.getAttribute("nuevo").equals("true")?"NUEVOEXP":"AUDITEXP";
	String[] pestanasOcultas = (String[])request.getAttribute("pestanasOcultas");
	String idTipoExpediente = (String)request.getAttribute("idTipoExpediente");
	String idInstitucionTipoExpediente = (String)request.getAttribute("idInstitucionTipoExpediente");
	
	String accion="edicion";
	if(request.getAttribute("nuevo").equals("true"))
		accion="nuevo";
%>	
	
<html>

<script language="JavaScript">
			<!-- Funcion asociada a boton buscar -->
			function buscar() 
			{
				 window.frames["mainPestanas"].location.href= window.frames["mainPestanas"].location.href;
			}

</script>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>

	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el título y localización en la barra de título del frame principal -->
	<siga:Titulo 
		titulo="expedientes.auditoria.literal.titulo" 
		localizacion="expedientes.auditoria.literal.localizacion"/>
	<!-- FIN: TITULO Y LOCALIZACION -->
	
</head>
 
<body onload="ajusteAlto('mainPestanas');return activarPestana();">

<div class="posicionPestanas">

	<!-- Formulario de la lista de detalle multiregistro -->
	<html:form action="/EXP_AuditoriaExpedientes.do" method="POST">
	<input type="hidden" name="accion"	value="<%=accion%>">		
	
	<!-- Campo obligatorio -->
	<html:hidden property = "modo" value = ""/>	
	
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
			pestanaId="<%=grupo%>" 
			target="mainPestanas"
			parametros="expediente"
			elementoactivo="1"
			idTipoExpediente="<%=idTipoExpediente%>"
			idInstitucionTipoExpediente="<%=idInstitucionTipoExpediente%>"
			procesosinvisibles="<%=pestanasOcultas%>"
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
			class="framePestanas">
	</iframe>
	<!-- FIN: IFRAME GESTION PRINCIPAL -->

<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

</body>
</html>
