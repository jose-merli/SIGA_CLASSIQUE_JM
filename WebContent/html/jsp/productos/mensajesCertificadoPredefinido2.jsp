<!-- mensajesCertificadoPredefinido2.jsp -->
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
	String metodoSolicitud=(String)request.getAttribute("metodoSolicitud");
	String fechaSolicitud=(String)request.getAttribute("fechaSolicitud");
	
	
%>

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	<script type="text/javascript">
	function reloadPage() {
		var ret = ventaModalGeneral("DummyForm","P");
		if (ret!=undefined && ret[0]!=undefined) {
			//document.DummyForm.idPlantilla.value=ret[0];
			if (ret[0]!=undefined) {
				document.DummyForm.idInstitucionPresentador.value=ret[0];
			}
			if (ret[1]!=undefined) {
				document.DummyForm.idProductoCertificado.value=ret[1];
			}
			if (ret[2]!=undefined) {
				document.DummyForm.metodoSolicitud.value=ret[2];
			}
			if (ret[3]!=undefined) {
				document.DummyForm.fechaSolicitud.value=ret[3];
			}
			if (ret[4]!=undefined) {
				document.DummyForm.idInstitucionColegiacion.value=ret[4];
			}			
			document.DummyForm.modo.value="modificar";
			document.DummyForm.submit();
		}
	}
	</script>
</head>

<body onload="reloadPage();">

<html:form action="/PYS_CompraPredefinida.do" method="POST" target="_self">
	<input type="hidden" name="modo" value="ver">
	<input type="hidden" name="actionModal" value="">
	<input type="hidden" name="idPlantilla" value="">
	<input type="hidden" name="idInstitucionPresentador" value="">
	<input type="hidden" name="idInstitucionColegiacion" value="">	
	<input type="hidden" name="idProductoCertificado" value="">
	<input type="hidden" name="idBoton" value="<%=idBoton %>">
	<input type="hidden" name="idPersona" value="<%=idPersonaX%>">
	<input type="hidden" name="idInstitucion" value="<%=idInstitucionX%>">
	<!-- Añadidos para el cambio 6361,6362 -->
	<input type="hidden" name="fechaSolicitud" value="<%=fechaSolicitud%>">
	<input type="hidden" name="metodoSolicitud" value="<%=metodoSolicitud%>">
</html:form>

</body>
</html:html>