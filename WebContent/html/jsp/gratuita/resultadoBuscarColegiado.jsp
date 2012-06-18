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
<%@ page import="java.util.Hashtable" %>

<html:html>
<head>
<%
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	
	// ATRIBUTOS
	// MENSAJE = mensaje a mostrar (si no hay mensaje no muestra alert)  
	Hashtable res = (request.getAttribute("datosCliente")!=null)?(Hashtable) request.getAttribute("datosCliente"):null;
	
%>

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp"/>
	<link rel="stylesheet" href="<%=app%>/html/js/themes/base/jquery.ui.all.css"/>
		
	
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.js"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.custom.js"></script>
	<script type="text/javascript">
	 function reloadPage() {
	 
		<%  if (res==null){%>
			var type = '<siga:Idioma key="messages.validarVolantesGuardias.noencuentracolegiado"/>';
			alert(type);
		<%  } %>
			
		parent.document.forms[0].target="resultadoModal";
		parent.document.forms[0].idPersona.value="<%=(res!=null)?(String)res.get("IDPERSONA"):""%>";
		parent.document.forms[0].nomColegiado.value="<%=(res!=null)?(String)res.get("NOMCOLEGIADO"):""%>";
		parent.document.forms[0].numColegiado.value="<%=(res!=null)?(String)res.get("NCOLEGIADO"):""%>";
		

	 }

	</script>
</head>

<body onload="reloadPage();">
</body>
</html:html>