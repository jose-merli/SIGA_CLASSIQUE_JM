<!DOCTYPE html>
<html>
<head>
<!-- pestanasCertificado.jsp -->
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
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="java.util.Properties"%>
<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
		
	String[] pestanasOcultas = (String[])request.getAttribute("pestanasOcultas");
	
	System.out.println("Prueba"+request.toString());
		
	Hashtable htDatos = (Hashtable)request.getAttribute("datos");
	
	String idInstitucion = (String)request.getAttribute("idInstitucion");
	CerSolicitudCertificadosBean beanSolicitud = (CerSolicitudCertificadosBean) request.getAttribute("solicitud");
	String idInstitucionSolicitud = "";
	String sIdCompra = "", numSolicitud = "", idTipoProducto = "", idProducto = "";
	String idProductoInstitucion = "", idPeticion="", aceptMut = "";
	
	if (beanSolicitud != null){
		if(beanSolicitud.getIdInstitucion_Sol() != null) {
			idInstitucionSolicitud = beanSolicitud.getIdInstitucion_Sol().toString();
		}
	
		if(beanSolicitud.getIdPeticionProducto() != null) {
			sIdCompra = beanSolicitud.getIdPeticionProducto().toString();
		}
		
		if(beanSolicitud.getIdSolicitud() != null) {
			numSolicitud = beanSolicitud.getIdSolicitud().toString();
		}
		if(beanSolicitud.getPpn_IdTipoProducto() != null) {
			idTipoProducto = beanSolicitud.getPpn_IdTipoProducto().toString();
		}
		if(beanSolicitud.getPpn_IdProducto() != null) {
			idProducto = beanSolicitud.getPpn_IdProducto().toString();
		}
		if(beanSolicitud.getPpn_IdProductoInstitucion() != null) {
			idProductoInstitucion = beanSolicitud.getPpn_IdProductoInstitucion().toString();
		}
		
		if(beanSolicitud.getIdPeticionProducto()!=null)
			idPeticion =  beanSolicitud.getIdPeticionProducto().toString();
		
		if(beanSolicitud.getAceptaCesionMutualidad()!=null)
			aceptMut = beanSolicitud.getAceptaCesionMutualidad();
	}
	String tipoCertificado = (String) request.getAttribute("tipoCertificado");

	String idsTemp = "";
		
%>	
	


<script language="JavaScript">
			<!-- Funcion asociada a boton buscar -->
			function buscar() 
			{
				 window.frames["mainPestanas"].location.href= window.frames["mainPestanas"].location.href;
			}

</script>

<!-- HEAD -->


	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>

	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el título y localización en la barra de título del frame principal -->
	<siga:Titulo 
		titulo="certificados.solicitudes.titulo" 
		localizacion="certificados.solicitudes.literal.localizacion"/>
	<!-- FIN: TITULO Y LOCALIZACION -->
	
</head>
 
<body onload="ajusteAlto('mainPestanas');return activarPestana();">

<div class="posicionPestanas">

	<!-- Formulario de la lista de detalle multiregistro -->
	<html:form action="/CER_DetalleSolicitud.do" method="POST">

	<!-- Campo obligatorio -->
	<html:hidden property="modo" value="" />
	<html:hidden property="idInstitucion" styleId="idInstitucion" value="<%=idInstitucion%>" />
	<html:hidden property="idInstitucionSolicitud" value="<%=idInstitucionSolicitud%>" />
	<html:hidden property="buscarIdPeticionCompra" value="<%=sIdCompra %>" />
		
	<html:hidden property="idSolicitud" styleId="idSolicitud" value="<%=numSolicitud%>" />
	<html:hidden property="idTipoProducto" styleId="idTipoProducto" value="<%=idTipoProducto%>" />
	<html:hidden property="idProducto" styleId="idProducto" value="<%=idProducto%>" />
	<html:hidden property="idProductoInstitucion" styleId="idProductoInstitucion" value="<%=idProductoInstitucion%>" />
	<html:hidden property="tipoCertificado" value="<%=tipoCertificado%>" />
	<html:hidden property="idsTemp" value="<%=idsTemp%>"/>
	<html:hidden property="idSerieSeleccionada" styleId="idSerieSeleccionada" />
	<html:hidden property="regenerar" value="" />
	<input type="hidden" id="idPeticion" name="idPeticion" value="<%=idPeticion%>">	
	<input type="hidden" id="descargarCertificado" name="descargarCertificado" value="">	
	
	<html:hidden property="aceptaCesionMutualidad" value="<%=aceptMut%>" />
	
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
			pestanaId="DATOSCERT" 
			target="mainPestanas"
			parametros="datos"
			modos="modos"
			elementoactivo="1"
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
