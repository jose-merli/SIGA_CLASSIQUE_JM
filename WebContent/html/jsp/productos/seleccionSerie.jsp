<!DOCTYPE html>
<html>
<head>
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

<%
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	String idPeticion=(String)request.getAttribute("idPeticionSeleccion");
%>

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	<script type="text/javascript">
	 function reloadPage() {
 		var resultado = ventaModalGeneral("solicitudCompraForm","P");
 		
 		sub(parent.document);
		if(resultado!= undefined){
			
			document.forms[0].serieSeleccionada.value = resultado;	
			document.forms[0].modo.value="facturacionRapidaCompra";
			document.forms[0].submit();

		}else{
			fin(parent.document);
			//parent.activarBotones();
		} 
	 }
	</script>
</head>

<body onload="reloadPage();">
<html:form action="/PYS_GenerarSolicitudes.do" method="POST" target="submitArea">
	<input type="hidden" name="actionModal" value="">
	<html:hidden property="modo" value="seleccionSerie"/>
	<html:hidden property="serieSeleccionada" value=""/>
	<html:hidden property="idInstitucion"/>
	<html:hidden property="idPeticion" value="<%=idPeticion%>"/> 				
</html:form>
</body>
</html>