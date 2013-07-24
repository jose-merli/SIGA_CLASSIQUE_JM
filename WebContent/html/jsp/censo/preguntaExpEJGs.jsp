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
<%@ page import="com.atos.utils.*"%>

<%@page import="com.siga.expedientes.form.CampoTipoExpedienteForm;"%>

<%
	String app=request.getContextPath();

%>

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	<script type="text/javascript">
		function reloadPage() {
			var type = '<siga:Idioma key="messages.expedientes.tipoExpedientes.errorExpEJGs"/>';
			if (confirm(type)) {
			  parent.comprobarEJG();
			}
		}
	</script>
</head>

<body onload="reloadPage();">

		<html:form action="/EXP_TiposExpedientes_Campos.do" method="POST" >
		    <html:hidden property = "modo" value = ""/>
			
		 </html:form> 	

</body>
</html>