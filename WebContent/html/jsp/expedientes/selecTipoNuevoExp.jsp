<!DOCTYPE html>
<html>
<head>
<!-- selecTipoNuevoExp.jsp -->
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
<%@ page import="com.atos.utils.ClsConstants"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.siga.Utilidades.UtilidadesString"%>
<%@ page import="java.util.Properties"%>
<%@ page import="java.util.Hashtable"%>
<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
		

	String	idInstitucion_TipoExpediente="";
	String	idTipoExpediente="";
	
	
	Hashtable datosTipoExpediente = (Hashtable) request.getAttribute("datosTipoExpediente");
	if (datosTipoExpediente!=null) {
		idInstitucion_TipoExpediente = (String) datosTipoExpediente.get("idInstitucion_TipoExpediente");
		if (idInstitucion_TipoExpediente==null) idInstitucion_TipoExpediente="";
		idTipoExpediente = (String) datosTipoExpediente.get("idTipoExpediente");
		if (idTipoExpediente==null) idTipoExpediente="";			
	}	

%>	


	<!-- HEAD -->
	

		<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>

		<script>
			function cerrarVentana() {
				var aux = new Array();
				aux[0]="<%=idInstitucion_TipoExpediente %>";
				aux[1]="<%=idTipoExpediente %>";				
				top.cierraConParametros(aux);
			}
		</script>

	</head>


<body onLoad="cerrarVentana();" >
</body>

</html>

