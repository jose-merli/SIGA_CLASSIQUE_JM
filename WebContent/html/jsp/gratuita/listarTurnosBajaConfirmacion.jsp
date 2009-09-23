
<!-- listarTurnosConfirmacion.jsp -->


<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri="struts-bean.tld"   prefix="bean"%>
<%@ taglib uri="struts-html.tld"   prefix="html"%>
<%@ taglib uri="struts-logic.tld"  prefix="logic"%>

<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.gratuita.form.BajaTurnosForm"%>

<html:html>
<head>
<%
	String app=request.getContextPath();
	
	String mensajeConfirmacion = (String) request.getAttribute("mensajeConfirmacion");
	if (mensajeConfirmacion == null) {
		mensajeConfirmacion = new String("");
	}
%>

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>
	<script type="text/jscript" language="JavaScript1.2">

	</script>
</head>

<body onload="parent.preguntaConfirmacion('<%=mensajeConfirmacion%>');">
</body>

</html:html>