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

<html:html>
<head>
<%
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	
%>

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.js"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.custom.js"></script>
	<script type="text/javascript">
	 function reloadPage() {
	 	
 		var resultado = ventaModalGeneral("solicitudCompraForm","P");					
		if(resultado!= undefined){
			document.forms[0].serieSeleccionada.value = resultado;	
			document.forms[0].modo.value = 'facturacionRapidaCompra';	
		 	// Una vez seleccionado llamamos de nuevo.
			document.forms[0].submit();
		} 
		
	 }
	</script>
</head>

<body onload="reloadPage();">
<html:form action="/PYS_GenerarSolicitudes.do" method="POST" target="submitArea">
	<input type="hidden" name="actionModal" value="">
	<input type="hidden" name="modo" value="seleccionSerie">
	<input type="hidden" name="serieSeleccionada" value=""/>
</html:form>
</body>
</html:html>