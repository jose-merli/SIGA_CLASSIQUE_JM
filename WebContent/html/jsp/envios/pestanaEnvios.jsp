<!-- pestanaEnvios.jsp -->
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

<!-- IMPORTS -->
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="java.util.Properties"%>
<%@ page import="java.util.Hashtable"%>
<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);
	
	Hashtable htDatos = (Hashtable)request.getAttribute("envio");
	String idTipoEnvio=(String)htDatos.get("idTipoEnvio");
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

	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el título y localización en la barra de título del frame principal -->
	<siga:Titulo 
		titulo="envios.definir.literal.titulo" 
		localizacion="envios.definir.literal.localizacion"/>
	<!-- FIN: TITULO Y LOCALIZACION -->
		
</head>
 
<body onload="ajusteAlto('mainPestanas');return activarPestana();">

<div class="posicionPestanas">

	<!-- Formulario de la lista de detalle multiregistro -->
	<html:form action="/ENV_DefinirEnvios.do" method="POST">

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
	
	<%--<siga:PestanasExt 
			pestanaId="ENVIOS" 
			target="mainPestanas"
			parametros="envio"			
	/>--%>

<%
	String[] lista = new String[3];
	if (idTipoEnvio.equals(EnvTipoEnviosAdm.K_CORREO_ELECTRONICO)) {
		lista[0] = "73i";
	} else {
		lista[0] = "73j";
		if (idTipoEnvio.equals(EnvTipoEnviosAdm.K_SMS) || idTipoEnvio.equals(EnvTipoEnviosAdm.K_BUROSMS)) {
			lista[1] = "73h";
			lista[2] = "73g";
		} else {
			lista[1] = "73h";
			lista[2] = "73i";
		} 
	}
	
	
%>
	
	<siga:PestanasExt 
			pestanaId="ENVIOS" 
			target="mainPestanas"
			parametros="envio"
			elementoactivo="1"
			procesosinvisibles="<%=lista%>"
	/>
	
	<%--<siga:PestanasExt 
			pestanaId="PERMEXP" 
			target="mainPestanas"
			parametros="envio"
	/>		--%>
		

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
