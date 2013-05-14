<!-- seleccionListaCorreoModal.jsp -->
<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "libreria_SIGA.tld" prefix="siga"%>

<!-- JSP -->
<% 
	String app=request.getContextPath();
	
	String idListaCorreo = "";
	idListaCorreo = (String) request.getAttribute("idListaCorreo");
%>	

<html>
	<!-- HEAD -->
	<head>

		<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>"/>
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/js/jquery.ui/css/jquery-ui.1.9.2.custom.min.css'/>"/>
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-1.8.3.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-ui-1.9.2.custom.min.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script>
		
		<script language="JavaScript">
			function cerrarVentana() {
				var aux = "<%=idListaCorreo %>";				
				top.cierraConParametros(aux);
			}
		</script>

	</head>


<body onLoad="cerrarVentana()" >
</body>

</html>
