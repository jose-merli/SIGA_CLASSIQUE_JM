<!DOCTYPE html>
<html>
<head>
<!-- administracionPago.jsp -->
<!-- Contiene la zona de pestanhas y la zona de gestion principal 
	 VERSIONES:
	 david.sanchez 17-03-2005 creacion
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
<%@ page import="java.util.*"%>
<%@ page import="com.atos.utils.*"%>


<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
		

	// para saber hacia donde volver
	String busquedaVolver = (String) request.getSession().getAttribute("SJCSBusquedaPagoTipo");
	if (busquedaVolver==null) {
		busquedaVolver = "volverNo";
	}

%>	
	


<!-- HEAD -->


	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	<!-- TITULO Y LOCALIZACION -->
	<title><siga:Idioma key="factSJCS.datosGeneralesPagos.cabecera"/></title>
	<script>
		var pretitulo = "<siga:Idioma key="general.ventana.cgae"/>";
		var tit = "<siga:Idioma key="factSJCS.datosGeneralesPagos.cabecera"/>";
		var loc = "<siga:Idioma key="factSJCS.datosGeneralesPagos.ruta"/>";
		top.setTitulo(pretitulo, tit);
		setLocalizacion(loc);
	</script>

	
</head>
 
<body onload="ajusteAlto('subPestanas');return activarPestana();">

<div class="posicionPestanas">

	<!-- Formulario de la lista de detalle multiregistro -->
	<html:form action="/CEN_MantenimientoPago.do?noReset=true" method="POST">

	<!-- Campo obligatorio -->
	<html:hidden property = "modo" value = ""/>
	
	<!-- INICIO: CONJUNTO DE PESTA�AS (LINEA) -->

	<!-- TAG DE CONJUNTO DE PESTANAS -->
	<%
		// hash de pestanhas
		Hashtable hashPestanas = (Hashtable) request.getAttribute("datosPago");
		if (hashPestanas!=null) {
			String idInstitucion = (String)hashPestanas.get("idInstitucion");

	%>
	<siga:PestanasExt 
			pestanaId="PAGOSJCS" 
			target="subPestanas"
			parametros="datosPago"
			elementoactivo="1"
			
		
	/>
	<%
		}
	%>

<!-- FIN: CONJUNTO DE PESTA�AS (LINEA) -->

	</html:form> 

</div>
	

	<!-- INICIO: IFRAME GESTION PRINCIPAL -->
	<iframe src="<%=app%>/html/jsp/general/blank.jsp"
			id="subPestanas"
			name="subPestanas"
			scrolling="no"
			frameborder="0"
			marginheight="0"
			marginwidth="0"
			class="framePestanas"
			>
	</iframe>
	<!-- FIN: IFRAME GESTION PRINCIPAL -->

<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las p�ginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

</body>
</html>
