<!DOCTYPE html>
<html>
<head>
<!-- pestanasMantenimientoPagos.jsp -->
<!-- EJEMPLO DE VENTANA DE PESTAÑAS -->
<!-- Contiene la zona de pestanhas y la zona de gestion principal 
	 VERSIONES:
	 jose.barrientos 23-02-2009 Creacion
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
		
	UsrBean usrbean = (UsrBean)ses.getAttribute(ClsConstants.USERBEAN);
	String idInstitucion = "";
	String tipoAcceso = "";

	
	// para saber hacia donde volver

	int elementoActivo=1;
	if(request.getAttribute("elementoActivo")!=null && !request.getAttribute("elementoActivo").equals("1")){
	 
	  elementoActivo=new Integer((String)request.getAttribute("elementoActivo")).intValue();
	}  

%>	
	


<!-- HEAD -->


	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>

	
</head>
 
<body  onload="ajusteAlto('mainPestanas');return activarPestana();">

	<!-- Formulario de la lista de detalle multiregistro -->
	<html:form action="/CEN_MantenimientoPago.do" method="POST" style="display:none">

	<!-- Campo obligatorio -->
	<html:hidden property = "modo" value = ""/>
	</html:form> 


	<!-- INICIO: CONJUNTO DE PESTAÑAS (LINEA) -->

	<!-- TAG DE CONJUNTO DE PESTANAS -->
	<%
		// hash de pestanhas
		Hashtable hashPestanas = (Hashtable) request.getAttribute("datos");
	    if (hashPestanas!=null) {
		 	tipoAcceso = (String)hashPestanas.get("tipoAcceso");
			idInstitucion = (String)hashPestanas.get("idInstitucion");
	    }else{
	    	idInstitucion = usrbean.getLocation();
	    }
	%>
	<siga:PestanasExt
			pestanaId="PRINPAGOS" 
			target="mainPestanas"
			parametros="datos"
			elementoactivo="<%=elementoActivo%>"
			tipoAcceso="0xffffffff"
			idInstitucionCliente="<%=idInstitucion %>"			
	/>

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