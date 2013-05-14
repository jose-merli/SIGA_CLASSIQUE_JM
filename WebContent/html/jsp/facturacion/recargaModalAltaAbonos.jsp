<!-- recargaModalAltaAbonos.jsp -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>

<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.gratuita.form.*"%>
<%@ page import="com.siga.Utilidades.UtilidadesString"%>
<%@ page import="com.atos.utils.GstDate"%>
<%@ page import="com.atos.utils.UsrBean"%>

<%@page import="java.util.Properties"%>
<html:html>
<head>
<%
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);	
	UsrBean user=(UsrBean)request.getSession().getAttribute("USRBEAN");

	// Institucion, fecha e idAbono provisional 

	String idFactura="";
	String fechaFactura="";
	String estado="";
	String idPersona="";
	String nombrePersona="";
	String msgError = (String)request.getAttribute("MSGERROR");
	if (msgError==null) msgError="";
	idFactura=(String)request.getAttribute("IDFACTURA"); // Obtengo el identificador de la institucion
	fechaFactura=GstDate.getFormatedDateShort("",(String)request.getAttribute("FECHAFACTURA")); // Obtengo el identificador del abono provisional
	estado=(String)request.getAttribute("ESTADO"); // Obtengo la fechaActual
	idPersona=(String)request.getAttribute("IDPERSONA"); 
	nombrePersona=(String)request.getAttribute("NOMBREPERSONA"); 
	
	//RGG
	
	if(estado!=null && !estado.trim().equals(""))
		estado = UtilidadesString.getMensajeIdioma(user,estado);
	else
		estado = "";
	
	
%>

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>"/>
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/js/jquery.ui/css/jquery-ui.1.9.2.custom.min.css'/>"/>
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-1.8.3.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-ui-1.9.2.custom.min.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script>
	<script type="text/javascript">
	var idFactura="<%=idFactura%>";
	function reloadPage() {
		if (idFactura!="") {
			parent.document.forms[1].idFactura.value ="<%=idFactura%>";
			parent.document.forms[1].estado.value ="<%=estado%>";
			parent.document.forms[1].idPersonaFactura.value ="<%=idPersona%>";
			parent.document.forms[1].fechaFactura.value ="<%=fechaFactura%>";
			parent.document.forms[1].busquedaCliente.value ="<%=nombrePersona%>";
		} else {
			alert ("<siga:Idioma key='<%=msgError%>'/>");
			parent.document.forms[1].numFactura.value ="";
			parent.document.forms[1].estado.value ="";
			parent.document.forms[1].idPersonaFactura.value ="";
			parent.document.forms[1].fechaFactura.value ="";
			parent.document.forms[1].busquedaCliente.value ="";
		}
						
		return false;
}
 
</script>
</head>
<body onload="reloadPage()"></body>
</html:html>
