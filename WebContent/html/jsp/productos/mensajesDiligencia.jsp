<!-- mensajesDiligencia.jsp -->
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
	
	// ATRIBUTOS
	String idInstitucion=(String)request.getAttribute("idInstitucion");
	String idInstitucionX=(String)request.getAttribute("idInstitucionX");
	String idPersonaX=(String)request.getAttribute("idPersonaX");
	String idBoton=(String)request.getAttribute("idBoton");

	
	
%>

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>
	<script type="text/jscript">
	function reloadPage() { 
		document.DummyForm.modo.value="abrirDiligencia";
		var ret = ventaModalGeneral("DummyForm","P");
		if (ret!=undefined && ret[0]!=undefined) {
			sub();
			if (ret[0]!=undefined) {
				document.DummyForm.idInstitucionOrigen.value=ret[0];
			}
			if (ret[1]!=undefined) {
				document.DummyForm.idInstitucionDestino.value=ret[1];
			}
			if (ret[2]!=undefined) {
				document.DummyForm.descripcion.value=ret[2];
			}
			document.DummyForm.modo.value="insertarDiligencia";
			document.DummyForm.target="submitArea";
			document.DummyForm.submit();
		}
		
	}
	</script>
</head>

<body onload="reloadPage();">

<html:form action="/PYS_CompraPredefinida.do" method="POST" target="_self">
	<input type="hidden" name="modo" value="abrirDiligencia">
	<input type="hidden" name="actionModal" value="">
	<input type="hidden" name="idInstitucionOrigen" value="">
	<input type="hidden" name="idInstitucionDestino" value="">
	<input type="hidden" name="descripcion" value="">
	<input type="hidden" name="idBoton" value="<%=idBoton %>">
	<input type="hidden" name="idPersona" value="<%=idPersonaX%>">
	<input type="hidden" name="idInstitucion" value="<%=idInstitucionX%>">
</html:form>

</body>
</html:html>