<!DOCTYPE html>
<html>
<head>
<!-- recargarPantallaPrincipal.jsp -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>
<%@ taglib uri = "libreria_SIGA.tld" prefix="siga"%>

<%@ page import="com.siga.administracion.SIGAConstants"%>

	
	<%
		String app=request.getContextPath();
		String messageName = (String)request.getAttribute("descOperation");
		String menuPos=(String)request.getSession().getAttribute(SIGAConstants.MENU_POSITION_REF);
		String action=app+"/openMainFrameset.do";
		if(menuPos.equals(SIGAConstants.MENU_LEFT)) {
			action=app+"/openMainFrameset2.do";
		}
	%>
	<script type="text/javascript">
	function reloadPage() {
		<% if (messageName!= null && !messageName.equals("")) { %>
					var type = '<siga:Idioma key="<%=messageName%>"/>';
					alert(type);
		<% } %>

		var tab ='<%=action%>';
		top.location=tab;
		return false;
	}
	</script>
	</head>
<body onload="reloadPage()"></body>
</html>
