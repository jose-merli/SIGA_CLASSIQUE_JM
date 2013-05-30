<!-- administracionAbonos.jsp -->
<!-- EJEMPLO DE VENTANA DE PESTAÑAS -->
<!-- Contiene la zona de pestanhas y la zona de gestion principal 
	 VERSIONES:
	 miguel.villegas 10-03-2005 Creacion
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

	String leftPestanhas = "0";
	String altoIframe = "465";
	String pestanaId = "ABONOS";
	if(request.getAttribute("pestanaId")!=null)
		pestanaId = (String) request.getAttribute("pestanaId");
	
	Hashtable hashPestanas = (Hashtable) request.getAttribute("datosAbonos");
	String volver = null;
	if (hashPestanas!=null)
		volver = hashPestanas.get("botonVolver")==null?"NO":(String)hashPestanas.get("botonVolver");
	if (volver!=null && volver.equalsIgnoreCase("SI")){
		leftPestanhas = "18";
	} else {
		altoIframe = "445";	
	}
%>	
	
<html>

<!-- HEAD -->
<head>

		<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>

		<!-- INICIO: TITULO Y LOCALIZACION -->
		<!-- Escribe el título y localización en la barra de título del frame principal -->
		<siga:Titulo 
			  titulo="facturacion.administracionAbonos.literal.cabecera" 
			  localizacion="facturacion.administracionAbonos.ruta"/>
		<!-- FIN: TITULO Y LOCALIZACION -->

	
</head>
 
<body onload="ajusteAlto('mainPestanasNivel3');return activarPestana();">


	<!-- Formulario de la lista de detalle multiregistro -->
	<html:form action="/FAC_GenerarAbonos.do" method="POST" style="display:none">
	<!-- Campo obligatorio -->
	<html:hidden property = "modo" value = ""/>
	</html:form> 
	
	<!-- INICIO: CONJUNTO DE PESTAÑAS (LINEA) -->

	<!-- TAG DE CONJUNTO DE PESTANAS -->
	<%
		// Hash de pestanhas:
		if (hashPestanas!=null) {
			String idInstitucion = (String)hashPestanas.get("idInstitucion");
	%>
	<siga:PestanasExt 
			pestanaId="<%=pestanaId%>" 
			target="mainPestanasNivel3"
			parametros="datosAbonos"
			elementoactivo="1"
			idInstitucionCliente="<%=idInstitucion %>"/>
	<%
		}
	%>

<!-- FIN: CONJUNTO DE PESTAÑAS (LINEA) -->


	

	<!-- INICIO: IFRAME GESTION PRINCIPAL -->
	<iframe src="<%=app%>/html/jsp/general/blank.jsp"
			id="mainPestanasNivel3"
			name="mainPestanasNivel3"
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
