<!-- seleccionListaCorreoModal.jsp -->
<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>
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

		<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
		
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
