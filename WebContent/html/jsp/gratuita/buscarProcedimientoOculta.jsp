<!DOCTYPE html>
<html>
<head>
<!-- buscarProcediemientoOculta.jsp -->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
<%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga" %>
<%@ taglib uri="struts-bean.tld"   prefix="bean" %>
<%@ taglib uri="struts-html.tld"   prefix="html" %>
<%@ taglib uri="struts-logic.tld"  prefix="logic"%>

<!-- IMPORTS -->
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.siga.Utilidades.*"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.siga.tlds.FilaExtElement"%>

<!-- JSP -->
<%
	String app = request.getContextPath();
	HttpSession ses = request.getSession(true);
	Vector obj = (Vector) request.getAttribute("resultadoProcedimiento");

	Hashtable registro = null;
	if (obj != null && obj.size() > 0) {
		registro = (Hashtable) obj.get(0);
	}
%>


<!-- HEAD -->

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	<script language="JavaScript" type="text/javascript">	
	
	var aux = new Array();
	<% if (registro!=null) { %>	   
		aux[0]="<%=registro.get("IDPROCEDIMIENTO")%>"+","+"<%=registro.get("IDINSTITUCION")%>";
	<% } %>
	
	if (top.MantenimientoProcedimientosForm!=undefined) {
		top.traspasoProcDatos(aux);
	} else if (window.parent.MantenimientoProcedimientosForm!=undefined) {
		window.parent.traspasoProcDatos(aux);
	} else if (window.parent.parent.MantenimientoProcedimientosForm!=undefined) {
		window.parent.parent.traspasoProcDatos(aux);
	} else if (window.parent.parent.parent.MantenimientoProcedimientosForm!=undefined) {
		window.parent.parent.parent.traspasoProcDatos(aux);
	}
	</script>
</head>

<body>

<!-- INICIO: SUBMIT AREA -->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display: none"></iframe>
<!-- FIN: SUBMIT AREA -->
</body>
</html>