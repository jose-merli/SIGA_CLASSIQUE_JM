<!-- mensajeInsertarPrecioPorDefecto.jsp -->

<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "libreria_SIGA.tld" prefix="siga"%>

<%@ page import="com.siga.administracion.SIGAConstants"%>

<html:html>
<head>
<%
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	
	// ATRIBUTOS
	// MENSAJE = mensaje a mostrar (si no hay mensaje no muestra alert)  
	String mensaje = (String)request.getAttribute("mensajeComprobacionPorDefecto");
	
%>
	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>
	<script type="text/jscript" language="JavaScript1.2">
		function reloadPage() {
			<% if (mensaje!=null){%>
					if(confirm("<siga:Idioma key="productos.mantenimientoServicios.mensaje.exitePrecioDefecto" arg0="<%=mensaje%>" />")) {
						parent.document.forms[0].modo.value="insertarCriterio";
				  		parent.document.forms[0].submit();
					}
			<% } %>
		} 
	</script>
</head>
<body onload="reloadPage();">

</body>
</html:html>


