<!DOCTYPE html>
<html>
<head>
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
	String aviso = (String)request.getAttribute("aviso");
	String rutaFichero = (String)request.getAttribute("rutaFichero");
	String borrarFichero = (String)request.getAttribute("borrarFichero");
	String borrarDirectorio = (String)request.getAttribute("borrarDirectorio");
	String nombreFichero = (String)request.getAttribute("nombreFichero");
%>	
<%@page import="org.apache.struts.action.ActionMapping"%>


<!-- HEAD -->


	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	<script>
	function init(){
		<%if  (generacionOK!=null && generacionOK.equalsIgnoreCase("OK")) { %>
				
			<% if  (avisoFicherosNoGenerado!=null && !avisoFicherosNoGenerado.equalsIgnoreCase("")){ %>
				alert('<siga:Idioma key="messages.informes.listaErrorGeneracion"/>'+'\n\n\n'+'<%=avisoFicherosNoGenerado.trim()%>');
			<%}else if  (aviso!=null && !aviso.equalsIgnoreCase("")){%>
				alert('<siga:Idioma key="<%=aviso.trim()%>"/>');
			<%}%>
			document.forms[0].submit();
		<%}else{%>
			<%if  (avisoFicherosNoGenerado!=null && !avisoFicherosNoGenerado.equalsIgnoreCase("")){ %>
				alert('<siga:Idioma key="messages.informes.listaErrorGeneracion"/>'+'\n\n\n'+'<%=avisoFicherosNoGenerado.trim()%>');
			<%}else if  (aviso!=null && !aviso.equalsIgnoreCase("")){%>
				alert('<siga:Idioma key="<%=aviso.trim()%>"/>');
			<%}else{%>
	 			alert('<siga:Idioma key="messages.informes.errorGeneracion"/>');
			<%}
		}%>		
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
	<html:hidden name="<%=formulario%>" property = "nombreFichero" value = "<%=nombreFichero%>"/>
	<%if(borrarFichero!=null){%>
	<html:hidden property = "borrarFichero" value = "<%=borrarFichero%>"/>
	<%}%>
	<%if(borrarDirectorio!=null){%>
	<html:hidden property = "borrarDirectorio" value = "<%=borrarDirectorio%>"/>
	<%}%>
	</html:form>
	
<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las p�ginas-->
	<iframe name="submitArea22" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

<script>
	fin(window.parent.parent.document);
</script>
</body>
</html>