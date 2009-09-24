<!-- exitoInsertarPago.jsp -->
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

<html>
<head>
<%
	String app=request.getContextPath(); 
	HttpSession ses=request.getSession();
	
	
	// ATRIBUTOS
	// MENSAJE = mensaje a mostrar (si no hay mensaje no muestra alert)  
	String mensaje = (String)request.getAttribute("mensaje");
	String idPagosJG = (String)request.getAttribute("idPagosJG");
	String idInstitucion = (String)request.getAttribute("idInstitucion");
	String modo = (String)request.getAttribute("modo");
	String cerrarModal = request.getAttribute("cerrarModal")==null?"NO":"SI";
	if (mensaje==null) mensaje = "";
%>

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>

	<script>
	function reloadPage() {
		<%  if (mensaje!=null){%>
			var type = '<siga:Idioma key="<%=mensaje%>"/>';
			alert(type);
		<%  } %>
		
		<% if (cerrarModal.equals("SI")) { %>
			window.close();
		<% } %>
		document.mantenimientoPagoForm.submit();
	}
	</script>

</head>

<body onload="reloadPage();">
	<html:form action="/CEN_MantenimientoPago.do?noReset=true" method="POST" target="mainPestanas">
			<html:hidden name="mantenimientoPagoForm" property="modo" value="<%=modo%>" />
			<html:hidden name="mantenimientoPagoForm" property="idInstitucion" value="<%=idInstitucion%>" />
			<html:hidden name="mantenimientoPagoForm" property="idPagosJG" value="<%=idPagosJG%>" />
	</html:form>
</body>
</html>