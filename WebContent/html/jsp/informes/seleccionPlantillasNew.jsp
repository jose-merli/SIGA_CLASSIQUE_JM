<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>

<html:html>
<head>
<%
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	String idInstitucion = (String) request.getAttribute("IdInstitucion");
	String idInforme = (String) request.getAttribute("idInforme");
	String idTipoInforme = (String) request.getAttribute("idTipoInforme");
	String datosInforme = (String) request.getAttribute("datosInforme");
	String enviar = (String) request.getAttribute("enviar");
	String descargar = (String) request.getAttribute("descargar");
	String tipoPersonas = (String) request.getAttribute("tipoPersonas");
	String clavesIteracion = (String) request.getAttribute("clavesIteracion");
	String action = (String) request.getAttribute("action");
	String accion = (String) request.getAttribute("accion");
	String form = (String) request.getAttribute("form");
%>

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>
	<script type="text/jscript" language="JavaScript1.2">
	
	  function reloadPage() {
 		var resultado = ventaModalGeneral("<%=form%>","P");	
 		if(resultado==''){
 			alert('<siga:Idioma key="informes.message.plantilla.sinseleccion"/>');
			return false;
 		}				
		else if(resultado!= undefined){
		
			sub(window.parent.document);
		
			document.forms[0].idInforme.value = resultado;	
		 	// Una vez seleccionados llamamos de nuevo.
		 	document.forms[0].seleccionados.value="1";
		 	document.forms[0].submit();
			return false;
		}
				
	 }
	</script>
</head>

<body onload="reloadPage();">
<html:form action="<%=action%>" method="POST" target="submitArea">
	<input type="hidden" name="actionModal" value="">
	<input type="hidden" name="accion" value="<%=accion%>">
	<input type="hidden" name="idInstitucion" value="<%=idInstitucion%>">
	<input type="hidden" name="idInforme" value="<%=idInforme%>">
	<input type="hidden" name="idTipoInforme" value="<%=idTipoInforme%>">
	<input type="hidden" name="datosInforme" value="<%=datosInforme%>">
	<input type="hidden" name="enviar" value="<%=enviar%>">
	<input type="hidden" name="descargar" value="<%=descargar%>">
	<input type="hidden" name="tipoPersonas" value="<%=tipoPersonas%>">
	<input type="hidden" name="clavesIteracion" value="<%=clavesIteracion%>">
	<input type="hidden" name="seleccionados" value="3">
</html:form>

<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
</body>
</html:html>