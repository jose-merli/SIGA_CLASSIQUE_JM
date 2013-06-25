<!-- preguntaNumeroFacturas.jsp -->
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
	// MENSAJE = mensaje a mostrar (si no hay mensaje no muestra alert)  
	String mensaje = (String)request.getAttribute("mensaje");
	Integer numero = (Integer)request.getAttribute("numeroFacturas");
	
%>

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	<script type="text/javascript">
	function reloadPage() {	 
		<% 
		 if (mensaje!=null && numero!=null) {
		%>			
		    var type = '<siga:Idioma key="<%=mensaje%>" arg0="<%=numero.toString()%>" />';
			if(confirm(type)) {
				subicono(parent.iconoinhabilidado);
				parent.document.forms[0].validado.value="1";
				parent.document.forms[0].submit();
				window.setTimeout("fin()",5000,"Javascript");
			}
		<% 
		 }
		%>
	}
	</script>
</head>

<body onload="reloadPage();">
</body>
</html:html>