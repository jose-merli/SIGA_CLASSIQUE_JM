<!-- imprimir.jsp -->
<%-- 
	 VERSIONES:
	 david.sanchezp_ creado el  30-05-2005.
--%>

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>

<!-- IMPORTS -->

<!-- JSP -->
<%  
	String app=request.getContextPath();
	String generacionOK = (String)request.getAttribute("generacionOK");
	String avisoFicherosNoGenerado = (String)request.getAttribute("avisoFicherosNoGenerado");
	String rutaFichero = (String)request.getAttribute("rutaFichero");
	String borrarFichero = (String)request.getAttribute("borrarFichero");
	String borrarDirectorio = (String)request.getAttribute("borrarDirectorio");
	
%>	
<%@page import="org.apache.struts.action.ActionMapping"%>
<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp"/>
	<link rel="stylesheet" href="<%=app%>/html/js/themes/base/jquery.ui.all.css"/>
		
	
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.js"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.custom.js"></script>
	<script>
	function init(){
	<% if  (generacionOK!=null && generacionOK.equalsIgnoreCase("OK")) { %>
		//alert();
		
		<% if  (avisoFicherosNoGenerado!=null && !avisoFicherosNoGenerado.equalsIgnoreCase("")){ %>
			alert('<siga:Idioma key="messages.informes.listaErrorGeneracion"/>'+'\n\n\n'+'<%=avisoFicherosNoGenerado.trim()%>');
		<%}%>
		
		// document.forms[0].modo.value="descargar";
		document.forms[0].submit();
	<% } else { %>
	 	alert('<siga:Idioma key="messages.informes.errorGeneracion"/>');
	<% } %>		
	}
	</script>

</head>


<body onload="init();">
	<% 
	ActionMapping actionMapping = (ActionMapping)request.getAttribute("org.apache.struts.action.mapping.instance");
	String path = actionMapping.getPath();
	String formulario = actionMapping.getName();
	%>
	<html:form action="<%=path%>" method="POST" target="submitArea22">
	<html:hidden name="<%=formulario%>" property = "modo" value = "descargaFicheroGlobal"/>
	<html:hidden name="<%=formulario%>" property = "rutaFichero" value = "<%=rutaFichero%>"/>
	<%if(borrarFichero!=null){%>
	<html:hidden property = "borrarFichero" value = "<%=borrarFichero%>"/>
	<%}%>
	<%if(borrarDirectorio!=null){%>
	<html:hidden property = "borrarDirectorio" value = "<%=borrarDirectorio%>"/>
	<%}%>
	</html:form>
	
<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea22" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

<script>
	fin(window.parent.parent.document);
</script>
</body>
</html>