<!DOCTYPE html>
<html>
<head>
<!-- buscarJuzgadoOculta.jsp -->

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
<%@ page import="com.siga.gratuita.form.AsistenciasForm"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.siga.tlds.FilaExtElement"%>

<!-- JSP -->
<%
	String app = request.getContextPath();
	HttpSession ses = request.getSession(true);
	UsrBean usr = (UsrBean) ses.getAttribute("USRBEAN");
	
	Vector obj = (Vector) request.getAttribute("resultadoJuzgado");

	String nombreObjetoDestino = (String) request.getAttribute("nombreObjetoDestino");

	ScsJuzgadoBean myBean = null;

	if (obj != null && obj.size() > 0) {
		myBean = (ScsJuzgadoBean) obj.elementAt(0);
	}
%>


<!-- HEAD -->

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	<script language="JavaScript" type="text/javascript">	
	
	var aux = new Array();
<%	if (myBean!=null)
	{ %>	   
		aux[0]="<%=myBean.getIdJuzgado()%>"+","+"<%=myBean.getIdInstitucion()%>";
<%		if (nombreObjetoDestino != null && !nombreObjetoDestino.equals("")) 
		{ %>
         	aux[1]="<%=nombreObjetoDestino%>";
<%		} %>
<%	} %>
	if (top.MantenimientoJuzgadoForm!=undefined) {
		top.traspasoDatos(aux);
	} else if (window.parent.MantenimientoJuzgadoForm!=undefined) {
		window.parent.traspasoDatos(aux);
	} else if (window.parent.parent.MantenimientoJuzgadoForm!=undefined) {
		window.parent.parent.traspasoDatos(aux);
	} else if (window.parent.parent.parent.MantenimientoJuzgadoForm!=undefined) {
		window.parent.parent.parent.traspasoDatos(aux);
	}
	</script>
</head>

<body>

<!-- INICIO: SUBMIT AREA -->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display: none"></iframe>
<!-- FIN: SUBMIT AREA -->
</body>
</html>