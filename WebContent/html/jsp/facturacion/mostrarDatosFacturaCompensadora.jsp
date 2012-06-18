<!-- mostrarDatosFacturaCompensadora.jsp -->
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
<%@ page import="com.siga.Utilidades.UtilidadesString"%>
<%@ page import="com.atos.utils.GstDate"%>
<%@ page import="com.atos.utils.UsrBean"%>

<%@page import="java.util.Properties"%>
<html:html>
<head>
<%
	String app=request.getContextPath();
	
		
	UsrBean user=(UsrBean)request.getSession().getAttribute("USRBEAN");

	// Institucion, fecha e idAbono provisional 

	String idFactura="";
	String fechaFactura="";
	String estado="";
	//String idPersona="";
	String importeFactura="";
	String msgError = (String)request.getAttribute("MSGERROR");
	idFactura=(String)request.getAttribute("IDFACTURA"); // Obtengo el identificador de la institucion
	fechaFactura=GstDate.getFormatedDateShort("",(String)request.getAttribute("FECHAFACTURA")); // Obtengo el identificador del abono provisional
	estado=(String)request.getAttribute("ESTADO"); // Obtengo la fechaActual
	//idPersona=(String)request.getAttribute("IDPERSONA"); 
	importeFactura=(String)request.getAttribute("IMPORTEFACTURA"); 
	//RGG
	if(estado!=null && !estado.trim().equals(""))
		estado = UtilidadesString.getMensajeIdioma(user,estado);
	else
		estado = "";
	
	
	
%>

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.js"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.custom.js"></script>
	<script type="text/javascript">
	var idFactura="<%=idFactura%>";
	function reloadPage() {
		if (idFactura!="") {
			parent.document.AbonosPagosForm.idFacturaCompensadora.value ="<%=idFactura%>";
			parent.document.AbonosPagosForm.estadoFacturaCompensadora.value ="<%=estado%>";
			parent.document.AbonosPagosForm.fechaFacturaCompensadora.value ="<%=fechaFactura%>";
			parent.document.AbonosPagosForm.importeFacturaCompensadora.value ="<%=importeFactura%>";
			
		} else {
			
			alert ("<siga:Idioma key='<%=msgError%>'/>");
			parent.document.AbonosPagosForm.idFacturaCompensadora.value ="";
			parent.document.AbonosPagosForm.numFacturaCompensadora.value ="";
			parent.document.AbonosPagosForm.estadoFacturaCompensadora.value ="";
			parent.document.AbonosPagosForm.fechaFacturaCompensadora.value ="";
			parent.document.AbonosPagosForm.importeFacturaCompensadora.value ="";
			
		}
						
		return false;
}
 
</script>
</head>
<body onload="reloadPage()"></body>
</html:html>
