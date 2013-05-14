<!-- administracionFacturacion.jsp -->
<!-- EJEMPLO DE VENTANA DE PESTAÑAS -->
<!-- Contiene la zona de pestanhas y la zona de gestion principal 
	 VERSIONES:
	 raul.ggonzalez 07-03-2005 creacion
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
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);	

	// para saber hacia donde volver
	String busquedaVolver = (String) request.getSession().getAttribute("SJCSBusquedaFacturacionTipo");
	if (busquedaVolver==null) {
		busquedaVolver = "volverNo";
	}


%>	
	
<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>"/>
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/js/jquery.ui/css/jquery-ui.1.9.2.custom.min.css'/>"/>
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-1.8.3.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-ui-1.9.2.custom.min.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script>

	<!-- TITULO Y LOCALIZACION -->
	<title><siga:Idioma key="censo.consultaDatosGenerales.cabecera"/></title>
	<script>
		var pretitulo = "<siga:Idioma key="general.ventana.cgae"/>";
		var tit = "<siga:Idioma key="censo.fichaCliente.cabecera"/>";
		var loc = "<siga:Idioma key="censo.fichaCliente.localizacion"/>";
		setTitulo(pretitulo, tit);
		setLocalizacion(loc);
	</script>

	
</head>
 
<body onload="ajusteAlto('mainPestanas');return activarPestana();">

<div class="posicionPestanas">

	<!-- Formulario de la lista de detalle multiregistro -->
	<html:form action="/CEN_MantenientoFacturacion.do?noReset=true" method="POST">

	<!-- Campo obligatorio -->
	<html:hidden property = "modo" value = ""/>
	
	<!-- INICIO: CONJUNTO DE PESTAÑAS (LINEA) -->

	<!-- TAG DE CONJUNTO DE PESTANAS -->
	<%
		// hash de pestanhas
		Hashtable hashPestanas = (Hashtable) request.getAttribute("datosFacturacion");
		if (hashPestanas!=null) {
			String tipoAcceso = (String)hashPestanas.get("tipoAcceso");
			String idInstitucion = (String)hashPestanas.get("idInstitucion");

	%>
	<siga:PestanasExt 
			pestanaId="FACTSJCS" 
			target="mainPestanas"
			parametros="datosFacturacion"
			elementoactivo="1"
			tipoAcceso="<%=tipoAcceso.toString()%>"
			idInstitucionCliente="<%=idInstitucion %>"			
		
	/>
	<%
		}
	%>

<!-- FIN: CONJUNTO DE PESTAÑAS (LINEA) -->

	</html:form> 

</div>
	

	<!-- INICIO: IFRAME GESTION PRINCIPAL -->
	<iframe src="<%=app%>/html/jsp/general/blank.jsp"
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
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

</body>
</html>
