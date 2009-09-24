<!-- genericLoadFormTablasMaestras.jsp -->
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
%>

<html>
	<head>
		<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
		
		<script type="text/jscript" language="JavaScript1.2">
			function reloadPage()
			{
				var type = '<siga:Idioma key="<%=messageName%>"/>';
				alert(type);   
				var tab ='<%=app%>/listadoTablaMaestra.do?tableName=<%=(String) request.getAttribute("table")%>&mode=listing';
				parent.parent.frames['registros'].location=tab;
				parent.parent.frames['editar'].location='<%=app%>/html/jsp/general/blank.jsp';
				return false;
			}
		</script>
	</head>
	
	<body onload="reloadPage()"></body>
</html>