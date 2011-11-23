<!-- exitoInsercionNoColegiadoArt27Censo.jsp -->
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
<%@ page import="com.siga.Utilidades.UtilidadesString"%>


<html:html>
<head>
<%
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	
	// ATRIBUTOS
	String idPersona = (String)request.getAttribute("idPersona");
	String nColegiado = (String)request.getAttribute("nColegiado");
	String nombre=(String)request.getAttribute("nombre");
	String apellido1=(String)request.getAttribute("apellido1");
	String apellido2=(String)request.getAttribute("apellido2");
	
%>

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>
	<script type="text/jscript" language="JavaScript1.2">
	function reloadPage() {
		var aux = new Array();
			aux[0]="<%=idPersona %>";
			aux[1]="<%=nColegiado%>";
			aux[2]="<%=UtilidadesString.cambiarDoblesComillas(nombre) %>";
			aux[3]="<%=UtilidadesString.cambiarDoblesComillas(apellido1) %>";
			aux[4]="<%=UtilidadesString.cambiarDoblesComillas(apellido2) %>";	
				
			top.cierraConParametros(aux);
				
	}
	</script>
</head>

<body onload="reloadPage();">


</body>
</html:html>