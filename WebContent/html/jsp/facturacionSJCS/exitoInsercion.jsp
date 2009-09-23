<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
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
	String idFacturacion = (String)request.getAttribute("idFacturacion");
	String idInstitucion = (String)request.getAttribute("idInstitucion");
//	if (mensaje==null) mensaje = "";
%>

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>

	<script>
	function reloadPage() {
		<%  if (mensaje!=null){%>
			var type = '<siga:Idioma key="<%=mensaje%>"/>';
//			alert(type);
		<%  } %>
		document.mantenimientoPrevisionesForm.submit();
	}
	</script>

</head>

<body onload="reloadPage();">
		<html:form action="/FCS_MantenimientoPrevisiones.do" method="POST" target="mainWorkArea">
		<html:hidden property="modo" value="editar"/>
		<html:hidden property ="idFacturacion" value = "<%=idFacturacion%>"/>
		<html:hidden property ="idInstitucion" value = "<%=idInstitucion%>"/>
		</html:form>
</body>
</html>