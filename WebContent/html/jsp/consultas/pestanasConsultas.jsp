<!-- pestanasConsultas.jsp -->
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

<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);
	String accion=(String)request.getAttribute("accion");
	String[] pestanasOcultas = null;
	String titulo = "consultas.recuperarconsulta.literal.titulo";
	if (accion!=null && accion.equals("nuevo")){
		pestanasOcultas = new String[]{"451"};
		titulo = "consultas.nuevaconsulta.literal.titulo";
	}
%>	
	
<html>

<script language="JavaScript">
		<!-- Funcion asociada a boton buscar -->
		function buscar() 
		{
			document.frames["mainPestanas"].location.href=document.frames["mainPestanas"].location.href;
		}

</script>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>


		<siga:TituloExt 
			titulo="consultas.nuevaConsulta.cabecera" 
			localizacion="consultas.nuevaConsulta.localizacion"
		/>
	
</head>
 
<body onload="ajusteAlto('mainPestanas');return activarPestana();">


	<!-- Formulario de la lista de detalle multiregistro -->
	<html:form action="/CON_RecuperarConsultas.do" method="POST" style="display:none">
	

	<!-- Campo obligatorio -->
	<html:hidden property = "modo" value = ""/>	

	</html:form> 
	
	<!-- INICIO: CONJUNTO DE PESTAÑAS (LINEA) -->

	<!-- TAG DE CONJUNTO DE PESTANAS -->
<%if (pestanasOcultas!=null){%>	
	<siga:PestanasExt 
			pestanaId="CONSULTA" 
			target="mainPestanas"
			parametros="consulta"
			elementoactivo="1"
			procesosinvisibles="<%=pestanasOcultas%>"
	/>	
<%}else{%>
	<siga:PestanasExt 
			pestanaId="CONSULTA" 
			target="mainPestanas"
			parametros="consulta"
			elementoactivo="1"
	/>	
<%}%>		

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
