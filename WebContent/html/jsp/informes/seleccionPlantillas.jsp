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
	
%>

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>
	<script type="text/jscript" language="JavaScript1.2">
	
	  function reloadPage() {
 		var arrayResultado = ventaModalGeneral("InformesGenericosForm","P");	
 		if(arrayResultado && arrayResultado.length=='0'){
 			alert('<siga:Idioma key="informes.message.plantilla.sinseleccion"/>');
			return false;
 		}				
		else if(arrayResultado){
			
			sub(window.parent.document);
		
			document.forms[0].idInforme.value = arrayResultado[0];	
			document.forms[0].idTipoEnvio.value = arrayResultado[1];
		 	// Una vez seleccionados llamamos de nuevo.
		 	document.forms[0].seleccionados.value="1";
		 	//document.forms[0].submit();
		 	
		 	//document.forms[0].submit();
		 	var f = document.forms[0].name;
		 	document.forms[0].submit();
			return false;
		}
				
	 }
	</script>
</head>

<body onload="reloadPage();">
<html:form action="/INF_InformesGenericos.do" method="POST" target="submitArea">
	<input type="hidden" name="actionModal" value="">
	<html:hidden property="idInstitucion"/>
	<html:hidden property="idInforme"/> 				
	<html:hidden property="idTipoInforme"/>	
	<html:hidden property="datosInforme"/>
	<html:hidden property="enviar"/>
	<html:hidden property="descargar"/>
	<html:hidden property="tipoPersonas"/>
	<html:hidden property="clavesIteracion"/>	
	<html:hidden property="seleccionados" value="3"/>
	<html:hidden property="destinatarios"/>	
	<html:hidden property="idTipoEnvio"/>
	
</html:form>

<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
</body>
</html:html>