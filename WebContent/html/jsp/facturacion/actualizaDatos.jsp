<!-- actualizaDatos.jsp -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>

<%@ page import="com.siga.administracion.SIGAConstants"%>

<html>
<head>
<%

	String app=request.getContextPath();
	HttpSession ses=request.getSession();

	String generarPDF = (String)request.getAttribute("generarPDF");	
	if (generarPDF==null) generarPDF="";
	String envioFactura = (String)request.getAttribute("envioFactura");	
	if (envioFactura==null) envioFactura="";
%>

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>
	<script language="JavaScript">
	
	function reloadPage() {	
	
		var generarPDF2='<%=generarPDF%>'; 
		var envioFactura2='<%=envioFactura%>'; 
		if (generarPDF2!='' && generarPDF2=='1') {
			parent.document.forms[0].generarPDF.checked=true;
		} else {
			parent.document.forms[0].generarPDF.checked=false;
		}
		if (envioFactura2!='' && envioFactura2=='1') {
			parent.document.forms[0].enviarFacturas.checked=true;
			parent.document.forms[0].generarPDF.disabled=true;
			parent.document.forms[0].generarPDF.checked=true;
		} else {
			parent.document.forms[0].enviarFacturas.checked=false;
			parent.document.forms[0].generarPDF.disabled=false;
		}

	}

</script>
</head>
<body onload="reloadPage();">
</body>
</html>
