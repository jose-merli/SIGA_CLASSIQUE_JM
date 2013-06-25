<!-- seleccionEnvio.jsp -->
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
<%@ page import="com.atos.utils.ClsConstants"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.siga.Utilidades.UtilidadesString"%>
<%@ page import="com.siga.beans.EnvEnviosBean"%>
<%@ page import="java.util.Properties" %>
<%@ page import="java.util.Hashtable" %>

<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
		
	UsrBean user = (UsrBean) ses.getAttribute("USRBEAN");
	
		String idEnvio = "";
		String idTipoEnvio = "";
		String nombreEnvio = "";

	Hashtable envio = (Hashtable) request.getAttribute("envio");
	if (envio!=null) {
		idEnvio = (String) envio.get(EnvEnviosBean.C_IDENVIO);
		if (idEnvio == null) idEnvio = "";
		idTipoEnvio = (String) envio.get(EnvEnviosBean.C_IDTIPOENVIOS);
		if (idTipoEnvio == null) idTipoEnvio = "";
		nombreEnvio = (String) envio.get(EnvEnviosBean.C_DESCRIPCION);
		if (nombreEnvio == null) nombreEnvio = "";
	}	

%>	

<html>
	<!-- HEAD -->
	<head>

		<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>

		<script>
			
			function cerrarVentana() {
				var aux = new Array();
				aux[0]="<%=idEnvio%>";
				aux[1]="<%=idTipoEnvio%>";
				aux[2]="<%=nombreEnvio%>";
				top.cierraConParametros(aux);
			}
		</script>

	</head>


<body onLoad="cerrarVentana();" >
</body>

</html>
