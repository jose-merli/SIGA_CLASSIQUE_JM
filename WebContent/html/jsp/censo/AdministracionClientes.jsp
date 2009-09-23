<!-- AdministracionClientes.jsp -->
<!-- EJEMPLO DE VENTANA DE PESTAÑAS -->
<!-- Contiene la zona de pestanhas y la zona de gestion principal 
	 VERSIONES:
	 raul.ggonzalez 16-12-2004 Modificacion de formularios para validacion por struts de campos
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
<%@ page import="java.util.*"%>
<%@ page import="com.atos.utils.*"%>


<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);	
	String[] pestanasOcultas = (String[])request.getAttribute("pestanasOcultas");

	// para saber hacia donde volver
	String busquedaVolver = (String) request.getSession().getAttribute("CenBusquedaClientesTipo");
	if (busquedaVolver==null) {
		busquedaVolver = "volverNo";
	}
	int elementoActivo=1;
	if(request.getAttribute("elementoActivo")!=null && !request.getAttribute("elementoActivo").equals("1")){
	 
	  elementoActivo=new Integer((String)request.getAttribute("elementoActivo")).intValue();
	}  
   

%>	
	
<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>

	
</head>
 
<body onload="ajusteAlto('mainPestanas');return activarPestana();">

	<!-- Formulario de la lista de detalle multiregistro -->
	<html:form action="/CEN_BusquedaClientes.do" method="POST" style="display:none">

	<!-- Campo obligatorio -->
	<html:hidden property = "modo" value = ""/>
	</html:form> 
	
	<!-- INICIO: CONJUNTO DE PESTAÑAS (LINEA) -->

	<!-- TAG DE CONJUNTO DE PESTANAS -->
	<%
		// hash de pestanhas
		Hashtable hashPestanas = (Hashtable) request.getAttribute("datosCliente");
		if (hashPestanas!=null) {
			String tipoAcceso = (String)hashPestanas.get("tipoAcceso");
			String idInstitucion = (String)hashPestanas.get("idInstitucion");
			// Para ver si debemos abrir el jsp comun de colegiados o el propio para no colegiados:
			String tipo = (String)hashPestanas.get("tipo");
	%>
	<siga:PestanasExt 
			pestanaId="FICHACLIEN" 
			target="mainPestanas"
			parametros="datosCliente"
			elementoactivo="<%=elementoActivo%>"
			tipoAcceso="<%=tipoAcceso.toString()%>"
			idInstitucionCliente="<%=idInstitucion %>"	
			procesosinvisibles="<%=pestanasOcultas%>"		
	/>
	<%
		}
	%>

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